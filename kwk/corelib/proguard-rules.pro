-include "consumer-rules.pro"
-keep class com.appsflyer.** { *; }

-keep class org.apache.cordova.domen.Dataset { *; }
-keep class org.apache.cordova.domen.KwkResponse { *; }


#-keep class org.apache.cordova.domen.KwkResponseNew { *; }
-repackageclasses ''