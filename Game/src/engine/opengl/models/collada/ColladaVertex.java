package engine.opengl.models.collada;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIVector3D;

public class ColladaVertex {

	/** Position of the Collada Vertex in rest Position */
	private Vector3f position;
	/** Normal of the Collada Vertex in rest Position */
	private Vector3f normal;
	/** Color of the Collada Vertex */
	private byte r, g, b, a;
	/** Bones influencing the Collada Vertex*/
	private byte b0, b1, b2, b3;
	/** Position of the Collada Vertex in rest Position */
	private byte w0, w1, w2, w3;
	
	/** Constructor of the Collada Vertex */
	public ColladaVertex() {
		this.position = new Vector3f();
		this.normal = new Vector3f();
		this.r = -128;
		this.g = -128;
		this.b = -128;
		this.a = -128;
		this.b0 = -128;
		this.b1 = -128;
		this.b2 = -128;
		this.b3 = -128;
		this.w0 = -128;
		this.w1 = -128;
		this.w2 = -128;
		this.w3 = -128;
	}

	/** @return the position */
	public Vector3f position() {return position;}
	/**@return the normal*/
	public Vector3f normal() {return normal;}
	/**@return the red component of the vertex color*/
	public byte red() {return r;}
	/**@return the green component of the vertex color*/
	public byte green() {return g;}
	/** @return the blue component of the vertex color*/
	public byte blue() {return b;}
	/**@return the alpha component of the vertex color*/
	public byte alpha() {return a;}
	/** @return the first bone influencing*/
	public byte bone0() {return b0;}
	/**@return the second bone influencing*/
	public byte bone1() {return b1;}
	/**@return the third bone influencing*/
	public byte bone2() {return b2;}
	/**@return the fourth bone influencing*/
	public byte bone3() {return b3;}
	/** @return the weight of the first bone influencing*/
	public byte weight0() {return w0;}
	/**@return the weight of the second bone influencing*/
	public byte weight1() {return w1;}
	/**@return the weight of the third bone influencing*/
	public byte weight2() {return w2;}
	/**@return the weight of the fourth bone influencing*/
	public byte weight3() {return w3;}
	
	/**@param position the position to set*/
	public void setPosition(AIVector3D position) {
		this.position.x = position.x();
		this.position.y = position.y();
		this.position.z = position.z();
	
	}
	
	/** @param normal the normal to set*/
	public void setNormal(AIVector3D normal) {
		this.normal.x = normal.x();
		this.normal.y = normal.y();
		this.normal.z = normal.z();
	}
	
	public void setColor(AIColor4D color) {
		this.r = (byte) (255*color.r()-128.0f);
		this.g = (byte) (255*color.g()-128.0f);
		this.b = (byte) (255*color.b()-128.0f);
		//this.a = (byte) (255*color.a()-128.0f);
		this.a = -128;
	}
	
	/**
	 * Set the identifiers of the bones influencing this vertex
	 * @param b0 the first bone
	 * @param b1 the second bone
	 * @param b2 the third bone
	 * @param b3 the fourth bone
	 */
	public void setBonesID(byte b0, byte b1, byte b2, byte b3) {
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
	}
	
	/**
	 * Set the weight of the bones influencing this vertex
	 * @param w0 the first bone weight
	 * @param w1 the second bone weight
	 * @param w2 the third bone weight
	 * @param w3 the fourth bone weight
	 */
	public void setBonesWeight(byte w0, byte w1, byte w2, byte w3) {
		this.w0 = w0;
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
	}

}
