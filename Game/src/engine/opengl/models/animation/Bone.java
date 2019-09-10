package engine.opengl.models.animation;

import java.util.LinkedList;

import org.joml.Matrix4f;

/**
 * This class defines the bones of an animation 
 * @author louis
 *
 */
public class Bone {

	/** Id of the bone */
	private int identifier;
	/** Name of the bone */
	private String name;
	/** children bones */
	private LinkedList<Bone> children;
	/** matrix transform relative to the parent */
	private Matrix4f matrix;
	
	/** 
	 * Constructor of the Bone class
	 * @param identifier of the Bone
	 * @param name of the Bone 
	 * @param children Bones
	 * @param matrix rotation and translation of the bone
	 */
	public Bone(int identifier, String name, LinkedList<Bone> children, Matrix4f matrix) {
		this.identifier = identifier;
		this.name = name;
		this.children = children;
		this.matrix = matrix;
	}

	/** Display the bone data
	 */
	public void display() {
		System.out.println("Bone " + identifier + " " + name + " :");
		System.out.println(this.matrix);
		if(children != null) {
			for(Bone bone : children) {
				bone.display();
			}
		}
	}
	
	
}
