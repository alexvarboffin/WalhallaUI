#-classobfuscationdictionary D:\android\GitHub\facebook\proguard\examples\dictionaries\compact.txt
#-packageobfuscationdictionary D:\android\GitHub\facebook\proguard\examples\dictionaries\windows.txt
#-optimizationpasses 3
#-overloadaggressively
#-repackageclasses ''
#-allowaccessmodification

#-include "C:\android\proguard\dagger2.pro"
#-include "C:\android\proguard\okhttp3.pro"
#-include "C:\android\proguard\admob.pro"

# Firebase
-dontwarn retrofit2.Call

# Databinding CardViewBindingAdapter
-dontwarn androidx.cardview.widget.CardView

#Crashlytics
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.


#-keepclassmembers class com.com.google.samples.apps.iosched.model.** { <fields>; }
#-repackageclasses 'c'

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
-dontwarn java.lang.invoke.StringConcatFactory