package view.conversor.moeda;

import java.awt.EventQueue;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import metodos.ConversorMoeda;
import view.menuPrincipal.Menu;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.Font;

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
		frmConversorDeMoeda.setIconImage(Toolkit.getDefaultToolkit().getImage(Moeda.class.getResource("/view/conversor/moeda/money_icon.png")));
		frmConversorDeMoeda.setTitle("Conversor de moeda");
		frmConversorDeMoeda.setBounds(100, 100, 460, 390);
		frmConversorDeMoeda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConversorDeMoeda.getContentPane().setLayout(null);
		
		JLabel lblValorMoedaBase = new JLabel("1 X = X X");
		lblValorMoedaBase.setFont(new Font("Arial", Font.PLAIN, 11));
		lblValorMoedaBase.setBounds(10, 159, 202, 27);
		frmConversorDeMoeda.getContentPane().add(lblValorMoedaBase);
		
		JLabel lblValorMoedaConversao = new JLabel("1 X = X X");
		lblValorMoedaConversao.setFont(new Font("Arial", Font.PLAIN, 11));
		lblValorMoedaConversao.setBounds(10, 195, 202, 27);
		frmConversorDeMoeda.getContentPane().add(lblValorMoedaConversao);

		JComboBox comboBoxMoedaBase = new JComboBox();
		comboBoxMoedaBase.setBounds(10, 63, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaBase);
		
		JComboBox comboBoxMoedaConversao = new JComboBox();
		comboBoxMoedaConversao.setBounds(10, 111, 202, 37);
		frmConversorDeMoeda.getContentPane().add(comboBoxMoedaConversao);
		
		textFieldMoedaBase = new JTextField();
		textFieldMoedaBase.setBounds(222, 63, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaBase);
		textFieldMoedaBase.setColumns(10);
		
		textFieldMoedaConversao = new JTextField();
		textFieldMoedaConversao.setBounds(222, 111, 135, 37);
		frmConversorDeMoeda.getContentPane().add(textFieldMoedaConversao);
		textFieldMoedaConversao.setColumns(10);
		
		JLabel lblInfo = new JLabel("De X para X.");
		lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
		lblInfo.setBounds(10, 25, 382, 27);
		frmConversorDeMoeda.getContentPane().add(lblInfo);
		
		DTOMoeda dtoMoeda = new DTOMoeda();
		DAOMoeda daoMoeda = new DAOMoeda();
		
		ConversorMoeda.colocaConfigucaoPadraoConversor(daoMoeda, dtoMoeda, comboBoxMoedaConversao, comboBoxMoedaBase, lblInfo, lblValorMoedaBase, lblValorMoedaConversao);
		
		comboBoxMoedaBase.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConversorMoeda.alteraMoedaConversao(comboBoxMoedaBase, comboBoxMoedaConversao, dtoMoeda, daoMoeda, lblInfo, lblValorMoedaBase, lblValorMoedaConversao);
			}
		});
		
		comboBoxMoedaConversao.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConversorMoeda.alteraMoedaConversao(comboBoxMoedaBase, comboBoxMoedaConversao, dtoMoeda, daoMoeda, lblInfo, lblValorMoedaBase, lblValorMoedaConversao);
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
				new Menu().main(null);
				frmConversorDeMoeda.dispose();
			}
		});
		
		btnMenu.setBounds(123, 303, 89, 37);
		frmConversorDeMoeda.getContentPane().add(btnMenu);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSair.setBounds(222, 303, 89, 37);
		frmConversorDeMoeda.getContentPane().add(btnSair);
	}
}