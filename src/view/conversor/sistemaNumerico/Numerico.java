package view.conversor.sistemaNumerico;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import metodos.ConversorSistemaNumerico;
import view.menuPrincipal.Menu;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Numerico {

	private JFrame frmSistemaNumerico;
	private JTextField textFieldEntrada;
	private JTextField textFieldSaida;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Numerico window = new Numerico();
					window.frmSistemaNumerico.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Numerico() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSistemaNumerico = new JFrame();
		frmSistemaNumerico.setIconImage(Toolkit.getDefaultToolkit().getImage(Numerico.class.getResource("/view/conversor/sistemaNumerico/binary_icon.png")));
		frmSistemaNumerico.setTitle("Conversor de sistema n\u00FAmerico");
		frmSistemaNumerico.setBounds(100, 100, 415, 300);
		frmSistemaNumerico.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSistemaNumerico.getContentPane().setLayout(null);

		String[] sistemasNumericos = {"Hexadecimal", "Decimal", "Octal", "Binario"};
		
		JComboBox comboBoxEntrada = new JComboBox();
		comboBoxEntrada.setModel(new DefaultComboBoxModel(sistemasNumericos));
		comboBoxEntrada.setBounds(37, 26, 106, 37);
		frmSistemaNumerico.getContentPane().add(comboBoxEntrada);
		
		JComboBox comboBoxSaida = new JComboBox();
		comboBoxSaida.setModel(new DefaultComboBoxModel(sistemasNumericos));
		comboBoxSaida.setBounds(189, 26, 106, 37);
		frmSistemaNumerico.getContentPane().add(comboBoxSaida);
		
		textFieldEntrada = new JTextField();
		textFieldEntrada.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldEntrada.setToolTipText("");
		textFieldEntrada.setBounds(73, 74, 222, 37);
		frmSistemaNumerico.getContentPane().add(textFieldEntrada);
		textFieldEntrada.setColumns(10);
		
		JLabel lblInfo1 = new JLabel("De");
		lblInfo1.setFont(new Font("Arial", Font.BOLD, 14));
		lblInfo1.setBounds(10, 30, 26, 27);
		frmSistemaNumerico.getContentPane().add(lblInfo1);
		
		JLabel lblPara = new JLabel("Para");
		lblPara.setFont(new Font("Arial", Font.BOLD, 14));
		lblPara.setBounds(150, 30, 35, 27);
		frmSistemaNumerico.getContentPane().add(lblPara);
		
		JLabel lblTexto = new JLabel("N\u00FAmero:");
		lblTexto.setFont(new Font("Arial", Font.BOLD, 14));
		lblTexto.setBounds(10, 78, 61, 27);
		frmSistemaNumerico.getContentPane().add(lblTexto);
		
		textFieldSaida = new JTextField();
		textFieldSaida.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldSaida.setEditable(false);
		textFieldSaida.setToolTipText("");
		textFieldSaida.setColumns(10);
		textFieldSaida.setBounds(56, 122, 239, 37);
		frmSistemaNumerico.getContentPane().add(textFieldSaida);
		
		JLabel lblSada = new JLabel("Sa\u00EDda:");
		lblSada.setFont(new Font("Arial", Font.BOLD, 14));
		lblSada.setBounds(10, 126, 61, 27);
		frmSistemaNumerico.getContentPane().add(lblSada);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSair.setBounds(206, 220, 89, 37);
		frmSistemaNumerico.getContentPane().add(btnSair);
		
		JButton btnMenu = new JButton("Menu");
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSistemaNumerico.dispose();
				Menu.main(null);
			}
		});
		btnMenu.setBounds(10, 220, 89, 37);
		frmSistemaNumerico.getContentPane().add(btnMenu);
		
		JButton btnTraduzir = new JButton("Traduzir");
		btnTraduzir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConversorSistemaNumerico.traduzConteudo(textFieldEntrada, textFieldSaida, comboBoxEntrada, comboBoxSaida);
			}
		});
		btnTraduzir.setBounds(109, 220, 89, 37);
		frmSistemaNumerico.getContentPane().add(btnTraduzir);
		
		JButton btnCopiar = new JButton("Cop\u00EDar");
		btnCopiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(textFieldSaida.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		btnCopiar.setBounds(300, 220, 89, 37);
		frmSistemaNumerico.getContentPane().add(btnCopiar);
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldEntrada.setText("");
				textFieldSaida.setText("");
			}
		});
		btnLimpar.setBounds(300, 74, 89, 37);
		frmSistemaNumerico.getContentPane().add(btnLimpar);
		
		
	}
}
