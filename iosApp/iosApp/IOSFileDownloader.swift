import Foundation
import HabitsShared

class IOSFileDownloader: NSObject, NetworkFileDownloaderDelegate {
    
    private var activeTasks: [Int: URLSessionDownloadTask] = [:]
    private var taskCallbacks: [Int: TaskCallbacks] = [:]
    private var taskDestinations: [Int: String] = [:]
    private var taskSessions: [Int: URLSession] = [:]
    private let taskQueue = DispatchQueue(label: "file.downloader.queue", attributes: .concurrent)
    
    struct TaskCallbacks {
        let onProgress: (KotlinInt) -> Void
        let onComplete: () -> Void
        let onError: (String) -> Void
    }
    
    func downloadFile(
        urlSession: URLSession,
        url: String,
        destinationPath: String,
        onProgress: @escaping (KotlinInt) -> Void,
        onComplete: @escaping () -> Void,
        onError: @escaping (String) -> Void
    ) -> Any {
        
        guard let downloadURL = URL(string: url) else {
            onError("Invalid URL: \(url)")
            return ""
        }
        
        let request = URLRequest(url: downloadURL)

        let sessionConfig = urlSession.configuration
        let delegatedSession = URLSession(
            configuration: sessionConfig,
            delegate: self,
            delegateQueue: nil
        )
        
        let downloadTask = delegatedSession.downloadTask(with: request)
        let taskId = downloadTask.taskIdentifier
        
        taskQueue.async(flags: .barrier) {
            self.activeTasks[taskId] = downloadTask
            self.taskCallbacks[taskId] = TaskCallbacks(
                onProgress: onProgress,
                onComplete: onComplete,
                onError: onError
            )
            self.taskDestinations[taskId] = destinationPath
            self.taskSessions[taskId] = delegatedSession
        }
        
        downloadTask.resume()
        
        return taskId
    }
    
    func cancelDownload(taskId: Any) {
        guard let taskIdInt = taskId as? Int else { return }
        
        taskQueue.async(flags: .barrier) {
            if let task = self.activeTasks[taskIdInt] {
                task.cancel()
                self.cleanupTaskMaps(id: taskIdInt)
            }
        }
    }
    
    private func findTaskData(for downloadTask: URLSessionDownloadTask) -> (id: Int, callbacks: TaskCallbacks, destination: String?)? {
        let taskId = downloadTask.taskIdentifier
        
        return taskQueue.sync {
            guard let callbacks = taskCallbacks[taskId] else { return nil }
            let destination = taskDestinations[taskId]
            return (taskId, callbacks, destination)
        }
    }
    
    private func cleanupTaskMaps(id: Int) {
        activeTasks.removeValue(forKey: id)
        taskCallbacks.removeValue(forKey: id)
        taskDestinations.removeValue(forKey: id)
        
        if let session = taskSessions.removeValue(forKey: id) {
            session.invalidateAndCancel()
        }
    }
}

extension IOSFileDownloader: URLSessionDownloadDelegate {
    
    func urlSession(
        _ session: URLSession,
        downloadTask: URLSessionDownloadTask,
        didWriteData bytesWritten: Int64,
        totalBytesWritten: Int64,
        totalBytesExpectedToWrite: Int64
    ) {
        guard let taskData = findTaskData(for: downloadTask),
              totalBytesExpectedToWrite > 0 else { return }
        
        let progress = Int((Double(totalBytesWritten) / Double(totalBytesExpectedToWrite)) * 100.0)
        let clampedProgress = max(0, min(99, progress)) // Cap at 99% during download, 100% only on completion
        
        DispatchQueue.main.async {
            taskData.callbacks.onProgress(KotlinInt(integerLiteral: clampedProgress))
        }
    }
    
    func urlSession(
        _ session: URLSession,
        downloadTask: URLSessionDownloadTask,
        didFinishDownloadingTo location: URL
    ) {
        guard let taskData = findTaskData(for: downloadTask),
              let destPath = taskData.destination else { return }
        
        taskQueue.async(flags: .barrier) {
            self.cleanupTaskMaps(id: taskData.id)
        }
        
        let destinationURL = URL(fileURLWithPath: destPath)
        
        do {
            if FileManager.default.fileExists(atPath: destPath) {
                try FileManager.default.removeItem(at: destinationURL)
            }
            
            try FileManager.default.moveItem(at: location, to: destinationURL)
            
            DispatchQueue.main.async {
                taskData.callbacks.onProgress(KotlinInt(integerLiteral: 100))
                taskData.callbacks.onComplete()
            }
            
        } catch {
            DispatchQueue.main.async {
                taskData.callbacks.onError("Failed to move file: \(error.localizedDescription)")
            }
        }
    }
    
    func urlSession(
        _ session: URLSession,
        task: URLSessionTask,
        didCompleteWithError error: Error?
    ) {
        guard let downloadTask = task as? URLSessionDownloadTask,
              let taskData = findTaskData(for: downloadTask),
              let error = error else { return }
        
        taskQueue.async(flags: .barrier) {
            self.cleanupTaskMaps(id: taskData.id)
        }
        
        if (error as NSError).code != NSURLErrorCancelled {
            DispatchQueue.main.async {
                taskData.callbacks.onError("Download failed: \(error.localizedDescription)")
            }
        }
    }
}
