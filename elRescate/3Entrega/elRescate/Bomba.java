package elRescate;

import javax.swing.JOptionPane;

/**
 * Bomba a ser lanzada
 *
 */
public class Bomba extends Movible {
	
	private int danio;
	
	private Elemento duenio;
	
	//si la bomba ha explotado
	private boolean explotada;
	
	/**
	 * Crea una bomba, y la lanza direccion igual a la que apuntaba quién la lanzó
	 * @param posicion Posicion de su lanzante
	 */
	public Bomba(Posicion posicion, Elemento duenio, double direccion){
		super(new Tamanio(Config.ANCHO_BOMBA, Config.ALTO_BOMBA), posicion);
		this.setVelocidad(Config.VELOCIDAD_BOMBA);
		this.setDanio(Config.DANIO_BOMBA);
		this.setDireccion(direccion);
		this.explotada = false;
		
		this.duenio = duenio;
	}
	
	/**
	 * @param danio Daño que produce la bomba
	 */
	public void setDanio(int danio){
		this.danio = danio;
	}
	
	/**
	 * 
	 * @return Daño que produce la bomba
	 */
	public int getDanio(){
		return this.danio;
	}
	
	/**
	 * 
	 * @return duenio de la bomba
	 */
	public Elemento getDuenio() {
		return this.duenio;
	}
	//a
	
	
	/**
	 * Forma particular de avanzar de la bomba. A medida que avanza, va perdiendo velocidad
	 */
	@Override
	public void avanzar() {
		//si la bomba todavía posee velocidad
		if(this.getVelocidad() > 1){
			super.avanzar();
			this.setVelocidad(this.getVelocidad() - 0.1);
		}
		else{
			//si se quedó sin velocidad, tiene que explotar
			this.explotar();
		}
	}

	/**
	 * Explosión de la bomba, esta agranda su tamaño
	 * y afecta a los elementos con los que colisione
	 */
	public void explotar() {
		//conseguimos su tamaño actual
		if(!this.haExplotado()){
			this.setTam(new Tamanio(Config.ALTO_BOMBA*10, Config.ANCHO_BOMBA*10));
			this.explotada = true;
		}
	}
	
	/**
	 * Qué realiza la bomba en un turno
	 */
	public void jugar(){
		this.avanzar();
		if(this.haExplotado())
			this.destruir();
	}
	
	/**
	 * Inidica si la bomba explotó o no
	 * @return true si la bomba esta explotando
	 */
	public boolean haExplotado(){
		return this.explotada;
	}
	
	
	@Override
	public void chocarBonus(Bonus bonus) {
		super.chocarBonus(bonus);
		
		bonus.darBonus(this.getDuenio());
	}
	/**
	 * Comportamiento de la bomba al chocar con un elemento
	 * @param elem Elemento con el que chocó
	 */
	@Override
	public void chocarElemento(Elemento elem) {
		elem.chocarBomba(this);
		//si se choca con quien la lanzo, no explota
		if (elem != this.getDuenio()){
			this.explotar();
		}
		
	}
}
