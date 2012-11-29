package japcheckers.events.listeners;

import japcheckers.JCException;
import japcheckers.events.JCGameEvent;
import japcheckers.events.JCListener;
import japcheckers.game.GameHandler;

/**
 *
 * @author Александр
 */
public class StartGameListener implements JCListener {

	private GameHandler gHandler;

	public StartGameListener (GameHandler gHandler) {
		this.gHandler = gHandler;
	}

	@Override
	public void Signal(JCGameEvent event) {
		if ("start_game".equals(event.getMessage())) {
			try {
				gHandler.StartGame(event.getUsers());
			} catch (JCException ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println(event);
	}
}
