package com.nike.event;

import java.util.ArrayList;
import java.util.List;

public class ConnectEventListener {
	private List<EventListener> listeners = new ArrayList<>();

	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public void connect() {
		for (EventListener listener : listeners) {
			listener.connect();
		}
	}
}
