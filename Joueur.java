/*
 * A la cr�ation d'un joueur (constructeur), faire un setPosition pour affecter une piece
 */

public class Joueur extends Personnage {

	private String mdp;
	
	public Joueur(String pN, String pMdp, String pIdPosition) {
		super(pN, PV_MAX_JOUEUR, PV_MAX_JOUEUR, pIdPosition);
		setMdp(pMdp);
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public String getMdp() {
		return mdp;
	}
}
