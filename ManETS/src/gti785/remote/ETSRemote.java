package gti785.remote;


import gti785.model.Media;
import gti785.model.MediaFolder;
import gti785.model.PlaylistItem;
import gti785.param.Const;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
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
	private MediaListPlayer mediaPlayer;
	private MediaList mediaList;
	private boolean random;
	private int currentSongPlaylistID;
	private List<PlaylistItem> playlist;
	private boolean streamingMode = false;
	

	/**
	 * Constructor: instanciates the vlcj libraries and the objects used to
	 * assign medias to player.
	 * 
	 * @param mediaFolder
	 */
	public ETSRemote(MediaFolder mediaFolder){
		this.mediaFolder = mediaFolder;
		//vlc setup
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), Const.vlcj);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		//media remote setup
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory.newMediaListPlayer();
		mediaList =  mediaPlayerFactory.newMediaList();
		MediaPlayerListener listener = new MediaPlayerListener(this);
		mediaPlayer.addMediaListPlayerEventListener(listener);

		//playlist setup
		currentSongPlaylistID = 0;
		playlist = new ArrayList<PlaylistItem>();
	}
	
	private String formatHttpStream(String serverAddress, String serverPort) {
	    String sb = "";
	    sb+=(":sout=#transcode{acodec=mp4a}");
	    sb+=(":duplicate{dst=std{access=http,mux=ogg,");
	    sb+=("dst=");
	    sb+=(serverAddress);
	    sb+=(':');
	    sb+=(serverPort);
	    sb+=("}}");
	    return sb;
	  }


	
	/**
	 * Methode Play
	 * if param set to null, plays first item in play list
	 * else plays the mrl passed as argument
	 * @param mrl
	 * @return boolean
	 */
	public boolean play(int idPlaylist){
		
		if( mediaList.size() < 1){
			return false;
		}
		if( idPlaylist == -1 ){
			mediaPlayer.play();
			return true;
		} else {
			//mediaPlayer.playItem(idPlaylist);//causes application to crash
			//int songID = playlist.get(idPlaylist).getSongID();
			idPlaylist++; //client starts at 0 for playlist, server at 1
			System.out.println("Play song: "+idPlaylist);
			if(idPlaylist > playlist.size())
				return false;
			
			while(currentSongPlaylistID != idPlaylist){
				this.next();
				try {
					Thread.sleep(5);//avoid medialistener being late when incrementing currentSongPlaylistID
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		mediaPlayer.playNext();
	}
	
	public void previous(){
		mediaPlayer.playPrevious();
		currentSongPlaylistID--;
	}
	
	public void changePlaylist() {
		List<MediaListItem> newMediaList = mediaList.items();
		mediaList.clear();
		this.fillMediaList(newMediaList);
		mediaPlayer.setMediaList(mediaList);
	}
	
	public void shuffle(){
		//retrieve current playList in new list
		List<MediaListItem> mediaListForShuffle = mediaList.items();
		
		//shuffle list
		Collections.shuffle(mediaListForShuffle);
		mediaList.clear();
		
		//populate mediaList with shuffled list
		this.fillMediaList(mediaListForShuffle);
		mediaPlayer.setMediaList(mediaList);
	}
	
	/**
	 * repeat is used to repeat a song, a playlist or nothing
	 * @param option "song" to repear a song, "paylist" for the entire playlist, "none" to deactivate
	 * @return boolean information on success
	 */
	public boolean repeat(String option){
		if (option.equals("song")){
			mediaPlayer.setMode(MediaListPlayerMode.REPEAT);
			return true;
		} else if (option.equals("playlist")) {
			mediaPlayer.setMode(MediaListPlayerMode.LOOP);
			return true;
		} else if (option.equals("none")) {
			mediaPlayer.setMode(MediaListPlayerMode.DEFAULT);
			return true;
		}
		return false;
	}
	
	/**
	 * Add song to playlist
	 * @param idSong of media to add
	 * @return true if song was added
	 */
	public boolean playListAdd(int idSong){
		if(mediaFolder.getFiles().size() >= idSong && idSong > 0 ){
			Media song = mediaFolder.getFiles().get(idSong-1);
			if (this.streamingMode == true) {
				String options = formatHttpStream(Const.IP, Const.STREAMING_PORT);
				mediaList.addMedia(song.getMrl(),options);
			} else {
				mediaList.addMedia(song.getMrl());
			}
			mediaPlayer.setMediaList(mediaList);
			playlist.add(new PlaylistItem(playlist.size()+1,song.getSongID()));
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
		if(mediaList.size() > idPlaylist){
			mediaList.removeMedia(idPlaylist);
			mediaPlayer.setMediaList(mediaList);
			playlist.remove(idPlaylist-1);
			this.refreshID(idPlaylist-1);
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
			changePlaylist();
		}
	}
}
