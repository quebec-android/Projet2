package com.example.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewParent;
import android.widget.ListView;

public class MainActivity extends Activity {

	//private HttpURLConnection connection = null;
	ConnectivityManager connMgr;
	MainActivity that;
	List<Song> songs;
	List<Song> playlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		that = this;
		songs = new ArrayList<Song>();
		playlist = new ArrayList<Song>();
		
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		List<Song>result = new ArrayList<Song>();
		ListView lv = (ListView) findViewById(R.id.playlist_listview);
		//LinearLayout rl = (LinearLayout) findViewById(R.id.playList_container);
		SongAdapter aa = new SongAdapter(this,R.layout.playlist_song,result);
		lv.setAdapter(aa);
		
		getXML("getList",connMgr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void playListener(View v) {
		Log.d("ManETS","PLAY!!");
		try{
			//Utils.sendGetRequest(connection, Const.GET,"play");
			//BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			//rd.readLine();
			Utils.getUrl("play",connMgr);
		}
		catch(Exception e){
			Log.d("ManETS","Exception : echec dans la connexion");
		}
	}

	public void nextListener(View v) {
		Log.d("ManETS","NEXT!!");
		try{
			/*Utils.sendGetRequest(connection, Const.GET,"next");
				BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        rd.readLine();*/
			Utils.getUrl("next",connMgr);
		}
		catch(Exception e){
			Log.d("ManETS",e.getMessage());
		}
	}

	public void previousListener(View v) {
		Log.d("ManETS","PREVIOUS!!");
		Utils.getUrl("previous",connMgr);
	}

	
	public void stopListener(View v) {
		Log.d("ManETS","STOP!!");
		Utils.getUrl("stop",connMgr);
	}

	public void toBeginningListener(View v) {
		Log.d("ManETS","toBeginning!!");
	}
	
	public void playlist_eventlistener(View v){
		View parent = (View) v.getParent();
		
		int parentID = parent.getId();
		if(parentID == R.id.files_listview){//add song to play list
			int id = v.getId();
			Utils.getUrl("playlistadd&option="+id,connMgr);
			
			
			ListView lv2 = (ListView) findViewById(R.id.playlist_listview);
			SongAdapter ab = (SongAdapter) lv2.getAdapter();
			
			Song song = new Song(songs.get(id-1));
			if( playlist.isEmpty())
				song.id = 0;
			else
				song.id = playlist.size();
			
			playlist.add(song);
			
			ab.add(song);
			lv2.setAdapter(ab);
		}
		else if( parentID == R.id.playlist_listview) {//play song from playlist
			int id = v.getId();
			Utils.getUrl("play&option="+id,connMgr);
		}
		
		
		
	}

	public void getXML(String command, ConnectivityManager connMgr){
		String stringUrl = Const.GET+""+command;

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if( networkInfo != null && networkInfo.isConnected() ){
			new DownloadXmlTask().execute(stringUrl);
		}
		else{

		}
	}

	private class DownloadXmlTask extends AsyncTask<String, Void, List<Song>> {
		@Override
		protected List<Song> doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				//return getResources().getString(R.string.connection_error);
				return null;
			} catch (XmlPullParserException e) {
				// return getResources().getString(R.string.xml_error);
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Song> result) {  
			if(result != null){
			
				ListView lv =(ListView) findViewById(R.id.files_listview);
				//LinearLayout rl = (LinearLayout) findViewById(R.id.files_container);
				SongAdapter aa = new SongAdapter(that,R.layout.playlist_song,result);
				lv.setAdapter(aa);
				songs = result;
				
				//rl.addView(lv);
				
			}

		}

		// Uploads XML from stackoverflow.com, parses it, and combines it with
		// HTML markup. Returns HTML string.
		private List<Song> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
			InputStream stream = null;

			// Instantiate the parser
			StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
			List<Song> songs = null;
			String title = null;
			String url = null;

			try {
				stream = downloadUrl(urlString);        
				songs = stackOverflowXmlParser.parse(stream);

			} finally {
				if (stream != null) {
					stream.close();
				} 
			}


			return songs;
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
}
