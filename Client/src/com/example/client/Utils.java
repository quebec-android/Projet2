package com.example.client;

import java.net.HttpURLConnection;
import java.net.URL;

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
}	
