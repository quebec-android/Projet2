package gti785.multi;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.caprica.vlcj.medialist.MediaListItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Servlet implementation class MultiServlet
 */
public class ManETS_ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ETSRemote remote;
	private String dossier = "/Users/Cedric/Documents/Quebec/Cours/GTI785/Lab/Lab 02/Media/";
	/*
	 * fabien "C:\Users\Fab\Desktop\COURS\785\android\Projet2\music"
	 * cedric "/Users/Cedric/Documents/Quebec/Cours/GTI785/Lab/Lab 02/Media/"
	 * 
	 */
	private String dossierImage = "/Users/Cedric/Documents/Quebec/Cours/GTI785/Lab/Lab 02/Projet2/ManETS/WebContent/Artwork";
	private MediaFolder mediaFolder;
	private ArtworkFolder artwork;
	
	private static final XStream xstream = new XStream(new DomDriver());
	
	// Configuration de XStream
	static {
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("song", Media.class);
		xstream.alias("list", List.class);
		xstream.alias("playlist", MediaListItem.class);
	}
	
    /**
     * Default constructor. 
     */
    public ManETS_ServerServlet() {
    	artwork = new ArtworkFolder(new File(dossierImage));
    	mediaFolder = new MediaFolder(new File(dossier), artwork);
    	remote = new ETSRemote(mediaFolder);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		boolean error = false;
		String errorMessage = null;
		
		String command = null;
		command = request.getParameter("command");
		
		if(command != null && command.equals("getList")){
			mediaFolder.print(response, xstream);
		}
		
		else if(command != null && command.equals("play")){
			String id = null;
			id = request.getParameter("option");
			int idPlaylist = -1;
			if (id != null) {
				idPlaylist = Integer.parseInt(id);
			}
			if(remote.play(idPlaylist)){
				System.out.println("Song in play");
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else{
				error = true;
				errorMessage = "Play : Error while trying to play song.";
			}
		}
		
		else if(command != null && command.equals("pause")){
			remote.pause();
			System.out.println("Song paused");
		}
		
		else if(command != null && command.equals("stop")){
			remote.stop();
			System.out.println("Song stopped");
		}
		
		else if(command != null && command.equals("playlistadd")){
			String idSong = null;
			idSong = request.getParameter("option");
			System.out.println("Received parameters: " + idSong);
			if( idSong != null ){
				if(remote.playListAdd(Integer.parseInt(idSong)))
					remote.printPlayList(response, xstream);
				else{
					error = true;
					errorMessage = "Play list add: Song does not exist";
				}
			}
			else{
				error = true;
				errorMessage = "Play list add: no option";
			}
			
		}
		
		else if(command != null && command.equals("playlistremove")){
			String idPlaylist = null;
			idPlaylist = request.getParameter("option");
			if( idPlaylist != null ){
				if(remote.playListRemove(Integer.parseInt(idPlaylist)))
					remote.printPlayList(response, xstream);
				else{
					error = true;
					errorMessage = "Play list remove: Song does not exist";
				}
				
			}else{
				error = true;
				errorMessage = "Play list remove: no option";
			}
			
		}
		
		else if(command!=null && command.equals("next")){
			remote.next();
		}
		
		else if(command!=null && command.equals("previous")){
			remote.previous();
		}
		
		else if(command != null && command.equals("shuffle")){
			remote.shuffle();
			remote.printPlayList(response, xstream);
		}
		
		else if(command != null && command.equals("repeat")){
			String mode = null;
			mode = request.getParameter("option");
			if( mode != null){
				if(remote.repeat(mode))
					System.out.println("repeat mode: "+mode);
				else{
					error = true;
					errorMessage = "Repeat mode: repeat mode does not exist";
				}
			}
			else{
				error = true;
				errorMessage = "Repeat mode: no option";
			}
		}
		
		else if(command != null && command.equals("GetPlayList")){
			remote.printPlayList(response, xstream);
		}
		
		else if(command != null && command.equals("poll")){
			int songPlayListID = remote.getCurrentSongPlaylistID();
			
		}
		
		else{
			response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
			response.getWriter().write("Method does not exist");
			System.out.println("Method does not exist");
		}
		
		if(error){
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.getWriter().write(errorMessage);
			System.out.println(errorMessage);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
