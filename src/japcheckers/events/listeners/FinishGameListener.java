package japcheckers.events.listeners;

import japcheckers.accounts.AccountsManager;
import japcheckers.events.JCGameEvent;
import japcheckers.events.JCListener;

/**
 *
 * @author Александр
 */
public class FinishGameListener implements JCListener {

	private AccountsManager accManager;

	public FinishGameListener (AccountsManager accManager) {
		this.accManager = accManager;
	}

	@Override
	public void Signal(JCGameEvent event) {
		if ("finish_game".equals(event.getMessage())) {
			accManager.finishGame(event.getUsers());
		}
		System.out.println(event);
	}
}
