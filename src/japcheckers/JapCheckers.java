package japcheckers;

import japcheckers.accounts.AccountsManager;
import japcheckers.game.GameHandler;

/**
 *
 * @author Александр
 */
public class JapCheckers {

	private static GameHandler gHandler;
	private static StartGameListener startGameListener;

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

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		gHandler = new GameHandler(2);
		startGameListener = new StartGameListener(gHandler);

		applyVisualStyle();
		AccountsManager accManager = new AccountsManager();
		accManager.addListener(startGameListener);
		accManager.createFrame();
	}

	public static int Bool2Int(boolean val) {
		return val ? 1 : 0;
	}
}
