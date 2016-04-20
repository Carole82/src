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
import java.util.Date;
import java.util.HashMap;


public class PersistanceImpl implements Persistance{
	/*
	 * Requête SQL (voir BanqueJDBC)
	 */
	
	private Connection conn;
	
	private static final String requeteSelectNom = "select * from JOUEUR where nom = ?";
	private static final String requeteSelectNomMdp = "select * from JOUEUR where nom = ? and mdp =?";
	private static final String requeteInsert = "insert into JOUEUR values (?, ?, ?, ?)";
	private static final String requeteUpdateMdp = "update JOUEUR set mdp=? where nom = ?";
	private static final String requeteUpdatePosition = "update JOUEUR set position=? where nom = ?";
	private static final String requeteUpdatePv = "update JOUEUR set pv=? where nom = ?";
	
	private PreparedStatement requeteSelectNomSt = null;
	private PreparedStatement requeteSelectNomMdpSt = null;
	private PreparedStatement requeteInsertSt = null;
	private PreparedStatement requeteUpdateMdpSt = null;
	private PreparedStatement requeteUpdatePositionSt = null;
	private PreparedStatement requeteUpdatePvSt = null;

	public PersistanceImpl(String nomBD)  {
		try {
			// récupération du driver
		    Class.forName("org.h2.Driver");
		    
		    // création d'une connexion
		    conn = DriverManager.getConnection("jdbc:h2:"+nomBD+";IGNORECASE=TRUE", "sa", "");
		    
	        // On regarde si la table existe deja
	        try {
	        	// construction du prepared statement
	        	requeteSelectNomSt = conn.prepareStatement(requeteSelectNom);
	        } catch(Exception e) {
	        	// sinon on l'a cree
	        	Statement s = conn.createStatement();
	        	s.execute("create table JOUEUR  ( " +
	        			" nom VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
	        			" mdp VARCHAR( 256 ) , " +
	        			" position VARCHAR( 2 ) , " +
	        			" pv INTEGER )");
	        	// on ajoute des entrees de test : Ajouter des joueurs
	        	s.executeUpdate("insert into JOUEUR values ('Carole', 'azerty', 'B5', 10)");
	        	s.executeUpdate("insert into JOUEUR values ('Mehdi', 'azerty', 'A2', 10)");
	        	s.executeUpdate("insert into JOUEUR values ('Maëva', 'azerty', 'D4', 10)");
	        	
	        	// on retente la construction qui devrait desormais marcher
	        	requeteSelectNomSt = conn.prepareStatement(requeteSelectNom);
	        }
	        // construction des autres prepared statement
	        requeteSelectNomSt = conn.prepareStatement(requeteSelectNom);
		    requeteInsertSt = conn.prepareStatement(requeteInsert);
		    requeteUpdateMdpSt = conn.prepareStatement(requeteUpdateMdp);
		    requeteUpdatePositionSt = conn.prepareStatement(requeteUpdatePosition);
		    requeteUpdatePvSt = conn.prepareStatement(requeteUpdatePv);
		} catch(Exception e) {
			// il y a eu une erreur
			e.printStackTrace();
		}
	}
	

	public boolean creerJoueur(String pNom, String pMdp) {
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
	        	requeteInsertSt.setInt(4, Personnage.PV_MAX_MONSTRE);
	        	requeteInsertSt.executeUpdate();
	        	return true;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Joueur seConnecter(String nomJoueur, String mdp) {
		// TODO Auto-generated method stub
		try {
			requeteSelectNomMdpSt.setString(1, nomJoueur);
			requeteSelectNomMdpSt.setString(2, mdp);
			
			ResultSet rs = requeteSelectNomMdpSt.executeQuery();
			if (rs.next()) {
				Joueur j = new Joueur(nomJoueur, mdp);
				j.setIdPosition(rs.getString("position"));
				j.setPv(rs.getInt("pv"));
				j.setPvMax(rs.getInt("pv"));
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

	public boolean quitterPartie(Joueur j) {
		// TODO Auto-generated method stub
		try {
			requeteUpdateMdpSt.setString(1, j.getMdp());
			requeteUpdateMdpSt.setString(2, j.getNom());
			
			requeteUpdatePositionSt.setString(1, j.getPosition().getIdPiece());
			requeteUpdatePositionSt.setString(2,j.getNom());
			
			requeteUpdatePvSt.setInt(2,Personnage.PV_MAX_MONSTRE);
			requeteUpdatePvSt.setString(2,j.getNom());
			
        	if (requeteUpdateMdpSt.executeUpdate()==1 && requeteUpdatePositionSt.executeUpdate()==1 && requeteUpdatePvSt.executeUpdate()==1)
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
