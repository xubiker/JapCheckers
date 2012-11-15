/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package japcheckers;

/**
 *
 * @author Александр
 */
public class JapCheckers {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		GameHandler gHandler = new GameHandler(2);
		gHandler.createFrame();
	}

	public static int Bool2Int (boolean val) {
		return val ? 1 : 0;
	}
}
