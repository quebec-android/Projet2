package gti785.multi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.thoughtworks.xstream.XStream;

public class ETSRemote {
	private MediaFolder mediaFolder;
	private MediaListPlayer mediaPlayer;
	private MediaList mediaList;
	private boolean random;
	private int currentSongPlaylistID;
	
	public ETSRemote(MediaFolder mediaFolder){
		this.mediaFolder=mediaFolder;
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"
		/*
		 * "EMPLACEMENT DU DOSSIER QUI CONTIENT libvlc"
		 * fabien C:\Program Files (x86)\VideoLAN\VLC\sdk\lib
		 * cedric /Applications/VLC.app/Contents/MacOS/lib
		 */
		);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory.newMediaListPlayer();
		mediaList =  mediaPlayerFactory.newMediaList();
		MediaPlayerListener listener = new MediaPlayerListener(this);
		mediaPlayer.addMediaListPlayerEventListener(listener);
		currentSongPlaylistID = 0;
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
			
			return true;
		}

	}
	
	public void pause(){
		mediaPlayer.pause();
	}
	
	public void stop(){
		mediaPlayer.stop();
	}
	
	public void next(){
		mediaPlayer.playNext();
	}
	
	public void previous(){
		mediaPlayer.playPrevious();
		currentSongPlaylistID--;
	}
	
	public void shuffle(){
		List<MediaListItem> mediaListForShuffle = mediaList.items();
		Collections.shuffle(mediaListForShuffle);
		mediaList.clear();
		this.fillMediaList(mediaListForShuffle);
		mediaPlayer.setMediaList(mediaList);
	}
	
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
	
	public boolean playListAdd(int idSong){
		if(MediaFolder.getFiles().size() >= idSong && idSong > 0 ){
			mediaList.addMedia(MediaFolder.getFiles().get(idSong-1).getMrl());
			mediaPlayer.setMediaList(mediaList);
			return true;
		}
		else
			return false;
	}

	public boolean playListRemove(int idPlaylist) {
		if(mediaList.size() > idPlaylist){
			mediaList.removeMedia(idPlaylist);
			mediaPlayer.setMediaList(mediaList);
			return true;
		}
		else{
			return false;
		}
	}
	
	public void printPlayList(HttpServletResponse response, XStream xstream) throws IOException{
		PrintWriter out = response.getWriter();
		out.write(xstream.toXML(mediaList.items()));
	}
	
	public void fillMediaList(List<MediaListItem> mediaListAdd){
		for(MediaListItem media:mediaListAdd)
			mediaList.addMedia(media.mrl());
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
	
}
