package streetfighter.worlds;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import streetfighter.Handler;
import streetfighter.entities.EntityManager;
import streetfighter.entities.Player;
import streetfighter.entities.Player1;
import streetfighter.entities.Player2;
import streetfighter.entities.PlayerIA;
import streetfighter.gfx.Animation;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.utils.Utils;

public class World {
	
	protected Handler handler;
	
	//Animacion lifeBar
	protected Animation animacionLB;
	//Seleccionar el stage del combate
	protected ArrayList<Animation> animacionesStage = new ArrayList<Animation>();
	
	//Tamano jugado 1 y 2
	private int width_1, height_1;
	private int width_2, height_2;
	
	//Spawn del jugador 1 y 2
	private int spawnX_1,spawnY_1;
	private int spawnX_2,spawnY_2;
	
	//Entities (Gestiona los 2 jugadores)
	protected EntityManager entityManager;
	
	//Para mover la camara en funcion del movimiento de players
	double xP1MoverCamara, xP2MoverCamara;
	double xP1MoverCamaraAnterior, xP2MoverCamaraAnterior;
	//Para mover camara
	protected double xOffset;
	protected double yOffset;
	
	//COLISIONES
	//Distancia entre los dos jugadores
	public double distancia;
	//Permitir a un jugador moverse hacia atras
	protected boolean collisionLimiteCamara;
	//Los dos jugadores chocan
	protected boolean collisionEntreJugadores;

	
	//Constructor
	public World(Handler handler, int fighter1, int fighter2, int mode) {
		//Inicializar los graficos del mundo correspondiente (El del fighter 2)
		animacionesStage = Assets.initAssets_World(fighter2);
		animacionLB = Assets.KO_logo; //Animacion life bar
		Assets.initAssets_fighters(handler.getGame().path);
		
		this.handler=handler;
		this.width_1 = 200;
		this.height_1 = 350;
		this.width_2 = 200;
		this.height_2 = 350;
		if(fighter1==0) {
			this.spawnX_1 = 240;
		}
		else {
			this.spawnX_1 = 340;
		}
		this.spawnY_1 = 325;
		if(fighter2==0) {
			this.spawnX_2 = 780;
		}
		else {
			this.spawnX_2 = 760;
		}
		this.spawnY_2 = 325;
		
		//Player 1 VS CPU
		if (mode==1) {
			handler.getGame().mode=1;
			//Crear jugadores
			entityManager=new EntityManager(handler,								  //Speed, Orientacion
					new Player1(handler, spawnX_1, spawnY_1, width_1, height_1, fighter1, 10, true),
					new PlayerIA(handler, spawnX_2, spawnY_2, width_2, height_2, fighter2, 10, false,2));
		}
		//Player 1 VS Player 2
		else if (mode==2) {
			handler.getGame().mode=2;
			//Crear jugadores
			entityManager=new EntityManager(handler,								  //Speed, Orientacion
					new Player1(handler, spawnX_1, spawnY_1, width_1, height_1, fighter1, 10, true),
					new Player2(handler, spawnX_2, spawnY_2, width_2, height_2, fighter2, 10, false));
		}
		//CPU vs CPU
		else if(mode==3) {
			handler.getGame().mode=3;
			//Crear jugadores
			entityManager=new EntityManager(handler,								  //Speed, Orientacion
					new PlayerIA(handler, spawnX_1, spawnY_1, width_1, height_1, fighter1, 10, true,1),
					new PlayerIA(handler, spawnX_2, spawnY_2, width_2, height_2, fighter2, 10, false,2));
		}
		
		
		
		
		//Colisiones y camara
		setDistancia();
		collisionLimiteCamara = false; //Permitir a un jugador moverse hacia atras sin salir de la pantalla
		collisionEntreJugadores = false;
		xP1MoverCamara = 0;
		xP2MoverCamara = 0;
		xP1MoverCamaraAnterior = 0;
		xP2MoverCamaraAnterior = 0;
		xOffset = 0;
		yOffset = 0;
		
		//Para para poder acceder al mundo desde el handler
		handler.getGame().getHandler().setWorld(this);
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	//Actualizar variables jugadores y colisiones
	public void tick() {}
	
	//Dibujar stage y players
	public void render(Graphics g) {}
	
	//Actualiza la distancia entre el centro de los dos jugadores
	public void setDistancia() {
		double xP1 = entityManager.getPlayer_1().getX() + entityManager.getPlayer_1().getWidth()/2;
		double yP1 = entityManager.getPlayer_1().getY() + entityManager.getPlayer_1().getHeight()/2;
		double xP2 = entityManager.getPlayer_2().getX() + entityManager.getPlayer_2().getWidth()/2;
		double yP2 = entityManager.getPlayer_2().getY() + entityManager.getPlayer_2().getHeight()/2;
		
		double radicando = Math.pow(xP2-xP1, 2) + Math.pow(yP2-yP1, 2);
		distancia = Math.sqrt(radicando);
	}
	
	//Calcula si los jugadores no pueden moverse mas hacia atras porq se salen de la pantalla
	public void setCollisionLimCamera() {
		double total = distancia + 
					   entityManager.getPlayer_1().getWidth()/2 + 
					   entityManager.getPlayer_2().getWidth()/2;
		
		if(total >= handler.getWidth()) {
			collisionLimiteCamara = true;
		}
		else {
			collisionLimiteCamara = false;
		}
	}
	
	//Calcula el movimiento de la camara eje X
	public void setXOffset() {
		//Jugador 1
		if(entityManager.getPlayer_1().getOrientacion()) {	//Si mira hacia la derecha
			xP1MoverCamara = entityManager.getPlayer_1().getX();
			//System.out.println(xP1MoverCamara);
			/*if(xP1MoverCamara < xP1MoverCamaraAnterior &&  xOffset > -410) {
				xOffset = xP1MoverCamara-300;
			}*/
			if(xP1MoverCamara<0) {
				xOffset = xP1MoverCamara;
			}
		}
		else {	//Si mira hacia la izquierda
			xP1MoverCamara = entityManager.getPlayer_1().getX() + entityManager.getPlayer_1().getWidth();
			if(xP1MoverCamara>1280) {
				xOffset = xP1MoverCamara - 1280;
			}
		}
		
		//Jugador 2
		if(entityManager.getPlayer_2().getOrientacion()) {	//Si mira hacia la derecha
			xP2MoverCamara = entityManager.getPlayer_2().getX();
			if(xP2MoverCamara<0) {
				xOffset = xP2MoverCamara;
			}
		}
		else {	//Si mira hacia la izquierda
			xP2MoverCamara = entityManager.getPlayer_2().getX() + entityManager.getPlayer_2().getWidth();
			if(xP2MoverCamara>1280) {
				xOffset = xP2MoverCamara - 1280;
			}
		}
		//System.out.println(xOffset);
		xP1MoverCamaraAnterior = xP1MoverCamara;
		xP2MoverCamaraAnterior = xP2MoverCamara;
	}
	
	//Calcula el movimiento de la camara eje Y
	public void setYOffset() {
		//Jugador 1
		int player1_Y = (int)entityManager.getPlayer_1().getY();
		int player2_Y = (int)entityManager.getPlayer_2().getY();
		int maxY = player1_Y;
		if(player2_Y < maxY) {
			maxY = player2_Y;
		}
		
		if(maxY < spawnY_1) {
			yOffset = (spawnY_1 - maxY)*0.10;
		}
		else if(maxY < spawnY_2) {
			yOffset = (spawnY_2 - maxY)*0.10;
		}
		else {
			yOffset = 0;
		}
	}
	
	//Si los jugadores se juntan y chocan
	public void setCollisionChoque() {
		collisionEntreJugadores = false;
		//offsetDeACercamiento para determinar cuanto de cerca pueden estar
		// 0 <= offsetDeAcercamiento <= (entityManager.getPlayer_X().getWidth())/2
		int offsetDeAcercamiento = 80;
		//Si alguno esta saltando, no se chocan
		if(!(entityManager.getPlayer_1().getAnimacionActual() == 4 || entityManager.getPlayer_2().getAnimacionActual() == 4)) {
			if(entityManager.getPlayer_1().getOrientacion()) { //Si P1 mira a la dcha y P2 mira a la izq
				int limX1 =(int) (entityManager.getPlayer_1().getX() + entityManager.getPlayer_1().getWidth());
				int limX2 =(int) (entityManager.getPlayer_2().getX());
				
				//System.out.println("LimX1: "+limX1+ ", LimX2: "+limX2);
				if((limX1-offsetDeAcercamiento) >= limX2) {
					collisionEntreJugadores = true;
				}
			}
			else { //Si P1 mira a la izq y P2 mira a la dcha
				int limX1 = (int) (entityManager.getPlayer_1().getX());
				int limX2 =(int) (entityManager.getPlayer_2().getX() + entityManager.getPlayer_2().getWidth());
				
				//System.out.println("LimX1: "+limX1+ ", LimX2: "+limX2);
				if((limX2-offsetDeAcercamiento) >= limX1) {
					collisionEntreJugadores = true;
				}
			}
		}
	}
}
