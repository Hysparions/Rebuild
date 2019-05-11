package world.behavior;

import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import world.models.PolyModel;
import world.models.StaticVertex;

public class StaticModel extends Behavior{
	
	//The location needed to translate the model to it's world position
	private Location position;
	//reference to the model used to create the buffer
	private PolyModel model;
	//Float buffer used to send vertices data to the GPU using glBUfferSubData
	private FloatBuffer buffer;

	
	//Size in vertex;
	public int size;
	//Offest position of the model into the buffer
	public long offset;
	//Boolean to true if the Floatbuffer needs to be compiled
	private boolean actualizeBuffer;
	
	private Vector4f vector;
	private Matrix4f matrix;
	
	public StaticModel (Location location, PolyModel model, boolean active){
		super(BehaviorType.STATICMODEL, active);
		this.position = location;
		this.model = model;
		this.size = model.getSize();
		this.offset = 0;
		this.actualizeBuffer = true;
		this.buffer  = BufferUtils.createFloatBuffer(size * StaticVertex.SIZE);
		vector = new Vector4f();
		matrix = new Matrix4f();
	}

	@Override
	public void update() {
		if(isActive) {
			if(actualizeBuffer) {
				List<StaticVertex> vertices = model.getVertices();
				for(int i = 0 ; i < size ; i ++) {
					StaticVertex vertex = vertices.get(i);
					vector.set( vertex.position.x, vertex.position.y, vertex.position.z, 1.0f);
					vector.mul(position.matrix);
					buffer.put(vector.x);
					buffer.put(vector.y);
					buffer.put(vector.z);
					vector.set( vertex.normal.x, vertex.normal.y, vertex.normal.z, 1.0f);
					matrix.set(position.matrix);
					matrix.transpose();
					matrix.invert();
					vector.mul(matrix);
					buffer.put(vector.x);
					buffer.put(vector.y);
					buffer.put(vector.z);
					buffer.put(vertex.color.x);
					buffer.put(vertex.color.y);
					buffer.put(vertex.color.z);
					buffer.put(vertex.flexibility);
				}
				buffer.flip();
				actualizeBuffer = false;
			}
			//Disactivate the behavior until location change
			this.Disactivate();
		}
	}
	

	public FloatBuffer getBuffer() {
		return this.buffer;
	}
}
