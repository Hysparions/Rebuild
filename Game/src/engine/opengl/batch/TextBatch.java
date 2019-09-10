package engine.opengl.batch;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL30.*;

import engine.gui.text.Font;
import engine.gui.text.Word;
import engine.opengl.models.stanford.PolyVertex;
import engine.utils.Utilities;

public class TextBatch {

	/** Capacity of the buffer */
	private long capacity;
	/** Limit of the buffer */
	private int limit;

	/** Vertex array buffer */
	private int VAO;
	/** Vertex Buffer object */
	private int VBO;
	/** Font used by the batch */
	private Font font;

	/** List containing the reference of all the Words that the textBatchContains */
	private List<Word> words;

	/**
	 * Text Batch constructor taking the default capacity in parameter
	 * 
	 * @param capacity	the capacity of the batch
	 * @param font	The font used by the text batch to Render the text
	 */
	public TextBatch(int capacity, Font font) {
		this.capacity = capacity;
		this.limit = 0;
		this.font = font;
		this.words = new ArrayList<>();

		// Creating a new vertex array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);

		// Creating a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, capacity * Utilities.FLOATSIZE, GL_DYNAMIC_DRAW);

		// Setting the vertex Array attribute pointers
		// Vertex Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 7 * Utilities.FLOATSIZE, 0);
		// Char Color
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 7 * Utilities.FLOATSIZE, 2 * Utilities.FLOATSIZE);
		// Texture Coordinates
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 7 * Utilities.FLOATSIZE, 5 * Utilities.FLOATSIZE);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	public void add(Word word) {
		// Adding the new Element to the buffer
		words.add(word);
		if ((limit + word.text().length() * 6) * Word.VERTEX_SIZE * Utilities.FLOATSIZE <= capacity) {
			// The model offset in the batch is now set to the end of the buffer
			word.offset = ((long) limit) * PolyVertex.SIZE * Utilities.FLOATSIZE;
			limit += word.text().length() * 6;
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferSubData(GL_ARRAY_BUFFER, word.offset, word.buffer());
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		} else {
			System.err.println("Not Enough Space");
		}
	}

	public void update(int i) {
		Word word = words.get(i);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferSubData(GL_ARRAY_BUFFER, word.offset, word.buffer());
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	public void render() {
		glBindVertexArray(VAO);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, font.textureID());
		glDrawArrays(GL_TRIANGLES, 0, limit);
		glBindVertexArray(0);
	}

	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
}
