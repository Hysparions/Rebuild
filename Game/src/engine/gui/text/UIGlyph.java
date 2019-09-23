package engine.gui.text;

import java.util.LinkedList;

public class UIGlyph {

	/** ASCII ID of the character */
	private char id;
	/** Texture Position X*/
	private float texX;
	/** Texture Position Y*/
	private float texY;
	/** Texture Width*/
	private float texW;
	/** Texture Height*/
	private float texH;
	/** Size of the char */
	private float sizeW;
	/** Size of the char */
	private float sizeH;
	/** Char X Offset  */
	private float bearX;
	/** Char Y Offset */
	private float bearY;
	/** Advance */
	private float advance;
	/** Linked list of UI Kerning if some */
	private LinkedList<UIKerning> kernings;
	

	/**
	 * UI Glyph Constructor
	 * @param id of the character
	 * @param texX position
	 * @param texY position
	 * @param texW size 
	 * @param texH size 
	 * @param sizeX in the 0 to 1 police size scale
	 * @param sizeY in the 0 to 1 police size scale
	 * @param BearX in the 0 to 1 police size scale
	 * @param BearY in the 0 to 1 police size scale
	 * @param advance
	 */
	public UIGlyph(char id, float texX, float texY, float texW, float texH, float sizeX, float sizeY, float bearX, float bearY, float advance) {
		// Initialization of the data
		this.id = id;
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
		this.sizeW = sizeX;
		this.sizeH = sizeY;
		this.bearX = bearX;
		this.bearY = bearY;
		this.advance = advance;
		this.kernings = null;
	}

	/**@return the id*/
	public char toChar() {return id;}

	/** @return the texX */
	public float texX() {return texX;}

	/** @return the texY*/
	public float texY() {return texY;}

	/** @return the texW*/
	public float texW() {return texW;}

	/** @return the texH*/
	public float texH() {return texH;}

	/** @return the sizeX*/
	public float sizeW() {return sizeW;}

	/**@return the sizeY*/
	public float sizeH() {return sizeH;}

	/**@return the bearX */
	public float bearX() {return bearX;}

	/**@return the bearY*/
	public float bearY() {return bearY;}

	/** @return the advance*/
	public float advance() {return advance;}
	
	/**
	 * Adds a new kerning to the glyph
	 * @param kerning
	 */
	public void addKerning(UIKerning kerning) {
		if(kerning != null) {
			if(this.kernings == null) {
				this.kernings = new LinkedList<UIKerning>();
			}
			this.kernings.add(kerning);
		}
	}
	
	/**
	 * Get the amount of kerning with the previous char given in parameter if any
	 * @param id of the char
	 * @return kerning amount
	 */
	public float kerningWith(UIGlyph previous) {
		if(this.kernings != null) {
			for(UIKerning k : this.kernings) {
				if(k.first() == previous.id) {
					return k.amount();
				}
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return new String("ID: " + this.id + " Texture x=" + this.texX + " y=" + this.texY + " w=" + this.texW + " h=" + this.texH
				+ " Size w=" + this.sizeW + " h=" + this.sizeH + " Bearing x=" + this.bearX + " y=" + this.bearY +  " Advance=" + this.advance);
	}

}
