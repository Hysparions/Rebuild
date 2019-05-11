package gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import gui.font.Font;
import gui.font.Word;


public class GUIText extends GUIComponent{

	private Vector3f color;
	private String text;
	private Font font;
	private Word word;
	
	public GUIText(Vector2f position, Vector2f surface, String text, Vector3f color, Font font) {
		super(GUIType.TEXT, position, surface);
		this.color = color;
		this.text = text;
		this.word = null;
	}

	@Override
	public void reSize() {
		
		
	}
	
	public Vector3f color() { return this.color; }
	public String text() { return this.text; }
	public Font font() { return this.font; }
	public Word word() { return this.word; }

}
