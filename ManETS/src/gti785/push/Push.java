package gti785.push;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread du serveur de Push
 * Le dernier client qui se connecte va recevoir les push
 * Permet d'envoyer un message au client sans fermer la connexion
 * @author Fab
 *
 */
public class Push extends Thread{

	private static ServerSocket server  ;
	private Socket connectSocket ;
	private ObjectOutputStream out;
	
	
    public void start ()
    {
    	try {
			server = new ServerSocket(8070);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	super.start();
    }
 
 
    public void run ()
    {
    	while(true) {
    		try {
				connectSocket = server.accept();
				connectSocket.setKeepAlive(true);
				out = new ObjectOutputStream(connectSocket.getOutputStream());
				out.flush();
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
			
		} 
    }
	
    /**
     * Envoie un message au client
     * @param message
     */
	public void pushMessage(String prefixe, String message) {
		try {
			if (connectSocket != null && !connectSocket.isClosed() ) {
				if (prefixe != null) {
					out.writeObject(prefixe+":"+message);
					System.out.println("Serveur> "+prefixe+":"+message);
				} else {
					out.writeObject(message);
					System.out.println("Serveur> "+message);
				}
				out.flush();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pushMessage(String message) {
		pushMessage(null, message);
	}
}

