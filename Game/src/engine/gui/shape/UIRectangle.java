package engine.gui.shape;

import java.nio.Buffer;

import engine.gui.UIBox;
import engine.utils.Color;

public class UIRectangle extends UIShape{

	/** Color of the Rectangle */
	protected Color color;

	public UIRectangle(Color color, float width, float height) {
		super(null, 6, width, height);
		this.color = color;
		// Compute the buffer
		computeBuffer();
		// Send the buffer
		sendBuffer();
	}

	public UIRectangle(UIBox box, Color color) {
		super(box, 6);
		this.color = color;
		// Compute the buffer
		computeBuffer();
		// Send the buffer
		sendBuffer();
	}

	@Override
	public void computeBuffer() {
		// TOP RIGHT
		buffer.putFloat(1.0f);
		buffer.putFloat(0.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		// TOP LEFT
		buffer.putFloat(0.0f);
		buffer.putFloat(0.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		// BOTTOM LEFT
		buffer.putFloat(0.0f);
		buffer.putFloat(1.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		// TOP RIGHT
		buffer.putFloat(1.0f);
		buffer.putFloat(0.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		// BOTTOM LEFT
		buffer.putFloat(0.0f);
		buffer.putFloat(1.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		// BOTTOM RIGHT
		buffer.putFloat(1.0f);
		buffer.putFloat(1.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		((Buffer) buffer).flip();

	}

	/**
	 * Set the color of the rectangle
	 * @param red color component 
	 * @param green color component 
	 * @param blue color component 
	 * @param alpha color component 
	 */
	public void color(int red, int green, int blue, int alpha) {
		this.color.set(red, green, blue, alpha);
	}

}
