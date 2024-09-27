package streetfighter.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import streetfighter.Game;
import streetfighter.Handler;
import streetfighter.Launcher;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;
import streetfighter.utils.Utils;

public class ControlesState extends State {
	
	// Evita el problema con el tick() mas rapido que el KeyReleased
	private boolean primeraVez;
	
	// Graficos
	private BufferedImage controles, lineaAzul, pPause, escQuit, back, playerr1, playerr2,
						  linea, lineaAzul2, specialAttack, crouchPunch, crouchKick, airPunch,
						  flecha, walkLeftRight, mas, changeKey, block, moreInfoIm, enterMoreInfo,
						  crouchBlock;
	
	private int posAvionX[]= {690,920};
	private int posAvionY[]= {100,150,200,250,300,350,400,483};
	private int opcion;
	private int player; //0->player1,1->player2
	private int iParpadear;
	private boolean parpadear;
	private boolean cambio;
	private boolean moreInfo;
	private int parpadearEnter;
	
	// Keys
	public boolean[] keys_p1, keys_p2; //Teclas de cada jugador
	public int[] keys_p1_index, keys_p2_index; //Indices de los keyevents de cada tecla
	public int N_Keys; //Numero de teclas que tiene cada jugador
	
	private BufferedImage movimientoss[];
	//public String[] movimientos= {"JUMP","CROUCH","WALK LEFT","WALK RIGHT","PUNCH",
	//							  "LOW KICK","HIGH KICK"};
	
	//SPECIAL AHORA ES COMBO 
	
	//Orden vector de Keys_p1 y Keys_p2:
	/*
	1 UP
	2 DOWN
	3 LEFT
	4 RIGHT
	5 PUNCH_M
	6 KICK_L
	7 KICK_H
	*/
	
	public Font pixelFont;	//Fuente arcade
	
	BufferedImage fondo;
	
	private LettersNumbers letNums;
		
	public ControlesState(Handler handler) {
		super(handler);
		primeraVez=false;
		moreInfo = false;
		letNums = new LettersNumbers();
		parpadearEnter=0;
		
		fondo=ImageLoader.loadImage("/res/menus/fondo.png");
		back=ImageLoader.loadImage("/res/textures/rankingMenu/back.png");
		pPause=ImageLoader.loadImage("/res/textures/optionsMenu/pPauseGame.png");
		escQuit=ImageLoader.loadImage("/res/textures/optionsMenu/escQuitGame.png");
		playerr1=ImageLoader.loadImage("/res/textures/controlsMenu/player1.png");
		playerr2=ImageLoader.loadImage("/res/textures/controlsMenu/player2.png");
		linea=ImageLoader.loadImage("/res/textures/controlsMenu/linea.png");
		lineaAzul2=ImageLoader.loadImage("/res/textures/controlsMenu/lineaAzul.png");
		specialAttack=ImageLoader.loadImage("/res/textures/controlsMenu/specialAttack.png");
		crouchPunch=ImageLoader.loadImage("/res/textures/controlsMenu/crouchPunch.png");
		crouchKick=ImageLoader.loadImage("/res/textures/controlsMenu/crouchKick.png");
		airPunch=ImageLoader.loadImage("/res/textures/controlsMenu/airPunch.png");
		flecha=ImageLoader.loadImage("/res/textures/controlsMenu/flech.png");
		walkLeftRight=ImageLoader.loadImage("/res/textures/controlsMenu/walkLeftRight.png");
		changeKey=ImageLoader.loadImage("/res/textures/controlsMenu/changeKey.png");
		mas=ImageLoader.loadImage("/res/textures/controlsMenu/mas.png");
		block=ImageLoader.loadImage("/res/textures/controlsMenu/block.png");
		moreInfoIm=ImageLoader.loadImage("/res/textures/controlsMenu/moreInfo.png");
		enterMoreInfo=ImageLoader.loadImage("/res/textures/controlsMenu/enterMoreInfo.png");
		crouchBlock=ImageLoader.loadImage("/res/textures/controlsMenu/crouchBlock.png");
		movimientoss = new BufferedImage[7];
		movimientoss[0]=ImageLoader.loadImage("/res/textures/controlsMenu/jump.png");
		movimientoss[1]=ImageLoader.loadImage("/res/textures/controlsMenu/crouch.png");
		movimientoss[2]=ImageLoader.loadImage("/res/textures/controlsMenu/walkLeft.png");
		movimientoss[3]=ImageLoader.loadImage("/res/textures/controlsMenu/walkRight.png");
		movimientoss[4]=ImageLoader.loadImage("/res/textures/controlsMenu/punch.png");
		movimientoss[5]=ImageLoader.loadImage("/res/textures/controlsMenu/lowKick.png");
		movimientoss[6]=ImageLoader.loadImage("/res/textures/controlsMenu/highKick.png");
		
		//Leemos el fichero de configuracion de teclas:
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (handler.getGame().path+"/.configStreetFighterII/keys");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
	        String linea;
	        String[] linea_split = null;
	        
	        //Leemos numero de teclas por jugador
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        N_Keys = Integer.parseInt(linea_split[0]);
	        
	        //Inicializamos vectores:
			keys_p1=new boolean[N_Keys];
			keys_p2=new boolean[N_Keys];
			keys_p1_index = new int[N_Keys];
			keys_p2_index = new int[N_Keys];
	        
			br.readLine(); //Linea vacia
			
			//Leemos los indices de las teclas del jugador 1
			char c;
	        for (int i=0; i<N_Keys; i++) {
		        //Leemos indice de la tecla I
		        linea = br.readLine();
		        linea_split = linea.split(" ");
		        if (linea_split[0].length()> 1) {
		        	//ENTER O FLECHA
		        	keys_p1_index[i] = Utils.string_to_key(linea_split[0]);
		        }
		        else {
		        	c = linea_split[0].charAt(0);
		        	keys_p1_index[i] = Utils.character_to_key(c);
		        }
	        }
	        
	        br.readLine(); //Linea vacia
	        
			//Leemos los indices de las teclas del jugador 2
	        for (int i=0; i<N_Keys; i++) {
		        //Leemos indice de la tecla I
		        linea = br.readLine();
		        linea_split = linea.split(" ");
		        if (linea_split[0].length()> 1) {
		        	//ENTER O FLECHA
		        	keys_p2_index[i] = Utils.string_to_key(linea_split[0]);
		        }
		        else {
		        	c = linea_split[0].charAt(0);
		        	keys_p2_index[i] = Utils.character_to_key(c);
		        }
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
		
		controles=ImageLoader.loadImage("/res/textures/optionsMenu/controles.png");
		lineaAzul=ImageLoader.loadImage("/res/textures/optionsMenu/lineaAzul.png");
		
		opcion=0;
		player=0;
		parpadear=false;
		cambio=false;
		iParpadear=0;
		
		try {
			InputStream is = Game.class.getResourceAsStream("/res/fonts/pixel.ttf");
			pixelFont=Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(25f);
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(pixelFont);
		}
		catch(IOException | FontFormatException e) {
			System.out.println("ERROR AL CARGAR LA FUENTE");
		}
	}
	
	
	@Override
	public void tick() {
		// Si se pulsa una tecla
		if(handler.getGame().getKeyManager().primeraVez  && primeraVez) {
			primeraVez=false;
			
			if(handler.getKeyManager().ESC && !handler.getGame().quererSalir){ // Salir juego
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
				if(handler.getKeyManager().enter && opcion == 7) {
					moreInfo = true;
				}
				if(handler.getKeyManager().enter && !moreInfo){
					if(parpadear && cambio) {	// Si pulsar enter supone confirmacion de cambio de tecla
						actualizarFicheroConfigKeys();
						handler.getKeyManager().actualizar();
					}
					parpadear=!parpadear;
					cambio=false;
				}
				else if(!moreInfo){
					if(!parpadear) {	// Si no se esta en el proceso de cambiar control
						if(handler.getKeyManager().W){ // Arriba
							handler.getGame().setSoundEffect(26,false);
							if(opcion==0) {
								opcion=7;
							}
							else {
								opcion--;
							}
						}
						if(handler.getKeyManager().S){ // Abajo
							handler.getGame().setSoundEffect(26,false);
							if(opcion==7) {
								opcion=0;
							}
							else {
								opcion++;
							}
						}
						
						if(handler.getKeyManager().D){ // Derecha
							handler.getGame().setSoundEffect(26,false);
							if(player==1) {
								player=0;
							}
							else {
								player++;
							}
						}
						if(handler.getKeyManager().A){ // Izquierda
							handler.getGame().setSoundEffect(26,false);
							if(player==0) {
								player=1;
							}
							else {
								player--;
							}
						}
					}
					else {	// Cambiar control correspondiente si necesario (Esta parpadeando una letra)
						int tecla = handler.getKeyManager().teclaPulsada();
						if(tecla!=-1) {
							String teclaString = Utils.key_to_string(tecla);
							if(teclaString.equals("NO RECONOCIDA") || teclaString.equals("P")) {
								//Error
								handler.getGame().setSoundEffect(24,false);
							}
							else {
								if(player==0) {
									keys_p1_index[opcion] = tecla;
								}
								else {
									keys_p2_index[opcion] = tecla;
								}
								cambio=true;
							}
						}
					}
				}
				
				if(handler.getKeyManager().B && moreInfo) { // Volver a options state
					handler.getGame().setSoundEffect(25,false);
					moreInfo = false;
				}
				else if(handler.getKeyManager().B && !moreInfo) { // Volver a options state
					handler.getGame().setSoundEffect(25,false);
					handler.getGame().optionsState.resetState();
					State.setState(handler.getGame().optionsState);
				}
			}
		}
		else if(!handler.getGame().getKeyManager().primeraVez) {
			primeraVez=true;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		//g.fillRect(0, 0, handler.getGame().getWidth(), handler.getGame().getHeight());
		g.drawImage(fondo, 0, 0, 1280,720, null);
		
		g.drawImage(controles, 150, 15, 400, 60, null);
		g.drawImage(lineaAzul, 155, 78, 390, 5, null);
		
		if(!moreInfo) {
			//g.drawString("PLAYER 1     PLAYER 2 ", 675, 200);
			g.drawImage(playerr1, 670, 40, 200, 40, null);
			g.drawImage(playerr2, 910, 40, 200, 40, null);
			
			g.setFont(handler.getGame().pixelFont);
			g.setColor(Color.WHITE);
			
			int yMovimientos=100;
			if(parpadear) {
				for(int i=0; i<movimientoss.length; i++) {
					//g.drawString(movimientos[i], 150, yMovimientos);
					renderNombreMov(i, 150, yMovimientos, g);
					if(player==0 && iParpadear>10 && iParpadear<=20 && opcion==i) {}
					else {
						renderMov(Utils.key_to_string(keys_p1_index[i]), 740, yMovimientos, g);
					}
					if(player==1 && iParpadear>10 && iParpadear<=20 && opcion==i) {}
					else {
						renderMov(Utils.key_to_string(keys_p2_index[i]), 975, yMovimientos, g);
					}
					yMovimientos+=50;
				}
			}
			else {
				for(int i=0; i<movimientoss.length; i++) {
					//g.drawString(movimientos[i], 150, yMovimientos);
					renderNombreMov(i, 150, yMovimientos, g);
					renderMov(Utils.key_to_string(keys_p1_index[i]), 740, yMovimientos, g);
					renderMov(Utils.key_to_string(keys_p2_index[i]), 975, yMovimientos, g);
					yMovimientos+=50;
				}
			}
			
			if(iParpadear>20) {
				iParpadear=0;
			}
			iParpadear++;
			
			//g.drawImage(linea, 30, 447, 1220, 4, null);
			//g.drawImage(lineaAzul2, 30, 451, 1220, 3, null);

			g.drawImage(moreInfoIm, 420, 480, 440, 50, null);
			
			if(parpadearEnter>20) {
				if(opcion!=7) {
					g.drawImage(changeKey, 420, 570, 440, 40, null);
				}
				else {
					g.drawImage(enterMoreInfo, 430, 570, 420, 40, null);
				}
				if(parpadearEnter==41)
					parpadearEnter=0;
			}
			parpadearEnter++;
			
			//g.drawImage(linea, 30, 690, 1220, 4, null);
			//g.drawImage(lineaAzul2, 30, 694, 1220, 3, null);
			
			// Avion
			if(opcion == 7) {
				g.drawImage(Assets.avion, 370, posAvionY[opcion], 40, 40, null);
			}
			else {
				g.drawImage(Assets.avion, posAvionX[player], posAvionY[opcion], 40, 40, null);
			}
			
			// Mensajes de ayuda
			g.drawImage(back, 20, 690, 140, 30, null);
			g.drawImage(pPause, 210, 690, 270, 30, null);
			g.drawImage(escQuit, 530, 690, 280, 30, null);
			
		}
		else {
			int a = 250;
			g.drawImage(specialAttack, 150, 460-a, 330, 35, null);
			g.drawImage(crouchPunch, 150, 505-a, 290, 35, null);
			g.drawImage(crouchKick, 150, 555-a, 270, 35, null);
			g.drawImage(airPunch, 150, 605-a, 220, 35, null);
			g.drawImage(block, 150, 655-a, 120, 35, null);
			g.drawImage(crouchBlock, 150, 705-a, 290, 35, null);
			
			g.drawImage(movimientoss[1], 540, 460-a, 160, 35, null);
			g.drawImage(flecha, 710, 460-a, 40, 35, null);
			g.drawImage(walkLeftRight, 760, 460-a, 300, 35, null);
			g.drawImage(flecha, 1070, 460-a, 40, 35, null);
			g.drawImage(movimientoss[4], 1120, 460-a, 120, 35, null);
			
			g.drawImage(movimientoss[1], 540, 505-a, 160, 35, null);
			g.drawImage(mas, 710, 505-a, 40, 35, null);
			g.drawImage(movimientoss[4], 760, 505-a, 120, 35, null);
			
			g.drawImage(movimientoss[1], 540, 555-a, 160, 35, null);
			g.drawImage(mas, 710, 555-a, 40, 35, null);
			g.drawImage(movimientoss[5], 760, 555-a, 200, 35, null);
			
			g.drawImage(movimientoss[0], 540, 605-a, 120, 35, null);
			g.drawImage(flecha, 670, 605-a, 40, 35, null);
			g.drawImage(movimientoss[4], 720, 605-a, 120, 35, null);
			
			g.drawImage(movimientoss[2], 540, 655-a, 220, 35, null);
			
			g.drawImage(movimientoss[1], 540, 705-a, 160, 35, null);
			g.drawImage(mas, 710, 705-a, 40, 35, null);
			g.drawImage(movimientoss[2], 760, 705-a, 220, 35, null);
			
			g.drawImage(back, 20, 690, 140, 30, null);
		}
		
		if(handler.getGame().quererSalir) {
			handler.getGame().renderMenuEsc(g, Assets.avion);
		}
		
	}
	
	private void renderMov(String tecla, int xPos, int yMovimientos, Graphics g) {
		if(tecla.length()>1) {
			int offset=0;
			for(int i=0; i<tecla.length();i++) {
				g.drawImage(letNums.getLetter(tecla.charAt(i)), xPos+offset, yMovimientos, 40, 40, null);
				offset+=35;
			}
		}
		else {
			g.drawImage(letNums.getLetter(tecla.charAt(0)), xPos, yMovimientos, 40, 40, null);
		}
		
	}
	
	private void renderNombreMov(int id, int xPos, int yMovimientos, Graphics g) {
		if(id==0) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 120, 40, null);
		}
		else if(id==1) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 160, 40, null);
		}
		else if(id==2) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 225, 40, null);		
		}
		else if(id==3) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 235, 40, null);
		}
		else if(id==4) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 150, 40, null);
		}
		else if(id==5) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 200, 40, null);
		}
		else if(id==6) {
			g.drawImage(movimientoss[id], 150, yMovimientos, 220, 40, null);
		}
		
	}
	
	private void actualizarFicheroConfigKeys() {
		String contenido = Integer.toString(N_Keys)+" #NKeys\n\n";
		
		String player1 = 
				Utils.key_to_string(keys_p1_index[0])+" #UP\t\t#PLAYER 1\n" + 
				Utils.key_to_string(keys_p1_index[1])+" #DOWN\n" +
				Utils.key_to_string(keys_p1_index[2])+" #LEFT\n" +
				Utils.key_to_string(keys_p1_index[3])+" #RIGHT\n" +
				Utils.key_to_string(keys_p1_index[4])+" #PUNCH_M\n" +
				Utils.key_to_string(keys_p1_index[5])+" #KICK_L\n" +
				Utils.key_to_string(keys_p1_index[6])+" #KICK_H\n\n";
		
		String player2 = 
				Utils.key_to_string(keys_p2_index[0])+" #UP\t\t#PLAYER 2\n" + 
				Utils.key_to_string(keys_p2_index[1])+" #DOWN\n" +
				Utils.key_to_string(keys_p2_index[2])+" #LEFT\n" +
				Utils.key_to_string(keys_p2_index[3])+" #RIGHT\n" +
				Utils.key_to_string(keys_p2_index[4])+" #PUNCH_M\n" +
				Utils.key_to_string(keys_p2_index[5])+" #KICK_L\n" +
				Utils.key_to_string(keys_p2_index[6])+" #KICK_H";
		
		contenido = contenido+player1+player2;
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(handler.getGame().path+"/.configStreetFighterII/keys");
            pw = new PrintWriter(fichero);

            pw.print(contenido);
            pw.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
}
