package streetfighter.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Launcher;
import streetfighter.audio.MusicFile;
import streetfighter.entities.Movs;
import streetfighter.utils.Utils;

//Clase para controlar los combos de los jugadores
public class ComboManager{
	//Ultimas tres teclas pulsadas por el jugador 1 y 2
	static long ini_combo1;
	static long ini_combo2;
	static ArrayList<Integer> historialTeclas1 = new ArrayList<Integer>();
	static ArrayList<Integer> historialTeclas2 = new ArrayList<Integer>();
	
	//Métodos
	
	//Añade tecla al historial
	public static void addMov1(int mov) {
		if (mov == 1) {
			ini_combo1 = System.currentTimeMillis();
		}
		//Evitar rebotes
		if (historialTeclas1.size() > 0) {
			if (mov != historialTeclas1.get(historialTeclas1.size()-1)) {
				if (historialTeclas1.size() == 3) {
					historialTeclas1.remove(0);
				}
				historialTeclas1.add(mov);
			}
		}
		else {
			historialTeclas1.add(mov);
		}
	}
	
	//Añade tecla al historial
	public static void addMov2(int mov) {
		if (mov == 1) {
			ini_combo2 = System.currentTimeMillis();
		}
		//Evitar rebotes
		if (historialTeclas2.size() > 0) {
			if (mov != historialTeclas2.get(historialTeclas2.size()-1)) {
				if (historialTeclas2.size() == 3) {
					historialTeclas2.remove(0);
				}
				historialTeclas2.add(mov);
			}
		}
		else {
			historialTeclas2.add(mov);
		}
	}
	
	//Comprueba si se ha efectuado el combo de ataque especial
	public static boolean especial(boolean p1) {
		if (p1) {
			if (historialTeclas1.size() == 3) {
				if (historialTeclas1.get(0) == 1 &&
					(historialTeclas1.get(1) == 2 || historialTeclas1.get(1) == 3 ) &&
					 historialTeclas1.get(2) == 4 &&
					 System.currentTimeMillis() - ini_combo1 < 1000) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			if (historialTeclas2.size() == 3) {
				if (historialTeclas2.get(0) == 1 &&
						(historialTeclas2.get(1) == 2 || historialTeclas2.get(1) == 3 ) &&
						 historialTeclas2.get(2) == 4 &&
						 System.currentTimeMillis() - ini_combo2 < 1000) {
						return true;
					}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	public ArrayList<Integer> getHistorialTeclas1() {
		return historialTeclas1;
	}
	
	public void setHistorialTeclas1(ArrayList<Integer> historialTeclas1) {
		this.historialTeclas1 = historialTeclas1;
	}
	
	public ArrayList<Integer> getHistorialTeclas2() {
		return historialTeclas2;
	}
	
	public void setHistorialTeclas2(ArrayList<Integer> historialTeclas2) {
		this.historialTeclas2 = historialTeclas2;
	}
	
}
