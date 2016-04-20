import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
			
			System.out.println("+---------------------------------+");
			System.out.println("| 1 - Se connecter                |");
			System.out.println("| 2 - Créer un joueur             |");
			System.out.println("| 0 - Arreter                     |");
			System.out.println("+---------------------------------+");
			
			choix = sc.nextInt();
			sc.nextLine(); // saute le retour à la ligne
			
			switch (choix) {
			case 0:
				sc.close();
				System.exit(0);
			case 1:
				System.out.println("Identifiant");
				nom = sc.next();
				sc.nextLine(); // saute le retour à la ligne
				System.out.println("Mot de passe");
				mdp = sc.next();
				sc.nextLine(); // saute le retour à la ligne
				j = souche.seConnecter(nom, mdp);
				if (j == null)
					System.out.println("Le joueur n'existe pas! Identifiant ou mot de passe incorrect.");
				else
					System.out.println("Vous êtes bien connecté! :)");
					//JOUER !!!!!!!!
				break;
			case 2:
				System.out.println("Tapez le nom du joueur");
				nom = sc.next();
				sc.nextLine(); // saute le retour à la ligne
				System.out.println("Tapez le mot de passe");
				mdp = sc.next();
				j = souche.creerJoueur(nom, mdp);
				if (j==null)
					System.out.println("Le joueur existe déjà!");
				else
					System.out.println(j);
					//JOUER !!!!
				break;
			}	
		}
	}

	public void notification(String pMessage) throws RemoteException {
		// TODO Auto-generated method stub
		// A faire
		System.out.println(pMessage);
		
	}


}
