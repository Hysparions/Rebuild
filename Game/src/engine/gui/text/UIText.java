package engine.gui.text;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
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

import engine.gui.UIBox;
import engine.gui.UIRenderable;
import engine.utils.Color;
import engine.utils.FontException;
import engine.utils.Utilities;

public abstract class UIText extends UIRenderable{

	
	/** Text as String */
	protected String text;
	/** Font used by the text */
	protected UIFont font;
	/** Font minimal size */
	protected float fontMin;
	/** Font maximal size */
	protected float fontMax;
	/** Vertex count */
	protected int vertexCount;
	/** Color of the text */
	protected Color color;
	/** Scale Matrix */
	protected Matrix4f model;
	
	
	protected UIText(UIBox box, String text, String font, Color color, float fontMin, float fontMax) {
		super(null);
		try {
			// Text 
			this.text = text;
			if(this.text == null) {
				this.text = new String("");
			}
			this.font = UIFont.get(font);
			if(this.font == null) {
				throw new FontException("No Font Provided");
			}
			fontMin = fontMin<UIFont.FONT_MIN_SIZE?UIFont.FONT_MIN_SIZE:(fontMin>UIFont.FONT_MAX_SIZE?UIFont.FONT_MAX_SIZE:fontMin);
			fontMax = fontMax<UIFont.FONT_MIN_SIZE?UIFont.FONT_MIN_SIZE:(fontMax>UIFont.FONT_MAX_SIZE?UIFont.FONT_MAX_SIZE:fontMax);
			if(fontMax<fontMin) {fontMax = fontMin;}
			this.fontMin = fontMin;
			this.fontMax = fontMax;
			// Vertex Count
			this.vertexCount = 6*charAmount();
			this.buffer = BufferUtils.createByteBuffer(vertexCount*UIWord.VERTEX_BYTE_SIZE);
			// Create model matrix
			this.model = new Matrix4f();
			// Keep color
			this.color = color;
			
			// Generate OpenGL Buffers
			this.VAO = glGenVertexArrays();
			glBindVertexArray(this.VAO);
			
			this.VBO = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
			glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);

			// Texture Position
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Utilities.FLOATSIZE, 0);
			// Vertex Position
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Utilities.FLOATSIZE, 2 * Utilities.FLOATSIZE);
			// Color attribute
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 4, GL_BYTE, false, 5 * Utilities.FLOATSIZE, 4 * Utilities.FLOATSIZE);
			
			glBindVertexArray(0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}catch(FontException e) {
			e.printStackTrace();
		}
		
	}
	
	protected UIText(UIBox box, String text, String font, Color color, float fontSize) throws FontException {
		this(box, text, font, color, fontSize, fontSize);
	}
	
	
	/**
	 * Getter for the number of chars in the text without spaces
	 * @return the number of visible chars
	 */
	public int charAmount() {
		return this.text.replaceAll(" ", "").length();
	}
	
	/**
	 * Getter for the string value of the UI Text
	 * @return the text value
	 */
	public String text() {
		return this.text;
	}

	@Override
	public void destroy() {
		glDeleteBuffers(this.VBO);
		glDeleteVertexArrays(this.VAO);
	}
	
	/** 
	 * Getter for the font used by the UI Text
	 * @return the font
	 */
	public UIFont font() {
		return this.font;
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
