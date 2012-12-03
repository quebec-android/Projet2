package com.example.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;


/**
 * Télécharge des données du serveur en asynchrone 
 * Permet d'actualiser la playlist ou de charger la liste des chansons
 * @author Fab
 *
 */
public class DownloadXmlTask extends AsyncTask<String, Void, RefreshListResult> {
	private MainActivity mainActivity = null;
	
	public DownloadXmlTask(MainActivity mainActivity) {
		super();
		this.mainActivity = mainActivity;
	}
	
	@Override
	protected RefreshListResult doInBackground(String... urls) {
		try {
			return loadXmlFromNetwork(urls[0]);
		} catch (IOException e) {
			//return getResources().getString(R.string.connection_error);
			Log.d("ManETS","Exception : conection error");
			return null;
		} catch (XmlPullParserException e) {
			// return getResources().getString(R.string.xml_error);
			Log.d("ManETS","Exception : string xml error");
			return null;
		}
	}

	@Override
	protected void onPostExecute(RefreshListResult result) {  
		if(result != null){
			if (result.getWhatList().equals("getList")){
				ListView lv =(ListView) mainActivity.findViewById(R.id.files_listview);
				SongAdapter aa = new SongAdapter(mainActivity,R.layout.playlist_song,result.getList());
				lv.setAdapter(aa);
				mainActivity.setSongs(result.getList());	
				
				//On démarre la socket pour écouter le serveur
				// on est sûr que le serveur est démarré et prêt
				new ListenerThread(mainActivity).execute();
				
				// on actualise la playlist
				Utils.getXML("getPlayList",mainActivity.getConnMgr(), mainActivity);
			} else if (result.getWhatList().equals("getPlayList")){
				ListView lv =(ListView) mainActivity.findViewById(R.id.playlist_listview);
				SongAdapter aa = new SongAdapter(mainActivity,R.layout.playlist_song,result.getList());
				lv.setAdapter(aa);
				mainActivity.setPlaylist(result.getList());	
			}
		}

	}

	// Uploads XML from stackoverflow.com, parses it, and combines it with
	// HTML markup. Returns HTML string.
	private RefreshListResult loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		InputStream stream = null;
		if (urlString.substring(urlString.lastIndexOf("=")+1).equals("getList")) {
			Log.d("ManETS","getList");
			
			// Instantiate the parser
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
			List<Song> songs = null;

			try {
				stream = downloadUrl(urlString);        
				songs = stackOverflowXmlParser.parse(stream);

			} finally {
				if (stream != null) {
					stream.close();
				} 
			}


			return new RefreshListResult(songs, "getList");
		} else if (urlString.substring(urlString.lastIndexOf("=")+1).equals("getPlayList")) {
			Log.d("ManETS","getPlayList");
			
			// Instantiate the parser
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser(mainActivity.getSongs());
			List<Song> songs = null;
			
			try {
				stream = downloadUrl(urlString);        
				songs = stackOverflowXmlParser.parse(stream);

			} finally {
				if (stream != null) {
					stream.close();
				} 
			}
			
			return new RefreshListResult(songs, "getPlayList");
			
		} else if (urlString.contains("setStream")) {
			Log.d("ManETS","setStream");
			
			// Instantiate the parser
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
			
			try {
				stream = downloadUrl(urlString);        
				mainActivity.setStreamingPort(stackOverflowXmlParser.parseToString(stream));

			} finally {
				if (stream != null) {
					stream.close();
				} 
			}
		} else if (urlString.contains("play")) {
			Log.d("ManETS","play");
			
			// Instantiate the parser
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
			
			try {
				stream = downloadUrl(urlString);        
				stackOverflowXmlParser.parse(stream);
	
			} finally {
				if (stream != null) {
					stream.close();
				} 
			}
		}
		return null;
	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();   
		return stream;
	}
}