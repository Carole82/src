import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class LabyrintheNotificationImpl extends UnicastRemoteObject implements LabyrintheNotification {
	String id;

	protected LabyrintheNotificationImpl(String id) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	public void notification(String message) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

}
