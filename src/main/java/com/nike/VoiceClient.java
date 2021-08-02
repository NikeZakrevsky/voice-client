package com.nike;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.*;

import com.nike.event.ConnectEventListener;
import com.nike.event.DisconnectEventListener;
import com.nike.microphone.MicrophoneReader;
import com.nike.microphone.MicrophoneSender;

public class VoiceClient {

	private Thread microphoneReaderThread;
	private Thread microphoneSenderThread;
	private Thread playVoiceThread;

	private DisconnectEventListener disconnectEventListener;
	private ConnectEventListener connectEventListener;

	public VoiceClient() {
		BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(1024);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(3001);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		MicrophoneReader microphoneReader = new MicrophoneReader(queue);
		MicrophoneSender microphoneSender = new MicrophoneSender(queue, socket);
		PlayVoice playVoice = new PlayVoice(socket);

		this.microphoneReaderThread = new Thread(microphoneReader);
		this.microphoneSenderThread = new Thread(microphoneSender);
		this.playVoiceThread = new Thread();

		disconnectEventListener = new DisconnectEventListener();
		disconnectEventListener.addListener(microphoneReader);
		disconnectEventListener.addListener(microphoneSender);
		disconnectEventListener.addListener(playVoice);

		connectEventListener = new ConnectEventListener();
		connectEventListener.addListener(microphoneReader);
		connectEventListener.addListener(microphoneSender);
		connectEventListener.addListener(playVoice);
	}

	public void start() {
		SwingUtilities.invokeLater(new com.nike.gui.MainWindow(connectEventListener, disconnectEventListener));

		microphoneReaderThread.start();
		microphoneSenderThread.start();
		playVoiceThread.start();
	}
}
