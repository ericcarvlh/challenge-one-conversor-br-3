package view.menuPrincipal;

import java.awt.EventQueue;

import javax.swing.JFrame;

import view.conversor.Moeda;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipal window = new MenuPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MenuPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnAbrirNovoMenu = new JButton("New button");
		btnAbrirNovoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Moeda.main(null);
				frame.setVisible(false);
			}
		});
		btnAbrirNovoMenu.setBounds(39, 208, 89, 23);
		frame.getContentPane().add(btnAbrirNovoMenu);
	}
}
