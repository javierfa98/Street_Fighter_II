package streetfighter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import streetfighter.ia.Brain;
import streetfighter.audio.MusicPlayer;
import streetfighter.display.Display;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;
import streetfighter.input.KeyManager;
import streetfighter.input.MouseManager;
import streetfighter.states.EndGameState;
import streetfighter.states.EnterGameState;
import streetfighter.states.MenuState;
import streetfighter.states.SelectFighterState1;
import streetfighter.states.SelectFighterState2;
import streetfighter.states.State;

/*********GAME LOOP***********
 * 1)actualizar variables, posiciones de objetos...
 * 2)dibujar todo en la pantalla
 * volver al paso 1)
 */

//Clase principal del juego
//implements Runnable para que se pueda correr la clase en un thread
public class Game implements Runnable{
	private Display display;
	//Para acceder a estos valores con facilidad (dimension pantalla)
	private int width, height;
	public String title;
	
	//Controla el bucle del juego (game loop)
	private boolean running=false;
	private Thread thread;
	
	//Escribir en buffer antes de escribir en el canvas para evitar que se vea mal
	private BufferStrategy bs;
	//Para pintar los graficos
	private Graphics g;
	
	//States
	public State entergameState;
	public State menuState;
	public State optionsState;
	public State controlesState;
	public State selectFighterState1;	//1 Player
	public State selectFighterState2;	//2 Players
	public State gameState;
	public State pauseState;
	public State controlesStatePause;
	public State endGameState;
	
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//Handler
	private Handler handler;
	
	MusicPlayer mp;
	
	//Se inicializa en el constructor de GameState
	public int tiempoRestante=0;
	
	//1=Player1 VS ia  		2=Player1 VS Player2
	public int mode=0;
	
	private Brain brain1;
	private Brain brain2;
	
	private int puntos1;		//Puntos acumulados por el player 1 en la pelea actual
	private int puntos2;		//Puntos acumulados por el player 2 en la pelea actual
	private int numPelea;		//Indica por qué pelea se va en el modo historia (0 o 1)
	public int dificultad;				//Dificultad del juego
	
	private boolean fighting;
	private boolean tiempoQuieto;	//Para que no pase el tiempo de la pelea mientras se muestran mensajes
	
	public Font pixelFont;	//Fuente arcade
	
	float alpha=1.0f;
	private boolean fade=false;  //fade=true, hacer efecto desvanecido
	
	int timeTime=10;		//Para que parpadee
	
	public boolean finPeleaDemo=false;
	public boolean tiempoDemo=false;
	
	public int volumenMusica; //Valor entre 0 y 8
	private int valoresMusica[] = {-80,-60,-50,-40,-30,-20,-10,0,5};
	public int volumenEfectos;	//Valor entre 0 y 8
	private int valoresEfectos[] = {-80,-40,-20,-10,0,2,4,5,6};
	public boolean mute;
	
	private int resolucionWidth[]= {0,0,1280,640};
	private int resolucionHeight[]= {0,0,720,480};
	public int valorResolucion=0;
	public boolean undecorated=false;
	
	public int wPantalla;
	public int hPantalla;
	
	public String path;
	
	public boolean quererSalir;
	public int xEsc, yEsc, widthEsc, heightEsc, posAvion;
	private BufferedImage exitGameMenu;
	private LettersNumbers letNums;
	
	public Game(String title, int width, int height, MusicPlayer mp, String path) throws IOException {
		this.path=path;
		this.width=width;
		this.height=height;
		this.title=title;
		this.mp=mp;
		keyManager=new KeyManager(path);
		mouseManager=new MouseManager();
		brain1=new Brain(this,1);
		brain2=new Brain(this,2);
		fighting=true;
		tiempoQuieto=true;
		numPelea=0;
		//mute=fsalse;
		wPantalla=width;
		hPantalla=height;
		quererSalir=false;
		xEsc=0;
		yEsc=0;
		widthEsc=0;
		heightEsc=0;
		posAvion=1;
		exitGameMenu=ImageLoader.loadImage("/res/textures/optionsMenu/exitGame.png");
		letNums = new LettersNumbers();
		//Leer fichero de configuracion de volumen
		leerConfigVolumen();
		//Para tener la dificultad de la pelea hay que leerla del fichero config/dificultad
		leerConfigDificultad();
		//Para que se habra el juego con la ultima resolucion guardada
		leerConfigResolucion();
		if(valorResolucion==0) {
			undecorated=true;
		}
		else {
			undecorated=false;
		}
		
		try {
			InputStream is = Game.class.getResourceAsStream("/res/fonts/pixel.ttf");
			pixelFont=Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f);
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(pixelFont);
			
		}
		catch(IOException | FontFormatException e) {
			System.out.println("ERROR AL CARGAR LA FUENTE");
			e.printStackTrace();
		}
	}
	
	private void leerConfigResolucion() {
		
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/resolucion");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
	        String linea;
	        String[] linea_split = null;
	        
	        //Leemos resolucion
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        valorResolucion = Integer.parseInt(linea_split[0]);
	        //Si la resolucion no es full screen la cambiamos
	        if(valorResolucion!=0  && valorResolucion!=1) {
	        	width=resolucionWidth[valorResolucion];
		        height=resolucionHeight[valorResolucion];
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
	
	private void leerConfigVolumen() {
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/volumenJuego");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
	        String linea;
	        String[] linea_split = null;
	        
	        //Leemos volumen ON OFF
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        if(Integer.parseInt(linea_split[0])==0) {
	        	mute=false;
	        }
	        else {
	        	mute=true;
	        }
	        
	  
	        
	        //Leemos volumen musica
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        volumenMusica = Integer.parseInt(linea_split[0]);
	        
	        
	        
	        //Leemos volumen efectos
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        volumenEfectos = Integer.parseInt(linea_split[0]);
	       
	        
	        
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
	
	private void leerConfigDificultad() {
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/dificultad");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
			// Lectura del fichero
	        String linea;
	        String[] linea_split = null;
	        
	        //Leemos dificultad
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        dificultad = Integer.parseInt(linea_split[0]);
			
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
	
	//Solo se va a ejecutar una vez y deja los gráficos iniciales preparados
	private void init() {
		//Crear pantalla
		initDisplay();
		//Importar imagenes o sonidos que se vayan a usar durante el juego
		
		handler=new Handler(this);
		
		//A gameState hauy que pasarle el escenario en el que se lucha (0,1,2) y el luchador(0,1,2)
		//gameState=new GameState(handler,0,2,1);
		entergameState=new EnterGameState(handler);
		menuState=new MenuState(handler);
		selectFighterState1=new SelectFighterState1(handler,3);
		selectFighterState2=new SelectFighterState2(handler);
		
		State.setState(entergameState);
		
		Assets.initAssets_EndGameState();
		Assets.initAssets_MenuState();
		
		/*//PARA HACER PRUEBAS DE EL FINAL DEL MODO HISTORIA
		Assets.initAssets_EndGameState();
		State EndGameState = new EndGameState(handler,0);
		State.setState(EndGameState);*/
		
	}
	
	public void initDisplay() {
		//Crear pantalla
		display=new Display(title,width,height,path,undecorated); 
		//Gestion de Keyboard Input
		display.getFrame().addKeyListener(keyManager);
		//Gestion del raton
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		//Añadir getCanvas porque sino puede haber glitches
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
	}
	
	private void fadeEffect(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    alpha-=0.01f;
	}
	
	
	//Actualiza todas las variables del juego (de acuerdo al estado en concreto)
	private void tick() {
		keyManager.tick();
		
		//BACK DOOR para las cinematicas del modo historia
		if(keyManager.uno) {
			handler.getGame().setCurrentSong(7);
			State EndGameState = new EndGameState(handler,0,true);
			fighting=false;
			State.setState(EndGameState);
		}
		else if(keyManager.dos) {
			handler.getGame().setCurrentSong(8);
			State EndGameState = new EndGameState(handler,1,true);
			fighting=false;
			State.setState(EndGameState);
		}
		else if(keyManager.tres) {
			handler.getGame().setCurrentSong(9);
			State EndGameState = new EndGameState(handler,2,true);
			fighting=false;
			State.setState(EndGameState);
		}
		
		//player1 vs CPU(player2)
		if(mode==1) {
			brain2.tick();
		}
		//CPU vs CPU
		else if(mode==3) {
			brain1.tick();
			brain2.tick();
		}
		
		//Si estamos en un estado concreto (que existe)
		if(State.getState()!=null) {
			State.getState().tick();
		}
	}
	
	//Renderiza el juego (muestra en la pantalla los cambios)
	private void render() {
		//Buffer->Buffer->Actual canvas
		
		//Cuantos buffer va a usar nuestro canvas
		bs=display.getCanvas().getBufferStrategy();
		//Si no se ha establecido ya el numero de buffers se añade
		if(bs==null) {
			//3 es el maximo a usar
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		//Dibujar cosas en el canvas---------------------------------------
		//pixel(0,0) es la esquina arriba izquierda  
		g=bs.getDrawGraphics();
		//g2=bs.getDrawGraphics();
		//Antes de dibujar nada limpiar la pantalla
		g.clearRect(0, 0, width, height); //limpiar toda la pantalla
		
		Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.scale((float)width/1280, (float)height/720);
        
	
		//Realizar render() de acuerdo al estado actual
		if(State.getState()!=null) {
			State.getState().render(g2d);
			//Mostrar el coundown si se esta en GameState
			if((State.getState().isGameState() || tiempoDemo) && fighting) {
				g2d.setFont(pixelFont); 
				g2d.setColor(Color.WHITE);
				int xPos = width/2 - 45;
				int yPos = (int)(height/5.5);
				String timee = Integer.toString(tiempoRestante);
				//Para que parpadee
				if(tiempoRestante<10 && tiempoRestante>0) {
					timeTime--;
					if(timeTime==0) {
						g2d.setColor(Color.YELLOW);
						//g2d.drawString(Integer.toString(tiempoRestante), 630, 155);
						for(int j=0; j<timee.length();j++) {
							g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(timee.charAt(j)))), 
									xPos, yPos, 45, 40, null);
							xPos+=45;
						}
						
						timeTime=10;
					}
					else {
						//g2d.drawString(Integer.toString(tiempoRestante), 625, 155);
						for(int j=0; j<timee.length();j++) {
							g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(timee.charAt(j)))), 
									xPos, yPos, 45, 40, null);
							xPos+=45;
						}
					}
				}
				else {
					//g2d.drawString(Integer.toString(tiempoRestante), 615, 155);
					for(int j=0; j<timee.length();j++) {
						g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(timee.charAt(j)))), 
								xPos, yPos, 45, 40, null);
						xPos+=45;
					}
				}
				

				
			}
		}
		
		//Efecto de fundido a la siguiente pantalla
		if(fade && alpha>0.0f) {
			fadeEffect(g);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		}
		else {
			alpha=1.0f;
			fade=false;
		}
		
		
		//Para que se muestre lo que hemos dibujado
		bs.show();
		
		
		
		//Para asegurar que se muestre de forma correcta
		g2d.dispose();
		
		//------------------------------------------------------------------
		
		
	}
	
	//Metodo obligatorio si se añade implements Runnable a una clase
	public void run() {
		running=true;
		
		init();
		
		//frames per second (veces que queremos que se ejecute un loop por segundo)
		int fps=60;
		//maximo tiempo para ejecutar cada game loop para lograr los fps (1000000000 ns = 1 second)
		double timePerTick=1000000000/fps;
		double delta=0; 	//Cuanto tiempo hay que esperar hasta ejecutar tick() y render()
		long now;
		long lastTime=System.nanoTime();
		long timer=0;		//Cuenta hasta llegar a un segundo
		int ticks=0;	    //Cuantos loops hemos realizado
		
		mp.setMusicStarts(true);
		//Game loop	
		while(running) {
			now=System.nanoTime();
			
			delta+=(now-lastTime)/timePerTick;
			timer+=now-lastTime;
			lastTime=now;
			
			if(delta >= 1) {
				tick();
				render();
				ticks++;
				delta--;
				
			}
			
			//Verificar que cada segundo se cumplen los fps
			if(timer >= 1000000000) {
				//System.out.println("fps: " + ticks);
				ticks=0;
				timer=0;	
				if((State.getState().isGameState() || tiempoDemo) && fighting && !tiempoQuieto) {
					tiempoRestante--;
				}
				else {
					lastTime=now;
				}

			}
			
		}
		
		//Finalizar thread
		stop();
	}
	
	public void renderMenuEsc(Graphics g, BufferedImage avion) {
		g.drawImage(exitGameMenu, xEsc, yEsc, widthEsc, heightEsc, null);
		
		if(widthEsc<800) {
			xEsc-=32;
			yEsc-=16;
			widthEsc+=64;
			heightEsc+=32;
		}
		else {
			if(posAvion==1) { //Izq
				g.drawImage(avion, 750, 345, 40, 40, null);
			}
			else { //Der
				g.drawImage(avion, 325, 345, 40, 40, null);
			}
		}
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	
	public void setCurrentSong(int song) {
		mp.setCurrentSongIndex(song);
	}
	
	public void setSoundEffect(int sound, boolean gameplay) {
		mp.setSoundEffect(sound,gameplay);
	}
	
	//Este es SOLO para puntos
	public void setSoundEffect(int sound) {
		mp.setSoundEffect(sound);
	}
	
	public void setMute(boolean muteValue) {
		mp.setMute(muteValue);
	}
	
	public void setVolumeMusic(int value) {
		mp.setMusicVolume(value);
	}
	
	public void setEfectsMusic(int value) {
		mp.setEfectsVolume(value);
	}
	
	public void stopCurrentSong() {
		mp.stopCurrentSong();
	}
	
	public boolean currentSoundFinished() {
		return mp.currentSoundFinished();
	}
	
	public Brain getBrain1() {
		return brain1;
	}
	
	public Brain getBrain2() {
		return brain2;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public int getPuntos1() {
		return puntos1;
	}
	
	public void setPuntos1(int puntos) {
		puntos1=puntos;
	}
	
	public int getPuntos2() {
		return puntos2;
	}
	
	public void setPuntos2(int puntos) {
		puntos2=puntos;
	}
	
	public void setFighting(boolean fighting) {
		this.fighting=fighting;
	}
	
	public void setTiempoQuieto(boolean tiempoQuieto) {
		this.tiempoQuieto=tiempoQuieto;
	}
	
	public void setFade(boolean fade) {
		this.fade=fade;
	}
	
	public boolean getFade() {
		return fade;
	}
	
	public int getNumPelea() {
		return numPelea;
	}
	
	public void setNumPelea(int numPelea) {
		this.numPelea=numPelea;
	}
	
	public int getDificultad() {
		return dificultad;
	}
	
	public void setDificultad(int dificultad) {
		this.dificultad=dificultad;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public MusicPlayer getMp() {
		return mp;
	}
	
	
	//Comienza el thread
	/*public synchronized void start() {
		//Si por algún motivo el thread ya está lanzado y por lo tanto el game loop
		//salimos de la funcion porque no queremos que se lance de nuevo otro thread
		if(running) {
			return;
		}
		running=true;
		//this porque queremos que corra en un thread la clase en la que estamos (Game)
		thread=new Thread(this);
		//Llama a la funcion run() declarada encima
		thread.start();
	}*/
	
	//Finaliza el thread
	public synchronized void stop() {
		//Al igual que en start evita que se finalice el thread si ya se había terminado el game loop
		if(!running) {
			return;
		}
		running=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return 1280;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return 720;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
