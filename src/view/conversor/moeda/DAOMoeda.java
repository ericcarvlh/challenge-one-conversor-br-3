package view.conversor.moeda;

import java.util.Map;

import metodos.ConversorMoeda;

public class DAOMoeda {
	public static void atribuiDadosMoeda(DTOMoeda dtoMoeda, String siglaMoeda) {
		Map<String, Object> mapMoedas = ConversorMoeda.retornaListaMoedas(siglaMoeda);
		dtoMoeda.setMapMoedas(mapMoedas);
		
		String[] chaves = ConversorMoeda.retornaChavesMap(mapMoedas);
		chaves = ConversorMoeda.retornaMoedas(chaves);
		dtoMoeda.setMoedas(chaves);
		
		double[] valoresMoedas = ConversorMoeda.retornaValoresMap(mapMoedas);
		valoresMoedas = ConversorMoeda.retornaValoresMap(mapMoedas);
		dtoMoeda.setValoresMoedas(valoresMoedas);
	}
}
