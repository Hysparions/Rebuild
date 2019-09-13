package engine.gui.renderable.shape;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.Buffer;

import engine.opengl.Shader;
import engine.utils.Color;

public class UIRectangle extends UIShape{
	
	/** Color of the Rectangle */
	protected Color color;

	public UIRectangle(Color color, float width, float height) {
		super(6, width, height);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateBuffer() {
		buffer.putFloat(box.position().x);
		buffer.putFloat(box.position().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		buffer.putFloat(box.position().x + box.size().x);
		buffer.putFloat(box.position().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		buffer.putFloat(box.position().x +  box.size().x);
		buffer.putFloat(box.position().y +  box.size().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		buffer.putFloat(box.position().x);
		buffer.putFloat(box.position().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		buffer.putFloat(box.position().x +  box.size().x);
		buffer.putFloat(box.position().y +  box.size().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		buffer.putFloat(box.position().x);
		buffer.putFloat(box.position().y + box.size().y);
		buffer.put(color.r());
		buffer.put(color.g());
		buffer.put(color.b());
		buffer.put(color.a());
		((Buffer) buffer).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, this.buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}


	
	

}
