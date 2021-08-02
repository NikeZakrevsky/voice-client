package com.nike.microphone;

import java.util.concurrent.BlockingQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.nike.event.EventListener;

public class MicrophoneReader implements Runnable, EventListener {

	private static final float SAMPLE_RATE = 16000.0f;
	private static final int SAMPLE_SIZE = 16;
	private static final int CHANNELS = 1;

	private TargetDataLine microphone;
	private volatile boolean isStopped = true;
	private BlockingQueue<byte[]> queue;

	public MicrophoneReader(BlockingQueue<byte[]> queue) {
		this.queue = queue;

		AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		try {
			this.microphone = (TargetDataLine) AudioSystem.getLine(info);
			this.microphone.open(audioFormat);
			this.microphone.start();
		} catch (LineUnavailableException e) {
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

	@Override
	public void run() {
		int i = microphone.getBufferSize() / 5;
		byte[] data = new byte[i];

		while (true) {
			if (!isStopped) {
				System.out.println(queue.size());
				microphone.read(data, 0, data.length);
				try {
					queue.put(data);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
