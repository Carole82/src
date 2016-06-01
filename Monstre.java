/*
 * A la création d'un monstre (constructeur), faire un setPosition pour affecter une piece
 */

public class Monstre extends Personnage{

	private boolean vivant;
	public Monstre(String pN) {
		super(pN, PV_MAX_MONSTRE, PV_MAX_MONSTRE, null);
		vivant = true;
	}
	
	public void revivre() {
		setPv(PV_MAX_MONSTRE);
		vivant = true;
	}
	
	public void mourrir () {
		vivant = false;
	}
	
	public boolean getVivant() {
		return vivant;
	}
	
}
