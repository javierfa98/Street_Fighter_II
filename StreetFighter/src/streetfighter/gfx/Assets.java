package streetfighter.gfx;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import streetfighter.Launcher;

//Culalquier imagen o sonido presente en el juego
//Para no tener que estar cargando todas las imagenes durante el game loop
public class Assets {
	//Velocidad de las animaciones de escenarios
	private static final int speedStages = 170;
	
	////////////////////// Stages //////////////////////
	public static BufferedImage lifeBar;
	public static BufferedImage lifeBar_KO_blanco;
	public static BufferedImage lifeBar_KO_negro;
	public static Animation KO_logo;
	
	static ArrayList<Animation> animacionesBrazilStage = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesChinaStage = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesJapanStage = new ArrayList<Animation>();
	//Stage Brazil
	static SpriteSheet brazilStageSheet;
	//Stage China
	static SpriteSheet chinaStageSheet;
	//Stage Japan
	static SpriteSheet japanStageSheet;
	public static BufferedImage fondoNocheJapanStage;
	public static BufferedImage nube1;
	////////////////////// Fin Stages //////////////////////
	
	//Personajes
	static ArrayList<Animation> animacionesBixo = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesChun = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesRyu = new ArrayList<Animation>();
	
	//Personajes
	static ArrayList<Animation> animacionesBixo2 = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesChun2 = new ArrayList<Animation>();
	static ArrayList<Animation> animacionesRyu2 = new ArrayList<Animation>();
	
	//public static BufferedImage fondoMenuState;
	public static BufferedImage optionsMenuState;
	public static BufferedImage ranking;
	public static BufferedImage streetLogo;
	public static BufferedImage avion;
	
	//seleccion de personajes
	static BufferedImage ryuR;
	static BufferedImage ryuL;
	static BufferedImage blankaR;
	static BufferedImage blankaL;
	static BufferedImage chunR;
	static BufferedImage chunL;
	
	//pantalla vs
	static BufferedImage ryuRvs;
	static BufferedImage ryuLvs;
	static BufferedImage blankaRvs;
	static BufferedImage blankaLvs;
	static BufferedImage chunRvs;
	static BufferedImage chunLvs;
	
	//pantalla despues de pelea derrotados
	static BufferedImage ryuRDead;
	static BufferedImage ryuLDead;
	static BufferedImage blankaRDead;
	static BufferedImage blankaLDead;
	static BufferedImage chunRDead;
	static BufferedImage chunLDead;
	
	//pantalla despues de pelea ganadores
	static BufferedImage ryuRAlive;
	static BufferedImage ryuLAlive;
	static BufferedImage blankaRAlive;
	static BufferedImage blankaLAlive;
	static BufferedImage chunRAlive;
	static BufferedImage chunLAlive;
	
	public static BufferedImage vs;
	public static BufferedImage mano;
	
	static BufferedImage p1Casilla;
	static BufferedImage p2Casilla;
	static BufferedImage cpuCasilla;
	
	
	static BufferedImage[] names;
	
	//Fondo de menus
	public static BufferedImage fondoSelectFighterState;
	public static BufferedImage fondoVS;
	public static BufferedImage fondoLetras;
	
	static BufferedImage japon;
	static BufferedImage japonGris;
	static BufferedImage brasil;
	static BufferedImage brasilGris;
	static BufferedImage china;
	static BufferedImage chinaGris;
			
	static BufferedImage japonName;
	static BufferedImage brasilName;
	static BufferedImage chinaName;
	
	//Hadouken
	public static BufferedImage hadoukenRyu;
	public static BufferedImage hadoukenChun;
	
	//Final del modo historia de blanka
	public static BufferedImage [] blankaEnd= new BufferedImage[11];
	public static BufferedImage [] blankaCry= new BufferedImage[2];
	public static Animation blankaCryAnimation;
	
	//Final del modo historia de chun
	public static BufferedImage [] chunEnd= new BufferedImage[4];
	
	//Final del modo historia de ryu
	public static BufferedImage [] ryuWalk= new BufferedImage[4];
	public static BufferedImage [] ryuWrites= new BufferedImage[5];
	public static Animation ryuWalkAnimation;
	
	// Letras y numeros
	//Romanos
	public static BufferedImage uno;
	public static BufferedImage dos;
	public static BufferedImage tres;
	public static BufferedImage cuatro;
	public static BufferedImage cinco;
	public static BufferedImage seis;
	public static BufferedImage siete;
	public static BufferedImage ocho;
	public static BufferedImage nueve;
	public static BufferedImage diez;
	//Normales
	public static BufferedImage number_Cero;
	public static BufferedImage number_Uno;
	public static BufferedImage number_Dos;
	public static BufferedImage number_Tres;
	public static BufferedImage number_Cuatro;
	public static BufferedImage number_Cinco;
	public static BufferedImage number_Seis;
	public static BufferedImage number_Siete;
	public static BufferedImage number_Ocho;
	public static BufferedImage number_Nueve;
	public static BufferedImage number_Cero_T;
	public static BufferedImage number_Uno_T;
	public static BufferedImage number_Dos_T;
	public static BufferedImage number_Tres_T;
	public static BufferedImage number_Cuatro_T;
	public static BufferedImage number_Cinco_T;
	public static BufferedImage number_Seis_T;
	public static BufferedImage number_Siete_T;
	public static BufferedImage number_Ocho_T;
	public static BufferedImage number_Nueve_T;
	//Letras
	public static BufferedImage letra_Guion;
	public static BufferedImage letra_A;
	public static BufferedImage letra_B;
	public static BufferedImage letra_C;
	public static BufferedImage letra_D;
	public static BufferedImage letra_E;
	public static BufferedImage letra_F;
	public static BufferedImage letra_G;
	public static BufferedImage letra_H;
	public static BufferedImage letra_I;
	public static BufferedImage letra_J;
	public static BufferedImage letra_K;
	public static BufferedImage letra_L;
	public static BufferedImage letra_M;
	public static BufferedImage letra_N;
	public static BufferedImage letra_O;
	public static BufferedImage letra_P;
	public static BufferedImage letra_Q;
	public static BufferedImage letra_R;
	public static BufferedImage letra_S;
	public static BufferedImage letra_T;
	public static BufferedImage letra_U;
	public static BufferedImage letra_V;
	public static BufferedImage letra_W;
	public static BufferedImage letra_X;
	public static BufferedImage letra_Y;
	public static BufferedImage letra_Z;
	
	public static BufferedImage fondoSelectFighterState2;
	
	public static void initAssets_LettersNumbers() {
		uno=ImageLoader.loadImage("/res/textures/rankingMenu/uno.png");
		dos=ImageLoader.loadImage("/res/textures/rankingMenu/dos.png");
		tres=ImageLoader.loadImage("/res/textures/rankingMenu/tres.png");
		cuatro=ImageLoader.loadImage("/res/textures/rankingMenu/cuatro.png");
		cinco=ImageLoader.loadImage("/res/textures/rankingMenu/cinco.png");
		seis=ImageLoader.loadImage("/res/textures/rankingMenu/seis.png");
		siete=ImageLoader.loadImage("/res/textures/rankingMenu/siete.png");
		ocho=ImageLoader.loadImage("/res/textures/rankingMenu/ocho.png");
		nueve=ImageLoader.loadImage("/res/textures/rankingMenu/nueve.png");
		diez=ImageLoader.loadImage("/res/textures/rankingMenu/diez.png");
		
		number_Cero=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberCero.png");
		number_Uno=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberUno.png");
		number_Dos=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberDos.png");
		number_Tres=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberTres.png");
		number_Cuatro=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberCuatro.png");
		number_Cinco=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberCinco.png");
		number_Seis=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberSeis.png");
		number_Siete=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberSiete.png");
		number_Ocho=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberOcho.png");
		number_Nueve=ImageLoader.loadImage("/res/textures/rankingMenu/numbers/numberNueve.png");
		
		number_Cero_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberCero.png");
		number_Uno_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberUno.png");
		number_Dos_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberDos.png");
		number_Tres_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberTres.png");
		number_Cuatro_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberCuatro.png");
		number_Cinco_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberCinco.png");
		number_Seis_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberSeis.png");
		number_Siete_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberSiete.png");
		number_Ocho_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberOcho.png");
		number_Nueve_T=ImageLoader.loadImage("/res/textures/rankingMenu/numbersTrans/numberNueve.png");
		
		letra_Guion=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraGuion.png");
		letra_A=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraA.png");
		letra_B=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraB.png");
		letra_C=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraC.png");
		letra_D=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraD.png");
		letra_E=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraE.png");
		letra_F=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraF.png");
		letra_G=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraG.png");
		letra_H=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraH.png");
		letra_I=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraI.png");
		letra_J=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraJ.png");
		letra_K=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraK.png");
		letra_L=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraL.png");
		letra_M=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraM.png");
		letra_N=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraN.png");
		letra_O=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraO.png");
		letra_P=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraP.png");
		letra_Q=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraQ.png");
		letra_R=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraR.png");
		letra_S=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraS.png");
		letra_T=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraT.png");
		letra_U=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraU.png");
		letra_V=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraV.png");
		letra_W=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraW.png");
		letra_X=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraX.png");
		letra_Y=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraY.png");
		letra_Z=ImageLoader.loadImage("/res/textures/rankingMenu/letras/letraZ.png");
	}
	
	public static void initAssets_MenuState() {
		SpriteSheet menuSM=new SpriteSheet(ImageLoader.loadImage("/res/menus/2.1_AvionTransparente.png"));
		avion=menuSM.crop(190, 261, 35, 35);
		
		//fondoMenuState=ImageLoader.loadImage("/menus/2.png");
		optionsMenuState=ImageLoader.loadImage("/res/menus/menuDefinitive.png");
		ranking=ImageLoader.loadImage("/res/textures/optionsMenu/ranking.png");
		streetLogo=ImageLoader.loadImage("/res/textures/enterGameMenu/streetLogo3.png");
	}
	
	public static void initAssets_SelectFighterState() {
		fondoSelectFighterState=ImageLoader.loadImage("/res/menus/3.png");
		fondoSelectFighterState2=ImageLoader.loadImage("/res/menus/fondo2.png");
		fondoVS=ImageLoader.loadImage("/res/menus/fondo.png");
		
		SpriteSheet fightersSheet=new SpriteSheet(ImageLoader.loadImage("/res/menus/fightersSheet.png"));
		
		ryuR=fightersSheet.crop(1090, 10, 490, 500);
		ryuL=fightersSheet.crop(1595, 10, 490, 500);
		blankaR=fightersSheet.crop(1100, 988, 485, 455);
		blankaL=fightersSheet.crop(1620, 988, 475, 455);
		chunR=fightersSheet.crop(1085, 520, 490, 470);
		chunL=fightersSheet.crop(1605, 520, 490, 470);
		
		vs=fightersSheet.crop(93, 1370, 655, 263);
		ryuRvs=fightersSheet.crop(770, 1460, 490, 520);
		ryuLvs=fightersSheet.crop(1290, 1460, 490, 520);
		blankaRvs=fightersSheet.crop(1800, 1520, 485, 455);
		blankaLvs=fightersSheet.crop(2300, 1520, 485, 455);
		chunRvs=fightersSheet.crop(2120, 100, 490, 490);
		chunLvs=fightersSheet.crop(2120, 600, 490, 490);
		
		SpriteSheet menuSP=new SpriteSheet(ImageLoader.loadImage("/res/textures/sheetMenu.png"));
		p1Casilla=menuSP.crop(280, 123, 21, 38);
		p2Casilla=menuSP.crop(280, 160, 21, 38);
		cpuCasilla=menuSP.crop(305, 160, 21, 38);
		
		
		//Flags
		//japon=fightersSheet.crop(128, 1665, 174, 83 );
		japon=ImageLoader.loadImage("/res/menus/japanColor.png");
		japonGris=ImageLoader.loadImage("/res/menus/japanGris.png");
		brasil=ImageLoader.loadImage("/res/menus/brazilColor.png");
		brasilGris=ImageLoader.loadImage("/res/menus/brazilGris.png");
		china=ImageLoader.loadImage("/res/menus/chinaColor.png");
		chinaGris=ImageLoader.loadImage("/res/menus/chinaGris.png");
		
		//FlagsName
		japonName=fightersSheet.crop(638, 1664, 100, 30 );
		brasilName=fightersSheet.crop(638, 1700, 100, 34 );
		chinaName=fightersSheet.crop(638, 1745, 93, 31 );
		
		
	}
	
	public static void initAssets_GameState() {
		fondoLetras=ImageLoader.loadImage("/res/menus/frase.png");
		
		SpriteSheet fightersSheet=new SpriteSheet(ImageLoader.loadImage("/res/menus/fightersSheet.png"));
		
		//Menu dead
		ryuRAlive=fightersSheet.crop(770, 1460, 490, 430);
		ryuLAlive=fightersSheet.crop(1290, 1460, 490, 430);
		blankaRAlive=fightersSheet.crop(1800, 1520, 485, 360);
		blankaLAlive=fightersSheet.crop(2300, 1520, 485, 365);
		chunRAlive=fightersSheet.crop(2120, 100, 490, 400);
		chunLAlive=fightersSheet.crop(2120, 600, 490, 395);
		
		//Menu dead
		ryuRDead=fightersSheet.crop(60, 900, 490, 430);
		ryuLDead=fightersSheet.crop(580, 900, 490, 430);
		blankaRDead=fightersSheet.crop(50, 30, 470, 370);
		blankaLDead=fightersSheet.crop(580, 30, 470, 370);
		chunRDead=fightersSheet.crop(50, 455, 470, 400);
		chunLDead=fightersSheet.crop(560, 455, 470, 400);
		
		mano=fightersSheet.crop(530, 1835, 118, 115);
	}
	
	public static void initAssets_EndGameState() {
		//BLANKA
		blankaEnd[0]=ImageLoader.loadImage("/res/end/blanka/blanka1.jpeg");
		blankaEnd[1]=ImageLoader.loadImage("/res/end/blanka/blanka2.jpeg");
		blankaEnd[2]=ImageLoader.loadImage("/res/end/blanka/blanka3.jpeg");
		blankaEnd[3]=ImageLoader.loadImage("/res/end/blanka/blanka4.jpeg");
		blankaEnd[4]=ImageLoader.loadImage("/res/end/blanka/blanka5.jpeg");
		blankaEnd[5]=ImageLoader.loadImage("/res/end/blanka/blanka6.jpeg");
		blankaEnd[6]=ImageLoader.loadImage("/res/end/blanka/blanka7.jpeg");
		blankaEnd[7]=ImageLoader.loadImage("/res/end/blanka/blanka8.jpeg");
		blankaEnd[8]=ImageLoader.loadImage("/res/end/blanka/blanka9.jpeg");
		blankaEnd[9]=ImageLoader.loadImage("/res/end/blanka/blanka10.jpeg");
		blankaEnd[10]=ImageLoader.loadImage("/res/end/blanka/blanka11.jpeg");
		
		blankaCry[0]=ImageLoader.loadImage("/res/end/blanka/blankaCry1.jpeg");
		blankaCry[1]=ImageLoader.loadImage("/res/end/blanka/blankaCry2.jpeg");
		blankaCryAnimation=new Animation(300,blankaCry);
		
		//CHUN
		chunEnd[0]=ImageLoader.loadImage("/res/end/chun/chun1.jpeg");
		chunEnd[1]=ImageLoader.loadImage("/res/end/chun/chun2.jpeg");
		chunEnd[2]=ImageLoader.loadImage("/res/end/chun/chun3.jpeg");
		chunEnd[3]=ImageLoader.loadImage("/res/end/chun/chun4.jpeg");
		
		//RYU
		ryuWalk[0]=ImageLoader.loadImage("/res/end/ryu/ryuWalk1.jpg");
		ryuWalk[1]=ImageLoader.loadImage("/res/end/ryu/ryuWalk2.jpg");
		ryuWalk[2]=ImageLoader.loadImage("/res/end/ryu/ryuWalk3.jpg");
		ryuWalk[3]=ryuWalk[1];
		ryuWalkAnimation = new Animation(300,ryuWalk);
		
		ryuWrites[0]=ImageLoader.loadImage("/res/end/ryu/ryu1.jpeg");
		ryuWrites[1]=ImageLoader.loadImage("/res/end/ryu/ryu2.jpeg");
		ryuWrites[2]=ImageLoader.loadImage("/res/end/ryu/ryu3.jpeg");
		ryuWrites[3]=ImageLoader.loadImage("/res/end/ryu/ryu4.jpeg");
		ryuWrites[4]=ImageLoader.loadImage("/res/end/ryu/ryu5.jpeg");
		
	}
	
	public static ArrayList<Animation> initAssets_World(int id) {
		//Life bar
		SpriteSheet lifeBarSprite=new SpriteSheet(ImageLoader.loadImage("/res/textures/lifeBar/lifeBar.png"));
		lifeBar = lifeBarSprite.crop(31, 9, 258, 14);
		lifeBar_KO_blanco = lifeBarSprite.crop(144, 9, 32, 14);
		SpriteSheet lifeBarSpriteKONegro=new SpriteSheet(ImageLoader.loadImage("/res/textures/lifeBar/lifeBar_KO_negro.png"));
		lifeBar_KO_negro = lifeBarSpriteKONegro.crop(144, 9, 32, 14);
		
		BufferedImage[] animationsKO_logo = new BufferedImage[2];
		animationsKO_logo[0] = lifeBar_KO_blanco;
		animationsKO_logo[1] = lifeBar_KO_negro;
		KO_logo = new Animation(100,animationsKO_logo);
		
		if(id==0) {
			initAssets_BrazilStage();
			return animacionesBrazilStage;
		}
		else if(id==1) {
			initAssets_ChinaStage();
			return animacionesChinaStage;
		}
		else if(id==2) {
			initAssets_JapanStage();
			return animacionesJapanStage;
		}
		
		return null;
		
	}
	
	public static void initAssets_BrazilStage() {
		brazilStageSheet = new SpriteSheet(ImageLoader.loadImage("/res/stages/brazilStage.png"));
		
		BufferedImage[] brazilStageSuelo = new BufferedImage[1];
		brazilStageSuelo[0] = brazilStageSheet.crop(8, 416, 511, 39);
		
		BufferedImage[] hombrePez = new BufferedImage[2];
		hombrePez[0] = brazilStageSheet.crop(156, 129, 26, 61);
		hombrePez[1] = brazilStageSheet.crop(8, 885, 26, 74);
		
		BufferedImage[] dosHombres = new BufferedImage[2];
		dosHombres[0] = brazilStageSheet.crop(308, 129, 42, 62);
		dosHombres[1] = brazilStageSheet.crop(48, 897, 42, 62);
		
		BufferedImage[] fotografo = new BufferedImage[3];
		fotografo[0] = brazilStageSheet.crop(319, 63, 35, 46);
		fotografo[1] = brazilStageSheet.crop(319, 63, 35, 46);
		fotografo[2] = brazilStageSheet.crop(96, 914, 35, 46);
		
		BufferedImage[] mujer = new BufferedImage[2];
		mujer[0] = brazilStageSheet.crop(156, 46, 21, 63);
		mujer[1] = brazilStageSheet.crop(144, 897, 24, 63);
		
		BufferedImage[] casaSiluro = new BufferedImage[1];
		casaSiluro[0] = brazilStageSheet.crop(357, 8, 131, 175);
		
		BufferedImage[] palitos = new BufferedImage[1];
		palitos[0] = brazilStageSheet.crop(200, 206, 168, 26);
		
		BufferedImage[] fondoBrazilStage = new BufferedImage[1];
		fondoBrazilStage[0] = brazilStageSheet.crop(56, 240, 416, 176);
		
		BufferedImage[] arbolSnake = new BufferedImage[1];
		arbolSnake[0] = brazilStageSheet.crop(200, 0, 106, 184);
		
		BufferedImage[] palito2 = new BufferedImage[1];
		palito2[0] = brazilStageSheet.crop(130, 143, 8, 26);
		
		animacionesBrazilStage.add(new Animation(speedStages,brazilStageSuelo));
		animacionesBrazilStage.add(new Animation(440,hombrePez));
		animacionesBrazilStage.add(new Animation(440,dosHombres));
		animacionesBrazilStage.add(new Animation(550,fotografo));
		animacionesBrazilStage.add(new Animation(440,mujer));
		animacionesBrazilStage.add(new Animation(speedStages,casaSiluro));
		animacionesBrazilStage.add(new Animation(speedStages,palitos));
		animacionesBrazilStage.add(new Animation(speedStages,fondoBrazilStage));
		animacionesBrazilStage.add(new Animation(speedStages,arbolSnake));
		animacionesBrazilStage.add(new Animation(speedStages,palito2));
	}
	
	public static void initAssets_ChinaStage() {
		chinaStageSheet = new SpriteSheet(ImageLoader.loadImage("/res/stages/chinaStage.png"));
		
		BufferedImage[] chinaStageSuelo = new BufferedImage[1];
		chinaStageSuelo[0] = chinaStageSheet.crop(0, 160, 512, 63);
		
		BufferedImage[] chinaStageValla = new BufferedImage[1];
		chinaStageValla[0] = chinaStageSheet.crop(0, 336, 512, 91);
		
		BufferedImage[] chinaStageFondo = new BufferedImage[1];
		chinaStageFondo[0] = chinaStageSheet.crop(80, 0, 351, 159);
		
		animacionesChinaStage.add(new Animation(speedStages,chinaStageSuelo));
		animacionesChinaStage.add(new Animation(speedStages,chinaStageValla));
		animacionesChinaStage.add(new Animation(speedStages,chinaStageFondo));
	}
	
	public static void initAssets_JapanStage() {
		japanStageSheet = new SpriteSheet(ImageLoader.loadImage("/res/stages/japanStage.png"));
		fondoNocheJapanStage = ImageLoader.loadImage("/res/stages/fondoNocheJapanStage.png");
		
		nube1 = ImageLoader.loadImage("/res/stages/nube1.png");
		
		BufferedImage[] japanStageSuelo = new BufferedImage[1];
		japanStageSuelo[0] = japanStageSheet.crop(0, 0, 512, 215);
		
		BufferedImage[] japanStageTejadilloMedio = new BufferedImage[1];
		japanStageTejadilloMedio[0] = japanStageSheet.crop(0, 235, 512, 188);
		
		BufferedImage[] japanStageMansion = new BufferedImage[1];
		japanStageMansion[0] = japanStageSheet.crop(258, 504, 77, 63);
		
		BufferedImage[] japanStageNubes = new BufferedImage[1];
		japanStageNubes[0] = japanStageSheet.crop(0, 426, 512, 74);
		
		animacionesJapanStage.add(new Animation(speedStages,japanStageSuelo));
		animacionesJapanStage.add(new Animation(speedStages,japanStageTejadilloMedio));
		animacionesJapanStage.add(new Animation(speedStages,japanStageMansion));
		animacionesJapanStage.add(new Animation(speedStages,japanStageNubes));
	}
	
	public static void initAssets_fighters(String path){
		//Conjunto de assets de los 3 personajes
        SpriteSheet ryuSS=new SpriteSheet(ImageLoader.loadImage("/res/textures/spriteSheetRyuTransp.png"));
        SpriteSheet bixoSS=new SpriteSheet(ImageLoader.loadImage("/res/textures/spriteSheetBixoTransp.png"));
        SpriteSheet chunSS=new SpriteSheet(ImageLoader.loadImage("/res/textures/spriteSheetChunTransp.png"));
        SpriteSheet SS = null;
        
		//Hadouken Ryu
		hadoukenRyu = ryuSS.crop(417,651,40,30);
		
		//Hadouken Chun
		hadoukenChun = chunSS.crop(481, 606, 40, 30);
		
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			archivo = new File (path+"/.configStreetFighterII/fighters.asset");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			//Stats
	        int id,speed,width,height;
	        // Lectura del fichero
	        String linea;
	        String[] stat = null;
	        while(!(linea=br.readLine()).equals("FIN")) {        	  
	        	if (linea.equals("INIT_F")) {
	        		br.readLine(); //Linea de informacion
	        		linea=br.readLine();
	        		stat = linea.split(" ");
	        		id = Integer.parseInt(stat[0]);
	        		speed = Integer.parseInt(stat[1]);
	        		width = Integer.parseInt(stat[2]);
	        		height = Integer.parseInt(stat[3]);
	        		//Accedemos al sprite sheet correspondiente:
	        		if (id == 0) SS = bixoSS;
	        		if (id == 1) SS = chunSS;
	        		if (id == 2) SS = ryuSS;
	        		//Bucle de assets
	        		while(!(linea=br.readLine()).equals("FIN_F")) { //INIT_ASSET
		        		int tam = Integer.parseInt(br.readLine());
		        		BufferedImage[] anim = new BufferedImage[tam];
		        		int vWidth[] = new int[tam];
		        		int vHeight[] = new int[tam];
		        		//Bucle de frames del asset
		        		int i = 0;
		        		while(!(linea=br.readLine()).equals("FIN_ASSET")) { 
		        			stat = linea.split(" ");
		        			anim[i]= SS.crop(Integer.parseInt(stat[0]), Integer.parseInt(stat[1]),
		        					Integer.parseInt(stat[2]), Integer.parseInt(stat[3]));
		        			
		        			if (stat[4].equals("D")) { //Por defecto
		        				vWidth[i] = width;
		        			}
		        			else {
		        				vWidth[i] = width+Integer.parseInt(stat[4]);
		        			}
		        			if (stat[5].equals("D")) { //Por defecto
		        				vHeight[i] = height;
		        			}
		        			else {
		        				vHeight[i] = height + Integer.parseInt(stat[5]);
		        			}
		        			
		        			i++;
		        		}
		        		//Leemos la velocidad de la animacion que hemos cargado
		        		linea=br.readLine();
		        		int speedA;
		        		if (linea.equals("D")) {
		        			speedA = speed;
		        		}
		        		else {
		        			speedA = speed + Integer.parseInt(linea);
		        		}
		        		Animation a = new Animation(speedA,anim,vWidth,vHeight);
		        		if (id == 0) animacionesBixo.add(a);
		        		if (id == 1) animacionesChun.add(a);
		        		if (id == 2) animacionesRyu.add(a);
		        		
		        		Animation a2 = new Animation(speedA,anim,vWidth,vHeight);
		        		if (id == 0) animacionesBixo2.add(a2);
		        		if (id == 1) animacionesChun2.add(a2);
		        		if (id == 2) animacionesRyu2.add(a2);
	        		}
	        		
	        	}
	        }

			br.close();
			fr.close();
        }
		catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{                    
        		if( null != fr ){   
        			fr.close();     
        		}                  
        	}catch (Exception e2){ 
        		e2.printStackTrace();
        	}
        }
	}
	
	public static ArrayList<Animation> getAnimation(int id) {
		if(id==0)
			return animacionesBixo;
		if(id==1)
			return animacionesChun;
		if(id==2)
			return animacionesRyu;
		
		return null;
	}
	
	public static ArrayList<Animation> getAnimation2(int id) {
		if(id==0)
			return animacionesBixo2;
		if(id==1)
			return animacionesChun2;
		if(id==2)
			return animacionesRyu2;
		
		return null;
	}
	
	public static BufferedImage getFace(int player, int oneOrtwo) {
		if(player==0) {
			if(oneOrtwo==1) {
				return blankaR;
			}
			else {
				return blankaL;
			}
		}
		if(player==1) {
			if(oneOrtwo==1) {
				return chunR;
			}
			else {
				return chunL;
			}
		}
		if(player==2) {
			if(oneOrtwo==1) {
				return ryuR;
			}
			else {
				return ryuL;
			}
		}
		
		return null;
	}
	
	public static BufferedImage getFaceAlive(int player, int oneOrtwo) {
		if(player==0) {
			if(oneOrtwo==1) {
				return blankaRAlive;
			}
			else {
				return blankaLAlive;
			}
		}
		if(player==1) {
			if(oneOrtwo==1) {
				return chunRAlive;
			}
			else {
				return chunLAlive;
			}
		}
		if(player==2) {
			if(oneOrtwo==1) {
				return ryuRAlive;
			}
			else {
				return ryuLAlive;
			}
		}
		
		return null;
	}
	
	public static BufferedImage getFaceVS(int player, int oneOrtwo) {
		if(player==0) {
			if(oneOrtwo==1) {
				return blankaRvs;
			}
			else {
				return blankaLvs;
			}
		}
		if(player==1) {
			if(oneOrtwo==1) {
				return chunRvs;
			}
			else {
				return chunLvs;
			}
		}
		if(player==2) {
			if(oneOrtwo==1) {
				return ryuRvs;
			}
			else {
				return ryuLvs;
			}
		}
		
		return null;
	}
	
	public static BufferedImage getFaceDead(int player, int oneOrtwo) {
		if(player==0) {
			if(oneOrtwo==1) {
				return blankaRDead;
			}
			else {
				return blankaLDead;
			}
		}
		if(player==1) {
			if(oneOrtwo==1) {
				return chunRDead;
			}
			else {
				return chunLDead;
			}
		}
		if(player==2) {
			if(oneOrtwo==1) {
				return ryuRDead;
			}
			else {
				return ryuLDead;
			}
		}
		
		return null;
	}
	
	public static BufferedImage getCasilla(int player) {
		if(player==0)
			return cpuCasilla;
		if(player==1)
			return p1Casilla;
		if(player==2)
			return p2Casilla;
					
		return null;
	}
	
	public static BufferedImage getFlag(int player) {
		if(player==0)
			return brasil;
		if(player==1)
			return china;
		if(player==2)
			return japon;
					
		return null;
	}
	
	public static BufferedImage getFlagGris(int player) {
		if(player==0)
			return brasilGris;
		if(player==1)
			return chinaGris;
		if(player==2)
			return japonGris;
					
		return null;
	}
	
	public static BufferedImage getFlagName(int player) {
		if(player==0)
			return brasilName;
		if(player==1)
			return chinaName;
		if(player==2)
			return japonName;
					
		return null;
	}

}
