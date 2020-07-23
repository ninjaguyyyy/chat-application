package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserList extends JFrame implements UserStatusListener {

	private JPanel contentPane;
	private final Client client;
	private DefaultListModel<String> userListModel;

	
	/**
	 * Launch the application.
	 */
	
	

	public void handleLogout() throws IOException {
		client.logout();
		dispose();
		RegisterFrame reg = new RegisterFrame();
		reg.setVisible(true);
	}

	/**
	 * Create the frame.
	 * @param username 
	 */
	public UserList(final Client client, String username) {
		setTitle("Trang chu\u0309 - " + username);
		this.client = client;
		this.client.addUserStatusListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					client.logout();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		JLabel lblOnlineUsers = new JLabel("Ca\u0301c user \u0111ang online");
		lblOnlineUsers.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnLogout = new JButton("\u0110\u0103ng xu\u00E2\u0301t");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					handleLogout();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblOnlineUsers)
						.addComponent(btnLogout))
					.addPreferredGap(ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addGap(29))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblOnlineUsers)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnLogout))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		
		userListModel = new DefaultListModel<String>();
		final JList<String> listUser = new JList<String>(userListModel);
		
		listUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() > 1) {
					String username = listUser.getSelectedValue();
					MessageFrame messageFrame = new MessageFrame(client, username);
					messageFrame.setVisible(true);
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(listUser)
					.addContainerGap(119, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(listUser)
					.addContainerGap(170, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	public void online(String username) {
		userListModel.addElement(username);
	}

	public void offline(String username) {
		userListModel.removeElement(username);
	}
}
