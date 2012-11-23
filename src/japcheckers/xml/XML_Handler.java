package japcheckers.xml;

import japcheckers.accounts.User;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Александр
 */
public class XML_Handler {

	private static enum LastEvent {
		READ,
		WRITE,
		UNKNOWN;
	}

	private String xmlPath;
	private File xmlFile;
	private Document doc;
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Transformer transformer;
	private Element rootElement;
	private NodeList users;
	private boolean modified;
	private LastEvent lastevent;

	private static int currentID = 0;

	private static void nextID() {
		currentID++;
	}

	//**********************************************************************************************
	public XML_Handler (String path) {
		modified = false;
		lastevent = LastEvent.UNKNOWN;
		xmlPath = path;

		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			xmlFile = new File(xmlPath);
			if (xmlFile.exists()) {
				System.out.println("xml already exists!");
				doc = docBuilder.parse(xmlFile);
				rootElement = doc.getDocumentElement();
				rootElement.normalize();
			} else {
				doc = docBuilder.newDocument();
				rootElement = doc.createElement("accounts");
				doc.appendChild(rootElement);
				modified = true;
			}
			currentID = getLastID() + 1;
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	//**********************************************************************************************
	private Element addUser (String _nick, String _password, int _winsCnt) {
		Element user = doc.createElement("user");
		rootElement.appendChild(user);

		// set attribute to user element
		user.setAttribute("id", Integer.toString(currentID));

		// create nick element
		Element nickName = doc.createElement("nick");
		nickName.appendChild(doc.createTextNode(_nick));
		user.appendChild(nickName);

		// create password element
		Element pswd = doc.createElement("pswd");
		pswd.appendChild(doc.createTextNode(_password));
		user.appendChild(pswd);

		// create winCnt element
		Element winsCnt = doc.createElement("wins");
		winsCnt.appendChild(doc.createTextNode(Integer.toString(_winsCnt)));
		user.appendChild(winsCnt);

		lastevent = LastEvent.WRITE;
		modified = true;
		nextID();
		return user;
	}

	//**********************************************************************************************
	private Element searchUser (String _nick) {
		// refresh the account list if needed
		if (lastevent == LastEvent.UNKNOWN || lastevent == LastEvent.WRITE) {
			users = doc.getElementsByTagName("user");
		}
		// check if the account list is empty
//		if ((users.getLength() == 0) || (users.getLength() == 1) && (users.item(0).getNodeValue() == null)) {
		if ((users.getLength() == 0)) {
			return null;
		}
		for (int i = 0; i < users.getLength(); i++) {
			Node nNode = users.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element userElem = (Element) nNode;
				if (_nick.equals(getTagValue("nick", userElem))) {
					return userElem;
				}
			}
		}
		return null;
	}

	//**********************************************************************************************
	private int getLastID () {
		// check if the account list is empty
		if (lastevent == LastEvent.UNKNOWN || lastevent == LastEvent.WRITE) {
			users = doc.getElementsByTagName("user");
		}
		if ((users == null) || (users.getLength() == 1) && (users.item(0).getNodeValue() == null)) {
			return 0;
		}
		int maxID = 0;
		for (int i = 0; i < users.getLength(); i++) {
			Node nNode = users.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				int tmpID = Integer.parseInt(getAttributeValue("id", (Element) nNode));
				if (tmpID > maxID) {
					maxID = tmpID;
				}
			}
		}
		return maxID;
	}

	//**********************************************************************************************
	private User fillUser (Element usr) {
		// Create new User and fill all its properties
		if (usr == null || usr.getNodeType() != Node.ELEMENT_NODE)
			return null;
		int uid = Integer.parseInt(getAttributeValue("id", usr));
		String nick = getTagValue("nick", usr);
		int wins_cnt = Integer.parseInt(getTagValue("wins", usr));
		return new User(uid, nick, wins_cnt);
	}

	//**********************************************************************************************
	public User loginAttempt (String _nick, String _pswd) {
		Element user = searchUser(_nick);
		if (user == null) {
			return null;
		}
		if (user.getNodeType() == Node.ELEMENT_NODE) {
			if (!_pswd.equals(getTagValue("pswd", user))) {
				return null;
			}
		}
		User usr_res = fillUser(user);
		return usr_res;
	}

	//**********************************************************************************************
	public User registerAttempt (String _nick, String _pswd) {
		Element user = searchUser(_nick);
		if (user != null) {
			return null;
		}
		user = addUser(_nick, _pswd, 0);
		User usr_res = fillUser(user);
		return usr_res;
	}

	//**********************************************************************************************
	private static String getTagValue (String sTag, Element elem) {
		NodeList lst = elem.getElementsByTagName(sTag).item(0).getChildNodes();
		return lst.item(0).getNodeValue();
	}

	//**********************************************************************************************
	private static String getAttributeValue (String sTag, Element elem) {
		NamedNodeMap attr = elem.getAttributes();
		Node nodeAttr = attr.getNamedItem(sTag);
		return nodeAttr.getNodeValue();
	}

	//**********************************************************************************************
	public void Finish () {
		if (modified == true) {
			saveToFile();
		}
	}

	//**********************************************************************************************
	private void saveToFile() {
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
			System.out.println("XML Successfully saved!");
		} catch (TransformerException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
