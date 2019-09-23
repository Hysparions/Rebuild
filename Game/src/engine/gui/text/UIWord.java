package engine.gui.text;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.joml.Vector2f;

import engine.utils.Color;
import engine.utils.Utilities;

class UIWord {

	/** Vertex byte size = (2 texture float + 2 position + 1 float Color)*4 bytes */
	public static int VERTEX_BYTE_SIZE = 5 * Utilities.FLOATSIZE;
	
	/** String containing the word */
	private String word;
	/** Length of the word in Font Space*/
	private float length;
	/** Position of the Word in Text Space length */
	private Vector2f position;
	/** Color of the word */
	private Color color;
	/** Buffer containing the vertex data */
	private ByteBuffer buffer;
	/** Font */
	private UIFont font;

	/**
	 * Constructor of the word class
	 * @param word as String
	 * @param position
	 * @param font
	 * @param textBuffer
	 * @param offset
	 * @param color
	 */
	public UIWord(String word, Vector2f position, UIFont font, ByteBuffer textBuffer, int offset, Color color) {
		this.word = word;
		this.color = color;
		this.font = font;
		this.position  = position;
		((Buffer)textBuffer).position(offset);
		((Buffer)textBuffer).limit(offset+bytesAmount(this.word));
		this.buffer = textBuffer.slice();
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		this.length = wordLength(word, font);
		this.computeBuffer();
		
	}
	
	private void computeBuffer() {
		// Text has chars
		char[] text = word.toCharArray();
		float cursor = position.x;
		float lineTop = position.y;
		UIGlyph current = null;
		UIGlyph previous = null;
		float kerning = 0.0f;
		float padScaleW = font.baseSize()/font.textureWidth()/2;
		float padScaleH = font.baseSize()/font.textureHeigth()/2;
		((Buffer)buffer).position(0);
		((Buffer)buffer).limit(bytesAmount(this.word));
		for (int i = 0; i < word.length(); i++) {

			// Getting the correct Glyph data
			current = font.glyph(text[i]);
			
			// Getting kerning x Offset
			if(previous != null) {
				kerning = current.kerningWith(previous);
			}

			// First Vertex TOP LEFT
			this.buffer.putFloat(current.texX() -(font.left()*padScaleW));
			this.buffer.putFloat(current.texY()+current.texH()+(font.top()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() - font.left());
			this.buffer.putFloat(lineTop + current.sizeH()+ current.bearY()  + font.top());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			// Second Vertex TOP RIGHT
			this.buffer.putFloat(current.texX()+current.texW()+(font.right()*padScaleW));
			this.buffer.putFloat(current.texY()+current.texH()+(font.top()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() + current.sizeW() + font.right());
			this.buffer.putFloat(lineTop + current.sizeH()+ current.bearY() + font.top());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			// Second Vertex BOTTOM LEFT
			this.buffer.putFloat(current.texX()-(font.left()*padScaleW));
			this.buffer.putFloat(current.texY()-(font.bottom()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() - font.left());
			this.buffer.putFloat(lineTop + current.bearY()  -font.bottom());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			

			// Second Vertex BOTTOM LEFT
			this.buffer.putFloat(current.texX()-(font.left()*padScaleW));
			this.buffer.putFloat(current.texY()-(font.bottom()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() - font.left());
			this.buffer.putFloat(lineTop + current.bearY()  -font.bottom());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			// Second Vertex TOP RIGHT
			this.buffer.putFloat(current.texX()+current.texW()+(font.right()*padScaleW));
			this.buffer.putFloat(current.texY()+current.texH()+(font.top()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() + current.sizeW() + font.right());
			this.buffer.putFloat(lineTop + current.bearY() + current.sizeH() + font.top());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			// Second Vertex BOTTOM RIGHT
			this.buffer.putFloat(current.texX()+current.texW()+(font.right()*padScaleW));
			this.buffer.putFloat(current.texY()-(font.bottom()*padScaleH));
			this.buffer.putFloat(cursor + kerning + current.bearX() + current.sizeW() + font.right());
			this.buffer.putFloat(lineTop + current.bearY()  -font.bottom());
			this.buffer.put(color.r()).put(color.g()).put(color.b()).put(color.a());
			
			previous = current;
			cursor += current.advance() + kerning - (font.right() + font.left())/2.0f;
		}
		((Buffer)this.buffer).flip();
		/**
		for(int i = 0;  i< word.length()*6; i++) {
			System.out.println("Tex " + buffer.getFloat() + " " + buffer.getFloat() + " "+ buffer.getFloat() + " "+ buffer.getFloat() );
			buffer.getFloat();
		}*/
	}

	/** Setter for Position */
	public void setPosition(int x, int y) {
		position.set(x, y);
	}

	/** Simple getter for the word value in string */
	public String text() {
		return word;
	}

	public static int bytesAmount(String word) {
		return word.length()*6*VERTEX_BYTE_SIZE;
	}
	
	/** Getter for the buffer */
	public ByteBuffer buffer() {
		return buffer;
	}
	
	/** @return Returns the font space length of the word*/
	public float length() {
		return this.length;
	}

	/**
	 * Get the length in font Space of the string given in parameters 
	 * @return
	 */
	public static float wordLength(String word, UIFont font) {
		// Calculating the length of the Word
		char[] text = word.toCharArray();
		UIGlyph current = null;
		UIGlyph previous = null;
		float kerning = 0.0f;
		float length = 0.0f;
		float padding = (font.right() + font.left())/2.0f;
		
		for (int i = 0; i < word.length(); i++) {
			current = font.glyph(text[i]);
			if(previous != null) {
				kerning = current.kerningWith(previous);
			}
			length += current.advance()+kerning-padding;
			previous = current;
		}

		return length;
	}

}
