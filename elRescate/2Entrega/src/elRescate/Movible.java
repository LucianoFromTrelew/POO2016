package elRescate;
import java.lang.Math;

public abstract class Movible extends Elemento {
	
	private double direccion;
	
	//velocidad a la que se mueve
	private double velocidad;
	
	//Velocidad por defecto
	private static final double VELOCIDAD_DEFECTO = 10;
	
	
	/**
	 * Constructor que recibe los dos objetos
	 * @param tamanio Objeto Tamaño para crear el elemento
	 * @param posicion objeto que contiene la posicion en donde crear el movible
	 */
	public Movible(Tamanio tamanio, Posicion posicion){
		super(tamanio, posicion);
		this.direccion = 0;
		this.velocidad = 0;
		
		this.setVelocidad(VELOCIDAD_DEFECTO);
	}
	
	/**
	 * Constructor con los parametros sueltos
	 * @param ancho ancho del Movible
	 * @param alto	alto del movible
	 * @param x		coordenada x 
	 * @param y		coordenada y
	 */
	public Movible(int ancho, int alto, int x, int y){
		this(new Tamanio(ancho, alto), new Posicion(x,y));
	}
	
	/**
	 * Realiza el movimiento del Movible
	 */
	public void avanzar(double velocidad){
		//consigo la posicion actual
		Posicion posicionMovible = this.getPos();
		
		//le damos velocidad dada por parametro
		this.setVelocidad(velocidad);
		
		//modifico la posicion utilizando los deltas
		posicionMovible.setX((int)(posicionMovible.getX() +  this.deltaY()));
		posicionMovible.setY((int)(posicionMovible.getY() + this.deltaX()));

		//le doy la posicion que manipule
		this.setPos(posicionMovible); 		
	}
	
	//coseguimos el delta X para la velocidad
	protected double deltaX() {
		return this.getVelocidad() * Math.cos(this.direccion);
	}

	//Conseguimos el delta Y para la velocidad
	protected double deltaY() {
		return this.getVelocidad() * Math.sin(this.direccion);
	}
	
	/**
	 * Devulve la velocidad
	 * @return Velocidad del movible
	 */
	public double getVelocidad(){
		return this.velocidad;
	}
	
	/**
	 * Devuelve la dirección del movible
	 * @return Dirección del Movible
	 */
	public double getDireccion(){
		return this.direccion;
	}
	
	/**
	 * Setea la velocidad
	 * @param velocidad Velocidad a darle al Movible
	 */
	public void setVelocidad(double velocidad){
		this.velocidad = 0;

		if(velocidad > 0){
			this.velocidad = velocidad;
		}
	}
	
	/**
	 * Setea la dirección del Movible
	 * @param direccion Dirección a darle al movible
	 */
	public void setDireccion(double direccion){
		/* De esta mananera si se le da una dirección 
		 * que supere los 360 grados, esta se mantiene 
		 * en el rango de 0 a 360 */
		this.direccion = direccion % 360;
	}
	
}
