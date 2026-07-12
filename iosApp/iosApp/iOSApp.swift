import SwiftUI
import HabitsShared

@main
struct iOSApp: App {
    let fileDownloader = IOSFileDownloader()
    var rootComponent: ComposeAppRootComponent
    
    init() {
        InitDIKt.InitDI(fileDownloaderDelegate: fileDownloader)
        rootComponent = MainViewControllerKt.CreateRootComponent()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView(rootComponent: self.rootComponent)
        }
    }
}
