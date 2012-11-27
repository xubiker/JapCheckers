package japcheckers.accounts;

/**
 *
 * @author Александр
 */

public class User implements CurrentGame {
	private int uid;
	private String nick;
	private int wins_cnt;
	// variebles for current game
	private int CG_turn;
	private int CG_capturedEnemiesCnt = 0;

	public User(int uid, String nick, int wins_cnt) {
		this.uid = uid;
		this.nick = nick;
		this.wins_cnt = wins_cnt;
	}

	public String getNick () {
		return nick;
	}

	@Override
	public boolean equals (Object obj) {
		User inputUsr = (User)obj;
		if (this.nick.equals(inputUsr.getNick())) {
			return true;
		}
		return false;
	}

	@Override
	public int getTurn() {
		return CG_turn;
	}

	@Override
	public void setTurn(int val) {
		CG_turn = val;
	}

	@Override
	public int getCapturedEnemiesCnt() {
		return CG_capturedEnemiesCnt;
	}

	@Override
	public void addCapturedEnemies(int val) {
		CG_capturedEnemiesCnt += val;
	}

	@Override
	public void addCapturedEnemies() {
		CG_capturedEnemiesCnt++;
	}

}
