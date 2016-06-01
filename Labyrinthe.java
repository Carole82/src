import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface Labyrinthe extends Remote {
	
	//Action de jeux
	
	public void envoyerMessage(Joueur emmeteur, Joueur recepteur, String message) throws RemoteException;

	//Connexion
	public Joueur connecterServeur (Joueur j, LabyrintheClient client, int cas) throws RemoteException;
	
	public void deconnecterServeur(String nomJoueur) throws RemoteException;
	
	//Persistance
	public Joueur creerJoueur (String nomJoueur, String mdp) throws RemoteException;
	
	public Joueur seConnecter (String nomJoueur, String mdp) throws RemoteException;
	
	public boolean quitterPartie(Joueur j) throws RemoteException;
	
	//Notification
	
	public void enregistrerNotification(String id, LabyrintheNotification l, double minimum) throws RemoteException;
	//double minimum ==> Pas besoin??
	
	public void enleverNotification(String id) throws RemoteException;
}
