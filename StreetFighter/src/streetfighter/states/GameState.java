package streetfighter.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import streetfighter.Game;
import streetfighter.Handler;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;
import streetfighter.worlds.World;
import streetfighter.worlds.WorldBrazil;
import streetfighter.worlds.WorldChina;
import streetfighter.worlds.WorldJapan;

public class GameState extends State{
	
	private World world;
	private boolean fighting=true;  //fighting=true -> luchando  / fighting=false -> caras reventadas
	private int fighter1, fighter2, mode, stage;
	private Handler handler;
	private int resultado;  				//1=1win 2=2win
	private int marcador1=0; 				//Contar las rondas ganadas por el jugador 1
	private int marcador2=0; 				//Contar las rondas ganadas por el jugador 2
	private boolean finRonda=false; 		//Comprobar que se ha llegado al final de una ronda
	private boolean mostradaRonda1=false;
	int contadorRonda=1;					//Numero de ronda en la que estamos
	boolean msjRonda=true;				    //Mostrar el msj Ronda
	int tiempoRondaMsj=100;					//Tiempo mostrando el msj Ronda
	boolean msjFight=false;					//Mostrar el msj Fight
	int tiempoFightMsj=100;					//Tiempo mostrando el msj Fight
	boolean msjGanador=false;				//Mostrar quien ha ganado la ronda
	int tiempoGanador=100;
	boolean msjPuntos=false;				//Mostrar toda la info de los puntos
	int tiempoTime=50;
	int tiempoVital=100;					//El doble de tiem porque Vital se decrementa desde que se empieza a mostrar Time
	int puntosRonda=0;
	int puntosTime=0;
	int puntosVital=0;
	int bonus=0;							//Sumar puntos de 100 en 100 hasta llegar al bonus
	private BufferedImage nombres[];
	int tiempoVS=250;
	int fighterToSelectFighter=0;			//El luchador que se va a pasar a selectFighter (evita perdida del fighter al asignarle 3)
	boolean mostrarScoreLose=false;
	boolean mostrarScoreWin=false;
	public Font pixelFontBig;
	boolean ko = true;
	private boolean primeraVez;
	private boolean primeraVez2;
	private boolean pause;
	private BufferedImage roundONE, roundTWO, roundTRE, fightRonda, youWin, winAlone, youLose,
						  timePoints, vitalPoints, bonusPoints;
	
	protected LettersNumbers letNums;
	
	public GameState(Handler handler,int stage,int fighter1, int fighter2, int mode) {
		//Llama al constructor de la clase que extiende (State)
		super(handler);
		Assets.initAssets_GameState();
		primeraVez=false;
		primeraVez2=false;
		pause=false;
		roundONE = ImageLoader.loadImage("/res/textures/rankingMenu/roundONE.png");
		roundTWO = ImageLoader.loadImage("/res/textures/rankingMenu/roundTWO.png");
		roundTRE = ImageLoader.loadImage("/res/textures/rankingMenu/roundTRE.png");
		fightRonda = ImageLoader.loadImage("/res/textures/rankingMenu/fightLoko.png");
		youWin = ImageLoader.loadImage("/res/textures/rankingMenu/youWin.png");
		youLose = ImageLoader.loadImage("/res/textures/rankingMenu/youLose.png");
		winAlone = ImageLoader.loadImage("/res/textures/rankingMenu/winAlone.png");
		nombres = new BufferedImage[3];
		nombres[0] = ImageLoader.loadImage("/res/textures/rankingMenu/blankaTrans.png");
		nombres[1] = ImageLoader.loadImage("/res/textures/rankingMenu/chunTrans.png");
		nombres[2] = ImageLoader.loadImage("/res/textures/rankingMenu/ryuTrans.png");
		timePoints = ImageLoader.loadImage("/res/textures/rankingMenu/timePoints.png");
		vitalPoints = ImageLoader.loadImage("/res/textures/rankingMenu/vitalPoints.png");
		bonusPoints = ImageLoader.loadImage("/res/textures/rankingMenu/bonusPoints.png");
		letNums = new LettersNumbers();
		
		//0->Bixo, 1->Chun, 2->Ryu
		if(fighter2==0) {
			//Stage Blanka
			//handler.getGame().setCurrentSong(4);
			world=new WorldBrazil(handler,fighter1,fighter2,mode);
		}
		else if(fighter2==1) {
			//Stage Chun
			//handler.getGame().setCurrentSong(5);
			world=new WorldChina(handler,fighter1,fighter2,mode);
		}
		else {
			//Stage Ryu
			//handler.getGame().setCurrentSong(6);
			world=new WorldJapan(handler,fighter1,fighter2,mode);
		}
		
		this.handler=handler;
		this.stage=stage;
		this.fighter1=fighter1;
		this.fighter2=fighter2;
		this.mode=mode;
		
		//Para que la clase Game sepa el modo y haga brain.tick() si es oportuno
		handler.getGame().mode=mode;
		//Porque cuando acaba la pelea se sigue en este estado mostrando las caras reventadas
		//Si no se inicia podria estar ya empezado 
		handler.getGame().tiempoRestante=99;
		//Para que cada vez que empiece una pelea, comience en el principio
		handler.getGame().setFighting(true);
		
		try {
			InputStream is = Game.class.getResourceAsStream("/res/fonts/pixel.ttf");
			pixelFontBig=Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(35f);
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(pixelFontBig);
		}
		catch(IOException | FontFormatException e) {
			System.out.println("ERROR AL CARGAR LA FUENTE");
		}
		
		
	}
	
	@Override
	public void tick() {
		
		//Si se esta en la pelea al mejor de 3
		if(fighting) {
			//Mostrar numero de ronda
			if(msjRonda) {
				tiempoRondaMsj--;
				if(tiempoRondaMsj==0) {
					msjRonda=false;
					tiempoRondaMsj=100;
					msjFight=true;
					handler.getGame().setSoundEffect(6,true);
				}
				handler.getGame().setTiempoQuieto(true);
			}
			//Mostrar mensaje FIGHT!
			else if(msjFight) {
				tiempoFightMsj--;
				if(tiempoFightMsj==0) {
					msjFight=false;
					tiempoFightMsj=100;
				}
				handler.getGame().setTiempoQuieto(true);
			}
			//Mostrar el ganador de la ronda
			else if(msjGanador) {
				handler.getGame().setTiempoQuieto(true);
				tiempoGanador--;
				if(tiempoGanador==0) {
					msjGanador=false;
					tiempoGanador=100;
					//Si gana la CPU no mostramos los puntos que ha hecho (nos saltamos el estado msjGanador)
					if((mode==1 && resultado==2) || mode==3) {
						tiempoTime=50;
						tiempoVital=100;
						msjPuntos=false;
						bonus=0;
						handler.getGame().setFade(true);
						
						//Actualizar variables para gestionar el final de ronda
						accionFinRonda();
					}
					else {
						msjPuntos=true;
						
					}
					
				}
			}
			//Mostrar puntos ganados
			else if(msjPuntos) {
				tiempoTime--;
				tiempoVital--;
				//bonus se usa para controlar el tiempo a mostrar despues de llegar hasta el maximo valor del bonus
				if(bonus>=(puntosTime+puntosVital+10000)) {
					tiempoTime=50;
					tiempoVital=100;
					msjPuntos=false;
					handler.getGame().setFade(true);	
					bonus=0;
					
					//Actualizar variables para gestionar el final de ronda
					accionFinRonda();
					
				}
			}
			//Comenzamos a hacer tick de la pelea cuando se han mostrado los mensajes iniciales
			else {
				if(handler.getGame().getKeyManager().primeraVez  && primeraVez2) {
					primeraVez2=false;
					
					if(handler.getKeyManager().ESC){ // Salir juego
						pause=!pause;
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
						if(handler.getKeyManager().P && mode!=3){ // Pause !!!!
							//System.out.println("Hola");
							pause=!pause;
						}
					}
				}
				else if(!handler.getGame().getKeyManager().primeraVez) {
					primeraVez2=true;
				}
				
				if(pause) {
					//que se detenga el tiempo de la pelea
					handler.getGame().setTiempoQuieto(true);
					handler.getGame().pauseState=new PauseState(handler);
					pause=false;
					State.setState(handler.getGame().pauseState);
				}
				else {
					//que avance el tiempo de la pelea
					handler.getGame().setTiempoQuieto(false);
					
					world.tick();
				}
				
				//Si se ha acabado el tiempo
				if(handler.getGame().tiempoRestante==0) {
					//Y el player 2 tiene mas vida, gana player 2
					if(handler.getWorld().getEntityManager().getPlayer_1().getVida()<
					   handler.getWorld().getEntityManager().getPlayer_2().getVida()) {
						marcador2++;
						resultado=2;
					}
					//Y el player 1 tiene mas vida, gana el player 1
					else {
						resultado=1;
						marcador1++;
					}
					finRonda=true;
				}
				//Player 2 ha ganado
				else if(handler.getWorld().getEntityManager().getPlayer_1().getVida()<=0) {
					//Comienza la animacion de KO
					if (handler.getWorld().getEntityManager().getPlayer_1().getAnimacionActual() != 13) {
						//Sonido de grito
						if (handler.getWorld().getEntityManager().getPlayer_1().getFighter() == 1) {
							//Chun
							handler.getGame().setSoundEffect(30);
						}
						else {
							//Ryu y Blanka
							handler.getGame().setSoundEffect(29);
						}
						if (ko) {
							handler.getWorld().getEntityManager().getPlayer_1().KO = true;
							handler.getWorld().getEntityManager().getPlayer_2().KO = true;
							resultado=2;
							marcador2++;
							ko = false;
						}
						handler.getWorld().getEntityManager().getPlayer_1().setAnimacionActual(13);
						handler.getWorld().getEntityManager().getPlayer_1().getAnimaciones().get(13).setAnimacionEnCurso(true);
						handler.getWorld().getEntityManager().getPlayer_1().getAnimaciones().get(13).resetAnimtaion();
					}
					//Finaliza la animacion de KO
					else if(handler.getWorld().getEntityManager().getPlayer_1().getAnimacionActual() == 13
							&& handler.getWorld().getEntityManager().getPlayer_1().getAnimaciones().get(13).getIndex() == 4) {
						finRonda=true;
						ko = true;
						handler.getWorld().getEntityManager().getPlayer_1().KO = false;
						handler.getWorld().getEntityManager().getPlayer_2().KO = false;
					}
				}
				//Player 1 ha ganado
				else if(handler.getWorld().getEntityManager().getPlayer_2().getVida()<=0) {
					//Comienza la animacion de KO
					if (handler.getWorld().getEntityManager().getPlayer_2().getAnimacionActual() != 13) {
						//Sonido de grito
						if (handler.getWorld().getEntityManager().getPlayer_2().getFighter() == 1) {
							//Chun
							handler.getGame().setSoundEffect(30);
						}
						else {
							//Ryu y Blanka
							handler.getGame().setSoundEffect(29);
						}
						if (ko) {
							handler.getWorld().getEntityManager().getPlayer_1().KO = true;
							handler.getWorld().getEntityManager().getPlayer_2().KO = true;
							resultado=1;
							marcador1++;
							ko = false;
						}
						handler.getWorld().getEntityManager().getPlayer_2().setAnimacionActual(13);
						handler.getWorld().getEntityManager().getPlayer_2().getAnimaciones().get(13).setAnimacionEnCurso(true);
						handler.getWorld().getEntityManager().getPlayer_2().getAnimaciones().get(13).resetAnimtaion();
					}
					//Finaliza la animacion de KO
					else if(handler.getWorld().getEntityManager().getPlayer_2().getAnimacionActual() == 13
							&& handler.getWorld().getEntityManager().getPlayer_2().getAnimaciones().get(13).getIndex() == 4) {
						finRonda=true;
						ko = true;
						handler.getWorld().getEntityManager().getPlayer_1().KO = false;
						handler.getWorld().getEntityManager().getPlayer_2().KO = false;
					}
				}
				
				//SUMAR LOS PUNTOS AL JUGADOR QUE CORRESPONDA SI HA TERMINADO LA RONDA
				if(finRonda) {
					//Si ha ganado la ronda el jugador 1, se le suman los puntos por tiempo y vida
					if(resultado==1) {
						//TIEMPO
						puntosTime=handler.getGame().tiempoRestante*100;
						//VIDA
						puntosVital=(int)handler.getWorld().getEntityManager().getPlayer_1().getVida()*10;
						//Bonus de dificutad:
						if (handler.getGame().mode == 1) {
							if (handler.getGame().getDificultad() == 1 || handler.getGame().getDificultad() == 3) {
								//Medio o incremental
								puntosTime *= 1.2;
								puntosVital*=1.2;
							}
							else if (handler.getGame().getDificultad() == 2) {
								//Medio o incremental
								puntosTime *= 1.4;
								puntosVital*=1.4;
							}
						}
					}
					else if(resultado==2) {
						//TIEMPO
						puntosTime=handler.getGame().tiempoRestante*100;
						//VIDA
						puntosVital=(int)handler.getWorld().getEntityManager().getPlayer_2().getVida()*10;
					}
					msjGanador=true;
					if(mode==1) {
						if(resultado==1) {
							handler.getGame().setSoundEffect(19);
						}
						else {
							handler.getGame().setSoundEffect(20);
						}
						
					}
				}
			}
			
		}
		//Si ha terminado la pelea y se muestran las caras reventadas
		else {
			tiempoVS--;
			if(tiempoVS<=0) {
				tiempoVS=250;
				if(mode==2) {
					handler.getGame().setPuntos1(0);
					handler.getGame().setPuntos2(0);
					handler.getGame().setCurrentSong(3);
					State.setState(handler.getGame().selectFighterState2);
				}
				else if(mode==1) {
					//Si ha ganado la CPU se guarda el score
					if(mostrarScoreLose) {
						State ScoreState = new ScoreState(handler,fighter1,false);
						handler.getGame().setCurrentSong(0);
						State.setState(ScoreState);
					}
					//Si se termina el modo historia se guarda el score
					else if(mostrarScoreWin) {
						State EndGameState = new EndGameState(handler,fighter1,false);
						if(fighter1==0) {
							handler.getGame().setCurrentSong(7);
						}
						else if(fighter1==1) {
							handler.getGame().setCurrentSong(8);
						}
						else {
							handler.getGame().setCurrentSong(9);
						}
						
						Assets.initAssets_EndGameState();
						State.setState(EndGameState);
					}
					else {
						State selectFighterState1=new SelectFighterState1(handler,fighterToSelectFighter);
						handler.getGame().setCurrentSong(3);
						State.setState(selectFighterState1);
					}
					
				}
				else if(mode==3) {
					handler.getGame().finPeleaDemo=true;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		//Mostrar pelea
		if(fighting) {
			
			//Siempre se hace el render para que cuando se pongan mensajes se muestre en el fondo la pelea parada
			world.render(g);
			
			
			if(marcador1==1) {
				g.drawImage(Assets.mano, 0, 60, 40, 50, null);
			}
			else if(marcador1==2) {
				g.drawImage(Assets.mano, 0, 60, 40, 50, null);
				g.drawImage(Assets.mano, 40, 60, 40, 50, null);
			}
			if(marcador2==1) {
				g.drawImage(Assets.mano, 1240, 60, 40, 50, null);
			}
			else if(marcador2==2) {
				g.drawImage(Assets.mano, 1240, 60, 40, 50, null);
				g.drawImage(Assets.mano, 1200, 60, 40, 50, null);
			}
			//Mostrar numero de ronda
			if(msjRonda) {
				g.setFont(handler.getGame().pixelFont); 
				g.setColor(Color.WHITE);
				//g.drawString("ROUND "+Integer.toString(contadorRonda), 570, 400);
				if(contadorRonda==1) {
					g.drawImage(roundONE, 548, 375, 215, 40, null);
				}
				else if(contadorRonda==2) {
					g.drawImage(roundTWO, 548, 375, 215, 40, null);
				}
				else if(contadorRonda==3) {
					g.drawImage(roundTRE, 548, 375, 215, 40, null);
				}
				
				if(contadorRonda==1 && !mostradaRonda1) {
					handler.getGame().setSoundEffect(21,true);
					mostradaRonda1=true;
				}
			}
			//Mostrar mensaje FIGHT!
			else if(msjFight) {
				g.setFont(handler.getGame().pixelFont); 
				g.setColor(Color.WHITE);
				//g.drawString("FIGHT!", 590, 400);
				g.drawImage(fightRonda, 558, 375, 195, 40, null);
			}
			//Mostrar nombre ganador
			else if(msjGanador) {
				g.setFont(pixelFontBig); 
				if(tiempoGanador>75 || (50>tiempoGanador && tiempoGanador>25)) {
					g.setColor(Color.GRAY);
				}
				else {
					g.setColor(Color.WHITE);
				}
				if(resultado==1) {
					//Si estas en un jugador y ganas...
					if(mode==1) {
						//g.drawString("YOU WIN", 560, 220);
						g.drawImage(youWin, 530, 200, 215, 40, null);
					}
					//Si estas en 2 jugadores y gana el player 1
					else {
						if(fighter1==2) {
							g.drawImage(nombres[fighter1], 535, 200, 100, 40, null);
							g.drawImage(winAlone, 650, 200, 100, 40, null);
						}
						else {
							//g.drawString(nombres[fighter1]+" WIN", 500, 220);
							g.drawImage(nombres[fighter1], 480, 200, 215, 40, null);
							g.drawImage(winAlone, 705, 200, 100, 40, null);
						}
						
					}
					
				}
				else if(resultado==2) {
					//Si estas en un jugador y pierdes...
					if(mode==1) {
						//g.drawString("YOU LOSE", 540, 220);
						g.drawImage(youLose, 520, 200, 235, 40, null);
					}
					//Si estas en 2 jugadores y gana el player 2
					else {
						if(fighter2==2) {
							g.drawImage(nombres[fighter2], 535, 200, 100, 40, null);
							g.drawImage(winAlone, 650, 200, 100, 40, null);
						}
						else {
							g.drawImage(nombres[fighter2], 480, 200, 215, 40, null);
							g.drawImage(winAlone, 705, 200, 100, 40, null);
						}
						
					}
					
				}
				
			}
			//Mostrar puntos
			else if(msjPuntos) {
			    
				//Puntos time
				g.setFont(pixelFontBig); 
				g.setColor(Color.WHITE);
				//g.drawString("TIME "+Integer.toString(puntosTime), 500, 220);
				g.drawImage(timePoints, 490, 190, 115, 40, null);
				String puntoss = Integer.toString(puntosTime);
				int xPos1 = 648;
				for(int j=0; j<puntoss.length();j++) {
					g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(puntoss.charAt(j)))), 
							xPos1, 190, 45, 40, null);
					xPos1+=45;
				}
				
				//Puntos time + puntos vital
				if(tiempoTime<=0) {
					g.setFont(pixelFontBig); 
					g.setColor(Color.WHITE);
					
					//g.drawString("VITAL "+Integer.toString(puntosVital), 500, 270);
					//g.drawString("VITAL "+Integer.toString(puntosVital), 500, 270);	
					g.drawImage(vitalPoints, 490, 235, 148, 40, null);
					String vitall = Integer.toString(puntosVital);
					int xPos2 = 648;
					for(int j=0; j<vitall.length();j++) {
						g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(vitall.charAt(j)))), 
								xPos2, 235, 45, 40, null);
						xPos2+=45;
					}
				}
				//Puntos time + puntos vita + puntos bonus
				if(tiempoVital<=0) {
					//El bonus se usa para controlar el tiempo a mostrar el bonus
					//asi que si se supera el valor real de bonus, se muestra dicho valor maximo
					if(bonus>(puntosTime+puntosVital)) {
						//g.drawString("BONUS "+Integer.toString(puntosTime+puntosVital), 500, 320);
						g.drawImage(bonusPoints, 490, 280, 148, 40, null);
						String bonuss = Integer.toString(puntosTime+puntosVital);
						int xPos3 = 648;
						for(int j=0; j<bonuss.length();j++) {
							g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(bonuss.charAt(j)))), 
									xPos3, 280, 45, 40, null);
							xPos3+=45;
						}
					}
					else {
						//Sonido puntos
						handler.getGame().setSoundEffect(27);
						//Sumar puntos al marcador superior que suma los puntos por golpes + bonus
						if(resultado==1) {
							handler.getGame().setPuntos1(handler.getGame().getPuntos1()+100);
						}
						else if(resultado==2) {
							handler.getGame().setPuntos2(handler.getGame().getPuntos2()+100);
						}
						//Mostrar puntos de bonus 
						//g.drawString("BONUS "+Integer.toString(bonus), 500, 320);
						g.drawImage(bonusPoints, 490, 280, 148, 40, null);
						String bonuss = Integer.toString(bonus);
						int xPos3 = 648;
						for(int j=0; j<bonuss.length();j++) {
							g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(bonuss.charAt(j)))), 
									xPos3, 280, 45, 40, null);
							xPos3+=45;
						}
					}
					
					bonus+=100;
				}
			}
		}
		//Mostrar cara reventada
		else {
			g.drawImage(Assets.fondoLetras, 0, 0, 1280,720,null);
			//Si ha ganado player 1 -> player1 normal y player2 reventado
			if(resultado==1) {
				g.drawImage(Assets.getFaceAlive(fighter1, 1),50,100,400,400,null);
				g.drawImage(Assets.getFaceDead(fighter2, 2),830,100,400,400,null);
			}
			//Si ha ganado player 2 -> player2 normal y player1 reventado
			else if(resultado==2) {
				g.drawImage(Assets.getFaceDead(fighter1, 1),50,100,400,400,null);
				g.drawImage(Assets.getFaceAlive(fighter2, 2),830,100,400,400,null);
			}
			
		}
	}
	
	//0=nadie ha ganado de momento / 1=player1 win / 2=player2 win
	public int hayGanador() {
		if(marcador1==2) {
			resultado=1;
			return 1;
		}
		else if(marcador2==2) {
			resultado=2;
			return 2;
		}
		else {
			return 0;
		}
	}
	
	//Actualizar variables para gestionar el final de ronda
	public void accionFinRonda() {
		//Si ha ganado alguien pero aun no se ha vencido al mejor de 3
		if(hayGanador()==0 && finRonda) {
			//Se reinicia el tiempo
			handler.getGame().tiempoRestante=99;
			
			if(fighter2==0) {
				world=new WorldBrazil(handler,fighter1,fighter2,mode);
			}
			else if(fighter2==1) {
				world=new WorldChina(handler,fighter1,fighter2,mode);
			}
			else {
				world=new WorldJapan(handler,fighter1,fighter2,mode);
			}
			finRonda=false;
			
			//Va a comenzar una nueva ronda
			contadorRonda++;
			msjRonda=true;
			if(contadorRonda==2) {
				handler.getGame().setSoundEffect(22,true);
			}
			else if(contadorRonda==3) {
				handler.getGame().setSoundEffect(23,true);
			}
		}
		//Si ha ganado alguien y ya hay vencedor al mejor de 3 se muestran las caras reventadas
		else if(hayGanador()!=0 && finRonda){
			fighting=false;
			handler.getGame().setFighting(false);
			finRonda=false;
			handler.getGame().setFade(true);
			handler.getGame().setCurrentSong(1);
			//Si ha sido la primera pelea
			if(handler.getGame().getNumPelea()==0) {
				//Si gana el player1 se sigue con el modo historia
				if(hayGanador()==1) {
					//Contar la pelea en la que se esta
					handler.getGame().setNumPelea(handler.getGame().getNumPelea()+1);
					fighterToSelectFighter=fighter1;
				}
				//Si gana la CPU se reinicia el modo historia
				else if(hayGanador()==2) {
					handler.getGame().setNumPelea(0);
					//fighterToSelectFighter=3;
					mostrarScoreLose=true;
					
				}
			}
			//Ha terminado el modo historia asi que se vuelve a empezar
			else {
				handler.getGame().setNumPelea(0);
				
				//Si ganas la segunda pelea del modo historia se muestra el final
				if(hayGanador()==1) {
					mostrarScoreWin=true;
				}
				//Si pierdes la segunda pelea del modo historia se va directamente a ScoreState
				else if(hayGanador()==2) {
					mostrarScoreLose=true;
				}
				
			}
			
		}
	}
	
	public boolean isGameState() {
		return true;
	}
	
	public void resetState() {}
}
