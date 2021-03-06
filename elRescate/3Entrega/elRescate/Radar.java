package elRescate;
import java.awt.Polygon;
import java.util.ArrayList;


public class Radar{
	private int anguloApertura;
	private double alcance;
	
	private Elemento duenio;
	
	private static int incrementoGradosPoligono = 5;
	
	//dirección del radar
	private double direccion;
	
	//ArrayList de los listeners
	private ArrayList<RadarListener> listeners;
	
	
	/**
	 * Agregamos a un escuchador del Radar
	 * @param listener objeto que escucha al radar
	 */
	public void addRadarListener(RadarListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * Quitamos al objeto escuchador
	 * @param listener Escuchador a remover
	 */
	public void removeRadarListener(RadarListener listener){
		this.listeners.remove(listener);
	}
	
	/**
	 * Crea un radar en una posición determinada (dependiente del satélite/robot)
	 * @param posicion
	 */
	public Radar(Elemento duenio, double direccion){
		/*
		 * Con este constructor limitamos a que nos pasen
		 * la posicion y el tamaño del elemento que posee
		 * el radar (robot o satelite)
		 */
		this.setDuenio(duenio);
		this.setAnguloApertura(30);
		this.direccion = direccion;
		this.listeners = new ArrayList<RadarListener>();
		this.setAlcance(50);
	}
	
	
	/**
	 * Setea angulo de apertura del radar
	 * @param angulo
	 */
	public void setAnguloApertura(int angulo){
		this.anguloApertura = angulo;
		this.setAlcance(angulo);
	}
	
	/**
	 * Devuelve angulo de apertura del radar
	 * @return
	 */
	public int getAnguloApertura(){
		return this.anguloApertura;
	}
	
	public void setDuenio(Elemento duenio) {
		this.duenio = duenio;
	}
	
	public Elemento getDuenio() {
		return duenio;
	}
	
	/**
	 * Devuelve alcance del radar
	 * @return
	 */
	public double getAlcance(){
		return this.alcance;
	}
	
	/**
	 * Setea alcance del radar
	 * @param angulo
	 */
	private void setAlcance(double alcance){
		//TODO PONER UNA FORMULA DE ALCANCE COHERENTE!!!!
		//acá vemos qué hacemos, por ahora lo dejamos así
		this.alcance = alcance;
	}
	
	/**
	 * Setea la dirección en la que apunta el radar
	 * @param direccion Nueva dirección
	 */
	public void setDireccion(double direccion){
		this.direccion = direccion % 360;
	}
	
	/**
	 * @return Direccion a la que apunta el radar
	 */
	public double getDireccion(){
		return this.direccion;
	}
	
	
	/**
	 * Dado un elemento, orienta el radar en esa
	 * direccion
	 * @param elemento elemento al que se debe apuntar
	 * @return ángulo en el cual posicionarse
	 */
	public void apuntar(Elemento elemento){
		
		//vector de referencia (1,0)
		double xo = 1;
		double yo = 0;

		//vector de coordenadas hacia donde queremos apuntar el radar
		double xd = elemento.getPos().getX() - this.getDuenio().getPos().getX();
		double yd = elemento.getPos().getY() - this.getDuenio().getPos().getY();

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
	

	
	/**
	 * Radar escanea su area
	 */
	public void escanear(){
		//System.out.println("Radar escaneando...");
		
		//Armar poligono
		Polygon zonaBarrida = this.armarPoligono(this.getDuenio().getPos().getX(), this.getDuenio().getPos().getY());
		
		//le pido al escenario que me diga si hay elementos dentro del poligono
		ArrayList<Elemento> elementosDetectados = Escenario.getEscenario().enArea(zonaBarrida);
		
		//elemino de la lista al radar en sí
		elementosDetectados.remove(this);
		
		//Si el radar detecto al menos un elemento, le avisa a sus oyentes (listeners)
		if(!elementosDetectados.isEmpty())
			for(RadarListener listener : this.listeners){
				listener.elementosDetectado(elementosDetectados);
			}
	}

	/**
	 * Armado del poligono usado para la detección del radar
	 * @param x coordenada x del radar
	 * @param y coordenada y del radar
	 * @return Poligono de alcance del radar
	 */
	private Polygon armarPoligono(int x, int y) {
		
		/* 
		 * pido el angulo y lo divido para poder barrer
		 * con una mitad del angulo para un lado y mitad
		 * para el otro
		 */
		double angulo = this.getAnguloApertura() / 2;
		
		/* 
		 * calculo la cantidad de puntos 
		 * uno mas para que incluya la posicion del radar
		 * */
		int cantidadPuntos = 2 + (int)(this.getAnguloApertura() / incrementoGradosPoligono);
		
		
		//creo los arreglos para pasale al poligono
		int[] xPuntos = new int[cantidadPuntos];
		int[] yPuntos = new int[cantidadPuntos];
		
		//le agrego la posicion del radar
		xPuntos[0] = x;
		yPuntos[0] = y;
		xPuntos[1] = x+(int)Movible.deltaX(this.getAlcance(), this.getDireccion());
		yPuntos[1] = y-(int)Movible.deltaY(this.getAlcance(), this.getDireccion());
		
		//voy poniendo en el arreglo los puntos calculados con los deltas
		for (int i = 2; i< cantidadPuntos; i++){
			xPuntos[i] = x+(int)Movible.deltaX(this.getAlcance(), angulo);
			yPuntos[i] = y-(int)Movible.deltaY(this.getAlcance(), angulo);
			angulo += incrementoGradosPoligono;
		}
		
		//devuelvo el poligono ya armado
		return new Polygon(xPuntos, yPuntos, cantidadPuntos);
	}

}
