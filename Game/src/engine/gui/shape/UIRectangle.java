package engine.gui.shape;

import java.nio.Buffer;

import engine.utils.Color;

public class UIRectangle extends UIShape{

	/** Color of the Rectangle */
	protected Color color;

	public UIRectangle(Color color, float width, float height) {
		super(6, width, height);
		this.color = color;
		// Compute the buffer
		computeBuffer();
		// Send the buffer
		sendBuffer();
	}

	public UIRectangle(Color color) {
		super(false, 6);
		this.color = color;
	}

	@Override
	public void computeBuffer() {
		// TOP LEFT
		buffer.putFloat(0.0f);
		buffer.putFloat(0.0f);
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
		// BOTTOM RIGHT
		buffer.putFloat(1.0f);
		buffer.putFloat(1.0f);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		((Buffer) buffer).flip();

	}


}
