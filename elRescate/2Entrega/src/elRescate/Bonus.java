package elRescate;

import java.util.Random;

/**
 * 
 * Bonus que otorga beneficios al que lo recoge
 *
 */

public abstract class Bonus extends Elemento{
	//Tamanio del bonus - predeterminado
	protected static final int ANCHO_BONUS = 3;
	protected static final int ALTO_BONUS = 3;
	
	private static int turnoAparicion;
	
	//tiempo de vida del bonus
	private int tiempoVida;
	
	
	/**
	 * @return Tiempo de vida del Bonus
	 */
	public int getTiempoVida() {
		return tiempoVida;
	}

	/**
	 * Le da un valor al tiempo de vida del bonus
	 * @param tiempoVida
	 */
	public void setTiempoVida(int tiempoVida) {
		this.tiempoVida = tiempoVida;
	}
	
	public abstract void darBonus(Elemento elem);
	
	//TODO crear bonus es posicion libre del escenario
	public Bonus(Posicion pos){
		super(new Tamanio(ANCHO_BONUS, ALTO_BONUS), pos);
		//creamos contenedor del numero aleatorio
		Random random = new Random();
		//asignamos un nuevo valor de aparición para el bonus
		turnoAparicion = random.nextInt((20-10)+1) + 10;
	}
	
	@Override
	public void chocarElemento(Elemento elem){
		if(elem instanceof Municion){
			Municion muni = (Municion) elem;
			this.darBonus(muni.getDuenio());			
		}
		else if (elem instanceof Bomba){
			Bomba bomb = (Bomba) elem;
			this.darBonus(bomb.getDuenio());			
		}
		else if (elem instanceof Robot){
			Robot robot = (Robot) elem;
			this.darBonus(robot);
		}
		this.setExiste(false);
	}


}
