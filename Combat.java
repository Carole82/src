import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

public class Combat implements Runnable{
	
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


	@Override
	public void run() {
		// TODO Auto-generated method stub
		p.attaquer(j, perso);
	}
}
