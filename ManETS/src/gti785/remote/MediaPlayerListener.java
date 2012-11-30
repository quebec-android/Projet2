package gti785.remote;

import gti785.param.Const;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * Implements listener for MediaListPlayer to detect passage to next song
 * and refresh the current played song
 * 
 * @author Cedric
 *
 */
public class MediaPlayerListener implements MediaPlayerEventListener {
	private ETSRemote remote;
	
	public MediaPlayerListener(ETSRemote remote){
		super();
		this.remote = remote;
	}

	public void backward(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void buffering(MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	public void endOfSubItems(MediaPlayer arg0) {
	}

	public void error(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void finished(MediaPlayer arg0) {
		if (!remote.getRepeatMode().equals(Const.SONG)) {
			remote.next();
		} else {
			remote.toBeginning();
		}
	}

	public void forward(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void lengthChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaFreed(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaStateChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
		// TODO Auto-generated method stub
		
	}

	public void newMedia(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void opening(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void pausableChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void paused(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void playing(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void positionChanged(MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	public void seekableChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void snapshotTaken(MediaPlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void stopped(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void subItemFinished(MediaPlayer arg0, int arg1) {
	}

	public void subItemPlayed(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void timeChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	public void titleChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void videoOutput(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
