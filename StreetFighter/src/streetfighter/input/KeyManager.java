package streetfighter.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Launcher;
import streetfighter.audio.MusicFile;
import streetfighter.utils.Utils;

//Teclas que van a ser apretadas
public class KeyManager implements KeyListener{
	
	//KEYS:
	public boolean[] keys; //Vector que contiene todas las teclas posibles
	public boolean[] keys_p1, keys_p2; //Teclas de cada jugador
	public int[] keys_p1_index, keys_p2_index; //Indices de los keyevents de cada tecla
	public int N_Keys; //Numero de teclas que tiene cada jugador
	public boolean primeraVezAtaque, primeraVezAtaque2;
	//Orden vector de Keys_p1 y Keys_p2:
	/*
	1 UP
	2 DOWN
	3 LEFT
	4 RIGHT
	5 PUNCH_M
	6 KICK_L
	7 KICK_H
	8 SPECIAL
	*/
	public boolean selectPlayer1;
	public boolean selectPlayer2;
	public boolean enter;	// Tecla enter
	public boolean B;		// Tecla B (para ir hacia atras en menus)
	public boolean W,A,S,D,ESC,P;
	public boolean UP,LEFT,DOWN,RIGHT;
	
	//Primera vez que se pulsa tecla
	public boolean primeraVez=true;
	public boolean primeraVez1=true;
	public boolean primeraVez2=true;
	
	//Ninguna tecla pulsada
	public boolean p1_stop=true;
	public boolean p2_stop=true;
	
	//backdoors
	public boolean uno,dos,tres;
	
	private String path;
	
	public KeyManager(String path) {
		this.path=path;
		keys=new boolean[256];
		primeraVezAtaque = false;
		primeraVezAtaque2 = false;
		uno=false;
		dos=false;
		tres=false;
		//Leemos el fichero de configuracion de teclas:
		
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/keys");
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
	}
	
	public void tick() {
		//Jugador1
		p1_stop = true; //Por defecto no hemos detectado ninguna tecla
		for (int i = 0; i<N_Keys; i++) {
			if (i < 4) {
				keys_p1[i] = keys[keys_p1_index[i]];
			}
			else {
				keys_p1[i] = keys[keys_p1_index[i]] && primeraVezAtaque;
			}
			if (keys_p1[i]) {
				ComboManager.addMov1(i);
				p1_stop = false;
			}
			
		}
		//Jugador2
		p2_stop = true; //Por defecto no hemos detectado ninguna tecla
		for (int i = 0; i<N_Keys; i++) {
			if (i < 4) {
				keys_p2[i] = keys[keys_p2_index[i]];
			}
			else {
				keys_p2[i] = keys[keys_p2_index[i]] && primeraVezAtaque2;
			}
			if (keys_p2[i]) {
				ComboManager.addMov2(i);
				p2_stop = false;
			}
			
		}
		
		uno=keys[KeyEvent.VK_5];
		dos=keys[KeyEvent.VK_6];
		tres=keys[KeyEvent.VK_7];
		
		selectPlayer1=keys[KeyEvent.VK_X];
		selectPlayer2=keys[KeyEvent.VK_M];

		enter=keys[KeyEvent.VK_ENTER];
		primeraVezAtaque = false;
		primeraVezAtaque2 = false;
		B=keys[KeyEvent.VK_B];
		W=keys[KeyEvent.VK_W];
		A=keys[KeyEvent.VK_A];
		S=keys[KeyEvent.VK_S];
		D=keys[KeyEvent.VK_D];
		UP=keys[KeyEvent.VK_UP];
		LEFT=keys[KeyEvent.VK_LEFT];
		DOWN=keys[KeyEvent.VK_DOWN];
		RIGHT=keys[KeyEvent.VK_RIGHT];
		ESC=keys[KeyEvent.VK_ESCAPE];
		P=keys[KeyEvent.VK_P];
	}
	
	public void actualizar() {
		//Leemos el fichero de configuracion de teclas:
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/keys");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
	        String linea;
	        String[] linea_split = null;
	        
	        //Leemos numero de teclas por jugador
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        N_Keys = Integer.parseInt(linea_split[0]);
	        
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
	}
	
	public int teclaPulsada() {
		for(int i=0; i<keys.length; i++) {
			if(keys[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	//Tecla presionada = true
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()>=0 && e.getKeyCode()<=255) {
			keys[e.getKeyCode()]=true;
			primeraVez=true;
			
			for(int i=0; i<N_Keys; i++) {
				if (keys_p1_index[i] == e.getKeyCode()) {
					primeraVezAtaque=!primeraVez1;
					primeraVez1=true;
				}
			}

			for(int i=0; i<N_Keys; i++) {
				if (keys_p2_index[i] == e.getKeyCode()) {
					primeraVezAtaque2=!primeraVez2;
					primeraVez2=true;
				}
			}
		}
	}

	@Override
	//Teclada soltada = false
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()>=0 && e.getKeyCode()<=255) {
			keys[e.getKeyCode()]=false;
			primeraVez=false;
			primeraVez1=false;
			primeraVez2=false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
