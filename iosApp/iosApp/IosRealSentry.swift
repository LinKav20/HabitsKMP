import WbProShared
import Sentry

class IosRealSentry: SentryRealSentry {
    func trackMessage(message: String) {
        SentrySDK.capture(message: message)
    }
    
    func trackThrowable(throwable: KotlinThrowable) {
        let nsError = throwable.asError() as NSError
        
        SentrySDK.capture(error: nsError)
    }
}
