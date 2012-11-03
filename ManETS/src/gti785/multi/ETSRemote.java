package gti785.multi;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class ETSRemote {
	private final AudioMediaPlayerComponent MediaPlayer;
	
	public ETSRemote(){
		NativeLibrary.addSearchPath(
		RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"//"EMPLACEMENT DU DOSSIER QUI CONTIENT libvlc"
		);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		MediaPlayer = new AudioMediaPlayerComponent();
	}
	
	public void play(){

	}
}
