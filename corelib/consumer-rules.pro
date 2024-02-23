-keep class com.appsflyer.** { *; }


-keep class org.apache.cordova.domen.KwkResponse { *; }

-repackageclasses 'c'#modify this

#-keep class org.apache.cordova.domen.KwkResponseNew { *; }
-keepclassmembers class org.apache.cordova.domen.KwkResponseNew {
    java.lang.String toString();
    java.lang.String message;
}

#===============================================================
#ВСЕСТО -->>-keep class org.apache.cordova.domen.Dataset { *; }
# Сохраняем аннотации Gson
-keepattributes *Annotation*

# Сохраняем все поля и методы в классе Dataset
#-keepclassmembers class org.apache.cordova.domen.** {
#    *;
#}

#-keepclassmembers class org.apache.cordova.domen.** {
#    void set*(***);
#        *** get*();
#}
-keepclassmembers class org.apache.cordova.domen.** {
    public *;
}

#===============================================================