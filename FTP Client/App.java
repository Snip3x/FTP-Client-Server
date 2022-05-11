package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import java.awt.Color;

public class App {

	private JFrame frame;
	private JTextField IpField;
	private JTextField PwField;
	static Socket s;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
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
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setForeground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		IpField = new JTextField();
		IpField.setForeground(Color.WHITE);
		IpField.setBackground(Color.LIGHT_GRAY);
		IpField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IpField.setBounds(127, 48, 260, 34);
		frame.getContentPane().add(IpField);
		IpField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(24, 58, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		PwField = new JTextField();
		PwField.setForeground(Color.WHITE);
		PwField.setBackground(Color.LIGHT_GRAY);
		PwField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		PwField.setColumns(10);
		PwField.setBounds(127, 112, 260, 34);
		frame.getContentPane().add(PwField);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(24, 115, 107, 24);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setForeground(Color.WHITE);
		btnConnect.setBackground(Color.GRAY);
		btnConnect.setBounds(145, 186, 137, 45);
		frame.getContentPane().add(btnConnect);
		
		JLabel error = new JLabel("");
		error.setFont(new Font("Tahoma", Font.PLAIN, 16));
		error.setForeground(Color.RED);
		error.setBounds(147, 11, 152, 26);
		frame.getContentPane().add(error);
		
		
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					s = new Socket(IpField.getText(),3000);
					
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					
					dout.writeUTF(PwField.getText());
					dout.flush();
					
					DataInputStream iout = new DataInputStream(s.getInputStream());
					if(iout.readUTF().equals("OK")){
						new FileScreen(true).Start();
						frame.dispose();
						
					}else {
						
						error.setText("Error");
						dout.close();
						s.close();
					}
					
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			}
		});
		


		
		
	}
}
