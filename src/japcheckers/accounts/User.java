package japcheckers.accounts;

/**
 *
 * @author Александр
 */
public class User {
	private int uid;
	private String nick;
	private int wins_cnt;


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

}
