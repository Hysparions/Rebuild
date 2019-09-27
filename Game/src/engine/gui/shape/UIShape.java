package engine.gui.shape;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import engine.ShaderManager;
import engine.gui.UIBox;
import engine.gui.UIRenderable;
import engine.opengl.Shader;
import engine.utils.ShaderException;
import engine.utils.Utilities;

public abstract class UIShape extends UIRenderable{

	/** Float by vertex */
	public static final int VERTEXBYTES = 12;
	/** Number of vertices in the shape */
	protected int vertexCount;
	/** Boolean used to know if the Shape should fit the size of its parent */
	protected boolean fitParent;
	/** Model matrix */
	protected final Matrix4f model;
	
	
	/**
	 * Basic constructor of the shape
	 * @param vertexCount the vertex Amount of the shape defined in children class
	 */
	protected UIShape(UIBox box, int vertexCount) {
		super(box);		
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
		this.model = new Matrix4f();
	}

	
	/**
	 * Overloaded Constructor for the UIShape class
	 * Set minimal optimal and maximal width to the specified parameter
	 * @param vertexCount number of vertex used to draw the shape (3 per Triangle)
	 * @param width	in pixels
	 * @param height in pixels
	 */
	public UIShape(UIBox box, int vertexCount, float width, float height) {
		this(box, vertexCount);
		this.box.minimal(width, height);
		this.box.optimal(width, height);
		this.box.maximal(width, height);
	}
	

	/**
	 * Overloaded Constructor for the UIShape class
	 * Set minimal optimal and maximal width to the specified parameter
	 * @param vertexCount number of vertex used to draw the shape (3 per Triangle)
	 * @param width	in pixels
	 * @param height in pixels
	 */
	public UIShape(UIBox box, int vertexCount, float minWidth, float minHeight, float optWidth, float optHeight, float maxWidth, float maxHeight) {
		this(box, vertexCount);
		this.box.minimal(minWidth, minHeight);
		this.box.optimal(optWidth, optHeight);
		this.box.maximal(maxWidth, maxHeight);
	}
	

	@Override
	public void render(ShaderManager shaders) {
		try {
			Shader shader = shaders.get("UIShape");
			// Compute model matrix
			this.model.identity();
			this.model.translate(this.box.position().x(),this.box.position().y() ,0.0f);
			this.model.scale(this.box.size().x(), this.box.size().y() ,1.0f);
			shader.use();
			shader.setMat4Uni("model", model);
			glBindVertexArray(VAO);
			glDrawArrays(GL_TRIANGLES, 0, vertexCount);
			glBindVertexArray(0);
			
		} catch (ShaderException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}

	/**
	 * Get the minimal box value
	 * @return minimal vector
	 */
	public Vector2f minimal() {
		return this.box.minimal();
	}
	
	/**
	 * Get the maximal box value
	 * @return maximal vector
	 */
	public Vector2f maximal() {
		return this.box.maximal();
	}
	
	/**
	 * Get the optimal box value
	 * @return optimal vector
	 */
	public Vector2f optimal() {
		return this.box.optimal();
	}

}
