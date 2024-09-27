package streetfighter.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Launcher;
import streetfighter.utils.Utils;

//Almacena todos los archivos de audio del juego y los ejecuta en un thread paralelo al Game loop
public class MusicPlayer implements Runnable{
	
	private boolean musicStarts=false;
	private ArrayList<MusicFile> musicFiles;
	private ArrayList<String> audioFiles;
	private MusicFile audioFilePuntos;
	private int currentSongIndex;
	private int soundEffect;		//que sonido suena
	private int songVolume = -20;
	private int audioVolume = 0;
	private int songVolumeAnteriorMute=-20;	//Para cuando se quite el mute, volver al volumen anterior
	private int audioVolumeAnteriorMute=-0;
	private boolean sound=false;	//suena algun sonido?
	private boolean puntos=false;	//suena sonido puntos?
	private boolean gameplay=false;	//suena algun sonido en gameplay?
	private boolean mute=false;	//Para mutear el juego o cambiar sonido
	private boolean cambiarVolumen=false;	//Para mutear el juego o cambiar sonido
	private boolean running;
	
	private MusicFile song=null;
	private MusicFile audio=null;
	private MusicFile audioGameplays[];
	private int indexAudioGameplays;
	
	private int valoresMusica[] = {-80,-60,-50,-40,-30,-20,-10,0,5};
	private int valoresEfectos[] = {-80,-40,-20,-10,0,2,4,5,6};
	
	private String path;
	
	private boolean cambioRonda;
	
	//Cargar todos los audios
	public MusicPlayer(String[] song, String[] sound, String path) {
		this.path=path;
		this.musicFiles=new ArrayList<MusicFile>();
		for(String m:song) {
			musicFiles.add(new MusicFile(getClass().getResource("/res/music/song/"+m+".wav")));
		}
		this.audioFiles=new ArrayList<String>();
		for(String a:sound) {
			audioFiles.add("/res/music/sound/"+a+".wav");
		}
		audioFilePuntos = new MusicFile(getClass().getResource("/res/music/sound/puntosSound.wav"));
		
		audioGameplays = new MusicFile[256];
		for(MusicFile m:audioGameplays) {
			m=null;
		}
		indexAudioGameplays=0;
		
		cambioRonda=false;
		
		
	
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
	        songVolume = valoresMusica[Integer.parseInt(linea_split[0])];
	        songVolumeAnteriorMute = songVolume;
	        
	        //Leemos volumen efectos
	        linea = br.readLine();
	        linea_split = linea.split(" ");
	        audioVolume = valoresEfectos[Integer.parseInt(linea_split[0])];
	        audioVolumeAnteriorMute = audioVolume;
	        
	        setMute(mute);
	        
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
	
	public void setCurrentSongIndex(int currentSongIndex) {
		this.currentSongIndex=currentSongIndex;
		
	}
	
	public void setSoundEffect(int sound, boolean gameplay) {
		this.soundEffect=sound;
		this.sound=true;
		this.gameplay=gameplay;
	}
	
	//Este solo se llama cuando se mete el sonido de los puntos !!!
	public void setSoundEffect(int sound) {
		this.soundEffect=sound;
		this.puntos=true;
	}
	
	public int getCurrentSongIndex() {
		return currentSongIndex;
	}
	
	public void setMusicStarts(boolean valor) {
		this.musicStarts=valor;
	}
	
	
	public boolean isCambioRonda() {
		return cambioRonda;
	}

	public void setCambioRonda(boolean cambioRonda) {
		this.cambioRonda = cambioRonda;
	}

	public void setMute(boolean muteValue) {
		if(muteValue) { // Si hay que mutear
			songVolumeAnteriorMute=songVolume;
			audioVolumeAnteriorMute=audioVolume;
			songVolume=-80;
			audioVolume=-80;
			mute=true;
		}
		else { // Si se devuelve sonido
			songVolume=songVolumeAnteriorMute;
			audioVolume=audioVolumeAnteriorMute;
			mute=false;
		}
		cambiarVolumen=true;
	}
	
	public void setMusicVolume(int value) {
		if(mute) {
			songVolumeAnteriorMute=value;
		}
		else {
			songVolume=value;
			songVolumeAnteriorMute=value;
			cambiarVolumen=true;
		}
	}
	
	public void setEfectsVolume(int value) {
		if(mute) {
			audioVolumeAnteriorMute=value;
		}
		else {
			audioVolume=value;
			audioVolumeAnteriorMute=value;
			cambiarVolumen=true;
		}
			
	}
	
	public void stopCurrentSong() {
		song.stop();
	}
	
	public boolean currentSoundFinished() {
		return !audio.isPlaying();
	}
	
	@Override
	public void run() {
		while(!musicStarts) {
			System.out.print("");
		}
		int currentSongIndexAnt=0;
		running=true;
		song = musicFiles.get(currentSongIndex);
		song.play(songVolume);
		audio = new MusicFile(getClass().getResource(audioFiles.get(soundEffect)));
		
		
		while(running) {
			
			if(cambiarVolumen) {
				if(song.isPlaying()) {
					song.changeVolume(songVolume);
				}
				if(audio.isPlaying()) {
					audio.changeVolume(audioVolume);
				}
				cambiarVolumen=false;
			}
			
			//Si el indice de cancion ha cambiado, es que se quiera otra cancion
			if(currentSongIndex!=currentSongIndexAnt || cambioRonda) {
				currentSongIndexAnt=currentSongIndex;
				song.stop();
				song = musicFiles.get(currentSongIndex);
				song.play(songVolume);
				cambioRonda=false;
			}
			//Si sound a true es que hay que ejecutar un efecto de sonido
			if(sound && gameplay) {
				/*if(audio.isPlaying()) {
					audio.stop(); // Para que el audio actual empiece y termine el que suena
				}
				audio = audioFiles.get(soundEffect);
				audio.play(audioVolume);	
				this.sound=false;*/
				//System.out.println(indexAudioGameplays);
				audioGameplays[indexAudioGameplays]=new MusicFile(getClass().getResource(audioFiles.get(soundEffect)));
				audioGameplays[indexAudioGameplays].play(audioVolume);
				this.sound=false;
				indexAudioGameplays++;
				if(indexAudioGameplays==256) {
					indexAudioGameplays=0;
				}
			}
			else if(sound && !gameplay) {
				if(audio.isPlaying()) {
					audio.stop(); // Para que el audio actual empiece y termine el que suena
				}
				audio = new MusicFile(getClass().getResource(audioFiles.get(soundEffect)));
				audio.play(audioVolume);	
				this.sound=false;
			}
			
			//Si puntos a true es que hay que ejecutar el sonido de los puntos
			if(puntos) {
				if(soundEffect==27) {
					audioFilePuntos.play(audioVolume);	
				}
				else {
					audio = new MusicFile(getClass().getResource(audioFiles.get(soundEffect)));
					audio.play(audioVolume);	
				}
				this.puntos=false;
			}
			
			//Se reinicia el efecto de sonido cuando termina
			if(audio.getClip().getMicrosecondLength() == audio.getClip().getMicrosecondPosition()) {
				audio.stop();
				audio.setPlaying(false);
			}
			for(int i=0; i<audioGameplays.length;i++) {
				if(audioGameplays[i] != null && audioGameplays[i].getClip().getMicrosecondLength() == audioGameplays[i].getClip().getMicrosecondPosition()) {
					audioGameplays[i].stop();
					audioGameplays[i].setPlaying(false);
				}
			}
			if(audioFilePuntos.getClip().getMicrosecondLength() == audioFilePuntos.getClip().getMicrosecondPosition()) {
				audioFilePuntos.stop();
				audioFilePuntos.setPlaying(false);
			}
			
			// La musica no puede terminar nunca, que se reinicie y suene de nuevo
			if(song.getClip().getMicrosecondLength() == song.getClip().getMicrosecondPosition()) {
				song.stop();
				song.play(songVolume);
			}
		}
	}

}
