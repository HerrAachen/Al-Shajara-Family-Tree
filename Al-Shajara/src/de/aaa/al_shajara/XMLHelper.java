package de.aaa.al_shajara;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.aaa.al_shajara.data.PartiallyDefinedDate;

public class XMLHelper {

	public static Document parse(File f) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return dBuilder.parse(f);
	}
	
	/** Writes an xml document to a file 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws IOException */
	public static void xml2File(Document doc, File f) throws TransformerFactoryConfigurationError, TransformerException, IOException{
		if (f.isDirectory()){
			throw new FileNotFoundException("Error exporting xml document. Specified file is a directory:" + f);
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		transformerFactory.setAttribute("indent-number", 2);
		Transformer transformer = transformerFactory.newTransformer();
        DOMSource        source = new DOMSource( doc );
        FileOutputStream os     = new FileOutputStream( f );
        StreamResult     result = new StreamResult( os );
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform( source, result );
        os.close();
	}
	
	/** Gets the first sub-node of the specified node by name. Descends recursively */ 
	public static Node getNodeByName(Node node, String nodeName){
		if (node==null)
			return null;
		if (node.getNodeName().equals(nodeName)){
			return node;
		}
		NodeList childNodes = node.getChildNodes();
		for(int i=0;i<childNodes.getLength();i++){
			Node childNode = childNodes.item(i);
			Node res = getNodeByName(childNode, nodeName);
			if (res!=null){
				return res;
			}
		}
		return null;
	}
	
	/** Gets all direct subnodes of this node that have the specified name */
	public static List<Node> getSubNodesByName(Node node, String nodeName){
	  List<Node> result = new LinkedList<Node>();
	  if (node==null){
	    return null;
	  }
	  NodeList childs = node.getChildNodes();
	  for(int i=0;i<childs.getLength();i++){
	    Node child = childs.item(i);
	    if (child.getNodeName().equals(nodeName)){
	      result.add(child);
	    }
	  }
	  return result;
	}
	
	/**
	 * Gets the text of a certain sub node. Returns null if the sub node does not exist.
	 * @param node
	 * @param nodeName
	 * @return
	 */
	public static String getChildNodeText(Node node, String nodeName){
		Node child = getNodeByName(node, nodeName);
		if (child==null)
			return null;
		return child.getTextContent();
	}
	
	public static Date getChildNodeDateValue(Node node, String nodeName, DateFormat dateFormat){
		String childNodeValue = getChildNodeText(node, nodeName);
		if (childNodeValue==null)
			return null;
		try {
			Date date = dateFormat.parse(childNodeValue);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static PartiallyDefinedDate getChildNodePartialDate(Node node, String nodeName){
		String childNodeValue = getChildNodeText(node, nodeName);
		if (childNodeValue==null)
			return null;
		try {
			return PartiallyDefinedDate.parse(childNodeValue);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Gets the first child node of the specified node which has the specified name and the returns the value
	 * of the attribute with the specified name. Descends recursively.
	 * @param node
	 * @param childNodeName
	 * @param attributeName
	 * @return
	 */
	public static String getChildNodeAttributeValue(Node node,String childNodeName, String attributeName) {
		Node childNode = getNodeByName(node, childNodeName);
		if (childNode==null){
			return null;
		}
		NamedNodeMap attributes = childNode.getAttributes();
		if (attributes == null){
			return null;
		}
		Node namedItem = attributes.getNamedItem(attributeName);
		if (namedItem!=null)
			return namedItem.getNodeValue();
		return null;
	}
	
	public static void addAttribute(Document doc, Node node, String attributeName, String attributeValue){
	  Attr attribute = doc.createAttribute(attributeName);
	  attribute.setValue(attributeValue);
	  node.getAttributes().setNamedItem(attribute);
	}
}
