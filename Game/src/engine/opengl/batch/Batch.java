package engine.opengl.batch;

import java.util.List;


/**
 * This interface defines the GUI Batching behavior of the different Drawable
 * elements of the GUI
 * 
 * @author louis
 *
 */
public interface Batch<T> {
	
	/**
	 * This method generate the batch and creates all the OpenGL stuff needed to display things on screen
	 */
	public void generate();
	
	/**
	 * Getter for the VBO
	 * @return Vertex Array Object of the batch
	 */
	public int VBO();

	/**
	 * Getter for the VAO
	 * @return Vertex Array Object of the batch
	 */
	public int VAO();

	/**
	 * Getter for the Capacity
	 * @return Capacity of the batch in bytes
	 */
	public int capacity();

	/**
	 * Getter for the Limit
	 * @return Limit of the Batch in bytes
	 */
	public int limit();

	/**
	 * Getter for the elements list
	 * 
	 * @return List of the elements contained in the batch
	 */
	public List<T> elements();
	/**
	 * Getter for the elements list
	 * 
	 * @param i id of the element
	 * @return the element contained in the batch
	 */
	public T element(int i);

	/**
	 * This Function render the batch to the bound FBO
	 */
	public void draw();

	/**
	 * This Function adds an existing object at the end of the batch and calls the
	 * resize function if the batch is too small.
	 */
	public void add(T obj, boolean updateVBO);

	/**
	 * This Function render the batch to the bounded FBO
	 */
	public void remove(T obj, boolean updateVBO);

	/**
	 * This Function resize the batch to the adequate capacity. This
	 * function calls shift method to remove batch holes
	 */
	public void sizeBuffer();

	/**
	 * This Function destroys the batch and clear GPU memory as well as Elements
	 * contained in it
	 */
	public void destroy();

	/**
	 * This function send to the GPU the data of the byte buffer between those two values
	 * @param offset the starting byte
	 * @param limit the ending byte
	 */
	public void update(int offset, int limit);

	/**
	 * This Function update all the elements of the batch. Use it when the elements
	 * changed size or moved but the amount of vertices stays unchanged
	 */
	public void updateAll();

	/**
	 * This Function reload all elements in the batch and resize it if necessary.
	 * Calls the clear() method
	 */
	public void reload();

	/**
	 * This Function clear all elements in the batch
	 */
	public void clear();
	

}
