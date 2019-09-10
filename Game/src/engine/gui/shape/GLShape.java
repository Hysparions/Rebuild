package engine.gui.shape;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;

import engine.utils.Utilities;

public abstract class GLShape extends GLPrimitive {

	/** Float by vertex */
	public static final int VERTEXBYTES = 12;
	/** Vertex array object */
	protected final int VAO;
	/** Vertex Array Object */
	protected int VBO;
	/** Capacity of the buffer in bytes*/
	protected int capacity;
	
	/**
	 * Basic constructor of the shape
	 * @param vertexCount the vertex Amount of the shape defined in children class
	 */
	public GLShape(int vertexCount) {
		super();
		
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);

		// Creating a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertexCount * VERTEXBYTES, GL_STATIC_DRAW);

		// Setting the vertex Array attribute pointers
		// Vertex Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEXBYTES, 0);
		// Vertex Color
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_BYTE, false, VERTEXBYTES ,2 * Utilities.FLOATSIZE);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		this.vertexCount = vertexCount;
		this.buffer = BufferUtils.createByteBuffer(VERTEXBYTES * vertexCount);
	}


	/**
	 * Constructor of the GUI Shape class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 * @param vertexCount number of vertex used to draw the shape (3 per Triangle)
	 */
	public GLShape(int vertexCount, int optimalX, int optimalY) {
		this(vertexCount);
		this.optimal().set(optimalX, optimalY);
	}
	
	/** This method is used to build the buffer of the GUI component */
	public abstract void updateBuffer();
	
	/**
	 * Destroys the Shape
	 */
	protected void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
	
}
