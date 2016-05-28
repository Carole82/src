import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class LabyrintheImpl2 extends UnicastRemoteObject implements Labyrinthe {

	protected LabyrintheImpl2() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		try {
			p = (Persistance) Naming.lookup("rmi://localhost:1099/Pers");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private Persistance p;
	private int idServeur = 2;
	
	//Serveur
	private HashMap<String, Piece> lesPieces = new HashMap<String, Piece>();
	private HashMap<String, Personnage> lesPersonnages = new HashMap<String, Personnage>();
	
	//CLient
	private HashMap<String, LabyrintheNotification> notifications = new HashMap<String, LabyrintheNotification>();
	
	public void initLabyrinthe() {
		//Création des pièces
		lesPieces.put("A1", new Piece("A1", new Monstre("Gravalanche"), idServeur, true));
		lesPieces.put("A2", new Piece("A2", new Monstre("Virgo"), idServeur, false));
		lesPieces.put("A3", new Piece("A3", new Monstre("Wizzel"), idServeur, false));
		lesPieces.put("B1", new Piece("B1", new Monstre("Zabro"), idServeur, true));
		lesPieces.put("B2", new Piece("B2", new Monstre("Stanis"), idServeur, false));
		lesPieces.put("B3", new Piece("B3", new Monstre("Logos"), idServeur, false));
		lesPieces.put("C1", new Piece("C1", new Monstre("Saturos"), idServeur, true));
		lesPieces.put("C2", new Piece("C2", new Monstre("Vaglor"), idServeur, false));
		lesPieces.put("C3", new Piece("C3", new Monstre("Aeron"), idServeur, false));
	}
	
	public boolean getLiensServeur (int pServeur, String pPosition) {
		if (pPosition == "A1" || pPosition == "B1" || pPosition == "C1")
			return true;
		else
			return false;
	}
	
	public HashMap<String, Piece> getLesPieces() {
		return lesPieces;
	}
	
	public void creerLiens(HashMap<String, Piece> pLab)
	{
		//lesPieces.get("Test").setLiens(pN, pS, pE, pO);
		lesPieces.get("A1").setLiens(null, lesPieces.get("B1"), lesPieces.get("A2"), pLab.get("A3"));
		lesPieces.get("A2").setLiens(null, lesPieces.get("B2"), lesPieces.get("A3"), lesPieces.get("A1"));
		lesPieces.get("A3").setLiens(null, lesPieces.get("B3"), null, lesPieces.get("A2"));
		lesPieces.get("B1").setLiens(lesPieces.get("A1"), lesPieces.get("C1"), lesPieces.get("B2"), pLab.get("B3"));
		lesPieces.get("B2").setLiens(lesPieces.get("A2"), lesPieces.get("C2"), lesPieces.get("B3"), lesPieces.get("B1"));
		lesPieces.get("B3").setLiens(lesPieces.get("A3"), lesPieces.get("C3"), null, lesPieces.get("B2"));
		lesPieces.get("C1").setLiens(lesPieces.get("B1"), null, lesPieces.get("C2"), pLab.get("C3"));
		lesPieces.get("C2").setLiens(lesPieces.get("B2"), null, lesPieces.get("C3"), lesPieces.get("C1"));
		lesPieces.get("C3").setLiens(lesPieces.get("B3"), null, null, lesPieces.get("C2"));
	}
	
	public Joueur seDeplacer(Joueur j, Piece pieceCourante, char direction) throws RemoteException {
		// TODO Auto-generated method stub
		boolean deplace = false ;

		int nbJoueurs;
		System.out.println(pieceCourante.toString());
		int pvM = pieceCourante.getLeMonstre().getPv();
		
		if (direction == 'N' && pieceCourante.getN() != null){ 
			j.setPosition(pieceCourante.getN());
			deplace = true;
		}
		else if (direction == 'S' && pieceCourante.getS() != null){ 
			j.setPosition(pieceCourante.getS());
			deplace = true;
		}
		else if (direction == 'E' && pieceCourante.getE() != null){ 
			j.setPosition(pieceCourante.getE());
			deplace = true;
		}
		else if (direction == 'O' && pieceCourante.getO() != null){ 
			j.setPosition(pieceCourante.getO());
			deplace = true;
		}
		
		if (deplace == true) 
		{
			pieceCourante = j.getPosition();
			pieceCourante.getLesJoueurs().add(j);
			nbJoueurs = j.getPosition().getLesJoueurs().size();
			
			System.out.println("Vous êtes maintenant dans la pièce " + pieceCourante.getIdPiece());
			
			if (nbJoueurs == 1){ //Seul dans la pièce
				
				System.out.println("Attention ! Il y a un monstre ! ");
			    Thread t = new Thread(new Combat(pieceCourante, j, pieceCourante.getLeMonstre()));
			    t.start();
			}
			else if (nbJoueurs != 1)
			{
				Scanner sc = new Scanner(System.in);
				//Lui dire qu'il s'est déplacé, l'état de la pièce
			    if(pvM != 0)
			    {
			    	System.out.print("Il y a actuellement 1 monstre et ");
			    }
			    System.out.println(nbJoueurs+" joueur(s) présent(s) dans la pièce : ");
			    
			    for(int i = 1; i <= nbJoueurs; i++)
			    {
			      System.out.println( i + " - " + pieceCourante.getLesJoueurs().get(i).getNom());
			    }
			    
			    System.out.println("Que souhaitez-vous faire?");
			    System.out.println("1 - Fuir"); 
			    System.out.println("2 - Attaquer un joueur"); //Envoyer une notif pour dire qu'un joueur arrive
			    if(pvM != 0)
			    {
			    	System.out.println("3 - Attaquer le monstre");
			    }		    
			    String rep = sc.nextLine();
			    sc.close();
			    if (rep == "1")
			    {
			    	System.out.print("Quelle direction voulez-vous prendre? (N pour Nord, S pour Sud, E pour Est, O pour Ouest)");
			    	String repDirection = sc.nextLine();
			    	char repDChar = repDirection.charAt(0);
			    	//seDeplacer(j, repDChar);
			    }
			    if (rep == "2")
			    {
			    	System.out.print("Entrez le nom du joueur que vous souhaitez attaquer : ");
			    	String repJ = sc.nextLine();
			    	//On récupère le joueur qui à ce nom "repJ"
			    	Joueur joueurAttaque = null;
			    	
			    	int i = 1;
			    	while (i<= nbJoueurs && joueurAttaque == null)
			    	{
			    		if(pieceCourante.getLesJoueurs().get(i).getNom() == repJ){
					    	  joueurAttaque = pieceCourante.getLesJoueurs().get(i);
					    }
			    		i++;
			    	}
			    	
				    Thread t = new Thread(new Combat(pieceCourante, j, joueurAttaque));
				    t.start();
			    }
			    if (rep == "3" && pvM != 0)
			    {
				    Thread t = new Thread(new Combat(pieceCourante, j, pieceCourante.getLeMonstre()));
				    t.start();
			    }
		    }
		}
		return j;
	}

	public void envoyerMessage(Joueur emmeteur, Joueur recepteur, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
	}


	public Joueur creerJoueur(String nomJoueur, String mdp)
			throws RemoteException {
		// TODO Auto-generated method stub
		//Appel à la BD
		if (p.creerJoueur(nomJoueur, mdp)) {
			//Création du joueur en local
			Joueur j = new Joueur(nomJoueur, mdp, "A1", 1);
			return j;
		}
		else {
			return null;
		}
	}


	public Joueur seConnecter(String nomJoueur, String mdp)
			throws RemoteException {
		// TODO Auto-generated method stub
		return p.seConnecter(nomJoueur, mdp);
	}


	public boolean quitterPartie(Joueur j) throws RemoteException {
		// TODO Auto-generated method stub
		return p.quitterPartie(j);
	}
	
	
	public void enregistrerNotification(String id, LabyrintheNotification b,
			double minimum) throws RemoteException {
		// TODO Auto-generated method stub
		notifications.put(id, b);
		
		//Rajouter déplacer, combat, discussion !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	public void enleverNotification(String id) throws RemoteException {
		// TODO Auto-generated method stub
		notifications.remove(id);
	}
	
	
	public static void main(String[] args) throws Exception {
		//LocateRegistry.createRegistry(1099);
		 Naming.rebind("Labi", new LabyrintheImpl2());
	}
}
