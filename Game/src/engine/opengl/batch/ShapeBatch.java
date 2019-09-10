package engine.opengl.batch;

import java.util.List;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL30.*;

import engine.gui.shape.GLShape;
import engine.utils.Utilities;

/**
 * Batch used to store simple color Rectangles for GUI Rendering
 * 
 * @author louis
 *
 */
public class ShapeBatch implements Batch<GLShape> {
	
	/** Default capacity */
	private int DEFAULT_CAPACITY = 300;
	/** Vertex Buffer Object */
	protected int VBO;
	/** Vertex Array Object */
	protected int VAO;
	/** Capacity of the buffer in bytes*/
	protected int capacity;
	/** Limit of the buffer in bytes */
	protected int limit;
	/** Limit of the buffer */
	protected List<GLShape> elements;
	private Matrix4f transform;

	public ShapeBatch() {
		this.capacity = DEFAULT_CAPACITY;
		this.limit = 0;
		
		transform = new Matrix4f();
		// Creating a new vertex array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);

		// Creating a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, capacity * Utilities.FLOATSIZE, GL_DYNAMIC_DRAW);

		// Setting the vertex Array attribute pointers
		// Vertex Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, GLShape.VERTEXBYTES, 0);
		// Vertex Color
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_BYTE, false, GLShape.VERTEXBYTES ,2 * Utilities.FLOATSIZE);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	@Override
	public void add(GLShape shape, boolean updateVBO) {
		if (shape != null) {
			// Adding the new Element to the buffer
			elements.add(shape);
			if ((limit + shape.vertexCount()) * GLShape.VERTEXBYTES <= capacity) {
				// The model offset in the batch is now set to the end of the buffer
				shape.setOffset(limit * GLShape.VERTEXBYTES);
				limit += shape.vertexCount();
				glBindBuffer(GL_ARRAY_BUFFER, VBO);
				glBufferSubData(GL_ARRAY_BUFFER, shape.offset(), shape.buffer());
				glBindBuffer(GL_ARRAY_BUFFER, 0);
			} else {
				System.err.println("Not Enough Space");
			}
		}
	}

	@Override
	public void remove(GLShape panel, boolean updateVBO) {
		// Adding the new Element to the buffer
		elements.remove(panel);
		// Shifting elements to remove holes
	
	}

	@Override
	public void sizeBuffer() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void update(int offset, int limit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public GLShape element(int i) {
		GLShape elem = null;
		try {
			elem = this.elements.get(i);
		} catch (IndexOutOfBoundsException e) {

		}
		return elem;
	}

	@Override
	public void draw() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, limit);
		glBindVertexArray(0);

	}

	@Override
	public void destroy() {
		this.elements.clear();
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}

	public void buildMatrix(float x, float y, float w, float h) {
		transform.identity();
		transform.scale(2.0f / w, 2.0f / h, 1.0f);
		transform.translate(-1.0f + 2.0f / x, -1.0f + 2.0f / y, 0.0f);		
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int VBO() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int VAO() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int capacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int limit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GLShape> elements() {
		// TODO Auto-generated method stub
		return null;
	}
}
