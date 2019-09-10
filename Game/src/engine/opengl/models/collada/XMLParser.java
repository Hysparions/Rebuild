package engine.opengl.models.collada;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLParser {

	private static final Pattern DATA = Pattern.compile(">(.+?)<");
	private static final Pattern NODE = Pattern.compile("<(.+?)>");
	private static final Pattern ATTR_NAME = Pattern.compile("(.+?)=");
	private static final Pattern ATTR_VAL = Pattern.compile("\"(.+?)\"");
	private static final Pattern CLOSED = Pattern.compile("(</|/>)");
	
	public static XMLNode loadXML(String fileName) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(XMLParser.class.getResourceAsStream(fileName)));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Can't find the XML file: " + fileName);
			System.exit(0);
			return null;
		}
		try {
			reader.readLine();
			XMLNode node = loadNode(reader);
			reader.close();
			return node;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error with XML file format for: " + fileName);
			System.exit(0);
			return null;
		}
	}
	
	private static XMLNode loadNode(BufferedReader reader) throws Exception {
		String line = reader.readLine().trim();
		if (line.startsWith("</")) {
			return null;
		}
		String[] startTagParts = getStartTag(line).split(" ");
		XMLNode node = new XMLNode(startTagParts[0].replace("/", ""));
		addAttributes(startTagParts, node);
		addData(line, node);
		if (CLOSED.matcher(line).find()) {
			return node;
		}
		XMLNode child = null;
		while ((child = loadNode(reader)) != null) {
			node.addChild(child);
		}
		return node;
	}

	private static void addData(String line, XMLNode node) {
		Matcher matcher = DATA.matcher(line);
		if (matcher.find()) {
			node.addData(matcher.group(1));
		}
	}

	private static void addAttributes(String[] titleParts, XMLNode node) {
		for (int i = 1; i < titleParts.length; i++) {
			if (titleParts[i].contains("=")) {
				addAttribute(titleParts[i], node);
			}
		}
	}

	private static void addAttribute(String attributeLine, XMLNode node) {
		Matcher nameMatch = ATTR_NAME.matcher(attributeLine);
		nameMatch.find();
		Matcher valMatch = ATTR_VAL.matcher(attributeLine);
		valMatch.find();
		node.addAttribute(nameMatch.group(1), valMatch.group(1));
	}

	private static String getStartTag(String line) {
		Matcher match = NODE.matcher(line);
		match.find();
		return match.group(1);
	}
	
	/**
	 * Finds the first child of the specified node that is associated to the child name given as a string
	 * @param node to look for children
	 * @param childName of the child searched
	 * @return the first child of the specified node that is associated to the child name given as a string
	 */
	public static XMLNode findFirstChild(XMLNode node, String childName){
		HashMap<String, LinkedList<XMLNode>> childrenMap = node.childrenMap();
		if(childrenMap != null) {
			if(childrenMap.containsKey(childName)) {
				return childrenMap.get(childName).get(0);
			}else {
				for(LinkedList<XMLNode> children : childrenMap.values()) {
					for(XMLNode childNode : children) {
						return findFirstChild(childNode, childName);
					}
			    }
			}
		}
		return null;
	}

	public static XMLNode findChild(XMLNode node, String childName, String attribute, String value) {
		HashMap<String, LinkedList<XMLNode>> childrenMap = node.childrenMap();
		if(childrenMap != null) {
			if(childrenMap.containsKey(childName)) {
				for(XMLNode child : childrenMap.get(childName)) {
					if(child.attribute(attribute) != null) {
						if(child.attribute(attribute).equals(value)) {
							return child;
						}
					}
			    }
			}else {
				for(LinkedList<XMLNode> children : childrenMap.values()) {
					for(XMLNode childNode : children) {
						XMLNode returnNode = findChild(childNode, childName, attribute, value);
						if(returnNode != null) {
							return returnNode;
						}
					}
			    }
			}
		}
		return null;
	}
	

}
