import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneReader extends Thread {

	private AudioFormat format;
	private int CHUNK_SIZE = 8500;
	private TargetDataLine microphone;
	private static DatagramSocket socket = null;
	private static String IP;
	private static boolean stopSocket = true;
	private DataLine.Info info;
	private static String quitMessage = "quit";

	DatagramPacket p;
	private static int serverPort = 3005;

	public MicrophoneReader(DatagramSocket sock, String ip, String Nick)
			throws IOException {
		stopSocket = true;
		socket = sock;
		IP = ip;
		info = new DataLine.Info(TargetDataLine.class, format);
		format = new AudioFormat(16000.0f, 16, 2, true, false);
		socket.send(new DatagramPacket(Nick.getBytes(), Nick.length(), InetAddress
				.getByName(IP), serverPort));
	}

	public static String getIP() {
		return IP;

	}

	public static void sendQuitMessage() throws UnknownHostException,
			IOException {
		socket.send(new DatagramPacket(quitMessage.getBytes(), quitMessage.getBytes().length,
				InetAddress.getByName(IP), serverPort));
	}

	public static void stopSock() {
		stopSocket = false;
	}

	public void run() {
		try {
			microphone = AudioSystem.getTargetDataLine(format);

			microphone = (TargetDataLine) AudioSystem.getLine(info);

			microphone.open(format);

			byte[] data = new byte[CHUNK_SIZE];
			microphone.start();

			while (stopSocket) {

				microphone.read(data, 0, CHUNK_SIZE);
				if (!MainWindow.isMute()) {
					try {
						socket.send(new DatagramPacket(data, CHUNK_SIZE, InetAddress
								.getByName(IP), serverPort));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
		} finally {
			microphone.close();
		}
	}
}
