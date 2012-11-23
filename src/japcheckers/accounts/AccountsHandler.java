package japcheckers.accounts;

import japcheckers.EventProducer;
import japcheckers.JapListener;
import japcheckers.xml.XML_Handler;
import java.util.ArrayList;

/**
 *
 * @author Александр
 */
public class AccountsHandler {
	private AccountsFrame frame;
	private XML_Handler xmlHandler;
	private ArrayList<User> currentUsers;

	private EventProducer eventProducer;

	public void createFrame () {
		frame = new AccountsFrame();
		frame.setHandler(this);
		frame.setVisible(true);
	}

	public AccountsHandler () {
		eventProducer = new EventProducer();
		currentUsers = new ArrayList<>();
		xmlHandler = new XML_Handler("accounts.xml");
	}

	public void addListener (JapListener lst) {
		eventProducer.addListener(lst);
	}

	public void startGame () {
		eventProducer.doWork("start_game");
	}

	//**********************************************************************************************
	public void loginAttempt (String login, String pswd) {
		frame.displayMessage("login attempt");
		User usr = xmlHandler.loginAttempt(login, pswd);
		if (usr == null) {
			frame.displayMessage("Invalid");
		} else {
			if (currentUsers.indexOf(usr) != -1) {
				frame.displayMessage("User already loged in");
			} else {
				currentUsers.add(usr);
				frame.displayMessage("Ok");
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
			frame.displayMessage("User already exists");
		} else {
			frame.displayMessage("user successfully registered");
		}
	}

	//**********************************************************************************************
	public void Finish () {
		xmlHandler.Finish();
	}
}