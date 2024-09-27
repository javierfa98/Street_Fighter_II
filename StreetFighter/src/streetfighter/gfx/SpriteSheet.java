package streetfighter.gfx;

import java.awt.image.BufferedImage;

//Dada una imagen compuesta por muchas, permite separarlas para escoger la imagen deseada
public class SpriteSheet {

	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet) {
		this.sheet=sheet;
	}
	
	//Separar una imagen de un sheet
	public BufferedImage crop(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
}
