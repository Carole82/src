import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class PersistanceImpl extends UnicastRemoteObject implements Persistance {
	/*
	 * Requête SQL (voir BanqueJDBC)
	 */
	
	private Connection conn;
	
	private static final String requeteSelect = "select * from JOUEUR";
	private static final String requeteSelectNom = "select * from JOUEUR where nom = ?";
	private static final String requeteSelectNomMdp = "select * from JOUEUR where nom = ? and mdp = ?";
	private static final String requeteInsert = "insert into JOUEUR values (?, ?, ?, ?, ?)";
	private static final String requeteUpdateMdp = "update JOUEUR set mdp=? where nom = ?";
	private static final String requeteUpdatePosition = "update JOUEUR set position=? where nom = ?";
	private static final String requeteUpdatePv = "update JOUEUR set pv=? where nom = ?";
	private static final String requeteUpdateServeur = "update JOUEUR set serveur=? where nom = ?";
	
	private PreparedStatement requeteSelectSt = null;
	private PreparedStatement requeteSelectNomSt = null;
	private PreparedStatement requeteSelectNomMdpSt = null;
	private PreparedStatement requeteInsertSt = null;
	private PreparedStatement requeteUpdateMdpSt = null;
	private PreparedStatement requeteUpdatePositionSt = null;
	private PreparedStatement requeteUpdatePvSt = null;
	private PreparedStatement requeteUpdateServeurSt = null;

	protected PersistanceImpl(String nomBD) throws RemoteException{
		try {
			// récupération du driver
		    Class.forName("org.h2.Driver");
		    
		    // création d'une connexion
		    conn = DriverManager.getConnection("jdbc:h2:"+nomBD+";IGNORECASE=TRUE", "sa", "");
		    
	        // On regarde si la table existe deja
	        try {
	        	// construction du prepared statement
	        	requeteSelectSt = conn.prepareStatement(requeteSelect);
	        	ResultSet rs = requeteSelectSt.executeQuery();
	        	requeteSelectNomSt = conn.prepareStatement(requeteSelectNom);
	        } catch(Exception e) {
	        	// sinon on l'a cree
	        	System.out.println("Création de la table");
	        	Statement s = conn.createStatement();
	        	s.execute("create table JOUEUR  ( " +
	        			" nom VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
	        			" mdp VARCHAR( 256 ) , " +
	        			" position VARCHAR( 2 ) , " +
	        			" pv INTEGER , " + 
	        			" serveur INTEGER )");
	        	System.out.println(s.toString());
	        	
	        	// on ajoute des entrees de test : Ajouter des joueurs
	        	s.executeUpdate("insert into JOUEUR values ('Carole', 'azerty', 'B3', 10, 1)");
	        	s.executeUpdate("insert into JOUEUR values ('Mehdi', 'azerty', 'A2', 10, 1)");
	        	s.executeUpdate("insert into JOUEUR values ('Maeva', 'azerty', 'C3', 10, 1)");
	        	System.out.println(s.toString());
	        	
	        	// on retente la construction qui devrait desormais marcher
	        	requeteSelectNomSt = conn.prepareStatement(requeteSelectNom);
	        }
	        // construction des autres prepared statement
	        requeteSelectNomMdpSt = conn.prepareStatement(requeteSelectNomMdp);
		    requeteInsertSt = conn.prepareStatement(requeteInsert);
		    requeteUpdateMdpSt = conn.prepareStatement(requeteUpdateMdp);
		    requeteUpdatePositionSt = conn.prepareStatement(requeteUpdatePosition);
		    requeteUpdatePvSt = conn.prepareStatement(requeteUpdatePv);
		    requeteUpdateServeurSt = conn.prepareStatement(requeteUpdateServeur);
		} catch(Exception e) {
			// il y a eu une erreur
			e.printStackTrace();
		}
	}
	

	public boolean creerJoueur(String pNom, String pMdp) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			requeteSelectNomSt.setString(1,pNom);
			ResultSet rs = requeteSelectNomSt.executeQuery();
			
	        if (rs.next()) {
	        	// un joueur existe déja avec ce nom
	        	return false;
	        } else {
	        	requeteInsertSt.setString(1, pNom);
	        	requeteInsertSt.setString(2, pMdp);
	        	requeteInsertSt.setString(3, "A1");
	        	requeteInsertSt.setInt(4, Personnage.PV_MAX_JOUEUR);
	        	requeteInsertSt.setInt(5, 1);
	        	requeteInsertSt.executeUpdate();
	        	return true;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Joueur seConnecter(String nomJoueur, String mdp) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			requeteSelectNomMdpSt.setString(1, nomJoueur);
			requeteSelectNomMdpSt.setString(2, mdp);			
			ResultSet rs = requeteSelectNomMdpSt.executeQuery();
			
        	/*
        	 * 1 = Nom / 2 = Mdp / 3 = Position / 4 = PV / 5 = Serveur
        	 */
			
			if (rs.next()) 
			{
				Joueur j = new Joueur(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(5));
				j.setPv(rs.getInt(4));
				j.setPvMax(rs.getInt(4));
				return j;
			}
			else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean quitterPartie(Joueur j) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			requeteUpdateMdpSt.setString(1, j.getMdp());
			requeteUpdateMdpSt.setString(2, j.getNom());
			
			requeteUpdatePositionSt.setString(1, j.getIdPosition());
			requeteUpdatePositionSt.setString(2,j.getNom());
			
			requeteUpdatePvSt.setInt(1,Personnage.PV_MAX_JOUEUR);
			requeteUpdatePvSt.setString(2,j.getNom());
			
			requeteUpdateServeurSt.setInt(1, j.getServeur());
			requeteUpdateServeurSt.setString(2,j.getNom());
			
        	if (requeteUpdateMdpSt.executeUpdate()==1 && requeteUpdatePositionSt.executeUpdate()==1 && 
        			requeteUpdatePvSt.executeUpdate()==1 && requeteUpdateServeurSt.executeUpdate() ==1)
				return true;
        	else
        		return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public static void main(String[] args) throws Exception {
		 LocateRegistry.createRegistry(1099);
		 Naming.rebind("Pers", new PersistanceImpl("BD"));
	}
}
