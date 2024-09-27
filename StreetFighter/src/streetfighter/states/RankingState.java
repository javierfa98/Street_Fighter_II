package streetfighter.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import streetfighter.Handler;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;

public class RankingState extends State{
	
	// Evita el problema con el tick() mas rapido que el KeyReleased
	private boolean primeraVez=true;
	
	BufferedImage menu, pos, score, fighter, name, back, flechaAbajo, flechaArriba;
	ArrayList<String> nombres = new ArrayList<String>();
	ArrayList<Integer> scores = new ArrayList<Integer>();
	ArrayList<Integer> luchadores = new ArrayList<Integer>();
	BufferedImage fighters[];
	//String fighters[]= {"BLANKA","CHUN LI", "RYU"};
	int puntero;
	boolean mostrarFlechas=true;
	int contadorFlechas=20;
	
	private LettersNumbers letNums;
	
	public RankingState(Handler handler) {
		super(handler);
		
		letNums = new LettersNumbers();
		
		menu=ImageLoader.loadImage("/res/menus/fondo.png");
		pos=ImageLoader.loadImage("/res/textures/rankingMenu/pos.png");
		score=ImageLoader.loadImage("/res/textures/rankingMenu/score.png");
		fighter=ImageLoader.loadImage("/res/textures/rankingMenu/fighter.png");
		name=ImageLoader.loadImage("/res/textures/rankingMenu/name.png");
		back=ImageLoader.loadImage("/res/textures/rankingMenu/back.png");
		fighters = new BufferedImage[3];
		fighters[0] = ImageLoader.loadImage("/res/textures/rankingMenu/blankaRanking.png");
		fighters[1] = ImageLoader.loadImage("/res/textures/rankingMenu/chunRanking.png");
		fighters[2] = ImageLoader.loadImage("/res/textures/rankingMenu/ryuRanking.png");
		flechaAbajo = ImageLoader.loadImage("/res/textures/rankingMenu/flechaAbajo.png");
		flechaArriba = ImageLoader.loadImage("/res/textures/rankingMenu/flechaArriba.png");
		
		puntero=0;
		
		try {
			cargarScores();
		} catch (IOException e) {
			e.printStackTrace();
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
				
				if(handler.getKeyManager().B) { // Volver a menu state
					handler.getGame().setSoundEffect(25,false);
					State.setState(handler.getGame().menuState);
				}
				
				if(handler.getKeyManager().W){ // Arriba
					//Si estamos arriba del todo no se puede subir mas
					if(puntero==0) {
						handler.getGame().setSoundEffect(24,false);
					}
					else {
						puntero--;
						handler.getGame().setSoundEffect(26,false);
					}
				}
				if(handler.getKeyManager().S){ // Abajo
					//No se puede bajar mas
					if(puntero==scores.size()-10) {
						handler.getGame().setSoundEffect(24,false);
					}
					else {
						puntero++;
						handler.getGame().setSoundEffect(26,false);

					}
				}
			}
		}
		else if(!handler.getGame().getKeyManager().primeraVez) {
			primeraVez=true;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(menu,0,0,1280,720,null);
		g.setFont(handler.getGame().pixelFont); 
		
		mostrarScores(g);
		
		//g.drawString("B-BACK", 20, 690);
		g.drawImage(back, 20, 690, 140, 30, null);
		
		contadorFlechas--;
		if(contadorFlechas==0) {
			contadorFlechas=20;
			mostrarFlechas=!mostrarFlechas;
		}
		
		if(mostrarFlechas) {
			if(puntero==0) {
				g.drawImage(flechaAbajo, 30, 420, 60, 60, null);
			}
			else if(puntero==scores.size()-10) {
				g.drawImage(flechaArriba, 30, 300, 60, 60, null);
			}
			else {
				g.drawImage(flechaArriba, 30, 300, 60, 60, null);
				g.drawImage(flechaAbajo, 30, 420, 60, 60, null);
			}
		}
		
		if(handler.getGame().quererSalir) {
			handler.getGame().renderMenuEsc(g, Assets.avion);
		}
	}
	
	public void cargarScores() throws FileNotFoundException, IOException{
		String cadena;
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (handler.getGame().path+"/.configStreetFighterII/score");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			//Van a estar ordenados por orden de puntuacion (de mayor a menor)
			while((cadena = br.readLine())!=null) {
				scores.add(Integer.parseInt(cadena.split("	")[0]));
				luchadores.add(Integer.parseInt(cadena.split("	")[1]));
				nombres.add(cadena.split("	")[2]);
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
	
	
	public void mostrarScores(Graphics g) {
		//g.setColor(Color.WHITE);
		
		g.drawImage(pos, 100, 25, 130, 50, null);

		g.drawImage(score, 350, 25, 210, 50, null);
		
		g.drawImage(fighter, 670, 25, 270, 50, null);
		
		g.drawImage(name, 1050, 25, 165, 50, null);
		
		//int pos=150;
		int pos = 115;
		
		String nombre = "";
		String score = "";
		String numberPos = "";
		for(int i=puntero;i<puntero+10;i++) {

			numberPos = Integer.toString(i+1);
			int xPosNumber = 138;
			if(numberPos.length()==2) {
				xPosNumber = 115;
			}
			else {
			}
			for(int j=0; j<numberPos.length();j++) {
				g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(numberPos.charAt(j)))), 
						xPosNumber, pos, 45, 50, null);
				xPosNumber+=45;
			}
			
			score = Integer.toString(scores.get(i));
			int xPos = 350;
			for(int j=0; j<score.length();j++) {
				
				g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(score.charAt(j)))), 
						xPos, pos, 45, 50, null);
				xPos+=45;
			}
			
			dibujaFighter(g, fighters[luchadores.get(i)], luchadores.get(i), pos);
			
			nombre = nombres.get(i);
			g.drawImage(letNums.getLetter(nombre.charAt(0)), 1065, pos, 45, 50, null);
			g.drawImage(letNums.getLetter(nombre.charAt(1)), 1110, pos, 45, 50, null);
			g.drawImage(letNums.getLetter(nombre.charAt(2)), 1155, pos, 45, 50, null);
			
			pos+=55;
		}
			
	}
	
	private void dibujaFighter(Graphics g, BufferedImage image, int fighter, int pos) {
		switch(fighter) {
			case 0: g.drawImage(image, 682, pos, 250, 50, null); break; //Blanka
			case 1: g.drawImage(image, 676, pos, 260, 50, null); break; //Chun
			case 2: g.drawImage(image, 735, pos, 150, 50, null); break; //Ryu
			default:
				throw new IllegalArgumentException("No existe fighter con id" + fighter);
		}
	}
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
}