package gti785.controller;

import gti785.model.MediaFolder;
import gti785.remote.ETSRemote;
import gti785.view.PrintXML;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MultiServlet
 */
public class ManETS_ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ETSRemote remote;
	
	private MediaFolder mediaFolder;
	private PrintXML XMLprinter;
	
    /**
     * Default constructor. Instanciates objects artwork, mediaFolder and remote
     */
    public ManETS_ServerServlet() {
    	mediaFolder = MediaFolderFactory.getInstance().build();
    	remote = new ETSRemote(mediaFolder);
    	XMLprinter = new PrintXML();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		boolean error = false;
		String errorMessage = null;
		
		String command = null;
		command = request.getParameter("command");
		
		//get all media list
		if(command != null && command.equals("getList")){
			XMLprinter.printMedia(mediaFolder.getFiles(), response);
		}
		
		//play song
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
		
		//pause song
		else if(command != null && command.equals("pause")){
			remote.pause();
			System.out.println("Song paused");
		}
		
		//stop song
		else if(command != null && command.equals("stop")){
			remote.stop();
			System.out.println("Song stopped");
		}
		
		//add song to playlist
		else if(command != null && command.equals("playlistadd")){
			String idSong = null;
			idSong = request.getParameter("option");
			System.out.println("Received parameters: " + idSong);
			if( idSong != null ){
				if(remote.playListAdd(Integer.parseInt(idSong)))
					XMLprinter.printPlaylist(remote.getPlaylist(),response);
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
		
		//remove song from playlist
		else if(command != null && command.equals("playlistremove")){
			String idPlaylist = null;
			idPlaylist = request.getParameter("option");
			if( idPlaylist != null ){
				if(remote.playListRemove(Integer.parseInt(idPlaylist)))
					XMLprinter.printPlaylist(remote.getPlaylist(),response);
				else{
					error = true;
					errorMessage = "Play list remove: Song does not exist";
				}
				
			}else{
				error = true;
				errorMessage = "Play list remove: no option";
			}
			
		}
		
		//play next song
		else if(command!=null && command.equals("next")){
			remote.next();
		}
		
		//play previous song
		else if(command!=null && command.equals("previous")){
			remote.previous();
		}
		
		//shuffle play list
		else if(command != null && command.equals("shuffle")){
			remote.shuffle();
			XMLprinter.printPlaylist(remote.getPlaylist(),response);
		}
		
		//repeat action
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
		
		//print play list songs
		else if(command != null && command.equals("GetPlayList")){
			XMLprinter.printPlaylist(remote.getPlaylist(),response);
		}
		
		//get information on current song
		else if(command != null && command.equals("poll")){
			int songPlayListID = remote.getCurrentSongPlaylistID();
			response.getWriter().write("current song: " + songPlayListID);
		}
		
		//return not implemented
		else{
			response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
			response.getWriter().write("Method does not exist");
			System.out.println("Method does not exist");
		}
		
		//print errors
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
		doGet(request, response);
	}

}
