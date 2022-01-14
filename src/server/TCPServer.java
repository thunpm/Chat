package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.Client;

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
		if ("Chat Group".equals(request.client.getFriend())) {  // chat Group thi gui het ban be trong ket noi
			for (int i = 0; i < listRequest.size(); i++) {
				if (listRequest.get(i) != request) {
					try {
						listRequest.get(i).send(request.client.getUsername() + ": " + message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			ServerGUI.updateNotification("Da gui message \"" + message + "\" tu " + request.client.getUsername() + " den moi nguoi");
		} else {
			for (int i = 0; i < listRequest.size(); i++) {
				if (listRequest.get(i).client.getUsername().equals(request.client.getFriend())) {
					try {
						listRequest.get(i).send(request.client.getUsername() + ": " + message);
						ServerGUI.updateNotification("Da gui message \"" + message + "\" tu " + request.client.getUsername() + " den " + request.client.getFriend());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
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
                while (true) {
                    String str = ipstr.readLine();
                    
                    if (str.length() > 8 && "username".equals(str.substring(0, 8))) { // message dau tien gui username
                    	client.setUsername(str.substring(8, str.length()));
                    	ServerGUI.updateNotification("Da ket noi den " + client.getUsername() + " tai " + skc.getRemoteSocketAddress().toString());
                    } else {
                    	if (str.length() > 6 && "friend".equals(str.substring(0, 6))) { // message gui nguoi ban muon chat cung
                    		client.setFriend(str.substring(6, str.length()));
                    		ServerGUI.updateNotification("Da ket noi " + client.getUsername() + " voi " + client.getFriend());
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
            ServerGUI.updateNotification(client.getUsername() + " da ngat ket noi");
            
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
