package com.example.client;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Thread qui écoute les push du serveur
 * Permet d'actualiser la chanson qui se joue
 * Permet d'actualiser la progressBar
 * @author Fab
 *
 */
public class ListenerThread extends AsyncTask<Void, Void, Void> {

	private MainActivity mainActivity = null;


	public ListenerThread(MainActivity mainActivity) {
		super();
		this.setMainActivity(mainActivity);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		Socket clientSocket; 
		try {
			clientSocket = new Socket(InetAddress.getByName(Const.IP),8070);	
			if (clientSocket.isConnected()) {
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
				Log.d("Listener Thread", "Client : connexion ok");
				while(true) {
					String msg = (String)in.readObject();
					if (msg!=null) {
						Log.d("Listener Thread", "Message reçu du client : "+msg);
						DoPushMessage(msg);
					}
				} 
			} else {
				Log.e("Listener Thread", "Le serveur ne gère pas les PUSH :(");
			}
		}catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	public void DoPushMessage(String message) {
		if (message.contains(Const.ID)) {
			Song s = mainActivity.getPlaylist().get(Integer.parseInt(message.substring(message.indexOf(":")+1)));
			mainActivity.setTmpID(Integer.parseInt(message.substring(message.indexOf(":")+1)));
			mainActivity.hilightPlayedSong();
			mainActivity.setPlayingSongID(Integer.parseInt(message.substring(message.indexOf(":")+1)));
			mainActivity.play();
			if (mainActivity.getProgressBarThread() != null) { 
				mainActivity.getProgressBarThread().cheat();
				mainActivity.getProgressBarThread().cancel(true);
				mainActivity.setProgressBarThread(null);
			}
			mainActivity.setProgressBarThread(new ProgressBarThread(mainActivity));
			mainActivity.getProgressBarThread().setMax(s.getLength());
			mainActivity.getProgressBarThread().execute();
		} else if (message.contains(Const.END)) {
			mainActivity.stop();
		}
	}

	public MainActivity getMainActivity() {
		return mainActivity;
	}


	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
}
