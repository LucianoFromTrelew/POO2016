package elRescate;

import java.util.ArrayList;

public abstract class Satelite extends Elemento implements TieneEscudo, RadarListener {


	private Radar radar;
	private int nivelEscudo;
	private int cantidadMuniciones;
	



	/**
	 * Crea un satélite con un tamanio fijo en la posición indicada. Tiene un nivel de escudo inicial predeterminado
	 * @param posicion
	 */
	public Satelite(Posicion posicion){
		super(new Tamanio(Config.ANCHO_SATELITE, Config.ALTO_SATELITE), posicion);
		this.radar = new Radar(this.getPos(),0);
		this.radar.addRadarListener(this);
		
		this.setNivelEscudo(Config.ESCUDO_INICIAL);
		this.setCantidadMuniciones(Config.MUNICIONES_DEFAULT);
		
	}
	
	
	/**
	 * Setea nivel de escudo
	 * @param nivelEscudo
	 */
	public void setNivelEscudo(int nivelEscudo){
		this.nivelEscudo = nivelEscudo;
		if (this.nivelEscudo <= 0){
			this.setExiste(false);
		}
	}
	
	/**
	 * Devuelve el nivel de escudo del satélite
	 * @return
	 */
	public int getNivelEscudo(){
		return this.nivelEscudo;
	}
	
	/**
	 * 
	 * @return cantidad de municiones del satelite
	 */
	public int getCantidadMuniciones(){
		return this.cantidadMuniciones;
	}

	/**
	 * 
	 * @param cantMuniciones cantidad de municiones a setear en el satelite
	 */
	public void setCantidadMuniciones(int cantMuniciones){
		this.cantidadMuniciones = cantMuniciones;
		if(this.cantidadMuniciones < 0)
			this.cantidadMuniciones = 0;
	}
	
	/**
	 * 
	 * @return referencia al radar del satelite
	 */
	public Radar getRadar(){
		return this.radar;
	}
	
	/**
	 * @return referencia al equipo al que pertence
	 */
	public abstract Equipo getEquipo();
	
	
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
			//Disminuimos cantidad de municiones
			this.setCantidadMuniciones(this.getCantidadMuniciones() - 1);

		}
		
	}
	
	
	@Override
	public void chocarMunicion(Municion municion) {
		super.chocarMunicion(municion);
		
		if(!(this.getEquipo().getElementos().contains(municion.getDuenio())))
			this.setNivelEscudo(this.getNivelEscudo() - municion.getDanio());
		
	}
	
	@Override
	public void chocarBomba(Bomba bomba) {
		super.chocarBomba(bomba);
		if(!(this.getEquipo().getElementos().contains(bomba.getDuenio())))
			this.setNivelEscudo(this.getNivelEscudo() - bomba.getDanio());
	}
	
	@Override
	public void chocarElemento(Elemento elem) {
		elem.chocarSatelite(this);
	}

	@Override
	public void jugar() {
		//Si no se encuentra algo que todos los satelites van a hacer
		//hacer este metodo abstracto
		this.getRadar().escanear();
	}
	
	/**
	 * Procesa los elementos detectados por el radar
	 * @param elementos elementos que detectó el radar
	 */
	@Override
	public void elementosDetectado(ArrayList<Elemento> elementos) {
		//eliminar satelite de la lista
		elementos.remove(this);
		
		/*
		 * Procesar elementos detectados
		 */
	}
	
	/**
	 * Muestra las stats del satelite
	 */
	@Override
	public String toString() {
		
		return String.format(
				"%s\n"
				+ "Municiones: %d\n"
				+ "Nivel Escudo: %d\n",
				super.toString(), 
				this.getCantidadMuniciones(),
				this.getNivelEscudo());
	}
}
