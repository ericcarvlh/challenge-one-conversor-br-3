package metodos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.json.JSONObject;

public class ConversorMoeda {
	
	public static JSONObject retornaRequisicaoJSON(URL urlParaChamada) throws IOException {
		HttpURLConnection conexao = (HttpURLConnection) urlParaChamada.openConnection();

        int codigoSucesso = 200;
        if (conexao.getResponseCode() != codigoSucesso) 
            throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());
        
        BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        String response, jsonEmString = "";
        while ((response = resposta.readLine()) != null) {
            jsonEmString += response;
        }
                
        return new JSONObject(jsonEmString);
	}
	
	/**
	 * Retornar as sigla e os valore da moeda.
	 * @author Eric Carvalho
	 * @return Map<String, Object> do valor da moeda e a sigla de acordo com a moeda base.
	*/
	public static Map<String, Object> retornaListaMoedas(String siglaMoeda) {	
		String url = "https://api.exchangerate-api.com/v4/latest/";
		       
		try {
			URL urlParaChamada = new URL(url+siglaMoeda);

			JSONObject jsonObject = retornaRequisicaoJSON(urlParaChamada);
			
            return jsonObject.getJSONObject("rates").toMap();
		} catch (IOException e) {
			System.out.println(String.format("Erro de fluxo: %s", e.getMessage()));
		}
		return null;
	}	
	
	/** 
	 * Retorna as chaves das moedas com a sigla, simbolo monet�rio e o nome da moeda (se houver).
	 * @author Eric Carvalho
	 */
	public static String[] retornaChavesMap(Map<String, Object> map) {
		Set<String> keys = map.keySet();
		return keys.toArray(new String[keys.size()]);
	}
	
	public static String[] retornaMoedas(String[] siglaMoedas) {
		// gerando a sigla, simbolo e nome de cada moeda para auxiliar no entedimento do usu�rio.
		for (int i = 0; i < siglaMoedas.length; i++) {
			String siglaMoeda = siglaMoedas[i], nomeMoeda = "Desconhecido", simboloMoeda = "$";
			try {
				nomeMoeda = retornaNomeMoeda(siglaMoeda);
				simboloMoeda = retornaSimboloMoeda(siglaMoeda);
				siglaMoedas[i] = String.format("%s (%s) - %s", siglaMoeda, simboloMoeda, nomeMoeda);  
			} catch (Exception e) {
				System.out.println(String.format("Erro com a moeda %s: %s", siglaMoeda, e.getMessage()));
				siglaMoedas[i] = String.format("%s (%s) - %s", siglaMoeda, simboloMoeda, nomeMoeda);  
			}
		}
		
		return siglaMoedas;
	}
	
	public static double[] retornaValoresMap(Map<String, Object> map) {
		double[] valores = new double[map.size()];
		int i = 0;
		for (Object value :  map.values()) {
			BigDecimal  big = new BigDecimal(value+"");	       
			valores[i] = big.doubleValue();
			i++;
		}
				
		return valores;
	}

	public static Map<String, Object> retornaDadosHistoricoMoeda(String siglaMoedaBase, String siglaMoedaDesejada) {
		LocalDate today = LocalDate.now();
		LocalDate dataRetrasada = today.minus(Period.ofMonths(1));
		LocalDate dataAtual = today.minus(Period.ZERO);
		
		String url = "https://www.forbes.com/advisor/wp-json/advisor/v1/currency-conversion/time-series?"
				+ "base="+siglaMoedaBase+""
				+ "&symbols="+siglaMoedaDesejada+""
				+ "&start="+dataRetrasada+""
				+ "&end="+dataAtual+""
				+ "&noauthfilter1=true";
	    System.out.println(url);
		try {
			URL urlParaChamada = new URL(url);
	        
            JSONObject jsonObject = retornaRequisicaoJSON(urlParaChamada);
            
            return jsonObject.getJSONObject("data").toMap();
		} catch (IOException e) {
			System.out.println(String.format("Erro de fluxo: %s", e.getMessage()));
		}
		return null;
	}
	
	/**
	 * Necessita do array das chaves (keys) das moedas e a sigla que deseja encontrar, se n�o houver nada que coincida ent�o � retornado -1, caso contr�rio � retornado a posi��o do elemento.
	 * @author Eric Carvalho
	 * @param chavesMoedas � o array onde se encontra as chaves (keys) das moedas.
	 * @param siglaDesejada � a sigla que vai ser buscada no array.
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
	 * Necessita do valor que vai ser analisidado para ver se ele � um n�mero (se este for o caso � retornado verdadeiro) ou n�o (nesse caso � retornado falso). Se o valor vinher vazio tamb�m � retornado falso.
	 * @author Eric Carvalho
	 * @param valorASerAnalisado
	 * @return verdadeiro se n�o houver nenhuma letra ou falso se houver letras ou se o conte�do for vazio.
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
	
	public static String encontraSimboloMoeda(String moedaDesejada) {
		int indexComeco = moedaDesejada.indexOf("(");
		int indexFinal = moedaDesejada.indexOf(")");
		
		if (indexComeco != -1) {
			if (indexFinal != -1) {
				indexComeco++; // soma-se mais um para ele n�o come�ar no primeiro parenteses.
				return moedaDesejada.substring(indexComeco, indexFinal);
			}
		}
		return null;
	}
	
	public static void calculaConversaoMoedaBase(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas, String[] chavesMoedas) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) {
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			total = montante * valoresMoedas[indexMoeda];
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
	public static void calculaConversao(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas, String[] chavesMoedas) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) {
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			total = montante / valoresMoedas[indexMoeda];
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
	public static String retornaSiglaMoeda(String moedaDesejada) {
		return moedaDesejada.substring(0, 3);
	}
	
	
	public static String retornaNomeMoeda(String moedaDesejada) {
		Currency moeda = Currency.getInstance(moedaDesejada);
		return moeda.getDisplayName(Locale.getDefault());
	}
	
	public static String retornaSimboloMoeda(String moedaDesejada) {
		Currency moeda = Currency.getInstance(moedaDesejada);
		return moeda.getSymbol();
	}
}