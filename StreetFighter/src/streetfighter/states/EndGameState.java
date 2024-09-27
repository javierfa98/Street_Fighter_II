package streetfighter.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import streetfighter.Handler;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;

//Cuando te pasas el juego se muestra el final del fighter usado
public class EndGameState extends State{
	BufferedImage menu;
	int fighter1;
	int state=0;
	int timeState=200;
	int imagenActual=1;
	int timeImagenActual=50;
	boolean subiendo=true;
	private boolean primeraVez;
	boolean closeDoor;
	

	public EndGameState(Handler handler, int fighter1, boolean closeDoor) {
		super(handler);
		this.fighter1=fighter1;
		primeraVez=false;
		this.closeDoor=closeDoor;
	}
	
	@Override
	public void tick() {
		timeState--;
		
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
		}
		else if(!handler.getGame().getKeyManager().primeraVez) {
			primeraVez=true;
		}
		
		if(timeState==0) {
			timeState=200;
			state++;
			if((state==9 && fighter1==0) || (state==2 && fighter1==1) || (state==3 && fighter1==2)) {
				handler.getGame().setFade(true);	
			}
			
		}
		
		if(fighter1==0) {
			Assets.blankaCryAnimation.tick();
			if(timeState==100 && state<3) {
				state++;
				timeState=200;
			}
			if(state>10) {
				State ScoreState = new ScoreState(handler,fighter1,closeDoor);
				handler.getGame().setCurrentSong(0);
				State.setState(ScoreState);
			}
		}
		else if(fighter1==1) {
			if(state>3) {
				State ScoreState = new ScoreState(handler,fighter1,closeDoor);
				handler.getGame().setCurrentSong(0);
				State.setState(ScoreState);
			}
		}
		else if(fighter1==2) {
			Assets.ryuWalkAnimation.tick();
			
			if(state>4) {
				State ScoreState = new ScoreState(handler,fighter1,closeDoor);
				handler.getGame().setCurrentSong(0);
				State.setState(ScoreState);
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		if(fighter1==0) {
			switch(state) {
			case 0:
				g.drawImage(Assets.blankaEnd[0], 0, 0, 1280, 720, null);
			break;
			case 1:
				g.drawImage(Assets.blankaEnd[1], 0, 0, 1280, 720, null);
			break;
			case 2:
				g.drawImage(Assets.blankaEnd[2], 0, 0, 1280, 720, null);
			break;
			case 3:
				g.drawImage(Assets.blankaEnd[3], 0, 0, 1280, 720, null);
			break;
			case 4:
				g.drawImage(Assets.blankaEnd[4], 0, 0, 1280, 720, null);
			break;
			case 5:
				g.drawImage(Assets.blankaEnd[5], 0, 0, 1280, 720, null);
			break;
			case 6:
				g.drawImage(Assets.blankaEnd[6], 0, 0, 1280, 720, null);
			break;
			case 7:
				g.drawImage(Assets.blankaEnd[7], 0, 0, 1280, 720, null);
			break;
			case 8:
				g.drawImage(Assets.blankaEnd[8], 0, 0, 1280, 720, null);
			break;
			case 9:
				g.drawImage(Assets.blankaEnd[9], 0, 0, 1280, 720, null);
				g.drawImage(Assets.blankaCryAnimation.getCurrentFrame(), 0, 150, 1280, 357, null);
			break;
			case 10:
				g.drawImage(Assets.blankaEnd[10], 0, 0, 1280, 720, null);
				g.drawImage(Assets.blankaCryAnimation.getCurrentFrame(), 0, 150, 1280, 357, null);
			break;
			
			}
		}
		else if(fighter1==1) {
			switch(state) {
			case 0:
				g.drawImage(Assets.chunEnd[0], 0, 0, 1280, 720, null);
			break;
			case 1:
				g.drawImage(Assets.chunEnd[1], 0, 0, 1280, 720, null);
			break;
			case 2:
				g.drawImage(Assets.chunEnd[2], 0, 0, 1280, 720, null);
			break;
			case 3:
				g.drawImage(Assets.chunEnd[3], 0, 0, 1280, 720, null);
			break;
			}
		}
		
		else if(fighter1==2) {
			switch(state) {
			case 0:
				g.drawImage(Assets.ryuWrites[0], 0, 0, 1280, 720, null);
			break;
			case 1:
				g.drawImage(Assets.ryuWrites[1], 0, 0, 1280, 720, null);
			break;
			case 2:
				g.drawImage(Assets.ryuWrites[2], 0, 0, 1280, 720, null);
			break;
			case 3:
				g.drawImage(Assets.ryuWrites[3], 0, 0, 1280, 720, null);
				g.drawImage(Assets.ryuWalkAnimation.getCurrentFrame(), 0, 150, 1280, 357, null);
			break;
			case 4:
				g.drawImage(Assets.ryuWrites[4], 0, 0, 1280, 720, null);
				g.drawImage(Assets.ryuWalkAnimation.getCurrentFrame(), 0, 150, 1280, 357, null);
			break;
			}
		}
		
		if(handler.getGame().quererSalir) {
			handler.getGame().renderMenuEsc(g, Assets.avion);
		}
	}
	
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
}
