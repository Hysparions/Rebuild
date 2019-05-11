package gui.font;

import org.joml.Vector2f;

public class Glyph{
	
	private char id;				/** ASCII ID of the character */
	private Vector2f position;		/** 2D position of the Text onScreen */
	private Vector2f surface;		/** 2D width and height of the Text onScreen */
	private Vector2f offset;		/** 2D offset of the character from the cursor of the Text onScreen */
	private float advance;			/** Distance to the next Character */
	
	/**
	 * Constructor of the Text Interface Component Object
	 * @param id
	 * @param position
	 * @param surface
	 * @param offset
	 * @param advance2
	 */
	public Glyph(char id, Vector2f position, Vector2f surface, Vector2f offset, float advance2) {
		//Initialization of the data
		this.id = id;
		this.position = position;
		this.surface = surface;
		this.offset = offset;
		this.advance = advance2;
	}

	/** Getter returning the position vector of the char in the texture*/
	public Vector2f position() { return this.position; }
	/** Getter returning the width and height of the vector*/
	public Vector2f surface() { return this.surface; }
	/** Getter returning the offset of the character form the cursor*/
	public Vector2f offset() { return this.offset; }
	/** Getter returning the advance of the character form the cursor*/
	public float advance() { return this.advance; }
	/** Getter returning the ASCII Code of the Character */
	public char toASCII() { return this.id; }
	
	
	/**Method returning the data of the glyph as a String */
	public String toString() { return new String("ID: " + (int)this.id + " Position : " + this.position.x + " " + this.position.y + " Surface : " + this.surface.x + " " + this.surface.y + " Offset : " + this.offset.x + " " + this.offset.y + " Advance : " + this.advance);}

}
