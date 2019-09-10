package recover.behaviors.batch;

import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.batch.Batch;
import engine.opengl.models.StaticModel;
import engine.opengl.models.stanford.PolyModel;
import engine.utils.Utilities;
import recover.behaviors.model.TerrainModel;

/** This batch contains all elements of the scene that are static and may emit
 * light. Rocks, resources and buildings for example
 * @author louis
 */
public class DecorationBatch extends EngineBehavior implements Batch<StaticModel>{

	/** Default Capacity of the batch = 1000k bytes */
	private static int DEFAULT_CAPACITY = 1000000;
	
	/** Vertex Buffer Object */
	private int VBO;
	/** Vertex Array Object */
	private int VAO;
	/** Position offset */
	private Vector3f offset;
	/** Limit of the buffer in bytes */
	private int limit;
	/** List of batch elements */
	private List<StaticModel> elements;
	/** Bytebuffer of the batch */
	ByteBuffer buffer;
	
	
	private TerrainModel terrain;
	
	public DecorationBatch(TerrainModel terrain) {
		super(BehaviorType.DECORATION_BATCH);
		this.VAO = 0;
		this.VBO = 0;
		this.limit = 0;
		this.offset = new Vector3f();
		this.elements = new LinkedList<StaticModel>();
		this.buffer = BufferUtils.createByteBuffer(DEFAULT_CAPACITY);
		this.terrain = terrain;
	}

	@Override
	public void generate() {
		// Generating and binding a new Vertex Array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		// Generating and binding a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
 
		// Position of the Vertex
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Utilities.FLOATSIZE, 0);
		// Normals of the Vertex
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 7 * Utilities.FLOATSIZE, 3 * Utilities.FLOATSIZE);
		// Color and alpha Value of the Vertex
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 4, GL_BYTE, false, 7 * Utilities.FLOATSIZE, 6 * Utilities.FLOATSIZE);
		
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		this.deactivate();
		
	}

	@Override
	public void draw() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, limit/PolyModel.VERTEX_BYTE_SIZE);
		glBindVertexArray(0);
	}

	@Override
	public void add(StaticModel model, boolean updateVBO) {
		
		boolean regenerate = false;
		// Setting the offset of the model
		model.setOffset(limit);
		// Calculating new Limit
		limit += model.size()*PolyModel.VERTEX_BYTE_SIZE;
		
		// If the bytebuffer is too small
		if(limit > buffer.capacity()) {
			this.sizeBuffer();
			regenerate = true;
		}
		// Adding the model to the list of models
		this.elements.add(model);

		// Creating a byte buffer for the model
		((Buffer)this.buffer).position(model.offset());
		((Buffer)this.buffer).limit(limit);

		model.setViewBuffer(buffer.slice());		
		model.calculateVertices();
		((Buffer)this.buffer).position(0);
		((Buffer)this.buffer).limit(limit);
		
		// Update VBO
		if(updateVBO) {
			if(regenerate) {
				reload();
			}else {
				update(model.offset(), limit);
			}
		}
		
	}

	@Override
	public void remove(StaticModel model, boolean updateVBO) {
		// Get the index of the object given as argument
		int index = this.elements.indexOf(model);
		// if the object exists
		if(index != -1) {
			// Remove the model
			this.elements.remove(index);
			this.limit -= model.size()*PolyModel.VERTEX_BYTE_SIZE;
			// If the new element at the index position isn't the last one of the list
			if(index < this.elements.size()) {
				int offset = model.offset();
				//Shift every other models
				for(int i = index ; i < this.elements.size(); i++) {
					StaticModel shifted = this.elements.get(index);
					// Set the new offset of the model shifted
					shifted.setOffset(offset);
					// Copying data to its new location
					((Buffer)this.buffer).position(offset);
					this.buffer.put(shifted.byteBuffer());
					// Recreating bytebuffer view
					((Buffer)this.buffer).position(offset);
					((Buffer)this.buffer).limit(offset+shifted.size()*PolyModel.VERTEX_BYTE_SIZE);
					shifted.setViewBuffer(buffer.slice());
					offset+=shifted.size()*PolyModel.VERTEX_BYTE_SIZE;
				}
			}
			if(updateVBO) {
				update(model.offset(), limit);
			}
			((Buffer)buffer).position(0);
			((Buffer)buffer).limit(limit);
		}	
	}

	@Override
	public void sizeBuffer() {
		
		int newCapacity = this.buffer.capacity();
		while(newCapacity < this.limit) {
			newCapacity += DEFAULT_CAPACITY;
		}
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		// Copying old buffer to new buffer
		newBuffer.put(this.buffer);
		// Recreating viewBuffers
		for(StaticModel model : elements) {
			newBuffer.position(model.offset());
			newBuffer.limit(model.offset() + model.size() * PolyModel.VERTEX_BYTE_SIZE);
			model.setViewBuffer(newBuffer.slice());
		}
		this.buffer = newBuffer;
		((Buffer)this.buffer).position(0);
		((Buffer)this.buffer).limit(limit);
	}

	@Override
	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}


	@Override
	public void update(int offset, int limit) {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		((Buffer)buffer).position(offset);
		((Buffer)buffer).limit(limit);
		ByteBuffer drawBuffer = buffer.slice();
		drawBuffer.order(ByteOrder.LITTLE_ENDIAN);
		glBufferSubData(GL_ARRAY_BUFFER, offset, drawBuffer);
		((Buffer)buffer).position(0);
		((Buffer)buffer).limit(this.limit);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void updateAll() {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void reload() {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public int VBO() {return this.VBO;}

	@Override
	public int VAO() {return this.VAO;}

	@Override
	public int capacity() {return this.buffer.capacity();}

	@Override
	public int limit() {return this.limit;}

	@Override
	public List<StaticModel> elements() {return this.elements;}

	@Override
	public StaticModel element(int i) {return this.elements.get(i);}

	@Override
	public boolean generateSceneEvent() {
		return true;
	}
	
	/**
	 * @return the offset
	 */
	public Vector3f offset() {
		return offset;
	}
	
	/**
	 * @param offset position from the camera
	 */
	public void calculateOffset(Vector3f camera) {
		this.offset.set(-camera.x, -camera.y, -camera.z);
	}
	
	/**
	 * Get the culling visibility of the batch
	 * @return true if the batch should be drawn
	 */
	public boolean isVisible() {return this.terrain.isVisible();}
	
	/**
	 * Get the terrain model associated with the entities
	 * @return the terrain model
	 */
	public TerrainModel terrain() {return this.terrain;}

}
