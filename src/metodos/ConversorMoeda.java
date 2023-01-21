package metodos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.json.JSONObject;

public class ConversorMoeda {
	
	/**
	 * Retornar as sigla e os valore da moeda.
	 * @author Eric Carvalho
	 * @return Map<String, Object> do valor da moeda e a sigla de acordo com a moeda base.
	*/
	public static Map<String, Object> retornaListaMoedas() {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();		
		String url = "https://api.exchangerate-api.com/v4/latest/";
		       
		try {
			URL urlParaChamada = new URL(url+currencyCode);
			HttpURLConnection conexao = (HttpURLConnection) urlParaChamada.openConnection();

	        int codigoSucesso = 200;
	        if (conexao.getResponseCode() != codigoSucesso) 
	            throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());
	        
	        BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

	        String response, jsonEmString = "";
	        while ((response = resposta.readLine()) != null) {
	            jsonEmString += response;
	        }
	        
            JSONObject jsonObject = new JSONObject(jsonEmString);
            return jsonObject.getJSONObject("rates").toMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** 
	 * Retorna as chaves das moedas com a sigla, simbolo monetário e o nome da moeda (se houver).
	 * @author Eric Carvalho
	 */
	public static String[] retornaListaChavesMoedas(Map<String, Object> moedas) {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode(); // pega a sigla da moeda local.
		String moedaBase = currencyCode; // atribui a sigla da moeda local a moeda base.

		Set<String> keys = moedas.keySet();
		String[] chavesMoedas = keys.toArray(new String[keys.size()]);
				
		// gerando o significado de cada sigla para auxiliar no entedimento.
		for (int i = 0; i < chavesMoedas.length; i++) {
			String siglaMoeda = chavesMoedas[i], nomeMoeda = "Desconhecido", simboloMoeda = "Desconhecido";
			try {
				Currency moeda = Currency.getInstance(siglaMoeda);
				nomeMoeda = moeda.getDisplayName(Locale.getDefault());
				simboloMoeda = moeda.getSymbol();
				chavesMoedas[i] = String.format("%s (%s) - %s", siglaMoeda, simboloMoeda, nomeMoeda);  
			} catch (Exception e) {
				System.out.println(String.format("Erro com a moeda %s: %s", siglaMoeda, e.getMessage()));
				chavesMoedas[i] = String.format("%s (%s) - %s", siglaMoeda, simboloMoeda, nomeMoeda);  
			}
		} 
		
		return chavesMoedas;
	}
	
	public static double[] retornaListaValoresMoedas(Map<String, Object> moedas) {
		double[] valores = new double[moedas.size()];
		int i = 0;
		for (Object value :  moedas.values()) {
			BigDecimal  big = new BigDecimal(value+"");	        
			valores[i] = big.doubleValue();
			i++;
		}
				
		return valores;
	}
	
	/**
	 * Necessita do array das chaves (keys) das moedas e a sigla que deseja encontrar, se não houver nada que coincida então é retornado -1, caso contrário é retornado a posição do elemento.
	 * @author Eric Carvalho
	 * @param chavesMoedas é o array onde se encontra as chaves (keys) das moedas.
	 * @param siglaDesejada é a sigla que vai ser buscada no array.
	 * @return -1 se nada for encontrado ou o indice do elemento caso algo for encontrado.
	 */
	public static int buscaIndexSigla(String[] chavesMoedas, String siglaDesejada) {
		for (int i = 0; i < chavesMoedas.length; i++) {
			String nomeMoeda = chavesMoedas[i];
			if (nomeMoeda.contains(String.format("%s (", siglaDesejada)))
				return i;
		}
		return -1;
	}
	
	/**
	 * Necessita do valor que vai ser analisidado para ver se ele é um número (se este for o caso é retornado verdadeiro) ou não (nesse caso é retornado falso). Se o valor vinher vazio também é retornado falso.
	 * @author Eric Carvalho
	 * @param valorASerAnalisado
	 * @return verdadeiro se não houver nenhuma letra ou falso se houver letras ou se o conteúdo for vazio.
	 */
	public static boolean contemSomentNumero(String valorASerAnalisado) {
		if (valorASerAnalisado.length() == 0)
			return false;
		
		boolean resultado = true;
		for (int i = 0; i < valorASerAnalisado.length(); i++) {
			if (!Character.isDigit(valorASerAnalisado.charAt(i)) && !(valorASerAnalisado.charAt(i) == '.'))
				resultado = false;
		}
		
		return resultado;
	}
	
	public static void calculaConversaoMoedaBase(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) {
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			System.out.println(comboBoxConversao.getSelectedIndex());
			total = montante * valoresMoedas[indexMoeda];
			System.out.println(valoresMoedas[indexMoeda]);
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
	public static void calculaConversao(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) {
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			System.out.println(comboBoxConversao.getSelectedIndex());
			total = montante / valoresMoedas[indexMoeda];
			System.out.println(String.format("%.2f / %.2f", montante, valoresMoedas[indexMoeda]));
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
}
