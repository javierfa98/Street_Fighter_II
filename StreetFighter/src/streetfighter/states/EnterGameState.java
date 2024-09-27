package streetfighter.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import streetfighter.Handler;
import streetfighter.gfx.Animation;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.utils.Utils;
import streetfighter.worlds.World;

public class EnterGameState extends State{
	
	// Graficos
	private BufferedImage matonRubio, matonMoreno, punyetazoRubio, 
						  morenoSangre, morenoCayendo, edificios,
						  streetLogo, worldWarriorLogo, pressEnter, fondoCielo,
						  nube1, nube2, nube3, publico, arbol1, arbol2, arbol3,
						  publico2,fondo, capcom;
	private boolean todoNegro;
	private boolean parpadeaWorldWarrior;
	
	// Manejar tiempos
	private int i;
	private int iPressEnter;
	private boolean siguientePantalla;
	
	// Manejar posiciones pantalla
	private int xMorenoCayendo;
	private int yMorenoCayendo;
	private int yOffset;
	private int xOffsetNube1;
	private int xOffsetNube2;
	private int xOffsetNube3;
	private int degrees;
	private int spawnX, spawnY, widthOffset, heightOffset;
	private int indexWorldWarrior;
	private int[] spawnXWorldWarrior = {0, 1240, 40, 1140, 80, 1040, 120, 
			 							840, 160, 640, 200, 440, 240,265};
	
	// Reposo matones rubio y moreno
	private Animation anim_matonRubio;
	private Animation anim_matonMoreno;
	
	// Animacion publico
	private Animation anim_publico;
	
	// Momento de dar punyetazo
	//private long initial;
	private long punyetazoTime;
	private boolean punyetazo;
	
	// Fade
	private float alpha;
	private boolean fade;
	
	//Mostrar pelea demo
	int espera=400;
	boolean peleaDemo=false;
	World world;
	boolean evitarRebotes=false;
	int rebotes=20;
	boolean finPelea=false;
	
	// Constructor
	public EnterGameState(Handler handler) {
		super(handler);
		
		matonRubio=ImageLoader.loadImage("/res/textures/enterGameMenu/matonRubio.png");
		matonMoreno=ImageLoader.loadImage("/res/textures/enterGameMenu/matonMoreno.png");
		punyetazoRubio=ImageLoader.loadImage("/res/textures/enterGameMenu/punyetazoRubio.png");
		morenoSangre=ImageLoader.loadImage("/res/textures/enterGameMenu/morenoSangre.png");
		morenoCayendo=ImageLoader.loadImage("/res/textures/enterGameMenu/morenoCayendo.png");
		edificios=ImageLoader.loadImage("/res/textures/enterGameMenu/edificios3.png");
		streetLogo=ImageLoader.loadImage("/res/textures/enterGameMenu/streetLogo3.png");
		worldWarriorLogo=ImageLoader.loadImage("/res/textures/enterGameMenu/worldWarriorLogo.png");
		pressEnter=ImageLoader.loadImage("/res/textures/abc.png");
		fondoCielo=ImageLoader.loadImage("/res/textures/enterGameMenu/fondoIntro.png");
		nube1=ImageLoader.loadImage("/res/textures/enterGameMenu/nubes1_1.png");
		nube2=ImageLoader.loadImage("/res/textures/enterGameMenu/nubes1_2.png");
		nube3=ImageLoader.loadImage("/res/textures/enterGameMenu/nubes1_3.png");
		publico=ImageLoader.loadImage("/res/textures/enterGameMenu/publico.png");
		arbol1=ImageLoader.loadImage("/res/textures/enterGameMenu/arbol1Modif.png");
		arbol2=ImageLoader.loadImage("/res/textures/enterGameMenu/arbol2Modif.png");
		arbol3=ImageLoader.loadImage("/res/textures/enterGameMenu/arbol3Modif.png");
		publico2=ImageLoader.loadImage("/res/textures/enterGameMenu/publico2.png");
		fondo=ImageLoader.loadImage("/res/menus/fondo.png");
		capcom=ImageLoader.loadImage("/res/textures/enterGameMenu/capcom.png");
		
		
		BufferedImage[] vec_matonRubio = new BufferedImage[2];
		vec_matonRubio[0] = matonRubio;
		vec_matonRubio[1] = matonRubio;
		anim_matonRubio = new Animation(250,vec_matonRubio);
		
		BufferedImage[] vec_matonMoreno = new BufferedImage[2];
		vec_matonMoreno[0] = matonRubio;
		vec_matonMoreno[1] = matonRubio;
		anim_matonMoreno = new Animation(500,vec_matonMoreno);
		
		BufferedImage[] vec_publico = new BufferedImage[2];
		vec_publico[0] = publico;
		vec_publico[1] = publico2;
		anim_publico = new Animation(600,vec_publico);
		
		todoNegro = false;
		parpadeaWorldWarrior=true;
		
		i = 0;
		iPressEnter=0;
		siguientePantalla=false;
		
		xMorenoCayendo = 125;
		yMorenoCayendo = 245;
		yOffset = 0;
		degrees = 45;
		spawnX = 0;
		spawnY = 0;
		widthOffset = 0;
		heightOffset = 0;
		indexWorldWarrior=0;
		xOffsetNube1=0;
		xOffsetNube2=0;
		xOffsetNube3=0;
		
		//initial = System.nanoTime();
		punyetazoTime = 0;
		punyetazo = false;
		
		alpha=1.0f;
		fade = false;
		
		handler.getGame().setFade(true);
	}
	
	// Actualizar variables
	@Override
	public void tick() {
		
		if(peleaDemo) {
			if(handler.getKeyManager().enter || handler.getGame().finPeleaDemo){
				peleaDemo=false;
				finPelea=false;
				evitarRebotes=true;
				handler.getGame().finPeleaDemo=false;
				handler.getGame().setCurrentSong(0);
				handler.getGame().tiempoDemo=false;
				handler.getGame().setPuntos1(0);
				handler.getGame().setPuntos2(0);
			}
			handler.getGame().gameState.tick();
			
		}
		else if(evitarRebotes) {
			rebotes--;
			if(rebotes==0) {
				rebotes=20;
				evitarRebotes=false;
			}
		}
		else {
			anim_matonRubio.tick();
			anim_matonMoreno.tick();
			anim_publico.tick();
			
			/*if(((System.nanoTime() - initial)/1000) > 4000000) {
				punyetazo = true;
				System.out.println(punyetazoTime);
			}*/
			if(punyetazoTime == 239) {
				punyetazo = true;	// Momento dar punyetazo
			}
			
			//PARA SALTAR EL COMIENZOOOOOO
			if(handler.getKeyManager().enter) {
				Assets.initAssets_MenuState();
				handler.getGame().setSoundEffect(25,false);
				handler.getGame().stopCurrentSong();
				State.setState(handler.getGame().menuState);
				//handler.getGame().controlesState=new ControlesState(handler);
				//State.setState(handler.getGame().controlesState);
			}
			//------------------------------------------------------
			
			if(siguientePantalla && handler.getKeyManager().enter){
				Assets.initAssets_MenuState();
				handler.getGame().setSoundEffect(25,false);
				handler.getGame().stopCurrentSong();
				State.setState(handler.getGame().menuState);
			}
			
			if(yOffset > 100) {
				xOffsetNube2++;
			}
			if(yOffset > 899) {
				xOffsetNube3++;
			}
			xOffsetNube1++;
			punyetazoTime++;
		}
		//System.out.println(punyetazoTime);

	}

	// Dibujar graficos
	@Override
	public void render(Graphics g) {
		if (yOffset > 1000 && alpha<0) { // El fade ha terminado
			todoNegro = true;
			i=0;
			alpha=1.0f;
			
		}
		
		if(!todoNegro) { // Parte inicial
			if(xMorenoCayendo<-650 && yOffset<1000) {
				yOffset+=5;
				if(yOffset == 1000) {
					fade=true;
					yOffset++;
				}
			}
			
			g.drawImage(fondoCielo, 0, -1000+yOffset, 1280, 1720, null);
			g.drawImage(nube1,(int) (10+xOffsetNube2*0.2), -600+yOffset, 300, 100, null);
			g.drawImage(nube1, (int) (1000+xOffsetNube3*0.15), -900+yOffset, 300, 100, null);
			g.drawImage(nube1, (int) (900+xOffsetNube2*0.2), -400+yOffset, 300, 100, null);
			g.drawImage(nube2, (int) (-50+xOffsetNube2*0.08), -700+yOffset, 300, 100, null);
			g.drawImage(nube3, (int) (20+xOffsetNube3*0.15), -1000+yOffset, 300, 100, null);
			g.drawImage(nube3, (int) (-20+xOffsetNube1*0.1), -100+yOffset, 325, 150, null);
			g.drawImage(edificios, 0, -1000+yOffset, 1280, 1720, null);
			g.drawImage(streetLogo, 345, -1000+yOffset+140, 587, 265, null);
			g.drawImage(arbol1, -20, 500+yOffset, 200, 300, null);
			g.drawImage(arbol2, 320, 400+yOffset, 200, 300, null);
			g.drawImage(arbol1, 380, 430+yOffset, 200, 300, null);
			g.drawImage(arbol3, 230, 450+yOffset, 200, 300, null);
			g.drawImage(arbol1, 100, 400+yOffset, 200, 300, null);
			g.drawImage(arbol3, 620, 450+yOffset, 200, 300, null);
			g.drawImage(arbol2, 500, 370+yOffset, 250, 400, null);
			g.drawImage(arbol1, 900, 470+yOffset, 200, 300, null);
			g.drawImage(arbol1, 1050, 500+yOffset, 200, 300, null);
			g.drawImage(arbol3, 1150, 450+yOffset, 200, 300, null);
			
			if(anim_publico.getIndex()==0) {
				g.drawImage(publico, -300, 480+yOffset, 400, 240, null);
				g.drawImage(publico, 100, 480+yOffset, 400, 240, null);
				g.drawImage(publico, 500, 480+yOffset, 400, 240, null);
				g.drawImage(publico, 900, 480+yOffset, 400, 240, null);
			}
			else {
				g.drawImage(publico2, -317, 480+yOffset, 395, 240, null);
				g.drawImage(publico2, 78, 480+yOffset, 395, 240, null);
				g.drawImage(publico2, 473, 480+yOffset, 395, 240, null);
				g.drawImage(publico2, 868, 480+yOffset, 410, 240, null);
			}
			
			if(!punyetazo) {
				if(anim_matonRubio.getIndex()==0) {
					g.drawImage(matonRubio, 680, 435, 400, 300, null);
				}
				else {
					g.drawImage(matonRubio, 680, 425, 400, 300, null);
				}
				
				if(anim_matonMoreno.getIndex()==0) {
					g.drawImage(matonMoreno, 200, 435, 400, 300, null);
				}
				else {
					g.drawImage(matonMoreno, 200, 425, 400, 300, null);
				}
			}
			else {
				if(i<3) {
					handler.getGame().setSoundEffect(18,false);
					g.drawImage(punyetazoRubio, 260, 380, 780, 370, null);
					g.drawImage(morenoSangre, 150, 250, 350, 500, null);
					i++;
				}
				else {
					g.drawImage(punyetazoRubio, 260, 380+yOffset, 780, 370, null);
					g.drawImage(morenoCayendo, xMorenoCayendo, yMorenoCayendo, 375, 500, null);
					xMorenoCayendo-=10;
					yMorenoCayendo+=5;
				}
			}
			
			if(fade && alpha>0.0f) {
				fadeEffect(g);
				g.setColor(Color.BLACK);
				//g.fillRect(0, 0, handler.getGame().getWidth(), handler.getGame().getHeight());
				g.drawImage(fondo, 0, 0, 1280,720, null);
				g.drawImage(streetLogo, 345, -1000+yOffset+140, 587, 265, null);
				
			}
			else {
				fade=false;
				alpha=1.0f;
			}
		}
		else {	// Parte final
			g.setColor(Color.BLACK);
			//g.fillRect(0, 0, handler.getGame().getWidth(), handler.getGame().getHeight());
			g.drawImage(fondo, 0, 0, 1280,720, null);
			if(i<20) {
				g.drawImage(streetLogo, 345, -1000+yOffset+140, 587, 265, null);
				i++;
			}
			else {
				if(i<100) {	// Rotacion logo
					g.drawImage(Utils.rotateImageByDegrees(streetLogo, degrees), 345-spawnX, -1000+yOffset+140-spawnY, 587+widthOffset, 265+heightOffset, null);
					degrees += 45;
					spawnX+=2;
					widthOffset+=4;
					spawnY+=1;
					heightOffset+=2;
					i++;
				}
				else {	// Terminada rotacion
					g.drawImage(streetLogo, 345-spawnX, -1000+yOffset+140-spawnY, 587+widthOffset, 265+heightOffset, null);
					if(parpadeaWorldWarrior) {
						if(i%4==0) {
							g.drawImage(worldWarriorLogo, spawnXWorldWarrior[indexWorldWarrior], -1000+yOffset+500, 750, 45, null);
							if(indexWorldWarrior<spawnXWorldWarrior.length-1) {
								indexWorldWarrior++;
								if(indexWorldWarrior==spawnXWorldWarrior.length-1)
									parpadeaWorldWarrior=false;
							}
						}
						i++;
					}
					else {
						if(peleaDemo) {
							handler.getGame().gameState.render(g);
							
							if(iPressEnter>10 && iPressEnter<=20) {
								g.drawImage(pressEnter, 515, -1000+yOffset+570, 270, 30, null);
								siguientePantalla=true;
							}
							else if(iPressEnter>20) {
								iPressEnter=0;
							}
							iPressEnter++;
						}
						else {
							espera--;
							if(espera==0) {
								espera=400;
								generarPeleaRandom();
								peleaDemo=true;
								handler.getGame().tiempoDemo=true;
							}
							g.drawImage(worldWarriorLogo, spawnXWorldWarrior[indexWorldWarrior], -1000+yOffset+500, 750, 45, null);
							if(iPressEnter>10 && iPressEnter<=20) {
								g.drawImage(pressEnter, 515, -1000+yOffset+570, 270, 30, null);
								siguientePantalla=true;
							}
							else if(iPressEnter>20) {
								iPressEnter=0;
							}
							iPressEnter++;
							
							g.drawImage(capcom, 495, 640, 310, 35, null);
						}
						
					}
				}
			}
		}
	}
	
	//Generar escenario en el que se pelea
	private void generarPeleaRandom() {
		int random = ((int)Math.floor(Math.random()*10))%6;
		switch(random) {
		case 0:
			//handler.getGame().setCurrentSong(4);
			handler.getGame().gameState=new GameState(handler,0,1,0,3);
			break;
		case 1:
			//handler.getGame().setCurrentSong(4);
			handler.getGame().gameState=new GameState(handler,0,2,0,3);
			break;
		case 2:
			//handler.getGame().setCurrentSong(5);
			handler.getGame().gameState=new GameState(handler,1,0,1,3);
			break;
		case 3:
			//handler.getGame().setCurrentSong(5);
			handler.getGame().gameState=new GameState(handler,1,2,1,3);
			break;
		case 4:
			//handler.getGame().setCurrentSong(6);
			handler.getGame().gameState=new GameState(handler,2,0,2,3);
			break;
		case 5:
			//handler.getGame().setCurrentSong(6);
			handler.getGame().gameState=new GameState(handler,2,1,2,3);
			break;
		}
	}
	
	private void fadeEffect(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    alpha-=0.01f;
	}
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
	
	public void setFinPelea(boolean finPelea) {
		this.finPelea=finPelea;
	}
}
