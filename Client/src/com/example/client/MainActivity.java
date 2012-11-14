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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ui.PlayListElement;

public class MainActivity extends Activity {

	//private HttpURLConnection connection = null;
	ConnectivityManager connMgr;
	MainActivity that;
	/*private Button play = null;
	private Button next = null; 
	private Button previous = null; 
	private Button stop = null; 
	private Button toBeginning = null;*/


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		that = this;
		/*play = (Button)findViewById(R.id.play);
        play.setOnClickListener(playListener);

        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(nextListener);

        previous = (Button)findViewById(R.id.previous);
        previous.setOnClickListener(previousListener);

        stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(stopListener);

        toBeginning = (Button)findViewById(R.id.toBeginning);
        toBeginning.setOnClickListener(toBeginningListener);*/

		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		//LinearLayout rl2 = (LinearLayout) findViewById(R.id.files);
		//rl2.addView(lv);
		getXML("getList",connMgr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// Play
	// private OnClickListener playListener = new OnClickListener() {
	//@Override
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
	// };

	// Next
	// private OnClickListener nextListener = new OnClickListener() {
	//   @Override
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
	//   };

	// Previous
	//private OnClickListener previousListener = new OnClickListener() {
	//@Override
	public void previousListener(View v) {
		Log.d("ManETS","PREVIOUS!!");
		Utils.getUrl("previous",connMgr);
	}
	//};

	// Stop
	//private OnClickListener stopListener = new OnClickListener() {
	//  @Override
	public void stopListener(View v) {
		Log.d("ManETS","STOP!!");
		Utils.getUrl("stop",connMgr);
	}
	//};

	// Play
	// private OnClickListener toBeginningListener = new OnClickListener() {
	//   @Override
	public void toBeginningListener(View v) {
		Log.d("ManETS","toBeginning!!");
	}
	//};

	public void playlist_eventlistener(View v){
		Log.d("test","playlist");
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
			//setContentView(R.layout.main);
			// Displays the HTML string in the UI via a WebView
			//WebView myWebView = (WebView) findViewById(R.id.webview);
			//myWebView.loadData(result, "text/html", null);
			ListView lv = new ListView(that);

			List<String> songs = new ArrayList<String>();
			//String[] songs = new String[] {"Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3",
					//"Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3","Title1", "Title2", "Title3"};
			for(Song title: result){
				songs.add(title.title);
			}
			final ArrayAdapter<String> aa = new ArrayAdapter<String>(that, R.layout.playlist_song, songs);
			lv.setAdapter(aa);

			LinearLayout rl = (LinearLayout) findViewById(R.id.playList_container);
			rl.addView(lv);

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
