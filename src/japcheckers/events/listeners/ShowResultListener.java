package japcheckers.events.listeners;

import japcheckers.events.JCGameEvent;
import japcheckers.events.JCListener;
import japcheckers.game.GameHandler;

/**
 *
 * @author Александр
 */
public class ShowResultListener implements JCListener {

	private GameHandler gHandler;

	public ShowResultListener (GameHandler gHandler) {
		this.gHandler = gHandler;
	}

	@Override
	public void Signal(JCGameEvent event) {
		if ("show_result".equals(event.getMessage())) {
			gHandler.showDialog(event.getContent());
		}
		System.out.println(event);
	}
}
