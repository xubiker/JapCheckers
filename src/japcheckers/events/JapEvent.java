package japcheckers.events;

import java.util.EventObject;

/**
 *
 * @author Александр
 */
@SuppressWarnings("serial")
public class JapEvent extends EventObject {

	private String message;

	public JapEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public JapEvent(Object source) {
		this(source, "");
	}

	public JapEvent(String message) {
		this(null, message);
	}

	public JapEvent() {
		this(null, "");
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}