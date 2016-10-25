package elRescate;

import java.util.ArrayList;

public abstract class Robot extends Movible implements TieneEscudo, RadarListener{
	private int nivelEscudo;
	private int nivelEnergia;
	private int cantidadMuniciones;
	private int cantidadBombas;

	private Radar radar;
	private Persona persona;

	protected static final int ENERGIA_DEFAULT = 100;
	protected static final int ESCUDO_DEFUALT = 100;
	private static final int MUNICIONES_DEFAULT = 100;
	private static final int BOMBAS_DEFAULT = 10;

	private static final int ANCHO_ROBOT = 2;
	private static final int ALTO_ROBOT= 2;

	private static final int GASTO_ENERGIA_MOVIMIENTO = 1;
	protected static final double VELOCIDAD_ROBOT = 5;

	/**
	 * Crea un robot con un tamanio fijo en una posicion indicada.
	 * Setea sus valores de cantidad de bombas y municiones a un valor por defecto
	 * @param posicion
	 */
	public Robot(Posicion posicion){
		super(new Tamanio(ANCHO_ROBOT, ALTO_ROBOT), posicion);

		setCantidadBombas(BOMBAS_DEFAULT);
		setCantidadMuniciones(MUNICIONES_DEFAULT);

		this.setNivelEscudo(ESCUDO_DEFUALT);
		this.setNivelEnergia(ENERGIA_DEFAULT);

		this.radar = new Radar(this.getPos(),this.getDireccion());
		this.radar.addRadarListener(this);
	}

	public void setCantidadMuniciones(int cantidadMuniciones){
		this.cantidadMuniciones = cantidadMuniciones;
		if(this.cantidadMuniciones <= 0)
			this.cantidadMuniciones = 0;
	}

	/**
	 * Setea cantidad de bombas del robot
	 * @param cantidadBombas
	 */
	public void setCantidadBombas(int cantidadBombas){
		this.cantidadBombas = cantidadBombas;
		if(this.cantidadBombas <= 0)
			this.cantidadBombas = 0;
	}

	/**
	 * Devuelve cantidad de municiones del robot
	 * @return
	 */
	public int getCantidadMuniciones(){
		return this.cantidadMuniciones;
	}

	/**
	 * Devuelve cantidad de bombas del robot
	 * @return
	 */
	public int getCantidadBombas(){
		return this.cantidadBombas;
	}


	/**
	 *
	 * @return referencia al equipo al que pertence
	 */
	public abstract Equipo getEquipo();



	/**
	 * Setea nivel de escudo del robot
	 * @param nivelEscudo
	 */
	public void setNivelEscudo(int nivelEscudo){
		this.nivelEscudo = nivelEscudo;
		if (this.nivelEscudo <= 0){
			this.setExiste(false);
		}
	}

	/**
	 * Setea nivel de energia del robot
	 * @param nivelEnergia
	 */
	public void setNivelEnergia(int nivelEnergia){
		this.nivelEnergia = nivelEnergia;
		if(this.nivelEnergia < 0)
			this.nivelEnergia = 0;
	}

	/**
	 * Devuelve nivel de escudo del robot
	 * @return
	 */
	public int getNivelEscudo(){
		return this.nivelEscudo;
	}

	/**
	 * Devuelve nivel de energia del robot
	 * @return Nivel de Energía del Robot
	 */
	public int getNivelEnergia(){
		return this.nivelEnergia;
	}

	public Radar getRadar(){
		return this.radar;
	}

	@Override
	public void avanzar(double velocidad) {
		super.avanzar(velocidad);
		//se actualiza la posición del radar
		this.radar.setPos(this.getPos());

		//Preguntamos si tiene una persona cargada para controlar el gasto de energia
		if(this.llevandoPersona())
			this.setNivelEnergia(this.getNivelEnergia() - GASTO_ENERGIA_MOVIMIENTO);
		else
			this.setNivelEnergia(this.getNivelEnergia() - (GASTO_ENERGIA_MOVIMIENTO * 2));

	}

	/**
	 * Lanza una bomba en la dirección en la
	 * que apunta el radar. Ésta es agregada al
	 * escenario
	 */
	public void lanzarBomba(){
		if(this.getCantidadBombas() > 0){
			Escenario.getEscenario().agregarElemento(new Bomba(
					new Posicion(this.getPos().getX(), this.getPos().getY()),
					this,
					this.getRadar().getDireccion()));
			//Disminuimos cantidad de bombas
			this.setCantidadBombas(this.getCantidadBombas() - 1);
		}
	}

	/**
	 * Dispara una munición en la dirección en la
	 * que apunta el radar. Ésta es agregada al
	 * escenario
	 */
	public void dispararMunicion(){
		//disparamos la munición
		if(this.getCantidadMuniciones() > 0){

			Escenario.getEscenario().agregarElemento(new Municion(
					new Posicion(this.getPos().getX(), this.getPos().getY()),
					this,
					this.getRadar().getDireccion()));

			/*
			Escenario.getEscenario().agregarElemento(new Municion(
					this.getPos(),
					this,
					this.getRadar().getDireccion()));*/

			//Disminuimos cantidad de municiones
			this.setCantidadMuniciones(this.getCantidadMuniciones() - 1);
		}

	}

	@Override
	public void chocarElemento(Elemento elem) {

		//Si es una municion
		if(elem instanceof Municion){
			//lo casteo
			Municion muni = (Municion) elem;
			//si no es de mi equipo, me genero el daño
			if(!(this.getEquipo().getElementos().contains(muni.getDuenio())))
				this.setNivelEscudo(this.getNivelEscudo() - muni.getDanio());
		} //si es una bomba
		else if(elem instanceof Bomba){
			//casteo
			Bomba bomb = (Bomba) elem;
			//verifico equipo y me aplico el daño
			if(!(this.getEquipo().getElementos().contains(bomb.getDuenio())))
				this.setNivelEscudo(this.getNivelEscudo() - bomb.getDanio());
		}
		else if (elem instanceof Bonus){
			((Bonus)elem).darBonus(this);
		}

		/*
		 * estos else if son a modo de ejemplo para mostrar
		 * que cuando un robot se choca contra la ZonaRescate
		 * o contra un refugio va a mostrar un mensaje
		 */
		else if(elem instanceof ZonaRescate){
			System.out.println("Rescate!");
		} else if(elem instanceof Refugio){
			System.out.println("Dejo");
		}

	}



	/**
	 * Rescata una persona de un refugio o zona de rescates
	 * @param persona
	 */
	public void cargarPersona(Persona persona){
		if (!(this.llevandoPersona()))
			this.persona = persona;
	}

	/**
	 * Indica si se esta llevando una persona o no
	 * @return True si se tiene una persona cargada, falso si no
	 */
	public boolean llevandoPersona(){
		return this.persona != null;
	}


	public Persona entregarPersona(){
		Persona p = this.persona;
		this.persona = null;
		return p;
	}

	/**
	 * Comportamiento del robot (GENERICO) en su turno
	 */
	@Override
	public void jugar() {
		//Si no se encuentra algo que todos los robots van a hacer
		//hacer este metodo abstracto
		this.getRadar().escanear();

		this.avanzar(VELOCIDAD_ROBOT);
	}

	/**
	 * Dado un elemento, orienta el Robot hacia ese
	 * elemento
	 * @param elemento elemento al que se debe apuntar
	 * @return ángulo en el cual posicionarse
	 */
	public void orientar(Elemento elemento){

		//vector de referencia (1,0)
		double xo = 1;
		double yo = 0;

		//vector de coordenadas hacia donde queremos orientar el robot
		double xd = elemento.getPos().getX()-this.getPos().getX();
		double yd = elemento.getPos().getY() - this.getPos().getY();

		//calculamos angulos absolutos de ambos vectores
		double origen = Math.toDegrees(Math.atan2(xo,yo));
		if(origen < 0){
			origen +=360;
		}

		double destino = Math.toDegrees(Math.atan2(xd,yd));
		if(destino < 0){
			destino += 360;
		}

		//la orientación final es la diferencia entre ambos angulos
		double orientacion = destino - origen;
		if(orientacion < 0){
			orientacion += 360;
		}

		this.setDireccion(orientacion);

	}


	@Override
	public void elementosDetectado(ArrayList<Elemento> elementos){
		elementos.remove(this);
	}


}