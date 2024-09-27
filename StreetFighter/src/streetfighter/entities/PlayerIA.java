package streetfighter.entities;

import java.awt.Color;
import java.awt.Graphics;

import streetfighter.Handler;

public class PlayerIA extends Player{
	
	int player;	//Para saber si la CPU actua como player1 o player2

	public PlayerIA(Handler handler,float x, float y, int width, int height,int fighter,int speed, boolean orientacion, int player) {
		super(handler,x,y,width,height,fighter,speed, orientacion, false, true);
		keys_p=new boolean[8];
		this.player=player;
	}
	
	
	public void tick_manager() {
		if(player==1) {
			keys_p[0] = handler.getGame().getBrain1().up2;
			keys_p[1]  = handler.getGame().getBrain1().down2;
			keys_p[2]  = handler.getGame().getBrain1().left2;
			keys_p[3]  = handler.getGame().getBrain1().right2;
			keys_p[4]  = handler.getGame().getBrain1().punch2;
			keys_p[5]  = handler.getGame().getBrain1().kickl2;
			keys_p[6]  = handler.getGame().getBrain1().kickH2;
			keys_p[7]  = handler.getGame().getBrain1().special2;
			stop= handler.getGame().getBrain1().p2_stop;
		}
		else {
			keys_p[0] = handler.getGame().getBrain2().up2;
			keys_p[1]  = handler.getGame().getBrain2().down2;
			keys_p[2]  = handler.getGame().getBrain2().left2;
			keys_p[3]  = handler.getGame().getBrain2().right2;
			keys_p[4]  = handler.getGame().getBrain2().punch2;
			keys_p[5]  = handler.getGame().getBrain2().kickl2;
			keys_p[6]  = handler.getGame().getBrain2().kickH2;
			keys_p[7]  = handler.getGame().getBrain2().special2;
			stop= handler.getGame().getBrain2().p2_stop;
		}
		
		
	}
	
	public void render_stats(Graphics g) {
		mostrarNombre(g);
	}
	
	private void mostrarNombre(Graphics g) {
		g.setFont(handler.getGame().pixelFont); 
		g.setColor(Color.WHITE);
		if(fighter==0) {
			if(player==1) {
				//g.drawString("BLANKA", 100, 140);
				g.drawImage(blankaName, 105, 110, 190, 40, null);
			}
			else {
				//g.drawString("BLANKA", 1020, 140);
				g.drawImage(blankaName, 990, 110, 190, 40, null);
			}
		}
		else if(fighter==1) {
			if(player==1) {
				//g.drawString("CHUN LI", 100, 140);
				g.drawImage(chunName, 105, 110, 190, 40, null);
			}
			else {
				//g.drawString("CHUN LI", 1030, 140);
				g.drawImage(chunName, 990, 110, 190, 40, null);
			}
		}
		else if(fighter==2) {
			if(player==1) {
				//g.drawString("RYU", 100, 140);
				g.drawImage(ryuName, 105, 110, 100, 40, null);
			}
			else {
				//g.drawString("RYU", 1100, 140);
				g.drawImage(ryuName, 1080, 110, 100, 40, null);
			}
			
		}
	}
}