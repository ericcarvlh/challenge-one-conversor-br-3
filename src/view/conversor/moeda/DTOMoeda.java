package view.conversor.moeda;

import java.util.Map;

import metodos.ConversorMoeda;

public class DTOMoeda {
	private Map<String, Object> mapMoedas; // criaando o objeto mapMoedas que mapeia os dados com uma chave (String) e o valor (Objeto).
	private String[] moedas;  // criando o objeto moeda que é um array responsável por armazenar as moedas com seus respectivas siglas, simboloes e nome (se houver).
	private double[] valoresMoedas; // criando o objeto valoresMoedas que é responsável por armazenar os valores de cada moeda.
	
	/**
	 * Retorna o valor do objeto valoresMoedas.
	 * @author Eric Carvalho@ 
	 * */
	public double[] getValoresMoedas() { 
	    return valoresMoedas;
	}
	
	/**
	 * Muda o valor do objeto valoresMoedas.
	 * @author Eric Carvalho
	 * @param valoresMoedas é a variável que contém os novos valores substitutos.
	 * */
	public void setValoresMoedas(double[] valoresMoedas) {
	    this.valoresMoedas = valoresMoedas;
	}
	
	/**
	 * Retorna o valor do objeto moedas.
	 * @author Eric Carvalho@ 
	 * */
	public String[] getMoedas() {
	    return moedas;
	}
	
	/**
	 * Muda o valor do objeto moedas.
	 * @author Eric Carvalho
	 * @param moedas é a variável que contém os novos valores substitutos.
	 * */
	public void setMoedas(String[] moedas) {
	    this.moedas = moedas;
	}
	
	/**
	 * Retorna o valor do objeto mapMoedas.
	 * @author Eric Carvalho@ 
	 * */
	public Map<String, Object> getMapMoedas() {
	    return mapMoedas;
	}
	
	/**
	 * Muda o valor do objeto mapMoedas.
	 * @author Eric Carvalho
	 * @param mapMoedas é a variável que contém os novos valores substitutos.
	 * */
	public void setMapMoedas(Map<String, Object> mapMoedas) {
	    this.mapMoedas = mapMoedas;
	}
}
