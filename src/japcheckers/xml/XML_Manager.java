package japcheckers.xml;

import japcheckers.Pair;
import japcheckers.accounts.JCUser;
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
public class XML_Manager {

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

	//**********************************************************************************************
	private static int generateID() {
		int res = currentID;
		currentID++;
		return res;
	}

	//**********************************************************************************************
	public XML_Manager (String path) {
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
	private Element addUserToXML (JCUser usr) {
		Element usrElem = doc.createElement("user");
		rootElement.appendChild(usrElem);

		// set attribute to user element
		usrElem.setAttribute("id", Integer.toString(usr.getID()));

		for (Pair<String, String> p : usr.getAllProperties()) {
			Element elem = doc.createElement(p.x);
			elem.appendChild(doc.createTextNode(p.y));
			usrElem.appendChild(elem);
		}

		lastevent = LastEvent.WRITE;
		modified = true;
//		nextID();
		return usrElem;
	}

	//**********************************************************************************************
	private Element addUserToXML (String _nick, String _password) {
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

		lastevent = LastEvent.WRITE;
		modified = true;
//		generateID();
		return user;
	}

	//**********************************************************************************************
	private Element searchUser (String _nick) {
		// refresh the account list if needed
		if (lastevent == LastEvent.UNKNOWN || lastevent == LastEvent.WRITE) {
			users = doc.getElementsByTagName("user");
		}
		// check if the account list is empty
		if (users.getLength() == 0) {
			return null;
		}
		for (int i = 0; i < users.getLength(); i++) {
			Node nNode = users.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element usrElem = (Element) nNode;
				if (_nick.equals(getTagValue("nick", usrElem))) {
					return usrElem;
				}
			}
		}
		return null;
	}

	//**********************************************************************************************
	private int getLastID () {
		// refresh the account list if needed
		if (lastevent == LastEvent.UNKNOWN || lastevent == LastEvent.WRITE) {
			users = doc.getElementsByTagName("user");
		}
		// check if the account list is empty
		if (users.getLength() == 0) {
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
	private JCUser getUserFromXML (Element usrElem) {
		// Create new User and fill all its properties
		if (usrElem == null || usrElem.getNodeType() != Node.ELEMENT_NODE)
			return null;
		int uid = Integer.parseInt(getAttributeValue("id", usrElem));
		String nick = getTagValue("nick", usrElem);
		String pswd = getTagValue("pswd", usrElem);
		JCUser resUser = new JCUser(uid, nick, pswd);
		int wins = Integer.parseInt(getTagValue("wins", usrElem));
		int losses = Integer.parseInt(getTagValue("losses", usrElem));
		int score = Integer.parseInt(getTagValue("score", usrElem));
		int skill = Integer.parseInt(getTagValue("skill", usrElem));
		resUser.fillInfo(wins, losses, score, skill);
		return resUser;
	}

	//**********************************************************************************************
	public void updateUser (JCUser usr) {
		Element usrElem = searchUser(usr.getNick());
		if (usrElem == null) {
			return;
		}
		for (Pair<String, String> p : usr.getSafeProperties()) {
			modifyTagValue(p.x, usrElem, p.y);
		}
		lastevent = LastEvent.WRITE;
		modified = true;
	}

	//**********************************************************************************************
	public JCUser loginAttempt (String _nick, String _pswd) {
		Element usrElem = searchUser(_nick);
		if (usrElem == null) {
			return null;
		}
		if (usrElem.getNodeType() == Node.ELEMENT_NODE) {
			if (!_pswd.equals(getTagValue("pswd", usrElem))) {
				return null;
			}
		}
		return getUserFromXML(usrElem);
	}

	//**********************************************************************************************
	public JCUser registerAttempt (String _nick, String _pswd) {
		Element usrElem = searchUser(_nick);
		if (usrElem != null) {
			return null;
		}
		JCUser newUsr = new JCUser(generateID(), _nick, _pswd);
		addUserToXML(newUsr);
		return newUsr;
	}

	//**********************************************************************************************
	private static String getTagValue (String sTag, Element elem) {
		NodeList lst = elem.getElementsByTagName(sTag).item(0).getChildNodes();
		return lst.item(0).getNodeValue();
	}

	//**********************************************************************************************
	private static void modifyTagValue (String sTag, Element elem, String newVal) {
		NodeList lst = elem.getElementsByTagName(sTag).item(0).getChildNodes();
		lst.item(0).setTextContent(newVal);
	}

	//**********************************************************************************************
	private static String getAttributeValue (String sTag, Element elem) {
		NamedNodeMap attr = elem.getAttributes();
		Node nodeAttr = attr.getNamedItem(sTag);
		return nodeAttr.getNodeValue();
	}

	//**********************************************************************************************
	private static void modifyAttributeValue (String sTag, Element elem, String newVal) {
		NamedNodeMap attr = elem.getAttributes();
		Node nodeAttr = attr.getNamedItem(sTag);
		nodeAttr.setTextContent(newVal);
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
