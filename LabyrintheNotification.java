import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LabyrintheNotification extends Remote {
	/*
	 * Interface permettant � LabyrintheImpl (Client) de donner l'�tat de la pi�ce
	 * � LabyrintheClient (Serveur).
	 * Etat de la pi�ce
	 * Discussion
	 */
	
	public void notification(String message) throws RemoteException;
}
