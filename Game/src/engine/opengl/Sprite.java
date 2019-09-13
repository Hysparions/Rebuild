package engine.opengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite {

	private int VAO;

	private Vector2f position;
	private Vector4f rectangle;
	private Texture texture;
	private Shader shader;

	private float scale;
	private Vector4f color;

	private Matrix4f model;
	private Matrix4f spriteProjection;

	// Basic Constructor for the Sprite class
	public Sprite() {
		VAO = 0;
		position = new Vector2f(0.0f, 0.0f);
		rectangle = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
		texture = null;
		shader = null;
		scale = 1.0f;
		color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		model = new Matrix4f();
		spriteProjection = new Matrix4f();
		init();
	}

	/// Overloaded Constructor using Texture Width and height
	public Sprite(Vector2f pos, Texture tex, Shader s) {
		VAO = 0;
		position = pos;
		rectangle = new Vector4f(0.0f, 0.0f, tex.width(), tex.height());
		texture = tex;
		shader = s;
		scale = 1.0f;
		color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		model = new Matrix4f();
		spriteProjection = new Matrix4f();
		init();
	}

	/// Overloaded Constructor using Custom Rectangle Position
	public Sprite(Vector2f pos, Vector4f rect, Texture tex, Shader s) {
		VAO = 0;
		position = pos;
		rectangle = rect;
		texture = tex;
		shader = s;
		scale = 1.0f;
		color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		model = new Matrix4f();
		spriteProjection = new Matrix4f();
		init();
	}

	// Initialize the vertex data and generate a VAO/VBO
	private void init() {

		// Vertex/TexPos Data
		float[] vertices = new float[] { 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,

				0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f };

		// Generating a New VAO
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);

		// Generating a new VBO
		int VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		// Bind Vertex Attributes
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * (Float.SIZE / Byte.SIZE), 0);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(VBO);

	}

	public void destroy() {
		glBindVertexArray(0);
		glDeleteVertexArrays(VAO);
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void setScale(float s) {
		scale = s;
	}

	public void setRectangle(float x, float y, float w, float h) {
		rectangle.x = x;
		rectangle.y = y;
		rectangle.z = w;
		rectangle.w = h;
	}

	public void setColor(float r, float g, float b, float a) {
		color.x = r;
		color.y = g;
		color.z = b;
		color.w = a;
	}

	public void draw() {
		shader.use();

		model.identity();
		model.translate(position.x - ((rectangle.z * scale) / 2.0f), position.y - ((rectangle.w * scale) / 2.0f), 0.0f);
		model.scale(rectangle.z * scale, rectangle.w * scale, 1.0f);
		shader.setMat4Uni("model", model);

		spriteProjection.identity();
		spriteProjection.translate(rectangle.x / texture.width(), rectangle.y / texture.height(), 0.0f);
		spriteProjection.scale(rectangle.z / texture.width(), rectangle.w / texture.height(), 1.0f);
		shader.setMat4Uni("spriteProjection", spriteProjection);

		shader.setIntUni("image", 0);

		shader.setVec4Uni("color", color);
		glActiveTexture(GL_TEXTURE0);

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texture.ID());
		// render container
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, 6);

	}

}
