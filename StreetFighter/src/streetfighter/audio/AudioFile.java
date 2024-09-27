package streetfighter.audio;

import java.io.File;
import java.io.IOException;

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
public class AudioFile{
	
	private File soundFile;
	private AudioInputStream ais;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private volatile boolean playing;
	
	public AudioFile(String fileName) {
		soundFile=new File(fileName);
		try {
			ais= AudioSystem.getAudioInputStream(soundFile);
			format = ais.getFormat();
			DataLine.Info info=new DataLine.Info(Clip.class, format);
			clip= (Clip)AudioSystem.getLine(info);
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	//Por si se quiere ajustar el volumen
	public void play() {
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
	
	public boolean isPlaying() {
		return playing;
	}

}
