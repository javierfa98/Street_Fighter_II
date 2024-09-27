package streetfighter.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//Carga imagenes
public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		
		try {
			return ImageIO.read(ImageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			//Se añade esto porque si no se carga bien la imagen no queremos que siga el juego
			System.exit(1);
		}
		return null;
	}
}
