package streetfighter.states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
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

public class PauseState extends State {
	
	// Evita el problema con el tick() mas rapido que el KeyReleased
	private boolean primeraVez;
	
	// Graficos
	private BufferedImage menuPause, sonido, on, off, onGris, offGris,
						  volMusica, volEfectos, palitoColor, palitoGris,
						  quitToMenu, controles, lineaAzul, resolucion,
						  flechDcha, flechIzq, backFight;
	private BufferedImage resoluciones[];
	private int posAvionY[]= {153,233,313,393,473,553};
	private int opcion;
	private int xSpawnResoluciones[]= {730,652,760,760};
	private int anchuraResoluciones[]= {390,540,340,340};
	private int valoresMusica[] = {-80,-60,-50,-40,-30,-20,-10,0,5};
	private int valoresEfectos[] = {-80,-40,-20,-10,0,2,4,5,6};
	private int resolucionWidth[]= {0,0,1280,640};
	private int resolucionHeight[]= {0,0,720,480};
	
	public Font pixelFont;	//Fuente arcade
	
	BufferedImage fondo;

	public PauseState(Handler handler) {
		super(handler);
		
		menuPause=ImageLoader.loadImage("/res/textures/optionsMenu/menuPause.png");
		sonido=ImageLoader.loadImage("/res/textures/optionsMenu/sonido.png");
		on=ImageLoader.loadImage("/res/textures/optionsMenu/on.png");
		off=ImageLoader.loadImage("/res/textures/optionsMenu/off.png");
		onGris=ImageLoader.loadImage("/res/textures/optionsMenu/onGris.png");
		offGris=ImageLoader.loadImage("/res/textures/optionsMenu/offGris.png");
		volMusica=ImageLoader.loadImage("/res/textures/optionsMenu/volMusica.png");
		volEfectos=ImageLoader.loadImage("/res/textures/optionsMenu/volEfectos.png");
		palitoColor=ImageLoader.loadImage("/res/textures/optionsMenu/palitoColor.png");
		palitoGris=ImageLoader.loadImage("/res/textures/optionsMenu/palitoGris.png");
		quitToMenu=ImageLoader.loadImage("/res/textures/optionsMenu/quitToMenu.png");
		controles=ImageLoader.loadImage("/res/textures/optionsMenu/controles.png");
		lineaAzul=ImageLoader.loadImage("/res/textures/optionsMenu/lineaAzul.png");
		fondo=ImageLoader.loadImage("/res/menus/fondo.png");
		resolucion=ImageLoader.loadImage("/res/textures/optionsMenu/resolucion.png");
		resoluciones = new BufferedImage[4];
		resoluciones[0]=ImageLoader.loadImage("/res/textures/optionsMenu/fullScreen.png");
		resoluciones[1]=ImageLoader.loadImage("/res/textures/optionsMenu/fullScreenWindow.png");
		resoluciones[2]=ImageLoader.loadImage("/res/textures/optionsMenu/res2.png");
		resoluciones[3]=ImageLoader.loadImage("/res/textures/optionsMenu/res3.png");
		flechDcha=ImageLoader.loadImage("/res/textures/rankingMenu/flechaDcha.png");
		flechIzq=ImageLoader.loadImage("/res/textures/rankingMenu/flechaIzq.png");
		backFight=ImageLoader.loadImage("/res/textures/rankingMenu/backFight.png");
		
		opcion=0;
		
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
				if(handler.getKeyManager().enter){
					if(opcion==3) { // Opcion controles
						handler.getGame().setSoundEffect(25,false);
						//Assets.initAssets_ControlesState();
						handler.getGame().controlesStatePause=new ControlesStatePause(handler);
						State.setState(handler.getGame().controlesStatePause);
					}
					if(opcion==5) { // Volver al menu
						actualizarFicheroResolucion();
						handler.getGame().setSoundEffect(25,false);
						handler.getGame().setCurrentSong(3);
						//Para que en el nuevo empiecen a 0
						handler.getGame().setPuntos1(0);
						handler.getGame().setPuntos2(0);
						handler.getGame().setNumPelea(0);
						handler.getGame().setFighting(false);
						State.setState(handler.getGame().menuState);
					}
				}
				
				if(handler.getKeyManager().W){ // Arriba
					handler.getGame().setSoundEffect(26,false);
					if(opcion==0) {
						opcion=5;
					}
					else {
						opcion--;
					}
				}
				if(handler.getKeyManager().S){ // Abajo
					handler.getGame().setSoundEffect(26,false);
					if(opcion==5) {
						opcion=0;
					}
					else {
						opcion++;
					}
				}
				
				//Cambiar mute
				if(opcion==0 && handler.getKeyManager().A) { // Left
					if(handler.getGame().mute) {
						handler.getGame().mute = !handler.getGame().mute;
						handler.getGame().setMute(handler.getGame().mute);
						if(!handler.getGame().mute) {
							handler.getGame().setSoundEffect(25,false);
						}
						
						actualizarFicheroVolumen();
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
				}
				if(opcion==0 && handler.getKeyManager().D) { // Right
					if(!handler.getGame().mute) {
						handler.getGame().mute = !handler.getGame().mute;
						if(!handler.getGame().mute) {
							handler.getGame().setSoundEffect(25,false);
						}
						handler.getGame().setMute(handler.getGame().mute);
						
						actualizarFicheroVolumen();
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
				}
				
				//Cambiar volumen musica
				if(opcion==1 && handler.getKeyManager().A) { // Left
					if(handler.getGame().volumenMusica>0) {
						handler.getGame().volumenMusica--;
						handler.getGame().setVolumeMusic(valoresMusica[handler.getGame().volumenMusica]);
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
					
					actualizarFicheroVolumen();
				}
				if(opcion==1 && handler.getKeyManager().D) { // Right
					if(handler.getGame().volumenMusica<8) {
						handler.getGame().volumenMusica++;
						handler.getGame().setVolumeMusic(valoresMusica[handler.getGame().volumenMusica]);
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
					
					actualizarFicheroVolumen();
				}
				
				//Cambiar volumen efectos
				if(opcion==2 && handler.getKeyManager().A) { // Left
					if(handler.getGame().volumenEfectos>0) {
						handler.getGame().volumenEfectos--;
						handler.getGame().setEfectsMusic(valoresEfectos[handler.getGame().volumenEfectos]);
						handler.getGame().setSoundEffect(26,false); // Para que el jugador compruebe el nuevo valor
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
					
					actualizarFicheroVolumen();
				}
				if(opcion==2 && handler.getKeyManager().D) { // Right
					if(handler.getGame().volumenEfectos<8) {
						handler.getGame().volumenEfectos++;
						handler.getGame().setEfectsMusic(valoresEfectos[handler.getGame().volumenEfectos]);
						handler.getGame().setSoundEffect(26,false); // Para que el jugador compruebe el nuevo valor
					}
					else {
						//Error
						handler.getGame().setSoundEffect(24,false);
					}
					
					actualizarFicheroVolumen();
				}
				
				//Cambiar resolucion
				if(opcion==4 && handler.getKeyManager().A) { // Left
					handler.getGame().valorResolucion--;
					if(handler.getGame().valorResolucion==-1) {
						handler.getGame().valorResolucion=3;
					}
					//Modos Full Screen
					if(handler.getGame().valorResolucion==0 || handler.getGame().valorResolucion==1) {
						handler.getGame().setSoundEffect(26,false);
						//Pantalla completa
				        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				        int wPantalla = screenSize.width;
				        int hPantalla = screenSize.height;
				        handler.getGame().setWidth(wPantalla);
						handler.getGame().setHeight(hPantalla);
						if(handler.getGame().valorResolucion==1) {
							handler.getGame().undecorated=false;
						}
						else {
							handler.getGame().undecorated=true;
						}
						handler.getGame().getDisplay().closeJFrame();
						handler.getGame().initDisplay();
						
					}
					else {
						handler.getGame().setSoundEffect(26,false);
						handler.getGame().setWidth(resolucionWidth[handler.getGame().valorResolucion]);
						handler.getGame().setHeight(resolucionHeight[handler.getGame().valorResolucion]);
						handler.getGame().undecorated=false;
						handler.getGame().getDisplay().closeJFrame();
						handler.getGame().initDisplay();
					
					}
					
					actualizarFicheroResolucion();
					
					
				}
				if(opcion==4 && handler.getKeyManager().D) { // Right
					handler.getGame().valorResolucion++;
					if(handler.getGame().valorResolucion==4) {
						handler.getGame().valorResolucion=0;
					}
					
					//Modos Full Screen
					if(handler.getGame().valorResolucion==0 || handler.getGame().valorResolucion==1) {
						handler.getGame().setSoundEffect(26,false);
						//Pantalla completa
				        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				        int wPantalla = screenSize.width;
				        int hPantalla = screenSize.height;
				        handler.getGame().setWidth(wPantalla);
						handler.getGame().setHeight(hPantalla);
						if(handler.getGame().valorResolucion==1) {
							handler.getGame().undecorated=false;
						}
						else {
							handler.getGame().undecorated=true;
						}
						handler.getGame().getDisplay().closeJFrame();
						handler.getGame().initDisplay();
						
					}
					else {
						handler.getGame().setSoundEffect(26,false);
						handler.getGame().setWidth(resolucionWidth[handler.getGame().valorResolucion]);
						handler.getGame().setHeight(resolucionHeight[handler.getGame().valorResolucion]);
						handler.getGame().undecorated=false;
						handler.getGame().getDisplay().closeJFrame();
						handler.getGame().initDisplay();
					
					}
					
					actualizarFicheroResolucion();
					
				}
				
				if(handler.getKeyManager().B) { // Volver a game state (a la pelea)
					//if(cambioDificultad)
					//	actualizarFicheroDificultad();
					//handler.getGame().setSoundEffect(25,false);
					//handler.getGame().gameState.setPause(false);
					State.setState(handler.getGame().gameState);
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
		g.drawImage(menuPause, 420, 75, 440, 60, null);
		g.drawImage(lineaAzul, 420, 133, 440, 5, null);
		
		// Sonido on/off
		g.drawImage(sonido, 120, 150, 300, 60, null);
		if(handler.getGame().mute) {
			g.drawImage(onGris, 907, 157, 100, 50, null);
			g.drawImage(off, 1012, 157, 130, 50, null);
		}
		else {
			g.drawImage(on, 907, 157, 100, 50, null);
			g.drawImage(offGris, 1012, 157, 130, 50, null);
		}
		if(handler.getGame().mute) {
			g.drawImage(flechIzq, 795, 160, 40, 40, null);
		}
		if(!handler.getGame().mute) {
			g.drawImage(flechDcha, 1200, 160, 40, 40, null);
		}
		
		// Volumen musica
		g.drawImage(volMusica, 120, 230, 610, 60, null);
		int offset=0;
		for(int i=0;i<handler.getGame().volumenMusica;i++) {
			g.drawImage(palitoColor, 847+offset, 230, 40, 60, null);
			offset+=43;
		}
		for(int i=handler.getGame().volumenMusica;i<8;i++) {
			g.drawImage(palitoGris, 847+offset, 230, 40, 60, null);
			offset+=43;
		}
		if(handler.getGame().volumenMusica>0) {
			g.drawImage(flechIzq, 795, 240, 40, 40, null);
		}
		if(handler.getGame().volumenMusica<8) {
			g.drawImage(flechDcha, 1200, 240, 40, 40, null);
		}
		
		// Volumen efectos
		g.drawImage(volEfectos, 120, 310, 660, 60, null);
		offset=0;
		for(int i=0;i<handler.getGame().volumenEfectos;i++) {
			g.drawImage(palitoColor, 847+offset, 310, 40, 60, null);
			offset+=43;
		}
		for(int i=handler.getGame().volumenEfectos;i<8;i++) {
			g.drawImage(palitoGris, 847+offset, 310, 40, 60, null);
			offset+=43;
		}
		if(handler.getGame().volumenEfectos>0) {
			g.drawImage(flechIzq, 795, 320, 40, 40, null);
		}
		if(handler.getGame().volumenEfectos<8) {
			g.drawImage(flechDcha, 1200, 320, 40, 40, null);
		}
		
		// Controles
		g.drawImage(controles, 120, 390, 420, 60, null);
		
		// Resolucion
		g.drawImage(resolucion, 120, 470, 440, 60, null);
		g.drawImage(resoluciones[handler.getGame().valorResolucion], xSpawnResoluciones[handler.getGame().valorResolucion],
				470, anchuraResoluciones[handler.getGame().valorResolucion], 60, null);
		g.drawImage(flechIzq, 600, 480, 40, 40, null);
		g.drawImage(flechDcha, 1200, 480, 40, 40, null);
		
		//Volver menu
		g.drawImage(quitToMenu, 120, 550, 530, 60, null);
		
		// Mensajes de ayuda
		g.setFont(pixelFont);
		g.setColor(Color.WHITE);
		//g.drawString("B-GET BACK IN THE FIGHT", 10, 700);
		g.drawImage(backFight, 20, 680, 500, 30, null);
		
		// Avion
		g.drawImage(Assets.avion, 62, posAvionY[opcion], 40, 40, null);
		
		if(handler.getGame().quererSalir) {
			handler.getGame().renderMenuEsc(g, Assets.avion);
		}
		
	}
	
	private void actualizarFicheroResolucion() {
        
		String contenido = Integer.toString(handler.getGame().valorResolucion);
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	
            fichero = new FileWriter(handler.getGame().path+"/.configStreetFighterII/resolucion");
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
	
	private void actualizarFicheroVolumen() {
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	
            fichero = new FileWriter(handler.getGame().path+"/.configStreetFighterII/volumenJuego");
            pw = new PrintWriter(fichero);
            
            if(handler.getGame().mute) {
            	pw.println(1+" #1=OFF 0=ON");
            	pw.flush();
            }
            else {
            	pw.println(0+" #1=OFF 0=ON");
            	pw.flush();
            }
            
            pw.println(handler.getGame().volumenMusica+ " #musica");
            pw.flush();
            
            pw.println(handler.getGame().volumenEfectos + " #efectos");
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
	
	public void resetState() {
		opcion=0;
	}
	
	public boolean isGameState() {
		return false;
	}

	
}
