package com.example.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.client.R.id;
import com.example.client.R.string;

/**
 * The MainActivity activity is the main page of the application with display
 * of the song list, the playlist and the control buttons
 * @author Cedric
 *
 */
public class MainActivity extends Activity {

	private ConnectivityManager connMgr;
	private List<Song> songs;
	private List<Song> playlist;
	private boolean streamingMode = false; 
	private String streamingPort = null; 
	private ProgressBarThread progressBarThread = null;
	private ProgressBar progressBar = null;
	private boolean modifyPlaylist = false;
	private int playingSongID = -1; // -1 ==> joue pas // 0 et + ==> idPlaylist qui est en train d'être jouée
	private SeekBar volumeBar = null; //0 à 200%

	private int tmpID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		
		//custom de la volumeBar
		volumeBar = (SeekBar) findViewById(id.volumeBar);
		volumeBar.setProgress(100);
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		    public void onStopTrackingTouch(SeekBar arg0) {  
		    	changeVolume(arg0.getProgress());  
		    }
		 
		    public void onStartTrackingTouch(SeekBar arg0) { 
		        }
		    
		    //When progress level of seekbar1 is changed
		    public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		    }
		});
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * change volume listener
	 * @param progress
	 */
	public void changeVolume(int progress){
		Utils.getUrl("volume&option="+progress,connMgr,this);
	}
	
	/**
	 * play button event listener
	 * @param v
	 */
	public void playListener(View v) { 
		if (playlist.size() > 0) {
			Button button = (Button)findViewById(R.id.play);
			if (button.getText().equals(getString(string.pause))) {
				Log.d("ManETS","PAUSE!!");
				Utils.getUrl("pause",connMgr,this);
				button.setText(string.play);
				if (progressBarThread != null) {
					progressBarThread.setInPause(true);
				}
			} else {
				play();
				if (playingSongID != -1) {
					if (progressBarThread != null) {
						progressBarThread.setInPause(false);
					}
					Utils.getUrl("pause",connMgr,this);
					Log.d("ManETS","Reprise de la chanson courante");
				} else {
					try{
						Utils.getXML("play",connMgr,this);	 	
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
						//On va chercher la cover
						//	Utils.getImage(playlist.get(0).getUrl(), connMgr, this);
					}
					catch(Exception e){
						Log.d("ManETS","Exception : echec dans la connexion");
					}
				}
			}
		}
	}

	/**
	 * repeat button event listener
	 * @param v
	 */
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
	
	/**
	 * next button event listener
	 * @param v
	 */
	public void nextListener(View v) {
		Log.d("ManETS","NEXT!!");
		Utils.getUrl("next",connMgr,this);
	}
	
	/**
	 * previous button event listener
	 * @param v
	 */
	public void previousListener(View v) {
		Log.d("ManETS","PREVIOUS!!");
		Utils.getUrl("previous",connMgr,this);
		//Utils.getUrl("poll",connMgr,this);
	}
	
	/**
	 * stop button event listener
	 * @param v
	 */
	public void stopListener(View v) {
		Log.d("ManETS","STOP!!");
		Button button = (Button)findViewById(R.id.play);
		button.setText(string.play);
		Utils.getUrl("stop",connMgr,this);
		stop();
	} 

	/**
	 * play button event listener
	 */
	public void play(){
		runOnUiThread(new Runnable() {
			public void run() {
				Button play = (Button)findViewById(R.id.play);
				play.setText(string.pause);
				Log.d("ManETS","PLAY!!");
			}
		});
	}
	
	/**
	 * Adds the song information to the view
	 */
	public void setInformation(){
		runOnUiThread(new Runnable() {//added so method can be called from without the MainActivity
			public void run() {
				Song song = playlist.get(playingSongID);
				TextView tv = (TextView) findViewById(R.id.information);
				String info = "Album: "+song.getAlbum()+"\nTitle: "+song.getTitle();
				tv.setText(info);
			}
		});
	}
	
	/**
	 * stop button event listener
	 */
	public void stop(){
		runOnUiThread(new Runnable() {
			public void run() {
				if (playingSongID != -1) {
					findViewById(playingSongID).setBackgroundColor(Color.TRANSPARENT);
				}
				Button play = (Button)findViewById(R.id.play);
				play.setText(string.play);
				TextView tv = (TextView) findViewById(R.id.information);
				String info = "";
				tv.setText(info);
			}
		});
		playingSongID = -1;
		if (progressBarThread != null) {
			progressBarThread.cheat();
		}
	}
	
	/**
	 * toBeginning event listener
	 * @param v
	 */
	public void toBeginningListener(View v) {
		Log.d("ManETS","toBeginning!!");
		Utils.getUrl("toBeginning",connMgr,this);
	} 
	
	/**
	 * random button event listener
	 * @param v
	 */
	public void modifyRandom(View v) {
		stop();
		Utils.getUrl("shuffle&option=1",connMgr,this); 
		Log.d("ManETS","Shuffle up & deal bitch!!");
	} 
	
	/**
	 * stream button event listener
	 * @param v
	 */
	public void modifyStreaming(View v) {
		ToggleButton button = (ToggleButton)findViewById(R.id.streaming);
		if(button.isChecked()) {
			streamingMode = true;
			Utils.getXML("setStream&option=1", connMgr, this);
			Log.d("ManETS","Streaming ==> 1");
		} else {
			streamingMode = false;
			Utils.getXML("setStream&option=0", connMgr, this);
			Log.d("ManETS","Streaming ==> 0");
		} 
	}
	
	/**
	 * modify button event listener
	 * @param v
	 */
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
	
	/**
	 * Event listener on the listview elements
	 * @param v the textview element that was clicked on
	 */
	public void playlist_eventlistener(View v){
		View parent = (View) v.getParent();

		int parentID = parent.getId();
		if(parentID == R.id.files_listview){//add song to play list
			int id = v.getId();
			Utils.getUrl("playlistadd&option="+id,connMgr,this);  

		} else if( parentID == R.id.playlist_listview) {//play song from playlist 
			int id = v.getId();
			String command = null;
			if(modifyPlaylist) { //suprime chanson de la playlist
				command = "playlistremove&option="+id;
				int statusCode = Utils.getUrl(command,connMgr,this);
				if ( statusCode == Const.OK) {
					Utils.getXML("getPlayList",connMgr,this);
					hilightPlayedSong();
				} else {
					Log.d("Playlist Listener","Status code : "+statusCode);
				}
			} else { //joue une chanson
				if (playingSongID != -1) {
					findViewById(playingSongID).setBackgroundColor(Color.TRANSPARENT);
				}
				playingSongID = id;
				command = "play&option="+id; 
				play();
				try{
					Utils.getXML(command,connMgr,this);	 	
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
				}
				catch(Exception e){
					Log.d("ManETS","Exception : echec dans la connexion");
				}
			}
		}
	}
	
	/**
	 * Highlights the current song
	 */
	public void hilightPlayedSong(){
		runOnUiThread(new Runnable() {
			public void run() {
				if (playlist.size() > 0) {
					if (playingSongID != -1) {
						findViewById(playingSongID).setBackgroundColor(Color.TRANSPARENT);
					}
					findViewById(tmpID).setBackgroundColor(Color.GRAY);
					playingSongID = tmpID;
				}
			}
		});
	}


	/************************  GETTERS & SETTERS  ************************/


	public ConnectivityManager getConnMgr() {
		return connMgr;
	}

	public void setConnMgr(ConnectivityManager connMgr) {
		this.connMgr = connMgr;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public List<Song> getPlaylist() {
		return playlist;
	}

	public void setPlaylist(List<Song> playlist) {
		this.playlist = playlist;
	}

	public boolean isStreamingMode() {
		return streamingMode;
	}

	public void setStreamingMode(boolean streamingMode) {
		this.streamingMode = streamingMode;
	}

	public String getStreamingPort() {
		return streamingPort;
	}

	public void setStreamingPort(String streamingPort) {
		this.streamingPort = streamingPort;
	}

	public boolean isModifyPlaylist() {
		return modifyPlaylist;
	}

	public void setModifyPlaylist(boolean modifyPlaylist) {
		this.modifyPlaylist = modifyPlaylist;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public ProgressBarThread getProgressBarThread() {
		return progressBarThread;
	}

	public void setProgressBarThread(ProgressBarThread progressBarThread) {
		this.progressBarThread = progressBarThread;
	}

	public int getPlayingSongID() {
		return playingSongID;
	}

	public void setPlayingSongID(int playingSongID) {
		this.playingSongID = playingSongID;
	}

	public int getTmpID() {
		return tmpID;
	}

	public void setTmpID(int tmpID) {
		this.tmpID = tmpID;
	}
}
