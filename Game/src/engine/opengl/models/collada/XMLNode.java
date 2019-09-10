package engine.opengl.models.collada;

import java.util.HashMap;
import java.util.LinkedList;

public class XMLNode {

	private String name;
	private String data;
	private HashMap<String, LinkedList<XMLNode>> children;
	private HashMap<String, String> attributes;
	
	public XMLNode(String name) {
		this.name = name;
		//System.out.println(name);
		this.data = new String("");
		this.children = null;
		this.attributes = null;
	}
	
	/** Add data to the node */
	public void addData(String data) {
		this.data += data;
	}
	
	/**
	 * Adds a children node
	 * Creates the map and the associated node list if needed
	 * @param node to add
	 * @return true if the operation succeed
	 */
	public boolean addChild(XMLNode node) {
		if(children == null) {
			children = new HashMap<>();
		}
		if(!children.containsKey(node.name())) {
			children.put(node.name(), new LinkedList<>());
		}
		if(node.name() != null && node.name() != "" && node != null) {
			LinkedList<XMLNode> list = children.get(node.name());
			list.push(node);
			return true;
		}
		return false;
	}
	
	/**
	 * Try to add an attribute to the node
	 * @param name of the attribute
	 * @param data of the attribute
	 * @return true if the operation succeed
	 */
	public boolean addAttribute(String name, String data) {
		if(attributes == null){
			attributes = new HashMap<>();
		}
		if(attributes.containsKey(name)) {
			return false;
		}
		attributes.put(name, data);
		return true;
	}
	
	public void displayNode() {
		System.out.print(this.name + " | Data : " +  data + " | Attributes : ");
		if(attributes != null)
		attributes.forEach((k,v) -> {
			System.out.print(k+"="+v+" | ");
		});
		System.out.println();
		if(children != null) {
			for(LinkedList<XMLNode> childList : children.values()) {
				for(XMLNode childNode : childList) {
					childNode.displayNode();
				}
			}
		}
	}
	
	
	/**@return the name of the node*/
	public String name(){
		return this.name;
	}
	
	/** @return data as String */
	public String data() {return this.data;}
	
	/** 
	 * @param name of the attribute
	 * @return the attribute data as String
	 */
	public String attribute(String name) {
		if(this.attributes != null) {
			return this.attributes.get(name);
		}
		return null;
	}
	
	/** 
	 * @return the attribute map
	 */
	public HashMap<String,String> attributes() {
		if(this.attributes != null) {
			return this.attributes;
		}
		return null;
	}
	
	/** 
	 * @param name of the child node
	 * @return the List of nodes corresponding to this name
	 */
	public LinkedList<XMLNode> childrenList(String name){
		if(this.children != null) {
			return this.children.get(name);
		}
		return null;
	}
	
	
	/** 
	 * @param name of the child node
	 * @return the List of nodes corresponding to this name
	 */
	public HashMap<String, LinkedList<XMLNode>> childrenMap(){
		if(this.children != null) {
			return this.children;
		}
		return null;
	}
	
}
