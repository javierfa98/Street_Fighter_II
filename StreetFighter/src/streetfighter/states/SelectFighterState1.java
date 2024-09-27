package streetfighter.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import streetfighter.Game;
import streetfighter.Handler;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.utils.Utils;

public class SelectFighterState1 extends State{

	int fighter1=0;
	int fighter2=2;
	boolean primeraVez;	//Evita el problema con el tick() mas rapido que el KeyReleased
	int casillasPos[]= {490,591,690};
	int pos1=0;
	int pos2=2;
	boolean finSelectPlayer1=false;	//Player 1 ha seleccionado personaje
	boolean finSelectPlayer2=false; //Player 2 (CPU) ha seleccionado personaje
	int casilla=0;
	boolean mostrarCasilla=false;
	private boolean viajeAvion=false;
	private int tiempoViajeAvion=0;
	private double xOrigen=0,yOrigen=0,xDestino=0,yDestino=0;
	private boolean sonidoPais=false;
	int xVeces;		//Numero aleatorio que indica el numero de veces que la CPU cambia de fighter
	int cuenta=0;	//Levar la cuenta para controlar cuando la CPU llega al fighter a seleccionar
	int ahora=0;	//Controla la velocidad de cambio de posicion de la casilla
	boolean showFighters=false;
	int tiempoVS=170;	//Tiempo que se muestra la pantalla VS
	Random rand = new Random(); 
	int iPressEnter=0;
	private BufferedImage back, selectP1;
	private BufferedImage equis;
	
	public Font pixelFont;	//Fuente arcade
	
	private BufferedImage pressEnter;
	
	private int parpadeo=10;
	private boolean mostrarFlag=true;

	public SelectFighterState1(Handler handler, int fighter1) {
		super(handler);
		primeraVez=false;
		pressEnter=ImageLoader.loadImage("/res/textures/enterGameMenu/pressEnter.png");
		back=ImageLoader.loadImage("/res/textures/rankingMenu/back.png");
		selectP1=ImageLoader.loadImage("/res/textures/rankingMenu/selectP1.png");
		equis=ImageLoader.loadImage("/res/textures/equis.png");
		
		//Si se le pasa un luchador, se va a esta pantalla con el fighter1 ya seleccionado
		//para las siguientes peleas del modo historia
		if (fighter1<3) {
			this.fighter1=fighter1;
			finSelectPlayer1=true;
			this.pos1=fighter1;
		}

        xVeces = rand.nextInt(15)+5; 	//Rango del numero aleatorio
        
        try {
        	InputStream is = Game.class.getResourceAsStream("/res/fonts/pixel.ttf");
			pixelFont=Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f);
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(pixelFont);
		}
		catch(IOException | FontFormatException e) {
			System.out.println("ERROR AL CARGAR LA FUENTE");
		}
        
	}
	
	@Override
	public void tick() {
		
		if(!showFighters) {
			
			if(handler.getGame().getKeyManager().primeraVez && primeraVez) {
				primeraVez=false;
				
				if(handler.getKeyManager().ESC && !handler.getGame().quererSalir && !viajeAvion){ // Salir juego
					handler.getGame().setSoundEffect(25,false);
					handler.getGame().quererSalir=true;
					handler.getGame().posAvion=1;
					handler.getGame().xEsc=590;
					handler.getGame().yEsc=335;
					handler.getGame().widthEsc=100;
					handler.getGame().heightEsc=50;
				}
				
				if(handler.getGame().quererSalir) {
					if(handler.getKeyManager().enter && handler.getGame().posAvion==2){ // yes
						System.exit(0);
					}
					if(handler.getKeyManager().enter && handler.getGame().posAvion==1){ // no
						handler.getGame().setSoundEffect(25,false);
						handler.getGame().quererSalir=false;
					}
					if(handler.getKeyManager().D){ // dcha
						handler.getGame().setSoundEffect(26,false);
						if(handler.getGame().posAvion==2) {
							handler.getGame().posAvion=1;
						}
						else {
							handler.getGame().posAvion=2;
						}
					}
					if(handler.getKeyManager().A){ // izq
						handler.getGame().setSoundEffect(26,false);
						if(handler.getGame().posAvion==1) {
							handler.getGame().posAvion=2;
						}
						else {
							handler.getGame().posAvion=1;
						}
					}
				}
				else {
					//Si los 2 jugadores han elegido luchador y se pulsa enter, termina seleccion de luchador
					if(handler.getKeyManager().enter && finSelectPlayer1 && finSelectPlayer2){
							viajeAvion=true;
					}
					//Si se pulsa enter pero los 2 jugadores no han elegido, sonido de error
					else if(handler.getKeyManager().enter){
						handler.getGame().setSoundEffect(24,false);
					}
					
					if(handler.getKeyManager().B) { // Volver a menu state
						handler.getGame().setSoundEffect(25,false);
						handler.getGame().setNumPelea(0);
						handler.getGame().setPuntos1(0);
						handler.getGame().setPuntos2(0);
						State.setState(handler.getGame().menuState);
					}
					
					//Para que no suene el sonido de moverte si ya no puedes moverte
					if(!finSelectPlayer1) {
						//Player 1 bloquea personaje
						if(handler.getKeyManager().selectPlayer1){
							finSelectPlayer1=true;
							handler.getGame().setSoundEffect(5,false);
						}
						
						
						//Jugador 1 pasa hacia la derecha de personaje
						if(handler.getKeyManager().D && !finSelectPlayer1) {
							handler.getGame().setSoundEffect(3,false);
							if(fighter1==2) {
								fighter1=0;
								pos1=0;
							}
							else {
								fighter1++;
								pos1++;
							}
						}
						//Jugador 1 pasa hacia la izquierda de personaje
						if(handler.getKeyManager().A && !finSelectPlayer1) {
							handler.getGame().setSoundEffect(3,false);
							if(fighter1==0) {
								fighter1=2;
								pos1=2;
							}
							else {
								fighter1--;
								pos1--;
							}
						}
						
					}
				}
			}
			else if(!handler.getGame().getKeyManager().primeraVez) {
				primeraVez=true;
			}
			
			if(viajeAvion) {
				parpadeo--;
				if(parpadeo==0) {
					mostrarFlag=!mostrarFlag;
					parpadeo=10;
				}
				handler.getGame().quererSalir=false;
				if(tiempoViajeAvion==0) {
					
					if((fighter1==0 && handler.getGame().getNumPelea()==0)
							||(fighter1==2 && handler.getGame().getNumPelea()==1)) { // origen Brazil
						xOrigen=860;
						yOrigen=260;
						if((fighter1==0 && handler.getGame().getNumPelea()==0)
								|| (fighter1==2 && handler.getGame().getNumPelea()==1)) { // destino China
							xDestino=570;
							yDestino=50;
						}
						else { 			  // destino Japon
							xDestino=630;
							yDestino=230;
						}
						
					}
					else if((fighter1==1 && handler.getGame().getNumPelea()==0)
							||(fighter1==0 && handler.getGame().getNumPelea()==1)) { // origen China
						xOrigen=530;
						yOrigen=38;
						if((fighter1==1 && handler.getGame().getNumPelea()==0)
								|| (fighter1==0 && handler.getGame().getNumPelea()==1)) { // destino Japon
							xDestino=600;
							yDestino=230;
						}
						else { // destino Brasil
							xDestino=860;
							yDestino=260;
						}
					}
					else if((fighter1==2 && handler.getGame().getNumPelea()==0)
							||(fighter1==1 && handler.getGame().getNumPelea()==1)) { // origen Japon
						xOrigen=600;
						yOrigen=230;
						if((fighter1==2 && handler.getGame().getNumPelea()==0)
								|| (fighter1==1 && handler.getGame().getNumPelea()==1)) { // destino Brazil
							xDestino=860;
							yDestino=285;
						}
						else { // destino China
							xDestino=570;
							yDestino=70;
						}
					}
					handler.getGame().setSoundEffect(1);
				}
				if(tiempoViajeAvion>50 && handler.getGame().currentSoundFinished() && !sonidoPais) { // Si ha terminado el sonido del viajecito en avion
					sonidoPais=true;
					// Se mete sonido del pais
					if(fighter2==0) {
						handler.getGame().setSoundEffect(2);
					}
					else if(fighter2==1){
						handler.getGame().setSoundEffect(4);
					}
					else {
						handler.getGame().setSoundEffect(12);
					}
				}
				
				if(tiempoViajeAvion>230) {
					showFighters=true;
					handler.getGame().setFade(true);
					handler.getGame().setCurrentSong(2);
				}
				tiempoViajeAvion++;
			}
			
			//Seleccion aleatoria de fighter por parte de la CPU
			if(finSelectPlayer1 && !finSelectPlayer2) {
				/*//Jugador 2 pasa hacia la derecha de personaje
				ahora++;
				if(cuenta<xVeces && ahora==10) {
					cuenta++;
					ahora=0;
					handler.getGame().setSoundEffect(0,false);
					if(fighter2==2) {
						fighter2=0;
						pos2=0;
					}
					else {
						fighter2++;
						pos2++;
					}
				}
				//Bloquear fighter
				else if(cuenta>=xVeces){
					finSelectPlayer2=true;
					playSound(fighter2);
				}*/
				//Si es la pimera pelea del modo historia...
				if(handler.getGame().getNumPelea()==0) {
					if(fighter1==0) {
						fighter2=1;
						pos2=1;
					}
					else if(fighter1==1) {
						fighter2=2;
						pos2=2;
					}
					else if(fighter1==2) {
						fighter2=0;
						pos2=0;
					}
				}
				//Si es la segunda pelea del modo historia...
				else if(handler.getGame().getNumPelea()==1) {
					if(fighter1==0) {
						fighter2=2;
						pos2=2;
					}
					else if(fighter1==1) {
						fighter2=0;
						pos2=0;
					}
					else if(fighter1==2) {
						fighter2=1;
						pos2=1;
					}
				}
				finSelectPlayer2=true;
			}
		}
		
		else if(showFighters) {
			if(tiempoVS<=0) {
				handler.getGame().gameState=new GameState(handler,0,fighter1,fighter2,1);
				State.setState(handler.getGame().gameState);
				
				//PARA QUE SI SE VUELVE A ESTE MENU TRAS UNA PELEA, ESTE COMO SI SE ENTRERA
				//LA PRIMERA VEZ
				showFighters=false;
				finSelectPlayer1=false;
				finSelectPlayer2=false;
				mostrarCasilla=false;
				fighter1=0;
				fighter2=2;
				primeraVez=false;
				pos1=0;
				pos2=2;
				casilla=0;
				ahora=0;
				xVeces = rand.nextInt(15)+5; 
				tiempoVS=170;
				viajeAvion=false;
				tiempoViajeAvion=0;
				iPressEnter=0;
			}
			else {
				tiempoVS--;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(!showFighters) {
			g.drawImage(Assets.fondoSelectFighterState, 0, 0, 1280, 720, null);
			g.drawImage(Assets.getFace(fighter1,1), -10, 377, 400, 400, null);
			//g.drawImage(Assets.getFace(fighter2,2), 888, 377, 400, 400, null);
			g.drawImage(Assets.getFlagGris(2), 627, 127, 115, 60, null);
			dibujarFlag(g,fighter1);
			
			//Mostrar equis cuando se ha vencido a un enemigo en el modo historia
			if(handler.getGame().mode==1 && handler.getGame().getNumPelea()==1) {
				if(fighter1==0) {
					g.drawImage(equis, 520, 35, 120, 110, null);
					
				}
				else if(fighter1==1) {
					g.drawImage(equis, 608, 208, 120, 110, null);
				}
				else if(fighter1==2) {
					g.drawImage(equis, 847, 252, 120, 110, null);
				}
			}
			
			
			casilla++;
			//Para que parpadeen las casillas de seleccion a una buena velocidad
			if(casilla==4) {
				mostrarCasilla=!mostrarCasilla;
				casilla=0;
			}
			
			if(!finSelectPlayer1) {
				//Casilla1 parpadeando
				if(mostrarCasilla && !finSelectPlayer1) {
					g.drawImage(Assets.getCasilla(1), casillasPos[pos1], 500, 100, 100, null);
				}
			}
			//Cuando el player 1 ha seleccionado fighter, la CPU procede a elegirlo 
			else {
				g.drawImage(Assets.getCasilla(1), casillasPos[pos1], 500, 100, 100, null);
				
				//Dibujar casilla sobre las banderas
				if(fighter1==0) {
					g.drawImage(Assets.getCasilla(1), 840, 245, 140, 145, null);
				}
				else if(fighter1==1) {
					g.drawImage(Assets.getCasilla(1), 509, 25, 140, 145, null);
				}
				else if(fighter1==2) {
					g.drawImage(Assets.getCasilla(1), 595, 200, 140, 145, null);
				}
				
				//Casilla2 parpadeando
				if(mostrarCasilla && !finSelectPlayer2) {
					g.drawImage(Assets.getCasilla(0), casillasPos[pos2], 505, 100, 100, null);
				}
				//Casilla2 sin parpadeo porque se ha seleccionado luchador
				else if(finSelectPlayer2) {
					//g.drawImage(Assets.getCasilla(0), casillasPos[pos2], 505, 100, 100, null);
					
					if(iPressEnter>10 && iPressEnter<=20) {
						g.drawImage(pressEnter, 515, 470, 250, 25, null);
					}
					else if(iPressEnter>20) {
						iPressEnter=0;
					}
					iPressEnter++;
					
					
					
					//Se muestra viaje avion
					if(viajeAvion) {
						if((fighter1==0 && handler.getGame().getNumPelea()==0)
								||(fighter1==2 && handler.getGame().getNumPelea()==1)) { // origen Brazil
							
							if((fighter1==0 && handler.getGame().getNumPelea()==0)
									|| (fighter1==2 && handler.getGame().getNumPelea()==1)) { // destino China
								
								g.drawImage(Utils.rotateImageByDegrees(Assets.avion, -180), (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen>xDestino)
									xOrigen-=2.5;
								if(yOrigen>yDestino) {
									yOrigen-=1.8;
								}
							}
							else { // destino Japon
								g.drawImage(Utils.rotateImageByDegrees(Assets.avion, -180), (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen>xDestino)
									xOrigen-=1.77;
								if(yOrigen>yDestino) {
									yOrigen-=0.25;
								}
							}
							
						}
						else if((fighter1==1 && handler.getGame().getNumPelea()==0)
								||(fighter1==0 && handler.getGame().getNumPelea()==1)) { // origen China
							
							
							if((fighter1==1 && handler.getGame().getNumPelea()==0)
									|| (fighter1==0 && handler.getGame().getNumPelea()==1)) { // destino Japon
								
								g.drawImage(Assets.avion, (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen<xDestino)
									xOrigen+=0.8;
								if(yOrigen<yDestino) {
									yOrigen+=2.3;
								}
							}
							
							else { // destino Brazil
								
								g.drawImage(Assets.avion, (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen<xDestino)
									xOrigen+=3;
								if(yOrigen<yDestino) {
									yOrigen+=2.08;
								}
							}
						}
						else if((fighter1==2 && handler.getGame().getNumPelea()==0)
								||(fighter1==1 && handler.getGame().getNumPelea()==1)) { // origen Japon
							
							if((fighter1==2 && handler.getGame().getNumPelea()==0)
									|| (fighter1==1 && handler.getGame().getNumPelea()==1)) { // destino Brazil
						
								g.drawImage(Assets.avion, (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen<xDestino)
									xOrigen+=1.97;
								if(yOrigen<yDestino) {
									yOrigen+=0.4;
								}
							}
							else { // destino China
								g.drawImage(Utils.rotateImageByDegrees(Assets.avion, -90), (int)xOrigen, (int)yOrigen, 40, 40, null);
								if(xOrigen>xDestino)
									xOrigen-=0.30;
								if(yOrigen>yDestino) {
									yOrigen-=1.6;
								}
							}
						}
					}
				}
			}
			
			// Mensajes de ayuda
			g.setFont(pixelFont);
			g.setColor(Color.WHITE);
			//g.drawString("B-BACK", 605, 710);
			g.drawImage(back, 575, 690, 140, 30, null);
			//g.drawString("X-SELECT P1", 580, 690);
			g.drawImage(selectP1, 530, 660, 230, 30, null);
			
			
			
			if(handler.getGame().quererSalir) {
				handler.getGame().renderMenuEsc(g, Assets.avion);
			}
		}
		//Mostrar VS
		else {
			handler.getGame().setCurrentSong(2);
			g.drawImage(Assets.fondoVS,0,0,1280,720,null);
			g.drawImage(Assets.vs, 390, 460, 500, 250, null);
			g.drawImage(Assets.getFaceVS(fighter1, 1),50,100,400,400,null);
			g.drawImage(Assets.getFaceVS(fighter2, 2),830,100,400,400,null);
		}

	}
	
	public void dibujarFlag(Graphics g, int fighter) {
		if(finSelectPlayer2) {
			if(fighter==0) {  //Brasil
				g.drawImage(Assets.getFlag(0), 850, 275, 116, 57, null);
				g.drawImage(Assets.getFlagName(0), 860, 337, 99, 33, null);
				if(fighter2==1) {
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(1), 529, 60, 103, 55, null);
						g.drawImage(Assets.getFlagName(1), 534, 120, 93, 30, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
					}
					
					g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
				}
				else if(fighter2==2) {
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(2), 610, 231, 117, 60, null);
						g.drawImage(Assets.getFlagName(2), 619, 295, 98, 29, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
					}
					g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
				}
				else {
					g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
					g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
				}
			}
			else if(fighter==1) {  //China
				g.drawImage(Assets.getFlag(1), 529, 60, 103, 55, null);
				g.drawImage(Assets.getFlagName(1), 534, 120, 93, 30, null);
				if(fighter2==0) {
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(0), 850, 275, 116, 57, null);
						g.drawImage(Assets.getFlagName(0), 860, 337, 99, 33, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
					}
					g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
				}
				else if(fighter2==2){
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(2), 610, 231, 117, 60, null);
						g.drawImage(Assets.getFlagName(2), 619, 295, 98, 29, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
					}
					g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
				}
				else {
					g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
					g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
				}
			}
			else if(fighter==2) {	//Japon
				g.drawImage(Assets.getFlag(2), 610, 231, 117, 60, null);
				g.drawImage(Assets.getFlagName(2), 619, 295, 98, 29, null);
				if(fighter2==0) {
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(0), 850, 275, 116, 57, null);
						g.drawImage(Assets.getFlagName(0), 860, 337, 99, 33, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
					}
					g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
				}
				else if(fighter2==1) {
					if(viajeAvion && mostrarFlag) {
						g.drawImage(Assets.getFlag(1), 529, 60, 103, 55, null);
						g.drawImage(Assets.getFlagName(1), 534, 120, 93, 30, null);
					}
					else {
						g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
					}
					g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
				}
				else {
					g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
					g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
				}
			}
		}
		else {
			if(fighter==0) {  //Brasil
				g.drawImage(Assets.getFlag(0), 850, 275, 116, 57, null);
				g.drawImage(Assets.getFlagName(0), 860, 337, 99, 33, null);
				g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
				g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
			}
			else if(fighter==1) {  //China
				g.drawImage(Assets.getFlag(1), 529, 60, 103, 55, null);
				g.drawImage(Assets.getFlagName(1), 534, 120, 93, 30, null);
				g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
				g.drawImage(Assets.getFlagGris(2), 610, 231, 117, 60, null);
			}
			else if(fighter==2) {	//Japon
				g.drawImage(Assets.getFlag(2), 610, 231, 117, 60, null);
				g.drawImage(Assets.getFlagName(2), 619, 295, 98, 29, null);
				g.drawImage(Assets.getFlagGris(0), 850, 275, 116, 57, null);
				g.drawImage(Assets.getFlagGris(1), 529, 60, 103, 55, null);
			}
		}
	}
	
	//Suena la voz del fighter que corresponda
	public void playSound(int fighter) {
		if(fighter==0)  //Bixo
			handler.getGame().setSoundEffect(2,false);
		if(fighter==1)  //Chun
			handler.getGame().setSoundEffect(3,false);
		if(fighter==2)	//Ryu
			handler.getGame().setSoundEffect(4,false);
	}
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
	
}
