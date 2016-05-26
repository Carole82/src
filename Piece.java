import java.util.ArrayList;
import java.util.HashMap;


public class Piece {
	
	private HashMap<String,Piece> lien;
	private String idPiece;
	private ArrayList<Joueur> lesJoueurs;
	private Monstre leMonstre;
	private int serveur;
	private boolean chgtServeur;
	
	public Piece (String pIdPiece, String pNomM, int pServeur, boolean pChgtServeur) {
		setIdPiece(pIdPiece);
		// Rentrer des pièces null
		lien = new HashMap<String, Piece>();
		lesJoueurs = new ArrayList<Joueur>();
		leMonstre = new Monstre(pNomM);
		serveur = pServeur;
		chgtServeur = pChgtServeur;
	}
	
	public void setN(Piece pN)
	{
		lien.put("N", pN);
	}

	public void setS(Piece pS)
	{
		lien.put("S", pS);
	}
	
	public void setE(Piece pE)
	{
		lien.put("E", pE);
	}
	
	public void setO(Piece pO)
	{
		lien.put("O", pO);
	}
	
	public void setLiens(Piece pN, Piece pS, Piece pE, Piece pO)
	{
		this.setN(pN);
		this.setS(pS);
		this.setE(pE);
		this.setO(pO);
	}
	
	public Piece getN() {
		return lien.get("N");
	}
	
	public Piece getS() {
		return lien.get("S");
	}
	
	public Piece getO() {
		return lien.get("O");
	}
	
	public Piece getE() {
		return lien.get("E");
	}
	
	public String getIdPiece() {
		return idPiece;
	}

	public void setIdPiece(String idPiece) {
		this.idPiece = idPiece;
	}

	public Monstre getLeMonstre() {
		return leMonstre;
	}

	public void setLeMonstre(Monstre leMonstre) {
		this.leMonstre = leMonstre;
	}
	
	public ArrayList<Joueur> getLesJoueurs() {
		return lesJoueurs;
	}

	public void setLesJoueurs(ArrayList<Joueur> lesJoueurs) {
		this.lesJoueurs = lesJoueurs;
	}
	
	public int getServeur() {
		return serveur;
	}
	
	public void setServeur(int serveur) {
		this.serveur = serveur;
	}
	
	public boolean getChgtServeur() {
		return chgtServeur;
	}
	
	public void setChgtServeur(boolean chgtServeur) {
		this.chgtServeur = chgtServeur;
	}
	
	
	
}
