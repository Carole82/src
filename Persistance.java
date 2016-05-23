import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Persistance extends Remote{
	
	public boolean creerJoueur(String pNom, String pMdp) throws RemoteException;
	public Joueur seConnecter (String nomJoueur, String mdp) throws RemoteException;
	public boolean quitterPartie(Joueur j) throws RemoteException;
	
}
