package com.nike.microphone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

import com.nike.event.EventListener;

public class MicrophoneSender implements Runnable, EventListener {

	private static final String serverIp = "192.168.0.147";
	private static final int SERVER_PORT = 3005;
	private DatagramSocket socket;

	private BlockingQueue<byte[]> queue;
	private volatile boolean isStopped = true;

	public MicrophoneSender(final BlockingQueue<byte[]> queue, DatagramSocket socket) {
		this.queue = queue;
		this.socket = socket;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (!isStopped) {
					if (!queue.isEmpty()) {
						byte[] data = queue.poll();
						if (data != null) {
							socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(serverIp), SERVER_PORT));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void connect() {
		isStopped = false;
	}

	@Override
	public void disconnect() {
		isStopped = true;
	}
}
