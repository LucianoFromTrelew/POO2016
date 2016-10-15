package elRescate;

import java.util.ArrayList;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * 
 * Clase administradora del juego
 * Sigue un patron SINGLETON
 */

public class Escenario{
	//lista de elementos
	private ArrayList<Elemento> elementos;
	private static Escenario escenario;
	private Tamanio tamanioEscenario;
	/** 
	 * @return devuelve el escenario de juego
	 */
	public static Escenario getEscenario(){
		if(escenario == null){
			escenario = new Escenario(new Tamanio(Posicion.MAX_X, Posicion.MAX_Y));
		}
		return escenario;
	}
	
	/**
	 * Crea un escenario 
	 * @param tamanio
	 */
	private Escenario(Tamanio tamanio){
		this.crearListaElementos();
		this.tamanioEscenario = tamanio;
	}
	
	/**
	 * Inicia el juego
	 */
	public void iniciarJuego(){
		//crearElementos();
		
		while(true){
			//que jueguen todos
			turnos();
			//ver qué paso
			verificarChoques();
			//quitar los que haya que
			depurarElementos();
			
			mostrarEstado();
			
			
		}
	}
	
	private void mostrarEstado() {
		
		for(int i = 0;i < this.elementos.size(); i++){
			System.out.println(elementos.get(i).toString());
		}
		
		System.out.println("Presione una tecla");
		try{
			System.in.read();
		}catch (IOException e){
			
		}
		
	}

	/**
	 * Se fija si hay elementos para quitar del escenario y los quita
	 */
	private void depurarElementos() {
		Elemento elemento;
		for(int i = 0;i < this.elementos.size(); i++){
			elemento = elementos.get(i);
			//si tiene que ser quitado
			if(!elemento.estaVivo()){
				//se lo quita
				elemento.destruir();
			}
		}
	}
	
			
	/**
	 * Verifica las coliciónes entre elementos de juego
	 */
	private void verificarChoques() {
		Elemento e1, e2;
		for(int i = 0; i < elementos.size(); i++){
			//conseguimos tamaño y posicion de e1
			e1 = elementos.get(i);
			Rectangle rectanguloE1 = this.armarRectangulo(e1);
			
			for(int j = 0; j < elementos.size(); j++){
				//armo rectangulo de e2
				e2 = elementos.get(j);
				Rectangle rectanguloE2 = this.armarRectangulo(e2);
				
				//reviso si se intersectan, si se da, los choco
				if((rectanguloE1.intersects(rectanguloE2))&&(e1!=e2)){
					e1.chocarElemento(e2);
					e2.chocarElemento(e1);
				}
			}
		}
	}
	
	/**
	 * Otorga un turno de juego a cada elemento del escenario
	 */
	public void turnos(){
		
		Elemento elemento;
		for(int i = 0;i < this.elementos.size(); i++){
			elemento = elementos.get(i);
			elemento.jugar();
		}
	}
	
	/**
	 * Crea los elementos del juego
	 */
	private void crearListaElementos(){
		this.elementos = new ArrayList<Elemento>();
	}
	
	/**
	 * Agrega un elemento al escenario
	 * @param elemento Elemento a agregar
	 */
	public void agregarElemento(Elemento elemento){
		this.elementos.add(elemento);
	}
	
	public void quitarElemento(Elemento elemento){
		this.elementos.remove(elemento);
	}
	
	public Tamanio getTamanio(){
		return this.tamanioEscenario;
	}
	
	public ArrayList<Elemento> enArea(Polygon poligono){
		ArrayList<Elemento> listaElementos = new ArrayList<Elemento>();
		for(Elemento e: elementos){
			if(poligono.intersects(armarRectangulo(e))){
				listaElementos.add(e);
			}
		}
		
		return listaElementos;
	}
	
	private Rectangle armarRectangulo(Elemento e){
		Posicion posE = e.getPos();
		Tamanio tamE = e.getTam();
		return new Rectangle(
				tamE.getAlto(), 
				tamE.getAncho(), 
				posE.getX(), 
				posE.getY());
		
		
	}
}