package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Client;
import dao.UserDAO;

public class TCPServer {
	private static ArrayList<Request> listRequest; // list cac luong xu li ket noi den Client
	private ServerSocket ss;
	private static boolean isStop = false;
	
	public TCPServer(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listRequest = new ArrayList<>();
		
		(new Thread() {
			public void run() {
				while (isStop == false) {  // chung nao Server chua dung thi tiep tuc lang nghe yeu cau tu Client
					try {
						Socket s = ss.accept();
						
						Request request = new Request(s);
						listRequest.add(request);
						
						ServerGUI.updateUserOnline(listRequest.size()); // co ket noi thi cap nhat so Client online o giao dien Server
						
						request.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void stop() {
		isStop = true;
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToClient(Request request, String message) {  // nhan message tu Client va gui den cho Client khac
//		UserDAO userDAO = new UserDAO();
		if ("chat group".equals(request.client.getFriend())) {  // chat Group thi gui het
			for (int i = 0; i < listRequest.size(); i++) {
				if (listRequest.get(i) != request) { // && userDAO.isFriend(request.client.getUsername(), listRequest.get(i).client.getUsername()) == 1) {
					try {
						listRequest.get(i).send(request.client.getUsername() + ": " + message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			ServerGUI.updateNotification("Đã gửi message \"" + message + "\" từ " + request.client.getUsername() + " đến mọi người");
		} else {
			for (int i = 0; i < listRequest.size(); i++) {
				if (listRequest.get(i).client.getUsername().equals(request.client.getFriend())) {
					try {
						listRequest.get(i).send(request.client.getUsername() + ": " + message);
						ServerGUI.updateNotification("Đã gửi message \"" + message + "\" từ " + request.client.getUsername() + " đến " + request.client.getFriend());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	
	public boolean checkOnline(String username) {
    	if ("chat group".equals(username)) {
    		if (listRequest.size() > 0) {
    			return true;
    		}
    	} else {
    		for (int i = 0; i < listRequest.size(); i++) {
    			if (listRequest.get(i).client.getUsername().equals(username)) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
	
	class Request extends Thread { // luong xu li yeu cau cua Client
		Socket skc;
		Client client; // cap nhat lai nguoi ban muon chat cung
		BufferedReader ipstr;
		DataOutputStream opstr;

        public Request(Socket s) throws IOException {
            skc = s;
            client = new Client();
            opstr = new DataOutputStream(skc.getOutputStream());
            ipstr = new BufferedReader(new InputStreamReader(skc.getInputStream()));
        }
        
        private void send(String message) throws IOException {
        	opstr.writeBytes(message);
			opstr.write(13);
			opstr.write(10);
			opstr.flush();
		}
        
        public void run() {
            try { 
                while (isStop ==  false) {
                    String str = ipstr.readLine();
                    
                    if (str.length() > 8 && "username".equals(str.substring(0, 8))) { // message dau tien gui username
                    	client.setUsername(str.substring(8, str.length()));
                    	ServerGUI.updateNotification("Đã kết nối đến " + client.getUsername() + " tại " + skc.getRemoteSocketAddress().toString());
                    } else {
                    	if (str.length() > 6 && "friend".equals(str.substring(0, 6)) && checkOnline(str.substring(6, str.length())) == false) {
                    		client.setFriend(str.substring(6, str.length()));
                    		ServerGUI.updateNotification("Không thể kết nối " + client.getUsername() + " với " + client.getFriend() + " vì " + client.getFriend() + " không hoạt động");
                    	} else if (str.length() > 6 && "friend".equals(str.substring(0, 6)) && checkOnline(str.substring(6, str.length()))) { // message gui nguoi ban muon chat cung
                    		client.setFriend(str.substring(6, str.length()));
                    		ServerGUI.updateNotification("Đã kết nối " + client.getUsername() + " với " + client.getFriend());
                    	} else {
                    		sendMessageToClient(this, str); // message chua tin nhan muon gui
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Client thoat ra thi tien hanh ngat ket noi va thong bao cho Server  
            listRequest.remove(this);
            ServerGUI.updateNotification(client.getUsername() + " đã ngắt kết nối");
            
            try {
				skc.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				ServerGUI.updateUserOnline(listRequest.size());
			}
        }
    }
	
}
