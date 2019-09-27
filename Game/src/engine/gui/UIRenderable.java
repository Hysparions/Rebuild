package engine.gui;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.ByteBuffer;

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
	protected UIRenderable() {
		this(null);
	}
	
	/**
	 * Default constructor of a Renderable object
	 */
    protected UIRenderable(UIBox box) {
    	super(box);
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
	
	@Override
	public void build(float x, float y, float width, float height) {
		this.box.position(x, y);
		this.box.size(width, height);
		if(this.box.keepAspect()) {
			float boxAspect = this.box().optimal().x() / this.box().optimal().y();
			float aspect = this.box().size().x() / this.box().size().y();
			if(boxAspect != 0.0f && aspect != 0.0f) {
				float ratio = boxAspect/aspect;
				if(ratio > 1.0f) {
					float yOffset = (this.box.size().y() - (this.box.size().y()/ratio))/2.0f;
					this.box.size(this.box.size().x(), this.box.size().y()/ratio);
					this.box.position(x, y + yOffset);
				}else if(ratio < 1.0f){
					float xOffset = (this.box.size().x() - (this.box.size().x()*ratio))/2.0f;
					this.box.size(this.box.size().x()*ratio, this.box.size().y());
					this.box.position(x+xOffset, y);
				}
			}
		}
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
