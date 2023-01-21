package view.conversor;

import java.util.Map;

import metodos.ConversorMoeda;

public class DTOMoeda {
	private Map<String, Object> mapMoedas;
	private String[] moedas;
	private double[] valoresMoedas;
	
	public double[] getValoresMoedas() {
	    return valoresMoedas;
	}
	
	public void setValoresMoedas(double[] valoresMoedas) {
	    this.valoresMoedas = valoresMoedas;
	}
	
	public String[] getMoedas() {
	    return moedas;
	}
	
	public void setMoedas(String[] moedas) {
	    this.moedas = moedas;
	}
	
	public Map<String, Object> getMapMoedas() {
	    return mapMoedas;
	}
	
	public void setMapMoedas(Map<String, Object> mapMoedas) {
	    this.mapMoedas = mapMoedas;
	}
}
