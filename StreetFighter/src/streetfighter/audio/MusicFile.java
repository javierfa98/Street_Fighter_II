package streetfighter.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;

//Representa a cada uno de los archivos de audio
public class MusicFile{
	
	private AudioInputStream ais;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private boolean playing;
	private int index;
	
	public MusicFile(URL fileName) {
		try {
			ais= AudioSystem.getAudioInputStream(fileName);
			format = ais.getFormat();
			DataLine.Info info=new DataLine.Info(Clip.class, format);
			clip= (Clip)AudioSystem.getLine(info);
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		index=0;
		
	}
	
	//Por si se quiere ajustar el volumen (para la musica)
	public void play(int volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
		clip.start();
		playing=true;
	}
	
	//Para sound effects
	public void play2() {
		clip.start();
		playing=true;
	}
	
	public void stop() {
		//parar la cancion
		clip.stop();
		clip.flush();
		//que comience desde el principio cuando se vuelva a reproducir
		clip.setFramePosition(0);
	}
	
	// Para cambiar el volumen
	public void changeVolume(int volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
		//clip.start();
		//playing=true;
	}
		
	public boolean isPlaying() {
		return playing;
	}
	
	public void setPlaying(boolean playing) {
		this.playing=playing;
	}
	
	public Clip getClip() {
		return clip;
	}
	
	public void setIndex(int index) {
		this.index=index;
	}
	
	public int getIndex() {
		return index;
	}

}
