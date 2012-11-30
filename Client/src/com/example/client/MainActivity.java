package com.example.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.example.client.R.string;

public class MainActivity extends Activity {

	//private HttpURLConnection connection = null;
	ConnectivityManager connMgr;
	MainActivity that;
	List<Song> songs;
	List<Song> playlist;
	boolean streamingMode = false; 
	String streamingPort = null; 
 
	private ProgressBar progressBar = null;
	private float progressBarStatus = 0;
	private int progressBarStatusInt = 0;
	private float currentSongLength = 0;
	private float increment = 0;

	private boolean modifyPlaylist = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		that = this;
		songs = new ArrayList<Song>();
		playlist = new ArrayList<Song>(); 
		
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		//create playlist view
		List<Song>result = new ArrayList<Song>();
		ListView lv = (ListView) findViewById(R.id.playlist_listview);
		SongAdapter aa = new SongAdapter(this,R.layout.playlist_song,result);
		lv.setAdapter(aa);
		
		//on fait un stop au cas où le serveur était en marche
		Utils.getUrl("stop",connMgr,this);
		
		//on met le mode repeat à NONE
		Utils.getUrl("repeat&option=none",connMgr,this);
		
		//on va cherche les deux listes courantes sur le serveur
		Utils.getXML("getList",connMgr,this);
		Utils.getXML("setStream&option=0", connMgr, this);
	} 
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void playListener(View v) { 
		Button button = (Button)findViewById(R.id.play);
		if (button.getText().equals(getString(string.pause))) {
			Log.d("ManETS","PAUSE!!");
			Utils.getUrl("pause",connMgr,this);
			button.setText(string.play);
		} else {
			button.setText(string.pause);
			Log.d("ManETS","PLAY!!");
			try{
				Utils.getUrl("play",connMgr,this);	 	
				if (streamingMode) {
					if (streamingPort == null) {
						Utils.getXML("setStream&option=0", connMgr, this);
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
				currentSongLength = playlist.get(0).getLength();
				increment = 100/currentSongLength;
				
				//On va chercher la cover
//			Utils.getImage(playlist.get(0).getUrl(), connMgr, this);
				
				
				new Thread(new Runnable() {
					public void run() {
						while (progressBarStatusInt < 100) {
							
							try {
								progressBarStatus = progressBarStatus + increment;
								progressBarStatusInt = (int) Math.round(progressBarStatus);
								progressBar.setProgress(progressBarStatusInt);
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			catch(Exception e){
				Log.d("ManETS","Exception : echec dans la connexion");
			}
		}
	}

	public void modifyRepeat(View v) { 
		Button button = (Button)findViewById(R.id.repeatMode);
		if (button.getText().equals(getString(string.repeat_NONE))) {
			Log.d("ManETS","repeat one!!");
			Utils.getUrl("repeat&option=song",connMgr,this);
			button.setText(string.repeat_SONG);
		} else if (button.getText().equals(getString(string.repeat_SONG))) {
			Log.d("ManETS","repeat all!!");
			Utils.getUrl("repeat&option=playlist",connMgr,this);
			button.setText(string.repeat_ALL);
		} else {
			Log.d("ManETS","repeat none!!");
			Utils.getUrl("repeat&option=none",connMgr,this);
			button.setText(string.repeat_NONE);
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
		Utils.getUrl("toBeginning",connMgr,this);
	} 
	
	public void modifyRandom(View v) {
		Utils.getUrl("shuffle&option=1",connMgr,this); 
		Log.d("ManETS","Shuffle up & deal bitch!!");
	} 
	
	public void modifyStreaming(View v) {
		ToggleButton button = (ToggleButton)findViewById(R.id.streaming);
		if(button.isChecked()) {
			Utils.getXML("setStream&option=1", connMgr, this);
			Log.d("ManETS","Streaming ==> 1");
		} else {
			Utils.getXML("setStream&option=0", connMgr, this);
			Log.d("ManETS","Streaming ==> 0");
		} 
	}
	
	public void modifyPlaylist(View v) {
		Button button = (Button)findViewById(R.id.modifyPlaylistButton);
		if(modifyPlaylist) {
			modifyPlaylist = false;
			button.setText(string.modifyPlaylistButton_turn_on);
		} else {
			modifyPlaylist = true;
			button.setText(string.modifyPlaylistButton_turn_off);
		}
	}  
	
	public void playlist_eventlistener(View v){
		View parent = (View) v.getParent();
		
		int parentID = parent.getId();
		if(parentID == R.id.files_listview){//add song to play list
			int id = v.getId();
			Utils.getUrl("playlistadd&option="+id,connMgr,this);
			
		} else if( parentID == R.id.playlist_listview) {//play song from playlist 
			int id = v.getId();
			String command = null;
			if(modifyPlaylist)
				command = "playlistremove&option="+id;
			if(!modifyPlaylist)
				command = "play&option="+id; 
			
			int statusCode = Utils.getUrl(command,connMgr,this);
			if ( statusCode == Const.OK) {
				Utils.getXML("getPlayList",connMgr,this);
			} else {
				Log.d("Playlist Listener","Status code : "+statusCode);
			}
		}
	}
	
	public void setImage(Bitmap bitmap) {
		ImageView image = (ImageView) findViewById(R.id.artwork);
		image.setImageBitmap(bitmap);
	}
}
