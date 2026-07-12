import UIKit
import UserNotifications
import FirebaseMessaging
import WbProShared

class PushServiceImpl: NSObject, UNUserNotificationCenterDelegate {
    static let shared = PushServiceImpl()

    private let firebaseMessageDelegate: FirebaseMessageDelegate
    private var currentToken: String = ""

    override init() {
        self.firebaseMessageDelegate = FirebaseMessageDelegate()
        super.init()
        
        InitDIKt.PushFirebaseBridge.setCallbacks(
            getToken: { [weak self] in
                var result = ""
                let semaphore = DispatchSemaphore(value: 0)
                
                self?.getTokenWithCompletion { token in
                    result = token
                    semaphore.signal()
                }
                
                semaphore.wait()
                return result
            },
            deleteMyToken: { [weak self] in
                let semaphore = DispatchSemaphore(value: 0)
                
                self?.deleteMyTokenWithCompletion { _ in
                    semaphore.signal()
                }
                
                semaphore.wait()
            },
            subscribeToTopic: { [weak self] (topic: String) in
                let semaphore = DispatchSemaphore(value: 0)
                
                self?.subscribeToTopicWithCompletion(topic: topic) { _ in
                    semaphore.signal()
                }
                
                semaphore.wait()
            },
            unSubscribeFromTopic: { [weak self] (topic: String) in
                let semaphore = DispatchSemaphore(value: 0)
                
                self?.unSubscribeFromTopicWithCompletion(topic: topic) { _ in
                    semaphore.signal()
                }
                
                semaphore.wait()
            }
        )

        self.firebaseMessageDelegate.onNewToken = { [weak self] token in
            InitDIKt.PushFirebaseBridge.onNewTokenReceived(token: token)
            self?.currentToken = token
        }

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self.firebaseMessageDelegate

        requestAuthorizationForPushes()
    }

    private func requestAuthorizationForPushes() {
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            if let error = error {
                print("PUSH: Authorization request error: \(error.localizedDescription)")
                return
            }

            guard granted else { return }

            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
            }
        }
    }
    func getToken() async -> String {
        await withCheckedContinuation { continuation in
            getTokenWithCompletion { token in
                continuation.resume(returning: token)
            }
        }
    }
    
    func deleteMyToken() async {
        await withCheckedContinuation { continuation in
            deleteMyTokenWithCompletion { _ in
                continuation.resume()
            }
        }
    }

    func subscribeToTopic(topic: String) async {
        await withCheckedContinuation { continuation in
            subscribeToTopicWithCompletion(topic: topic) { _ in
                continuation.resume()
            }
        }
    }
    
    func unSubscribeFromTopic(topic: String) async {
        await withCheckedContinuation { continuation in
            unSubscribeFromTopicWithCompletion(topic: topic) { _ in
                continuation.resume()
            }
        }
    }
   
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound, .badge])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        // TODO: on push tap
        completionHandler()
    }
    
    private func getTokenWithCompletion(completion: @escaping (String) -> Void) {
        Messaging.messaging().token { token, error in
            if let error = error {
                print("PUSH: Error while getting token: \(error.localizedDescription)")
                completion(self.currentToken)
                return
            }

            if let token = token {
                self.currentToken = token
                completion(token)
            } else {
                completion(self.currentToken)
            }
        }
    }
    
    private func deleteMyTokenWithCompletion(completion: @escaping (Error?) -> Void) {
        Messaging.messaging().deleteToken { error in
            if let error = error {
                print("PUSH: Error while deleting token: \(error.localizedDescription)")
            } else {
                self.currentToken = ""
            }
            completion(error)
        }
    }

    private func subscribeToTopicWithCompletion(topic: String, completion: @escaping (Error?) -> Void) {
        Messaging.messaging().subscribe(toTopic: topic) { error in
            if let error = error {
                print("PUSH: Error while subscribing to topic \(topic): \(error.localizedDescription)")
            }
            completion(error)
        }
    }
    
    private func unSubscribeFromTopicWithCompletion(topic: String, completion: @escaping (Error?) -> Void) {
        Messaging.messaging().unsubscribe(fromTopic: topic) { error in
            if let error = error {
                print("PUSH: Error while unsubscribing from topic \(topic): \(error.localizedDescription)")
            }
            completion(error)
        }
    }
}

private class FirebaseMessageDelegate: NSObject, MessagingDelegate {
    var onNewToken: ((String) -> Void)?

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let token = fcmToken {
            onNewToken?(token)
        }
    }
}
