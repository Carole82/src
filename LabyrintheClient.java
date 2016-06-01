import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;


public class LabyrintheClient extends UnicastRemoteObject implements LabyrintheNotification {
	protected LabyrintheClient() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private Labyrinthe souche;

	/*public static void connexion(String pSouche) {
		try {
			souche = (Labyrinthe) Naming.lookup("rmi://localhost:1099/" + pSouche);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		// TODO Auto-generated method stub
		LabyrintheClient client = new LabyrintheClient();
		client.souche = (Labyrinthe) Naming.lookup("rmi://localhost:1099/Labi1");
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			int choix;
			String nom, mdp;
			Joueur j;
			
			//Menu d'accueil
			System.out.println("+---------------------------------+");
			System.out.println("| 1 - Se connecter                |");
			System.out.println("| 2 - Cr�er un joueur             |");
			System.out.println("| 0 - Arreter                     |");
			System.out.println("+---------------------------------+");
			
			//R�ponse
			choix = sc.nextInt();
			sc.nextLine(); // saute le retour � la ligne
			
			switch (choix) {
			case 0:
				sc.close();
				System.exit(0);
			case 1:
				System.out.println("Identifiant");
				nom = sc.next();
				sc.nextLine(); // saute le retour � la ligne
				System.out.println("Mot de passe");
				mdp = sc.next();
				sc.nextLine(); // saute le retour � la ligne
				j = client.souche.seConnecter(nom, mdp);
				if (j == null)
				{
					System.out.println("Le joueur n'existe pas! Identifiant ou mot de passe incorrect.");
				}
				else 
				{
					System.out.println(Piece.lesPieces.size());
					for (int i = 0; i<Piece.lesPieces.size(); i++) {
						System.out.println(Piece.lesPieces.get(i));
					}
					j.setPosition(Piece.lesPieces.get(j.getIdPosition()));
					j = client.souche.connecterServeur(j, client, 1);
					System.out.println("Vous �tes bien connect�! :)");
					
					System.out.println("La partie peut commencer !");
					System.out.println("Position : " + j.getIdPosition() + " dans le labyrinthe n� " + j.getServeur());
					//------------------ JOUER !!!!!!!! ------------------
					client.jouer(j);
				}
				break;
			case 2:
				System.out.println("Tapez le nom du joueur");
				nom = sc.next();
				sc.nextLine(); // saute le retour � la ligne
				System.out.println("Tapez le mot de passe");
				mdp = sc.next();
				j = client.souche.creerJoueur(nom, mdp);
				if (j==null)
				{
					System.out.println("Le joueur existe d�j�!");
				}
				else
				{
					System.out.println("Le joueur a bien �t� cr�� :)");
					
					System.out.println("La partie peut commencer !");
					System.out.println("Position : " + j.getIdPosition() + " dans le labyrinthe n� " + j.getServeur());
					//------------------ JOUER !!!!!!!! ------------------
					client.jouer(j);
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
	
	public String choisir_direction() throws RemoteException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Quelle direction voulez-vous prendre? (N = Nord, S = Sud, E = Est, O = Ouest)");
		String repD = sc.nextLine();
		sc.close();
    	return repD;
	}
	
	public void jouer (Joueur pJ) throws RemoteException {
		Scanner sc = new Scanner(System.in);
		String rep;
		int choix;
		while (true) {
			System.out.println("+---------------------------------+");
			System.out.println("| 1 - Se d�placer                 |");
			System.out.println("| 0 - Quitter                     |");
			System.out.println("+---------------------------------+");
			
			choix = sc.nextInt();
			sc.nextLine(); // saute le retour � la ligne
			
			switch (choix) {
			case 0: //Quitter
				souche.quitterPartie(pJ);
				System.out.println("Votre partie a �t� sauvegard�e!");
				sc.close();
				System.exit(0);
				break;
			case 1: //Se d�placer
				rep = choisir_direction();
		    	
		    	if (pJ.test_deplacement(rep) == null) //D�placement possible
		    	{ 
		    		System.out.println("Vous ne pouvez pas prendre cette direction");
		    	}
		    	else {
		    		//Fuite
		    		pJ.fuir();
		    		if (pJ.test_deplacement(rep) == "deplacement") //d�placement norrmal
		    			pJ.seDeplacer(rep);
		    		else //d�placement + changement de serveur
		    			pJ = souche.connecterServeur(pJ, this, 2);
		    		
		    		int nbJoueurs = pJ.getPosition().getLesJoueurs().size();
		    		if (nbJoueurs == 1) { //Seul avec le monstre
		    			if (!pJ.getPosition().getLeMonstre().getVivant()) //Monstre mort
		    				pJ.getPosition().getLeMonstre().revivre();
		    			// Combat avec le monstre
		    			pJ.attaquer(pJ.getPosition().getLeMonstre());
		    		}
		    		else //Autres joueur(s) dans la salle
		    		{ 
						if (!pJ.getPosition().getLeMonstre().getVivant()) //Monstre mort
						{ 
							Scanner sc_discussion = new Scanner(System.in);

							System.out.println("Voulez-vous discuter? (O/N)");
							String rep_dis = sc_discussion.nextLine();
							sc_discussion.close();
							
							do {
								String nomJ = null;
								if (rep_dis == "O") {
									if (nbJoueurs == 2) { 
										while (nomJ == null) {
											int j = 0;
											if (pJ.getPosition().getLesJoueurs().get(j).getNom() != pJ.getNom()) {
												nomJ = pJ.getPosition().getLesJoueurs().get(j).getNom();
											}
											j++;											
										}
										System.out.println("Vous entrez en communication avec )" + nomJ);
										
										
										//DISCUTER
									}
									else {
										System.out.println("Voici les joueurs pr�sents dans la pi�ce : ");
									    for(int k = 0; k < nbJoueurs; k++)
									    {
									      System.out.println( k+1 + " - " + pJ.getPosition().getLesJoueurs().get(k).getNom());
									    }
									    System.out.println("Choisissez le num�ro de celui avec lequel vous voulez communiquer");
									    // Joueur choisi : k-1
									    
									    
									    //DISCUTER
									}
								}
							} while (rep_dis != "O" && rep_dis != "N");					
						}
						else 
						{ //Choisir qui attaquer
						    System.out.print("Il y a actuellement 1 monstre et ");
						    System.out.println(nbJoueurs+" joueur(s) pr�sent(s) dans la pi�ce : ");
						    
						    for(int i = 0; i < nbJoueurs; i++)
						    {
						      System.out.println( i+1 + " - " + pJ.getPosition().getLesJoueurs().get(i).getNom());
						    }
						    
						    System.out.println("Que souhaitez-vous faire?");
						    System.out.println("1 - Fuir"); 
						    System.out.println("2 - Attaquer un joueur"); //Envoyer une notif pour dire qu'un joueur arrive
						    if(pJ.getPosition().getLeMonstre().getPv() != 0)
						    {
						    	System.out.println("3 - Attaquer le monstre");
						    }		    
						    int rep1 = sc.nextInt();
						    boolean repCorrect = false;
						    
						    do {
						    	switch (rep1) {
							    case 1:
							    	repCorrect = true;					    	
							    	break;
							    	
							    case 2:
							    	repCorrect = true;
							    	System.out.print("Entrez le num�ro du joueur que vous souhaitez attaquer : ");
							    	int repJ = sc.nextInt();
							    	Joueur joueurAttaque = pJ.getPosition().getLesJoueurs().get(repJ-1);
							    	pJ.attaquer(joueurAttaque);			    	
							    	break;
							    	
							    case 3 :
							    	if(pJ.getPosition().getLeMonstre().getPv() != 0) {
							    		System.out.println("Vous devez saisir 1 ou 2");
							    		repCorrect = false;
							    	}
							    	else {
							    		repCorrect = true;
							    		pJ.attaquer(pJ.getPosition().getLeMonstre());
							    	}
							    	break;
							    default :
							    	if(pJ.getPosition().getLeMonstre().getPv() != 0) {
							    		System.out.println("Vous devez saisir 1 ou 2");
							    	}
							    	else {
							    		System.out.println("Vous devez saisir 1, 2 ou 3");
							    	}
							    	repCorrect = false;
							    	break;
							    }
						    } while (repCorrect == false);
						}		
		    		}
		    		if(pJ.getPv() == 0)
		    			pJ.mourrir();
		    	    else
		    	    	pJ.gagner();
		    	}
			break;
			}		
		}
	}
}
