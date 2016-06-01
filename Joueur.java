import java.rmi.RemoteException;

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
	
	public void setPosition(Piece position) {
		super.setPosition(position);
		getPosition().getLesJoueurs().add(this);
	}
	
	public String test_deplacement(String pDir) {
		String rep = null;
		if (getPosition().getPiece(pDir) != null) {
			rep = "deplacement";
		}
		if(getPosition().changementServeur(pDir)) {
			rep = "changement serveur";
		}
		return rep;
	}
	
	public void seDeplacer (String pDir)  {
		getPosition().getLesJoueurs().remove(this);
		setPosition(getPosition().getPiece(pDir));
		System.out.println("Vous êtes maintenant dans la pièce " + getIdPosition());
	}
	
	public void attaquer (Personnage pP) {
		Combat c = new Combat(getPosition(), this, pP);
	    Thread t = new Thread(c);
	    Combat.lesCombats.put(getNom(), t);
	    t.start();
	    while (!t.isInterrupted()){   	
	    }
	    Combat.lesCombats.remove(t);
	    setPv(c.getJ().getPv());
	    getPosition().getLeMonstre().setPv(c.getPerso().getPv());
	}
	
	public void fuir() {
		if (Combat.lesCombats.containsKey(getNom())) {
			Combat.lesCombats.get(getNom()).interrupt();
			Combat.lesCombats.remove(getClass());
			System.out.println("Vous avez interrompu votre combat!");
		}
	}
	
	public void gagner() {
		setPv(getPvMax() + 1);
	}
	
	public void mourrir () {
		setPv(PV_MAX_JOUEUR);
		getPosition().getLesJoueurs().remove(this);
		setPosition(Piece.lesPieces.get("A1"));
		getPosition().getLesJoueurs().add(this);
	}
	
	
}
