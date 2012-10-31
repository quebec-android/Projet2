package gti785.multi;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.jna.NativeLibrary;

/**
 * Servlet implementation class MultiServlet
 */
public class ManETS_ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ManETS_ServerServlet() {
    	NativeLibrary.addSearchPath(
		RuntimeUtil.getLibVlcLibraryName(), "EMPLACEMENT DU DOSSIER QUI CONTIENT libvlc"
		);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("command");
		
		if(command.equals("getList")){
			
		}
		
		if(command.equals("play")){
			
		}
		
		if(command.equals("play")){
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
