import java.rmi.Remote;


public interface Persistance extends Remote{
	
	public boolean creerJoueur(String pNom, String pMdp) ;
	public Joueur seConnecter (String nomJoueur, String mdp);
	public boolean quitterPartie(Joueur j);
	
}
