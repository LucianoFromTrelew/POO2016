package elRescate;

import java.util.ArrayList;

public class SateliteSoldadoRyan extends Satelite{
	
	public SateliteSoldadoRyan(Posicion posicion) {
		super(posicion);
	}
	
	@Override
	public void jugar() {
		//que podria hacer??
		super.jugar();
	}
	
	@Override
	public void elementosDetectado(ArrayList<Elemento> elementos) {
		super.elementosDetectado(elementos);
		for(Elemento e : elementos){
			if((e instanceof Bomba) || 
			  ((e instanceof BonusEscudo) && (this.getNivelEscudo() < (ESCUDO_INICIAL * 0.4)))){
				/*Si detecta una bomba o si detecta un bonus de escudo 
				 * y tiene menos del 40% del escudo inicial, apunta y dispara*/
				
				//el apuntar deberia ser de Radar, o de Elemento de ultima
				//this.apuntar(e);
				this.getRadar().apuntar(e);
				this.dispararMunicion();
			}
		}
	}
	
	@Override
	public Equipo getEquipo() {
		return EquipoSoldadoRyan.getEquipo();
	}
}
