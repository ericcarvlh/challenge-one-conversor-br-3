package view.conversor.moeda;

import java.util.Map;

import metodos.ConversorMoeda;

public class DAOMoeda {
	/**
	 * Atualiza/atribui os dados a clase DTOMoeda, mudando os valores da moeda se houver resposta.
	 * @author Eric Carvalho
	 * @param dtoMoeda objetos da moeda.
	 * @param siglaMoeda sigla da moeda.
	 * @return retorna verdadeiro se houver uma resposta válida
	 * */
	public static boolean atribuiDadosMoeda(DTOMoeda dtoMoeda, String siglaMoeda) {
		Map<String, Object> mapMoedas = ConversorMoeda.retornaListaMoedas(siglaMoeda); // coleta a lista de moeda com o nome e valor.
		
		if (mapMoedas != null) { // se o tamanho do mapMoedas for diferente de 0, então há dados
			dtoMoeda.setMapMoedas(mapMoedas); // transfere os dados para o objeto mapMoedas do dtoMoeda.
			
			String[] chaves = ConversorMoeda.retornaChavesMap(mapMoedas); // reotrna as chaves (siglas) das moedas.
			chaves = ConversorMoeda.retornaMoedas(chaves); // pega o sigla, simbolo e nome da moeda, formando para ficar visualmente melhor para o usuario.
			dtoMoeda.setMoedas(chaves); // transfere os dados para o objeto moedas do dtoMoeda.
			
			double[] valoresMoedas = ConversorMoeda.retornaValoresMap(mapMoedas); // coleta e retorna os valores de cada moeda.
			dtoMoeda.setValoresMoedas(valoresMoedas); // transfere os dados para o objeto valores do dtoMoeda.
		
			return true;
		} else // caso contrário, algo deu errado.
			return false;
	}
}
