package metodos;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConversorSistemaNumerico {
	public static void popUpErro(String mensagem, String tituloPopUp) {
		JOptionPane.showMessageDialog(null, mensagem, tituloPopUp, JOptionPane.ERROR_MESSAGE);
	}
	
	public static String traduzParaDecimal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario"))
			return String.valueOf(Integer.parseInt(conteudoEntrada, 2));
		else if (sistemaNumerico.equals("octal"))
			return String.valueOf(Integer.parseInt(conteudoEntrada, 8));
			
		return String.valueOf(Integer.parseInt(conteudoEntrada, 16));
	}
	
	public static String traduzParaHexaDecimal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario")) {
			int binario = Integer.parseInt(conteudoEntrada, 2);
			return Integer.toHexString(binario);
		} else if (sistemaNumerico.equals("octal")) {
			int octal = Integer.parseInt(conteudoEntrada, 8);
			return Integer.toHexString(octal);
		}
		
		int decimal = Integer.parseInt(conteudoEntrada);
		return Integer.toHexString(decimal);
	}
	
	public static String traduzParaOctal(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("binario")) {
			int binario = Integer.parseInt(conteudoEntrada, 2);
			return Integer.toOctalString(binario);
		} else if (sistemaNumerico.equals("hexadecimal")) {
			int hexaDecimal = Integer.parseInt(conteudoEntrada, 16);
			return Integer.toOctalString(hexaDecimal);
		}
		
		int decimal = Integer.parseInt(conteudoEntrada);
		return Integer.toOctalString(decimal);
	}
	
	public static String traduzParaBinario(String sistemaNumerico, String conteudoEntrada) {
		if (sistemaNumerico.equals("octal")) {
			int octal = Integer.parseInt(conteudoEntrada, 8);
			return Integer.toBinaryString(octal);
		} else if (sistemaNumerico.equals("binario")) {
			int hexaDecimal = Integer.parseInt(conteudoEntrada, 16);
			return Integer.toBinaryString(hexaDecimal);
		}
		
		int decimal = Integer.parseInt(conteudoEntrada);
		return Integer.toBinaryString(decimal);
	}
	
	public static String validaEntrada(String entrada, int tipoEntrada) {
		if (tipoEntrada == 0) {
			if (entrada.matches("^[0-9A-Fa-f]+$"))
			   return "hexadecimal";
		} else if (tipoEntrada == 1) {
			if (entrada.matches("^[0-9]+"))
			    return "decimal";
		} else if (tipoEntrada == 2) {
			if (entrada.matches("^[0-7]+$"))
				return "octal";
		} else if (tipoEntrada == 3) {
			if (entrada.matches("^[01]+$"))
			    return "binario";
		}
			
		return "nenhum";
	}
	
	public static void traduzConteudo(JTextField textFieldEntrada, JTextField textFieldSaida, JComboBox comboBoxEntrada, JComboBox comboBoxSaida) {
		String conteudoEntrada = textFieldEntrada.getText();
		
		if (conteudoEntrada.length() > 0) {
			int tipoEntrada = comboBoxEntrada.getSelectedIndex();
			int tipoSaidaDesejada = comboBoxSaida.getSelectedIndex();
			conteudoEntrada = conteudoEntrada.replace(" ","");
						
			String sistemaNumerico = validaEntrada(conteudoEntrada, tipoEntrada);
			
			if (sistemaNumerico.equals("nenhum"))
				popUpErro("Ops...o número informado é invalido.", "Entrada inválida");
			else {
				if (tipoEntrada != tipoSaidaDesejada) {
					String traducao = "";
					if (tipoSaidaDesejada == 0)
						traducao = traduzParaHexaDecimal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 1)
						traducao = traduzParaDecimal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 2)
						traducao = traduzParaOctal(sistemaNumerico, conteudoEntrada);
					else if (tipoSaidaDesejada == 3)
						traducao = traduzParaBinario(sistemaNumerico, conteudoEntrada);

					textFieldSaida.setText(traducao);
				} else
					textFieldSaida.setText(conteudoEntrada);				
			} 
		} else
			popUpErro("Ops...parece que você esqueceu de digitar o número.", "Entrada inválida");
	}
}
