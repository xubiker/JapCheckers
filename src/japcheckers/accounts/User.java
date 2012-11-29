package japcheckers.accounts;

import japcheckers.Pair;
import java.util.ArrayList;

/**
 *
 * @author Александр
 */

public class User implements CurrentGame {

	private int uid;
	private String nick;
	private String pswd;

	private int winsCnt;
	private int lossesCnt;
	private int totalScore;
	private int skill;
	// variebles for current game
	private int CG_turn;
	private int CG_capturedEnemiesCnt = 0;

	public User(int uid, String nick, String pswd) {
		this.uid = uid;
		this.nick = nick;
		this.pswd = pswd;
		winsCnt = 0;
		lossesCnt = 0;
		totalScore = 0;
		skill = 0;
	}

	public void fillInfo (int winsCnt, int lossesCnt, int totalScore, int skill) {
		this.winsCnt = winsCnt;
		this.lossesCnt = lossesCnt;
		this.totalScore = totalScore;
		this.skill = skill;
	}

	public int getID () {
		return uid;
	}

	public String getNick () {
		return nick;
	}

	public int getWinsCnt () {
		return winsCnt;
	}

	public void setWinsCnt (int val) {
		winsCnt = val;
	}

	public int getLossesCnt () {
		return lossesCnt;
	}

	public int getTotalScore () {
		return totalScore;
	}

	public int getSkill () {
		return skill;
	}

	public ArrayList<Pair<String, String>> getAllProperties () {
		ArrayList<Pair<String, String>> properties = new ArrayList<>();
		properties.add(new Pair<>("nick", nick));
		properties.add(new Pair<>("pswd", pswd));
		properties.add(new Pair<>("wins", Integer.toString(winsCnt)));
		properties.add(new Pair<>("losses", Integer.toString(lossesCnt)));
		properties.add(new Pair<>("score", Integer.toString(totalScore)));
		properties.add(new Pair<>("skill", Integer.toString(skill)));
		return properties;
	}

	public ArrayList<Pair<String, String>> getSafeProperties () {
		ArrayList<Pair<String, String>> properties = new ArrayList<>();
		properties.add(new Pair<>("wins", Integer.toString(winsCnt)));
		properties.add(new Pair<>("losses", Integer.toString(lossesCnt)));
		properties.add(new Pair<>("score", Integer.toString(totalScore)));
		properties.add(new Pair<>("skill", Integer.toString(skill)));
		return properties;
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
