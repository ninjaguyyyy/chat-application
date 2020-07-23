package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MessageFrame extends JFrame implements MessageListener {

	private JPanel contentPane;
	private Client client;
	private String username;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JTextField textField;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client client = new Client("localhost", 3006);
					MessageFrame frame = new MessageFrame(client, "chi");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param username 
	 * @param client 
	 */
	public MessageFrame(final Client client, final String username) {
		setTitle("Message: -> " + username);
		this.client = client;
		this.username = username;
		
		client.addMessageListener(this);
		
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(28, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE))
					.addGap(25))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					String text = textField.getText();
					client.msg(username, text);
					listModel.addElement("You: " + text);
					textField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		final JButton btnSendFile = new JButton("G\u01B0\u0309i file");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("e");
				JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            int option = fileChooser.showOpenDialog(MessageFrame.this);
	            if(option == JFileChooser.APPROVE_OPTION) {
	            	System.out.println("voday");
	            	;
	            	File file = fileChooser.getSelectedFile();
	            	System.out.println(file.getName());
//	            	try {
//						client.sendFile(username, file);
//					} catch (IOException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					}
	            	listModel.addElement("You: " + "da gui 1 file - " + file.getName());
	            	try {
						String base64 = FileHandle.encodeFileToBase64Binary(file);
						System.out.println(base64);
						client.sendFile2(username, base64, file.getName());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            } else {
	               // handle when cancel
	            }
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSendFile)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
					.addComponent(btnSendFile))
		);
		panel_1.setLayout(gl_panel_1);
		
		JList listMessage = new JList(listModel);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(listMessage, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(listMessage, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	public void onMessage(String fromUsername, String msgBody) {
		if(username.equalsIgnoreCase(fromUsername)) {
			String line = fromUsername + ": " + msgBody;
			listModel.addElement(line);
		}
		
	}
	
	
}
