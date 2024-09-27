package streetfighter.ia;

import java.util.ArrayList;

import streetfighter.Game;
import streetfighter.Handler;
import streetfighter.entities.Hadouken;
import streetfighter.entities.Movs;
import streetfighter.entities.Player;

//Tiene que sustituir al key Manager que detecta las teclas pulsadas
public class Brain {
	public boolean up2,down2,left2,right2,punch2,kickl2,kickH2,special2;	//ORDEN en vector
	public boolean p2_stop;
	public int[] movesPoints;  //guard
	int dificultad;	
	boolean dir;
	boolean moverse;
	boolean saltar=false;
	boolean agachar=false;
	boolean primeraVez=true;
	Hadouken hadouken;
	float xIA;
	boolean usarPuntos=true;	//Si entrar en el switch de puntos o no (segun si se pega o se hace otra cosa)
	
	
	Game game;
	
	ArrayList<Movs> pegar = new ArrayList<Movs>();
	
	Movs right=new Movs(0,0,0,40,false,false,false,0);	//Solo riesgo si es para moverte hacia el rival
	Movs left=new Movs(0,0,0,40,false,false,false,0);	//Solo riesgo si es para moverte hacia el rival
	Movs especialRyuChun=new Movs(11,1200,50,50,false,true,true,8);
	
	int moving;					//Para que no tiemble el personaje (si se mueve, se mueve un poco seguido)
	int aggressive=0;
	
	int fighter;
	int cpuPlayer;				//Para saber si CPU del player1 o player2	
	
	boolean golpeAereo1=false;
	boolean golpeAereo2=false;
	int partesGolpeAereo=0;
	
	boolean golpeBajoKick=false;
	boolean golpeBajoPunch=false;
	int partesGolpeBajo=0;
	
	
	
	public Brain(Game game, int cpuPlayer) {
		
		int N_Movimientos = 19;
		
		p2_stop=false;
		
		up2=false;
		down2=false;
		left2=false;
		right2=false;
		punch2=false;
		kickl2=false;
		kickH2=false;
		special2=false;
		
		movesPoints=new int[N_Movimientos];
		for(int i=0;i<movesPoints.length;i++) {
			movesPoints[i]=0;
		}
		
		
		moving=0;
		
		this.cpuPlayer=cpuPlayer;
		this.game=game;
	}
	
	public void tick() {
		//Si se empieza a moverse left o right, que se este moviendo unas cuantas iteraciones (evita temblor personaje)
		if(moving!=0) {
			if(moving==10) {
				moving=0;
			}
			else {
				moving++;
			}
		}
		else {
			
			if(aggressive==120) {
				aggressive=0;
			}
			
			aggressive++;
			
			//resetear movimientos a true
			resetMoves();
			
			//Basadas en el rango
			rangePoints();
			
			//Basadas en la vida 
			riskPoints();
			
			bonusRivalCrouch();
			
			if(dificultad==0) {
				//No se acerca 
				dir=walk(aggressive,5);
			}
			else if(dificultad==1) {
				//Se acerca un poco
				dir=walk(aggressive,4);
			}
			else if(dificultad==2) {
				//Se acerca mucho
				dir=walk(aggressive,2);
			}
			
			
			
			//Realizar la accion mas adecuada
			doMove(dir);
			
			//Si se ha pulsado alguna tecla
			if (up2 | down2 | left2 | right2
					| punch2 | kickl2 | kickH2 | special2) {
				p2_stop = false;
			}
			//Si no se ha pulsado ninguna tecla
			else {
				p2_stop = true;
			}
			
			//Poner puntuaciones a 0
			resetMovesPoints();
		}
		
	}
	
	
	//Realizar la accion que haya recibido mayor puntuacion
	private void doMove(boolean dir) {
		
		//Si comienza golpe aereo no se hace otra cosa hasta que se acaba
		if(golpeAereo2) {
			partesGolpeAereo++;
			//Cuanto mas dificil mas rapido hace el combo de jump+punch
			if(dificultad==0 && partesGolpeAereo==25) {
				punch2=true;
				partesGolpeAereo=0;
				golpeAereo2=false;
			}
			else if(dificultad==1 && partesGolpeAereo==15){
				punch2=true;
				partesGolpeAereo=0;
				golpeAereo2=false;
			}
			else if(dificultad==2 && partesGolpeAereo==10) {
				punch2=true;
				partesGolpeAereo=0;
				golpeAereo2=false;
			}
		}
		else if(golpeBajoKick) {
			partesGolpeBajo++;
			down2=true;
			if(partesGolpeBajo==10) {
				kickl2=true;
				partesGolpeBajo=0;
				golpeBajoKick=false;
			}
		}
		else if(golpeBajoPunch) {
			partesGolpeBajo++;
			down2=true;
			if(partesGolpeBajo==5) {
				punch2=true;
				partesGolpeBajo=0;
				golpeBajoPunch=false;
			}
		}
		else {
			int max=0;
			int maxPoint=movesPoints[0];
			//Ataque que se haria por puntos sumados
			for(int i=1;i<movesPoints.length;i++) {
				if(movesPoints[i]>max) {
					max=i;
					maxPoint=movesPoints[i];
				}
			}
			
			//Probabilidad de que te muevas o de que pegues
			if(dificultad==0) {
				//Mas probabilidad de moverse que de pegar
				moverse=sumarPunto(1);
			}
			else if(dificultad==1) {
				//Algo mas probabilidad moverse que de pegar
				moverse=sumarPunto(3);
			}
			else if(dificultad==2){
				//Misma probabilidad de moverse que de pegar
				moverse=sumarPunto(5);
			}
			
			//Si la votacion de todos los movimientos es cero, nos movemos seguro (no se sabria con que pegar)
			if(maxPoint==0) {
				moverse=true;
				usarPuntos=false;
			}
			
			//Comprobamos si un hadouken esta lo suficientemente cerca de la CPU como para saltarlo
			esquivarSaltando();
			
			//Comprobamos si el player1 hace punch2 o kickh, si es asi la CPU puede agacharse
			esquivarAgachando();
			
			Player cpu=game.getHandler().getWorld().getEntityManager().getPlayer_1();
			Player player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
			boolean orientation=false;
			
			//CPU es player1
			if(cpuPlayer==1) {
				cpu=game.getHandler().getWorld().getEntityManager().getPlayer_1();
				player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
				orientation=game.getHandler().getWorld().getEntityManager().getPlayer_1().getOrientacion();
			}
			//CPU es player2
			else if(cpuPlayer==2) {
				cpu=game.getHandler().getWorld().getEntityManager().getPlayer_2();
				player=game.getHandler().getWorld().getEntityManager().getPlayer_1();
				orientation=game.getHandler().getWorld().getEntityManager().getPlayer_2().getOrientacion();
			}
			
			
			//-------------------------------------------------------------------------------------------------
			//Si la CPU debe saltar por hadouken o porque es chun li y salta mucho
			if(saltar || (fighter==1 && sumarPunto(8))) {
				usarPuntos=false;
				if(dificultad==0 && sumarPunto(4)) {
					//Golpe bajo
					if(sumarPunto(6) && game.getHandler().getWorld().distancia<200) {
						if(sumarPunto(5)) {
							down2=true;
							golpeBajoPunch=true;
						}
						else {
							down2=true;
							golpeBajoKick=true;
						}
						
					}
					//Jump+punch
					else {
						up2=true;
						//Solo se quiere hacer el golpe aereo si se ha entrado aqui y por lo tanto se ha saltado
						//Si esquivarSaltando dice que se salte pues si no entra aqui se ignora el golpeAereo
						if(golpeAereo1 && sumarPunto(4) && cpu.getAnimacionActual()==4) {
							golpeAereo2=true;
						}
					}
					
				}
				else if(dificultad==1 && sumarPunto(2)) {
					//Golpe bajo
					if(sumarPunto(6) && game.getHandler().getWorld().distancia<200) {
						if(sumarPunto(5)) {
							down2=true;
							golpeBajoPunch=true;
						}
						else {
							down2=true;
							golpeBajoKick=true;
						}
						
					}
					//Jump+punch
					else {
						up2=true;
						//Solo se quiere hacer el golpe aereo si se ha entrado aqui y por lo tanto se ha saltado
						//Si esquivarSaltando dice que se salte pues si no entra aqui se ignora el golpeAereo
						if(golpeAereo1 && sumarPunto(4) && cpu.getAnimacionActual()==4) {
							golpeAereo2=true;
						}
					}
				}
				else if(dificultad==2) {
					//Golpe bajo
					if(sumarPunto(6) && game.getHandler().getWorld().distancia<200) {
						if(sumarPunto(5)) {
							down2=true;
							golpeBajoPunch=true;
						}
						else {
							down2=true;
							golpeBajoKick=true;
						}
						
					}
					//Jump+punch
					else {
						up2=true;
						//Solo se quiere hacer el golpe aereo si se ha entrado aqui y por lo tanto se ha saltado
						//Si esquivarSaltando dice que se salte pues si no entra aqui se ignora el golpeAereo
						if(golpeAereo1 && sumarPunto(4) && cpu.getAnimacionActual()==4) {
							golpeAereo2=true;
						}
					}
				}
				saltar=false;
				
				

			}
			//Si la CPU debe agacharse para esquivar un golpe del rival, se hara o no en funcion de la dificultad
			else if(agachar) {
				down2=true;
				agachar=false;
				usarPuntos=false;
			
				
			}
			//Si ha salido moverse en vez de pegar...
			else if(moverse) {
				usarPuntos=false;
				
				//right
				if(dir) {
					right2=true;
					moving++;
					//Si CPU esta en la izquierda y el player1 le esta encimando puede saltarle por encima para escaparse un poco
					if((orientation && cpu.getX()<100 && player.getX()<100 && cpu.getX()<player.getX())
							|| (fighter==1 && sumarPunto(8))) {
						if(dificultad==0 && sumarPunto(7)) {
							up2=true;
						}
						else if(dificultad==1 && sumarPunto(5)) {
							up2=true;
						}
						else if(dificultad==2 && sumarPunto(3)) {
							up2=true;
						}
						
					}
					
				}
				//left
				else {
					left2=true;
					moving++;
					//Si CPU esta en la derecha y el player1 le esta encimando puede saltarle por encima para escaparse un poco
					if((!orientation && cpu.getX()>900 && player.getX()>900 && cpu.getX()>player.getX())
							|| (fighter==1 && sumarPunto(8))) {
						if(dificultad==0 && sumarPunto(7)) {
							up2=true;
						}
						else if(dificultad==1 && sumarPunto(5)) {
							up2=true;
						}
						else if(dificultad==2 && sumarPunto(3)) {
							up2=true;
						}
						
					}
					
				}
				
				
			}
			//--------------------------------------------------------------------------------------
			//Solo se entra si hay que pegar
			if(usarPuntos) {
				//Ordenados segun el id de animacionesID. En PlayerIA ya se asignan correctamente a keys_p
				//Orden de relevancia: saltar, agacharse, moverse, pegar
				
				//Se decide pegar, pero si el rival esta agachado se reducen las probabilidades de todo menos de la patada baja
				switch(max) {
				case 3:
					punch2=true;
					break;
				case 6:
					kickl2=true;
					break;
				case 10:
					kickH2=true;
					break;
				case 11:
					special2=true;
					break;
				case 15:
					down2=true;
					golpeBajoPunch=true;
					break;
				case 16:
					down2=true;
					golpeBajoKick=true;
					break;
				}
			}
		}

	}
	
	private void rangePoints() {
		double distance=game.getHandler().getWorld().distancia;
		//Si el movimiento esta dentro de su rango para golpear...
		for (int i = 0; i < pegar.size(); i++) {
		  //Sumo 2 porque importa mucho que este en el rango para hacerlo
	      if(distance<pegar.get(i).getRange() && sumarPunto(3)) {
	    	  //Si es ataque especial lo ponemos más dificil para que no abuse de él
	    	  if(i==3) {
	    		  if(sumarPunto(5)) {
	    			  movesPoints[pegar.get(i).getId()]+=2;
	    		  }
	    	  }
	    	  //Si no es ataque especial...
	    	  else {
	    		  movesPoints[pegar.get(i).getId()]+=2;
	    	  }
	    	  
	      }
	     //Al igual que un humano la IA puede hacer un movimiento sin sentido (pegar sin estar a rango)
	      else if(distance>pegar.get(i).getRange() && sumarPunto(9)) {
	    	  movesPoints[pegar.get(i).getId()]++;
	      }
		}
	}
	
	//Solo sumar puntos si en el caso del rango se ha posibilitado dicho movimiento
	private void riskPoints() {
		double life=0;
		if(cpuPlayer==1) {
			life=game.getHandler().getWorld().getEntityManager().getPlayer_1().getVida();
		}
		else if(cpuPlayer==2) {
			life=game.getHandler().getWorld().getEntityManager().getPlayer_2().getVida();
		}
	
		life=life*100/500; //porcentaje de vida que le queda (0 a 100)
		
		for (int i = 0; i < pegar.size(); i++) {
		  //Si voy bien de vida igual me la juego a pegar... (y si en rangePoints ya se le ha dado puntos)
	      if(life>pegar.get(i).getRisk() && sumarPunto(pegar.get(i).getRisk()/10) && movesPoints[i]>0) {
	    	  movesPoints[pegar.get(i).getId()]++;
	      }
	      //Si voy mal de vida es raro que me la juegue a ese golpe pero podría ocurrir...
	      else if(life<pegar.get(i).getRisk() && sumarPunto(pegar.get(i).getRisk()/10+2) && movesPoints[i]>0) {
	    	  movesPoints[pegar.get(i).getId()]++;
	      }
		}
	}
	
	private void bonusRivalCrouch() {
		Player player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		if(cpuPlayer==1) {
			player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		}
		else if(cpuPlayer==2) {
			player=game.getHandler().getWorld().getEntityManager().getPlayer_1();
		}
		
		if(player.getAnimacionActual()==2 || player.getAnimacionActual()==18) {
			movesPoints[pegar.get(1).getId()]+=2;
			movesPoints[pegar.get(4).getId()]+=2;
			movesPoints[pegar.get(5).getId()]+=2;
		}
		
	}
	
	
	private boolean walk(int aggressive, int dificultad) {
		int right=0;
		int left=0;
		double distance=game.getHandler().getWorld().distancia;
		boolean orientation=false;
		double lifeCPU=0;
		double lifePlayer=0;
		
		if(cpuPlayer==1) {
			orientation=game.getHandler().getWorld().getEntityManager().getPlayer_1().getOrientacion();
			lifeCPU=game.getHandler().getWorld().getEntityManager().getPlayer_1().getVida();
			lifeCPU=lifeCPU*100/500;
			lifePlayer=game.getHandler().getWorld().getEntityManager().getPlayer_2().getVida();
			lifePlayer=lifePlayer*100/500;
		}
		else if(cpuPlayer==2) {
			orientation=game.getHandler().getWorld().getEntityManager().getPlayer_2().getOrientacion();
			lifeCPU=game.getHandler().getWorld().getEntityManager().getPlayer_2().getVida();
			lifeCPU=lifeCPU*100/500;
			lifePlayer=game.getHandler().getWorld().getEntityManager().getPlayer_1().getVida();
			lifePlayer=lifePlayer*100/500;
		}
		
		//Mirando a la derecha
		if(orientation) {
			if(distance>200 && sumarPunto(dificultad)) {
					right++;
				
			}
			
			if(lifeCPU>30 && sumarPunto(dificultad)) {
				right++;
			}
			else if(sumarPunto(3)){
				left++;
			}
			
			if(lifePlayer<30 && sumarPunto(dificultad)) {
				right++;
			}
			else if(sumarPunto(3)){
				left++;
			}
			
		}
		//Mirando hacia la izquierda
		else {
			if(distance>200 && sumarPunto(dificultad)) {
				left++;
			}
	
			
			if(lifeCPU>30 && sumarPunto(dificultad)) {
				left++;
			}
			else if(sumarPunto(3)){
				right++;
			}
			
			if(lifePlayer<30 && sumarPunto(dificultad)) {
				left++;
			}
			else if(sumarPunto(3)){
				right++;
			}
		
		}
		
		//En caso de empate alejarse del rival (Defensivo) (Se está más tiempo así)
		if(aggressive<60) {
			if(orientation) {
				return right>left;
			}
			else {
				return right>=left;
			}
		}
		//En caso de empate acercarse al rival (Ofensivo)
		else {
			if(orientation) {
				return right>=left;
			}
			else {
				return right>left;
			}
		}
		
	}
	
	
	
	
	public void esquivarSaltando() {
		
		//Saltar Hadouken
		hadouken=game.getHandler().getWorld().getEntityManager().getPlayer_1().getHadouken();
		xIA=game.getHandler().getWorld().getEntityManager().getPlayer_2().getX();
		//Saltar porque rival agachado
		Player player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		double distanciaFighters=game.getHandler().getWorld().distancia;
		
		if(cpuPlayer==1) {
			hadouken=game.getHandler().getWorld().getEntityManager().getPlayer_2().getHadouken();
			xIA=game.getHandler().getWorld().getEntityManager().getPlayer_1().getX();
			player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		}
		else if(cpuPlayer==2) {
			hadouken=game.getHandler().getWorld().getEntityManager().getPlayer_1().getHadouken();
			xIA=game.getHandler().getWorld().getEntityManager().getPlayer_2().getX();
			player=game.getHandler().getWorld().getEntityManager().getPlayer_1();
		}
		
		//HADOUKEN--------------------------------------------------------------------------
		if(!hadouken.getActivo()) {
			primeraVez=true;
		}
		
		
		//Si viene el hadouken y no has saltado aun...
		if(hadouken.getActivo() && primeraVez) {
			//Si se lanza hacia la derecha
			if(hadouken.getOrientacion()) {
				//Si aun no te ha sobrepasado y esta cerca se salta
				if((hadouken.getX() < xIA ) && (xIA-hadouken.getX()<250)){
					saltar=true;
					primeraVez=false;
					golpeAereo1=false;
				}
					
			}
			//Si se lanza hacia la izquierda
			else {
				float distancia=0;
				//Si CPU en posicion negativa (muy a la izquierda)
				if(xIA<0) {
					distancia=xIA;
					//Si hadouken va por posicion negativa (muy a la izquierda)
					if(hadouken.getX()<0) {
						distancia+=Math.abs(hadouken.getX());
					}
					else {
						distancia=Math.abs(distancia-hadouken.getX());
					}
					//Si aun no te ha sobrepasado y esta cerca se salta
					if((xIA < hadouken.getX()) && (Math.abs(distancia)<400)){
						saltar=true;
						primeraVez=false;
						golpeAereo1=false;
					}
				}
				else {
					if((xIA < hadouken.getX()) && (hadouken.getX()-xIA<400)){
						saltar=true;
						primeraVez=false;
						golpeAereo1=false;
					}
				}
				
				
			}
		}
		//----------------------------------------------------------------------------------
		
		//Saltar porque enemigo agachado
		else if((player.getAnimacionActual()==2 || player.getAnimacionActual()==18) && distanciaFighters<240) {
			if(dificultad==0 && sumarPunto(4)) {
				saltar=true;
			}
			else if(dificultad==1 && sumarPunto(2)) {
				saltar=true;
			}
			else if(dificultad==2) {
				saltar=true;
			}
			golpeAereo1=true;
		}
		
	}
	
	public void esquivarAgachando() {
		Player player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		double distancia=game.getHandler().getWorld().distancia;
		
		if(cpuPlayer==1) {
			player=game.getHandler().getWorld().getEntityManager().getPlayer_2();
		}
		else if(cpuPlayer==2) {
			player=game.getHandler().getWorld().getEntityManager().getPlayer_1();
		}
		
		//Si el player1 uno lanza punch o kickh...
		if((player.getAnimacionActual()==3 || player.getAnimacionActual()==10) && distancia<250) {
			if(dificultad==0 && sumarPunto(4)) {
				agachar=true;
			}
			else if(dificultad==1 && sumarPunto(2)) {
				agachar=true;
			}
			else if(dificultad==2) {
				agachar=true;
			}
			
		}

	}
	
	private void resetMovesPoints() {
		for(int i=0;i<movesPoints.length;i++) {
			movesPoints[i]=0;
		}
	}
	
	private void resetMoves() {
		up2=false;
		down2=false;
		left2=false;
		right2=false;
		punch2=false;
		kickl2=false;
		kickH2=false;
		special2=false;
		usarPuntos=true;	//En cada tick es true a menos que no se vaya a pegar
	}
	
	//Se suma punto con un (100-risk)% de probabilidades
	private boolean sumarPunto(int risk) {
		double random = Math.floor(Math.random()*10+0);
		return random>risk;
	}

	public ArrayList<Movs> getPegar() {
		return pegar;
	}

	public void setPegar(ArrayList<Movs> pegar) {
		this.pegar = (ArrayList<Movs>) pegar.clone();
	}
	
	public void setFighter(int fighter) {
		this.fighter=fighter;
		//Si la CPU no es Blanka, sustituimos el el especial de blanka por el de RyuChun (mucho mas alcance)
		if(fighter>0) {
			//Borro especial de blanka
			pegar.remove(3);
			pegar.add(3,especialRyuChun);
		}
		//Se quita jump+punch porque se gestiona se gestiona de otra forma (no con puntuaciones)
		pegar.remove(6);
		
		
		//Si CPU vs CPU siempre en dificil
		if(game.mode==3) {
			dificultad=2;
		}
		//Si modo progresivo, la dificultad equivale a la pelea por la que se va
		else if(game.getDificultad()==3) {
			dificultad=game.getNumPelea();
		}
		else {
			dificultad=game.getDificultad();
		}
	}
	
}
