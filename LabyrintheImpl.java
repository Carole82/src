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


public class LabyrintheImpl extends UnicastRemoteObject implements Labyrinthe {

	protected LabyrintheImpl() throws RemoteException {
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
	private int idServeur = 1;
	
	//Serveur
	private HashMap<String, Piece> lesPieces = new HashMap<String, Piece>();
	private HashMap<String, Personnage> lesPersonnages = new HashMap<String, Personnage>();
	
	//CLient
	private HashMap<String, LabyrintheNotification> notifications = new HashMap<String, LabyrintheNotification>();
	
	public void initLabyrinthe() {
		//Création des pièces
		lesPieces.put("A1", new Piece("A1", "Gravalanche", idServeur, false));
		lesPieces.put("A2", new Piece("A2", "Virgo", idServeur, false));
		lesPieces.put("A3", new Piece("A3", "Wizzel", idServeur, true));
		lesPieces.put("B1", new Piece("B1", "Zabro", idServeur, false));
		lesPieces.put("B2", new Piece("B2", "Stanis", idServeur, false));
		lesPieces.put("B3", new Piece("B3", "Logos", idServeur, true));
		lesPieces.put("C1", new Piece("C1", "Saturos", idServeur, false));
		lesPieces.put("C2", new Piece("C2", "Vaglor", idServeur, false));
		lesPieces.put("C3", new Piece("C3", "Aeron", idServeur, true));
	}
	
	public boolean getLiensServeur (int pServeur, String pPosition) {
		if (pPosition == "A3" || pPosition == "B3" || pPosition == "C3")
			return true;
		else
			return false;
	}
	
	public HashMap<String, Piece> getLesPieces() {
		return lesPieces;
	}
	
	public void creerLiens(HashMap<String, Piece> pLab)
	{
		//A modifier !!!! Ajouter tous les liens entre les 2 serveurs
		lesPieces.get("A1").setLiens(null, lesPieces.get("B1"), lesPieces.get("A2"), null);
		lesPieces.get("A2").setLiens(null, lesPieces.get("B2"), lesPieces.get("A3"), lesPieces.get("A1"));
		lesPieces.get("A3").setLiens(null, lesPieces.get("B3"), pLab.get("A1"), lesPieces.get("A2"));
		lesPieces.get("B1").setLiens(lesPieces.get("A1"), lesPieces.get("C1"), lesPieces.get("B2"), null);
		lesPieces.get("B2").setLiens(lesPieces.get("A2"), lesPieces.get("C2"), lesPieces.get("B3"), lesPieces.get("B1"));
		lesPieces.get("B3").setLiens(lesPieces.get("A3"), lesPieces.get("C3"), pLab.get("B1"), lesPieces.get("B2"));
		lesPieces.get("C1").setLiens(lesPieces.get("B1"), null, lesPieces.get("C2"), null);
		lesPieces.get("C2").setLiens(lesPieces.get("B2"), null, lesPieces.get("C3"), lesPieces.get("C1"));
		lesPieces.get("C3").setLiens(lesPieces.get("B3"), null, pLab.get("C1"), lesPieces.get("C2"));
	}
	
	public boolean seDeplacer(Joueur j, char direction) throws RemoteException {
		// TODO Auto-generated method stub
		boolean deplace = false ;
		Piece pieceCourante = new Piece(j.getIdPosition(),j.getPosition().getLeMonstre().getNom(), j.getServeur(), getLiensServeur(j.getServeur(), j.getIdPosition()));
		
		pieceCourante = j.getPosition();
		int nbJoueurs = pieceCourante.getLesJoueurs().size();
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
		if (deplace == true && nbJoueurs == 0){ 
			System.out.println("Vous êtes maintenant dans la pièce "+pieceCourante.getIdPiece());
			System.out.println("Attention ! Il y a un monstre ! ");
			LabyrintheImpl lab = new LabyrintheImpl();
		    Thread t = new Thread(new Combat(lab, pieceCourante, j, pieceCourante.getLeMonstre()));
		    t.start();
		}
		 else if (deplace == true && nbJoueurs != 0){
			 Scanner sc = new Scanner(System.in);
			 //Lui dire qu'il s'est déplacé, l'état de la pièce
		    System.out.println("Vous êtes maintenant dans la pièce "+pieceCourante.getIdPiece());
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
		    if (rep == "1"){
		    	System.out.print("Quelle direction voulez-vous prendre? (N pour Nord, S pour Sud, E pour Est, O pour Ouest)");
		    	String repDirection = sc.nextLine();
		    	char repDChar = repDirection.charAt(0);
		    	seDeplacer(j, repDChar);
		    }
		    /*******Pourquoi pas d'appel à la méthode attaquer() ?******/
		    if (rep == "2"){
		    	System.out.print("Entrez le nom du joueur que vous souhaitez attaquer : ");
		    	String repJ = sc.nextLine();
		    	LabyrintheImpl lab = new LabyrintheImpl();
		    	//On récupère le joueur qui à ce nom "repJ"
		    	Joueur joueurAttaque = null;
		    	for(int i = 1; i <= nbJoueurs; i++)
			    {
			      if(pieceCourante.getLesJoueurs().get(i).getNom() == repJ){
			    	  joueurAttaque = pieceCourante.getLesJoueurs().get(i);
			      }
			    }
			    Thread t = new Thread(new Combat(lab, pieceCourante, j, joueurAttaque));
			    t.start();
		    }
		    if (rep == "3" && pvM != 0){
		    	LabyrintheImpl lab = new LabyrintheImpl();
			    Thread t = new Thread(new Combat(lab, pieceCourante, j, pieceCourante.getLeMonstre()));
			    t.start();
		    	}
		    }
		
		return deplace;
		/*
		 * Si le personnage rentre dans une pi�ce sans joueur, il se fait attaquer par un monstre (Classe Combat (Thread))
		 * S'il y a un joueur et un monstre, il peut choisir d'attaquer le joueur, le monstre ou personne. Mais il ne se fait pas attaquer directement.
		 * ==> n'appelle la m�thode attaquer() directement
		 */
	}

	
	public void attaquer(Joueur j, Personnage p) throws RemoteException {
		// TODO Auto-generated method stub
		int pvJ = j.getPv(); //Point de vie du joueur
		int pvP = p.getPv(); //Point de vie du personnage (autre joueur ou monstre)
		
		//Cr�ation d'un random object pour savoir lequel des 2 personnages va perdre une vie
		 Random randomno = new Random();
		      
		do{	
			//Obtenir al�atoirement true ou false
			boolean value = randomno.nextBoolean();
			
			if(value == true){
				pvJ--;
				j.setPv(pvJ);
			}else{
				pvP--;
				j.setPv(pvP);
			}
		  }while(pvJ != 0 || pvP != 0); //Cr�er m�thode fuir?
		
		//Lorsque le joueur perd, il meurt et le vainqueur gagne 1 point de vie
		if(pvJ == 0)
		{
			System.out.println("Oh non vous avez perdu ! ");
			pvP++;
		}
		//Lorsque le joueur gagne, il gagne 1 point de vie et continue le jeu
		else if(pvP == 0)
		{
			System.out.println("Bravo, vous avez gagné ! ");
			pvJ++;
			System.out.print("Quelle direction voulez-vous prendre? (N pour Nord, S pour Sud, E pour Est, O pour Ouest)");
			Scanner sc = new Scanner(System.in);
	    	String repDirection = sc.nextLine();
	    	char repDChar = repDirection.charAt(0);
	    	seDeplacer(j, repDChar);
	    	sc.close();
		}
		
		/*Gérer ceci : "dès qu'il n'y a plus de combat dans la pièce,
		on considère que tous les personnages ou monstres encore en vie regagnent tous leurs points de vie."*/
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
		 Naming.rebind("Labi", new LabyrintheImpl());
	}
}
