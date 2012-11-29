package japcheckers.events;

import japcheckers.accounts.User;
import java.util.ArrayList;
import java.util.EventObject;

/**
 *
 * @author Александр
 */
@SuppressWarnings("serial")
public class JCGameEvent extends EventObject {

	private String message;
	private ArrayList<User> users;

	public JCGameEvent(Object source, String message, ArrayList<User> users) {
		super(source);
		this.message = message;
		this.users = users;
	}

	public JCGameEvent(Object source) {
		this(source, "", null);
	}

	public JCGameEvent(String message) {
		this(null, message, null);
	}

	public JCGameEvent(ArrayList<User> users) {
		this(null, "", users);
	}

	public JCGameEvent() {
		this(null, "", null);
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return message;
	}
}