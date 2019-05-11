package world.water;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;

import engine.Engine;
import openGL.Texture;

public class WaterBuffers {
	
	public static final int REFLECT_RES_WIDTH = 3*Engine.SCR_W/2;
	public static final int REFLECT_RES_HEIGHT = 3*Engine.SCR_H/2;
	
	
	private int reflectFrameBuffer;
	private int reflectTexture;
	private int reflectDepthBuffer;
	

	public WaterBuffers() {
		// Initializing Reflection Frame buffer
		reflectFrameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, reflectFrameBuffer);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		// Initializing Reflection Texture attachment
		reflectTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, reflectTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, REFLECT_RES_WIDTH, REFLECT_RES_HEIGHT, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, reflectTexture, 0);
		// Initializing Reflection depth render buffer attachment
		reflectDepthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, reflectDepthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, REFLECT_RES_WIDTH, REFLECT_RES_HEIGHT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, reflectDepthBuffer);
        //Exit Status
        
        //Unbind the framebuffer
        unBindCurrentFBO();
		
	}
	
	public void bindWaterReflectFBO() {
		glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		glActiveTexture(GL_TEXTURE0);
        glBindFramebuffer(GL_FRAMEBUFFER, reflectFrameBuffer);
        glViewport(0, 0, REFLECT_RES_WIDTH, REFLECT_RES_HEIGHT);
	}
	
	
	public void unBindCurrentFBO() {
		 glBindFramebuffer(GL_FRAMEBUFFER, 0);
	     glViewport(0, 0, Engine.SCR_W, Engine.SCR_H);
	}
	
	public Texture getReflectionTexture() {//get the resulting texture
		Texture reflect = new Texture();
		reflect.setID(reflectTexture);
		reflect.setWidth(REFLECT_RES_WIDTH);
		reflect.setHeight(REFLECT_RES_HEIGHT);
        return reflect;
    }
     
	
	
	
	public int getReflectionTextureID() {//get the resulting texture
		return reflectTexture;
	}
	
	
     
	
	public void destroy() {
        glDeleteFramebuffers(reflectFrameBuffer);
        glDeleteTextures(reflectTexture);
        glDeleteRenderbuffers(reflectDepthBuffer);
	}
}
