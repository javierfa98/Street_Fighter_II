package streetfighter.gfx;

import java.awt.image.BufferedImage;

public class Animation {
	
	private int speed,index;
	public long lastTime,timer;
	private BufferedImage[] frames;
	//Altura y anchura de cada frame de la animacion
	private int width[],height[];
	private boolean animacionEnCurso = false; //Para las animaciones que deben empezar y terminar
	
	//Constructor sin tamaño
	public Animation(int speed, BufferedImage[] frames) {
		this.speed=speed;
		this.frames=frames;
		//Para empezar por la primera animacion del movimiento
		index=0;
		timer=0;
		lastTime=System.currentTimeMillis();
	}
	
	//Constructor con tamaño
	public Animation(int speed, BufferedImage[] frames, int width[], int height[]) {
		this.speed=speed;
		this.frames=frames;
		this.width=width;
		this.height=height;
		//Para empezar por la primera animacion del movimiento
		index=0;
		timer=0;
		lastTime=System.currentTimeMillis();
	}
	
	public void tick() {
		//Sumar a timer el tiempo que ha pasado desde la ultima vez que se llamo a este metodo (tick())
		timer+=System.currentTimeMillis()-lastTime;
		lastTime=System.currentTimeMillis();
		
		if(timer>speed) {
			index++;
			timer=0;
			if(index>=frames.length) {
				animacionEnCurso = false;
				index=0;
			}
		}
	}
	
	//Frame actual
	public BufferedImage getCurrentFrame() {
		return frames[index];
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void resetAnimtaion() {
		//Para empezar por la primera animacion del movimiento
		index=0;
		timer=0;
		lastTime=System.currentTimeMillis();
	}
	
	public void setAnimacionEnCurso(boolean animacionEnCurso) {
		this.animacionEnCurso = animacionEnCurso;
	}
	
	public boolean getAnimacionEnCurso(){
		return this.animacionEnCurso;
	}

	public int[] getWidth() {
		return width;
	}
	
	public int getWidthActual() {
		return width[index];
	}

	public void setWidth(int[] width) {
		this.width = width;
	}

	public int[] getHeight() {
		return height;
	}
	
	public int getHeightActual() {
		return height[index];
	}

	public void setHeight(int[] height) {
		this.height = height;
	}

}
