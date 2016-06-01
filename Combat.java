import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Combat implements Runnable{
	
	public static HashMap<String, Thread> lesCombats = new HashMap<String, Thread>();
	
	//private LabyrintheImpl lab;
	private Labyrinthe lab;
	private Piece p;
	private Joueur j;
	private Personnage perso;	

	  public Combat(Piece p, Joueur j, Personnage perso){
	    this.p = p;
	    this.j = j;
	    this.perso = perso;
	  }


	public void run() {
		// TODO Auto-generated method stub
		lesCombats.put(perso.getNom(), new Thread(this));
		combattre();
	}
	
	public void combattre () {
		Random randomno = new Random();
		do{	
			//Obtenir alatoirement true ou false
			//MANQUE LE FAIT QUE CA SOIT TOUTES LES SECONDES!!!
			boolean value = randomno.nextBoolean();
			
			if(value == true){
				j.decrementerPv();
			}
			else {
				perso.decrementerPv();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }while(j.getPv() != 0 && perso.getPv() != 0);
	}
	
	public Joueur getJ() {
		return j;
	}
	
	public Labyrinthe getLab() {
		return lab;
	}
	
	public Piece getP() {
		return p;
	}
	
	public Personnage getPerso() {
		return perso;
	}
	
}
