package japcheckers.accounts;

import japcheckers.events.EventProducer;
import japcheckers.events.JapListener;
import japcheckers.xml.XML_Manager;
import java.util.ArrayList;

/**
 *
 * @author Александр
 */
public class AccountsManager {
	private AccountsFrame frame;
	private XML_Manager xmlHandler;
	private ArrayList<User> currentUsers;

	private EventProducer eventProducer;

	public void createFrame () {
		frame = new AccountsFrame();
		frame.setHandler(this);
		frame.setVisible(true);
	}

	public AccountsManager () {
		eventProducer = new EventProducer();
		currentUsers = new ArrayList<>();
		xmlHandler = new XML_Manager("accounts.xml");
	}

	public void addListener (JapListener lst) {
		eventProducer.addListener(lst);
	}

	public void startGame () {
		eventProducer.doWork("start_game");
	}

	//**********************************************************************************************
	public void loginAttempt (String login, String pswd) {
		User usr = xmlHandler.loginAttempt(login, pswd);
		if (usr == null) {
			frame.displayMessage("Invalid login or password");
		} else {
			if (currentUsers.indexOf(usr) != -1) {
				frame.displayMessage("User already logged in");
			} else {
				currentUsers.add(usr);
				frame.displayMessage("User " + currentUsers.size() + " - successfull login");
				if (currentUsers.size() == 2) {
					frame.AllowGame();
				}
			}
		}
	}

	//**********************************************************************************************
	public void registerAttempt (String login, String pswd) {
		User usr = xmlHandler.registerAttempt(login, pswd);
		if (usr == null) {
			frame.displayMessage("User with simular name already exists");
		} else {
			frame.displayMessage("New user successfully registered");
		}
	}

	//**********************************************************************************************
	public void finishXML () {
		xmlHandler.Finish();
	}
}