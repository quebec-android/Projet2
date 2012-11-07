package gti785.multi;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class ETSRemote {
	private final HeadlessMediaPlayer MediaPlayer;
	private MediaList mediaList;
	
	public ETSRemote(){
		NativeLibrary.addSearchPath(
		RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"//"EMPLACEMENT DU DOSSIER QUI CONTIENT libvlc"
		);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		MediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		mediaList =  mediaPlayerFactory.newMediaList();
	}
	
	/**
	 * Methode Play
	 * if param set to null, plays first item in play list
	 * else plays the mrl passed as argument
	 * @param mrl
	 * @return boolean
	 */
	public boolean play(String mrl){
		if( mrl == null ){
			List<MediaListItem> items = mediaList.items();
			mrl = items.get(0).mrl();
		}
		return MediaPlayer.playMedia(mrl);
	}
	
	public void pause(){
		MediaPlayer.pause();
	}
	
	public void stop(){
		MediaPlayer.stop();
	}
	
	public void next(){
		
	}
	
	public void previous(){
		
	}
	
	public void shuffle(){
		
	}
	
	public void repeat(){
		if(MediaPlayer.getRepeat())
			MediaPlayer.setRepeat(true);
		else
			MediaPlayer.setRepeat(false);
	}
	
	public void playListAdd(String mrl){
		mediaList.addMedia(mrl);
	}

	public void playListRemove(int index) {
		mediaList.removeMedia(index);
	}
	
	public void printPlayList(HttpServletResponse response) throws IOException{
		System.out.println("Play-list:");
		for( MediaListItem item:mediaList.items() ){
			System.out.println(item.name());
			response.getWriter().write(item.name());
		}
	}
}
