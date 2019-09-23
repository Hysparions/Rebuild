package recover;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_RGB;

import java.util.ArrayList;

import org.joml.Vector3f;

import engine.Engine;
import engine.behaviors.BehaviorType;
import engine.entities.EngineEntity;
import engine.gui.UIWindow;
import engine.gui.text.UIFont;
import engine.opengl.Shader;
import engine.opengl.framebuffers.EngineFramebuffer;
import engine.utils.ShaderException;
import recover.behaviors.model.WaterModel;
import recover.gui.views.MainMenuNavigation;
import recover.systems.SystemChunkCulling;
import recover.systems.SystemDecorationRenderer;
import recover.systems.SystemSpreadingBiome;
import recover.systems.SystemTerrainRenderer;
import recover.systems.SystemVegetationRenderer;
import recover.systems.SystemWaterRenderer;
import recover.systems.SystemWorldConnector;
import recover.threads.ChunkIO;
import recover.threads.ChunkManager;
import recover.threads.ModelManager;
import recover.utils.TerrainPoint;

/**
 * Recover class extends Engine class and contains the main game loop
 * @author louis
 *
 */
public class Recover extends Engine{

	/** The seed used to create the world */
	public static final int SEED = 23021998;
	// SEED x 10000 z 0 = 23021998

	/** Chunk In / Out Thread */
	private ChunkIO chunkIO;
	/** Chunk Manager holding the bridge between the main thread and the chunk In/Out thread*/
	private ChunkManager chunkManager;
	/** model dataBase */
	private ModelManager modelManager;
	/** Event Manager */
	private WindowEventManager eventManager;

	/**
	 * Constructor of the Recover Engine
	 * @param width of the screen
	 * @param height of the screen
	 * @param name of the window
	 */
	public Recover(int width, int height, String name) {
		super(name, width, height, 0.0f, 80.0f, 0.0f);
		// Chunk Thread Loader
		this.modelManager = new ModelManager();
		this.chunkManager = new ChunkManager();
		this.eventManager = new WindowEventManager(this.scene(), this.gui(), this.size);
		this.chunkIO = new ChunkIO(chunkManager, modelManager);
	}

	@Override
	public void handleKeyEvent(long window, int key, int scancode, int action, int mods) {
		eventManager.handleKeyEvent(window, key, scancode, action, mods);
	}

	@Override
	public void handleSizeEvent(long window, int width, int height) {
		eventManager.handleSizeEvent(window, width, height);
	}

	@Override
	public void handleMousePositionEvent(long window, float x, float y) {
		eventManager.handleMousePositionEvent(window, x, y);
	}

	@Override
	public void beforeLoop() {

		// START STEP

		// Enable vertical synchronization
		this.vSync(true);
		// Show the engine window
		this.showEngine(true);
		// Show the cursor
		this.showEngineMouse(false);
		// Enabling Depth Test 
		this.depthTest(true);
		// Enabling Face Culling
		this.cullTest(true);
		// Enable Blending
		this.blendTest(true);
		// Provoking vertex shading
		this.provokinVertexShading(true);
		// Enabling MSAA
		this.antiAliasing(true);
		
		// Set blank screen on launch
		this.setClearColor(1.f, 1.f, 1.f, 1.f);
		this.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.swap();
		
		// LOADING STEP		
		
		// Creation of the fonts
		createFonts();
		// Creation of the shaders
		createShaders();
		// Creation of the Framebuffers
		createFramebuffers();

		// Creating the Systems
		createSystems();

		// Create the GUI
		createGUI();

		// Set max priority to the rendering thread
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// Start Chunk Loader
		this.chunkIO.start();

	}

	/**
	 * This is the heart of the program. this function is executed 
	 * in loop until the user asked for the closing of the window
	 * Systems acting on behaviors and Threads loading terrains and
	 * entities act during this loop
	 */
	@Override
	public void onLoop() {
		this.setClearColor(0.8f, 0.93f, 1.0f, 1.0f);
		this.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		/// CHUNK INPUT / OUTPUT
		// camera position
		this.chunkManager.setPosition(this.scene().camera().position());
		// Adding one chunk if ready
		if(!this.chunkManager.toAddIsEmpty()) {
			EngineEntity chk = this.chunkManager.popToAdd();
			this.scene().addEntity(chk);	
		}
		// Removing one Chunk if needed
		if(!this.chunkManager.toRemoveIsEmpty()) {
			Integer identifier = this.chunkManager.popToRemove();
			this.scene().removeEntity(identifier);	
		}

		// Polling Scene Event
		scene().dispatchEvents();

		// Culling
		scene().executeSystem(BehaviorType.CHUNK_CULLING);
		// Terrain Drawing
		scene().executeSystem(BehaviorType.TERRAIN_BATCH);
		// Entity Drawing
		scene().executeSystem(BehaviorType.DECORATION_BATCH);
		// Entity Drawing
		scene().executeSystem(BehaviorType.VEGETATION_BATCH);
		// Water Drawing
		scene().executeSystem(BehaviorType.WATER_BATCH);
		// Spreading Biome
		scene().executeSystem(BehaviorType.SPREAD_BIOME);
		//scene().camera().print();

		// UI Process
		gui().process();
		//gui().render("Main Menu Navigation");


		//this.printFPS();
		this.swap();
	}

	@Override
	public void afterLoop() {
		chunkManager.stop();
		// Destroy  all the chunks
		ArrayList<Integer> list = chunkManager.getLoadedChunksID();
		for(int i  = 0; i < list.size(); i++) {
			scene().removeEntity(list.get(i));
		}

		// Destroy scene shaders and fbos
		scene().destroy();
		// Destroy gui shaders and buffers
		gui().destroy();
	}

	/**
	 * Create and initializes all the shader programs of the game
	 */
	private void createShaders() {
		// Shaders creation
		try {

			// Terrain shader
			this.shaders().addFromSource("Terrain");
			Shader terrainShader = this.scene().shaders().get("Terrain");
			terrainShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.3f, 0.3f, 0.3f), new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(0.4f, 0.4f, 0.4f));
			terrainShader.setFloatUni("viewDistance", 400.f);
			terrainShader.setFloatUni("swapTime", TerrainPoint.SWAP_TIME);

			// Water Shader
			this.shaders().addFromSource("Water");
			Shader waterShader = scene().shaders().get("Water");
			waterShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.3f, 0.3f, 0.3f),new Vector3f(0.9f, 0.9f, 0.9f), new Vector3f(0.7f, 0.7f, 0.7f));
			waterShader.setIntUni("reflection", 0);
			waterShader.setFloatUni("waterLevel", WaterModel.WATER_LEVEL);
			waterShader.setFloatUni("viewDistance", 400.f);

			// Decoration Shader
			this.shaders().addFromSource("Decoration");
			Shader decorationShader = scene().shaders().get("Decoration");
			decorationShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.3f, 0.3f, 0.3f), new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(0.4f, 0.4f, 0.4f));
			decorationShader.setFloatUni("material.shininess", 4.0f);
			decorationShader.setFloatUni("viewDistance", 400.f);

			// Vegetation Shader
			this.shaders().addFromSource("Vegetation");
			Shader vegetationShader = scene().shaders().get("Vegetation");
			vegetationShader.setDirLight(new Vector3f(-0.8f, -0.7f, -0.5f), new Vector3f(0.3f, 0.3f, 0.3f), new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(0.4f, 0.4f, 0.4f));
			vegetationShader.setFloatUni("material.shininess", 4.0f);
			vegetationShader.setFloatUni("viewDistance", 400.f);


		} catch (ShaderException e) {e.printStackTrace();}

	}

	/**
	 * Create and initializes all the systems of the game
	 */
	private void createSystems() {
		this.scene().addSystem(new SystemTerrainRenderer());
		this.scene().addSystem(new SystemWaterRenderer());
		this.scene().addSystem(new SystemWorldConnector());
		this.scene().addSystem(new SystemChunkCulling());
		this.scene().addSystem(new SystemDecorationRenderer());
		this.scene().addSystem(new SystemVegetationRenderer());
		this.scene().addSystem(new SystemSpreadingBiome());
	}

	/**
	 * Creates and initializes the frame buffers objects
	 */
	private void createFramebuffers() {
		EngineFramebuffer waterFBO = new EngineFramebuffer("WaterReflection", size);
		waterFBO.addColorAttachment(size.x, size.y, GL_RGB, 0, false, true, false);
		waterFBO.setDepthBufferAttachment(size.x, size.y, GL_DEPTH_COMPONENT, false);
		scene().addFramebuffer(waterFBO);
	}

	/**
	 * Creates and Initializes the GUI component
	 */
	private void createGUI() {
		UIWindow mainMenuNavigation = new MainMenuNavigation();
		this.gui().register(mainMenuNavigation);
	}

	private void createFonts() {
		UIFont.createFont("HelvLight");

	}


}
