package gti785.multi;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Servlet implementation class MultiServlet
 */
public class ManETS_ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ETSRemote remote;
	private String dossier = "C:/Users/Fab/Music/Oasis";
	/*
	 * fabien "C:/Users/Fab/Music/Oasis"
	 * cedric "/Users/Cedric/Documents/Quebec/Cours/GTI785/Lab/Lab 02/Media/"
	 * 
	 */
	MediaFolder mediaFolder;
	
    /**
     * Default constructor. 
     */
    public ManETS_ServerServlet() {
    	mediaFolder = new MediaFolder(new File(dossier));
    	remote = new ETSRemote(mediaFolder);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String command = null;
		command = request.getParameter("command");
		
		if(command != null && command.equals("getList")){
			mediaFolder.print(response);
		}
		
		if(command != null && command.equals("play")){
			String id = null;
			id = request.getParameter("option");
			int idSong = -1;
			if (id != null) {
				idSong = Integer.parseInt(id);
			}
			if(remote.play(idSong)){
				System.out.println("Song in play");
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else{
				System.out.println("Error while trying to play song.");
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); 
			}
		}
		
		if(command != null && command.equals("pause")){
			remote.pause();
			System.out.println("Song paused");
		}
		
		if(command != null && command.equals("stop")){
			remote.stop();
			System.out.println("Song stopped");
		}
		
		if(command != null && command.equals("playlistadd")){
			String media = null;
			media = request.getParameter("option");
			if( media != null ){
				String mrl = dossier+media;
				remote.playListAdd(mrl);
				remote.printPlayList(response);
			}
		}
		
		if(command != null && command.equals("playlistremove")){
			int index = -1;
			index = Integer.parseInt(request.getParameter("option"));
			if( index >= 0 ){
				remote.playListRemove(index);
				remote.printPlayList(response);
			}
		}
		
		if(command!=null && command.equals("next")){
			remote.next();
		}
		
		if(command!=null && command.equals("previous")){
			remote.previous();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
