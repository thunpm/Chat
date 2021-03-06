package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class TCPClient extends Thread {
	private Socket socket;
	private BufferedReader ipstr;
	private DataOutputStream opstr;

	public TCPClient(String serverAddress, int serverPort, String username) throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByName(serverAddress), serverPort);

		opstr = new DataOutputStream(socket.getOutputStream());
		ipstr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		send("username" + username);
	}

	public void send(String message) {
		try {
			if (message != null && ! "".equals(message)) {
				opstr.writeBytes(message);
				opstr.write(13);
				opstr.write(10);
				opstr.flush();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Không thể kết nối đến Server!");
			e.printStackTrace();
		}
	}
	
	public void chatWithFriend(String friend) throws IOException {
		send("friend" + friend);
	}

	@Override
	public void run() {
		try {
			while (true) {
				String message = ipstr.readLine();
				
				if (message != null && ! "".equals(message.trim())) {
					ClientGUI.updateMessage(message, false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		close();
		System.exit(0);
	}
	
	private void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
