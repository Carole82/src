import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LabyrintheNotification extends Remote {
	/*
	 * Interface permettant à LabyrintheImpl (Client) de donner l'état de la pièce
	 * à LabyrintheClient (Serveur).
	 * Etat de la pièce
	 * Discussion
	 */
	
	public void notification(String message) throws RemoteException;
}
