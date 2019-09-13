package engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import engine.gui.UIManager;
import engine.opengl.camera.Camera;
import engine.scene.EngineScene;

/**
 * This class is the engine used by the game to draw everything on screen
 * @author louis
 *
 */
public abstract class Engine {

	/** Reference to the Context window */
	private long window;
	/** Screen size */
	public Vector2i size = new Vector2i();

	protected String name;
	protected EngineTime time;
	protected ShaderManager shaders;
	protected EngineScene scene;
	protected UIManager gui;



	/**
	 * Constructor of the Engine
	 * @param name of the window
	 * @param width of the screen
	 * @param height of the screen
	 * @param x
	 * @param y
	 * @param z
	 */
	public Engine(String name, int width, int height, float x, float y, float z) {

		this.name = name;
		size.set(width, height);
	
		this.time = new EngineTime();		
		this.shaders = new ShaderManager();
		this.scene = new EngineScene(size, time, this.shaders, new Camera(x, y, z, size));
		this.gui = new UIManager(size);

	}
	
	/**
	 * initialize GLFW
	 * doing pre Looping instantiation
	 * running the Game loop
	 * doing post Looping cleaning
	 * destroy GLFW
	 */
	public final void launch() {

		init();
		beforeLoop();
		while (!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			this.time.update();
			this.scene.processCamera(window);
			onLoop();
		}
		afterLoop();
		destroy();

	}

	// Initialization function for the OpenGL context of the engine
	private final void init() {
		// Setup the GlFW Error Callback System
		GLFWErrorCallback.createPrint(System.err).set();

		/// Init GLFW Context and throw back exception in case of failure
		if (!glfwInit()) {throw new IllegalStateException("Failed to load GLFW");}

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_SAMPLES, 4);

		/// Creation of the context window for the game
		window = glfwCreateWindow(size.x(), size.y(), name, 0, 0);
		if (window == NULL) {throw new RuntimeException("Failed to Create Window");}

		// Setup of the key callback as the Event Manager dispatch key function
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			handleKeyEvent(window, key, scancode, action, mods);
		});

		//Set mouse callback
		glfwSetCursorPosCallback(window, (window, x, y) -> {
			handleMousePositionEvent(window, (float)x, (float)y);
		});

		// Setup a Frame buffer callback used to resize scene object when the window is
		// resized
		glfwSetWindowSizeCallback(window, (window, width, height) -> {
			size.set(width, height);
			glViewport(0, 0, width, height);
			handleSizeEvent(window, width, height);
		});

		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		
		/// Getting video mode presets to set the window position on the screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center the window
		glfwSetWindowPos(window, (vidmode.width() - size.x()) / 2, (vidmode.height() - size.y()) / 2);

		// Finally create gui shaders
		gui.createShaders();
		
	}
	
	/**
	 * Override this method to react on user keyboard input
	 * @param window The pointer to the window
	 * @param key The key that raised an event
	 * @param scancode of the key
	 * @param action Action Pressed Repeated released
	 * @param mods of the key
	 */
	public abstract void handleKeyEvent(long window, int key, int scancode, int action, int mods);
	
	
	/**
	 * Override this method to react on user Window resizing input
	 * @param window The pointer to the Engine Window
	 * @param width The new width of the window
	 * @param height The new height of the Window
	 */
	public abstract void handleSizeEvent(long window, int width, int height);
	
	/**
	 * Override this method to react on user cursor input
	 * @param window The Engine window
	 * @param x The mouse horizontal position
	 * @param y The mouse vertical position
	 */
	public abstract void handleMousePositionEvent(long window, float x, float y);
	
	
	/**
	 * This function contains the code executed before the Game Loop
	 */
	public abstract void beforeLoop() ;
	
	/**
	 * This function contains the code executed during the game Loop
	 */
	public abstract void onLoop() ;

	/**
	 * This function contains the code executed after the Game Loop
	 */
	public abstract void afterLoop() ;

	/**
	 * Destroy Shaders
	 * Destroy the Window
	 * Destroy the Callback
	 * Destroy GL context
	 */
	private final void destroy() {

		// Free shaders
		this.scene().destroy();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	
	/**
	 * Set Engine Clear color
	 * @param red color component
	 * @param green color component
	 * @param blue color component
	 * @param alpha color component
	 */
	public final void setClearColor(float red, float green , float blue, float alpha) {
		glClearColor(red, green, blue, alpha);
	}
	
	/**
	 * Clear the target buffers
	 * @param target
	 */
	public final void clear(int target) {
		glClear(target);
	}
	
	/**
	 * Swap front and back buffers;
	 */
	public final void swap() {
		glfwSwapBuffers(window);
	}
	
	/**
	 * Enable or disable Vsync limitation with this function
	 * @param enable should be true if you want to enable vSync
	 */
	public final void vSync(boolean enable) { 
		if(enable) { glfwSwapInterval(1); }
		else { glfwSwapInterval(0); }
	}
	
	/** Show or hide window
	 * @param show is True when window is visible
	 */
	public final void showEngine(boolean show) {
		if(show) {glfwShowWindow(window);}
		else {glfwShowWindow(0);}
	}
	
	/** Show or hide mouse
	 * @param showMouse is True when mouse is visible
	 */
	public final void showEngineMouse(boolean showMouse) {
		if(showMouse) {glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);}
		else {glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);}
				
	}

	/**
	 * Print the Update Frequency
	 */
	public final void printFPS() {
		System.out.println(1.0 / time.getDeltaTime());
	}
	
	/** Get the engine scene
	 * @return the scene
	 */
	public EngineScene scene() {
		return this.scene;
	}
	
	/** Get the engine shaders
	 * @return the shaders
	 */
	public ShaderManager shaders() {
		return this.shaders;
	}
	
	/** Get the engine gui
	 * @return the gui
	 */
	public UIManager gui() {
		return this.gui;
	}
	
	/**
	 * @return the name of the window
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return the size of the engine
	 */
	public Vector2i size() {
		return this.size;
	}
}

