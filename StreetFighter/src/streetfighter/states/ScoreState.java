package streetfighter.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Handler;
import streetfighter.Launcher;
import streetfighter.gfx.Assets;
import streetfighter.gfx.ImageLoader;
import streetfighter.gfx.LettersNumbers;

public class ScoreState extends State{
	BufferedImage menu, pos, score, fighter, name, back,
				  pressEnterContinue, enterInitialsImage;
	ArrayList<String> nombres = new ArrayList<String>();
	ArrayList<Integer> scores = new ArrayList<Integer>();
	ArrayList<Integer> luchadores = new ArrayList<Integer>();
	
	boolean primeraVez=true;
	boolean mostrar=true;			//Para que parpadee
	int timeMostrar=20;				//Para que parpadee
	int timeMostrarInicial=timeMostrar;				//Para que parpadee
	char nombre[]= {'A','-','-'};	//Nombre con el que el player guarda el score
	int indicePlayer;				//Posicion que ocupa en las listas el player
	boolean letra1=true;			//Estados 	(letra que se esta modificando)
	boolean letra2=false;			//Estados	(letra que se esta modificando)
	boolean letra3=false;
	boolean teclaPulsada=false;		//Para que al darle un toque a las felchas no avance muchas letras, solo queremos que avance una
	int timeTeclaPulsada=10;		//Para que al darle un toque a las felchas no avance muchas letras, solo queremos que avance una
	int fighter1;
	//String fighters[]= {"BLANKA","CHUN LI", "RYU"};
	BufferedImage fighters[];
	int timeEnterInitials=50;
	boolean enterInitials=true;
	//CLOSE DOOR
	boolean closeDoor;
	int listaPuntos[]= {10000,30000,60000,80000};
	//Evitar rebotes
	int time=2;
	private LettersNumbers letNums;
	
	boolean stopParpadeo=false;
	boolean noRebotes=false;
		
	public ScoreState(Handler handler,int fighter1,boolean closeDoor) {
		super(handler);
		
		letNums = new LettersNumbers();
		this.fighter1=fighter1;
		menu=ImageLoader.loadImage("/res/menus/fondo.png");
		pos=ImageLoader.loadImage("/res/textures/rankingMenu/pos.png");
		score=ImageLoader.loadImage("/res/textures/rankingMenu/score.png");
		fighter=ImageLoader.loadImage("/res/textures/rankingMenu/fighter.png");
		name=ImageLoader.loadImage("/res/textures/rankingMenu/name.png");
		pressEnterContinue=ImageLoader.loadImage("/res/textures/rankingMenu/pressEnterToContinue.png");
		enterInitialsImage=ImageLoader.loadImage("/res/textures/rankingMenu/enterInitials.png");
		fighters = new BufferedImage[3];
		fighters[0] = ImageLoader.loadImage("/res/textures/rankingMenu/blankaRanking.png");
		fighters[1] = ImageLoader.loadImage("/res/textures/rankingMenu/chunRanking.png");
		fighters[2] = ImageLoader.loadImage("/res/textures/rankingMenu/ryuRanking.png");
		
		this.closeDoor=closeDoor;
	}
	
	@Override
	public void tick() {
		
		//La primera vez que se entra en el estado ScoreState, se leen las puntuaciones del fichero
		//para no tener que leerlo mas para mostrarlas
		if(primeraVez) {
			try {
				cargarScores();
				primeraVez=false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//A�ado al player al ranking con el nombre sin elegir 
			addPlayer();
		}
		
		timeMostrar--;
		if(timeMostrar==0) {
			mostrar=!mostrar;
			timeMostrar=timeMostrarInicial;
		}
		
		timeEnterInitials--;
		if(timeEnterInitials==0) {
			enterInitials=!enterInitials;
			timeEnterInitials=50;
		}
		
		if(!teclaPulsada) {
			
			if(handler.getKeyManager().enter || noRebotes){
				teclaPulsada=true;
				//Paso a la segunda letra
				if(letra1 && !letra2) {
					letra1=false;
					letra2=true;
					nombre[1]='A';
					nombres.remove(indicePlayer);
					nombres.add(indicePlayer,Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
				}
				//Paso a la tercera letra
				else if(!letra1 && letra2) {
					letra2=false;
					letra3=true;
					nombre[2]='A';
					nombres.remove(indicePlayer);
					nombres.add(indicePlayer,Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
				}
				else if(!letra1 && !letra2 && letra3) {
					letra3=false;
					handler.getGame().setSoundEffect(25,false);
					stopParpadeo=true;
					
				}
				else if(!letra1 && !letra2 && !letra3) {
					
			        //Reiniciamos los puntos del modo historia porque ya se han guardado los puntos
					
					if(time>0) {
						time--;
						noRebotes=true;
					}
					else {
						
						time=2;
						
						//Se borra el fichero con la info anterior
						File score=new File(handler.getGame().path+"/.configStreetFighterII/score");
						score.delete();
						//Se vuelve a crear con la info actualizada
						FileWriter fichero = null;
				        PrintWriter pw = null;
				        try
				        {
				            fichero = new FileWriter(handler.getGame().path+"/.configStreetFighterII/score");
				            pw = new PrintWriter(fichero);

				            for (int i = 0; i < nombres.size(); i++) {
				            	//SCORE FIGHTER	NAME
				                pw.println(scores.get(i)+"	"+luchadores.get(i)+"	"+nombres.get(i));
				            }
				            
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
						handler.getGame().setPuntos1(0);
						handler.getGame().setPuntos2(0);
						handler.getGame().setSoundEffect(25,true);
						Assets.initAssets_MenuState();
						State.setState(handler.getGame().menuState);
					}
					
				}
			}
			
			//Para que no suene el subir y bajar si ya no se puede subir y bajar 
			if(letra1 || letra2 || letra3) {
				if(handler.getKeyManager().W) {
					handler.getGame().setSoundEffect(26,true);
					teclaPulsada=true;
					int letra;
					if(letra1) {
						letra=(int)nombre[0]+1;
						//Para que la siguiente de la Z vuelva a ser la A
						if(letra>90) {
							letra=65;
						}
						nombre[0]=(char)letra;
					}
					else if(letra2) {
						letra=(int)nombre[1]+1;
						//Para que la siguiente de la Z vuelva a ser la A
						if(letra>90) {
							letra=65;
						}
						nombre[1]=(char)letra;
					}
					else if(letra3){
						letra=(int)nombre[2]+1;
						//Para que la siguiente de la Z vuelva a ser la A
						if(letra>90) {
							letra=65;
						}
						nombre[2]=(char)letra;
					}
					
					nombres.remove(indicePlayer);
					nombres.add(indicePlayer,Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
				}
				
				if(handler.getKeyManager().S) {
					handler.getGame().setSoundEffect(26,true);
					teclaPulsada=true;
					int letra;
					if(letra1) {
						letra=(int)nombre[0]-1;
						//Para que la anterior letra de la A vuelva a ser la Z
						if(letra<65) {
							letra=90;
						}
						nombre[0]=(char)letra;
					}
					else if(letra2) {
						letra=(int)nombre[1]-1;
						//Para que la anterior letra de la A vuelva a ser la Z
						if(letra<65) {
							letra=90;
						}
						nombre[1]=(char)letra;
					}
					else if(letra3){
						letra=(int)nombre[2]-1;
						//Para que la anterior letra de la A vuelva a ser la Z
						if(letra<65) {
							letra=90;
						}
						nombre[2]=(char)letra;
					}
					
					nombres.remove(indicePlayer);
					nombres.add(indicePlayer,Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
				}
			}
			
			
		}
		else {
			timeTeclaPulsada--;
			if(timeTeclaPulsada==0) {
				teclaPulsada=false;
				timeTeclaPulsada=10;
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(menu,0,0,1280,720,null);
		g.setFont(handler.getGame().pixelFont); 
		
		mostrarScores(g);
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
	
	public void addPlayer() {
		int scorePlayer;
		if(!closeDoor) {
			scorePlayer=handler.getGame().getPuntos1();
		} 
		else {
			int random=(int)Math.floor(Math.random()*3);
			scorePlayer=listaPuntos[random];
		}
		int i=0; 
		boolean encontrado=false;
		while(i<scores.size() && !encontrado) {
			//Si el score es mayor al del playe i, se coloca en la posicion i
			if(scores.get(i)<scorePlayer){
				encontrado=true;
				scores.add(i, scorePlayer);
				nombres.add(i,Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
				luchadores.add(i,fighter1);
				indicePlayer=i;
			}
			i++;
		}
		//Si es la peor puntuacion, se a�ade la ultima puntuacion de todas
		if(!encontrado) {
			scores.add(scorePlayer);
			nombres.add(Character.toString(nombre[0])+Character.toString(nombre[1])+Character.toString(nombre[2]));
			luchadores.add(i,fighter1);
			
			indicePlayer=i;
		}
	}
	
	public void mostrarScores(Graphics g) {
		
		g.setColor(Color.WHITE);
		
		if(enterInitials) {
			g.drawImage(enterInitialsImage, 340, 80, 600, 50, null);
		}
		else{
			g.drawImage(pressEnterContinue, 290, 80, 700, 50, null);
		}
		
		
		g.drawImage(pos, 100-15, 250, 130, 50, null);

		g.drawImage(score, 350-15, 250, 210, 50, null);
		
		g.drawImage(fighter, 670-15, 250, 270, 50, null);
		
		g.drawImage(name, 1050-15, 250, 165, 50, null);
		
		int pos=330;
		
		//Si hay suficientes por arriba y por abajo
		if(indicePlayer>1 && (nombres.size()-indicePlayer>2)){
			String nombre = "";
			String scoreS = "";
			String numberPos = "";
			for(int i=0;i<nombres.size();i++) {
				
				if(Math.abs(i-indicePlayer)<=2) {
					if(i==indicePlayer && !mostrar &&!stopParpadeo){
						g.setColor(Color.YELLOW);
					}
					else {
						numberPos = Integer.toString(i+1);
						int xPosNumber = 123;
						if(numberPos.length()==2) {
							xPosNumber = 100;
						}
						else {
						}
						for(int j=0; j<numberPos.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(numberPos.charAt(j)))), 
									xPosNumber, pos, 45, 50, null);
							xPosNumber+=45;
						}
						
						scoreS = Integer.toString(scores.get(i));
						int xPos = 350;
						for(int j=0; j<scoreS.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(scoreS.charAt(j)))), 
									xPos, pos, 45, 50, null);
							xPos+=45;
						}
						
						dibujaFighter(g, fighters[luchadores.get(i)], luchadores.get(i), pos);
						
						//g.drawString(nombres.get(i), 1000, pos);
						nombre = nombres.get(i);
						dibujaNombre(g, nombre, pos);
					}
					pos+=50;
				}
				
			}
		}
		//Si el player ha quedado el segundo o el primero 
		else if(indicePlayer==0 || indicePlayer==1) {
			String nombre = "";
			String scoreS = "";
			String numberPos = "";
			for(int i=0;i<nombres.size();i++) {
				if(i<5) {
					if(i==indicePlayer && !mostrar){
						g.setColor(Color.YELLOW);
					}
					else {
						numberPos = Integer.toString(i+1);
						int xPosNumber = 123;
						if(numberPos.length()==2) {
							xPosNumber = 100;
						}
						else {
						}
						for(int j=0; j<numberPos.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(numberPos.charAt(j)))), 
									xPosNumber, pos, 45, 50, null);
							xPosNumber+=45;
						}
						
						scoreS = Integer.toString(scores.get(i));
						int xPos = 350;
						for(int j=0; j<scoreS.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(scoreS.charAt(j)))), 
									xPos, pos, 45, 50, null);
							xPos+=45;
						}
						
						dibujaFighter(g, fighters[luchadores.get(i)], luchadores.get(i), pos);
						
						//g.drawString(nombres.get(i), 1000, pos);
						nombre = nombres.get(i);
						dibujaNombre(g, nombre, pos);
					}
					pos+=50;
				}
				
			}
		}
		//Si el player ha quedado ultimo o penultimo
		else if(nombres.size()==(indicePlayer+1) || nombres.size()==(indicePlayer+2)) {
			String nombre = "";
			String scoreS = "";
			String numberPos = "";
			for(int i=0;i<nombres.size();i++) {
				if(i>nombres.size()-6) {
					if(i==indicePlayer && !mostrar){
						g.setColor(Color.YELLOW);
					}
					else {
						numberPos = Integer.toString(i+1);
						int xPosNumber = 123;
						if(numberPos.length()==2) {
							xPosNumber = 100;
						}
						else {
						}
						for(int j=0; j<numberPos.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(numberPos.charAt(j)))), 
									xPosNumber, pos, 45, 50, null);
							xPosNumber+=45;
						}
						
						scoreS = Integer.toString(scores.get(i));
						int xPos = 350;
						for(int j=0; j<scoreS.length();j++) {
							g.drawImage(letNums.getNumberNormal(Integer.parseInt(Character.toString(scoreS.charAt(j)))), 
									xPos, pos, 45, 50, null);
							xPos+=45;
						}
						
						dibujaFighter(g, fighters[luchadores.get(i)], luchadores.get(i), pos);
						
						//g.drawString(nombres.get(i), 1000, pos);
						nombre = nombres.get(i);
						dibujaNombre(g, nombre, pos);
					}
					pos+=50;
				}
				
			}
		}
	}
	
	private void dibujaFighter(Graphics g, BufferedImage image, int fighter, int pos) {
		switch(fighter) {
			case 0: g.drawImage(image, 682-15, pos, 250, 50, null); break; //Blanka
			case 1: g.drawImage(image, 676-15, pos, 260, 50, null); break; //Chun
			case 2: g.drawImage(image, 735-15, pos, 150, 50, null); break; //Ryu
			default:
				throw new IllegalArgumentException("No existe fighter con id" + fighter);
		}
	}
	
	private void dibujaNombre(Graphics g, String nombre, int pos) {
		if(nombre.charAt(0) == '-') {
			g.drawImage(letNums.getLetter(nombre.charAt(0)), 1065-15, pos+15, 45, 20, null);
		}
		else {
			g.drawImage(letNums.getLetter(nombre.charAt(0)), 1065-15, pos, 45, 50, null);
		}
		if(nombre.charAt(1) == '-') {
			g.drawImage(letNums.getLetter(nombre.charAt(1)), 1110-15, pos+15, 45, 20, null);
		}
		else {
			g.drawImage(letNums.getLetter(nombre.charAt(1)), 1110-15, pos, 45, 50, null);
		}
		if(nombre.charAt(2) == '-') {
			g.drawImage(letNums.getLetter(nombre.charAt(2)), 1155-15, pos+15, 45, 20, null);
		}
		else {
			g.drawImage(letNums.getLetter(nombre.charAt(2)), 1155-15, pos, 45, 50, null);
		}
	}
	
	public boolean isGameState() {
		return false;
	}
	
	public void resetState() {}
}