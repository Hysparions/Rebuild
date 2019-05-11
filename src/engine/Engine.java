package engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import gui.GUI;
import openGL.camera.Camera;
import openGL.camera.Direction;
import world.World;
import openGL.Shader;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
//import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector3f;


public class Engine {
	
	//Size of the SCREEN
	public static int SCR_W = 1600;
	public static int SCR_H = 900;
	
	//Setting the Clip Space
	public static float NEAR = 0.1f;
	public static float FAR = 1000.f;
	
	//Reference to the Context window
	private long window;
	private Camera camera;
	
	//MouseUtils
	private float lastX;
	private float lastY;
	private boolean firstMouse = true;
	private boolean cameraIsActive = true;
	
	//Time Utils
	private double lastTime;
	private double deltaTime;
	
	//Engine Constructor
	public Engine(){
		
		//Creating the Camera
		camera = new Camera(100.0f, 80.0f, 100.0f);
		//Initializing the time utils
		deltaTime = 0.05f;
		lastTime = 0.f;
	}
	
	//Initialization function for the OpenGL context of the engine
	private void init() {
		
		// Setup the GlFW Error Callback System
		GLFWErrorCallback.createPrint(System.err).set();
		
		///Init GLFW Context and throw back exception in case of failure
		if(!glfwInit()) { throw new IllegalStateException("Failed to load GLFW"); }
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_SAMPLES, 4 );
		
		///Creation of the context window for the game 
		window = glfwCreateWindow(SCR_W, SCR_H, "GameEngine", 0, 0);
		if(window == NULL) { throw new RuntimeException("Failed to Create Window"); }
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			}
		});
		
		// Setup a mouse callback. It will be called when the mouse move
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetCursorPosCallback(window, (window, x, y) -> {
			//This is to avoid Camera bounce at the first frame
			if (firstMouse)
			{
				lastX = (float)x;
				lastY = (float)y;
				firstMouse = false;
			}
			//Process Camera if it is activated
			if (cameraIsActive)
			{
				float xoffset = (float)x - lastX;
				float yoffset = lastY - (float)y; // reversed since y-coordinates go from bottom to top
				lastX = (float)x;
				lastY = (float)y;
				camera.processMouse(xoffset, yoffset);
			}
		});
		
		//Setup a Frame buffer callback used to resize scene object when the window is resized
		glfwSetWindowSizeCallback(window, (window, width, height) -> {
			glViewport(0, 0, width, height);
			SCR_W = width;
			SCR_H = height;
		});

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		///Getting video mode presets to set the window position on the screen
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center the window
		glfwSetWindowPos(window, (vidmode.width() - SCR_W) / 2, (vidmode.height() - SCR_H) / 2);

		
		// Enable v-sync
		glfwSwapInterval(0);
		///Activate Window
		glfwShowWindow(window);
		glEnable(GL_DEPTH_TEST); 
		glEnable(GL_CULL_FACE);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CLIP_DISTANCE0);
		//Show Mouse
		//glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	//Launch method to run the Engine
	public void launch() {
		
		init();
		runGame();
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	//Run the Game 
	private void runGame() 
	{
		GUI gui = GUI.createBasicGUI("Helvetica", SCR_W, SCR_H);
		
		///SPRITES
		Shader SpriteShader = new Shader();
		if(SpriteShader.create("Sprite") == false) {System.out.println("Failed to create Shader");}
		SpriteShader.setOrthoProjection(SCR_W, SCR_H);
		

		///Terrain
		Shader solidShader = new Shader();
		if(solidShader.create("Terrain") == false) {System.out.println("Failed to create Shader");}
		Shader waterShader = new Shader();
		if(waterShader.create("Water") == false) {System.out.println("Failed to create Shader");}
		solidShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(0.8f, 0.8f, 0.8f), new Vector3f(0.4f, 0.4f, 0.4f));
		waterShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(0.8f, 0.8f, 0.8f), new Vector3f(0.4f, 0.4f, 0.4f));

		///Entities
		Shader staticEntityShader = new Shader();
		if(staticEntityShader.create("StaticModel") == false) {System.out.println("Failed to create Shader");}
		staticEntityShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(0.8f, 0.8f, 0.8f), new Vector3f(0.4f, 0.4f, 0.4f));
		staticEntityShader.setFloatUni("material.shininess", 4.0f);

		//Shader Projection
		staticEntityShader.setPerspectiveProjection(camera.zoom, SCR_W, SCR_H, NEAR, FAR);
		solidShader.setPerspectiveProjection(camera.zoom, SCR_W, SCR_H, NEAR, FAR);
		waterShader.setPerspectiveProjection(camera.zoom, SCR_W, SCR_H, NEAR, FAR);
		
		
		//Creating the world component
		World world = new World(solidShader, waterShader, staticEntityShader);
		
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		while(!glfwWindowShouldClose(window)) {
			
			glClearColor(0.7f, 0.85f, 0.9f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//Polling Events
			glfwPollEvents();
			
			
			//System.out.println(1/deltaTime);
			//Input
			processInput(window);
			processTime();
			camera.update();
			//System.out.println("Camera" + camera.getPosition().x/2 + " " + camera.getPosition().z/2);
			world.render(camera);	
			
			gui.render();

			
			glfwSwapBuffers(window);
			
		}
		
		world .destroy();
		solidShader.destroy();
		waterShader.destroy();
		staticEntityShader.destroy();
		gui.destroy();

	}
	
	
	public void processInput(long window) {
		//Process Camera movments in every direction
		if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {	camera.processKeyBoard(Direction.FORWARD, (float)deltaTime);}
		if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {	camera.processKeyBoard(Direction.BACKWARD, (float)deltaTime);}
		if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {	camera.processKeyBoard(Direction.LEFT, (float)deltaTime);}
		if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {	camera.processKeyBoard(Direction.RIGHT, (float)deltaTime);}
		if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {	camera.processKeyBoard(Direction.DOWN, (float)deltaTime);}
		if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {	camera.processKeyBoard(Direction.UP, (float)deltaTime);}
	}
	
	public void processTime() {
		double time = glfwGetTime();
		deltaTime = time-lastTime;
		lastTime = time;
	}
	
	//Main function
	public static void main(String[] args) {
		
		//Creation Launch of the game engine
		Engine gameEngine = new Engine();
		gameEngine.launch();
	}
}
