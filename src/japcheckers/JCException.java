package japcheckers;

/**
 *
 * @author Александр
 */
@SuppressWarnings("serial")
public class JCException extends Exception{
	public JCException () {
		super("general JCException");
	}

	public JCException (String msg) {
		super(msg);
	}
}
