package com.example.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
	
	public static int getUrl(String command, ConnectivityManager connMgr, MainActivity mainActivity){
		String stringUrl = Const.GET+""+command;
    	
    	 NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
         if( networkInfo != null && networkInfo.isConnected() ){
        	DownloadWebpage web = new DownloadWebpage(mainActivity);
        	web.execute(stringUrl);
        	
        	return web.getStatusCode();
         }
         else{
        	 Log.d("ManETS","Exception : No network connection available");
        	 return Const.ERROR;
         }
	}
	
	public static void getXML(String command, ConnectivityManager connMgr, MainActivity mainActivity){
		String stringUrl = Const.GET+command;

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if( networkInfo != null && networkInfo.isConnected() ){
			new DownloadXmlTask(mainActivity).execute(stringUrl);
		}
		else{
			Log.d("ManETS","Exception : No network connection available");
		}
	}

	
	
	public static boolean refreshId(List<Song> l, int id) {
		int i=0;
		for (Song s : l) {
			if (s.getId() == id) {
				l.remove(s);
				return true;
			} else {
				s.setId(i);
				i++;
			}
		}
		return false;
	}
	
	public static Song findSongById(List<Song> l, int id) {
		for (Song s : l) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
}	
