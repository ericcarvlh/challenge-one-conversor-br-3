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

import view.conversor.moeda.DAOMoeda;
import view.conversor.moeda.DTOMoeda;
import view.conversor.moeda.GraficoMoeda;

public class ConversorMoeda {
	
	/**
	 * Realiza requisição e retorna a resposta em JSON.
	 * @author Eric Carvalho
	 * @param urlParaChamada é a URL que deve ser feita a requisição e a respsota precisa ser em JSON.
	 * @return retorna a resposta em JSONObject.
	 * */
	public static JSONObject retornaRequisicaoJSON(URL urlParaChamada) throws IOException {
		HttpURLConnection conexao = (HttpURLConnection) urlParaChamada.openConnection();

        int codigoSucesso = 200;
        if (conexao.getResponseCode() != codigoSucesso) // se for um codigo diferente de 200 então deu algum erro.
            throw new RuntimeException("HTTP error code: " + conexao.getResponseCode());
        
        BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        String response, jsonEmString = "";
        while ((response = resposta.readLine()) != null) { // pego o conteudo da resposta e vou armazenando em uma variavel
            jsonEmString += response;
        }
                
        return new JSONObject(jsonEmString); // converto para JSONObject e retorno.
	}
	
	/**
	 * Mapeia a lista de moedas e a retorna em Map<String, Object>.
	 * @author Eric Carvalho
	 * @param siglaMoeda é a moeda base.
	 * @return retorna os dados mapeados, sendo a chave em String e o valor em objeto.
	 * */
	public static Map<String, Object> retornaListaMoedas(String siglaMoeda) {	
		String url = "https://api.exchangerate-api.com/v4/latest/";
		       
		try { // tenta executar
			URL urlParaChamada = new URL(url+siglaMoeda); // monta a URL que deve ser acessada.

			JSONObject jsonObject = retornaRequisicaoJSON(urlParaChamada); // realiza a requisição e obtém a resposta em JSONObject
			
            return jsonObject.getJSONObject("rates").toMap(); // retorna as moedas e os valores.
		} catch (IOException e) { // pode ocorrer um erro de acesso a internet ou outras fatores e isso deve ser tratado.
			System.out.println(String.format("Erro de fluxo: %s", e.getMessage()));
		}
		return null; // retorna nulo se ocorrer algo inesperado.
	}	

	/**
	 * Retorna as chaves (sigla) das moedas que se encontram no map.
	 * @author Eric Carvalho
	 * @param map é o objeto onde se encontra as chaves e os valores.
	 * @return retorna um vetor com as chaves.
	 * */
	public static String[] retornaChavesMap(Map<String, Object> map) {
		Set<String> keys = map.keySet(); // coleta as chaves.
		return keys.toArray(new String[keys.size()]); // retorna as chaves em array.
	}
	
	/**
	 * Formata a sigla, simbolo e o nome da moeda.
	 * @author Eric Carvalho
	 * @param siglaMoedas é o array que contém as siglas das moedas.
	 * @return siglaMoedas é o array que contém sigla, simbolo e o nome da moeda formatados. 
	 * */
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
	
	/**
	 * Recebe o map das moedas e retorna os valores de cada uma em array.
	 * @author Eric Carvalho
	 * @param map é o objeto que contém os dados com os valores.
	 * @return valores é o array com os valores das respectivas moedas.
	 * */
	public static double[] retornaValoresMap(Map<String, Object> map) {
		double[] valores = new double[map.size()]; // valores é instanciado com o tamanho do map.
		int i = 0;
		for (Object value :  map.values()) {
			BigDecimal  big = new BigDecimal(value+"");	 // o valor é identificado como bigdecimal      
			valores[i] = big.doubleValue(); // e depois é convertido para double para ser adicionado ao array.
			i++;
		}
				
		return valores;
	}

	/**
	 * Retorna o histórico de uma moeda Y em relação a X.
	 * @author Eric Carvalho
	 * @param siglaMoedaBase é a moeda base.
	 * @param siglaMoedaDesejada é a que queremos observar.
	 * @return JSONObject que contém as datas e os valores. 
	 * */
	public static Map<String, Object> retornaDadosHistoricoMoeda(String siglaMoedaBase, String siglaMoedaDesejada) {
		// pega o mes retrasado e o mes atual de uma forma que o intervalo seja 1 mês.
		LocalDate today = LocalDate.now();
		LocalDate dataRetrasada = today.minus(Period.ofMonths(1));
		LocalDate dataAtual = today.minus(Period.ZERO);
		
		// monta a url
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
	 * Pega a posição da moeda desejada no array.
	 * @author Eric Carvalho
	 * @param chavesMoedas array que vai ser analisado.
	 * @param siglaDesejada é a sigla da moeda que desejamos encontrar.
	 * @return Se houver a presença da moeda desejada no array, é retornada a sua possição, caso contrário é retornado -1.
	 * */
	public static int buscaIndexSigla(String[] chavesMoedas, String siglaDesejada) {
		for (int i = 0; i < chavesMoedas.length; i++) {
			String nomeMoeda = chavesMoedas[i];
			if (nomeMoeda.contains(String.format("%s (", siglaDesejada))) // ve se o index atual possui como conteúdo a sigla da moeda desejada.
				return i;
		}
		return -1; // retorna -1 se nada for encontrado.
	}
	
	/**
	 * Verifica se o item possui algum numero ou ponto, se sim é retornado falso, caso contrario é retorno true.
	 * @author Eric Carvalho
	 * @param valorASerAnalisado array que vai ser analisado.
	 * @return Se houver a presença de letras é retornado falso, se houver a presença de número ou ponto, é retornado true.
	 * */
	public static boolean contemSomentNumero(String valorASerAnalisado) {
		if (valorASerAnalisado.length() == 0)
			return false;
		
		boolean resultado = true;
		for (int i = 0; i < valorASerAnalisado.length(); i++) { // percorre o valor verificando cada caractere,
			if (!Character.isDigit(valorASerAnalisado.charAt(i)) && !(valorASerAnalisado.charAt(i) == '.'))
				resultado = false;
		}
		
		return resultado; // retorna o resultado
	}	
	
	/**
	 * Encontra o simbolo da moeda e o retorna.
	 * @author Eric Carvalho
	 * @param moedaDesejada é a moeda que queremos obter o simbolo.
	 * @return Retorna o simbolo da moeda.
	 * */
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
	
	/**
	 * Pega a posição da moeda desejada no array.
	 * @author Eric Carvalho
	 * @param textFieldMontante é o campo que possui o valor a ser convertido.
	 * @param textFieldResultadoConversao  é o campo onde vai o valor convertido.
	 * @param comboBoxConversao é o elemento onde armazena a moeda para conversão. 
	 * @param valoresMoedas é o array que armazena os valores das moedas.
	 * @param chavesMoedas é o array que armazena as siglas, simbolos e nomes das moedas.
	 * @param tipoConversao é o tipo de conversão. Caso for 1 calcula da moeda X para Y e se for 0 ou outro numero calcula de Y para X.
	 * @return Se houver a presença da moeda desejada no array, é retornada a sua possição, caso contrário é retornado -1.
	 * */
	public static void calculaConversao(JTextField textFieldMontante, JTextField textFieldResultadoConversao, JComboBox comboBoxConversao, double[] valoresMoedas, String[] chavesMoedas, int tipoConversao) {
		String conteudoTextField = textFieldMontante.getText();
		if (ConversorMoeda.contemSomentNumero(conteudoTextField)) { // verifica se há somente números
			double montante = Double.valueOf(conteudoTextField), total = 0;
			int indexMoeda = comboBoxConversao.getSelectedIndex();
			
			if (tipoConversao == 1)
				total = montante / valoresMoedas[indexMoeda];
			else 
				total = montante * valoresMoedas[indexMoeda];
			
			textFieldResultadoConversao.setText(String.format("%.2f", total));
		}
	}
	
	/**
	 * Pega a moeda que queremos obter a sigla e a retorna.
	 * @author Eric Carvalho
	 * @param moedaDesejada é a moeda que queremos a sigla.
	 * @return Retorna a sigla da moeda.
	 * */
	public static String retornaSiglaMoeda(String moedaDesejada) {
		return moedaDesejada.substring(0, 3);
	}
	
	/**
	 * Pega a moeda que queremos obter o nome e o retorna.
	 * @author Eric Carvalho
	 * @param moedaDesejada é a moeda que queremos o nome.
	 * @return Retorna o nome da moeda.
	 * */
	public static String retornaNomeMoeda(String moedaDesejada) {		
		try {
			Currency moeda = Currency.getInstance(moedaDesejada);
			return moeda.getDisplayName(Locale.getDefault());
		} catch (Exception e) { // se entrar aqui foi informado somente a sigla e a sigla é null, entao retornamos ela 
			System.out.println(String.format("Erro com a moeda %s: %s", moedaDesejada, e.getMessage()));
			return moedaDesejada;
		}
	}
	
	/**
	 * Pega a moeda que queremos obter o simbolo e o retorna.
	 * @author Eric Carvalho
	 * @param moedaDesejada é a moeda que queremos o simbolo.
	 * @return Retorna o simbolo da moeda.
	 * */
	public static String retornaSimboloMoeda(String moedaDesejada) {
		try {
			Currency moeda = Currency.getInstance(moedaDesejada);
			return moeda.getSymbol();
		} catch (Exception e) { // se entrar aqui o valor da currency é null e então retornamos o simbolo "$"
			System.out.println(String.format("Erro com a moeda %s: %s", moedaDesejada, e.getMessage()));
			return "$";
		}
	}

	/**
	 * Retorna os valores da moeda se o conteudo estiver dentro de outro objeto.
	 * @author Eric Carvalho
	 * @param dadosMoeda é o map que contém os dados da moeda.
	 * @return Retorna os valores da moeda.
	 * */
	public static double[] extraiValorMoedaMapObjeto(Map<String, Object> dadosMoeda) {
		int index = 0;
		
		double[] valores = new double[dadosMoeda.size()]; // atribui o tamanho do vetor igual ao do map.
		
		// percorre o map, retira a "{" e "}" para o processo da coleta ser mais fácil.
		for (Map.Entry<String, Object> entry : dadosMoeda.entrySet()) {
		    String valor = entry.getValue().toString();
		    valor = valor.replace("{", "");
		    valor = valor.replace("}", "");
		    String chave = valor.split("=")[1];
		    valores[index] = Double.parseDouble(chave);
		    index++;
		}
		
		return valores; // após percorrer o map, retorna os valores objetidos.
	}

	/**
	 * Atualiza a label de moedas de acordo com os valores passados.
	 * @author Eric Carvalho
	 * @param lblInfo é o label onde contém as informações de conversão.
	 * @param valoresMoeda é o array que contém os valores da moeda.
	 * @param comboBoxMoedaBase é o combobox onde se encontra a moeda base.
	 * @param comboBoxMoedaConversao é o combobox onde se encontra a moeda para conversão.
	 * @param tipoLabelConversao é o tipo de conversão que será exibido no label.
	 * */
	public static void atualizaLabelMoedas(JLabel lblInfo, double[] valoresMoeda, JComboBox comboBoxMoedaBase, JComboBox comboBoxMoedaConversao, int tipoLabelConversao) {
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
		
		lblInfo.setText(conteudoLabel);
	}

	/**
	 * Altera a moeda, indo do valor até o GUI.
	 * @author Eric Carvalho
	 * @param comboBoxMoedaBase é o elemento que contém a moeda base.
	 * @param comboBoxMoedaConversao é o elemento que contém a moeda desejada para conversão.
	 * @param dtoMoeda é o objeto que contém os objetos das moedas.
	 * @param daoMoeda é o objeto que manipula o dtoMoeda.
	 * @param lblInfo é o label onde vai as informações
	 * @param lblValorMoedaBase é o label que contém os valores da moeda base.
	 * @param lblValorMoedaConversao é o label que contém os valores de conversão.
	 * */
	public static void alteraMoedaConversao(JComboBox comboBoxMoedaBase, JComboBox comboBoxMoedaConversao, DTOMoeda dtoMoeda, DAOMoeda daoMoeda, JLabel lblInfo, JLabel lblValorMoedaBase, JLabel lblValorMoedaConversao) {
		// obtém o conteúdo do comboBox e a sigla da moeda base.
		String conteudoComboBox = (String) comboBoxMoedaBase.getSelectedItem();
		String siglaMoedaBase = ConversorMoeda.retornaSiglaMoeda(conteudoComboBox);
		
		// obtém o conteúdo do comboBox e a sigla da moeda de conversão.
		conteudoComboBox = (String) comboBoxMoedaConversao.getSelectedItem();
		String siglaMoedaConversao = ConversorMoeda.retornaSiglaMoeda(conteudoComboBox);
		
		// verifica se a moeda de conversoa é igual a moeda de base.
		if (!siglaMoedaBase.equals(siglaMoedaConversao)) { // se não for, prosseguimos.
			DAOMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoedaBase);
			
			// obtém os valores anteriores da moeda e os respectivos dias desses valores.
			Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoedaBase, siglaMoedaConversao);
			String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
						
			// coleta os valores da moeda nas tais datas.
			double valoresPorData[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
			
			// seta uma informação sobre a conversão para o usuário.
			String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(siglaMoedaBase), 
			nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(siglaMoedaConversao),
			infoConversao = String.format("De %s para %s.", nomeMoedaBase, nomeMoedaConversao);
			lblInfo.setText(infoConversao);	
			
			// muda a interface, atualizando os labels.
			atualizaLabelMoedas(lblValorMoedaBase, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao, 0);
			atualizaLabelMoedas(lblValorMoedaConversao, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao,  1);

			// abrindo um gráfico com a valorização da moeda X em relaçaõ a Y.
			new GraficoMoeda(datas, valoresPorData, siglaMoedaBase, siglaMoedaConversao);
		} else { // a moeda base e a de conversao sao iguais, então voltamos ao form padrão dando avisos.
			JOptionPane.showMessageDialog(null, "Ops...Não há gráfico de valorização para uma mesma moeda e também conversão.", "Conversão inválida", JOptionPane.ERROR_MESSAGE);
			colocaConfigucaoPadraoConversor(daoMoeda, dtoMoeda, comboBoxMoedaConversao, comboBoxMoedaBase, lblInfo, lblValorMoedaBase, lblValorMoedaConversao);
		}
	}
	
	/**
	 * Coloca a configuração padrão do sistema no GUI.
	 * @author Eric Carvalho
	 * @param daoMoeda é o objeto que manipula o dtoMoeda.
	 * @param dtoMoeda é o objeto que contém os objetos das moedas.
	 * @param comboBoxMoedaConversao é o elemento que contém a moeda desejada para conversão.
	 * @param comboBoxMoedaBase é o elemento que contém a moeda base.
	 * @param lblInfo é o label onde vai as informações
	 * @param lblValorMoedaBase é o label que contém os valores da moeda base.
	 * @param lblValorMoedaConversao é o label que contém os valores de conversão.
	 * @return Retorna true se tudo o processo for realizado com sucesso ou false do caso contrário.
	 * */
	public static boolean colocaConfigucaoPadraoConversor(DAOMoeda daoMoeda, DTOMoeda dtoMoeda, JComboBox comboBoxMoedaConversao, JComboBox comboBoxMoedaBase, JLabel lblInfo, JLabel lblValorMoedaBase, JLabel lblValorMoedaConversao) {
		// pegando a moeda padrão de acordo com o sistema do usuário e sua localização.
		Currency currency = Currency.getInstance(Locale.getDefault());
		String siglaMoeda = currency.getCurrencyCode();
		
		if(DAOMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoeda)) { // se os dados tiverem sidos atribuidos, prosseguimos
		
			String moedaConversao = "USD"; // atribuindo USD como padrão.	
			// pegando a posição do USD em relação ao array.
			int indexDefaultConversionCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), moedaConversao);
			// atribuindo o USD no comboBoxMoedaConversao como moeda de conversão padrão.
			comboBoxMoedaConversao.setSelectedIndex(indexDefaultConversionCurrency);
			
			// coloca as moedas no comboBoxMoedaConversao.
			comboBoxMoedaConversao.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));
			
			// coloca as moedas no comboBoxMoedaBase.
			comboBoxMoedaBase.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));
	
			// coloca a moeda padrão (de acordo com a região do usuário) no comboBoxMoedaBase.
			int indexDefaultCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), siglaMoeda); // atribuinfo a moeda local como padrão.
			comboBoxMoedaBase.setSelectedIndex(indexDefaultCurrency);
			
			// obtém os valores anteriores da moeda e os respectivos dias desses valores.
			Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoeda, moedaConversao);
			String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
							
			// coleta os valores da moeda nas tais datas.
			double valoresMoedaPorData[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
			
			// abrindo um gráfico com a valorização da moeda X em relaçaõ a Y.
			new GraficoMoeda(datas, valoresMoedaPorData, siglaMoeda, moedaConversao);
			
			// seta uma informação sobre a conversão para o usuário.
			String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(siglaMoeda), 
			nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(moedaConversao),
			infoConversao = String.format("De %s para %s.", nomeMoedaBase, nomeMoedaConversao);
			lblInfo.setText(infoConversao);
			
			// muda a interface, atualizando os labels.
			atualizaLabelMoedas(lblValorMoedaBase, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao, 0);
			atualizaLabelMoedas(lblValorMoedaConversao, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao,  1);
		
			return true; // ao final, tudo deu certo então retornamos true.
		} else // retornando false porque os dados não foram atribuidos.
			return false; 
	}
}
