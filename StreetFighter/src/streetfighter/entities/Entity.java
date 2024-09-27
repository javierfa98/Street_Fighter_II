package streetfighter.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import streetfighter.Handler;

public abstract class Entity {
	
	protected Handler handler;
	//Como private pero las clases que la extiendes tienen acceso a ella
	protected float x,y;
	//Dibujar imagenes de distintos tamaños en la pantalla
	protected int width,height;
	//Colision de objetos
	protected Rectangle bounds;
	
	public Entity(Handler handler,float x, float y, int width, int height) {
		this.handler=handler;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		
		bounds=new Rectangle(0,0,width,height);
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
