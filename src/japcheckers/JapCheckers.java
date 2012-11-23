/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package japcheckers;

import japcheckers.game.GameHandler;
import japcheckers.accounts.AccountsHandler;

/**
 *
 * @author Александр
 */
public class JapCheckers {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		final GameHandler gHandler = new GameHandler(2);

		JapListener action = new JapListener() {
			@Override
			public void Signal(JapEvent myEvent) {
				if ("start_game".equals(myEvent.getMessage()))
					gHandler.createFrame();
					System.out.println(myEvent);
			}
		};

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			System.out.println("Error while setting style " + ex.getMessage());
		}


		AccountsHandler accHandler = new AccountsHandler();
		accHandler.addListener(action);
		accHandler.createFrame();
//		gHandler = new GameHandler(2);
//		gHandler.createFrame();
	}

	public static int Bool2Int (boolean val) {
		return val ? 1 : 0;
	}
}
