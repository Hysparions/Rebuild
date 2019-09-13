package engine.gui.renderable.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector2f;

import engine.opengl.Texture;


/**
 * Class holding all the data associated to a font, with all the associated
 * glyph and texture atlas. Fonts are always stored with a font size of 1.0f
 * it is then scale to the adequate police size in UIText and UIWord classes
 * @author louis
 *
 */
public class UIFont {

	/** Base Size of the Font in 0 to 1 scale */
	private float baseSize;
	/** Line Height of the Font in 0 to 1 scale */
	private float lineHeight;
	/** Padding top to add to the texture and glyph in 0 to 1 scale */
	private float paddingTop;
	/** Padding right to add to the texture and glyph in 0 to 1 scale */
	private float paddingRight;
	/** Padding bottom to add to the texture and glyph in 0 to 1 scale */
	private float paddingBottom;
	/** Padding left to add to the texture and glyph in 0 to 1 scale */
	private float paddingLeft;
	/** Type and Name of the font */
	private String name;
	/** List of the characters of the font */
	private Map<Character, UIGlyph> glyphs;
	/** Texture Atlas of the font */
	Texture texture;
	
	/**
	 * Constructor for the UI Font Class
	 * @param name - File Path and name of the font
	 */
	public UIFont(String name) {

		// Loading the font Texture
		texture = Texture.createTexture("res/fonts/" + name + ".png");
		// Loading the Glyphs data
		this.name = name;
		// Creating glyph hashMap
		this.glyphs = new HashMap<>();

		// Launch a buffer Reader to read the font File
		BufferedReader reader = null;
		try {
			// Reader as input for the file
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/fonts/" + name + ".fnt")));
			processFontParameters(reader);
			processGlyphs(reader);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This function query the font global parameters
	 * @param reader buffer
	 * @throws IOException if readers failed to read
	 */
	private void processFontParameters(BufferedReader reader) throws IOException{
		String line = reader.readLine().trim();
		
		String fields[] = line.split(" ");
		for(int i = 0; i < fields.length ; i++) {

			if(fields[i].contains("size")) {
				this.baseSize = (float)Integer.parseInt(fields[i].replace("size=", ""));
			}

			if(fields[i].contains("padding")) {
				String padding[] = fields[i].replace("padding=", "").split(",");
				this.paddingTop = (float)((float)Integer.parseInt(padding[0])/this.baseSize);
				this.paddingRight = (float)((float)Integer.parseInt(padding[1])/this.baseSize);
				this.paddingBottom = (float)((float)Integer.parseInt(padding[2])/this.baseSize);
				this.paddingLeft =(float)((float)Integer.parseInt(padding[3])/this.baseSize);
			}
			
		}
		line = reader.readLine().trim();
		fields = line.split(" ");
		for(int i = 0; i < fields.length ; i++) {
			if(fields[i].contains("lineHeight")) {
				this.lineHeight = (float)((float)Integer.parseInt(fields[i].replace("lineHeight=", ""))/this.baseSize);
			}			
		}
	}
	
	/**
	 * Process the glyphs and kernings of the font char
	 * @param reader to read values
	 * @throws IOException if there's a bug
	 */
	public void processGlyphs(BufferedReader reader) throws IOException{
		String line = reader.readLine().replaceAll("\\s+", " ");
		String fields[];
		
		char id;
		float texX;
		float texY;
		float texW;
		float texH;
		float sizeW;
		float sizeH;
		float bearX;
		float bearY;
		float advance;
		
		char first;
		char second;
		float amount;
		
		while(line != null) {
			line = line.replaceAll("\\s+", " ");
			fields = line.split(" ");
			if(fields.length > 0) {
				// Case glyph
				if(fields[0].equals("char")) {
					fields[1] = fields[1].replace("id=", "");
					fields[2] = fields[2].replace("x=", "");
					fields[3] = fields[3].replace("y=", "");
					fields[4] = fields[4].replace("width=", "");
					fields[5] = fields[5].replace("height=", "");
					fields[6] = fields[6].replace("xoffset=", "");
					fields[7] = fields[7].replace("yoffset=", "");
					fields[8] = fields[8].replace("xadvance=", "");

					id = (char) Integer.parseInt(fields[1]);
					texX = (float)((float)Integer.parseInt(fields[2])/(float)texture.width());
					texY = (float)((float)Integer.parseInt(fields[3])/(float)texture.height());
					texW = (float)((float)Integer.parseInt(fields[4])/(float)texture.width());
					sizeW = (float)((float)Integer.parseInt(fields[4])/baseSize);
					texH = (float)((float)Integer.parseInt(fields[5])/(float)texture.height());
					sizeH = (float)((float)Integer.parseInt(fields[5])/baseSize);
					bearX = (float)((float)Integer.parseInt(fields[6])/baseSize);
					bearY = (float)((float)Integer.parseInt(fields[7])/baseSize);
					advance = (float)((float)Integer.parseInt(fields[8])/baseSize);
					
					this.glyphs.put(id, new UIGlyph(id, texX, texY, texW, texH, sizeW, sizeH, bearX, bearY, advance));
				}
				// Case kerning
				else if(fields[0].equals("kerning")) {
					fields[1] = fields[1].replace("first=", "");
					fields[2] = fields[2].replace("second=", "");
					fields[3] = fields[3].replace("amount=", "");
					
					first = (char) Integer.parseInt(fields[1]);
					second = (char) Integer.parseInt(fields[2]);
					amount = (float)((float)Integer.parseInt(fields[3])/baseSize);
					
					UIKerning kerning = new UIKerning(first, second, amount);
					UIGlyph glyph = this.glyphs.get(kerning.second());
					if(glyph != null) {
						glyph.addKerning(kerning);
					}
				}
				line = reader.readLine();
			}
		}
	}
	

	/** Methods that destroy the texture at the end of the execution */
	public void destroy() {
		texture.destroy();
	}

	/** returns the fontName of the font object */
	public String fontName() {
		return name;
	}

	/** returns the id of the texture */
	public int textureID() {
		return texture.ID();
	}

	/** Method to access to a Glyph data using the char id */
	public UIGlyph glyph(char id) {
		return glyphs.get(id);
	}

	public float textureWidth() {
		return texture.width();
	}

	public float textureHeigth() {
		return texture.height();
	}
	
	public void display() {
		System.out.println("Font : " + name + " baseSize=" + baseSize + " LineHeight=" + lineHeight
				+ " Top=" + paddingTop + " Bottom=" + paddingBottom + " Right=" + paddingRight + " Left=" + paddingLeft);
	}
}
