package engine.gui.renderable.shape;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
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

import org.lwjgl.BufferUtils;

import engine.gui.core.UIBox;
import engine.gui.renderable.UIRenderable;
import engine.opengl.Shader;
import engine.utils.Utilities;

public abstract class UIShape extends UIRenderable{

	/** Float by vertex */
	public static final int VERTEXBYTES = 12;
	/** Number of vertices in the shape */
	protected int vertexCount;
	/** Boolean used to know if the Shape should fit the size of its parent */
	protected boolean fitParent;
	
	/**
	 * Basic constructor of the shape
	 * @param vertexCount the vertex Amount of the shape defined in children class
	 */
	public UIShape(int vertexCount, UIBox box) {
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
	}

	
	/**
	 * Overloaded Constructor for the UIShape class
	 * Set minimal optimal and maximal width to the specified parameter
	 * @param vertexCount number of vertex used to draw the shape (3 per Triangle)
	 * @param width	in pixels
	 * @param height in pixels
	 */
	public UIShape(int vertexCount, float width, float height) {
		this(vertexCount, new UIBox(width, height));
	}
	

	/**
	 * Overloaded Constructor for the UIShape class
	 * Set minimal optimal and maximal width to the specified parameter
	 * @param vertexCount number of vertex used to draw the shape (3 per Triangle)
	 * @param width	in pixels
	 * @param height in pixels
	 */
	public UIShape(int vertexCount, float minWidth, float minHeight, float optWidth, float optHeight, float maxWidth, float maxHeight) {
		this(vertexCount, new UIBox(minWidth, minHeight, optWidth, optHeight, maxWidth, maxHeight));
	}
	
	@Override
	public void render(Shader shader, float xOffset, float yOffset) {
		
	}
	
	@Override
	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}

}
