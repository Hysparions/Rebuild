package engine.gui.renderable;

import java.nio.ByteBuffer;

import engine.gui.core.UIBox;
import engine.opengl.Shader;

public abstract class UIRenderable {
	
	/** Box containing data on the renderable component */
	protected final UIBox box;
	/** Byte buffer containing vertex data */
	protected ByteBuffer buffer;
	/** Vertex array object */
	protected int VAO;
	/** Vertex Array Object */
	protected int VBO;
	
	/**
	 * Default constructor of a Renderable object
	 */
	public UIRenderable(UIBox box) {
		if(box == null) {
			this.box = new UIBox();
		}else {
			this.box = box;
		}
		this.buffer = null;
	}
	
	/** 
	 * This method is used to build the buffer of the GUI component 
	 * It is defined in child classes such as text and so on
	 */
	public abstract void updateBuffer();
	
	/**
	 * This method render the component to the screen
	 * It applies the global position offset of the absolute parent component
	 * @param xOffset in Pixels
	 * @param yOffset in Pixel
	 */
	public abstract void render(Shader shader, float xOffset, float yOffset);
	
	/**
	 * This method destroy all the OpenGL stuff associated to this renderable
	 */
	public abstract void destroy();

}
