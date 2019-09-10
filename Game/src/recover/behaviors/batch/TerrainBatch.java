package recover.behaviors.batch;

import org.joml.Vector3f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.utils.Utilities;
import recover.behaviors.model.TerrainModel;

import static org.lwjgl.opengl.GL30.*;
public class TerrainBatch extends EngineBehavior {
	
	/** Vertex Array Buffer */
	private int VAO;
	/** Vertex Buffer Object */
	private int VBO;
	/** Index Buffer Object */
	private int EBO;
	/** Terrain Model contains vertex Data */
	private TerrainModel model;
	/** offset position from the camera */
	private Vector3f offset;
	
	public TerrainBatch(TerrainModel model) {
		super(BehaviorType.TERRAIN_BATCH);
		this.model = model;
		this.model.addListeners(this);
		this.offset = new Vector3f();
		VAO = 0;
		VBO = 0;
		EBO = 0;
	}
	
	/**
	 * Generate the Vertex Buffer - the Index Buffer - and the Vertex Array
	 * Should only be called in the openGL thread
	 */
	public void generate() {
		// Generating and binding a new Vertex Array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		// Generating and binding a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, model.vertices(), GL_DYNAMIC_DRAW);
		// Generating and binding a new Element Buffer
		EBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, model.indices(), GL_STATIC_DRAW);

		// Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 13 * Utilities.FLOATSIZE, 0);
		// Normal
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 13 * Utilities.FLOATSIZE, 3 * Utilities.FLOATSIZE);
		// OldColor
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 13 * Utilities.FLOATSIZE, 6 * Utilities.FLOATSIZE);
		// NewColor
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 13 * Utilities.FLOATSIZE, 9 * Utilities.FLOATSIZE);
		// Time
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 1, GL_FLOAT, false, 13 * Utilities.FLOATSIZE, 12 * Utilities.FLOATSIZE);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Draw the batch to the screen
	 */
	public void draw() {
		if (model.vertices() != null && model.indices() != null) {
			glBindVertexArray(VAO);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
			glDrawElements(GL_TRIANGLES, model.indices().capacity(), GL_UNSIGNED_INT, 0);
			//glDrawArrays(GL_TRIANGLES,  model.indices().capacity(), GL_FLOAT);

			glBindVertexArray(0);
		}
	}

	/**
	 * Update the VBO With the terrain model data
	 */
	public void updateGPUBuffer() {
		if (model.vertices() != null && model.indices() != null) {
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferSubData(GL_ARRAY_BUFFER, 0, model.vertices());
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}

	/**
	 * Destroy the batch and openGL buffers
	 */
	public void destroy() {
		glDeleteBuffers(EBO);
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}

	@Override
	public boolean generateSceneEvent() { 
		// Should notify batch system that this terrain batch was added
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
	 * @return true if the batch has been approved by the chunk culling test visible 
 	 */
	public boolean isVisible() {
		return this.model.isVisible();
	}


}
