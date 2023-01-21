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
		
		textFieldMoedaConversao = new JTextField();
		textFieldMoedaConversao.setBounds(222, 111, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaConversao);
		textFieldMoedaConversao.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("De () para ()");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 25, 202, 27);
		frmConversorDeMoeda.getContentPane().add(lblNewLabel);
		
		comboBoxMoedaBase.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String conteudoComboBoxBase = (String) comboBoxMoedaBase.getSelectedItem();
				String siglaMoedaBase = ConversorMoeda.retornaSiglaMoeda(conteudoComboBoxBase);
				
				daoMoeda.atribuiDadosMoeda(dtoMoeda, siglaMoedaBase);
				
				String conteudoComboBoxConversao = (String) comboBoxMoedaConversao.getSelectedItem();
				String siglaMoedaConversao = ConversorMoeda.retornaSiglaMoeda(conteudoComboBoxConversao);
				
				Map<String, Object> dadosMoeda = ConversorMoeda.retornaDadosHistoricoMoeda(siglaMoedaBase, siglaMoedaConversao);
				String datas[] = ConversorMoeda.retornaChavesMap(dadosMoeda);
				
				double val[] = new double[datas.length];
				
				int index = 0;
				for (Map.Entry<String, Object> entry : dadosMoeda.entrySet()) {
				    String valor = entry.getValue().toString();
				    valor = valor.replace("{", "");
				    valor = valor.replace("}", "");
				    String chave = valor.split("=")[1];
				    val[index] = Double.parseDouble(chave);
				    index++;
				}
				
				GraficoMoeda grafico = new GraficoMoeda(datas, val, siglaMoedaBase, siglaMoedaConversao);
			}
		});
		
		// se a caixa de texto da moeda base for alterada entao pegamos o falor e calculamos.
		textFieldMoedaBase.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(), dtoMoeda.getMoedas());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(), dtoMoeda.getMoedas());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversaoMoedaBase(textFieldMoedaBase, textFieldMoedaConversao, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas());
			}
		});
		
		// se a caixa de texto da moeda de conversão for alterada entao pegamos o falor e calculamos.
		textFieldMoedaConversao.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ConversorMoeda.calculaConversao(textFieldMoedaConversao, textFieldMoedaBase, comboBoxMoedaConversao, dtoMoeda.getValoresMoedas(),  dtoMoeda.getMoedas());
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
