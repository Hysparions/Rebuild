package recover.behaviors.batch;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.utils.Utilities;
import recover.behaviors.model.WaterModel;

import static org.lwjgl.opengl.GL30.*;
public class WaterBatch extends EngineBehavior{

	/** water model */
	private WaterModel water;
	/** Position offset */
	private Vector3f offset;
	private int VAO;
	private int VBO;
	private int vertexCount;
	
	/**
	 * Constructor of the Water Batch 
	 * @param water model used to build the batch
	 */
	public WaterBatch(WaterModel water) {
		super(BehaviorType.WATER_BATCH);
		this.water = water;
		this.water.addListeners(this);
		this.offset = new Vector3f();
		this.VAO = 0;
		this.VBO = 0;
	}
	
	/**
	 * @return true if the batch has been approved by the chunk culling test visible 
 	 */
	public boolean isVisible() {
		return this.water.isVisible();
	}

	/**
	 * Generate the OpenGL Buffers at the adequate size
	 */
	public void generate() {

		ByteBuffer buffer = water.getByteBuffer();
		vertexCount = buffer.capacity() / 16;
		// Generating and binding a new Vertex Array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		// Generating and binding a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
 

		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * Utilities.FLOATSIZE, 0);

		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_BYTE, false, 4 * Utilities.FLOATSIZE, 3 * Utilities.FLOATSIZE);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	/**
	 * Draws the batch to the screen
	 */
	public void draw() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		glBindVertexArray(0);
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
		this.offset.set(-camera.x, WaterModel.WATER_LEVEL-camera.y, -camera.z);
	}

	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
	@Override
	public boolean generateSceneEvent() {
		return true;
	}

}
