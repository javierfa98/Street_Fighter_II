package streetfighter.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//Ventana donde se va a ver el juego
public class Display {

	//Creamos la escena en canvas y luego la copiamos en frame para que se vea
	private JFrame frame; 	//Es la pantalla y necesita (titulo,anchura,altura)
	private Canvas canvas;	//Añadir gráficos a la pantalla(frame)
	
	//Lo que necesita frame para crear la pantalla
	private String title;
	private int width,height; 
	
	public Display(String title, int width, int height, String path, boolean undecorated) {
		this.title=title;
		this.width=width;
		this.height=height;
		
		createDisplay(undecorated);
	}
	
	private void createDisplay(boolean undecorated) {
		
		frame=new JFrame(title);
		frame.setSize(width,height);
		//Para que cuando demos en la cruz de cerrar se cierre bien el juego
		//y no sigo corriendo en segundo plano o algo así
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Impide al jugador cambiar el tamaño de la pantalla (Opcional)
		frame.setResizable(false);
		//Al abrir el juego la pantalla sale en el centro (Opcional)
		frame.setLocationRelativeTo(null);
		//PANTALLA COMPLETA
		frame.setUndecorated(undecorated); //TRUE EN PANTALLA COMPLETA, FALSE SI NO
		//Por defecto no se ve la pantalla
		frame.setVisible(true);
		
		canvas=new Canvas();
		//Mismo tamaño que del frame
		canvas.setPreferredSize(new Dimension(width,height));
		//Aseguran que no se cambie el tamaño de la pantalla
		canvas.setMaximumSize(new Dimension(width,height));
		canvas.setMinimumSize(new Dimension(width,height));
		//Para que vaya Keyboard Input
		canvas.setFocusable(false);
		
		//Añadir el canvas a frame
		frame.add(canvas);
		//A lo mejor no ves todo el contenido del canvas si no ejecutas esto
		frame.pack();
		
	}
	
	//Getter
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setUndecorated(boolean undecorated) {
		frame.setUndecorated(undecorated);
	}
	
	public void closeJFrame() {
		frame.dispose();
	}
	
	
}
