package engine.gui.text;

/**
 * Kerning size between two glyph
 * @author louis
 *
 */
public class UIKerning {

	/** First glyph id */
	private char first;
	/** Second glyph id */
	private char second;
	/** Amount of displacement of the glyph*/
	private float amount;
	
	public UIKerning(char first, char second, float amount) {
		this.first = first;
		this.second = second;
		this.amount = amount;
	}
	
	/**
	 * Getter for the first glyph id
	 * @return first glyph id
	 */
	public char first() {
		return first;
	}
	
	/**
	 * Getter for the second glyph id
	 * @return second glyph id
	 */
	public char second() {
		return second;
	}
	
	/**
	 * Getter for the displacement amount 
	 * @return the amount
	 */
	public float amount() {
		return amount;
	}
	
	@Override
	public String toString() {
		return "UIKerning first=" + this.first + " second=" + this.second + " amount=" + this.amount;
	}
}
