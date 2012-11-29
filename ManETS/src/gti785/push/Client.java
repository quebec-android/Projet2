package gti785.push;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe pour tester si le serveur de Push est fonctionnel
 * @author Fab
 *
 */
public class Client {

	public static void main(String[] zero) {

		Socket clientSocket;

		try {

			clientSocket = new Socket(InetAddress.getByName("127.0.0.1"),8070);	
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Client : connexion ok");
			while(true) {
				String msg = (String)in.readObject();
				if (msg!=null) {
					System.out.println("Message reçu du client : "+msg);
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
