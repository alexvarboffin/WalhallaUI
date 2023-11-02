package org.apache.cordova.v70;


public class WebViewAppConfig
{


	// true for enabling Google Analytics, should be true in production release
	public static final boolean ANALYTICS = true;

	// app id and client key for Parse push notifications,
	// keep these constants empty if you do not want to use push notifications,
	// if you want to use push notifications, setup these constants and
	// uncomment necessary permissions, service and receivers in AndroidManifest.xml file
	public static final String PARSE_APPLICATION_ID = "";
	public static final String PARSE_CLIENT_KEY = "";

	// true for opening webview links in external web browser rather than directly in the webview
	public static final boolean OPEN_LINKS_IN_EXTERNAL_BROWSER = false;

	// rules for opening links in external browser,
	// if URL link contains the string, it will be opened in external browser,
	// these rules have higher priority than OPEN_LINKS_IN_EXTERNAL_BROWSER option
	public static final String[] LINKS_OPENED_IN_EXTERNAL_BROWSER = {
			"target=blank",
			"target=external",
			"play.google.com/store",
			"youtube.com/watch"
	};

	// rules for opening links in internal webview,
	// if URL link contains the string, it will be loaded in internal webview,
	// these rules have higher priority than OPEN_LINKS_IN_EXTERNAL_BROWSER option
	public static final String[] LINKS_OPENED_IN_INTERNAL_WEBVIEW = {
			"target=webview",
			"target=internal"
	};


}
