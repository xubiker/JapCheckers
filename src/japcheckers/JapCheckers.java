package japcheckers;

import japcheckers.accounts.AccountsManager;
import japcheckers.events.listeners.FinishGameListener;
import japcheckers.events.listeners.StartGameListener;
import japcheckers.game.GameHandler;

/**
 *
 * @author Александр
 */
public class JapCheckers {

	private static GameHandler gHandler;
	private static AccountsManager accManager;
	private static StartGameListener startGameListener;
	private static FinishGameListener finishGameListener;

	//**********************************************************************************************
	private static void applyVisualStyle() {
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
	}

	//**********************************************************************************************
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		gHandler = new GameHandler();
		accManager = new AccountsManager();
		startGameListener = new StartGameListener(gHandler);
		finishGameListener = new FinishGameListener(accManager);

		applyVisualStyle();
		accManager.addListener(startGameListener);
		gHandler.addListener(finishGameListener);
		accManager.createFrame();
	}

	//**********************************************************************************************
	public static int Bool2Int(boolean val) {
		return val ? 1 : 0;
	}
}
