import UIKit
import SwiftUI
import WbProShared

class OrientationManager: ObservableObject {
    static let shared = OrientationManager()
    
    @Published var isLandscapeLocked = false
    
    private init() {
        // Register callbacks with Kotlin
        InitDIKt.PlayerOrientationBridge.setCallbacks(
            lockToLandscape: { [weak self] in
                self?.lockToLandscape()
            },
            unlockOrientation: { [weak self] in
                self?.unlockOrientation()
            }
        )
    }
    
    func lockToLandscape() {
        DispatchQueue.main.async {
            self.isLandscapeLocked = true
            self.forceOrientation(.landscapeRight)
        }
    }
    
    func unlockOrientation() {
        DispatchQueue.main.async {
            self.isLandscapeLocked = false
            self.forceOrientationToCurrentDeviceOrientation()
        }
    }

    private func forceOrientationToCurrentDeviceOrientation() {
        let deviceOrientation = UIDevice.current.orientation
        
        var interfaceOrientation: UIInterfaceOrientation
        
        switch deviceOrientation {
        case .landscapeLeft:
            interfaceOrientation = .landscapeRight 
        case .landscapeRight:
            interfaceOrientation = .landscapeLeft
        case .portrait:
            interfaceOrientation = .portrait
        case .portraitUpsideDown:
            interfaceOrientation = .portraitUpsideDown
        default:
            interfaceOrientation = .portrait
        }
        
        forceOrientation(interfaceOrientation)
    }
    
    private func forceOrientation(_ orientation: UIInterfaceOrientation) {
        if #available(iOS 16.0, *) {
            guard let windowScene = UIApplication.shared.connectedScenes
                .first(where: { $0.activationState == .foregroundActive }) as? UIWindowScene else {
                return
            }
            
            let orientationMask: UIInterfaceOrientationMask = isLandscapeLocked ? .landscape : .all
            
            windowScene.requestGeometryUpdate(.iOS(interfaceOrientations: orientationMask)) { error in
                print("Orientation change error: \(error)")
            }
        } else {
            // For iOS 15 and below
            UIDevice.current.setValue(orientation.rawValue, forKey: "orientation")
        }
    }
    
    func supportedOrientations() -> UIInterfaceOrientationMask {
        return isLandscapeLocked ? .landscape : .all
    }
}
