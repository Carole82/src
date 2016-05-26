/*
 * A la création d'un joueur (constructeur), faire un setPosition pour affecter une piece
 */

public class Joueur extends Personnage {

	private String mdp;
	private int serveur;
	
	public Joueur(String pN, String pMdp, String pIdPosition, int pServeur) {
		super(pN, PV_MAX_JOUEUR, PV_MAX_JOUEUR, pIdPosition);
		setMdp(pMdp);
		setServeur(pServeur);
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public String getMdp() {
		return mdp;
	}
	
	public int getServeur() {
		return serveur;
	}
	
	public void setServeur(int serveur) {
		this.serveur = serveur;
	}
}
