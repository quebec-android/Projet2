package gti785.remote;


import gti785.model.Media;
import gti785.model.MediaFolder;
import gti785.model.PlaylistItem;
import gti785.param.Const;
import gti785.push.Push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Class ETSRemote is responsible for all the media related commands.
 * 
 * @author Cedric
 *
 */
public class ETSRemote {
	private MediaFolder mediaFolder;
	private MediaPlayer mediaPlayer;
	private MediaListPlayer mediaPlayerBis;
	private MediaList mediaList;
	private boolean random;
	private int currentSongPlaylistID;
	private List<PlaylistItem> playlist;
	private boolean streamingMode = true;
	private Push server;
	

	/**
	 * Constructor: instanciates the vlcj libraries and the objects used to
	 * assign medias to player.
	 * 
	 * @param mediaFolder
	 * @param server 
	 */
	public ETSRemote(MediaFolder mediaFolder, Push server ){
		this.mediaFolder = mediaFolder;
		//vlc setup
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), Const.vlcj);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		//media remote setup
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		
		//mediaPlayerBis = mediaPlayerFactory.newMediaListPlayer();
		mediaList =  mediaPlayerFactory.newMediaList();
		//MediaPlayerListener listener = new MediaPlayerListener(this);
		//mediaPlayerBis.addMediaListPlayerEventListener(listener);

		//playlist setup
		currentSongPlaylistID = 0;
		playlist = new ArrayList<PlaylistItem>();
		
		this.server = server;
	}
	
	/**
	 * Methode Play
	 * if param set to null, plays first item in play list
	 * else plays the mrl passed as argument
	 * @param mrl
	 * @return boolean
	 */
	public boolean play(int idPlaylist){
		
		//ICI on envoie l'id de la chanson que l'on va jouer pour actualiser le client
		server.pushMessage(""+idPlaylist);

		if( playlist.size() < 1){
			return false;
		}
		if( idPlaylist == -1 ){
			int songId = playlist.get(0).getSongID();
			if (this.streamingMode == true) {
				String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
				mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl(),options);
			} else {
				mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl());
			}
			
			currentSongPlaylistID = 0;
			return true;
		} else {
			int songId = playlist.get(idPlaylist).getSongID();
			currentSongPlaylistID = idPlaylist;
			idPlaylist++; //client starts at 0 for playlist, server at 1
			System.out.println("Play song: "+idPlaylist);
			if(idPlaylist > playlist.size())
				return false;
			
			if (this.streamingMode == true) {
				String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
				mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl(),options);
			} else {
				mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl());
			}
			return true;
		}

	}
	
	public void pause(){
		mediaPlayer.pause();
	}
	
	public void stop(){
		mediaPlayer.stop();
		currentSongPlaylistID = 0;
	}
	
	public void next(){
		currentSongPlaylistID++;
		if(currentSongPlaylistID > playlist.size()-1){
			currentSongPlaylistID = 0;
		}
		int songId = playlist.get(currentSongPlaylistID).getSongID();
		if (this.streamingMode == true) {
			String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
			mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl(),options);
		} else {
			mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl());
		}
	}
	
	public void previous(){
		currentSongPlaylistID--;
		if(currentSongPlaylistID < 0){
			currentSongPlaylistID = 0;
		}
		int songId = playlist.get(currentSongPlaylistID).getSongID();
		if (this.streamingMode == true) {
			String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
			mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl(),options);
		} else {
			mediaPlayer.playMedia(mediaFolder.getFiles().get(songId).getMrl());
		}
	}
	
	/*public void changePlaylist() {
		List<MediaListItem> newMediaList = mediaList.items();
		mediaList.clear();
		this.fillMediaList(newMediaList);
		mediaPlayer.setMediaList(mediaList);
	}*/
	
	public void shuffle(){
		//shuffle list
		Collections.shuffle(playlist);
	}
	
	/**
	 * repeat is used to repeat a song, a playlist or nothing
	 * @param option "song" to repear a song, "paylist" for the entire playlist, "none" to deactivate
	 * @return boolean information on success
	 */
	public boolean repeat(String option){
		/*if (option.equals("song")){
			mediaPlayer.setMode(MediaListPlayerMode.REPEAT);
			return true;
		} else if (option.equals("playlist")) {
			mediaPlayer.setMode(MediaListPlayerMode.LOOP);
			return true;
		} else if (option.equals("none")) {
			mediaPlayer.setMode(MediaListPlayerMode.DEFAULT);
			return true;
		}*/
		return false;
	}
	
	/**
	 * Add song to playlist
	 * @param idSong of media to add
	 * @return true if song was added
	 */
	public boolean playListAdd(int idSong){
		if(mediaFolder.getFiles().size() >= idSong && idSong >= 0 ){
			Media song = mediaFolder.getFiles().get(idSong);
			
			if (this.streamingMode == true) {
				String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
			} else {

			}
			
			playlist.add(new PlaylistItem(playlist.size(),song.getSongID()));
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Remove song from playlist
	 * @param idPlaylist of mediaListElement to remove
	 * @return true if media is removed
	 */
	public boolean playListRemove(int idPlaylist) {
		if(playlist.size() > idPlaylist && idPlaylist >= 0){
			playlist.remove(idPlaylist);
			this.refreshID(idPlaylist);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Used to fill the MediaList with a list of medias
	 * @param mediaListAdd media used to populate MediaList
	 */
	public void fillMediaList(List<MediaListItem> mediaListAdd){
		for(MediaListItem media : mediaListAdd) {
			if (this.streamingMode == true) {
				String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
				mediaList.addMedia(media.mrl(),options);
			} else {
				mediaList.addMedia(media.mrl());
			}
		}
	}

	public void incrementSongPlaylistID(){
		if(currentSongPlaylistID == mediaList.size())
			currentSongPlaylistID = 1;
		else
			currentSongPlaylistID++;
		
		System.out.println("Increment current song: "+currentSongPlaylistID);
	}
	
	private void refreshID(int pos){
		for(int i=pos;i<playlist.size();i++){
			playlist.get(i).decrementPlaylistID();
		}
	}
	
	private String formatHttpStream(String serverAddress, String serverPort) {
	    String sb = "";
	    sb+=(":sout=#transcode{acodec=mp4a}");
	    sb+=(":duplicate{dst=std{access=http,mux=ts,");
	    sb+=("dst=");
	    sb+=(serverAddress);
	    sb+=(':');
	    sb+=(serverPort);
	    sb+=("}}");
	    return sb;
	  }
	
	
	/**
	 * SETTERS and GETTERS
	 */
	public int getCurrentSongPlaylistID() {
		return currentSongPlaylistID;
	}

	public void setCurrentSongPlaylistID(int currentSongPlaylistID) {
		this.currentSongPlaylistID = currentSongPlaylistID;
	}
	
	public List<PlaylistItem> getPlaylist() {
		return playlist;
	}
	
	public boolean isStreamingMode() {
		return streamingMode;
	}

	public void setStreamingMode(boolean streamingMode) {
		if (this.streamingMode != streamingMode) {
			this.streamingMode = streamingMode;
		}
	}
}
