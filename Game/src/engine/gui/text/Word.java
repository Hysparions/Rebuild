package engine.gui.text;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Word {

	public static int VERTEX_SIZE = 7;

	/** String containing the word */
	private String word;
	/** Length of the word in pixel */
	private int length;
	/** Scale of the word */
	private float scale;
	/** Position of the Word in Screen Space length */
	private Vector2f position;
	/** Position of the Word in Screen Space length */
	private Vector3f color;
	/** Buffer containing the vertex data */
	private FloatBuffer vertices;
	/** Offset of the word in the Text Buffer */
	public long offset;
	/** Font */
	private Font font;

	public Word(String word, Vector2f position, Font font, Vector3f color, float policeSize) {
		this.word = word;
		this.color = color;
		this.position = position;
		this.font = font;
		this.vertices = BufferUtils.createFloatBuffer(word.length() * 6 * VERTEX_SIZE);
		this.length = 0;
		this.offset = 0;
		this.scale = policeSize;
		// Calculating the length of the Word
		char[] text = word.toCharArray();
		for (int i = 0; i < word.length(); i++) {
			Glyph glyph = font.glyph(text[i]);
			length += glyph.advance();
		}
		length *= scale;
		updateBuffer();
	}

	private void updateBuffer() {
		char[] text = word.toCharArray();
		float cursor = position.x;
		float x, y;
		for (int i = 0; i < word.length(); i++) {

			// Getting the correct Glyph data
			Glyph glyph = font.glyph(text[i]);
			// Then creating the quad as it Should be

			// First Vertex
			x = cursor + glyph.offset().x * scale;
			y = (-glyph.offset().y - (glyph.surface().y * font.textureHeigth())) * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x);
			vertices.put(glyph.position().y + glyph.surface().y);

			// Second Vertex
			x = cursor + (glyph.offset().x + (glyph.surface().x * font.textureWidth())) * scale;
			y = -glyph.offset().y * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x + glyph.surface().x);
			vertices.put(glyph.position().y);
			// Third Vertex
			x = cursor + glyph.offset().x * scale;
			y = -glyph.offset().y * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x);
			vertices.put(glyph.position().y);

			// Forth Vertex
			x = cursor + (glyph.offset().x + (glyph.surface().x * font.textureWidth())) * scale;
			y = (-glyph.offset().y - (glyph.surface().y * font.textureHeigth())) * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x + glyph.surface().x);
			vertices.put(glyph.position().y + glyph.surface().y);

			// Fifth Vertex
			x = cursor + (glyph.offset().x + (glyph.surface().x * font.textureWidth())) * scale;
			y = -glyph.offset().y * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x + glyph.surface().x);
			vertices.put(glyph.position().y);
			// Sixth Vertex
			x = cursor + glyph.offset().x * scale;
			y = (-glyph.offset().y - (glyph.surface().y * font.textureHeigth())) * scale;
			vertices.put(x);
			vertices.put(y + position.y);
			vertices.put(color.x);
			vertices.put(color.y);
			vertices.put(color.z);
			vertices.put(glyph.position().x);
			vertices.put(glyph.position().y + glyph.surface().y);
			cursor += glyph.advance() * scale;
		}
		vertices.flip();
	}

	/** Setter for Position */
	public void setPosition(int x, int y) {
		position.set(x, y);
		updateBuffer();
	}

	/** Simple getter for the word value in string */
	public String text() {
		return word;
	}

	/** Getter for the buffer */
	public FloatBuffer buffer() {
		return vertices;
	}

	/** Getter for the length */
	public int length() {
		return length;
	}

}
