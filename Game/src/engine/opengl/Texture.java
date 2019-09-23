package engine.opengl;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


import engine.utils.TextureException;

public class Texture {

	private String filePath;
	private int id;
	private int width;
	private int height;
	private int channels;
	// Used by Models to determine if it's a diffuse or a Specular texture
	private String type;

	public Texture()  throws TextureException{
		filePath = "";
		id = 0;
		width = 0;
		height = 0;
		channels = 0;
		type = "";
	}

	public Texture(String path) throws TextureException{
		try {
			// Storing the path could be useful to know if the texture have already beenstored
			this.filePath = path;
			// Reading the file

			BufferedImage bufferedImage = ImageIO.read(Texture.class.getResourceAsStream(this.filePath));
			// Get BUffer Values
			this.width = bufferedImage.getWidth();
			this.height = bufferedImage.getHeight();
			this.channels = bufferedImage.getColorModel().getNumComponents();
			int[] pixels = bufferedImage.getRGB(0, 0, this.width, this.height, null, 0,this.width);
		    ByteBuffer image = ByteBuffer.allocateDirect(pixels.length * 4);
		    for (int pixel : pixels) {
		    	image.put((byte) ((pixel >> 16) & 0xFF));
		    	image.put((byte) ((pixel >> 8) & 0xFF));
		    	image.put((byte) (pixel & 0xFF));
		    	image.put((byte) (pixel >> 24));
		    }
		    ((Buffer)image).flip();
		    

			// Generate the texture to the address defined in the id
			this.id = glGenTextures();
			// System.out.println(tex.filePath + tex.id);
			// Bind it as the current Texture
			glBindTexture(GL_TEXTURE_2D, this.id);

			// Setup wrap mode
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
			// Set the Texture Scaling filters
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 7);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			// Generate MipMap automatically
			glGenerateMipmap(GL_TEXTURE_2D);
			// Unbind Texture
			glBindTexture(GL_TEXTURE_2D, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Returns the id of the Texture */
	public int ID() {
		return this.id;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getPath() {
		return this.filePath;
	}

	public int getChannels() {
		return this.channels;
	}

	/** Returns the width of the Texture */
	public int width() {
		return this.width;
	}

	/** Returns the height of the Texture */
	public int height() {
		return this.height;
	}

	public void setType(String t) {
		type = t;
	}

	public String getType() {
		return type;
	}

	public void destroy() {
		glDisable(GL_TEXTURE_2D);
		glDeleteTextures(id);
	}
	
	public void bind(int textureUnit) {
		glActiveTexture(GL_TEXTURE0+textureUnit);
		glBindTexture(GL_TEXTURE_2D, this.id);
	}

	/** Returns the id of the Texture */
	public void setID(int id) {
		this.id = id;
	}

}
