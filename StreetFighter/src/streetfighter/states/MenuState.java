package streetfighter.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import streetfighter.Game;
import streetfighter.Handler;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;

public class MenuState extends State{
	
	private boolean primeraVez; //Evita el problema con el tick() mas rapido que el KeyReleased
	private int posAvion[]= {429,484,539,594,648};
	private int opcion;
	private int yOffset;
	private BufferedImage wUpSDown, enterSelect;
	
	private int tiempoEspera;
	private int limEspera;
	
	// Fade
	private float alpha;
	private boolean fade;
	
	private boolean avion;
	
	public Font pixelFont;	//Fuente arcade
	
	BufferedImage fondo;

	public MenuState(Handler handler) {
		super(handler);
		primeraVez=false;
		opcion=0;
		yOffset=0;
		tiempoEspera=0;
		limEspera=60;
		
		alpha=1.0f;
		fade=false;
		avion=false;
		
		handler.getGame().optionsState=new OptionsState(handler);
		fondo=ImageLoader.loadImage("/res/menus/fondo.png");
		wUpSDown=ImageLoader.loadImage("/res/textures/rankingMenu/wUpSDown.png");
		enterSelect=ImageLoader.loadImage("/res/textures/rankingMenu/enterSelect.png");
		
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
		if(tiempoEspera==40) {
			handler.getGame().setCurrentSong(3);
		}
		if(tiempoEspera>limEspera) {
			tiempoEspera=limEspera+2;
			if(handler.getGame().getKeyManager().primeraVez && primeraVez) {
				primeraVez=false;
				
				if(handler.getKeyManager().ESC && !handler.getGame().quererSalir && avion){ // Salir juego
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
					//Si se pulsa enter
					if(handler.getKeyManager().enter && avion){
						handler.getGame().setSoundEffect(25,false);
						
						if(opcion==0) { // Menu player vs CPU
							Assets.initAssets_SelectFighterState();
							handler.getGame().selectFighterState1=new SelectFighterState1(handler,3);
							State.setState(handler.getGame().selectFighterState1);
							handler.getGame().setCurrentSong(3);
						}
						else if(opcion==1) { // Menu player1 vs player2
							Assets.initAssets_SelectFighterState();
							handler.getGame().selectFighterState2=new SelectFighterState2(handler);
							State.setState(handler.getGame().selectFighterState2);
							handler.getGame().setCurrentSong(3);
						}
						else if(opcion==2) { // Menu opciones
							handler.getGame().optionsState.resetState();
							State.setState(handler.getGame().optionsState);
						}
						else if(opcion==3) { //Menu Ranking
							State rankingState = new RankingState(handler);
							State.setState(rankingState);
						}
						else if(opcion==4) { // Cerrar el juego
							System.exit(0);
						}
					}
					if(handler.getKeyManager().W && avion){ // Arriba
						handler.getGame().setSoundEffect(26,false);
						if(opcion==0) {
							opcion=4;
						}
						else {
							opcion--;
						}
					}
					if(handler.getKeyManager().S && avion){ // Abajo
						handler.getGame().setSoundEffect(26,false);
						if(opcion==4) {
							opcion=0;
						}
						else {
							opcion++;
						}
					}
				}
			}
			else if(!handler.getGame().getKeyManager().primeraVez) {
				primeraVez=true;
			}
			
			if(yOffset<50) {
				yOffset++;
			}
			else if(yOffset==50) {
				fade=true;
				yOffset++;
			}
			
			if(alpha<0) {
				avion=true;
				handler.getGame().setSoundEffect(0,false);
			}
		}
		tiempoEspera++;
	}

	@Override
	public void render(Graphics g) {
		
		g.setColor(Color.BLACK);
		//g.fillRect(0, 0, handler.getGame().getWidth(), handler.getGame().getHeight());
		g.drawImage(fondo, 0, 0, 1280,720, null);
		g.drawImage(Assets.streetLogo, 185, 61-yOffset, 907, 425, null);
		
		if(tiempoEspera>limEspera) {
			
			if(yOffset>50) {
				//g.drawImage(Assets.optionsMenuState, 440, 470, 400, 220, null);
				g.drawImage(Assets.optionsMenuState, 440, 430, 400, 260, null);
				//g.setColor(Color.BLACK);
				//g.fillRect(684, 589, 80, 40);
				//g.drawImage(Assets.ranking, 454, 589, 230, 40, null);
				if(avion) {
					g.drawImage(Assets.avion, 390, posAvion[opcion], 40, 40, null);
					// Mensajes de ayuda
					g.setFont(pixelFont);
					g.setColor(Color.WHITE);
					g.drawImage(wUpSDown, 85, 690, 250, 30, null);
					//g.drawString("W-UP", 115, 710);
					//g.drawString("S-DOWN", 175, 710);
					g.drawImage(enterSelect, 925, 690, 250, 30, null);
					//g.drawString("ENTER-SELECT", 985, 710);
					
					
					/*g.setColor(Color.WHITE);
					g.drawString("--", 505+aa, 710);
					g.drawString("--", 632+aa, 710);*/
					
				}
				
				if(fade && alpha>0.0f) {
					fadeEffect(g);
					g.setColor(Color.BLACK);
					g.fillRect(440, 430, 400, 260);
				}
				else {
					fade=false;
					alpha=1.0f;
				}
			}
		}
		
		if(handler.getGame().quererSalir) {
			handler.getGame().renderMenuEsc(g, Assets.avion);
		}
	}
	
	private void fadeEffect(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    alpha-=0.01f;
	}
	
	public boolean isGameState() {
		return false;
	}

	public void resetState() {}
}
