
public class Personnage {
	/*
	 * x = ligne
	 * y = colonne
	 * 
	 */
	
	public static final int PV_MAX_MONSTRE = 5;
	public static final int PV_MAX_JOUEUR = 10;
	
	private String nom;
	private int pv;
	private int pvMax; 
	private transient Piece position; 
	private String idPosition;
	
	public Personnage (String pN, int pPv, int pPvMax, Piece pPosition) {
		setNom(pN);
		setPv(pPv);
		setPvMax(pPvMax);
		setPosition(pPosition);
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getPvMax() {
		return pvMax;
	}

	public void setPvMax(int pvMax) {
		this.pvMax = pvMax;
	}

	public Piece getPosition() {
		return position;
	}

	public void setPosition(Piece position) {
		this.position = position;
		setIdPosition(position.getIdPiece());
	}

	public String getIdPosition() {
		return idPosition;
	}

	public void setIdPosition(String idPosition) {
		this.idPosition = idPosition;
	}
	
}
