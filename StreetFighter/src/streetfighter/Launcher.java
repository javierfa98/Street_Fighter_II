package streetfighter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import streetfighter.audio.MusicPlayer;


public class Launcher {
	private static Handler handler;

	//Responsable de comenzar el juego
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String path = System.getProperty("user.dir");
		/*if(args.length==0) {
			Process proc = Runtime.getRuntime().exec("java -Xmx512M -jar "+path+"/StreetFighterII.jar 1");
			proc.info();
		}
		else {*/
			//Crear directorio de configuracion si no existe
			if(!Files.isDirectory(Paths.get(path+"/.configStreetFighterII"))) {
				new File(path+"/.configStreetFighterII").mkdirs();
			}
			
			//Copiar los ficheros de configuracion a ese directorio
			comprobarFicherosConfig(path);

			//System.out.println("file:/"+path+"/.configStreetFighterII/song");
			//System.out.println(Launcher.class.getResource("/config/song").toString());
			
			String cadena;
		    File archivo = null;
	        FileReader fr = null;
	        BufferedReader br = null;

	        try {
				//SONG - Musica sobre la que pueden sonar otros sonidos porque esta de fondo en una pantalla
				//setCurrentSong para añadir musica
				archivo = new File (path+"/.configStreetFighterII/song");
				fr = new FileReader (archivo);
				br = new BufferedReader(fr);
				
				// Lectura del fichero
				cadena = br.readLine();
				int dim=Integer.parseInt(cadena);
				String []song = new String[dim];
				int i=0;
				while((cadena = br.readLine())!=null) {
					song[i]=cadena;
					i++;
				}
				br.close();
				fr.close();
				
				//SOUND - Sonidos momentaneos para producir un efecto
				//setSoundEffect para añadir sonido
				archivo = new File (path+"/.configStreetFighterII/sound");
			    fr = new FileReader (archivo);
			    br = new BufferedReader(fr);
			    
			    cadena = br.readLine();
			    dim=Integer.parseInt(cadena);
			    String []sound= new String[dim];
			    i=0;
			    while((cadena = br.readLine())!=null) {
			    	sound[i]=cadena;
			    	i++;
			    }
			    br.close();
			    fr.close();
			    
			    //Pantalla completa
		        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		        int wPantalla = screenSize.width;
		        int hPantalla = screenSize.height;
			    
			    MusicPlayer mp=new MusicPlayer(song,sound,path);
			    Game game=new Game("Street Fighter 2", wPantalla, hPantalla, mp, path);
	 			
	 			
				//Declarar threads
				Runnable r1=game;
				Runnable r2=mp;    //Cargar todas las canciones
				
				//Pool de threads
				ExecutorService pool = Executors.newFixedThreadPool(2); 
				
				//Para que se ejecuten los 2 threads (se invoca el metodo run() de las respectivas clases)
				pool.execute(r1); 
				pool.execute(r2); 
				  
				pool.shutdown();
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
		//}
	}
	
	private static void comprobarFicherosConfig(String path) throws IOException {
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/animacionesID"))) {
			copiarFicheroConfig("/config/animacionesID", "animacionesID", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/ataques.movs"))) {
			copiarFicheroConfig("/config/ataques.movs", "ataques.movs", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/dificultad"))) {
			copiarFicheroConfig("/config/dificultad", "dificultad", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/fighters.asset"))) {
			copiarFicheroConfig("/config/fighters.asset", "fighters.asset", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/keys"))) {
			copiarFicheroConfig("/config/keys", "keys", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/score"))) {
			copiarFicheroConfig("/config/score", "score", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/song"))) {
			copiarFicheroConfig("/config/song", "song", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/sound"))) {
			copiarFicheroConfig("/config/sound", "sound", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/volumenJuego"))) {
			copiarFicheroConfig("/config/volumenJuego", "volumenJuego", path);
		}
		if(!Files.exists(Paths.get(path+"/.configStreetFighterII/resolucion"))) {
			copiarFicheroConfig("/config/resolucion", "resolucion", path);
		}
	}
	
	private static void copiarFicheroConfig(String ficheroConfig, String ficheroConfig2, String path) throws IOException {
		String cadena;
		URL url = new URL(Launcher.class.getResource(ficheroConfig).toString());
        BufferedReader b = new BufferedReader(
        new InputStreamReader(url.openStream()));
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(path+"/.configStreetFighterII/"+ficheroConfig2);
            pw = new PrintWriter(fichero);
            
            while((cadena = b.readLine())!=null) {
            	pw.println(cadena);
	            pw.flush();
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
	}
}
