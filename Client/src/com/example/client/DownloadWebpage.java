package com.example.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Télécharge des données du serveur en asynchrone 
 * Utilisée quand il n'y a qu'un code réponse à gérer
 * @author Fab
 *
 */
public class DownloadWebpage extends AsyncTask<String,Object,String> {
	private static final String DEBUG_TAG = "HttpExample";
	private int statusCode = 200;
	private MainActivity mainActivity = null;
	
	public DownloadWebpage(MainActivity mainActivity) {
		super();
		this.mainActivity = mainActivity;
	}
	
	@Override
	/**
	 * return result as argument of onPostExecute
	 */
	protected String doInBackground(String... urls) {
		try{
			return downloadUrl(urls[0]);
		}
		catch(IOException e){
			Log.d("ManETS","Exception : unable to retrieve web page.");
			return "unable to retrieve web page.";
			
		}
	}
	
	@Override
	/**
	 * called by doInBackground with return
	 */
	protected void onPostExecute(String result){
		//textView.setText(result);
	}
	
	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        statusCode =conn.getResponseCode();
    		Log.d(DEBUG_TAG, "The response is: " + statusCode);
    		
        	if (statusCode==200 && (myurl.contains("playlistadd") || myurl.contains("playlistremove") || myurl.contains("shuffle"))) {
        		Utils.getXML("getPlayList",mainActivity.getConnMgr(),mainActivity);
        		is = conn.getInputStream();
        	}
        	
        	
//    		if( myurl.contains("Artwork")){
//    			BufferedOutputStream out = null;
//    			Bitmap bitmap = null;
//    			is = conn.getInputStream();
//    			bitmap = BitmapFactory.decodeStream(is);
//    			mainActivity.setImage(bitmap);
//    			return null;
//    		}
//    		else{
    			is = conn.getInputStream();
    			// Convert the InputStream into a string
    	        String contentAsString = readIt(is, len);
    	        return contentAsString;
//    		}
//	        
	        
	        
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
	
	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}

	public int getStatusCode() {
		return statusCode;  
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
			
}
