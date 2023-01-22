package view.menuPrincipal;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import metodos.MenuPrincipal;
import view.conversor.moeda.Moeda;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Menu {

	private JFrame frmMenuPrincipal;
	private JTextField textFieldDataAtual;
	private JTextField textFieldHoraAtual;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frmMenuPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMenuPrincipal = new JFrame();
		frmMenuPrincipal.setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/view/menuPrincipal/menu_principal_icon.png")));
		frmMenuPrincipal.setTitle("Menu Principal");
		frmMenuPrincipal.setBounds(100, 100, 450, 300);
		frmMenuPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMenuPrincipal.getContentPane().setLayout(null);
		
		JLabel lblBoasVindas = new JLabel("Bom dia, bem vindo!");
		lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 13));
		lblBoasVindas.setBounds(10, 11, 273, 55);
		frmMenuPrincipal.getContentPane().add(lblBoasVindas);
		

		textFieldDataAtual = new JTextField();
		textFieldDataAtual.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldDataAtual.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldDataAtual.setEditable(false);
		textFieldDataAtual.setBounds(293, 38, 131, 28);
		frmMenuPrincipal.getContentPane().add(textFieldDataAtual);
		textFieldDataAtual.setColumns(10);
		
		textFieldHoraAtual = new JTextField();
		textFieldHoraAtual.setEditable(false);
		textFieldHoraAtual.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldHoraAtual.setFont(new Font("Arial", Font.PLAIN, 12));
		textFieldHoraAtual.setBounds(293, 11, 131, 28);
		frmMenuPrincipal.getContentPane().add(textFieldHoraAtual);
		textFieldHoraAtual.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Qual funcionalidade voc\u00EA deseja acessar?");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 98, 273, 28);
		frmMenuPrincipal.getContentPane().add(lblNewLabel);
		
		JComboBox comboBoxFuncionalidades = new JComboBox();
		String[] funcionalidades = {"Conversor de moeda", 
				"Conversor de sistema númerico"};
		comboBoxFuncionalidades.setModel(new DefaultComboBoxModel(funcionalidades));
		comboBoxFuncionalidades.setBounds(10, 125, 273, 36);
		frmMenuPrincipal.getContentPane().add(comboBoxFuncionalidades);
		
		MenuPrincipal.atualizaInterface(textFieldHoraAtual, lblBoasVindas, textFieldDataAtual);
		
		comboBoxFuncionalidades.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				int indexFuncionalidade = comboBoxFuncionalidades.getSelectedIndex();
				MenuPrincipal.acessaFuncionalidade(frmMenuPrincipal, indexFuncionalidade);
			}
		});
		
	}
}
