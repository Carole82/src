import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface Labyrinthe extends Remote {
	
	//Action de jeux
	public boolean seDeplacer(Joueur j, char direction) throws RemoteException;
	
	public void attaquer (Joueur j, Personnage p) throws RemoteException;
	
	public void envoyerMessage(Joueur emmeteur, Joueur recepteur, String message) throws RemoteException;
	
	public void initLabyrinthe() throws RemoteException;
	
	public void creerLiens(HashMap<String, Piece> pLab) throws RemoteException;

	//Persistance
	public Joueur creerJoueur (String nomJoueur, String mdp) throws RemoteException;
	
	public Joueur seConnecter (String nomJoueur, String mdp) throws RemoteException;
	
	public boolean quitterPartie(Joueur j) throws RemoteException;
}
