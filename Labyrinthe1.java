import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Labyrinthe1 {

	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		//N, S, E, O
//		LocateRegistry.createRegistry(1099);
		try {
			Naming.rebind("Labi1", new LabyrintheImpl(1));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Création des pièces
		Piece p1 = new Piece("A1", new Monstre("Gravalanche"), 1, false);
		Piece p2 = new Piece("A2", new Monstre("Virgo"), 1, false);
		Piece p3 = new Piece("A3", new Monstre("Wizzel"), 1, false);
		Piece p4 = new Piece("B1", new Monstre("Zabro"), 1, false);
		Piece p5 = new Piece("B2", new Monstre("Stanis"), 1, false);
		Piece p6 = new Piece("B3", new Monstre("Logos"), 1, false);
		Piece p7 = new Piece("C1", new Monstre("Saturos"), 1, false);
		Piece p8 = new Piece("C2", new Monstre("Vaglor"), 1, false);
		Piece p9 = new Piece("C3", new Monstre("Aeron"), 1, true);
		
		System.out.println("Test Lab1");
		Piece.lesPieces.put("A1", p1);
		Piece.lesPieces.put("A2", p2);
		Piece.lesPieces.put("A3", p3);
		Piece.lesPieces.put("B1", p4);
		Piece.lesPieces.put("B2", p5);
		Piece.lesPieces.put("B3", p6);
		Piece.lesPieces.put("C1", p7);
		Piece.lesPieces.put("C2", p8);
		Piece.lesPieces.put("C3", p9);
		System.out.println(Piece.lesPieces.size());
		
		//Création des liens
		p1.setLiens(null, p4, p2, null);
		p2.setLiens(null, p5, p3, p1);
		p3.setLiens(null, p6, null, p2); //E
		p4.setLiens(p1, p7, p5, null);
		p5.setLiens(p2, p8, p6, p4);
		p6.setLiens(p3, p9, null, p5); //E
		p7.setLiens(p4, null, p8, null);
		p8.setLiens(p5, null, p9, p7);
		p9.setLiens(p6, null, null, p8); //E
	}

}
