package com.example.client;

import java.net.HttpURLConnection;
import java.net.URL;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {
	
	/**
	 * Used to send getRequest
	 * @param connection
	 * @param url
	 * @param parametre
	 * @throws Exception
	 */
	public static void sendGetRequest(HttpURLConnection connection, String url, String parametre) throws Exception{
		URL u = new URL(url+parametre);
		connection = (HttpURLConnection) u.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
	}
	
	public static void getUrl(String command, ConnectivityManager connMgr){
		String stringUrl = Const.GET+""+command;
    	
    	 NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
         if( networkInfo != null && networkInfo.isConnected() ){
         	new DownloadWebpage().execute(stringUrl);
         }
         else{
        	 Log.d("ManETS","Exception : No network connection available");
         }
	}
}	
