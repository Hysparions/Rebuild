package engine.opengl.models;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.models.stanford.PolyModel;
import engine.opengl.models.stanford.PolyVertex;
import recover.behaviors.Location;

public class StaticModel extends EngineBehavior{
	
	// The location needed to translate the model to it's world position
	private Location location;
	// reference to the model used to create the buffer
	private PolyModel model;
	// Float buffer used to send vertices data to the GPU using glBUfferSubData
	private ByteBuffer buffer;

	// Size in vertex;
	private int size;
	// Offest position in bytes of the model into the buffer
	private int offset;
	
	private Vector4f vector;
	private Matrix4f matrix;
	
	
	
	/**
	 * Constructor of the Batch element class
	 */
	public StaticModel(PolyModel model, Location location) {
		super(BehaviorType.STATIC_MODEL);
		this.location = location;
		this.model = model;
		this.size = model.size();
		this.offset = 0;
		this.vector = new Vector4f();
		this.matrix = new Matrix4f();
		this.buffer = null;
	}
	
	/** @return the offset of the model in the buffer */
	public int offset() {return offset;}
	
	/** @return the size of the model in vertex in the buffer */
	public int size() {return size;}
	
	/** Set the byte buffer of the model */
	public void setViewBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public void calculateVertices() {
		synchronized(model) {
			List<PolyVertex> vertices = model.getVertices();
			for (int i = 0; i < size(); i++) {
				PolyVertex vertex = vertices.get(i);
				vector.set(vertex.position.x, vertex.position.y, vertex.position.z, 1.0f);
				vector.mul(location.matrix);
				// Position of the Decoration Object
				buffer.putFloat(vector.x);
				buffer.putFloat(vector.y);
				buffer.putFloat(vector.z);
				vector.set(vertex.normal.x, vertex.normal.y, vertex.normal.z, 1.0f);
				matrix.set(location.matrix);
				matrix.transpose();
				matrix.invert();
				vector.mul(matrix);
				// Normals of the Decoration object
				buffer.putFloat(vector.x);
				buffer.putFloat(vector.y);
				buffer.putFloat(vector.z);
				// Color of the Decoration object
				buffer.put(vertex.color.r());
				buffer.put(vertex.color.g());
				buffer.put(vertex.color.b());
				buffer.put(vertex.color.a());
			}
			((Buffer)buffer).flip();
		}
	}

	/** @return the byte buffer of the decoration object */
	public ByteBuffer byteBuffer() {
		return this.buffer;
	}
	
	@Override
	public boolean generateSceneEvent() {
		return false;
	}

	/** 
	 * Set the offset of the model in the buffer
	 * @param offset in bytes 
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
