package engine.opengl.models.collada;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import engine.opengl.models.animation.Bone;
import engine.opengl.models.animation.Skeleton;
import engine.utils.Utilities;

public class ColladaModel {

	/** Name of the model */
	private String name;
	/** fileName directory */
	private String directory;
	/** Skeleton data */
	private Skeleton skeleton;
	/** Map containing the list of vertices of each mesh */
	private HashMap<String,ArrayList<ColladaVertex>> meshes;
	// Number of floats of a polyModel
	public final static int VERTEX_BYTE_SIZE = 6*Utilities.FLOATSIZE+4;

	/** Constructor of a Collada model reading the file specified by the string in args  
	 * @param name of the model
	 * @param directory of the model in ressource folder
	 */
	public ColladaModel(String name, String directory) {
		this.name = name;
		this.directory = directory;
		this.meshes = new HashMap<String,ArrayList<ColladaVertex>>();
		
		// Loading XLM Nodes hierarchy
		XMLNode masterNode = XMLParser.loadXML("/models/" + this.directory + "/" +  this.name + ".dae");
		XMLNode controllerNode = XMLParser.findFirstChild(masterNode, "library_controllers");
		XMLNode visualNode = XMLParser.findFirstChild(masterNode, "library_visual_scenes");
		this.skeleton = processSkeleton(controllerNode,visualNode);
		//skeleton.display();
	}

	
	public Skeleton processSkeleton(XMLNode controllerNode, XMLNode visualNode) {
		// Testing existence of controller node
		if(controllerNode != null) {
			// Testing existence of a Skeleton
			XMLNode skeletonNode = XMLParser.findFirstChild(controllerNode, "controller");
			if(skeletonNode != null) {
				String skeletonName = skeletonNode.attribute("name");
				XMLNode node = XMLParser.findChild(controllerNode, "Name_array", "id", "Skeleton_Hands-skin-joints-array");
				// Testing existence of bones
				if(node != null && skeletonName != null) {
					//Capture bones raw name data
					String data[] = node.data().split(" "); 
					
					// Getting node of skeleton
					skeletonNode = XMLParser.findChild(visualNode,"node", "name", skeletonName);
					node = XMLParser.findChild(visualNode,"matrix", "sid", "transform");
					Matrix4f skeletonMatrix4f = parseMatrix(node.data().split(" "));
					// Finally Process Bones
					return new Skeleton(skeletonName, processBone(XMLParser.findFirstChild(skeletonNode, "node"), data), skeletonMatrix4f);
				}
			}
		}
		return null;
	}
	
	public Bone processBone(XMLNode bone, String bonesOrder[]) {
		// Finding name
		String name = bone.attribute("sid");
		// Finding id
		int identifier=-1;
		for(int i = 0; i<bonesOrder.length; i++) {
			if(bonesOrder[i].equals(name)) {
				identifier = i;
				break;
			}
		}
		// Finding matrix
		XMLNode node = XMLParser.findFirstChild(bone,"matrix");
		Matrix4f matrix = parseMatrix(node.data().split(" "));

		LinkedList<Bone> children = null;
		if(bone.childrenMap().get("node") != null) {
			// Finding children
			children = new LinkedList<>();
			for(XMLNode child : bone.childrenMap().get("node")) {
				children.add(processBone(child, bonesOrder));
			}
		}
		
		return new Bone(identifier, name, children, matrix);
	}
	
	public void processGeometry(XMLNode node) {
		
	}
	
	public void processAnimation(XMLNode node) {
		
	}

	/** @return the name of the model */
	public String getName() {
		return name;

	}

	/** @return the amount of meshes in the model */
	public int amount() {
		return meshes.size();
	}

	/**
	 * @param mesh the name of the mesh
	 * @return the mesh vertex list
	 */
	public List<ColladaVertex> getVertices(String mesh) {
		return this.meshes.get(mesh);
	}
	
	public Matrix4f parseMatrix(String str[]) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(str.length);
		for(int i=0; i<str.length; i++) {
			buffer.put(Float.parseFloat(str[i]));
		}
		((Buffer)buffer).flip();
		return new Matrix4f(buffer);
	}
}
