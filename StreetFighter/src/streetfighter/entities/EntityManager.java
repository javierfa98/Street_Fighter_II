package streetfighter.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Handler;
import streetfighter.Launcher;
import streetfighter.entities.Player;

//Gestiona todas las entidades del juego
public class EntityManager {

	private Handler handler;
	
	//Jugadores
	private Player player_1;
	private Player player_2;
	
	private int animacionActual;
	
	private float offset;

	//Ataques
	ArrayList<Movs> ataques;
	
	public EntityManager(Handler handler,Player player_1, Player player_2) {
		this.handler=handler;
		this.player_1=player_1;
		this.player_2=player_2;
		ataques = new ArrayList<Movs>();
		//Calculamos offset
		float offset1 = player_1.getAnimaciones().get(0).getWidth()[0]-200;
		player_1.setWidth(player_1.getAnimaciones().get(0).getWidth()[0]);
		float offset2 = player_2.getAnimaciones().get(0).getWidth()[0]-200;
		player_2.setWidth(player_2.getAnimaciones().get(0).getWidth()[0]);
		offset = offset1/2 + offset2/2;
		cargarAtaques();
		//Guardar en brain la lista de ataques
		handler.getGame().getBrain1().setPegar(ataques);
		handler.getGame().getBrain2().setPegar(ataques);
		handler.getGame().getBrain1().setFighter(player_1.getFighter());
		handler.getGame().getBrain2().setFighter(player_2.getFighter());
		
		animacionActual=-1;
	}
	
	private void cargarAtaques() {
		
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (handler.getGame().path+"/.configStreetFighterII/ataques.movs");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
	         String linea;
	         String[] stat = null;
	         br.readLine(); //Eliminamos linea de informacion
	         while((linea=br.readLine())!=null) {        	  
	        	 stat = linea.split(" "); //stat[0]Nombre
	         	 int id = Integer.parseInt(stat[1]);
	         	 int range = Integer.parseInt(stat[2]);
	         	 int damage = Integer.parseInt(stat[3]);
	         	 int risk = Integer.parseInt(stat[4]);
	         	 boolean dH = Integer.parseInt(stat[5]) == 1;
	         	 boolean dM = Integer.parseInt(stat[6]) == 1;
	         	 boolean dL = Integer.parseInt(stat[7]) == 1;
	         	 int frame = Integer.parseInt(stat[8]);
	         	 Movs ataque_i = new Movs(id,range,damage,risk
	         			 ,dH,dM,dL,frame);
	         	 ataques.add(ataque_i);
	         }

			br.close();
			fr.close();
        }
		catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{                    
        		if( null != fr ){   
        			fr.close();     
        		}                  
        	}catch (Exception e2){ 
        		e2.printStackTrace();
        	}
        }
	}
	
	//Se calcula orientacion y se actualizan variables de jugadores
	public void tick(boolean collisionLimCam, boolean collisionLimSt_1, 
					 boolean collisionLimSt_2, boolean collisionChoq) {
		//Gestionamos la orientacion de los personajes:
		if (player_1.getX() < player_2.getX()) {
			//Player_1 esta mas a la izquierda que player_2
			if (!player_1.getOrientacion()) {
				//Cambiamos la orientacion si esta mal
				player_1.setOrientacion(true);
				player_2.setOrientacion(false);
			}
		}
		else {
			//Player_1 esta mas a la derecha que player_2
			if(!player_2.getOrientacion()) {
				//Cambiamos la orientacion si esta mal
				player_1.setOrientacion(false);
				player_2.setOrientacion(true);
			}
		}
		
		//Comprobar interacciones entre los personajes:
		controlAtaques();
		
		//Comprobamos que las posiciones de los personajes sean correctas
		controlEmpuje(collisionLimSt_1, collisionLimSt_2);
		controlX(collisionLimSt_1, collisionLimSt_2);
		controlY();
		
		player_1.tick(collisionLimCam,collisionLimSt_1,collisionChoq);
		player_2.tick(collisionLimCam,collisionLimSt_2,collisionChoq);
	}
	
	//Se dibujan jugadores
	public void render(Graphics g, double xOffset, double yOffset) {
		player_1.render(g, xOffset, yOffset);
		player_2.render(g, xOffset, yOffset);
	}

	//GETTERS AND SETTERS
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer_1() {
		return player_1;
	}

	public void setPlayer_1(Player player_1) {
		this.player_1 = player_1;
	}
	
	public Player getPlayer_2() {
		return player_2;
	}

	public void setPlayer_2(Player player_2) {
		this.player_2 = player_2;
	}
	
	public ArrayList<Movs> getAtaques(){
		return ataques;
	}
	
	//Comprueba si los dos jugadores estan a rango de un ataque
	private boolean rango(Movs ataque) {
		return ((player_1.getOrientacion()
				&& (player_2.getX()+player_2.getWidth()/2) - (player_1.getX()+player_1.getWidth()/2) <= ataque.getRange()+offset)
				||
				(player_2.getOrientacion()
				&& (player_1.getX()+player_1.getWidth()/2) - (player_2.getX()+player_2.getWidth()/2) <= ataque.getRange()+offset));
	}
	
	private boolean golpeSaltando(Player player_2) {
		if(//No esta saltando, y o esta en los primeros el primer frames o el ultimo
				(player_2.getAnimacionActual() != 4 && player_2.getAnimacionActual() != 12 && player_2.getAnimacionActual() != 14)
				||
				  ((player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14)
						  && (player_2.getAnimaciones().get(player_2.getAnimacionActual()).getIndex() < 2
						   || player_2.getAnimaciones().get(player_2.getAnimacionActual()).getIndex() > 5))) 
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	private void hitHadouken(Player player_1, Player player_2, boolean playerOrden) {
		//Golpea el Hadouken:
		player_1.getHadouken().setActivo(false);
		//Animacion de golpeo si estaba saltando
		if (player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14) {
			//Reset salto
			player_2.getAnimaciones().get(player_2.getAnimacionActual()).resetAnimtaion();
			player_2.setAnimacionActual(9);
			player_2.getAnimaciones().get(9).resetAnimtaion();
			player_2.getAnimaciones().get(9).setAnimacionEnCurso(true);
		}
		else if (player_2.getAnimacionActual() == 2) {
			player_2.setAnimacionActual(7);
			player_2.getAnimaciones().get(7).setAnimacionEnCurso(true);
		}
		else {
			player_2.setAnimacionActual(5);
			player_2.getAnimaciones().get(5).setAnimacionEnCurso(true);
		}
		player_2.setVida(player_2.getVida()-player_1.getHadouken().getDamage());
		//Si es RYU, hace mas damage:
		if (player_1.getFighter() == 2) {
			player_2.setVida(player_2.getVida()-5);
		}
		
		//Sumar puntos por golpe al total de puntos
		if (playerOrden) handler.getGame().setPuntos1(handler.getGame().getPuntos1()+500);
		else handler.getGame().setPuntos2(handler.getGame().getPuntos2()+500);
		elegirGolpeSound();
	}
	
	//Comprueba si hay Hadouken de J1 a J2:
	private void ataqueHadouken(Player player_1, Player player_2, boolean playerOrden) {
		//Comprobamos Hadouken [Chun o Ryu]
		if (player_1.getHadouken().getActivo()) {
			//Comprobamos si el Hadouken da al rival
			if ((player_1.getHadouken().getOrientacion() 
					&& (player_2.getX() - player_1.getHadouken().getX()+player_1.getHadouken().getSprite().getWidth() < 0)
					&& player_2.getX() - player_1.getHadouken().getX()+player_1.getHadouken().getSprite().getWidth() > -40)
						||
				(!player_1.getHadouken().getOrientacion() 
						&& (player_1.getHadouken().getX() - (player_2.getX()+player_2.getWidth()/2) < 0)
						&& player_1.getHadouken().getX() - (player_2.getX()+player_2.getWidth()/2) > -40)) {
					//Si el jugador 2 no lo esquiva
					if(golpeSaltando(player_2))
					{
						hitHadouken(player_1, player_2, playerOrden);
					}
			}
			
			//Por si sigue activo pero lo hemos esquivado en el primer frame
				if (player_1.getHadouken().getFirstFrame()) {
					if ((player_1.getHadouken().getOrientacion() && player_2.getX()+player_2.getWidth()/2 < player_1.getHadouken().getX()+70)
					|| (!player_1.getHadouken().getOrientacion() && player_2.getX()+player_2.getWidth()/2 > player_1.getHadouken().getX())) {
						if(golpeSaltando(player_2))
						{	
							hitHadouken(player_1, player_2, playerOrden);
						}
				}
			}
			//Indicamos que ya hemos comprobado el primer frame
			player_1.getHadouken().setFirstFrame(false);
		}
	}
	
	//Comprueba si P1 ataca a P2:
	private void ataque(Player player_1, Player player_2, boolean playerOrden) {
		if (esAtaque (player_1.getAnimacionActual(), player_1.fighter) && player_1.getGolpeando()){
			Movs ataque = getAtaque(player_1.getAnimacionActual());
			//Comprobamos que el ataque pueda seguir haciendo da�o
			if (player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex()
					< ataque.getFrameAtaque()
				&&
				//No se puede hacer damage en el frame 0 de la animacion
				player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex()
				> 0
				//Comprobamos que estamos a rango
				&&rango(ataque)){
				//Contrario esta agachado y el ataque puede afectarle
				if (ataque.getdL() && (player_2.getAnimacionActual() == 2 || player_2.getAnimacionActual() == 18 )) {
					player_1.setGolpeando(false);
					if (player_2.getAnimacionActual() == 2 || player_1.getAnimacionActual() == 17) {
						player_2.setAnimacionActual(7);
						player_2.getAnimaciones().get(7).setAnimacionEnCurso(true);
						player_2.setVida(player_2.getVida()-ataque.getDamage());
						//Si es RYU, hace mas damage:
						if (player_1.getFighter() == 2) {
							player_2.setVida(player_2.getVida()-5);
						}
						//Sumar puntos por golpe al total de puntos
						if (playerOrden) handler.getGame().setPuntos1(handler.getGame().getPuntos1()+500);
						else handler.getGame().setPuntos2(handler.getGame().getPuntos2()+500);
						elegirGolpeSound();
					}
					//BLOQUEO
					else {
						handler.getGame().setSoundEffect(7,true);
						player_2.getAnimaciones().get(18).resetAnimtaion();
						player_2.getAnimaciones().get(18).setAnimacionEnCurso(true);
					}
				}
				//Contrario esta de pie y el ataque puede afectarle
				else if (ataque.getdM() 
						//No esta agachado
						&& player_2.getAnimacionActual() != 2 && player_2.getAnimacionActual() != 18 
						//No esta saltando, y o esta en los primeros el primer frams o el ultimo
						&& golpeSaltando(player_2)) {
					
					player_1.setGolpeando(false);
					//Comprobamos si el contrario esta bloqueando el ataque
					if ((((player_2.keys_p[2] && player_2.getOrientacion()) || (player_2.keys_p[3] && player_1.getOrientacion()))
							&&
							//Si el ataque tambien afecta por abajo, no lo bloquea
							!ataque.getdL()) || (player_1.getAnimacionActual()==17 && ((player_2.keys_p[2] && player_2.getOrientacion()) || (player_2.keys_p[3] && player_1.getOrientacion())))) {
						player_2.setAnimacionActual(8);
						handler.getGame().setSoundEffect(7,true);
						player_2.getAnimaciones().get(8).setAnimacionEnCurso(true);
					}
					//Le golpea
					else {
						//Animacion de golpeo si estaba saltando
						if (player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14) {
							//Reset salto
							player_2.getAnimaciones().get(player_2.getAnimacionActual()).resetAnimtaion();
							player_2.setAnimacionActual(9);
							player_2.getAnimaciones().get(9).resetAnimtaion();
							player_2.getAnimaciones().get(9).setAnimacionEnCurso(true);
						}
						else {
							player_2.setAnimacionActual(5);
							player_2.getAnimaciones().get(5).setAnimacionEnCurso(true);
						}
						player_2.setVida(player_2.getVida()-ataque.getDamage());
						//Si es RYU, hace mas damage:
						if (player_1.getFighter() == 2) {
							player_2.setVida(player_2.getVida()-5);
						}
						//Sumar puntos por golpe al total de puntos
						if (playerOrden) handler.getGame().setPuntos1(handler.getGame().getPuntos1()+500);
						else handler.getGame().setPuntos2(handler.getGame().getPuntos2()+500);
						elegirGolpeSound();
					}
				}
				//Si el ataque hace da�o cuando el contrario est� arriba y puede afectarle
				else if (ataque.getdH() && (player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14)) {
					player_1.setGolpeando(false);
					//Reset salto
					player_2.getAnimaciones().get(player_2.getAnimacionActual()).resetAnimtaion();
					player_2.setAnimacionActual(9);
					player_2.getAnimaciones().get(9).resetAnimtaion();
					player_2.getAnimaciones().get(9).setAnimacionEnCurso(true);
					player_2.setVida(player_2.getVida()-ataque.getDamage());
					//Si es RYU, hace mas damage:
					if (player_1.getFighter() == 2) {
						player_2.setVida(player_2.getVida()-5);
					}
					//Sumar puntos por golpe al total de puntos
					if (playerOrden) handler.getGame().setPuntos1(handler.getGame().getPuntos1()+500);
					else handler.getGame().setPuntos2(handler.getGame().getPuntos2()+500);
					elegirGolpeSound();
				}
			}
			//Si lanza golpe pero no alcanza al otro, suena golpe al aire
			else {
				if(player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex()==0 && animacionActual==-1) {
					handler.getGame().setSoundEffect(11, true);
					animacionActual=player_1.getAnimacionActual();
				}
				if(player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex()!=0) {
					animacionActual=-1;
				}
				//System.out.println(player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex());
				//System.out.println(player_1.getAnimacionActual());
			}
		}
		ataqueHadouken(player_1, player_2, playerOrden);
	}
	
	private void choqueHadouken(Player player_1, Player player_2) {
		//Hadouken 1 de izquierda a derecha
		if (player_1.getHadouken().getOrientacion()) {
			if (player_2.getHadouken().getX() < player_1.getHadouken().getX()+player_1.getHadouken().getSprite().getWidth()) {
				player_1.getHadouken().setActivo(false);
				player_2.getHadouken().setActivo(false);
			}
		}
		//Hadouken 1 de derecha a izquierda
		else {
			if (player_1.getHadouken().getX() < player_2.getHadouken().getX()+player_2.getHadouken().getSprite().getWidth()) {
				player_1.getHadouken().setActivo(false);
				player_2.getHadouken().setActivo(false);
			}
		}
	}
	
	//Gestion de ataques y movimientos
	//Control ataques
	private void controlAtaques(){
		//Si los dos jugadores han lanzado un hadouken, comprobamos choque:
		if (player_1.getHadouken().getActivo() && player_2.getHadouken().getActivo()) {
			choqueHadouken(player_1,player_2);
		}
		//Comprobamos que el jugador 1 esta atacando
		ataque(player_1,player_2, true);
		//Comprobamos que el jugador 2 esta atacando
		ataque(player_2,player_1, false);
	}
	
	private Movs getAtaque(int id) {
		for (Movs ataque : ataques){
			if (ataque.getId() == id) {
				return ataque;
			}
		}
		return null;
	}
	
	private boolean esAtaque(int id, int fighter) {
		//Si es la ID 11 es ataque especial, solo es ataque fisico de blanka
		if (id == 11 && fighter != 0) {
			return false;
		}
		else {
			for (Movs ataque : ataques){
				if (ataque.getId() == id && ataque.getDamage() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	//Gestiona si los jugadores se están empujando
	private void controlEmpuje(boolean collisionLimSt_1, boolean collisionLimSt_2) {
		//Si los dos se están empujando o uno salta, o uno ha llegado al limite del stage, no se hace nada
		if((player_1.getEmpujando() && player_2.getEmpujando()) 
				|| (collisionLimSt_1 || collisionLimSt_2)
				|| (player_1.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 4
				|| player_1.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 12
				|| player_1.getAnimacionActual() == 14 || player_2.getAnimacionActual() == 14)) {
			player_1.setEmpujando(false);
			player_2.setEmpujando(false);
			player_1.setEsEmpujado(false);
			player_2.setEsEmpujado(false);
			//Permitimos que se puedan seguir empujando en el limite
			if (collisionLimSt_1) {
					player_1.setEmpujando(true);
			}
			if (collisionLimSt_2) {
					player_2.setEmpujando(true);
			}
		}
		else {
			//Si el player_1 está empujando al 2, lo indica
			if(player_1.getEmpujando()) {
				player_2.setEsEmpujado(true);
			}
			else {
				player_2.setEsEmpujado(false);
			}
			
			//Si el player_2 está empujando al 1, lo indica
			if(player_2.getEmpujando()) {
				player_1.setEsEmpujado(true);
			}
			else {
				player_1.setEsEmpujado(false);
			}	
		}
	}
	
	//Controla la distancia de los personajes (Para que no se superpongan si uno salta por encima del otro)
	private void controlX(boolean collisionLimSt_1,boolean collisionLimSt_2) {
		int distancia = (int) (100 + offset); //Distancia minima entre los personajes
		int velocidad = 10; //Velocidad del retroceso
		//Player_1 retroceso  hacia la izquierda y 2 hacia la derecha
		if (player_1.getOrientacion() &&
				((player_2.getX()+player_2.getWidth()/2) - (player_1.getX()+player_1.getWidth()/2)) < distancia) {
			//Si el jugador 1 o 2 estan saltando (y aun no estan cayendo) no hay retroceso
			if (!(((player_1.getAnimacionActual() == 4 || player_1.getAnimacionActual() == 12 || player_1.getAnimacionActual() == 14) 
					&& player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex() <5)
					||
					((player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14) 
					&& player_2.getAnimaciones().get(player_2.getAnimacionActual()).getIndex() <5))) {
				//Teniendo en cuenta el limite del stage
				if (!(collisionLimSt_1 || collisionLimSt_2)) {
					player_1.setX(player_1.getX()-velocidad);
					player_2.setX(player_2.getX()+velocidad);
				}
				if (collisionLimSt_1) {
					player_2.setX(player_2.getX()+velocidad*3);
					player_1.setX(player_1.getX()+velocidad*3);
				}
				if (collisionLimSt_2) {
					player_1.setX(player_1.getX()-velocidad*3);
					player_2.setX(player_2.getX()-velocidad*3);
				}
			
			}
		}
		else if (!(player_1.getOrientacion()) &&
				((player_1.getX()+player_1.getWidth()/2) - (player_2.getX()+player_2.getWidth()/2)) < distancia){
			//Si el jugador 1 o 2 estan saltando (y aun no estan cayendo) no hay retroceso
			if (!(((player_1.getAnimacionActual() == 4 || player_1.getAnimacionActual() == 12 || player_1.getAnimacionActual() == 14) 
					&& player_1.getAnimaciones().get(player_1.getAnimacionActual()).getIndex() <5)
					||
					((player_2.getAnimacionActual() == 4 || player_2.getAnimacionActual() == 12 || player_2.getAnimacionActual() == 14) 
					&& player_2.getAnimaciones().get(player_2.getAnimacionActual()).getIndex() <5))) {
				//Teniendo en cuenta el limite del stage
				if (!(collisionLimSt_1 || collisionLimSt_2)) {
					player_1.setX(player_1.getX()+velocidad);
					player_2.setX(player_2.getX()-velocidad);
				}
				
				if (collisionLimSt_1) {
					player_1.setX(player_1.getX()-velocidad*3);
					player_2.setX(player_2.getX()-velocidad*3);
				}
				if (collisionLimSt_2) {
					player_2.setX(player_2.getX()+velocidad*3);
					player_1.setX(player_1.getX()+velocidad*3);
				}
				
			}
		}
	}
	
	//Controla que la Y se posicione correctamente cuando la actualizamos (Saltando)
	void controlY() {
		//Y del jugador 1
		if ((player_1.getY() > player_1.getY_original() || player_1.getY() < player_1.getY_original())
				&& player_1.getAnimacionActual() != 4 && player_1.getAnimacionActual() != 9 
				&& player_1.getAnimacionActual() != 12 && player_1.getAnimacionActual() != 13
				&& player_1.getAnimacionActual() != 14 && player_1.getAnimacionActual() != 17) {
			
				player_1.setY(player_1.getY_original());

		}
		//Y del jugador 2
		if ((player_2.getY() > player_2.getY_original() || player_2.getY() < player_2.getY_original())
				&& player_2.getAnimacionActual() != 4 && player_2.getAnimacionActual() != 9 
				&& player_2.getAnimacionActual() != 12 && player_2.getAnimacionActual() != 13
				&& player_2.getAnimacionActual() != 14 && player_2.getAnimacionActual() != 17) {
			
				player_2.setY(player_2.getY_original());
		}
	}
	
	void elegirGolpeSound() {
		int golpe = (int)Math.floor(Math.random()*3);
		
		switch (golpe) {
        case 0: handler.getGame().setSoundEffect(8,true); 
        case 1: handler.getGame().setSoundEffect(9,true);
        case 2: handler.getGame().setSoundEffect(10,true); 
		}		
	}
	
	
}