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

	protected LabyrintheImpl(int serveur) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		idServeur = serveur;
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
	private int idServeur;
	private HashMap<String, LabyrintheClient> lesClients = new HashMap<String, LabyrintheClient>(); 
	
	//Serveur
	private static HashMap<String, Joueur> lesJoueurs = new HashMap<String, Joueur>();
	
	//CLient
	private HashMap<String, LabyrintheNotification> notifications = new HashMap<String, LabyrintheNotification>();

	public void envoyerMessage(Joueur emmeteur, Joueur recepteur, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
	}

	public Joueur connecterServeur(Joueur j, LabyrintheClient client, int cas) throws RemoteException {
		// TODO Auto-generated method stub
		//1 = connexion ; 2 = déplacement (changement de serveur) ; 3 = défaite
		Labyrinthe l = null;
		switch (cas) {
			case 1: 
				if (j.getServeur() == 2) {
					lesClients.remove(j.getNom());
					try {
						l = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi2");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lesClients.put(j.getNom(), client);
				}
				break;
			case 2 :
				lesClients.remove(j.getNom());
				if (j.getServeur() == 1) {
					try {
						l = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi2");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					j.setPosition(Piece.lesPieces.get("A4"));
				}
				else {
					try {
						l = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi1");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					j.setPosition(Piece.lesPieces.get("C3"));
				}
				lesClients.put(j.getNom(), client);
				j.getPosition().getLesJoueurs().add(j);
				break;
			case 3 :
				if (j.getServeur() == 2) {
					lesClients.remove(j.getNom());
					try {
						l = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi1");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lesClients.put(j.getNom(), client);
					j.setPosition(Piece.lesPieces.get("A1"));
					j.getPosition().getLesJoueurs().add(j);
				}
				break;	
		}
		return j;
	}
	
	public void deconnecterServeur(String nomJoueur) throws RemoteException {
		// TODO Auto-generated method stub
		lesClients.remove(nomJoueur);
	}
	
	public Joueur creerJoueur(String nomJoueur, String mdp) throws RemoteException {
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

	public Joueur seConnecter(String nomJoueur, String mdp) throws RemoteException {
		// TODO Auto-generated method stub
		Joueur j = p.seConnecter(nomJoueur, mdp);
		lesJoueurs.put(nomJoueur, j);
		//j.getPosition().getLesJoueurs().add(j);
		return j;
	}

	public boolean quitterPartie(Joueur j) throws RemoteException {
		// TODO Auto-generated method stub
		return p.quitterPartie(j);
	}
	
	public synchronized void enregistrerNotification(String id, LabyrintheNotification l, double minimum) throws RemoteException {
		// TODO Auto-generated method stub
		Piece p = Piece.lesPieces.get(id);
		p.setNotification(l);
		//notifications.put(id, b);
		
		//Rajouter déplacer, combat, discussion !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}
	
	public synchronized void enleverNotification(String id) throws RemoteException {
		// TODO Auto-generated method stub
		//notifications.remove(id);
		Piece p = Piece.lesPieces.get(id);
		p.setNotification(null);
	}
	
	public HashMap<String, LabyrintheClient> getLesClients() {
		return lesClients;
	}
}
