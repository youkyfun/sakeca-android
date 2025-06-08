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
-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses class com.youkydesign.sakeca.core.RecipeSortType { *; }
-keep,includedescriptorclasses class com.youkydesign.sakeca.core.SortUtils { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }

-keep class androidx.constraintlayout.widget.R$* { *; }
-keep public class * extends androidx.constraintlayout.widget.ConstraintLayout { *; }

-keep class com.youkydesign.sakeca.core.di.CoreComponent { *; }
-keep class com.youkydesign.sakeca.core.di.CoreComponent$Factory { *; }
-keep class com.youkydesign.sakeca.core.di.DaggerCoreComponent { *; }
-keep interface com.youkydesign.sakeca.core.di.CoreDependenciesProvider { *; }
-keep class * implements com.youkydesign.sakeca.core.di.CoreDependenciesProvider { *; }
-keep class com.youkydesign.sakeca.core.di.CoreDependencies { *; }
-keep class com.youkydesign.sakeca.core.domain.IRecipeRepository { *; }
-keep class com.youkydesign.sakeca.core.domain.*Component$Builder { *; }

# Keep Dagger generated components
-keep class *.Dagger*Component { *; }
-keep class *.Dagger* { *; } # More general rule for Dagger generated classes

# Keep classes annotated with Dagger annotations (if they are not processed correctly)
-keep @javax.inject.Inject class *
-keep class * { @javax.inject.Inject *; }
-keep class * { @dagger.* *; }
-keep @dagger.Module class *
-keep @dagger.Provides class *

# Keep your specific missing classes (as a more targeted approach if the above are too broad or don't work)
-keep class com.youkydesign.sakeca.core.di.CoreComponent
-keep class com.youkydesign.sakeca.core.di.CoreComponent$Factory
-keep class com.youkydesign.sakeca.core.di.DaggerCoreComponent
-keep class com.youkydesign.sakeca.core.di.CoreDependencies
-keep class com.youkydesign.sakeca.core.di.CoreDependenciesProvider
-keep class com.youkydesign.sakeca.core.domain.IRecipeRepository

# Keep Dagger generated classes
-keep class com.youkydesign.sakeca.core.di.CoreComponent { *; }
-keep class com.youkydesign.sakeca.core.di.CoreComponent$Factory { *; }
-keep class com.youkydesign.sakeca.core.di.DaggerCoreComponent { *; }
-keep class com.youkydesign.sakeca.core.di.CoreDependencies { *; }
-keep class com.youkydesign.sakeca.core.di.CoreDependenciesProvider { *; }

# Keep domain classes if they are being removed
-keep class com.youkydesign.sakeca.core.domain.IRecipeRepository { *; }
-keep class com.youkydesign.sakeca.core.domain.RecipeInteractor_Factory { *; }
-keep class com.youkydesign.sakeca.core.domain.RecipeUseCase { *; }
-keep class com.youkydesign.sakeca.core.domain.UiResource { *; }
-keep class com.youkydesign.sakeca.core.domain.UiResource$* { *; }