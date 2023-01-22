package view.conversor;

import java.awt.EventQueue;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import metodos.ConversorMoeda;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jfree.chart.JFreeChart;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.Font;

public class Moeda extends JPanel {

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
		
		JLabel lblValorMoedaBase = new JLabel("1 USD = X BRL");
		lblValorMoedaBase.setFont(new Font("Arial", Font.PLAIN, 11));
		lblValorMoedaBase.setBounds(10, 159, 202, 27);
		frmConversorDeMoeda.getContentPane().add(lblValorMoedaBase);
		
		JLabel lblValorMoedaConversao = new JLabel("1 BRL = X USD");
		lblValorMoedaConversao.setFont(new Font("Arial", Font.PLAIN, 11));
		lblValorMoedaConversao.setBounds(10, 195, 202, 27);
		frmConversorDeMoeda.getContentPane().add(lblValorMoedaConversao);
		
		Currency currency = Currency.getInstance(Locale.getDefault());
		String siglaMoeda = currency.getCurrencyCode();
		
		DTOMoeda dtoMoeda = new DTOMoeda();
		DAOMoeda daoMoeda = new DAOMoeda();
		
		daoMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoeda);

		JComboBox comboBoxMoedaBase = new JComboBox();
		comboBoxMoedaBase.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));
		String currencyCode = currency.getCurrencyCode();
		int indexDefaultCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), currencyCode); // atribuinfo a moeda local como padrão.
		comboBoxMoedaBase.setSelectedIndex(indexDefaultCurrency);
		comboBoxMoedaBase.setBounds(10, 63, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaBase);
				
		textFieldMoedaBase = new JTextField();
		textFieldMoedaBase.setBounds(222, 63, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaBase);
		textFieldMoedaBase.setColumns(10);
		
		JComboBox comboBoxMoedaConversao = new JComboBox();
		comboBoxMoedaConversao.setModel(new DefaultComboBoxModel(dtoMoeda.getMoedas()));
		String moedaConversao = "USD"; // atribuindo USD como padrão.	
		int indexDefaultConversionCurrency = ConversorMoeda.buscaIndexSigla(dtoMoeda.getMoedas(), moedaConversao);
		comboBoxMoedaConversao.setSelectedIndex(indexDefaultConversionCurrency);
		comboBoxMoedaConversao.setBounds(10, 111, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaConversao);
		
		Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(currencyCode, moedaConversao);
		String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
						
		double valoresMoedaPorDia[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
		
		new GraficoMoeda(datas, valoresMoedaPorDia, currencyCode, moedaConversao);
				
		textFieldMoedaConversao = new JTextField();
		textFieldMoedaConversao.setBounds(222, 111, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaConversao);
		textFieldMoedaConversao.setColumns(10);
		
		String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(currencyCode), 
		nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(moedaConversao),
		infoConversao = String.format("De %s para %s.", nomeMoedaBase, nomeMoedaConversao);
		
		JLabel lblNewLabel = new JLabel(infoConversao);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 25, 382, 27);
		frmConversorDeMoeda.getContentPane().add(lblNewLabel);
		
		ConversorMoeda.atualizaLabelMoedas(lblValorMoedaBase, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao, 0);
		ConversorMoeda.atualizaLabelMoedas(lblValorMoedaConversao, dtoMoeda.getValoresMoedas(), comboBoxMoedaBase, comboBoxMoedaConversao,  1);
		
		comboBoxMoedaBase.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String conteudoComboBoxBase = (String) comboBoxMoedaBase.getSelectedItem();
				String siglaMoedaBase = ConversorMoeda.retornaSiglaMoeda(conteudoComboBoxBase);
				
				DAOMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoedaBase);
				
				String conteudoComboBoxConversao = (String) comboBoxMoedaConversao.getSelectedItem();
				String siglaMoedaConversao = ConversorMoeda.retornaSiglaMoeda(conteudoComboBoxConversao);
				
				Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoedaBase, siglaMoedaConversao);
				String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
								
				double val[] = ConversorMoeda.extraiValorMoedaMapObjeto(dadosMoeda);
				
				new GraficoMoeda(datas, val, siglaMoedaBase, siglaMoedaConversao);
			}
		});
		
		// se a caixa de texto da moeda base for alterada entao pegamos o falor e calculamos.
		textFieldMoedaBase.getDocument().addDocumentListener(new DocumentListener() {
			int tipoConversao = 0;
			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(), dtoMoeda.getMoedas(), tipoConversao);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(), dtoMoeda.getMoedas(), tipoConversao);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas(), tipoConversao);
			}
		});
		
		// se a caixa de texto da moeda de conversão for alterada entao pegamos o falor e calculamos.
		textFieldMoedaConversao.getDocument().addDocumentListener(new DocumentListener() {
			int tipoConversao = 1;
			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas(), tipoConversao);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas(), tipoConversao);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas(), tipoConversao);
			}
			
		});
		
		JButton btnMenu = new JButton("Menu");
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnMenu.setBounds(10, 291, 89, 37);
		frmConversorDeMoeda.getContentPane().add(btnMenu);
	}
}
