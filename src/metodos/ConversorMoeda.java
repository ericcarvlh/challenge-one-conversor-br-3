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
import org.json.JSONException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONObject;

import view.conversor.DAOMoeda;
import view.conversor.DTOMoeda;
import view.conversor.GraficoMoeda;

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
	 * Retorna as chaves das moedas com a sigla, simbolo monetário e o nome da moeda (se houver).
	 * @author Eric Carvalho
	 */
	public static String[] retornaChavesMap(Map<String, Object> map) {
		Set<String> keys = map.keySet();
		return keys.toArray(new String[keys.size()]);
	}
	
	public static String[] retornaMoedas(String[] siglaMoedas) {
		// gerando a sigla, simbolo e nome de cada moeda para auxiliar no entedimento do usuário.
		for (int i = 0; i < siglaMoedas.length; i++) {
			String siglaMoeda = siglaMoedas[i],
			nomeMoeda = retornaNomeMoeda(siglaMoeda),
			simboloMoeda = retornaSimboloMoeda(siglaMoeda);
			siglaMoedas[i] = String.format("%s (%s) - %s", siglaMoeda, simboloMoeda, nomeMoeda);  
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
		
		try {
			URL urlParaChamada = new URL(url);
	        
            JSONObject jsonObject = retornaRequisicaoJSON(urlParaChamada);
            
            return jsonObject.getJSONObject("data").toMap();
		} catch (IOException e) {
			System.out.println(String.format("url para analise = %s\nErro de fluxo: %s", url, e.getMessage()));
		} catch (JSONException e) {
			System.out.println(String.format("url para analise = %s\\nErro de JSON: %s", url, e.getMessage()));
		}
		return null;
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
	
	public static String encontraSimboloMoeda(String moedaDesejada) {
		int indexComeco = moedaDesejada.indexOf("(");
		int indexFinal = moedaDesejada.indexOf(")");
		
		if (indexComeco != -1) {
			if (indexFinal != -1) {
				indexComeco++; // soma-se mais um para ele não começar no primeiro parenteses.
				return moedaDesejada.substring(indexComeco, indexFinal);
			}
		}
		return null;
	}
	
	public static void calculaConversao(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas, String[] chavesMoedas, int tipoConversao) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) {
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			
			if (tipoConversao == 1)
				total = montante / valoresMoedas[indexMoeda];
			else 
				total = montante * valoresMoedas[indexMoeda];
			
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
	public static String retornaSiglaMoeda(String moedaDesejada) {
		return moedaDesejada.substring(0, 3);
	}
	
	public static String retornaNomeMoeda(String moedaDesejada) {		
		try {
			Currency moeda = Currency.getInstance(moedaDesejada);
			return moeda.getDisplayName(Locale.getDefault());
		} catch (Exception e) { // se entrar aqui foi informado somente a sigla e a sigla é null, entao retornamos ela 
			System.out.println(String.format("Erro com a moeda %s: %s", moedaDesejada, e.getMessage()));
			return moedaDesejada;
		}
	}
	
	public static String retornaSimboloMoeda(String moedaDesejada) {
		try {
			Currency moeda = Currency.getInstance(moedaDesejada);
			return moeda.getSymbol();
		} catch (Exception e) { // se entrar aqui o valor da currency é null e então retornamos o simbolo "$"
			System.out.println(String.format("Erro com a moeda %s: %s", moedaDesejada, e.getMessage()));
			return "$";
		}
	}

	public static double[] extraiValorMoedaMapObjeto(Map<String, Object> dadosMoeda) {
		int index = 0;
		
		double[] valores = new double[dadosMoeda.size()];
		
		for (Map.Entry<String, Object> entry : dadosMoeda.entrySet()) {
		    String valor = entry.getValue().toString();
		    valor = valor.replace("{", "");
		    valor = valor.replace("}", "");
		    String chave = valor.split("=")[1];
		    valores[index] = Double.parseDouble(chave);
		    index++;
		}
		
		return valores;
	}

	public static void atualizaLabelMoedas(JLabel labelMoeda, double[] valoresMoeda, JComboBox comboBoxMoedaBase, JComboBox comboBoxMoedaConversao, int tipoLabelConversao) {
		int indexValorMoedaConversao = comboBoxMoedaConversao.getSelectedIndex();
		
		String conteudoComboBox = comboBoxMoedaBase.getSelectedItem().toString();
		String siglaMoedaBase = retornaSiglaMoeda(conteudoComboBox);
		
		conteudoComboBox = comboBoxMoedaConversao.getSelectedItem().toString();
		String siglaMoedaConversao = retornaSiglaMoeda(conteudoComboBox);
		
		double resultado = valoresMoeda[indexValorMoedaConversao];
		if (tipoLabelConversao == 1)
			resultado = 1 / resultado;
		
		String conteudoLabel;
		if (tipoLabelConversao == 1)
			conteudoLabel = String.format("1 %s = %f %s", siglaMoedaConversao, resultado, siglaMoedaBase);
		else 
			conteudoLabel = String.format("1 %s = %f %s", siglaMoedaBase, resultado, siglaMoedaConversao);
		
		labelMoeda.setText(conteudoLabel);
	}

	public static void alteraMoedaConversao(JComboBox comboBoxMoedaBase, JComboBox comboBoxMoedaConversao, DTOMoeda dtoMoeda, DAOMoeda daoMoeda, JLabel lblInfo, JLabel lblValorMoedaBase, JLabel lblValorMoedaConversao) {
		String conteudoComboBox = (String) comboBoxMoedaBase.getSelectedItem();
		String siglaMoedaBase = ConversorMoeda.retornaSiglaMoeda(conteudoComboBox);
				
		conteudoComboBox = (String) comboBoxMoedaConversao.getSelectedItem();
		String siglaMoedaConversao = ConversorMoeda.retornaSiglaMoeda(conteudoComboBox);
		
		if (!siglaMoedaBase.equals(siglaMoedaConversao)) {
			DAOMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoedaBase);
			
			Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoedaBase, siglaMoedaConversao);
			String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
							
			double valoresPorData[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
			
			String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(siglaMoedaBase), 
			nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(siglaMoedaConversao),
			infoConversao = String.format("De %s para %s.", nomeMoedaBase, nomeMoedaConversao);
			lblInfo.setText(infoConversao);	
			
			ConversorMoeda.atualizaLabelMoedas(lblValorMoedaBase, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao, 0);
			ConversorMoeda.atualizaLabelMoedas(lblValorMoedaConversao, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao,  1);

			new GraficoMoeda(datas, valoresPorData, siglaMoedaBase, siglaMoedaConversao);
		} else { // a moeda base e a de conversao sao iguais, então voltamos ao form padrão dando avisos.
			JOptionPane.showMessageDialog(null, "Ops...Não há gráfico de valorização para uma mesma moeda e também conversão.");
			colocaConfigucaoPadraoConversor(daoMoeda, dtoMoeda, comboBoxMoedaConversao, comboBoxMoedaBase, lblInfo, lblValorMoedaBase, lblValorMoedaConversao);
		}
	}
	
	public static void colocaConfigucaoPadraoConversor(DAOMoeda daoMoeda, DTOMoeda dtoMoeda, JComboBox comboBoxMoedaConversao, JComboBox comboBoxMoedaBase, JLabel lblInfo, JLabel lblValorMoedaBase, JLabel lblValorMoedaConversao) {
		Currency currency = Currency.getInstance(Locale.getDefault());
		String siglaMoeda = currency.getCurrencyCode();
		
		DAOMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoeda);
		
		comboBoxMoedaConversao.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));
		String moedaConversao = "USD"; // atribuindo USD como padrão.	
		int indexDefaultConversionCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), moedaConversao);
		comboBoxMoedaConversao.setSelectedIndex(indexDefaultConversionCurrency);
		
		comboBoxMoedaBase.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));

		int indexDefaultCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), siglaMoeda); // atribuinfo a moeda local como padrão.
		comboBoxMoedaBase.setSelectedIndex(indexDefaultCurrency);
		
		Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoeda, moedaConversao);
		String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
						
		double valoresMoedaPorDia[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
		
		new GraficoMoeda(datas, valoresMoedaPorDia, siglaMoeda, moedaConversao);
		
		String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(siglaMoeda), 
		nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(moedaConversao),
		infoConversao = String.format("De %s para %s.", nomeMoedaBase, nomeMoedaConversao);
		lblInfo.setText(infoConversao);
		
		ConversorMoeda.atualizaLabelMoedas(lblValorMoedaBase, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao, 0);
		ConversorMoeda.atualizaLabelMoedas(lblValorMoedaConversao, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao,  1);

	}
}
