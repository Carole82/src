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
	
	//Serveur
	private HashMap<String, Piece> lesPieces = new HashMap<String, Piece>();
	private HashMap<String, Personnage> lesPersonnages = new HashMap<String, Personnage>();
	
	//CLient
	private HashMap<String, LabyrintheNotification> notifications = new HashMap<String, LabyrintheNotification>();
	


	public boolean seDeplacer(Joueur j, char direction) throws RemoteException {
		// TODO Auto-generated method stub
		boolean deplace = false ;
		Piece pieceCourante = new Piece(j.getIdPosition(),j.getPosition().getLeMonstre().getNom());
		
		pieceCourante = j.getPosition();
		int nbJoueurs = pieceCourante.getLesJoueurs().size();
		
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
		    if(pieceCourante.getLeMonstre().getPv() != 0)
		    {
		    	System.out.print("Il y a actuellement 1 monstre et ");
		    }
		    System.out.println(nbJoueurs+" joueur(s) dans la pièce : ");
		    for(int i = 1; i <= nbJoueurs; i++)
		    {
		      System.out.println( i + " - " + pieceCourante.getLesJoueurs().get(i).getNom());
		    }
		    System.out.println("Que souhaitez-vous faire?");
		    System.out.println("1 - Attaquer le monstre");
		    System.out.println("2 - Attaquer un joueur"); //Envoyer une notif pour dire qu'un joueur arrive
		    System.out.println("3 - Fuir");
		    String rep = sc.nextLine();
		    sc.close();
		    if (rep == "1"){
		    	LabyrintheImpl lab = new LabyrintheImpl();
			    Thread t = new Thread(new Combat(lab, pieceCourante, j, pieceCourante.getLeMonstre()));
			    t.start();
		    }
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
		    if (rep == "3"){
		    	System.out.print("Quelle direction voulez-vous prendre? ");
		    	String repDirection = sc.nextLine();
		    	char repDChar = repDirection.charAt(0);
		    		seDeplacer(j, repDChar);
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
		int pvJ = j.getPv();
		int pvP = p.getPv();
		
		//Cr�ation d'un random object pour savoir lequel des 2 personnages va perdre une vie
		 Random randomno = new Random();
		      
		do{	
			//Obtenir al�atoirement true ou false
			boolean value = randomno.nextBoolean();
			
			if(value == true){
				pvJ = pvJ--;
				j.setPv(pvJ);
			}else{
				pvP = pvP--;
				j.setPv(pvP);
			}
		  }while(pvJ != 'O' || pvP != 'O'); //Cr�er m�thode fuir?
		
		
		
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
			Joueur j = new Joueur(nomJoueur, mdp);
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