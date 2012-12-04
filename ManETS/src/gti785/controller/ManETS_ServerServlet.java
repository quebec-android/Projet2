package gti785.controller;

import gti785.model.MediaFolder;
import gti785.param.Const;
import gti785.push.Push;
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
	private static Push server ; 
    /**
     * Default constructor. Instanciates objects artwork, mediaFolder and remote
     */
    public ManETS_ServerServlet() {
    	try {
    		server = new Push();
    		server.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	mediaFolder = MediaFolderFactory.getInstance().build();
    	remote = new ETSRemote(mediaFolder, server);
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
		System.out.println(command);
		
		//get all media list
		if(command != null && command.equals("getList")){
			XMLprinter.printMedia(mediaFolder.getFiles(), response);
		}
		
		//play song
		else if(command != null && command.equals("play")){
			String id = null;
			id = request.getParameter("option");
			int idPlaylist = 0;
			if (id != null) {
				idPlaylist = Integer.parseInt(id);
			}
			if(remote.play(idPlaylist)){
				System.out.println("Song in play");
				XMLprinter.printAfterPlay(""+idPlaylist, response);
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
		
		//toBeginning
		else if(command!=null && command.equals("toBeginning")){
			remote.toBeginning();
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
		else if(command != null && command.equals("getPlayList")){
			XMLprinter.printPlaylist(remote.getPlaylist(),response);
		}
		
		//get information on current song
		else if(command != null && command.equals("poll")){
			int songPlayListID = remote.getCurrentSongPlaylistID();
			response.getWriter().write("current song: " + songPlayListID);
		}
		
		//set volume
		else if(command != null && command.equals("volume")){
			String volume = request.getParameter("option");
			if( volume != null){
				remote.changeVolume(Integer.parseInt(volume));
				System.out.println("volume: "+volume);
			}
			else{
				error = true;
				errorMessage = "Volume: no option";
			}
		}
		
		//get information on current song
		else if(command != null && command.equals("setStream")){
			String mode = null;
			mode = request.getParameter("option");
			if( mode != null){
				if (mode.equals("0")) {
					remote.setStreamingMode(false);
					XMLprinter.printPort(Const.STREAMING_PORT,response);
					response.setStatus(HttpServletResponse.SC_OK);
				}else if (mode.equals("1")) {
					remote.setStreamingMode(true);
					XMLprinter.printPort(Const.STREAMING_PORT,response);
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
					response.getWriter().write("Streaming mode: no option");
				}
			} else {
				error = true;
				errorMessage = "Streaming mode: no option";
			}
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
	
	public void printResponse(HttpServletResponse response) {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
