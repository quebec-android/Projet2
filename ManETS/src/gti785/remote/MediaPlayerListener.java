package gti785.remote;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 * Implements listener for MediaListPlayer to detect passage to next song
 * and refresh the current played song
 * 
 * @author Cedric
 *
 */
public class MediaPlayerListener implements MediaListPlayerEventListener {
	private ETSRemote remote;
	
	public MediaPlayerListener(ETSRemote remote){
		super();
		this.remote = remote;
	}
	
	public void mediaDurationChanged(MediaListPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaFreed(MediaListPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mediaMetaChanged(MediaListPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaParsedChanged(MediaListPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaStateChanged(MediaListPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("media state changed");
		
	}

	public void mediaSubItemAdded(MediaListPlayer arg0, libvlc_media_t arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Invoked each time the player passes to the next song. Refresh the currently played song?
	 */
	public void nextItem(MediaListPlayer arg0, libvlc_media_t arg1, String arg2) {//called when mediaList skips to next item
		// TODO Auto-generated method stub
		System.out.println("next item");
		remote.incrementSongPlaylistID();

	}

	public void played(MediaListPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void stopped(MediaListPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
