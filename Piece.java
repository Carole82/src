import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Piece implements Serializable {
	
	private HashMap<String,Piece> lien;
	private String idPiece;
	private ArrayList<Joueur> lesJoueurs;
	private Monstre leMonstre;
	private int serveur;
	private boolean chgtServeur;
	private Piece lienServeur;
	private LabyrintheNotification notif;
	
	public static HashMap<String, Piece> lesPieces = new HashMap<String, Piece>();
	
	public Piece (String pIdPiece, Monstre pMonstre, int pServeur, boolean pChgtServeur) {
		setIdPiece(pIdPiece);
		// Rentrer des pièces null
		lien = new HashMap<String, Piece>();
		lesJoueurs = new ArrayList<Joueur>();
		leMonstre = pMonstre;
		serveur = pServeur;
		chgtServeur = pChgtServeur;
		lienServeur = null;
	}
	
	public boolean changementServeur (String pDir) {
		if ((pDir == "E" && serveur == 1 && chgtServeur == true)
				|| (pDir == "O" && serveur == 2 && chgtServeur == true))
			return true;
		else
			return false;
	}
	
	public void setPiece(String pDir, Piece p) {
		lien.put(pDir, p);
	}
	
	public void setLiens(Piece pN, Piece pS, Piece pE, Piece pO)
	{
		this.setPiece("N", pN);
		this.setPiece("S", pS);
		this.setPiece("E", pE);
		this.setPiece("O", pO);
	}

	
	public Piece getPiece(String pDir) {
		return lien.get(pDir);
	}
	
	public HashMap<String, Piece> getLien() {
		return lien;
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
	
	public void setNotification(LabyrintheNotification pNotif) {
		this.notif = pNotif;
	}
	
	public void setLienServeur(Piece lienServeur) {
		this.lienServeur = lienServeur;
	}
}
