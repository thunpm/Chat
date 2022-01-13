package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField passwordField;
	public static User user = new User(); // user dang nhap
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 393);
		contentPane = new JPanel();												// khung nhin chinh
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel welcomeTitle = new JLabel("CH\u00C0O M\u1EEANG");
		welcomeTitle.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeTitle.setFont(new Font("Tahoma", Font.BOLD, 15));				// title CHAO MUNG ...
		welcomeTitle.setForeground(new Color(0, 128, 128));
		welcomeTitle.setBounds(10, 29, 438, 35);
		contentPane.add(welcomeTitle);
		
		JLabel welcome = new JLabel("Vui l\u00F2ng \u0111\u0103ng nh\u1EADp \u0111\u1EC3 ti\u1EBFp t\u1EE5c");
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setForeground(new Color(0, 128, 128));
		welcome.setFont(new Font("Tahoma", Font.BOLD, 15));						// title Moi dang nhap ...
		welcome.setBounds(10, 75, 438, 35);
		contentPane.add(welcome);
		
		JLabel label1 = new JLabel("T\u00EAn \u0111\u0103ng nh\u1EADp");
		label1.setFont(new Font("Tahoma", Font.PLAIN, 12));						// lable Ten dang nhap
		label1.setBounds(25, 159, 92, 30);
		contentPane.add(label1);
		
		usernameField = new JTextField();
		usernameField.setBounds(127, 159, 277, 30);								// khung nhap Ten dang nhap
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		JLabel label2 = new JLabel("M\u1EADt kh\u1EA9u");
		label2.setFont(new Font("Tahoma", Font.PLAIN, 12));						// label Mat khau
		label2.setBounds(25, 200, 92, 30);
		contentPane.add(label2);
		
		passwordField = new JTextField();
		passwordField.setColumns(10);											// khung nhap Mat khau
		passwordField.setBounds(127, 200, 277, 30);
		contentPane.add(passwordField);
		
		JButton loginButton = new JButton("\u0110\u0103ng nh\u1EADp");
		loginButton.setBackground(new Color(0, 128, 128));
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 12));					// button Dang nhap
		loginButton.setForeground(Color.WHITE);
		loginButton.setBounds(170, 282, 110, 23);
		contentPane.add(loginButton);
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));					// label hien thi loi
		errorLabel.setForeground(new Color(178, 34, 34));
		errorLabel.setBounds(65, 125, 339, 23);
		contentPane.add(errorLabel);
		
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				
				StringBuilder sb = new StringBuilder();
				
				if ("".equals(username)) {
					sb.append("Tên đăng nhập trống! ");							// xu li button Dang nhap
				}
				
				if ("".equals(password)) {
					sb.append("Mật khẩu trống! ");
				}
				
				if (sb.length() > 0) {
					errorLabel.setText(sb.toString());
					return;
				}
				
				UserDAO userDAO = new UserDAO();
				
				User check = userDAO.getUserByUsername(username, password);
				
				if (check != null) {
					JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
					
					user = check;
					new ClientGUI().setVisible(true);
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không khớp!");
				}
			}
		});

	}
}
