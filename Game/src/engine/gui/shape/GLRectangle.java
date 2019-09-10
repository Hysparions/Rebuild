package engine.gui.shape;

import java.nio.Buffer;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4i;

public class GLRectangle extends GLShape {

	private byte red;
	private byte green;
	private byte blue;
	private byte alpha;

	/**
	 * Default Constructor for the Rectangle class
	 * @param red color component
	 * @param green color component
	 * @param blue color component
	 * @param alpha color component
	 */
	public GLRectangle(int red, int green, int blue, int alpha) {
		super(6);
		red = red>255?255:(red<0?0:red);
		this.red = (byte)(red-128);
		green = green>255?255:(green<0?0:green);
		this.green = (byte)(green-128);
		blue = blue>255?255:(blue<0?0:blue);
		this.blue = (byte)(blue-128);
		alpha = alpha>255?255:(alpha<0?0:alpha);
		this.alpha = (byte)(alpha-128);
	}

	@Override
	public void updateBuffer() {
		buffer.putFloat(position.x);			buffer.putFloat(position.y);			buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		buffer.putFloat(position.x+size.x);		buffer.putFloat(position.y);			buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		buffer.putFloat(position.x + size.x);	buffer.putFloat(position.y+size.y);		buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		buffer.putFloat(position.x);			buffer.putFloat(position.y);			buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		buffer.putFloat(position.x + size.x);	buffer.putFloat(position.y + size.y);	buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		buffer.putFloat(position.x);			buffer.putFloat(position.y + size.y);	buffer.put(red);	buffer.put(green);	buffer.put(blue);	buffer.put(alpha);
		((Buffer)buffer).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, this.buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Getter for the rectangle color
	 * 
	 * @return rectangle color
	 */
	public Vector4i color() {
		return new Vector4i(red+128, green+128, blue+128, alpha+128);
	}

	/**
	 * Setter for the rectangle color
	 * 
	 * @param red   color component
	 * @param green color component
	 * @param blue  color component
	 * @param alpha color component
	 */
	public void setColor(int red, int green, int blue, int alpha) {
		red = red>255?255:(red<0?0:red);
		this.red = (byte)(red-128);
		green = green>255?255:(green<0?0:green);
		this.green = (byte)(green-128);
		blue = blue>255?255:(blue<0?0:blue);
		this.blue = (byte)(blue-128);
		alpha = alpha>255?255:(alpha<0?0:alpha);
		this.alpha = (byte)(alpha-128);
	}
	

	@Override
	public void build(float x, float y, float w, float h) {
		this.setBuild(true);
		this.position().set(x, y);
		this.size().set(w, h);
		updateBuffer();
	}

	@Override
	public void render() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		glBindVertexArray(0);		
	}

}
