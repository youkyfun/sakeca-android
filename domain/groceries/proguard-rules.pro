# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Dagger generated components
-keep class *.Dagger*Component { *; }
-keep class *.Dagger* { *; } # More general rule for Dagger generated classes

# Keep classes annotated with Dagger annotations (if they are not processed correctly)
-keep @javax.inject.Inject class *
-keep class * { @javax.inject.Inject *; }
-keep class * { @dagger.* *; }
-keep @dagger.Module class *
-keep @dagger.Provides class *

# Keep Groceries classes domain if they are being removed
-keep class com.youkydesign.sakeca.domain.groceries.IGroceriesRepository { *; }
-keep class com.youkydesign.sakeca.domain.groceries.GroceriesInteractor_Factory { *; }
-keep class com.youkydesign.sakeca.domain.groceries.GroceriesUseCase { *; }

