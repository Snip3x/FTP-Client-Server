package client;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class FileScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	
	
	public FileScreen(boolean bol) {
		bol = true;
	}
	

	/**
	 * Launch the application.
	 */
	public void Start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileScreen frame = new FileScreen();
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
	public FileScreen() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 417);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 62, 445, 244);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File Name", "Date"
			}
		) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		scrollPane.setViewportView(table);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(10, 310, 89, 23);
		contentPane.add(btnDownload);
		
		JButton btnUpload = new JButton("Upload");
		
		btnUpload.setBounds(10, 344, 89, 23);
		contentPane.add(btnUpload);
		
		textField = new JTextField();
		textField.setBounds(109, 345, 228, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				filechooser.showOpenDialog(btnSelect);
				textField.setText(filechooser.getSelectedFile().getAbsolutePath());
				
				
				
			}
		});
		btnSelect.setBounds(347, 344, 81, 23);
		contentPane.add(btnSelect);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnExit.setBackground(Color.RED);
		btnExit.setBounds(347, 310, 81, 23);
		contentPane.add(btnExit);
		
		DefaultTableModel model =  (DefaultTableModel) table.getModel();
		
		
		DataInputStream iout = new DataInputStream(App.s.getInputStream());
		DataOutputStream dout = new DataOutputStream(App.s.getOutputStream());
		updateFile(iout, model);
		
		
		
		
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dout.writeInt(2);
					String filename = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					dout.writeUTF(filename);
					String size = iout.readUTF();
					System.out.println(filename+size);
					saveFile(filename, Integer.parseInt(size));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")) {
					try {
						sendFile(textField.getText(),dout);
						iout.readBoolean();
						System.out.println("done");
						updateFile(iout, model);
						
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}}
		});
		
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					iout.close();
					dout.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
}
	
	
	private void updateFile(DataInputStream iout, DefaultTableModel model) throws IOException {
		// TODO Auto-generated method stub
		model.setRowCount(0);
		int len = iout.readInt();
		for(int i=0; i<len;i++) {
			String[] f = iout.readUTF().split("/");
			model.addRow(new Object[] {f[0], f[1]});
			
			
		}
		

	}
    public void sendFile(String file, DataOutputStream dos) throws IOException {
        
        dos.writeInt(1);
        dos.flush();
        File fi = new File(file);
        String name = file.split("\\\\")[file.split("\\\\").length-1];
        System.out.println(fi.length());
        dos.writeUTF(name+"<->"+fi.length());
        dos.flush();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int count;
        while ((count = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, count);
            
        }
       
        dos.flush();
        
        

          
    }
    
    
    private void saveFile(String fileName, int filesize) throws IOException {
        DataInputStream dis = new DataInputStream(App.s.getInputStream());
        FileOutputStream fos = new FileOutputStream("D:\\DevTools\\DCCN\\FTP file Saver\\"+fileName);
        System.out.println(fileName+" "+filesize);
        // Send file size in separate msg
        int read = 0;
        System.out.println("waiting for file");
        byte[] buffer = new byte[8192];
        while (filesize > 0 && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, filesize))) != -1)
        {
            fos.write(buffer,0,read);
            filesize -= read;
            }
        System.out.println(fileName+" Downloaded");        
        
    }
}