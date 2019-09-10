package engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import engine.gui.EngineGUI;
import engine.opengl.camera.Camera;
import engine.scene.EngineScene;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector2i;

/**
 * This class is the engine used by the game to draw everything on screen
 * @author louis
 *
 */
public abstract class Engine {

	/** Reference to the Context window */
	private long window;
	/** Screen size */
	public Vector2i engineSize = new Vector2i();

	protected String name;
	protected EngineTime engineTime;
	protected ShaderManager engineShaders;
	protected EngineScene engineScene;
	protected EngineGUI engineGUI;
	protected EngineWindowEvents engineEvents;



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
		engineSize.set(width, height);
	
		this.engineTime = new EngineTime();		
		this.engineShaders = new ShaderManager();
		this.engineScene = new EngineScene(engineSize, engineTime, this.engineShaders, new Camera(x, y, z, engineSize));
		this.engineGUI = new EngineGUI(engineSize, this.engineShaders);

		this.engineEvents = new EngineWindowEvents(engineScene, engineGUI, engineSize);

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
			this.engineTime.update();
			this.engineScene.processCamera(window);
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
		window = glfwCreateWindow(engineSize.x(), engineSize.y(), name, 0, 0);
		if (window == NULL) {throw new RuntimeException("Failed to Create Window");}

		// Setup of the key callback as the Event Manager dispatch key function
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			engineEvents.dispatchKey(window, key, scancode, action, mods);
		});

		//Set mouse callback
		glfwSetCursorPosCallback(window, (window, x, y) -> {
			engineEvents.dispatchCursorPos(window, (float)x, (float)y);
		});

		// Setup a Frame buffer callback used to resize scene object when the window is
		// resized
		glfwSetWindowSizeCallback(window, (window, width, height) -> {
			engineSize.set(width, height);
			glViewport(0, 0, width, height);
			engineEvents.dispatchSize(window, width, height);
		});

		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		
		/// Getting video mode presets to set the window position on the screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center the window
		glfwSetWindowPos(window, (vidmode.width() - engineSize.x()) / 2, (vidmode.height() - engineSize.y()) / 2);


	}
	
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
		System.out.println(1.0 / engineTime.getDeltaTime());
	}
	
	/** Get the engine scene
	 * @return the scene
	 */
	public EngineScene scene() {
		return this.engineScene;
	}
	
	/** Get the engine shaders
	 * @return the shaders
	 */
	public ShaderManager shaders() {
		return this.engineShaders;
	}
	
	/** Get the engine gui
	 * @return the gui
	 */
	public EngineGUI gui() {
		return this.engineGUI;
	}
	
	/**
	 * @return the name of the window
	 */
	public String name() {
		return name;
	}
}

