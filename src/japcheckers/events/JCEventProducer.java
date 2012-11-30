package japcheckers.events;

import japcheckers.accounts.JCUser;
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

	protected void fire(String message, ArrayList<JCUser> users) {
		JCGameEvent ev = new JCGameEvent(this, message, users, "");
		for (JCListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	protected void fire(String Message) {
		JCGameEvent ev = new JCGameEvent(this, Message, null, "");
		for (JCListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	protected void fire(String Message, String content) {
		JCGameEvent ev = new JCGameEvent(this, Message, null, content);
		for (JCListener listener : listeners) {
			listener.Signal(ev);
		}
	}

	public void doWork(String workName, ArrayList<JCUser> users) {
		fire(workName, users);
	}

	public void doWork(String workName, String content) {
		fire(workName, content);
	}
}