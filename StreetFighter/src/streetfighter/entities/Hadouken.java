package streetfighter.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Hadouken{
	//Atributos
	int damage = 50;
	BufferedImage sprite;
	boolean activo, orientacion;
	boolean firstFrame;
	int x,y;
	int distancia;
	int visible;
	
	Hadouken (int x, int y, boolean activo, boolean orientacion){
		this.x = x;
		this.y = y;
		this.activo = activo;
		this.orientacion = orientacion;
		this.distancia = 0;
		this.visible = 0;
	}
	
	public void tick() {
		int velocidad = 10;
		int MAX_DIST = 1200;
		if (activo) {
			//Avanza mientras este activo
			if (distancia < MAX_DIST) {
				if (orientacion) {
					x += velocidad;
				}
				else {
					x -= velocidad;
				}
				distancia += velocidad;
			}
			else {
				activo = false;
			}
		}
	}
	
	public void render(Graphics g, double xOffset) {
		if (visible < 2 && activo) { 
			//De izquierda a derecha
			if (orientacion) {
				g.drawImage(sprite, (int) (x - xOffset), y, sprite.getWidth()*3, sprite.getHeight()*3, null);
			}
			//De derecha a izquierda
			else {
				Image image;
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-sprite.getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				image = op.filter(sprite, null);
				g.drawImage(image, (int) (x - xOffset), y, sprite.getWidth()*3, sprite.getHeight()*3, null);
			}
			visible++;
		}
		else {
			visible = 0;
		}
	}
	
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public BufferedImage getSprite() {
		return sprite;
	}
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	public boolean getOrientacion() {
		return orientacion;
	}
	public void setOrientacion(boolean orientacion) {
		this.orientacion = orientacion;
	}
	public boolean getActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.firstFrame = activo;
		this.activo = activo;
	}
	public boolean getFirstFrame() {
		return firstFrame;
	}
	public void setFirstFrame(boolean firstFrame) {
		this.firstFrame = firstFrame;
	}

	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}