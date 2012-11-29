package japcheckers.events;

import japcheckers.accounts.User;
import java.util.ArrayList;

/**
 *
 * @author Александр
 */
public class JCEventProducer {

	private ArrayList<JCListener> listeners = new ArrayList<>();

	public void addListener(JCListener listener) {
		listeners.add(listener);
	}

	public void removeListener(JCListener listener) {
		listeners.remove(listener);
	}

	protected void fire(String message, ArrayList<User> users) {
		JCGameEvent ev = new JCGameEvent(this, message, users);
		for (JCListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	protected void fire(String Message) {
		JCGameEvent ev = new JCGameEvent(this, Message, null);
		for (JCListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	public void doWork(String workName, ArrayList<User> users) {
		fire(workName, users);
	}
}