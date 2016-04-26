import java.util.ArrayList;
import java.util.HashMap;


public class Piece {
	
	private HashMap<String,Piece> lien;
	private String idPiece;
	private ArrayList<Joueur> lesJoueurs;
	private Monstre leMonstre;
	
	
	public Piece (String pIdPiece, String pNomM) {
		setIdPiece(pIdPiece);
		
		// Rentrer des pi�ces null
		lien = new HashMap<String, Piece>();

		lesJoueurs = new ArrayList<Joueur>();
		setLeMonstre(new Monstre(pNomM));
		
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
}