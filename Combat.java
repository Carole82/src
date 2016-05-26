import java.rmi.RemoteException;

public class Combat implements Runnable{
	
	//private LabyrintheImpl lab;
	private Labyrinthe lab;
	private Piece p;
	private Joueur j;
	private Personnage perso;
	

	  public Combat(Labyrinthe lab, Piece p, Joueur j, Personnage perso){
	    this.lab = lab;
	    this.p = p;
	    this.j = j;
	    this.perso = perso;
	  }
	  
	public void run() {
		// TODO Auto-generated method stub
		try {
			lab.attaquer(j, perso);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
