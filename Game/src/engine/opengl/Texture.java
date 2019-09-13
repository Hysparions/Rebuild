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
import static org.lwjgl.opengl.GL30.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

public class Texture {

	private String filePath;
	private int id;
	private int width;
	private int height;
	private int channels;
	// Used by Models to determine if it's a diffuse or a Specular texture
	private String type;

	public Texture() {
		filePath = "";
		id = 0;
		width = 0;
		height = 0;
		channels = 0;
		type = "";
	}

	public static Texture createTexture(String path) {
		try {

			Texture tex = new Texture();
			// Storing the path could be useful to know if the texture have already been
			// stored
			tex.filePath = path;
			File f = new File(tex.filePath);
			if (!f.exists()) {
				throw new IOException("Failed to load " + tex.filePath);
			}

			/// Buffers to get image info;
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			IntBuffer c = BufferUtils.createIntBuffer(1);

			// Loading Data Using STB
			STBImage.stbi_set_flip_vertically_on_load(true);
			ByteBuffer image = STBImage.stbi_load(f.toString(), w, h, c, 4);
			// Verification that the data has been successfully loaded
			if (image == null) {

				throw new IOException(STBImage.stbi_failure_reason());
			}

			// Get BUffer Values
			tex.width = w.get();
			tex.height = h.get();
			tex.channels = c.get();

			// Generate the texture to the address defined in the id
			tex.id = glGenTextures();
			// System.out.println(tex.filePath + tex.id);
			// Bind it as the current Texture
			glBindTexture(GL_TEXTURE_2D, tex.id);

			// Setup wrap mode
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
			// Set the Texture Scaling filters
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 7);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tex.width, tex.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			// Generate MipMap automatically
			glGenerateMipmap(GL_TEXTURE_2D);
			// Unbind Texture
			glBindTexture(GL_TEXTURE_2D, 0);
			// Finally Free the Image
			STBImage.stbi_image_free(image);

			return tex;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
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
