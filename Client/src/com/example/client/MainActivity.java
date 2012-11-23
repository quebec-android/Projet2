package com.example.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

	//private HttpURLConnection connection = null;
	ConnectivityManager connMgr;
	MainActivity that;
	List<Song> songs;
	List<Song> playlist;
	boolean streamingMode = false; 
	String streamingPort = null;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		that = this;
		songs = new ArrayList<Song>();
		playlist = new ArrayList<Song>();
		
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		//create playlist view
		List<Song>result = new ArrayList<Song>();
		ListView lv = (ListView) findViewById(R.id.playlist_listview);
		SongAdapter aa = new SongAdapter(this,R.layout.playlist_song,result);
		lv.setAdapter(aa);

		Utils.getXML("getList",connMgr,this);
		Utils.getXML("setStream&option=0", connMgr, this);
	} 
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void playListener(View v) {
		Log.d("ManETS","PLAY!!");
		try{ 
			Utils.getUrl("play",connMgr,this);	 	
			if (streamingMode) {
				if (streamingPort == null) {
					Utils.getXML("setStream&option=1", connMgr, this);
				}
				
				if (streamingPort != null) {
					MediaPlayer mediaPlayer = new MediaPlayer();
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDataSource(Const.URL+streamingPort); 
					mediaPlayer.prepare(); // Opération qui prend beaucoup de temps.
					mediaPlayer.start();
				} else {
					streamingMode = false;
				}
				
			}
		}
		catch(Exception e){
			Log.d("ManETS","Exception : echec dans la connexion");
		}
	}

	public void nextListener(View v) {
		Log.d("ManETS","NEXT!!");
		try{
			Utils.getUrl("next",connMgr,this);
		}
		catch(Exception e){
			Log.d("ManETS",e.getMessage());
		}
	}

	public void previousListener(View v) {
		Log.d("ManETS","PREVIOUS!!");
		Utils.getUrl("previous",connMgr,this);
	}

	
	public void stopListener(View v) {
		Log.d("ManETS","STOP!!");
		Utils.getUrl("stop",connMgr,this);
	} 

	public void toBeginningListener(View v) {
		Log.d("ManETS","toBeginning!!");
	} 
	
	public void playlist_eventlistener(View v){
		View parent = (View) v.getParent();
		
		int parentID = parent.getId();
		if(parentID == R.id.files_listview){//add song to play list
			int id = v.getId();
			Utils.getUrl("playlistadd&option="+id,connMgr,this);
			
		} else if( parentID == R.id.playlist_listview) {//play song from playlist 
			int id = v.getId(); 
			int statusCode = Utils.getUrl("playlistremove&option="+id,connMgr,this);
			if ( statusCode == Const.OK) {
				Utils.getXML("getPlayList",connMgr,this);
			} else {
				Log.d("Playlist Listener","Status code : "+statusCode);
			}
			
		}
	}

	
}
