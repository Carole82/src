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
	private LabyrintheNotification notif;
	
	public Piece (String pIdPiece, Monstre pMonstre, int pServeur, boolean pChgtServeur) {
		setIdPiece(pIdPiece);
		// Rentrer des pièces null
		lien = new HashMap<String, Piece>();
		lesJoueurs = new ArrayList<Joueur>();
		leMonstre = pMonstre;
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
	
	public void setNotification(LabyrintheNotification pNotif) {
		this.notif = pNotif;
	}
	
	public void attaquer(Joueur j, Personnage p) {
		//Cration d'un random object pour savoir lequel des 2 personnages va perdre une vie
		 Random randomno = new Random();
		      
		do{	
			//Obtenir alatoirement true ou false
			boolean value = randomno.nextBoolean();
			
			if(value == true){
				j.decrementerPv();
			}
			else {
				p.decrementerPv();
			}
			System.out.println("Pv " + j.getNom() + " : " + j.getPv());
			System.out.println("Pv " + p.getNom() + " : " + p.getPv());
			
		  }while(j.getPv() != 0 && p.getPv() != 0); //Crer mthode fuir?
		
		//Lorsque le joueur perd, il meurt et le vainqueur gagne 1 point de vie
		if(j.getPv() == 0)
		{
			if (notif != null) {
				try {
					notif.notification("Oh non vous avez perdu !");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			p.setPvMax(p.getPvMax()+1);
			p.setPv(p.getPvMax());
		}
		//Lorsque le joueur gagne, il gagne 1 point de vie et continue le jeu
		else if(p.getPv() == 0)
		{
			if (notif != null) {
				try {
					notif.notification("Bravo, vous avez gagné ! ");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			j.setPvMax(j.getPvMax()+1);
			j.setPv(j.getPvMax());
		}
	}
}
