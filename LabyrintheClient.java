import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;


public class LabyrintheClient extends UnicastRemoteObject implements LabyrintheNotification {

	protected LabyrintheClient() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Labyrinthe souche = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi");
		
		while (true) {
			int choix;
			String nom, mdp;
			Joueur j;
			LabyrintheImpl lab;
			LabyrintheImpl2 lab2;
			
			System.out.println("+---------------------------------+");
			System.out.println("| 1 - Se connecter                |");
			System.out.println("| 2 - Créer un joueur             |");
			System.out.println("| 0 - Arreter                     |");
			System.out.println("+---------------------------------+");
			
			choix = sc.nextInt();
			sc.nextLine(); // saute le retour ï¿½ la ligne
			
			switch (choix) {
			case 0:
				sc.close();
				System.exit(0);
			case 1:
				System.out.println("Identifiant");
				nom = sc.next();
				sc.nextLine(); // saute le retour ï¿½ la ligne
				System.out.println("Mot de passe");
				mdp = sc.next();
				sc.nextLine(); // saute le retour ï¿½ la ligne
				j = souche.seConnecter(nom, mdp);
				if (j == null)
				{
					System.out.println("Le joueur n'existe pas! Identifiant ou mot de passe incorrect.");
				}
				else 
				{
					System.out.println("Vous êtes bien connectï¿½! :)");
					//Crï¿½er les labyrinthes sur les serveurs
					lab = new LabyrintheImpl();
					lab.initLabyrinthe();
					lab2 = new LabyrintheImpl2();
					lab2.initLabyrinthe();
					lab.creerLiens(lab2.getLesPieces());
					lab2.creerLiens(lab.getLesPieces());
					
					if (j.getServeur() == 1)
					{
						j.setPosition(lab.getLesPieces().get(j.getIdPosition()));
						lab.getLesPieces().get(j.getIdPosition()).getLesJoueurs().add(j);
					}
					else
					{
						j.setPosition(lab2.getLesPieces().get(j.getIdPosition()));
						lab2.getLesPieces().get(j.getIdPosition()).getLesJoueurs().add(j);
					}
					
					System.out.println("La partie peut commencer !");
					System.out.println("Position : " + j.getIdPosition() + " dans le labyrinthe n° " + j.getServeur());
					//------------------ JOUER !!!!!!!! ------------------
					jouer(souche, j);
				}
				break;
			case 2:
				System.out.println("Tapez le nom du joueur");
				nom = sc.next();
				sc.nextLine(); // saute le retour ï¿½ la ligne
				System.out.println("Tapez le mot de passe");
				mdp = sc.next();
				j = souche.creerJoueur(nom, mdp);
				if (j==null)
				{
					System.out.println("Le joueur existe déjà!");
				}
				else
				{
					System.out.println("Le joueur a bien été créé :)");
					
					//Crï¿½er les labyrinthes sur les serveurs
					lab = new LabyrintheImpl();
					lab2 = new LabyrintheImpl2();
					lab.initLabyrinthe();
					lab2.initLabyrinthe();
					lab.creerLiens(lab2.getLesPieces());
					lab2.creerLiens(lab.getLesPieces());
					
					j.setPosition(lab.getLesPieces().get("A1"));
					lab.getLesPieces().get("A1").getLesJoueurs().add(j);
					
					System.out.println("La partie peut commencer !");
					System.out.println("Position : " + j.getIdPosition() + " dans le labyrinthe n° " + j.getServeur());
					//------------------ JOUER !!!!!!!! ------------------
					jouer(souche, j);
				}
				break;
			}	
		}
	}
	
	public void notification(String pMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// A faire
		System.out.println(pMessage);
		
	}
	
	public static void jouer (Labyrinthe pL, Joueur pJ) throws RemoteException {
		Scanner sc = new Scanner(System.in);
		int choix;
		while (true) {
			System.out.println("+---------------------------------+");
			System.out.println("| 1 - Se déplacer                 |");
			System.out.println("| 0 - Quitter                     |");
			System.out.println("+---------------------------------+");
			
			choix = sc.nextInt();
			sc.nextLine(); // saute le retour ï¿½ la ligne
			
			switch (choix) {
			case 0:
				pL.quitterPartie(pJ);
				System.out.println("Votre partie a été sauvegardée!");
				sc.close();
				System.exit(0);
			case 1:
				System.out.println("Quelle direction voulez-vous prendre? (N pour Nord, S pour Sud, E pour Est, O pour Ouest)");
		    	String repDirection = sc.nextLine();
		    	char repDChar = repDirection.charAt(0);
		    	pJ = pL.seDeplacer(pJ, pJ.getPosition(), repDChar);
		    	//Attaque
			break;
			}		
		}
	}
}
