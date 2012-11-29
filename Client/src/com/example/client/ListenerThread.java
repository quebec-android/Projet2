package com.example.client;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

/**
 * Thread qui écoute les push du serveur
 * Permet d'actualiser la cover de la chanson qui se joue
 * Permet d'actualiser la progressBar
 * @author Fab
 *
 */
public class ListenerThread extends Thread {

	  public void start ()
	    {
	        super.start();
	    }
	 
	 
	    public void run ()
	    {
       
	    	Socket clientSocket; 

			try {
				clientSocket = new Socket(InetAddress.getByName(Const.IP),8070);	
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
				Log.d("Listener Thread", "Client : connexion ok");
				while(true) {
					String msg = (String)in.readObject();
					if (msg!=null) {
						Log.d("Listener Thread", "Message reçu du client : "+msg);
					}
				} 

			}catch (Exception e) { 
				e.printStackTrace();
			}
	    	
      }
  }
