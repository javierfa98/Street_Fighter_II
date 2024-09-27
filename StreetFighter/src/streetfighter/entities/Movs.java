package streetfighter.entities;

public class Movs {
	int id; //Coincide con la ID de animacionesID
	int range;
	int damage;
	int risk;
	boolean dH; //Hace daño cuando el otro PJ esta arriba del salto [Por hacer]
	boolean dM; //Hace daño cuando el otro PJ esta de pie
	boolean dL; //Hace daño cuando el otro PJ esta agachado
	int frameAtaque; //Frames MAX en los que se puede hacer daño
	
	public Movs(int id, int range,int damage,int risk, boolean dH, boolean dM, boolean dL, int frameAtaque) {
		this.id=id;
		this.range=range;
		this.damage=damage;
		this.risk=risk;
		this.dH = dH;
		this.dM = dM;
		this.dL = dL;
		this.frameAtaque = frameAtaque;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getRisk() {
		return risk;
	}

	public void setRisk(int risk) {
		this.risk = risk;
	}

	public boolean getdH() {
		return dH;
	}

	public void setdH(boolean dH) {
		this.dH = dH;
	}

	public boolean getdM() {
		return dM;
	}

	public void setdM(boolean dM) {
		this.dM = dM;
	}

	public boolean getdL() {
		return dL;
	}

	public void setdL(boolean dL) {
		this.dL = dL;
	}

	public int getFrameAtaque() {
		return frameAtaque;
	}

	public void setFrameAtaque(int frameAtaque) {
		this.frameAtaque = frameAtaque;
	}
	
}

	