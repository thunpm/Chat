package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ServerGUI extends JFrame {
	public final int port = 7; 

	private JPanel contentPane;
	private JTextField txtIP; // IP Server
	private JTextField txtPort; // Cong Server
	public static JTextField txtOnline; // So Client dang online
	public static JTextArea notification; // Thong bao ket noi
	TCPServer server;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// update noi dung thong bao
	public static void updateNotification(String message) {
		notification.append(message + "\n");
	}
	
	// update so Client online
	public static void updateUserOnline(int number) {
		txtOnline.setText(String.valueOf(number));
	}

	public ServerGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 675, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));					// khung nhin chinh
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelIP = new JLabel("IP Server");
		labelIP.setFont(new Font("Tahoma", Font.BOLD, 12));					// label "IP Server"
		labelIP.setBounds(10, 11, 59, 21);
		contentPane.add(labelIP);
		
		txtIP = new JTextField();
		txtIP.setBackground(Color.WHITE);
		txtIP.setEditable(false);
		txtIP.setBounds(79, 12, 119, 20);
		contentPane.add(txtIP);
		txtIP.setColumns(10);												// khung hien thi IP Server
		try {
			txtIP.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		JLabel labelPort = new JLabel("Port");
		labelPort.setFont(new Font("Tahoma", Font.BOLD, 12));				// lable "Port"
		labelPort.setBounds(234, 11, 41, 21);
		contentPane.add(labelPort);
		
		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setBackground(Color.WHITE);
		txtPort.setBounds(281, 12, 71, 20);									// khung hien thi Port
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		txtPort.setText(String.valueOf(port));
		
		JLabel labelOnline = new JLabel("Online");
		labelOnline.setFont(new Font("Tahoma", Font.BOLD, 12));				// label "Online" (so Client online)
		labelOnline.setBounds(10, 43, 59, 21);
		contentPane.add(labelOnline);
		
		txtOnline = new JTextField("0");
		txtOnline.setEditable(false);
		txtOnline.setColumns(10);
		txtOnline.setBackground(Color.WHITE);								// khung hien thi so Client online, ban dau la 0
		txtOnline.setBounds(79, 44, 119, 20);
		contentPane.add(txtOnline);
		
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				server = new TCPServer(port);
				updateNotification("Server đang chạy");						// action cho button START Server
			}
		});
		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setBackground(new Color(0, 128, 128));
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStart.setBounds(421, 10, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.stop();
				updateNotification("Server đã dừng");						// action cho button STOP Server
			}
		});
		btnStop.setForeground(new Color(255, 255, 255));
		btnStop.setBackground(new Color(0, 128, 128));
		btnStop.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStop.setBounds(532, 10, 89, 23);
		contentPane.add(btnStop);
		
		notification = new JTextArea();
		notification.setFont(new Font("Monospaced", Font.PLAIN, 14));
		notification.setEditable(false);
		notification.setBounds(63, 161, 591, 110);							// khung hien thi thong bao
		notification.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(notification, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setSize(636, 263);
		scrollPane.setLocation(10, 87);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}
}
