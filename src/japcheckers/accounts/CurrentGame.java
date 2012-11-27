package japcheckers.accounts;

/**
 *
 * @author Александр
 */
public interface CurrentGame {
	public int getTurn ();
	public void setTurn (int val);
	public int getCapturedEnemiesCnt ();
	public void addCapturedEnemies (int val);
	public void addCapturedEnemies ();

}
