# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

-dontwarn java.util.ConcurrentModificationException

##---------------Begin: Kakao SDK  ----------
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep interface com.kakao.sdk.**.*Api
##---------------END: Kakao SDK  ----------

# Keep Parcelable classes
-keep class com.teamwable.core.model.** implements android.os.Parcelable { *; }

# Keep Serializable classes
-keep class com.teamwable.network.dto.** implements java.io.Serializable { *; }


##---------------Begin: Kotlin Serialization ----------
# Keep all @Serializable annotated classes
-keep class kotlinx.serialization.** { *; }

# Keep classes that use kotlinx.serialization
-keepclassmembers class ** {
    @kotlinx.serialization.* <fields>;
}
##---------------END: Kotlin Serialization ----------

##---------------Begin: Retrofit  ----------
# Retrofit does reflection on method and parameter annotations.
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retrofit uses reflection to invoke methods annotated with @HTTP method
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
##---------------End: Retrofit  ----------

##---------------Begin: OkHttp -------------
# Firebase related ProGuard rules
-dontwarn com.google.firebase.**
-keep class com.google.firebase.** { *; }

# Hilt related ProGuard rules
-keep class dagger.hilt.internal.** { *; }

# OkHttp related ProGuard rules
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
##---------------End : OkHttp --------------

-keep class * extends android.app.Activity { *; }
-keep class * extends android.app.Service { *; }
