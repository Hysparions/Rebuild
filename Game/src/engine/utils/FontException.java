package engine.utils;

public class FontException extends Exception{

	private static final long serialVersionUID = 1L;
	private String name;
	
	/**
	 * Font exception constructor
	 * @param name of the font
	 */
	public FontException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "The font " + name + " doesn't exist in the font folder or in the font map";
	}
	
	@Override
	public void printStackTrace() {
		System.err.println(getMessage());
		super.printStackTrace();
	}
}
