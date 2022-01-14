package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import dao.UserDAO;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame {
	private JPanel contentPane;
	private JTextField txtAddFriend;
	public static JTextPane txtMessage;	// khung hien thi tin nhan
	private JLabel nameFriend;
	private JTextField txtInput;
	private JButton btnSend;
	private ArrayList<JLabel> list;
	private TCPClient client;
	private UserDAO userDAO = new UserDAO();
	private JTextField listFriend;
	
	// thêm html vào message
	public static void appendToPane(JTextPane tp, String msg){
		tp.setContentType( "text/html" );
		
	    HTMLDocument doc = (HTMLDocument) tp.getDocument();
	    HTMLEditorKit editorKit = (HTMLEditorKit) tp.getEditorKit();
	    try {	
	      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
	      tp.setCaretPosition(doc.getLength());
	      
	    } catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
	public static void updateMessage(String message, boolean send) {
		if (send == false) {
			String name = "";
			int i = 0;
			for (; i < message.length(); i++) {
				if (':' == message.charAt(i)) {
					break;
				}
				name = name + message.charAt(i);
			}
			message = message.substring(i + 1, message.length());
			appendToPane(txtMessage, "<b style='font-size: 12px;'>" + name + "</b>");
			appendToPane(txtMessage, "<div style='width: 170px; background-color: #00b3b3; word-wrap: break-word; padding: 3px; color: white; font-size: 13px; margin-bottom: 7px;'>   "+ message +"</div>");
		} else {
			appendToPane(txtMessage, "<div style='margin-left: 210px; width: 170px; word-wrap: break-word; background-color: #ffffb3; padding: 3px; margin-bottom: 7px; color: black; font-size: 13px;'>   "+ message +"</div>");	
		}
	}
	
	public void setNameFriend(String name) {
		nameFriend.setText(name);
	}
	
	public ClientGUI() {
		setResizable(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent we) {
					
				try {
					client = new TCPClient("localhost", 7, Login.user.getUsername());	
					
					client.start();												// ket noi den Server khi hien thi giao dien
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Không thể kết nối đến Server!");
					setVisible(false);
					
					e.printStackTrace();
				}
			}
			
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 695, 425);
		contentPane = new JPanel();												// khung nhin chinh
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel nameClient = new JLabel("CH\u00C0O M\u1EEANG " + Login.user.getName());
		nameClient.setHorizontalAlignment(SwingConstants.CENTER);
		nameClient.setForeground(new Color(0, 128, 128));						// label CHAO MUNG ...
		nameClient.setFont(new Font("Tahoma", Font.BOLD, 14));
		nameClient.setBounds(10, 15, 207, 23);
		contentPane.add(nameClient);
		
		txtAddFriend = new JTextField();
		txtAddFriend.setBounds(237, 12, 176, 20);								// khung nhap Them lien he
		contentPane.add(txtAddFriend);
		txtAddFriend.setColumns(10);
		
		JButton btnAddFriend = new JButton("K\u1EBET N\u1ED0I");
		btnAddFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtAddFriend.getText();
				
				if (name != null && ! "".equals(name)) {
					int check = userDAO.addFriend(Login.user.getId(), name);	// xu li button Them lien he
					
					if (check == 0) {
						Login.user.setListFriend(userDAO.getFriendUser(Login.user.getId()));
						
						// hien thi nguoi lien he moi vao danh sach nguoi lien he
						if (Login.user.getListFriend().size() > 0) {
							list.get(Login.user.getListFriend().size()).setText(name);
						} else {
							list.get(0).setText(name); 
						}
						
						JOptionPane.showMessageDialog(null, "Thêm kết nối thành công!");
					} else if (check == 1) {
						JOptionPane.showMessageDialog(null, "Không tìm thấy người dùng trong mạng!");
					} else {
						JOptionPane.showMessageDialog(null, "Người liên hệ đã có trong danh sách rồi!");
					}
				}
			}
		});
		btnAddFriend.setBackground(new Color(0, 128, 128));
		btnAddFriend.setForeground(new Color(255, 255, 255));
		btnAddFriend.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAddFriend.setBounds(423, 11, 114, 23);
		contentPane.add(btnAddFriend);
		
		//////////////////////////////////////////////////////////////////////////
		listFriend = new JTextField();
		listFriend.setBackground(Color.WHITE);
		listFriend.setEditable(false);
		listFriend.setBounds(10, 84, 127, 291);
		contentPane.add(listFriend);
		listFriend.setColumns(10);
		
		list = new ArrayList<>();
		
		JLabel lbl = new JLabel("chat group");
		lbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbl.setBounds(10, 11, 107, 21);
		listFriend.add(lbl);
		
		list.add(lbl);
		
		Login.user.setListFriend(userDAO.getFriendUser(Login.user.getId()));
		
		for (int i = 0; i < Login.user.getListFriend().size() + 10; i++) {
			JLabel lblNewLabel;
			if (i < Login.user.getListFriend().size()) {
				lblNewLabel = new JLabel(Login.user.getListFriend().get(i).getUsername());
			} else {																// xu li hien thi danh sach cac lien he
				lblNewLabel = new JLabel();
			}
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel.setBounds(10, 11 + 21*(i + 1), 107, 21);
			listFriend.add(lblNewLabel);
			
			list.add(lblNewLabel);
		}
		
		for (int i = 0; i < list.size(); i++) {
			JLabel lblNewLabel = list.get(i);
			lblNewLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					try {
						client.chatWithFriend(lblNewLabel.getText());				// xu li khi nhap vao chon nguoi chat cung
						setNameFriend(lblNewLabel.getText());
						
						txtMessage.setText(null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		////////////////////////////////////////////////////////////////////////////
		
		nameFriend = new JLabel("Ng\u01B0\u1EDDi nh\u1EADn");
		nameFriend.setForeground(new Color(0, 128, 128));
		nameFriend.setFont(new Font("Tahoma", Font.BOLD, 12));						// label hien thi nguoi nhan
		nameFriend.setHorizontalAlignment(SwingConstants.CENTER);
		nameFriend.setBounds(147, 59, 114, 14);
		contentPane.add(nameFriend);
		
		txtMessage = new JTextPane();
		txtMessage.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtMessage.setEditable(false);
		txtMessage.setBounds(147, 85, 522, 259);									// khung hien thi tin nhan
//		txtMessage.setLineWrap(true);
		JScrollPane scrollMessage = new JScrollPane(txtMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMessage.setSize(522, 260);
		scrollMessage.setLocation(147, 84);
		contentPane.add(scrollMessage, BorderLayout.CENTER);
		
		txtInput = new JTextField();
		txtInput.setBounds(147, 355, 424, 20);										// khung nhap tin nhan
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		
		btnSend = new JButton("G\u1EECI");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String message = txtInput.getText();
				txtInput.setText(null);
				
				if ("Người nhận".equals(nameFriend.getText())) {
					JOptionPane.showMessageDialog(null, "Vui lòng chọn người gửi!");
				}
				
				if (message != null && ! "".equals(message.trim())) {
					client.send(message.trim());										// xu li button gui tin nhan
					
					updateMessage(message, true);
				}
			}
		});
		btnSend.setForeground(new Color(255, 255, 255));
		btnSend.setBackground(new Color(0, 128, 128));
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSend.setBounds(580, 355, 89, 23);
		contentPane.add(btnSend);
		
	}
	
}
