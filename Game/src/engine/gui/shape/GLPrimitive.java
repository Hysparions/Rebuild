package engine.gui.shape;

import java.nio.ByteBuffer;

import engine.gui.GUIComponent;

public abstract class GLPrimitive extends GUIComponent {

	/** Buffer containing vertex data for batching */
	protected ByteBuffer buffer;
	/** Vertex Size of the shape Component */
	protected int vertexCount;
	/** Offset of the Primitive Component in the batch */
	protected long offset;
	/** Boolean to know if the primitive have been registered for drawing */
	protected boolean registered;

	/**
	 * Default Constructor of the GUI Primitive class
	 */
	public GLPrimitive() {
		super();
		this.vertexCount = 0;
		this.offset = 0;
	}

	/**
	 * Constructor of the GUI Primitive class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public GLPrimitive( int optimalX, int optimalY) {
		super(optimalX, optimalY);
	}


	/**
	 * Update the vertex Float buffer with the Primitive vertex Information
	 * contained in child class
	 */
	protected abstract void updateBuffer();
	

	/**
	 * Getter for the vertex buffer
	 * 
	 * @return the Float buffer containing the primitive component vertex data
	 */
	public ByteBuffer buffer() {
		return buffer;
	}

	/**
	 * Getter for the vertex count
	 * 
	 * @return the primitive component vertex Count
	 */
	public int vertexCount() {
		return vertexCount;
	}

	/**
	 * Getter for the Batching Offset
	 * 
	 * @return Offset of the Primitive type in the buffer
	 */
	public long offset() {
		return offset;
	}

	/**
	 * Setter for the Offset parameter
	 * 
	 * @param offset
	 */
	public void setOffset(long offset) {
		if (offset >= 0) {
			this.offset = offset;
		}
	}
	
	/**
	 * Register the primitive has a batched one
	 */
	public void register() {
		this.registered = true;
	}
	
	/**
	 * Unregister the primitive has a batched one
	 */
	public void unregister() {
		this.registered = false;
	}
	
	/**
	 * @return true if the shape is registered to be drawn
	 */
	public boolean isRegistered() {
		return registered;
	}
}
