package elRescate;
/**
 * Bomba a ser lanzada
 *
 */
public class Bomba extends Movible {
	private int danio;
	
	//tamaño de la bomba
	private static final int ANCHO_BOMBA = 3;
	private static final int ALTO_BOMBA = 3;
	
	//velocidad inicial de la bomba
	private static final double VELOCIDAD_BOMBA = 10.0;
	
	//daño de la bomba
	private static final int DANIO_BOMBA = 10;
	
	/**
	 * Crea una bomba, y la lanza direccion igual a la que apuntaba quién la lanzó
	 * @param posicion Posicion de su lanzante
	 */
	public Bomba(Posicion posicion){
		super(new Tamanio(ANCHO_BOMBA, ALTO_BOMBA), posicion);
		this.setVelocidad(VELOCIDAD_BOMBA);
		this.setDanio(DANIO_BOMBA);
	}
	
	/**
	 * Cambiar el daño que produce la bomba
	 * @param danio Daño que produce la bomba
	 */
	public void setDanio(int danio){
		this.danio = danio;
	}
	
	/**
	 * Forma particular de avanzar de la bomba
	 */
	@Override
	public void avanzar() {
		//conseguimos la posicion de la bomba
		Posicion posicionBomba = this.getPos();
		
		//modifico la posicion utilizando los deltas
		posicionBomba.setX((int)(posicionBomba.getX() +  this.deltaY()));
		posicionBomba.setY((int)(posicionBomba.getY() + this.deltaX()));

		//le doy la posicion que manipule
		this.setPos(posicionBomba);
		this.setVelocidad(this.getVelocidad() - 1);
		//TODO qué hacer cuando la bomba explta¡????
	}
	
	
}
