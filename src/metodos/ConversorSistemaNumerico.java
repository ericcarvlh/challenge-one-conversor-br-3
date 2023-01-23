package metodos;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConversorSistemaNumerico {
	
	/**
	 * Abre um popUp de erro e mostra a mensagem de acordo com os parametros.
	 * @author Eric Carvalho
	 * @param mensagem é a mensagem que será exibida no pop-up.
	 * @param tituloPopUp é o titulo que será exibido no pop-up.
	 * */
	public static void popUpErro(String mensagem, String tituloPopUp) {
		JOptionPane.showMessageDialog(null, mensagem, tituloPopUp, JOptionPane.ERROR_MESSAGE);
	}
		
	/**
	 * Traduz o número recebido para decimal e o retorna.
	 * @author Eric Carvalho
	 * @param sistemaNumerico é o tipo de sistema numerico que vai ser traduzido.
	 * @param conteudoEntrada é o número que deve ser traduzido.
	 * @return retorna o numero para traducao em decimal.
	 * */
	public static String traduzParaDecimal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario"))
			return String.valueOf(Integer.parseInt(conteudoEntrada, 2));
		else if (sistemaNumerico.equals("octal"))
			return String.valueOf(Integer.parseInt(conteudoEntrada, 8));
			
		return String.valueOf(Integer.parseInt(conteudoEntrada, 16));
	}
	
	/**
	 * Traduz o número recebido para hexadecimal e o retorna.
	 * @author Eric Carvalho
	 * @param sistemaNumerico é o tipo de sistema numerico que vai ser traduzido.
	 * @param conteudoEntrada é o número que deve ser traduzido.
	 * @return retorna o numero para traducao em hexadecimal.
	 * */
	public static String traduzParaHexaDecimal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario")) {
			int binario = Integer.parseInt(conteudoEntrada, 2);
			return Integer.toHexString(binario);
		} else if (sistemaNumerico.equals("octal")) {
			int octal = Integer.parseInt(conteudoEntrada, 8);
			return Integer.toHexString(octal);
		}
		
		return Integer.toHexString(Integer.parseInt(conteudoEntrada));
	}
	
	/**
	 * Traduz o número recebido para octal e o retorna.
	 * @author Eric Carvalho
	 * @param sistemaNumerico é o tipo de sistema numerico que vai ser traduzido.
	 * @param conteudoEntrada é o número que deve ser traduzido.
	 * @return retorna o numero para traducao em octal.
	 * */
	public static String traduzParaOctal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario")) {
			int binario = Integer.parseInt(conteudoEntrada, 2);
			return Integer.toOctalString(binario);
		} else if (sistemaNumerico.equals("hexadecimal")) {
			int hexaDecimal = Integer.parseInt(conteudoEntrada, 16);
			return Integer.toOctalString(hexaDecimal);
		}
		
		return Integer.toOctalString(Integer.parseInt(conteudoEntrada));
	}

	/**
	 * Traduz o número recebido para binario e o retorna.
	 * @author Eric Carvalho
	 * @param sistemaNumerico é o tipo de sistema numerico que vai ser traduzido.
	 * @param conteudoEntrada é o número que deve ser traduzido.
	 * @return retorna o numero para traducao em binario.
	 * */
	public static String traduzParaBinario(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("octal")) {
			int octal = Integer.parseInt(conteudoEntrada, 8);
			return Integer.toBinaryString(octal);
		} else if (sistemaNumerico.equals("hexadecimal")) {
			int hexaDecimal = Integer.parseInt(conteudoEntrada, 16);
			return Integer.toBinaryString(hexaDecimal);
		}
		
		return Integer.toBinaryString(Integer.parseInt(conteudoEntrada));
	}
	
	/**
	 * Valida a entrada e retorna o sistema númerico.
	 * @author Eric Carvalho
	 * @param entrada é o número a ser traduzido.
	 * @param tipoEntrada representa o sistem númerico de entrada (número a ser traduzido).
	 * @return retorna o sistema númerico.
	 * */
	public static String sistemaNumerico(String entrada, int tipoEntrada) {
		if (tipoEntrada == 0) {
			if (entrada.matches("^[0-9A-Fa-f]+$")) // a entrada é hexadecimal
			   return "hexadecimal";
		} else if (tipoEntrada == 1) { // a entrada é decimal
			if (entrada.matches("^[0-9]+"))
			    return "decimal";
		} else if (tipoEntrada == 2) { // a entrada é octal
			if (entrada.matches("^[0-7]+$"))
				return "octal";
		} else if (tipoEntrada == 3) { // a entrada é binário
			if (entrada.matches("^[01]+$"))
			    return "binario";
		}
			
		return "nenhum";
	}
	
	/**
	 * Valida o conteudo recebido e o manda para a traducao se for valido e ao final seta o valor traduzido na caixa de texto.
	 * @author Eric Carvalho
	 * @param textFieldEntrada é o campo onde se encontra o conteúdo a ser traduzido.
	 * @param textFieldSaida é o campo onde vai ser inserido o conteúdo traduzido.
	 * @param comboBoxEntrada é o elemento que se encontra o sistema numerico de entrada.
	 * @param comboBoxSaida é o elemento que se encontra o sistema numerico de saída.
	 * */
	public static void traduzConteudo(JTextField textFieldEntrada, JTextField textFieldSaida, JComboBox comboBoxEntrada, JComboBox comboBoxSaida) {
		String conteudoEntrada = textFieldEntrada.getText();
		
		if (conteudoEntrada.length() > 0) { // verifica se o campo esta vazio
			int tipoEntrada = comboBoxEntrada.getSelectedIndex(); // recebe qual o sistema numerico de entrada.
			int tipoSaidaDesejada = comboBoxSaida.getSelectedIndex(); // recebe qual o sistema numerico de saida.
			conteudoEntrada = conteudoEntrada.replace(" ",""); // remove os espaços para tornar o precesso mais facil.
						
			String sistemaNumerico = sistemaNumerico(conteudoEntrada, tipoEntrada); // descpnre qual é o sistema numerico.
			
			if (sistemaNumerico.equals("nenhum")) // se for nenhum, então o número informado esta incorreto.
				popUpErro("Ops...o número informado é invalido.", "Entrada inválida");
			else {
				if (tipoEntrada != tipoSaidaDesejada) { // verifica se os sistema numerico de entrada e saida sao diferentes para prosseguir. 
					String traducao = "";
					if (tipoSaidaDesejada == 0) // devera ser traduzido para hexadecimal.
						traducao = traduzParaHexaDecimal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 1) // devera ser traduzido para deciaml.
						traducao = traduzParaDecimal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 2) // devera ser traduzido para octal.
						traducao = traduzParaOctal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 3) // devera ser traduzido para binario.
						traducao = traduzParaBinario(sistemaNumerico, conteudoEntrada);

					textFieldSaida.setText(traducao); // informa a traducao ao usuario.
				} else // se for, então a tradução ja esta feita.
					textFieldSaida.setText(conteudoEntrada);				
			} 
		} else // se estiver vazio, então a entrada é invalida
			popUpErro("Ops...parece que você esqueceu de digitar o número.", "Entrada inválida");
	}
}
