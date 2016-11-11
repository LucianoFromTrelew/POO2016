package elRescate;

public class Tamanio {
	
	//Constantes para definir un máximo y ancho que van a tener todos los elementos
//	public static final int MAX_ANCHO = Posicion.MAX_X;
//	public static final int MIN_ANCHO = 1;
//	public static final int MAX_ALTO = Posicion.MAX_Y;
//	public static final int MIN_ALTO = 1;
	
	private int ancho = Config.MIN_ANCHO;
	private int alto = Config.MIN_ALTO;
	
	/**
	 * Devuelve el ancho de un elemento
	 * @return el ancho del elemento
	 */
	public int getAncho(){
		return ancho;
	}
	
	/**
	 * Devuelve el alto de un elemento
	 * @return el alto del elemento
	 */
	public int getAlto(){
		return alto;
	}
	
	/**
	 * Setea el ancho de un elemento. Si el parámetro está fuera de rango, se setea al mínimo valor.
	 * @param ancho valor del ancho a setear
	 */
	public void setAncho(int ancho){
		if(ancho >= Config.MIN_ANCHO && ancho <= Config.MAX_ANCHO)
			this.ancho = ancho;
	}

	/**
	 * Setea el alto de un elemento. Si el parámetro está fuera de rango, se setea al mínimo valor.
	 * @param alto
	 */
	public void setAlto(int alto){
		if(alto >= Config.MIN_ALTO && alto<= Config.MAX_ALTO)
			this.alto = alto;
	}
	
	/**
	 * Crea un objeto Tamanio. Recibe un valor de ancho y otro de tamanio
	 * @param ancho
	 * @param alto
	 */
	public Tamanio(int ancho, int alto){
		this.setAlto(alto);
		this.setAncho(ancho);
	}
	
	/**
	 * Sin parámetros. Setea a los valores mínimos
	 */
	public Tamanio(){
		this(Config.MIN_ANCHO, Config.MIN_ALTO);
	}
}
