package engine.utils;

/**
 * This exception occurs when the shader asked isn't contained in the shader
 *  source directory nor in the shader map
 * @author louis
 *
 */
public class UnexistingShaderException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UnexistingShaderException() {
		
	}
	
	@Override
	public String getMessage() {
		return "The shader doesn't exist in the shader folder or in the shaders map";
	}
	
	@Override
	public void printStackTrace() {
		System.err.println(getMessage());
		super.printStackTrace();
	}
}