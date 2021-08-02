/*package com.nike;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class InputMessage extends Thread {

	private static DatagramSocket messageSocket;
	private static DatagramPacket packet;
	private static byte[] message = new byte[8500];
	private static boolean flag = true;

	public InputMessage() throws SocketException {
	
		if (messageSocket == null) {
			messageSocket = new DatagramSocket(5001);
		}
		packet = new DatagramPacket(message, message.length);
	}

	public static void SendMessage(String mess) throws IOException {
		messageSocket.send(new DatagramPacket(mess.getBytes(), mess.getBytes().length,
				InetAddress.getByName(""), 5002));
	}

	public static void stopSock() {
		flag = false;
	}

	public void run() {
		try {
			while (flag) {
					if (!messageSocket.isClosed()) {
						messageSocket.receive(packet);
						MainWindow.setText(new String(packet.getData()));
					}

			}
		} catch(Exception e){}
		
	}
}
*/