package japcheckers;

import japcheckers.events.JapEvent;
import japcheckers.events.JapListener;
import japcheckers.game.GameHandler;

/**
 *
 * @author Александр
 */
public class StartGameListener implements JapListener {

	private GameHandler gHandler;

	public StartGameListener (GameHandler gHandler) {
		this.gHandler = gHandler;
	}

	@Override
	public void Signal(JapEvent event) {
		if ("start_game".equals(event.getMessage())) {
			gHandler.createFrame();
		}
		System.out.println(event);
	}
}
