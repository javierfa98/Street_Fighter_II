package streetfighter.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Funciones que nos ayudan en distintos aspectos del videojuego
public class Utils {

	public static String loadFileAsString(String path){
		StringBuilder builder=new StringBuilder();
		
		try {
			BufferedReader br=new BufferedReader(new FileReader(path));
			String line;
			while((line=br.readLine())!=null) {
				builder.append(line + "\n");
			}
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
		
	}
	
	//Convertir numero(String) a numero(int)
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
	public static int string_to_key(String string) {
		if (string.equals("LEFT")) {
			return KeyEvent.VK_LEFT;
		}
		else if (string.equals("RIGHT")){
			return KeyEvent.VK_RIGHT;
		}
		else if (string.equals("DOWN")){
			return KeyEvent.VK_DOWN;
		}
		else if (string.equals("DOWN")){
			return KeyEvent.VK_DOWN;
		}
		else if (string.equals("UP")){
			return KeyEvent.VK_UP;
		}
		else if (string.equals("ENTER")){
			return KeyEvent.VK_ENTER;
		}
		else if (string.equals("SPACE")) {
			return KeyEvent.VK_SPACE;
		}
		else {
			//Caracter no reconocido
			return 0;
		}
	}
	
	public static int character_to_key(char character) {
        switch (character) {
        case 'a': return(KeyEvent.VK_A);
        case 'b': return(KeyEvent.VK_B); 
        case 'c': return(KeyEvent.VK_C); 
        case 'd': return(KeyEvent.VK_D); 
        case 'e': return(KeyEvent.VK_E); 
        case 'f': return(KeyEvent.VK_F); 
        case 'g': return(KeyEvent.VK_G); 
        case 'h': return(KeyEvent.VK_H); 
        case 'i': return(KeyEvent.VK_I); 
        case 'j': return(KeyEvent.VK_J); 
        case 'k': return(KeyEvent.VK_K); 
        case 'l': return(KeyEvent.VK_L); 
        case 'm': return(KeyEvent.VK_M); 
        case 'n': return(KeyEvent.VK_N); 
        case 'o': return(KeyEvent.VK_O); 
        case 'p': return(KeyEvent.VK_P); 
        case 'q': return(KeyEvent.VK_Q); 
        case 'r': return(KeyEvent.VK_R); 
        case 's': return(KeyEvent.VK_S); 
        case 't': return(KeyEvent.VK_T); 
        case 'u': return(KeyEvent.VK_U); 
        case 'v': return(KeyEvent.VK_V); 
        case 'w': return(KeyEvent.VK_W); 
        case 'x': return(KeyEvent.VK_X); 
        case 'y': return(KeyEvent.VK_Y); 
        case 'z': return(KeyEvent.VK_Z); 
        case 'A': return(KeyEvent.VK_A); 
        case 'B': return(KeyEvent.VK_B); 
        case 'C': return(KeyEvent.VK_C); 
        case 'D': return(KeyEvent.VK_D); 
        case 'E': return(KeyEvent.VK_E); 
        case 'F': return(KeyEvent.VK_F); 
        case 'G': return(KeyEvent.VK_G); 
        case 'H': return(KeyEvent.VK_H); 
        case 'I': return(KeyEvent.VK_I); 
        case 'J': return(KeyEvent.VK_J); 
        case 'K': return(KeyEvent.VK_K); 
        case 'L': return(KeyEvent.VK_L); 
        case 'M': return(KeyEvent.VK_M); 
        case 'N': return(KeyEvent.VK_N); 
        case 'O': return(KeyEvent.VK_O); 
        case 'P': return(KeyEvent.VK_P); 
        case 'Q': return(KeyEvent.VK_Q); 
        case 'R': return(KeyEvent.VK_R); 
        case 'S': return(KeyEvent.VK_S); 
        case 'T': return(KeyEvent.VK_T); 
        case 'U': return(KeyEvent.VK_U); 
        case 'V': return(KeyEvent.VK_V); 
        case 'W': return(KeyEvent.VK_W); 
        case 'X': return(KeyEvent.VK_X); 
        case 'Y': return(KeyEvent.VK_Y); 
        case 'Z': return(KeyEvent.VK_Z); 
        case '`': return(KeyEvent.VK_BACK_QUOTE); 
        case '0': return(KeyEvent.VK_0); 
        case '1': return(KeyEvent.VK_1); 
        case '2': return(KeyEvent.VK_2); 
        case '3': return(KeyEvent.VK_3); 
        case '4': return(KeyEvent.VK_4); 
        case '5': return(KeyEvent.VK_5); 
        case '6': return(KeyEvent.VK_6); 
        case '7': return(KeyEvent.VK_7); 
        case '8': return(KeyEvent.VK_8); 
        case '9': return(KeyEvent.VK_9); 
        case '-': return(KeyEvent.VK_MINUS); 
        case '=': return(KeyEvent.VK_EQUALS); 
        case '~': return(KeyEvent.VK_BACK_QUOTE); 
        case '!': return(KeyEvent.VK_EXCLAMATION_MARK); 
        case '@': return(KeyEvent.VK_AT); 
        case '#': return(KeyEvent.VK_NUMBER_SIGN); 
        case '$': return(KeyEvent.VK_DOLLAR); 
        case '%': return(KeyEvent.VK_5); 
        case '^': return(KeyEvent.VK_CIRCUMFLEX); 
        case '&': return(KeyEvent.VK_AMPERSAND); 
        case '*': return(KeyEvent.VK_ASTERISK); 
        case '(': return(KeyEvent.VK_LEFT_PARENTHESIS); 
        case ')': return(KeyEvent.VK_RIGHT_PARENTHESIS); 
        case '_': return(KeyEvent.VK_UNDERSCORE); 
        case '+': return(KeyEvent.VK_PLUS); 
        case '\t': return(KeyEvent.VK_TAB); 
        case '\n': return(KeyEvent.VK_ENTER); 
        case '[': return(KeyEvent.VK_OPEN_BRACKET); 
        case ']': return(KeyEvent.VK_CLOSE_BRACKET); 
        case '\\': return(KeyEvent.VK_BACK_SLASH); 
        case '{': return(KeyEvent.VK_OPEN_BRACKET); 
        case '}': return(KeyEvent.VK_CLOSE_BRACKET); 
        case '|': return(KeyEvent.VK_BACK_SLASH); 
        case ';': return(KeyEvent.VK_SEMICOLON); 
        case ':': return(KeyEvent.VK_COLON); 
        case '\'': return(KeyEvent.VK_QUOTE); 
        case '"': return(KeyEvent.VK_QUOTEDBL); 
        case ',': return(KeyEvent.VK_COMMA); 
        case '<': return(KeyEvent.VK_COMMA); 
        case '.': return(KeyEvent.VK_PERIOD); 
        case '>': return(KeyEvent.VK_PERIOD); 
        case '/': return(KeyEvent.VK_SLASH); 
        case '?': return(KeyEvent.VK_SLASH); 
        case ' ': return(KeyEvent.VK_SPACE); 
        default:
            throw new IllegalArgumentException("Caracter no reconocido " + character);
        }
    }
	
	public static String key_to_string(int key) {
        switch (key) {
        case KeyEvent.VK_LEFT: return "LEFT";
        case KeyEvent.VK_RIGHT: return "RIGHT";
        case KeyEvent.VK_UP: return "UP";
        case KeyEvent.VK_DOWN: return "DOWN";
        case KeyEvent.VK_ENTER: return "ENTER";
        case KeyEvent.VK_SPACE: return "SPACE";
        case KeyEvent.VK_A: return "A";
        case KeyEvent.VK_B: return "B"; 
        case KeyEvent.VK_C: return "C"; 
        case KeyEvent.VK_D: return "D"; 
        case KeyEvent.VK_E: return "E"; 
        case KeyEvent.VK_F: return "F"; 
        case KeyEvent.VK_G: return "G"; 
        case KeyEvent.VK_H: return "H"; 
        case KeyEvent.VK_I: return "I"; 
        case KeyEvent.VK_J: return "J"; 
        case KeyEvent.VK_K: return "K"; 
        case KeyEvent.VK_L: return "L"; 
        case KeyEvent.VK_M: return "M"; 
        case KeyEvent.VK_N: return "N"; 
        case KeyEvent.VK_O: return "O"; 
        case KeyEvent.VK_P: return "P"; 
        case KeyEvent.VK_Q: return "Q"; 
        case KeyEvent.VK_R: return "R"; 
        case KeyEvent.VK_S: return "S"; 
        case KeyEvent.VK_T: return "T"; 
        case KeyEvent.VK_U: return "U"; 
        case KeyEvent.VK_V: return "V"; 
        case KeyEvent.VK_W: return "W"; 
        case KeyEvent.VK_X: return "X"; 
        case KeyEvent.VK_Y: return "Y"; 
        case KeyEvent.VK_Z: return "Z"; 
        case KeyEvent.VK_BACK_QUOTE: return "`"; 
        case KeyEvent.VK_0: return "0"; 
        case KeyEvent.VK_1: return "1"; 
        case KeyEvent.VK_2: return "2"; 
        case KeyEvent.VK_3: return "3"; 
        case KeyEvent.VK_4: return "4"; 
        case KeyEvent.VK_5: return "5"; 
        case KeyEvent.VK_6: return "6"; 
        case KeyEvent.VK_7: return "7"; 
        case KeyEvent.VK_8: return "8"; 
        case KeyEvent.VK_9: return "9"; 
        case KeyEvent.VK_MINUS: return "-"; 
        case KeyEvent.VK_EQUALS: return "="; 
        case KeyEvent.VK_EXCLAMATION_MARK: return "!"; 
        case KeyEvent.VK_AT: return "@"; 
        case KeyEvent.VK_NUMBER_SIGN: return "#"; 
        case KeyEvent.VK_DOLLAR: return "$"; 
        case KeyEvent.VK_CIRCUMFLEX: return "^"; 
        case KeyEvent.VK_AMPERSAND: return "&"; 
        case KeyEvent.VK_ASTERISK: return "*"; 
        case KeyEvent.VK_LEFT_PARENTHESIS: return "("; 
        case KeyEvent.VK_RIGHT_PARENTHESIS: return ")"; 
        case KeyEvent.VK_UNDERSCORE: return "_"; 
        case KeyEvent.VK_PLUS: return "+"; 
        case KeyEvent.VK_TAB: return "\t"; 
        //case KeyEvent.VK_ENTER: return "\n"; 
        case KeyEvent.VK_OPEN_BRACKET: return "["; 
        case KeyEvent.VK_CLOSE_BRACKET: return "]"; 
        case KeyEvent.VK_BACK_SLASH: return "\\";
        case KeyEvent.VK_SEMICOLON: return ";"; 
        case KeyEvent.VK_COLON: return ":"; 
        case KeyEvent.VK_QUOTE: return "\""; 
        case KeyEvent.VK_QUOTEDBL: return "\""; 
        case KeyEvent.VK_COMMA: return ",";
        case KeyEvent.VK_PERIOD: return ".";
        case KeyEvent.VK_SLASH: return "/";
        default:
            return "NO RECONOCIDA";
        }
    }
}
