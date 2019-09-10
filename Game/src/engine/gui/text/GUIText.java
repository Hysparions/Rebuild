package engine.gui.text;

import engine.gui.shape.GLPrimitive;
import engine.utils.Color;

public class GUIText extends GLPrimitive {

	private Color color;
	private String text;
	private Font font;
	private int fontSize;
	private Word word;

	public GUIText( String text, Color color, Font font, int fontSize) {
		super();
		this.color = color;
		this.text = text;
		this.fontSize = fontSize;
		this.word = null;
	}

	public Color color() {
		return this.color;
	}

	public String text() {
		return this.text;
	}

	public Font font() {
		return this.font;
	}

	public Word word() {
		return this.word;
	}

	@Override
	public void build(float x, float y, float w, float h) {
		
	}

	@Override
	public void updateBuffer() {
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

}
