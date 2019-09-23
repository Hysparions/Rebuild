package engine.gui.text;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.Buffer;
import java.util.LinkedList;

import org.joml.Vector2f;

import engine.opengl.Shader;
import engine.utils.Color;
import engine.utils.FontException;

/**
 * UI Label is a OneLiner Text adapting the textBox to the content
 * Specify a min and max Font Size or a fixed one 
 * @author louis
 *
 */
public class UILabel extends UIText{
	
	protected LinkedList<UIWord> words;
	protected float length;
	
	// Render variables
	protected float fontSize;
	protected float edge;

	/**
	 * Constructor with fixed font size
	 * @param text to display
	 * @param font of the text
	 * @param fontSize of the text
	 * @throws FontException 
	 */
	public UILabel(String text, String font, Color color, float fontMin, float fontMax) {
		super(false, text, font, color, fontMin, fontMax);
		this.words = new LinkedList<UIWord>();
		this.box.keepAspect(true);
		// Compute words
		this.computeBuffer();
		// Send data to the gpu
		this.sendBuffer();

		//this.box.size(this.box.optimal().x(),this.box.optimal().y());
		
	}

	/**
	 * Constructor with fixed font size
	 * @param text to display
	 * @param font of the text
	 * @param fontSize of the text
	 */
	public UILabel(String text, String font, Color color, float fontSize) {
		this(text, font, color, fontSize, fontSize);
		
	}

	/**
	 * Compute the Text buffer by moving the words and managing their data
	 */
	@Override
	public void computeBuffer() {
		this.words.clear();
		String str[] = text.replaceAll("\\s+", " ").split(" ");
		float cursor = 0.0f;
		float lineTop = 0.0f;
		int offset = 0;
		UIWord word = null;
		for(int i = 0; i< str.length; i++) {
			word = new UIWord(str[i], new Vector2f(cursor, lineTop), this.font, this.buffer, offset, color);
			this.words.add(word);
			offset += UIWord.bytesAmount(str[i]);
			cursor += word.length();
			if(i<str.length-1) {
				cursor += font.glyph(' ').advance() - (font().right()+font().left())/2.0f;	
			}		
		}
		((Buffer)this.buffer).position(0);
		((Buffer)this.buffer).limit(offset);
		this.length = cursor;
		this.box.minimal(length*fontMin, font.baseLine()*fontMin);
		this.box.optimal(length*(fontMin+((fontMax-fontMin)/2.0f)), font.baseLine()*(fontMin+((fontMax-fontMin)/2.0f)));
		this.box.maximal(length*fontMax, font.baseLine()*fontMax);
	}

	@Override
	public void render(Shader shader, Vector2f windowPos) {
		// Computing font size
		this.fontSize = fontMin*(this.box.size().y()/this.box.minimal().y());
		// Computing model matrix
		this.model.identity();
		this.model.translate(this.box.position().x()+windowPos.x,this.box.position().y()+windowPos.y, 0.0f);
		this.model.scale(fontSize);
		// Computing edge and width
		this.edge = 0.3f - (0.25f * ((fontSize-UIFont.FONT_MIN_SIZE)/(UIFont.FONT_MAX_SIZE-UIFont.FONT_MIN_SIZE)));
		// Computing shaders uniforms
		shader.use();
		shader.setMat4Uni("model", model);
		shader.setFloatUni("edge", edge);
		this.font.texture.bind(0);
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		glBindVertexArray(0);
	}
	
	
	/**
	 * Returns the length of the UI Label in pixel with a font size given
	 * @param fontSize for the width
	 */
	public float width(float fontSize) {
		return this.length*fontSize;
	}
	
	public void setFontSize(float fontMin, float fontMax) {
		fontMin = fontMin<10?10:(fontMin>80?80:fontMin);
		fontMax = fontMax<10?10:(fontMax>80?80:fontMax);
		if(fontMax>fontMin) {fontMax = fontMin;}
		this.fontMin = fontMin;
		this.fontMax = fontMax;
		// Resize box preferences
		this.box.minimal(length*fontMin, font.baseLine()*fontMin);
		this.box.optimal(length*(fontMin+((fontMax-fontMin)/2.0f)), font.baseLine()*(fontMin+((fontMax-fontMin)/2.0f)));
		this.box.maximal(length*fontMax, font.baseLine()*fontMax);
	}

}
