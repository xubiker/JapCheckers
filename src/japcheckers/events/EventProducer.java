package japcheckers.events;

import java.util.ArrayList;

/**
 *
 * @author Александр
 */
public class EventProducer {

	private ArrayList<JapListener> listeners = new ArrayList<>();

	public void addListener(JapListener listener) {
		listeners.add(listener);
	}

	public void removeListener(JapListener listener) {
		listeners.remove(listener);
	}

	protected void fire(String message) {
		JapEvent ev = new JapEvent(this, message);
		for (JapListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	public void doWork(String workName) {
		fire(workName);
	}
}