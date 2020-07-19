package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.UserList.UserListCallback;

import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame implements UserListCallback{

	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JTextField textFieldPass;
	private Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	// nghe k
	// k m
	// bat ben m ak
	// thanh phia tren
	private void doLogin() throws IOException {
		String username = textFieldUsername.getText();
		String pass = textFieldPass.getText();
		
		if(client.login(username, pass)) {
			setVisible(false);
			UserList frame = new UserList(client, username);
			frame.setCallback(this);
			frame.setVisible(true);
			
		} else {
			JOptionPane.showMessageDialog(this, "Sai username/password.");
		}
	}
	public LoginFrame() {
		this.client = new Client("localhost", 3006);
		client.connect();
		
		setTitle("\u0110\u0103ng nh\u00E2\u0323p");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setColumns(10);
		
		textFieldPass = new JTextField();
		textFieldPass.setColumns(10);
		
		JButton btnLogin = new JButton("Xa\u0301c nh\u00E2\u0323n");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doLogin();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(50)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldPass, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(157)
							.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(54)
					.addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textFieldPass, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(60, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

	public void onClosingWindowClick() {
		setVisible(true);
		System.out.println("zo");
	}
	
	
}
