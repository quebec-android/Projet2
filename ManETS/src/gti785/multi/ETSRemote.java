package gti785.multi;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class ETSRemote {
	private MediaFolder mediaFolder;
	private final MediaListPlayer mediaPlayer;
	private MediaList mediaList;
	private boolean random;
	
	public ETSRemote(MediaFolder mediaFolder){
		this.mediaFolder=mediaFolder;
		NativeLibrary.addSearchPath(
		RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files (x86)/VideoLAN/VLC/sdk/lib"
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
	}
	
	/**
	 * Methode Play
	 * if param set to null, plays first item in play list
	 * else plays the mrl passed as argument
	 * @param mrl
	 * @return
	 */
	public boolean play(int id){
		if( id == -1 ){
			if (random) {
				
			} else {
				
			}
			List<MediaListItem> items = mediaList.items();
			mrl = items.get(0).mrl();
		} else {
			
		}
		return MediaPlayer.playMedia(mrl);
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
	}
	
	public void shuffle(){ //va changer les id ou pas ??
		Collections.shuffle(mediaList.items());
	}
	
	public void repeat(String option){
		if (option.equals("song")){
			mediaPlayer.setMode(MediaListPlayerMode.REPEAT);
		} else if (option.equals("playlist")) {
			mediaPlayer.setMode(MediaListPlayerMode.LOOP);
		} else if (option.equals("none")) {
			mediaPlayer.setMode(MediaListPlayerMode.DEFAULT);
		}
	}
	
	public void playListAdd(int idSong){
		mediaList.addMedia(mediaFolder.getFiles().get(idSong).getMlr());
	}

	public void playListRemove(int idPlaylist) {
		mediaList.removeMedia(idPlaylist);
	}
	
	public void printPlayList(HttpServletResponse response) throws IOException{
		System.out.println("Play-list:");
		for( MediaListItem item:mediaList.items() ){
			System.out.println(item.name());
			response.getWriter().write(item.name());
		}
	}
}
