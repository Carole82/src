import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Labyrinthe2 {

	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		//N, S, E, O
		
		//LocateRegistry.createRegistry(1099);
		try {
			Naming.rebind("Labi2", new LabyrintheImpl(2));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Création des pièces
		Piece p1 = new Piece("A4", new Monstre("Montre1"), 1, false);
		Piece p2 = new Piece("A5", new Monstre("Montre2"), 1, false);
		Piece p3 = new Piece("A6", new Monstre("Montre3"), 1, false);
		Piece p4 = new Piece("B4", new Monstre("Montre4"), 1, false);
		Piece p5 = new Piece("B5", new Monstre("Montre5"), 1, false);
		Piece p6 = new Piece("B6", new Monstre("Montre6"), 1, false);
		Piece p7 = new Piece("C4", new Monstre("Montre7"), 1, false);
		Piece p8 = new Piece("C5", new Monstre("Montre8"), 1, false);
		Piece p9 = new Piece("C6", new Monstre("Montre9"), 1, true);
		
		Piece.lesPieces.put("A4", p1);
		Piece.lesPieces.put("A5", p2);
		Piece.lesPieces.put("A6", p3);
		Piece.lesPieces.put("B4", p4);
		Piece.lesPieces.put("B5", p5);
		Piece.lesPieces.put("B6", p6);
		Piece.lesPieces.put("C4", p7);
		Piece.lesPieces.put("C5", p8);
		Piece.lesPieces.put("C6", p9);
		System.out.println(Piece.lesPieces.size());
		
		//Création des liens N ; S ; E ; O
		p1.setLiens(null, p4, p2, null); //O
		p2.setLiens(null, p5, p3, p1);
		p3.setLiens(null, p6, null, p2);
		p4.setLiens(p1, p7, p5, null); //O
		p5.setLiens(p2, p8, p6, p4);
		p6.setLiens(p3, p9, null, p5); 
		p7.setLiens(p4, null, p8, null); //O
		p8.setLiens(p5, null, p9, p7);
		p9.setLiens(p6, null, null, p8);
	}

}
