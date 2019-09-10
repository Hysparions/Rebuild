package engine.opengl.framebuffers;

import static org.lwjgl.opengl.GL33.*;

import java.nio.ByteBuffer;

/**
 * This class is an attachment factory for the FBO class
 * @author louis
 *
 */
public class Attachment {

	private int id;
	private int format;
	private boolean isDepth;
	private boolean isRenderBuffer;
	private int width;
	private int height;
	
	private Attachment(int width, int height, int id, int format, boolean isDepth, boolean isRenderBuffer) {
		this.id = id;
		this.isDepth = isDepth;
		this.format = format;
		this.isRenderBuffer = isRenderBuffer;
		this.width = width;
		this.height = height;
	}
	
	public static Attachment createRenderTexture(int width, int height, int format, boolean useNearest, boolean clampToEdge, boolean isDepth) {
		
		// Generates the texture
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		// Set the texture parameters
		int params = useNearest ? GL_NEAREST : GL_LINEAR;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, params);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, params);
		params = clampToEdge ? GL_CLAMP_TO_EDGE : GL_REPEAT;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, params);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, params);
		// Send info to the GPU to create an empty texture to the specified size
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		return new Attachment(width, height, id, format, isDepth, false);
	}
	
	public static Attachment createRenderBuffer(  int width, int height, int format, boolean isDepth) {
		// Generates the Render depth buffer
		int id = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
		return new Attachment(width, height, id, format, isDepth, true);
	}

	public void destroy() {
		if(isRenderBuffer) {
			glDeleteRenderbuffers(this.id);
		}else {
			glDeleteTextures(this.id);
		}
	}
	
	/**@return the id*/
	public int id() {return id;}
	/** @return the format*/
	public int format() {return format;}
	/** @return the isDepth*/
	public boolean isDepth() {return isDepth;}
	/** @return the isRenderBuffer*/
	public boolean isRenderBuffer() {return isRenderBuffer;}
	/** return width of the attachment */
	public int width() { return width; }
	/** return height of the attachment */
	public int height() { return height; }
	
	
}
