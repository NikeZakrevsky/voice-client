package com.nike;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.nike.event.EventListener;

public class PlayVoice implements Runnable, EventListener {
	private static final int CHUNK_SIZE = 3200;
	private DatagramSocket socket;
	private volatile boolean isStopped = true;

	public PlayVoice(DatagramSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		byte[] data = new byte[CHUNK_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);

		try (SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);) {
			speakers.open(format);
			speakers.start();
			while (true) {
				if (!isStopped) {
					socket.receive(receivePacket);
					speakers.write(data, 0, data.length);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void disconnect() {
		isStopped = true;
	}

	@Override
	public void connect() {
		isStopped = false;
	}
}