package sait.bankonit.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;
import ca.bankonit.exceptions.*;
import ca.bankonit.manager.*;
import ca.bankonit.models.*;

/**
 * Renders the account window.
 * @author Nick Hamnett
 * @version Aug 3, 2021
 */

public class AccountWindow extends JFrame {
	//Fields
	private Account account;
	private BankManager bankManager;
	
	private JButton submitButton;
	private JButton signoutButton;
	
	private JTextField textBox;
	
	private JRadioButton DepositButton;
	private JRadioButton WithdrawButton;
	
	private String display;
	private String display2;
	
	private JList ListTransactions;
	private JScrollPane scrollPane;
	
	private JLabel cardNumber;
	private JLabel balanceLabel;
	
	ImageIcon logo = new ImageIcon("Logo/logo.PNG");
	
	/**
	 * Initializes the account window
	 * @param account Account to manage
	 */
	public AccountWindow(Account account) {
		super("Bank On It Account");
		
		// Store account as field.
		this.account = account;
		
		this.bankManager = BankManagerBroker.getInstance();
		
		// Set size to 600x500
		this.setSize(700, 600);
		
		Image image = logo.getImage(); 
		Image newimg = image.getScaledInstance(500, 500,  java.awt.Image.SCALE_SMOOTH);  
		logo = new ImageIcon(newimg);
		this.setIconImage(logo.getImage());
		
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Center Account window in screen
		this.setLocationRelativeTo(null);
		
		// Create panel
		JPanel panel = this.createMainPanel();
		
		this.populateTransactions();
				
		// Add JPanel to the JFrame
		super.add(panel);
		
		
	}
	
	
	//Main Panel
	private JPanel createMainPanel() {
		JPanel panel = new JPanel();		
		panel.setLayout(new BorderLayout());
		
		panel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		JPanel panelTopCenter = this.TopCenterPanel();
		panel.add(panelTopCenter, BorderLayout.NORTH);
		
		JPanel panelBottomCenter = this.BottomCenterPanel();
		panel.add(panelBottomCenter, BorderLayout.SOUTH);
		
		JPanel ListPanel = this.MiddlePanel();
		panel.add(ListPanel, BorderLayout.CENTER);

				
		return panel;
	}
	
	//Northern Panel
	private JPanel TopCenterPanel() {
		JPanel panelTopCenter = new JPanel();
		panelTopCenter.setLayout(new BorderLayout());
		panelTopCenter.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		long accountCardNumber = this.account.getCardNumber();
		String cardNumberFormatted = String.format("Card #%d" , accountCardNumber);
		
		this.cardNumber = new JLabel(cardNumberFormatted); 
		this.cardNumber.setHorizontalAlignment(SwingConstants.CENTER);
		cardNumber.setFont(new Font("Default", Font.BOLD, 18));
						
		this.balanceLabel = new JLabel("Balance:");
		balanceLabel.setFont(new Font("Default", Font.BOLD, 15));				
		this.balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelTopCenter.add(this.cardNumber,BorderLayout.NORTH);
		panelTopCenter.add(this.balanceLabel,BorderLayout.CENTER);
		
		return panelTopCenter;
	}
	
	//Southern Panel
	private JPanel BottomCenterPanel() {
		JPanel panelBottomCenter = new JPanel();
		panelBottomCenter.setLayout(new FlowLayout());
		panelBottomCenter.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		JLabel label1 = new JLabel("Type: ");
		label1.setFont(new Font("Default", Font.BOLD, 15));
		panelBottomCenter.add(label1);
		
		
		DepositButton = new JRadioButton("Deposit");
		DepositButton.addActionListener(new MyActionListener());
		WithdrawButton = new JRadioButton("Withdraw");
		WithdrawButton.addActionListener(new MyActionListener());
		
		ButtonGroup radio = new ButtonGroup();
		radio.add(DepositButton);
		radio.add(WithdrawButton);
		
		panelBottomCenter.add(DepositButton);
		panelBottomCenter.add(WithdrawButton);
		
		JLabel label2 = new JLabel("Amount:");
		label2.setFont(new Font("Default", Font.BOLD, 15));
		panelBottomCenter.add(label2);
		
		this.textBox = new JTextField();
		textBox.addActionListener(new MyActionListener());
		textBox.setPreferredSize(new Dimension(200,30));
		
		panelBottomCenter.add(textBox);
		
		this.submitButton = new JButton("Submit");
		this.submitButton.addActionListener(new MyActionListener());
		
		this.signoutButton = new JButton("SIGNOUT");
		this.signoutButton.addActionListener(new MyActionListener());
		
		panelBottomCenter.add(submitButton);
		
		panelBottomCenter.add(signoutButton);
		 
		
		return panelBottomCenter;
	}
	
	//Middle Panel Containing Transaction Data
	private JPanel MiddlePanel(){
		
		JPanel ListPanel = new JPanel();
		ListPanel.setLayout(new GridLayout());
		ListPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		this.ListTransactions = new JList();
		this.scrollPane = new JScrollPane(this.ListTransactions);
			
		ListPanel.add(scrollPane);
		
			
		return ListPanel;
	}
	
	class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(AccountWindow.this.submitButton.equals(source)) {
				try {
					String inputAmount = AccountWindow.this.textBox.getText();
					double amount = Double.parseDouble(inputAmount);
					
					if(AccountWindow.this.WithdrawButton.isSelected()) {
						AccountWindow.this.bankManager.withdraw(account, amount);
						
						AccountWindow.this.populateTransactions();
						
						AccountWindow.this.bankManager.persist();
						
						JOptionPane.showMessageDialog(AccountWindow.this, "Withdraw complete.");
					
					}
					else if(AccountWindow.this.DepositButton.isSelected()) {
						AccountWindow.this.bankManager.deposit(account, amount);
						
						AccountWindow.this.populateTransactions();
						
						AccountWindow.this.bankManager.persist();
						
						JOptionPane.showMessageDialog(AccountWindow.this, "Deposit complete.");
					}
				}catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(AccountWindow.this, "Invalid amount. Please try again.");
				} catch (InvalidAccountException ex2) {
					JOptionPane.showMessageDialog(AccountWindow.this, "Withdraw Error. Please try again.");
				}
								
			}
			else if(source == AccountWindow.this.signoutButton) {
				//AccountWindow.this.bankManager.persist();
				
				AccountWindow.this.setVisible(false);
				System.exit(0);
				
			}
			
		}
	}
	 
	/**
	 * Clears and re-populates transactions as well as updates balance.
	 *  
	 */
	private void populateTransactions() {
		try {
			double balance = 0;
			ArrayList<Transaction> transactions = this.bankManager.getTransactionsForAccount(account);
			String[] items = new String[transactions.size()];
			
			for (int i = 0; i < transactions.size(); i++) {
				Transaction transaction = transactions.get(i);
				items[i] = transaction.toString();
				if(transaction.getTransactionType() == Transaction.TYPE_WITHDRAW) {
					balance -= transaction.getAmount();
				}
				else {
					balance += transaction.getAmount();
				}
			}
			
			this.balanceLabel.setText(String.format("Balance: $%.2f", balance ));
			this.ListTransactions.setListData(items);
		} catch (InvalidAccountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
}
