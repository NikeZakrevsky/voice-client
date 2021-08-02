package com.nike;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySound extends Thread {

	private String fileName;

	public PlaySound(String file) {
		this.fileName = file;
	}

	public void run() {
		try {

			URL url2 = this.getClass().getResource(fileName);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url2);
			Clip clip;
			clip = AudioSystem.getClip();

			clip.open(ais);

			clip.setFramePosition(0);
			clip.start();
			Thread.sleep(clip.getMicrosecondLength() / 1000);
			clip.stop();
			clip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}