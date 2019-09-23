package engine.utils;

public class TextureException extends Exception{

	private static final long serialVersionUID = 1L;
	private String name;
	
	/**
	 * Texture exception constructor
	 * @param name of the font
	 */
	public TextureException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "The Texture " + name + " doesn't exist to the specified path";
	}
	
	@Override
	public void printStackTrace() {
		System.err.println(getMessage());
		super.printStackTrace();
	}
}
