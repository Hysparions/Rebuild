package engine.gui.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import engine.opengl.Texture;
import engine.utils.FontException;
import engine.utils.TextureException;


/**
 * Class holding all the data associated to a font, with all the associated
 * glyph and texture atlas. Fonts are always stored with a font size of 1.0f
 * it is then scale to the adequate police size in UIText and UIWord classes
 * @author louis
 *
 */
public class UIFont {
	
	/** Font max Size */
	public static float FONT_MIN_SIZE = 12;
	/** Font max Size */
	public static float FONT_MAX_SIZE = 80;
	
	/** Base Size of the Font in Font Space */
	private float baseSize;
	/** Line Height of the Font in Font Space */
	private float lineHeight;
	/** Line Height of the Font in Font Space */
	private float baseLine;
	/** Padding top to add to the texture and glyph in Font Space */
	private float paddingTop;
	/** Padding right to add to the texture and glyph in  Font Space */
	private float paddingRight;
	/** Padding bottom to add to the texture and glyph in Font Space*/
	private float paddingBottom;
	/** Padding left to add to the texture and glyph in  Font Space */
	private float paddingLeft;
	/** Type and Name of the font */
	private String name;
	/** List of the characters of the font */
	private Map<Character, UIGlyph> glyphs;
	/** Texture Atlas of the font */
	Texture texture;
	
	/** Static font map accessible static */
	public static HashMap<String, UIFont> fontMap = new HashMap<>();
	
	/**
	 * Constructor for the UI Font Class
	 * @param name - File Path and name of the font
	 * @throws FontException if the file doesn't exist
	 */
	public UIFont(String name) throws FontException {

		// Loading the font Texture
		try {
			texture = new Texture("/fonts/" + name + ".png");
		} catch (TextureException e1) {
			e1.printStackTrace();
		}
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
			throw new FontException(name);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new FontException(name);
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

			if(fields[i].startsWith("size")) {
				this.baseSize = (float)Integer.parseInt(fields[i].replace("size=", ""));
			}

			if(fields[i].startsWith("padding")) {
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
			if(fields[i].startsWith("lineHeight")) {
				this.lineHeight = (float)((float)Integer.parseInt(fields[i].replace("lineHeight=", ""))/this.baseSize);
			}			
			if(fields[i].startsWith("base")) {
				this.baseLine = (float)((float)Integer.parseInt(fields[i].replace("base=", ""))/this.baseSize);
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
					// Texture is x right y down
					texX = (float)(((float)Integer.parseInt(fields[2])-paddingLeft)/(float)texture.width());
					texY = (float)(((float)Integer.parseInt(fields[3])-paddingTop)/(float)texture.height());
					texW = (float)(((float)Integer.parseInt(fields[4])+paddingRight+paddingLeft)/(float)texture.width());
					texH = (float)(((float)Integer.parseInt(fields[5])+paddingBottom+paddingTop)/(float)texture.height());
					sizeW = (float)((float)Integer.parseInt(fields[4])/baseSize);
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
	
	/**
	 * Create a font and add it to the font map if it exists
	 * If the font already exists, then it is returned
	 * If the file doesn't exists, return null
	 * @param name of the font (filename)
	 * @return the font after adding it to the map
	 */
	public static UIFont createFont(String name) {
		UIFont font = fontMap.get("name");
		if(font != null) {
			return font;
		}else {
			try {
				font = new UIFont(name);
				fontMap.put(font.name(), font);
				return font;
			}catch(FontException f) {
				f.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Remove a font from the fontMap
	 * @param name of the font
	 */
	public static void removeFont(String name) {
		UIFont font = fontMap.remove(name);
		if(font != null) {
			font.destroy();
		}
	}
	
	/** destroy all fonts */
	public static void destroyAll() {
		fontMap.forEach((name, font) ->{
			font.destroy();
		});
		fontMap.clear();
	}
	
	/**
	 * Getter for the font 
	 * @param name of the font
	 * @return the font or null
	 */
	public static UIFont get(String name) {
		return fontMap.get(name);
	}
		
	/** @return the baseSize */
	public float baseSize() {return baseSize;}

	/** @return the lineHeight */
	public float lineHeight() {return lineHeight;}

	/** @return the baseLine */
	public float baseLine() {return baseLine;}

	/** @return the paddingTop */
	public float top() {return paddingTop;	}

	/** @return the paddingRight */
	public float right() {return paddingRight;}

	/** @return the paddingBottom */
	public float bottom() {	return paddingBottom;}

	/**@return the paddingLeft*/
	public float left() {return paddingLeft;}

	/** Methods that destroy the texture at the end of the execution */
	public void destroy() {
		texture.destroy();
	}

	/** returns the fontName of the font object */
	public String name() {
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
		System.out.println("Font : " + name + " baseSize=" + baseSize + " LineHeight=" + lineHeight + " base=" + baseLine
				+ " Top=" + paddingTop + " Bottom=" + paddingBottom + " Right=" + paddingRight + " Left=" + paddingLeft);
		glyphs.forEach((id,glyph)->{
			System.out.println(glyph);
		});
	}
}
