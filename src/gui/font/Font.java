package gui.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import openGL.Texture;

public class Font {

	/** Padding Right by default to remove from the advance*/
	private static int PADDING = 12;
	/** Type and Name of the font */
	private String fontType;	
	/** List of the characters of the font */
	private Map<Character , Glyph> glyphs;
	/** Texture Atlas of the font */
	Texture texture;
	
	
	
	/**
	 * Constructor for the Font taking the name of the font as parameter
	 * @param fontType
	 * 	- File Path and name of the font
	 */
	public Font(String fontType) {
		
		//Loading the font Texture
		texture = Texture.createTexture("res/fonts/" + fontType + ".png");

		//Loading the Glyphs data
		this.fontType = fontType;
		this.glyphs = new HashMap<>();
		
		//Launch a buffer Reader to read the font File
		BufferedReader reader = null;		
		try {
			//Reader as input for the file
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/fonts/" + fontType + ".fnt")));
			//Buffer Values
			int character;
			String word = new String("");
			Vector2f position = new Vector2f();
			Vector2f surface = new Vector2f();
			Vector2f offset = new Vector2f();
			float advance;
			char id;
			//Looping and reading
			while ((character = reader.read()) != -1) {
				word += String.valueOf(Character.toChars(character));
				if(word.contains("char ")) {
					id = (char)getNextElement(reader, word);
					position.x = getNextElement(reader, word)/texture.width();
					position.y = getNextElement(reader, word)/texture.height();
					surface.x = getNextElement(reader, word)/texture.width();
					surface.y = getNextElement(reader, word)/texture.height();
					offset.x = getNextElement(reader, word);
					offset.y = getNextElement(reader, word);
					advance = getNextElement(reader, word)-PADDING;
					Glyph glyph = new Glyph(id, new Vector2f(position), new Vector2f(surface), new Vector2f(offset), advance);

					glyphs.put(id, glyph);
					word="";
				}else if(word.charAt(word.length()-1) == ' ') { word="";	}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Method Reading until it founds the string in parameter 
	 * @param reader 
	 * 	- the buffer reader in which we read the data from the file
	 * @param word
	 * 	- A string used as buffer
	 * */
	private float getNextElement(BufferedReader reader, String word) {
		word = "";
		int character;
		try {
			while ((character = reader.read()) != '=') {}
			while((character = reader.read()) != ' ') {
				
			word += String.valueOf(Character.toChars(character));
			}
		}catch(IOException e) {e.printStackTrace();}
		return Float.parseFloat(word);
	}
	
	/** Methods that destroy the texture at the end of the execution */
	public void destroy() {
		texture.destroy();
	}
	
	/** returns the fontType of the font object */
	public String fontType() { return fontType; }
	/** returns the id of the texture*/
	public int textureID() { return texture.ID();}
	/** Method to access to a Glyph data using the char id*/
	public Glyph glyph(char id) {return glyphs.get(id);}
	
	public float textureWidth() {return texture.width();}	
	public float textureHeigth() {return texture.height();}	
}
