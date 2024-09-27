package streetfighter.gfx;

import java.awt.image.BufferedImage;

public class LettersNumbers {
	public LettersNumbers(){
		Assets.initAssets_LettersNumbers();
	}
	
	public BufferedImage getNumberRomano(int number) {
		BufferedImage res = null;
		
		switch(number) {
		case 1: res = Assets.uno; break;
		case 2: res = Assets.dos; break;
		case 3: res = Assets.tres; break;
		case 4: res = Assets.cuatro; break;
		case 5: res = Assets.cinco; break;
		case 6: res = Assets.seis; break;
		case 7: res = Assets.siete; break;
		case 8: res = Assets.ocho; break;
		case 9: res = Assets.nueve; break;
		case 10: res = Assets.diez; break;
		default:
			throw new IllegalArgumentException("No existe imagen para el numero romano" + number);
		}
		
		return res;
	}
	
	public BufferedImage getNumberNormal(int number) {
		BufferedImage res = null;
		
		switch(number) {
		case 0: res = Assets.number_Cero; break;
		case 1: res = Assets.number_Uno; break;
		case 2: res = Assets.number_Dos; break;
		case 3: res = Assets.number_Tres; break;
		case 4: res = Assets.number_Cuatro; break;
		case 5: res = Assets.number_Cinco; break;
		case 6: res = Assets.number_Seis; break;
		case 7: res = Assets.number_Siete; break;
		case 8: res = Assets.number_Ocho; break;
		case 9: res = Assets.number_Nueve; break;
		default:
			throw new IllegalArgumentException("No existe imagen para el numero normal" + number);
		}
		
		return res;
	}
	
	public BufferedImage getNumberNormal_T(int number) {
		BufferedImage res = null;
		
		switch(number) {
		case 0: res = Assets.number_Cero_T; break;
		case 1: res = Assets.number_Uno_T; break;
		case 2: res = Assets.number_Dos_T; break;
		case 3: res = Assets.number_Tres_T; break;
		case 4: res = Assets.number_Cuatro_T; break;
		case 5: res = Assets.number_Cinco_T; break;
		case 6: res = Assets.number_Seis_T; break;
		case 7: res = Assets.number_Siete_T; break;
		case 8: res = Assets.number_Ocho_T; break;
		case 9: res = Assets.number_Nueve_T; break;
		default:
			throw new IllegalArgumentException("No existe imagen para el numero normal" + number);
		}
		
		return res;
	}
	
	public BufferedImage getLetter(char letter) {
		BufferedImage res = null;
		switch(letter) {
		case '-': res = Assets.letra_Guion; break;
		case 'A': res = Assets.letra_A; break;
		case 'B': res = Assets.letra_B; break;
		case 'C': res = Assets.letra_C; break;
		case 'D': res = Assets.letra_D; break;
		case 'E': res = Assets.letra_E; break;
		case 'F': res = Assets.letra_F; break;
		case 'G': res = Assets.letra_G; break;
		case 'H': res = Assets.letra_H; break;
		case 'I': res = Assets.letra_I; break;
		case 'J': res = Assets.letra_J; break;
		case 'K': res = Assets.letra_K; break;
		case 'L': res = Assets.letra_L; break;
		case 'M': res = Assets.letra_M; break;
		case 'N': res = Assets.letra_N; break;
		case 'O': res = Assets.letra_O; break;
		case 'P': res = Assets.letra_P; break;
		case 'Q': res = Assets.letra_Q; break;
		case 'R': res = Assets.letra_R; break;
		case 'S': res = Assets.letra_S; break;
		case 'T': res = Assets.letra_T; break;
		case 'U': res = Assets.letra_U; break;
		case 'V': res = Assets.letra_V; break;
		case 'W': res = Assets.letra_W; break;
		case 'X': res = Assets.letra_X; break;
		case 'Y': res = Assets.letra_Y; break;
		case 'Z': res = Assets.letra_Z; break;
		case 'a': res = Assets.letra_A; break;
		case 'b': res = Assets.letra_B; break;
		case 'c': res = Assets.letra_C; break;
		case 'd': res = Assets.letra_D; break;
		case 'e': res = Assets.letra_E; break;
		case 'f': res = Assets.letra_F; break;
		case 'g': res = Assets.letra_G; break;
		case 'h': res = Assets.letra_H; break;
		case 'i': res = Assets.letra_I; break;
		case 'j': res = Assets.letra_J; break;
		case 'k': res = Assets.letra_K; break;
		case 'l': res = Assets.letra_L; break;
		case 'm': res = Assets.letra_M; break;
		case 'n': res = Assets.letra_N; break;
		case 'o': res = Assets.letra_O; break;
		case 'p': res = Assets.letra_P; break;
		case 'q': res = Assets.letra_Q; break;
		case 'r': res = Assets.letra_R; break;
		case 's': res = Assets.letra_S; break;
		case 't': res = Assets.letra_T; break;
		case 'u': res = Assets.letra_U; break;
		case 'v': res = Assets.letra_V; break;
		case 'w': res = Assets.letra_W; break;
		case 'x': res = Assets.letra_X; break;
		case 'y': res = Assets.letra_Y; break;
		case 'z': res = Assets.letra_Z; break;
		default:
			throw new IllegalArgumentException("No existe imagen para la letra " + letter);
		}
		return res;
	}
}
