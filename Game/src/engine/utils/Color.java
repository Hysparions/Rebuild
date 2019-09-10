package engine.utils;

/**
 * Class used to manage easily color using bytes to save data instead of OpenGL float
 * @author louis
 *
 */
public class Color {

	/** Red color component */
	private byte red;
	/** Green color component */
	private byte green;
	/** Blue color component */
	private byte blue;
	/** Alpha color component */
	private byte alpha;
	
	/**
	 * Color constructor with values clamped between -128 to +127
	 * @param red color component (clamped between 0 and 255)
	 * @param green color component (clamped between 0 and 255) 
	 * @param blue color component (clamped between 0 and 255)
	 * @param alpha color component (clamped between 0 and 255)
	 */
	public Color(int red, int green, int blue, int alpha) {
		this.red = (byte)((red<0?0:(red>255?255:red))-128);
		this.green = (byte)((green<0?0:(green>255?255:green))-128);
		this.blue = (byte)((blue<0?0:(blue>255?255:blue))-128);
		this.alpha = (byte)((alpha<0?0:(alpha>255?255:alpha))-128);
	}
	
	/**
	 * Color constructor with values clamped between -128 to +127
	 * @param red color component (clamped between 0.0f and 1.0f)
	 * @param green color component (clamped between 0.0f and 1.0f)
	 * @param blue color component (clamped between 0.0f and 1.0f)
	 * @param alpha color component (clamped between 0.0f and 1.0f)
	 */
	public Color(float red, float green, float blue, float alpha) {
		this.red = (byte)(((red<0.0f?0.0f:(red>1.0f?1.0f:red))*255)-128);
		this.green = (byte)(((green<0.0f?0.0f:(green>1.0f?1.0f:green))*255)-128);
		this.blue = (byte)(((blue<0.0f?0.0f:(blue>1.0f?1.0f:blue))*255)-128);
		this.alpha = (byte)(((alpha<0.0f?0.0f:(alpha>1.0f?1.0f:alpha))*255)-128);
	}
	
	/**
	 * Default constructor
	 */
	public Color() {
		this.red = -128;
		this.green = -128;
		this.blue = -128;
		this.alpha = -128;
	}

	/**
	 * Getter for the red byte value
	 * @return the red color component
	 */
	public byte r() {
		return this.red;
	}
	
	/**
	 * Getter for the green byte value
	 * @return the green color component
	 */
	public byte g() {
		return this.green;
	}
	
	/**
	 * Getter for the blue byte value
	 * @return the blue color component
	 */
	public byte b() {
		return this.blue;
	}
	
	/**
	 * Getter for the alpha byte value
	 * @return the alpha color component
	 */
	public byte a() {
		return this.alpha;
	}

	@Override
	public String toString() {
		return "Color : red=" + Integer.toString((int)this.red+128) 
				+ "  green=" + Integer.toString((int)this.green+128)
				+ "  blue=" + Integer.toString((int)this.blue+128)
				+ "  alpha=" + Integer.toString((int)this.alpha+128);
	}
	
	/**
	 * Set the red component
	 * @param red color component
	 */
	public void r(float red) {
		this.red = (byte)(((red<0.0f?0.0f:(red>1.0f?1.0f:red))*255)-128);
	}
	
	/**
	 * Set the red component
	 * @param red color component
	 */
	public void g(float green) {
		this.green = (byte)(((green<0.0f?0.0f:(green>1.0f?1.0f:green))*255)-128);
	}
	
	/**
	 * Set the blue component
	 * @param blue color component
	 */
	public void b(float blue) {
		this.blue = (byte)(((blue<0.0f?0.0f:(blue>1.0f?1.0f:blue))*255)-128);
	}
	
	/**
	 * Set the alpha component
	 * @param alpha color component
	 */
	public void a(float alpha) {
		this.alpha = (byte)(((alpha<0.0f?0.0f:(alpha>1.0f?1.0f:alpha))*255)-128);
	}
	
	/**
	 * Set the red component
	 * @param red color component
	 */
	public void r(int red) {
		this.red = (byte)((red<0?0:(red>255?255:red))-128);
	}
	
	/**
	 * Set the green component
	 * @param green color component
	 */
	public void g(int green) {
		this.green = (byte)((green<0?0:(green>255?255:green))-128);
	}
	
	/**
	 * Set the blue component
	 * @param blue color component
	 */
	public void b(int blue) {
		this.blue = (byte)((blue<0?0:(blue>255?255:blue))-128);
	}
	
	/**
	 * Set the alpha component
	 * @param alpha color component
	 */
	public void a(int alpha) {
		this.alpha = (byte)((alpha<0?0:(alpha>255?255:alpha))-128);
	}
	
	/**
	 * Set the color using floats
	 * @param red color component (clamped between 0.0f and 1.0f)
	 * @param green color component (clamped between 0.0f and 1.0f)
	 * @param blue color component (clamped between 0.0f and 1.0f)
	 * @param alpha color component (clamped between 0.0f and 1.0f)
	 */
	public void set(float red, float green, float blue, float alpha) {
		this.red = (byte)(((red<0.0f?0.0f:(red>1.0f?1.0f:red))*255)-128);
		this.green = (byte)(((green<0.0f?0.0f:(green>1.0f?1.0f:green))*255)-128);
		this.blue = (byte)(((blue<0.0f?0.0f:(blue>1.0f?1.0f:blue))*255)-128);
		this.alpha = (byte)(((alpha<0.0f?0.0f:(alpha>1.0f?1.0f:alpha))*255)-128);
	}
	
	/**
	 * Set the color using integers
	 * @param red color component (clamped between 0.0f and 1.0f)
	 * @param green color component (clamped between 0.0f and 1.0f)
	 * @param blue color component (clamped between 0.0f and 1.0f)
	 * @param alpha color component (clamped between 0.0f and 1.0f)
	 */
	public void set(int red, int green, int blue, int alpha) {
		this.red = (byte)((red<0?0:(red>255?255:red))-128);
		this.green = (byte)((green<0?0:(green>255?255:green))-128);
		this.blue = (byte)((blue<0?0:(blue>255?255:blue))-128);
		this.alpha = (byte)((alpha<0?0:(alpha>255?255:alpha))-128);
	}
	
	
}
