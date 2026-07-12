import UIKit
import SwiftUI
import HabitsShared

struct ComposeView: UIViewControllerRepresentable {
    var rootComponent: ComposeAppRootComponent
    
    func makeUIViewController(context: Context) -> UIViewController {
        let viewController = MainViewControllerKt.MainViewController(rootComponent: self.rootComponent)
        return viewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var rootComponent: ComposeAppRootComponent
    
    var body: some View {
        ComposeView(rootComponent: self.rootComponent)
            .ignoresSafeArea(.all) // Compose has own keyboard handler
    }
}
