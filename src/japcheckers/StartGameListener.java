package japcheckers;

import japcheckers.events.JCListener;
import japcheckers.events.JCStartGameEvent;
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
	public void Signal(JCStartGameEvent event) {
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
