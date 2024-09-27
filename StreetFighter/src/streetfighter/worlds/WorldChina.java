package streetfighter.worlds;

import java.awt.Color;
import java.awt.Graphics;

import streetfighter.Handler;
import streetfighter.entities.Player;
import streetfighter.gfx.Assets;
import streetfighter.worlds.World;

public class WorldChina extends World {
	
	//Ancho y alto total del stage
	private int stageWidth, stageHeight;
	//Pixel de la pantalla donde debe colocarse el pixel (0,0) del stage para que quede centrado
	private int stageSpawnX;
	
	//Limites del stage por los lados
	private int stageLimDerecho;
	private int stageLimIzq;
	
	//Que un jugador no se salga del limite del stage 
	private boolean collisionLimSt_1; //Player 1
	private boolean collisionLimSt_2; //Player 2
	
	public WorldChina(Handler handler, int fighter1, int fighter2, int mode) {
		super(handler, fighter1, fighter2, mode);
		
		//Cargar stage
		this.stageWidth = 2600;
		this.stageHeight = 720;
		stageSpawnX = - (this.stageWidth - handler.getWidth())/2; //(TamanyoStage - tamanyoCanvas)/2
		stageLimDerecho = handler.getWidth() + (- stageSpawnX) - 250;
		stageLimIzq = stageSpawnX + 250;
		
		collisionLimSt_1 = false; //Que un jugador no se salga del limite del stage
		collisionLimSt_2 = false; //Que un jugador no se salga del limite del stage
		
		handler.getGame().setCurrentSong(5);
		handler.getGame().getMp().setCambioRonda(true);
	}
	
	public void tick() {
		animacionLB.tick();	//Animacion life bar
		
		setDistancia();	//Actualizar distancia entre los players
		setCollisionLimCamera();	//Para que no se salgan de la pantalla
		setCollisionLimStage();		//Para que no se salgan del stage
		setCollisionChoque();		//Los dos jugadores chocan
		setXOffset();	//Calcula el movimiento de la camara eje X
		setYOffset();	//Calcula el movimiento de la camara eje Y
		
		//Llama al tick() de los players
		entityManager.tick(collisionLimiteCamara,
						   collisionLimSt_1, 
						   collisionLimSt_2,
						   collisionEntreJugadores);
	}
	
	//Dibujar stage y players
	public void render(Graphics g) {
		//Fondo
		int anchoFondoOffset = 500;
		g.drawImage(animacionesStage.get(2).getCurrentFrame(), 
					(int) (stageSpawnX - xOffset*0.4 + (anchoFondoOffset/2)), (int)(-24 + yOffset*0.4), stageWidth-anchoFondoOffset, 600, null);
		//Suelo
		g.drawImage(animacionesStage.get(0).getCurrentFrame(), 
					(int) (stageSpawnX - xOffset), (int)(520 + yOffset), stageWidth, 200, null);
		//Valla
		int anchoVallaOffset = 320;
		g.drawImage(animacionesStage.get(1).getCurrentFrame(), 
					(int) (stageSpawnX - xOffset-(anchoVallaOffset/2)), (int)(380 + yOffset), stageWidth+anchoVallaOffset, 300, null);
				
		//Dibujar los players
		entityManager.render(g, xOffset, yOffset);
		
		//Dibujar vida
		g.drawImage(Assets.lifeBar, 100, 61, 1080, 49, null);	//LifeBar
		int damageAcumulated_1 = Player.VIDA_INICIAL - (int)entityManager.getPlayer_1().getVida(); //Ancho de la vida: 469
		int damageAcumulated_2 = Player.VIDA_INICIAL - (int)entityManager.getPlayer_2().getVida();
	    g.setColor(Color.red);
	    g.fillRect(104, 71, damageAcumulated_1, 28);	//Vida player 1
	    g.fillRect(1176 - damageAcumulated_2, 71, damageAcumulated_2, 28);	//Vida player 2
	    
	    //Animacion de K.O. logo cuando queda poco de vida
	    if(damageAcumulated_1 > 300 || damageAcumulated_2 > 300) {
	    	if(animacionLB.getIndex()==0) {
	    		g.drawImage(Assets.lifeBar_KO_blanco, 573, 61, 134, 49, null);
	    	}
	    	else {
	    		g.drawImage(Assets.lifeBar_KO_negro, 573, 57, 134, 54, null);
	    	}
	    }
	    g.setColor(Color.white);
	}
	
	//Calcula si un jugador no puede andar hacia atras porq se sale del stage
	public void setCollisionLimStage() {
		collisionLimSt_1 = false;
		collisionLimSt_2 = false;
		
		//Jugador 1
		if(entityManager.getPlayer_1().getOrientacion()) {	//Si mira hacia la derecha
			double limX1 = entityManager.getPlayer_1().getX();
			if(limX1 <= stageLimIzq) {
				collisionLimSt_1 = true;
			}
		}
		else {	//Si mira hacia la izquierda
			double limX1 = entityManager.getPlayer_1().getX() + entityManager.getPlayer_1().getWidth();
			if(limX1 >= stageLimDerecho) {
				collisionLimSt_1 = true;
			}
		}
				
		//Jugador 2
		if(entityManager.getPlayer_2().getOrientacion()) {	//Si mira hacia la derecha
			double limX2 = entityManager.getPlayer_2().getX();
			if(limX2 <= stageLimIzq) {
				collisionLimSt_2 = true;
			}
		}
		else {	//Si mira hacia la izquierda
			double limX2 = entityManager.getPlayer_2().getX() + entityManager.getPlayer_2().getWidth();
			if(limX2 >= stageLimDerecho) {
				collisionLimSt_2 = true;
			}
		}
	}
	
	public int getStageWidth() {
		return stageWidth;
	}

	public int getStageHeight() {
		return stageHeight;
	}	
}
