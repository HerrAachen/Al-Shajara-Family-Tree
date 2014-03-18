package de.aaa.al_shajara;

import static de.aaa.al_shajara.XMLConstants.BIOGRAPHY;
import static de.aaa.al_shajara.XMLConstants.BIOGRAPHICEVENTS;
import static de.aaa.al_shajara.XMLConstants.BIRTHDAY;
import static de.aaa.al_shajara.XMLConstants.CITY_OF_BIRTH;
import static de.aaa.al_shajara.XMLConstants.COUNRTY_OF_BIRTH;
import static de.aaa.al_shajara.XMLConstants.DAY_OF_DEATH;
import static de.aaa.al_shajara.XMLConstants.FATHER;
import static de.aaa.al_shajara.XMLConstants.FIRST_NAME;
import static de.aaa.al_shajara.XMLConstants.GENDER;
import static de.aaa.al_shajara.XMLConstants.ID;
import static de.aaa.al_shajara.XMLConstants.LAST_NAME;
import static de.aaa.al_shajara.XMLConstants.MAIDEN_NAME;
import static de.aaa.al_shajara.XMLConstants.MIDDLE_NAME;
import static de.aaa.al_shajara.XMLConstants.MOTHER;
import static de.aaa.al_shajara.XMLConstants.PERSON;
import static de.aaa.al_shajara.XMLConstants.PSEUDONYM;
import static de.aaa.al_shajara.XMLConstants.WRITING_SYSTEM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.aaa.al_shajara.data.BiographicEvent;
import de.aaa.al_shajara.data.FamilyTree;
import de.aaa.al_shajara.data.Gender;
import de.aaa.al_shajara.data.PartiallyDefinedDate;
import de.aaa.al_shajara.data.Person;

public class Serialization {

	public static final SimpleDateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static FamilyTree loadFamilyTree(File dir){
		FamilyTree tree = new FamilyTree();
		Map<Person,String> motherMap = new HashMap<Person, String>();
		Map<Person,String> fatherMap = new HashMap<Person, String>();
		for(File inputFile: getInputFiles(dir)){
			if (inputFile.getName().endsWith("xml")){
				try {
					Document doc = XMLHelper.parse(inputFile);
					//				Node personNode = XMLHelper.getNodeByName(doc, PERSON);
					Node personNode = doc.getDocumentElement();
					Person person = new Person();
					String id = XMLHelper.getChildNodeText(personNode, ID);
					person.setId(id);
					person.setOriginFile(inputFile);
					List<Node> alphabetNodes = XMLHelper.getSubNodesByName(personNode, WRITING_SYSTEM);
					for(Node alphabetNode: alphabetNodes){
						String alphabetName = alphabetNode.getAttributes().getNamedItem(ID).getNodeValue();
						person.setFirstName(XMLHelper.getChildNodeText(alphabetNode, FIRST_NAME),alphabetName);
						person.setMiddleName(XMLHelper.getChildNodeText(alphabetNode, MIDDLE_NAME),alphabetName);
						person.setLastName(XMLHelper.getChildNodeText(alphabetNode, LAST_NAME),alphabetName);
						person.setMaidenName(XMLHelper.getChildNodeText(alphabetNode, MAIDEN_NAME),alphabetName);
						person.setPseudonym(XMLHelper.getChildNodeText(alphabetNode, PSEUDONYM),alphabetName);
						person.setCityOfBirth(XMLHelper.getChildNodeText(alphabetNode, CITY_OF_BIRTH),alphabetName);
						person.setCountryOfBirth(XMLHelper.getChildNodeText(alphabetNode, COUNRTY_OF_BIRTH),alphabetName);
												person.setBiography(XMLHelper.getChildNodeText(alphabetNode, BIOGRAPHY),alphabetName);
						Node biographyNode = XMLHelper.getNodeByName(alphabetNode, BIOGRAPHY);
						if (biographyNode!=null){
							String freetext = XMLHelper.getChildNodeText(biographyNode, BIOGRAPHY);
							if (freetext!=null){
								person.setBiography(freetext.trim(), alphabetName);
							}
						}
					}
					Node eventsNode = XMLHelper.getNodeByName(personNode, BIOGRAPHICEVENTS);
					if (eventsNode!=null){
						List<Node> eventNodes = XMLHelper.getSubNodesByName(eventsNode, "event");
						for(Node eventNode: eventNodes){
							PartiallyDefinedDate startDate = XMLHelper.getChildNodePartialDate(eventNode, "startDate");
							PartiallyDefinedDate endDate = XMLHelper.getChildNodePartialDate(eventNode, "endDate");
							String eventText = XMLHelper.getChildNodeText(eventNode, "eventText");
							if (eventText!=null){
								eventText = eventText.trim();
							}
							BiographicEvent bioEvent = new BiographicEvent(startDate, endDate,eventText);
							person.addBiographicEvent(bioEvent);
						}
					}

					person.setGender(Gender.string2Gender(XMLHelper.getChildNodeText(personNode, GENDER)));
					//				person.setFirstNameSecondaryWritingSystem(XMLHelper.getChildNodeText(personNode, FIRST_NAME_SECONDARY_WRITING_SYSTEM));
					//				person.setMiddleNameSecondaryWritingSystem(XMLHelper.getChildNodeText(personNode, MIDDLE_NAME_SECONDARY_WRITING_SYSTEM));
					//				person.setLastNameSecondaryWritingSystem(XMLHelper.getChildNodeText(personNode, LAST_NAME_SECONDARY_WRITING_SYSTEM));
					//					person.setBirthday(XMLHelper.getChildNodeDateValue(personNode, BIRTHDAY, xmlDateFormat));
					person.setBirthday(XMLHelper.getChildNodePartialDate(personNode, BIRTHDAY));
					person.setDayOfDeath(XMLHelper.getChildNodePartialDate(personNode, DAY_OF_DEATH));
					motherMap.put(person, XMLHelper.getChildNodeAttributeValue(personNode,MOTHER,ID));
					fatherMap.put(person, XMLHelper.getChildNodeAttributeValue(personNode,FATHER,ID));

					tree.addMember(person);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (inputFile.getName().equals("writingsystems.txt")){
				try {
					BufferedReader reader = new BufferedReader(new FileReader(inputFile));
					String line = reader.readLine();
					while(line!=null){
						tree.addAlphabet(line);
						line = reader.readLine();
					}

					reader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//		for(Map.Entry<Person, String> entry: motherMap.entrySet()){
		//			System.out.println(entry.getKey().getId() + ":" + entry.getValue());
		//		}
		for(Person p: tree.getMembers()){
			p.setMother(tree.getMemberById(motherMap.get(p)));
			p.setFather(tree.getMemberById(fatherMap.get(p)));
		}
		tree.addChildLinks();
		return tree;
	}

	private static List<File> getInputFiles(File dir) {
		//		System.out.println("Serialization:" + dir.getAbsolutePath());
		File[] files = dir.listFiles();
		return Arrays.asList(files);
	}



	public static Document person2Xml(Person p) throws ParserConfigurationException{
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Node personNode = doc.createElement(PERSON);
		doc.appendChild(personNode);
		createAndAddNode(doc,personNode,ID,p.getId());
		for(String alphabet: p.getAlphabets()){
			Node alphabetNode = createAndAddNode(doc, personNode, WRITING_SYSTEM);
			XMLHelper.addAttribute(doc, alphabetNode, ID, alphabet);
			createAndAddNode(doc,alphabetNode,FIRST_NAME,p.getFirstName(alphabet));
			createAndAddNode(doc,alphabetNode,MIDDLE_NAME,p.getMiddleName(alphabet));
			createAndAddNode(doc,alphabetNode,MAIDEN_NAME,p.getMaidenName(alphabet));
			createAndAddNode(doc,alphabetNode,LAST_NAME,p.getLastName(alphabet));
			createAndAddNode(doc,alphabetNode,PSEUDONYM,p.getPseudonym(alphabet));
			createAndAddNode(doc,alphabetNode,CITY_OF_BIRTH,p.getCityOfBirth(alphabet));
			createAndAddNode(doc,alphabetNode,COUNRTY_OF_BIRTH,p.getCountryOfBirth(alphabet));
			createAndAddNode(doc,alphabetNode,BIOGRAPHY,p.getBiography(alphabet));
		}
		List<BiographicEvent> biographicEvents = p.getBiographicEvents();
		if ((biographicEvents!=null && !biographicEvents.isEmpty())){
			Node biographyNode = createAndAddNode(doc, personNode, BIOGRAPHICEVENTS);
			for(BiographicEvent event: biographicEvents){
				Node eventNode = createAndAddNode(doc, biographyNode, "event");
				if (event.getStartDate()!=null)
					createAndAddNode(doc, eventNode, "startDate", event.getStartDate().xmlString());
				if (event.getEndDate()!=null)
					createAndAddNode(doc, eventNode, "endDate", event.getEndDate().xmlString());
				if (event.getEventText()!=null)
					createAndAddNode(doc, eventNode, "eventText", event.getEventText());
			}
		}
		if (p.getGender()!=null)
			createAndAddNode(doc,personNode,GENDER,p.getGender().xmlString());
		if (p.getBirthday()!=null)
			//			createAndAddNode(doc,personNode,BIRTHDAY,Serialization.xmlDateFormat.format(p.getBirthday()));
			createAndAddNode(doc,personNode,BIRTHDAY,p.getBirthday().xmlString());
		if (p.getDayOfDeath()!=null)
			//			createAndAddNode(doc,personNode,DAY_OF_DEATH,Serialization.xmlDateFormat.format(p.getDayOfDeath()));
			createAndAddNode(doc,personNode,DAY_OF_DEATH,p.getDayOfDeath().xmlString());
		if (p.getMother()!=null) {
			Node motherNode = createAndAddNode(doc,personNode,MOTHER);
			XMLHelper.addAttribute(doc, motherNode, ID, p.getMother().getId());
		}
		if (p.getFather()!=null){
			Node fatherNode = createAndAddNode(doc,personNode,FATHER);
			XMLHelper.addAttribute(doc, fatherNode, ID, p.getFather().getId());
		}
		return doc;
	}

	private static Node createAndAddNode(Document doc, Node parentNode, String childNodeName){
		Node node = doc.createElement(childNodeName);
		parentNode.appendChild(node);
		return node;
	}

	/**
	 * Creates a node and returns it
	 * @param doc
	 * @param parentNode
	 * @param childNodeName
	 * @param childNodeValue
	 * @return
	 */
	private static Node createAndAddNode(Document doc, Node parentNode, String childNodeName, String childNodeValue){
		Node node = doc.createElement(childNodeName);
		if (childNodeValue!=null && !childNodeValue.trim().isEmpty()){
			node.setTextContent(childNodeValue);
			parentNode.appendChild(node);
		}
		return node;
	}
}
