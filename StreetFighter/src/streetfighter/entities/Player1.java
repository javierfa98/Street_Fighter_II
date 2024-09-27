package streetfighter.entities;

import java.awt.Color;
import java.awt.Graphics;

import streetfighter.Handler;

public class Player1 extends Player{

	public Player1(Handler handler,float x, float y, int width, int height,int fighter,int speed, boolean orientacion) {
		super(handler,x,y,width,height,fighter,speed, orientacion, true, false);
	}
	
	public void tick_manager() {
		keys_p = handler.getKeyManager().keys_p1;
		stop = handler.getKeyManager().p1_stop;
	}
	
	public void render_stats(Graphics g) {
		mostrarPuntos(g);
		mostrarNombre(g);
	}
	
	private void mostrarPuntos(Graphics g) {
		g.setFont(handler.getGame().pixelFont); 
		g.setColor(Color.WHITE);
		//g.drawString(Integer.toString(handler.getGame().getPuntos1()), 300, 50);
		String score = Integer.toString(handler.getGame().getPuntos1());
		int xPos = 295;
		for(int j=0; j<score.length();j++) {
			g.drawImage(letNums.getNumberNormal_T(Integer.parseInt(Character.toString(score.charAt(j)))), 
					xPos, 20, 45, 40, null);
			xPos+=45;
		}
		//g.drawString("1P", 100, 50);
		g.drawImage(player1, 105, 20, 60, 40, null);
	}
	
	private void mostrarNombre(Graphics g) {
		g.setFont(handler.getGame().pixelFont); 
		g.setColor(Color.WHITE);
		if(fighter==0) {
			//g.drawString("BLANKA", 100, 140);
			g.drawImage(blankaName, 105, 110, 190, 40, null);
		}
		else if(fighter==1) {
			//g.drawString("CHUN LI", 100, 140);
			g.drawImage(chunName, 105, 110, 190, 40, null);
		}
		else if(fighter==2) {
			//g.drawString("RYU", 100, 140);
			g.drawImage(ryuName, 105, 110, 100, 40, null);
		}
		
	}
}
