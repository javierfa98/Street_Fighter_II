package streetfighter.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import streetfighter.Handler;
import streetfighter.gfx.Animation;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;
import streetfighter.input.ComboManager;
import streetfighter.utils.Utils;

public class Player {
	
	//Vida inicial
		public static final int VIDA_INICIAL = 469;
		
		ArrayList<Animation> animaciones = new ArrayList<Animation>();
		protected int animacionActual=0;
		
		protected Handler handler;
		//Posicion del jugador
		protected float x,y;
		protected boolean p1;
		//Y inicial
		protected float y_original;
		//Tamano del jugador
		protected int width,height;
		//Rectangulo de colision del jugador
		protected Rectangle bounds;
		//Vida que tiene el jugador
		protected float vida;
		//Velocidad del jugador
		protected int speed;
		//Luchador que va a usar el jugador
		protected int fighter;
		//Orientacion del jugador (True == Mirando hacia la derecha)
		protected boolean orientacion;
		protected boolean orientacionFJ; //Orientacion cuando iniciamos un salto frontal
		//Inclinacion al retroceder:
		protected boolean inclinacion;
		//Indica si el jugador est� golpeando
		protected boolean golpeando;
		//Indica si el jugador est� empujando
		protected boolean empujando;
		//Indica si el jugador est� siendo empujado
		protected boolean esEmpujado;
		//Objeto hadoken del personaje [Ryu y CHUN]
		protected Hadouken hadouken;
		protected boolean hadoukenLanzado;
		
		//Teclas que tienen todos los jugadores
		public boolean[] keys_p;
		public boolean stop;
		
		//Velocidad de cada personaje:
		private int speedMov;
		
		//Si uno de los dos jugadores est� KO:
		public boolean KO = false;
		
		//Especial IA
		boolean esIA;
		protected BufferedImage blankaName, chunName, ryuName, player1, player2;
		
		protected LettersNumbers letNums;
	
	public Player(Handler handler,float x, float y, int width, int height,int fighter,int speed, boolean orientacion, boolean p1, boolean esIA) {
		this.handler=handler;
		this.x=x;
		this.y=y;
		y_original = y;
		this.width=width;
		this.height=height;
		this.vida = VIDA_INICIAL; //La vida tiene que ser 469 para que se ajuste bien a la barra de vida
		this.speed=speed;
		this.fighter=fighter;
		this.orientacion=orientacion;
		this.p1 = p1;
		inclinacion = false;
		golpeando = false;
		empujando = false;
		esEmpujado = false;
		hadoukenLanzado = false;
		this.esIA=esIA;
		blankaName = ImageLoader.loadImage("/res/textures/rankingMenu/blankaTrans.png");
		chunName = ImageLoader.loadImage("/res/textures/rankingMenu/chunTrans.png");
		ryuName = ImageLoader.loadImage("/res/textures/rankingMenu/ryuTrans.png");
		player1 = ImageLoader.loadImage("/res/textures/rankingMenu/player1.png");
		player2 = ImageLoader.loadImage("/res/textures/rankingMenu/player2.png");
		letNums = new LettersNumbers();
		
		if (p1) {
			animaciones=Assets.getAnimation(fighter);
		}
		else {
			animaciones=Assets.getAnimation2(fighter);
		}
		
		bounds=new Rectangle(0,0,width,height);
		
		//Hadouken para RYU o CHUN
		this.hadouken = new Hadouken(0,0,false, false);
		if (fighter == 2) {
			hadouken.setSprite(Assets.hadoukenRyu);
		}
		else if(fighter == 1) {
			hadouken.setSprite(Assets.hadoukenChun);
		}
		
		//Velocidad de cada personaje:
		//Blanka
		if (fighter == 0) {
			speedMov = 7;
		}
		//Chun
		else if (fighter == 1) {
			speedMov = 13;
		}
		//Por defecto
		else {
			speedMov = 10;
		}
	}
	
	//Inicia la animacion de un ataque fisico:
	private void attack(int id) {
		animacionActual = id;
		//Es un ataque fisico
		golpeando = true;
		//Animacion que cuando empieza, debe terminar
		animaciones.get(animacionActual).setAnimacionEnCurso(true);
		animaciones.get(animacionActual).resetAnimtaion();
	}
	
public void tick_manager() {}
	
	//Gestion de animaciones
	int N_salto = 6;
	int N_retroceso = 2;
	int veces=0;
	int estado_ant = 0;
	
	public void tick(boolean collisionLimCam, boolean collisionLimSt, boolean collisionChoq) {
	//Actualizamos las teclas
	tick_manager();
	
	animaciones.get(animacionActual).tick();
	//Por defecto, no estamos inclinados, solo al retroceder
	if (getInclinacion()) {
		setInclinacion(false);
	}
	//Si estamos empujando
	if (getEmpujando()) {
			if(orientacion) {
				if (!collisionLimSt) {
					x=x+speed/2;
				}
				//RIGHT
				else if (collisionLimSt && keys_p[3]) {
					x=x+speed/2;
				}
			}
			else {
				if (!collisionLimSt) {
					x=x-speed/2;
				}
				//LEFT
				else if (collisionLimSt && keys_p[2]) {
					x=x-speed/2;
				}
			}	
		setEmpujando(false);
	}
	//[1]Comprobar si estamos en una animacion que debe terminar o si la pelea ha terminado
	if (!animaciones.get(animacionActual).getAnimacionEnCurso()) {
		//No podemos estar golpeando
		if(golpeando) {
			handler.getGame().getKeyManager().tick();
			golpeando = false;
		}
		//[2]Comprobamos que se haya pulsado alguna tecla o estamos KO, si no nos paramos
		if (stop || KO) {
			animacionActual=0;
			if(esEmpujado) {
				//Mirando a la derecha
				if(orientacion) {
					x = x - speed/2;
				}
				else {
					x = x + speed/2;
				}
			}
		}
		else {
			//Si hemos pulsado alguna tecla
			
			////////////////////MOVIMIENTO////////////////////////
			
			//FORWARD JUMP
			if ((keys_p[0] && keys_p[3] && orientacion) ||
					(keys_p[0] && keys_p[2] && !orientacion)) {
				//Nos aseguramos de que estamos en el suelo
				if (y == y_original) {
					orientacionFJ = orientacion;
					handler.getGame().setSoundEffect(11,true); 
					animacionActual=12;
					//Animacion que cuando empieza, debe terminar
					animaciones.get(animacionActual).setAnimacionEnCurso(true);
				}
				else {
					animacionActual = 0;
				}
			}
			//BACK JUMP
			else if ((keys_p[0] && keys_p[2] && orientacion) ||
					(keys_p[0] && keys_p[3] && !orientacion)) {
				//Nos aseguramos de que estamos en el suelo
				if (y == y_original) {
					orientacionFJ = orientacion;
					handler.getGame().setSoundEffect(11,true); 
					animacionActual=14;
					//Animacion que cuando empieza, debe terminar
					animaciones.get(animacionActual).setAnimacionEnCurso(true);
				}
				else {
					animacionActual = 0;
				}
			}
			//JUMP
			else if(keys_p[0]){
				//Nos aseguramos de que estamos en el suelo
				if (y == y_original) {
					handler.getGame().setSoundEffect(11,true); 
					animacionActual=4;
					//Animacion que cuando empieza, debe terminar
					animaciones.get(animacionActual).setAnimacionEnCurso(true);
				}
				else {
					animacionActual = 0;
				}
			}
			//CROUCH
			else if(keys_p[1]){
				//CROUCH BLOCK
				if (orientacion && keys_p[2]) {
					animacionActual = 18;
				}
				//CROUCH BLOCK
				else if (!orientacion && keys_p[3]){
					animacionActual = 18;
				}
				else {
					animacionActual=2;
				}
			}
			
			//Si estoy mirando hacia la derecha
			else if(orientacion) {
				//LEFT
				if(keys_p[2]){
					//No vayas hacia atras si te sales de pantalla o del stage
					if(!collisionLimCam && !collisionLimSt)
						x=x-speedMov/2;
					setInclinacion(true);
					animacionActual=1;
				}
				//RIGHT
				if(keys_p[3]){
					//No vayas hacia delante si te chocas con rival
					if(!collisionChoq)
						x=x+speedMov;
					else {
						empujando = true;
					}
					animacionActual=1;
				}	
			}
			//Si estoy mirando hacia la izquierda
			else {
				//LEFT
				if(keys_p[2]){
					//No vayas hacia delante si te chocas con rival
					if(!collisionChoq)
						x=x-speedMov;
					else {
						empujando = true;
					}
					animacionActual=1;
				}
				//RIGHT
				if(keys_p[3]){
					//No vayas hacia atras si te sales de pantalla o del stage
					if(!collisionLimCam && !collisionLimSt)
						x=x+speedMov/2;
					setInclinacion(true);
					animacionActual=1;
				}
			}	

			////////////////////ATAQUE////////////////////////
			
			//SPECIAL
			if ((fighter == 2 || fighter == 1) && ((ComboManager.especial(p1) && !esIA) || (esIA && keys_p[7])) && !hadouken.getActivo()) {
				//Ataque especial de Ryu o Chun (Hadouken)
				animacionActual = 11;
				//La orientacion del hadouken se fija desde el primer momento
				hadouken.setOrientacion(orientacion);
				hadouken.setY((int) (y+height/4));
				if (orientacion) {
					hadouken.setX((int) x + width);
				}
				else {
					hadouken.setX((int) x - width);
				}
				//Animacion que cuando empieza, debe terminar
				animaciones.get(animacionActual).setAnimacionEnCurso(true);
				animaciones.get(animacionActual).resetAnimtaion();
			}
			else if(fighter == 0 && ((ComboManager.especial(p1) && !esIA) || (esIA && keys_p[7]))) {
				//Ataque especial de BLANKA (Electric shock)
				handler.getGame().setSoundEffect(15,true);
				attack(11);
			}
			//PUNCH
			else if(keys_p[4]){
				//Si estamos agachados, pu�etazo agachado
				if (keys_p[1]) {
					attack(15);
				}
				else {
					attack(3);
				}
			}
			//KICK LOW
			else if(keys_p[5]){
				//Si estamos agachados, patada agachado
				if (keys_p[1]) {
					attack(16);
				}
				else {
					attack(6);
				}
			}
			//KICK HIGH
			else if(keys_p[6]) {
				attack(10);
			}
		}
	}
	
	// ANIMACIONES EN CURSO QUE NO PUEDEN SER INTERRUMPIDAS
	
	else {
		//INICIO DEL HIGH PUNCH
		if(keys_p[4] && (getAnimacionActual() == 4 || getAnimacionActual() == 12 || getAnimacionActual() == 14)
				  && (getAnimaciones().get(getAnimacionActual()).getIndex() >= 2
				   || getAnimaciones().get(getAnimacionActual()).getIndex() <= 5)) {
			//Reseteamos la animacion de salto y la sustituimos
			getAnimaciones().get(getAnimacionActual()).resetAnimtaion();
			attack(17);
		}
		
		//Control del HIGH PUNCH
		if (animacionActual == 17) {
			int frame = animaciones.get(animacionActual).getIndex();
			//Si hemos cambiado de estado
			if (frame != estado_ant) {
				estado_ant = frame;
				veces = 0;
			}
			//Nos movemos en diagonal hacia el contrario, si se puede
			if (veces < 3) {
				if (orientacion) {
					if(!collisionLimCam && !collisionLimSt) {
						x += speed;
					}
				}
				else {
					if(!collisionLimCam && !collisionLimSt) {
						x -= speed;
					}
				}
			}
			//Caemos
			if (veces < 6 && y < y_original) {
				y += speed + 6;
			}
			veces++;
		}
		
		//KO
		if (animacionActual == 13) {
			int frame = animaciones.get(animacionActual).getIndex();
			//Si hemos cambiado de estado
			if (frame != estado_ant) {
				estado_ant = frame;
			}
			//En los frames 0 y 1 aplicamos retroceso
			if (frame < 2) {
				//Gestion de la X
				if (orientacion) {
					//Retroceso hacia izquierda
					if(!collisionLimCam && !collisionLimSt) {
						x -= speed/2;
					}
				}
				else {
					//Retroceso hacia derecha
					if(!collisionLimCam && !collisionLimSt) {
						x += speed/2;
					}
				}
				//Gestion de la Y
				//Si ya estamos en el suelo no disminuimos
				if (y < y_original) {
					y += (speed+4)/2;
				}
			}
		}
		
		//SALTO
		if(animacionActual == 4 || animacionActual == 12 || animacionActual == 14) {
			//Estamos saltando, hay que actualizar Y (N veces por cada estado)
			int j = animaciones.get(animacionActual).getIndex();
			//Si hemos cambiado de estado
			if (j != estado_ant) {
				estado_ant = j;
				veces = 0;
			}
			//1,2 y 3 aumentamos y
			if ( j > 0 && j < 4 && veces < N_salto) {
				y -= speed + 6;
			}
			//5,6 y 7 disminuimos y
			else if (j > 4 && veces < N_salto) {
				y += speed + 6;
			}
			//FORWARD JUMP (Actualizamos tambien la X)
			if (animacionActual == 12) {
				if(veces < N_salto) {
					//Si no nos chocamos con el limite del escenario
					if(!collisionLimCam && !collisionLimSt) {
						if(orientacionFJ) {
							x+= 12;
						}
						else {
							x-= 12;
						}
					}
				}
			}
			//BACK JUMP (Actualizamos tambien la X)
			if (animacionActual == 14) {
				if(veces < N_salto) {
					//Si no nos chocamos con el limite del escenario
					if(!collisionLimCam && !collisionLimSt) {
						if(orientacionFJ) {
							x-= 6;
						}
						else {
							x+= 6;
						}
					}
				}
			}
			veces++;
		}
		
		
		//PATADA ALTA DE BLANKA (CORREGIMOS LA Y)
		if (animacionActual == 10 && fighter == 0) {
			int frame = animaciones.get(animacionActual).getIndex();
			if (frame == 2 || frame == 3) {
				y = y_original - 50;
			}
			else if (frame == 4) {
				y = y_original;
			}
		}
		
		//GOLPEADO MEDIO, BAJO o ALTO (Retroceso) o BLOCK/BLOCK BAJO
		if(animacionActual == 5 ||animacionActual == 7 || animacionActual == 8 || animacionActual == 9 || animacionActual == 18) {
			int j = animaciones.get(animacionActual).getIndex();
			//Si hemos cambiado de estado
			if (j != estado_ant) {
				estado_ant = j;
				veces = 0;
			}
			if (veces < N_retroceso) {
				if (orientacion) {
					//Retroceso hacia izquierda
					if(!collisionLimCam && !collisionLimSt) {
						x -= speed;
						//Separamos m�s si estamos en retroceso alto
						if (animacionActual == 9) x -= speed;
					}
				}
				else {
					//Retroceso hacia derecha
					if(!collisionLimCam && !collisionLimSt) {
						x += speed;
						//Separamos m�s si estamos en retroceso alto
						if (animacionActual == 9) x += speed;
					}
				}
			}
			if (veces < N_salto) {
				//Si estamos en un golpeo alto, modificamos tambien la Y
				if (animacionActual == 9) {
					//Aumentamos la Y los dos primeros frames
					if (j < 2) {
						y -= speed-2;
					}
					//Disminuimos los ultimos 3 frames
					else {
						//Si ya estamos en el suelo no disminuimos
						if (y < y_original) {
							y += speed+4;
						}
					}
				}
			}
			veces++;
		}
		
		//Lanzar Hadouken (Ryu o Chun)
		if (animacionActual == 11 && (fighter == 2 || fighter == 1)) {
			int frame = animaciones.get(animacionActual).getIndex();
			if (frame == 4 && !hadoukenLanzado) {
				//RYU
				if(fighter==2) {
					handler.getGame().setSoundEffect(17,true);
				}
				//CHUN LI
				else {
					handler.getGame().setSoundEffect(16,true);
				}
				
				hadouken.setDistancia(0);
				hadouken.setActivo(true);
				hadoukenLanzado = true;
			}
			else if (frame == 5) {
				hadoukenLanzado = false;
			}
		}
	}
	//Tick hadouken, si hay y est� activo
	if (hadouken.getActivo()) {
		hadouken.tick();
	}
	
}
	
	public void render_stats(Graphics g) {}

	public void render(Graphics g, double xOffset, double yOffset) {
		boolean orientacionFighter;
		if (animacionActual == 11 && fighter != 0) {
			orientacionFighter = hadouken.getOrientacion(); //Mantenemos la orientacion mientras lanzamos hadouken
		}
		else {
			orientacionFighter = orientacion;
		}
		if (orientacionFighter) { //Mirando hacia la derecha
			//Si no estamos retrocediendo
			if (!getInclinacion()) {
				g.drawImage(animaciones.get(animacionActual).getCurrentFrame(), 
						(int) (x-xOffset), (int) (y + yOffset), animaciones.get(animacionActual).getWidthActual(),
						animaciones.get(animacionActual).getHeightActual(), null);
			}
			else {
			//Retrocediendo inclinamos la imagen
				g.drawImage(Utils.rotateImageByDegrees(Utils.resize(animaciones.get(animacionActual).getCurrentFrame(),
						(int) (animaciones.get(animacionActual).getWidthActual()),
						(int) (animaciones.get(animacionActual).getHeightActual()) ),-5), 
						(int) (x-xOffset), (int) (y + yOffset), (int) (animaciones.get(animacionActual).getWidthActual()*1.05),
						(int) (animaciones.get(animacionActual).getHeightActual()*1.05), null);
			}
		}
		else { //Mirando hacia la izquierda, hay que invertirlo horizontalmente
			Image image;
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-animaciones.get(animacionActual).getCurrentFrame().getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = op.filter(animaciones.get(animacionActual).getCurrentFrame(), null);
			//Si no estamos retrocediendo
			if (!getInclinacion()) {
				//Si la anchura es mayor que la de por defecto, mirando hacia la izquierda corregimos la posicion
				// (Si no lo hicieramos, se produce un desplazamiento)
				if (animaciones.get(animacionActual).getWidthActual() > width) {
					int diferencia = animaciones.get(animacionActual).getWidthActual()-width;
					g.drawImage(image, (int) (x-xOffset-diferencia), (int) (y + yOffset), animaciones.get(animacionActual).getWidthActual(),
							animaciones.get(animacionActual).getHeightActual(), null);
				}
				else {
					g.drawImage(image, (int) (x-xOffset), (int) (y + yOffset), animaciones.get(animacionActual).getWidthActual(),
							animaciones.get(animacionActual).getHeightActual(), null);
				}
			}
			else {
				//Retrocediendo inclinamos la imagen
				g.drawImage(Utils.rotateImageByDegrees(Utils.resize((BufferedImage) image,
						(int) (animaciones.get(animacionActual).getWidthActual()),
						(int) (animaciones.get(animacionActual).getHeightActual()) ),5), 
						(int) (x-xOffset), (int) (y + yOffset), (int) (animaciones.get(animacionActual).getWidthActual()*1.05),
						(int) (animaciones.get(animacionActual).getHeightActual()*1.05), null);
			}
		}
		if (hadouken.getActivo()) {
			hadouken.render(g, xOffset);
		}
		render_stats(g);
	}
	
	public float getVida() {
		return vida;
	}
	
	public void setVida(float vida) {
		this.vida = vida;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setOrientacion(boolean orientacion) {
		this.orientacion = orientacion;
	}
	
	public boolean getOrientacion() {
		return orientacion;
	}

	public boolean getInclinacion() {
		return inclinacion;
	}

	public void setInclinacion(boolean inclinacion) {
		this.inclinacion = inclinacion;
	}

	public ArrayList<Animation> getAnimaciones() {
		return animaciones;
	}

	public void setAnimaciones(ArrayList<Animation> animaciones) {
		this.animaciones = animaciones;
	}

	public int getAnimacionActual() {
		return animacionActual;
	}

	public void setAnimacionActual(int animacionActual) {
		this.animacionActual = animacionActual;
	}

	public boolean getGolpeando() {
		return golpeando;
	}

	public void setGolpeando(boolean golpeando) {
		this.golpeando = golpeando;
	}

	public boolean getEmpujando() {
		return empujando;
	}

	public void setEmpujando(boolean empujando) {
		this.empujando = empujando;
	}

	public boolean getEsEmpujado() {
		return esEmpujado;
	}

	public void setEsEmpujado(boolean esEmpujado) {
		this.esEmpujado = esEmpujado;
	}

	public float getY_original() {
		return y_original;
	}

	public void setY_original(float y_original) {
		this.y_original = y_original;
	}
	
	public Hadouken getHadouken() {
		return hadouken;
	}

	public void setHadouken(Hadouken hadouken) {
		this.hadouken = hadouken;
	}
	
	public int getFighter() {
		return fighter;
	}
	
	
}
