package japcheckers.events;

import japcheckers.accounts.JCUser;
import java.util.ArrayList;
import java.util.EventObject;

/**
 *
 * @author Александр
 */
@SuppressWarnings("serial")
public class JCGameEvent extends EventObject {

	private String message;
	private String content;
	private ArrayList<JCUser> users;

	public JCGameEvent(Object source, String message, ArrayList<JCUser> users, String content) {
		super(source);
		this.message = message;
		this.users = users;
		this.content = content;
	}

	public JCGameEvent(Object source) {
		this(source, "", null, "");
	}

	public JCGameEvent(String message) {
		this(null, message, null, "");
	}

	public JCGameEvent(ArrayList<JCUser> users) {
		this(null, "", users, "");
	}

	public JCGameEvent() {
		this(null, "", null, "");
	}

	public String getMessage() {
		return message;
	}

	public String getContent() {
		return content;
	}

	public ArrayList<JCUser> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return message;
	}
}