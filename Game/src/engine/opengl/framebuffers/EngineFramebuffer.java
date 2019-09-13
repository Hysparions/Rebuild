package engine.opengl.framebuffers;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import engine.opengl.Texture;
/**
 * This class holds the functions to create a custom frame buffer Object
 * @author louis
 *
 */
public class EngineFramebuffer {
	
	private final Vector2i engineSize;
	
	private String name;
	private int frambuffer;
	 
	private ArrayList<Attachment> colorAttachments;
	private Attachment depthAttachment;
	
	
	/**
	 * Constructor 
	 * @param name 
	 * @param adapt
	 */
	public EngineFramebuffer(String name, Vector2i engineSize) {
		this.engineSize = engineSize; 
		this.name = name;
		this.frambuffer = glGenFramebuffers();
		this.colorAttachments = new ArrayList<Attachment>();
	}
	
	/**
	 * Get the frameBuffer ID
	 * @return the id of the frame buffer
	 */
	public final int id() {
		return this.frambuffer;
	}
	
	/**
	 * Bind the frameBuffer 
	 */
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id());
	}
	
	/**
	 * Check the completeness of the frame buffer
	 * @return true if frame buffer is operational
	 */
	public boolean isComplete() {
		glBindFramebuffer(GL_FRAMEBUFFER, id());
		boolean complete = glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return complete;
	}
	
	/**
	 * Unbind the frame buffer
	 */
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0,  0, this.engineSize.x, this.engineSize.y);
	}
	
	public void bindToRender(int index, int textureUnit) {
		glBindTexture(GL_TEXTURE_2D, 0);// To make sure the texture isn't bound
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		this.bind();
		glViewport(0, 0, colorAttachments.get(index).width(), colorAttachments.get(index).height());
	}

	public void bindTexture(int index, int textureUnit) {
		glBindTexture(GL_TEXTURE_2D, colorAttachments.get(index).id());// To make sure the texture isn't bound
		glActiveTexture(GL_TEXTURE0+ textureUnit);
	}
	
	public void addColorAttachment(int width, int height, int format, boolean useNearest, boolean clampToEdge, boolean isDepth) {
		this.bind();
		Attachment attachment = Attachment.createRenderTexture(width, height, format,  useNearest, clampToEdge, isDepth); 
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+colorAttachments.size(), attachment.id(), 0);
		colorAttachments.add(attachment);
		glBindTexture(GL_TEXTURE_2D, 0);
		determineDrawBuffers();
		this.unbind();
	}

	public void setDepthBufferAttachment(int width, int height, int format, boolean isTexture) {
		this.bind();
		if(isTexture) {
			depthAttachment = Attachment.createRenderTexture(width, height, format, false, true, true);
			glFramebufferTexture(GL_FRAMEBUFFER,  GL_DEPTH_ATTACHMENT, depthAttachment.id(), 0);
			glBindTexture(GL_TEXTURE_2D, 0);
		}else {
			
			depthAttachment = Attachment.createRenderBuffer(width, height, format, true); 
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthAttachment.id());
			glBindRenderbuffer(GL_RENDERBUFFER, 0);
		}

		this.unbind();
	}
	
	
	private void determineDrawBuffers() {
		IntBuffer attachments = BufferUtils.createIntBuffer(this.colorAttachments.size());
		for(int i = 0 ; i<colorAttachments.size(); i++) {
			attachments.put(GL_COLOR_ATTACHMENT0 + i);
		}
		((Buffer)attachments).flip();
		glDrawBuffers(attachments);
	}
	
	public void destroy() {
		glDeleteFramebuffers(this.id());
		for(Attachment attachment : colorAttachments) {
			attachment.destroy();
		}
		if(depthAttachment != null) {
			depthAttachment.destroy();
		}
	}

	public Texture getTexture(int i) {
		Texture texture = new Texture();
		texture.setID(colorAttachments.get(i).id());
		texture.setWidth(colorAttachments.get(i).width());
		texture.setHeight(colorAttachments.get(i).height());
		return texture;
	}

	/**
	 * @return the name of the framebuffer
	 */
	public String name() {
		return this.name;
	}
}
