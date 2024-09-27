package streetfighter.states;

import java.awt.Graphics;

import streetfighter.Handler;

//Clase que contiene todo lo que es comun a todos los estados que se creen
//Cualquier clase que haga extends State debe tener estos 2 metodos
public abstract class State {

	//De que estado queremos tick() y render() en nuestro juego 
	//Al ser static se guarda el estado en el que se esta
	private static State currentState=null;
	
	public static void setState(State state) {
		currentState=state;
	}
	
	public static State getState() {
		return currentState;
	}
	
	//CLASS
	
	protected Handler handler;
	
	public State(Handler handler) {
		this.handler=handler;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	//Para saber si el estado actual es el GameState
	public abstract boolean isGameState();
	//Para moverse entre pantallas
	public abstract void resetState();
		
	
}
