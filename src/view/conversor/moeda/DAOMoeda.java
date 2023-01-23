package view.conversor.moeda;

import java.util.Map;

import metodos.ConversorMoeda;

public class DAOMoeda {
	/**
	 * atualiza/atribui os dados a clase DTOMoeda, mudando os valores da moeda.
	 * @author Eric Carvalho
	 * @param dtoMoeda objetos da moeda.
	 * @param siglaMoeda sigla da moeda.
	 * */
	public static void atribuiDadosMoeda(DTOMoeda dtoMoeda, String siglaMoeda) {
		Map<String, Object> mapMoedas = ConversorMoeda.retornaListaMoedas(siglaMoeda); // coleta a lista de moeda com o nome e valor.
		dtoMoeda.setMapMoedas(mapMoedas); // transfere os dados para o objeto mapMoedas do dtoMoeda.
		
		String[] chaves = ConversorMoeda.retornaChavesMap(mapMoedas); // reotrna as chaves (siglas) das moedas.
		chaves = ConversorMoeda.retornaMoedas(chaves); // pega o sigla, simbolo e nome da moeda, formando para ficar visualmente melhor para o usuario.
		dtoMoeda.setMoedas(chaves); // transfere os dados para o objeto moedas do dtoMoeda.
		
		double[] valoresMoedas = ConversorMoeda.retornaValoresMap(mapMoedas); // coleta e retorna os valores de cada moeda.
		dtoMoeda.setValoresMoedas(valoresMoedas); // transfere os dados para o objeto valores do dtoMoeda.
	}
}
