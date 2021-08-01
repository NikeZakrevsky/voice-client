import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class PlayVoice extends Thread {
	final int CHUNK_SIZE = 8500;
	private AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, false);
	private SourceDataLine speakers;
	private static DatagramSocket sock = null;
	private byte[] dataSource = new byte[CHUNK_SIZE];
	private byte[] dataWithIP = new byte[CHUNK_SIZE + 50];
	private DatagramPacket receivePacket;
	private char[] ip = new char[50];
	private static boolean stopSocket = true;
	private static String gsonListOfClients;
	private static List<Client> clientList= new ArrayList<Client>();
	private static List<String> stringClientList = new ArrayList<String>();;

	public PlayVoice(DatagramSocket s) {
		sock = s;
		stopSocket = true;

	}

	static class Client {
		public String IP;
		public String NickName;

		public Client(String IP, String NickName) {
			this.IP = IP;
			this.NickName = NickName;
		}

	}

	public static void stopFlag() {

		stopSocket = false;

	}

	public String getIPFromPacket(byte[] data) {

		int y = 0;
		String IP = "";
		for (int i = 0; i < ip.length; i++) {
			ip[i] = ' ';
		}
		for (int i = CHUNK_SIZE + 2; i < data.length; i++) {
			ip[y] = (char) data[i];
			y++;
		}
		String a = new String(ip);
		a.replaceAll("[^x0-9.]", "");

		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != ' ')
				IP += a.charAt(i);
		}
		return IP;

	}
	
	
	boolean ips(InetAddress[] ips, String ip) {
		boolean b = false;
		for(int i = 0; i < ips.length; i++) {
			if((ips[i].toString()).equals(ip)) {
				b = true;
			}
		}
		System.out.println(b);
		return b;
}
	
	public void run() {
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
				format);
		try {
			if (speakers != null) {
				speakers.close();
			}
			speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			speakers.open(format);
			speakers.start();
			while (stopSocket) {

				receivePacket = new DatagramPacket(dataWithIP,
						dataWithIP.length);
				sock.receive(receivePacket);
				if ((new String(dataWithIP)).charAt(1) == '{'
						|| (new String(dataWithIP)).charAt(1) == '[') {
					gsonListOfClients = new String(dataWithIP);
					
					Gson gson = new Gson();
					Type collectionType = new TypeToken<List<Client>>() {
					}.getType();
					JsonReader reader = new JsonReader(new StringReader(gsonListOfClients));
					reader.setLenient(true);
					stringClientList.clear();
					clientList = gson.fromJson(reader,
							collectionType);
					for (Client client : clientList) {
						stringClientList.add(client.NickName);
					}
					System.out.println("Client1: " + gsonListOfClients);
					MainWindow.setTextNickName(stringClientList);

				} else {
					for (int i = 0; i < CHUNK_SIZE; i++) {
						dataSource[i] = dataWithIP[i];
					}
					InetAddress inet = InetAddress.getLocalHost();
					InetAddress[] ips = InetAddress.getAllByName(inet
							.getCanonicalHostName());
					System.out.println(getIPFromPacket(dataWithIP).replaceAll("[^A-Za-z<>:1234567890.]","").substring(0, 12)+ips[2].getHostAddress().toString());
					if (getIPFromPacket(dataWithIP).replaceAll("[^A-Za-z<>:1234567890.]","").substring(0, 12).equals(
							ips[2].getHostAddress().toString().replaceAll("[^1234567890.]",""))) {
						if (!MainWindow.isMuteSound()) {
							speakers.write(dataSource, 0, dataSource.length);
						}
					}
				}
			}
		} catch (Exception e) {
		} finally {
			speakers.close();
		}
	}
}
