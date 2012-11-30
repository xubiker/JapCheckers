package japcheckers.accounts;

import japcheckers.events.JCEventProducer;
import japcheckers.events.JCListener;
import japcheckers.xml.XML_Manager;
import java.util.ArrayList;
import javax.swing.JDialog;

/**
 *
 * @author Александр
 */
public class AccountsManager {

	private JCAccountsFrame frame;
	private XML_Manager xmlHandler;
	private ArrayList<JCUser> currentUsers;
	private JCEventProducer eventProducer;

	public void createFrame() {
		frame = new JCAccountsFrame();
		frame.setHandler(this);
		frame.setVisible(true);
	}

	public AccountsManager() {
		eventProducer = new JCEventProducer();
		currentUsers = new ArrayList<>();
		xmlHandler = new XML_Manager("accounts.xml");
	}

	public void addListener(JCListener lst) {
		eventProducer.addListener(lst);
	}

	//**********************************************************************************************
	public void startGame() {
		eventProducer.doWork("start_game", currentUsers);
	}

	//**********************************************************************************************
	public void finishGame(ArrayList<JCUser> gamers) {
		System.out.println("ACCOUNT MANAGER - FINISH GAME");
		//--------------------------- update statistics and write it to XML
		int max_score = 1;
		ArrayList<JCUser> winners = new ArrayList<>();
		for (JCUser usr : gamers) {
			int t = usr.getCapturedEnemiesCnt();
			if (t > max_score) {
				max_score = t;
				winners.clear();
				winners.add(usr);
			} else if (t == max_score) {
				winners.add(usr);
			}
			usr.incTotalScore(t);
		}
		for (JCUser usr : gamers) {
			if (winners.indexOf(usr) != -1) {
				usr.incWinsCnt();
			} else {
				usr.incLossesCnt();
			}
			xmlHandler.updateUser(usr);
		}
		finishXML();
		//-----------------------
		String resStr = "Winners:\r\n";
		if (winners.isEmpty()) {
			resStr += "There are no winners. =(";
		} else {
			for (JCUser usr : winners) {
				resStr += usr.getNick() + "\r\n";
			}
		}
		eventProducer.doWork("show_result", resStr);
	}

	//**********************************************************************************************
	public void loginAttempt(int user, String login, String pswd) {
		JCUser usr = xmlHandler.loginAttempt(login, pswd);
		if (usr == null) {
			frame.displayMessage("Invalid login or password");
		} else {
			if (currentUsers.indexOf(usr) != -1) {
				frame.displayMessage("User already logged in");
			} else {
				if (user == 1) {
					frame.enableUser1Field(false);
				} else if (user == 2) {
					frame.enableUser2Field(false);
				}
				currentUsers.add(usr);
				frame.displayMessage("User" + user + " - successfull login");
				if (currentUsers.size() == 2) {
					frame.AllowGame();
				}
			}
		}
	}

	//**********************************************************************************************
	public void registerAttempt(String login, String pswd) {
		JCUser usr = xmlHandler.registerAttempt(login, pswd);
		if (usr == null) {
			frame.displayMessage("User with simular name already exists");
		} else {
			frame.displayMessage("New user successfully registered");
		}
	}

	//**********************************************************************************************
	public void finishXML() {
		xmlHandler.Finish();
	}
}