-dontwarn androidx.test.platform.app.InstrumentationRegistry
-dontwarn androidx.window.extensions.area.ExtensionWindowAreaPresentation
-dontwarn androidx.window.extensions.**
-keep class android.view.View.** { *; }
-keep class androidx.core.view.ScrollingView.** { *; }
-keep class androidx.recyclerview.widget.RecyclerView { *; }
-keep class androidx.sqlite.** { *; }
-keep class androidx.room.** { *; }

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
