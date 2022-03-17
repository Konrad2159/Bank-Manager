package sait.bankonit.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ca.bankonit.exceptions.InvalidAccountException;
import ca.bankonit.manager.*;
import ca.bankonit.models.*;
import sait.bankonit.gui.AccountWindow.MyActionListener;

/**
 * Renders the login window
 * @author Nick Hamnett
 * @version Aug 3, 2021
 */
public class LoginWindow extends JFrame {
	//fields
	JButton submitButton;
	JTextField cardNumber;
	JTextField pin;
	
	ImageIcon logo = new ImageIcon("Logo/logo.PNG");
	
	/**
	 * Initializes the login window.
	 */
	public LoginWindow() {
		super("Bank On It Login");
		
		// Set window size to 500x150
		this.setSize(500, 150);
		
		// Cause process to exit when X is clicked.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Center login window in screen
		this.setLocationRelativeTo(null);
		
		Image image = logo.getImage(); 
		Image newimg = image.getScaledInstance(500, 500,  java.awt.Image.SCALE_SMOOTH);  
		logo = new ImageIcon(newimg);
		this.setIconImage(logo.getImage());
		
		// Create panel
		JPanel panel = this.createMainPanel();
		
		panel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						
		// Add JPanel to the JFrame
		super.add(panel,BorderLayout.CENTER);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel();		
		panel.setLayout(new BorderLayout());
		
		//panel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		panel.add(this.centerPanel(), BorderLayout.CENTER);
		panel.add(this.topPanel(), BorderLayout.NORTH);
		panel.add(this.bottomPanel(), BorderLayout.SOUTH);
				
		return panel;
	}
	
	private JPanel centerPanel() {
		JPanel panelTopCenter = new JPanel();
		panelTopCenter.setLayout(new FlowLayout());
		//panelTopCenter.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		JLabel cardNumberLabel = new JLabel("Card Number:");
		cardNumberLabel.setFont(new Font("Default", Font.BOLD, 15));
		
		
		cardNumber = new JTextField();
		cardNumber.addActionListener(new signinActionListener());
		cardNumber.setPreferredSize(new Dimension(150,30));
		
		JLabel pinLabel = new JLabel("PIN:");
		pinLabel.setFont(new Font("Default", Font.BOLD, 15));
		
		pin = new JTextField();
		pin.addActionListener(new signinActionListener());
		pin.setPreferredSize(new Dimension(150,30));
		
		panelTopCenter.add(cardNumberLabel);
		panelTopCenter.add(cardNumber);
		
		panelTopCenter.add(pinLabel);
		panelTopCenter.add(pin);
		
		return panelTopCenter;
	}
	
	private JPanel topPanel() {
		JPanel topCenter = new JPanel();
		topCenter.setLayout(new BorderLayout());
		
		JLabel loginLabel = new JLabel("Bank On It Login");
		loginLabel.setFont(new Font("Default", Font.BOLD, 20));
		
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		topCenter.add(loginLabel,BorderLayout.NORTH);
	
		
		return topCenter;
	}
	
	private JPanel bottomPanel() {
		JPanel bottomCenter = new JPanel();
		bottomCenter.setLayout(new FlowLayout());
		
		this.submitButton = new JButton("SIGNIN");
		this.submitButton.addActionListener(new signinActionListener());
		
		bottomCenter.add(submitButton,BorderLayout.SOUTH);
	
		
		return bottomCenter;
	}
	
	private void login(long cardNumber, short pin) {
		BankManager bankManager = BankManagerBroker.getInstance();
		
		Account account = bankManager.login(cardNumber, pin);
		
		if(account != null) {
			AccountWindow accountWindow = new AccountWindow(account);
			accountWindow.setVisible(true);
			this.setVisible(false);
		}else {
			JOptionPane.showMessageDialog(LoginWindow.this, "Invalid login info. Please try again");
		}
	}
	
	class signinActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			long a = Long.parseLong(cardNumber.getText());
			short b = Short.parseShort(pin.getText());
			
			if(LoginWindow.this.submitButton.equals(source)) {
				login(a,b);
				
			}
			
		}
	}
}
