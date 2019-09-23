package engine.gui;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import engine.opengl.Shader;

public abstract class UIRenderable extends UIComponent{
	
	/** Byte buffer containing vertex data */
	protected ByteBuffer buffer;
	/** Vertex array object */
	protected int VAO;
	/** Vertex Array Object */
	protected int VBO;
	/** Horizontal Alignment when component is smaller than the UIcomponent containing it */
	protected int horizontal;
	/** Vertical Alignment when component is smaller than the UIcomponent containing it */
	protected int vertical;
	
	/**
	 * Default constructor of a Renderable object
	 */
	public UIRenderable(boolean fitParent) {
		this.buffer = null;
		this.horizontal = 0;
		this.vertical = 0;
	}
	
	/** 
	 * This method is used to build the buffer of the GUI component 
	 * It is defined in child classes such as text and so on
	 */
	public abstract void computeBuffer();
	
	/**
	 * Send the byteBuffer to the GPU
	 */
	public void sendBuffer() {
		// Bind OpenGL Buffers
		glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * This method render the component to the screen
	 * It applies the global position offset of the absolute parent component
	 * @param windowPos 
	 */
	public abstract void render(Shader shader, Vector2f windowPos);
	
	@Override
	public void build(float x, float y, float width, float height) {
		this.box.position(x, y);
		this.box.size(width, height);
	}
	
	/**
	 * Set the horizontal and vertical alignment for the renderable
	 * @param horizontal set to Center if bad value
	 * @param vertical set to Center if bad value
	 */
	public void align(int horizontal, int vertical) {
		this.horizontal = horizontal;
		if(horizontal <0 || horizontal >2) {horizontal = 0;}
		this.vertical = vertical;
		if(vertical <0 || vertical >2) {vertical = 0;}
	}

}
