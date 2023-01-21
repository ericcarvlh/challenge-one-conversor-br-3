package view.conversor;

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import metodos.ConversorMoeda;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.AbstractListModel;
import java.awt.Choice;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

public class Moeda {

	private JFrame frmConversorDeMoeda;
	private JTextField textFieldMoedaBase;
	private JTextField textFieldMoedaConversao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Moeda window = new Moeda();
					window.frmConversorDeMoeda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Moeda() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConversorDeMoeda = new JFrame();
		frmConversorDeMoeda.setIconImage(Toolkit.getDefaultToolkit().getImage(Moeda.class.getResource("/view/conversor/money_icon.png")));
		frmConversorDeMoeda.setTitle("Conversor de moeda");
		frmConversorDeMoeda.setBounds(100, 100, 570, 390);
		frmConversorDeMoeda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConversorDeMoeda.getContentPane().setLayout(null);
		
		Map<String, Object> moedas = ConversorMoeda.retornaListaMoedas();
		System.out.println(moedas);
		String[] chavesMoedas = ConversorMoeda.retornaListaChavesMoedas(moedas);
		double[] valoresMoedas = ConversorMoeda.retornaListaValoresMoedas(moedas);
//	    Currency currency = Currency.getInstance(moedaBase);
//	    String simboloMoedaBase = currency.getSymbol();
//		Double valorMoedaConversao = Double.parseDouble(moedas.get((Object) moedaConversao).toString()),
//		valorMoedaBase = montante / valorMoedaConversao;

		JComboBox comboBoxMoedaBase = new JComboBox();
		comboBoxMoedaBase.setModel(new DefaultComboBoxModel(chavesMoedas));
		Currency currency = Currency.getInstance(Locale.getDefault());
		String currencyCode = currency.getCurrencyCode();
		int indexDefaultCurrency = ConversorMoeda.buscaIndexSigla(chavesMoedas, currencyCode); // atribuinfo a moeda local como padrão.
		comboBoxMoedaBase.setSelectedIndex(indexDefaultCurrency);
		comboBoxMoedaBase.setBounds(10, 111, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaBase);
		
		JButton btnMenu = new JButton("Menu");
		btnMenu.setBounds(10, 291, 89, 37);
		frmConversorDeMoeda.getContentPane().add(btnMenu);
		
		textFieldMoedaBase = new JTextField();
		textFieldMoedaBase.setBounds(222, 111, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaBase);
		textFieldMoedaBase.setColumns(10);
		
		JComboBox comboBoxMoedaConversao = new JComboBox();
		comboBoxMoedaConversao.setModel(new DefaultComboBoxModel(chavesMoedas));
		String moedaConversao = "USD"; // atribuindo USD como padrão.	
		int indexDefaultConversionCurrency = ConversorMoeda.buscaIndexSigla(chavesMoedas, moedaConversao);
		comboBoxMoedaConversao.setSelectedIndex(indexDefaultConversionCurrency);
		comboBoxMoedaConversao.setBounds(10, 63, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaConversao);
		
		textFieldMoedaConversao = new JTextField();
		textFieldMoedaConversao.setBounds(222, 63, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaConversao);
		textFieldMoedaConversao.setColumns(10);
		
		// se a caixa de texto da moeda base for alterada entao pegamos o falor e calculamos.
		textFieldMoedaBase.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, valoresMoedas);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, valoresMoedas);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, valoresMoedas);
			}
		});
		
		// se a caixa de texto da moeda de conversão for alterada entao pegamos o falor e calculamos.
		textFieldMoedaConversao.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, valoresMoedas);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, valoresMoedas);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, valoresMoedas);
			}
			
		});
	}
}
