package world;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL30.*;

import openGL.Shader;
import openGL.camera.Camera;
import world.models.PolyModel;
import world.systems.Systems;
import world.terrain.TerrainData;
import world.water.WaterBuffers;

public class World {
	
	//Terrain width
	private static final float WIDTH = 3.0f;
	
	//Chunks
	private Map<Vector2f, Chunk> chunks;
	private HeightGenerator heightGen;
	//private ChunkLoader chunkLoader;
	
	//Systems
	Systems systems;
	
	//Static Models
	private Map<String, PolyModel> staticModels;
	
	private Shader terrainShader;
	private Shader waterShader;
	private Shader vegetationShader;
	
	WaterBuffers waterTextures;
	
	float time = 2.0f;
	Vector2f buffer;
	
	//Utils
	//private Vector2f playerPosition;
	//private Random rng = new Random(HeightGenerator.SEED);
	
	public World(Shader terrain, Shader water, Shader vegetation) {
		
		//Creating Chunks
		chunks = new HashMap<>();
		//Creating Systems
		systems = new Systems(chunks);
		//Loading Static Models
		staticModels = loadPolyModels();
		//Generating Water Reflection Buffer
		waterTextures =  new WaterBuffers();
		
		//Creating player position / view Distance for shaders
		//playerPosition = new Vector2f(125.0f, 125.0f);
		float viewDistance = WIDTH * Chunk.WIDTH * Chunk.SUB_CHUNK_NUMBER;
		
		//Setting Shaders uniforms
		terrainShader = terrain;
		terrainShader.use();
		terrainShader.setVec2Uni("center", 125.0f, 125.0f);
		terrainShader.setFloatUni("viewDistance", viewDistance);
		terrainShader.setFloatUni("swapTime", TerrainData.SWAP_TIME);
		
		waterShader = water;
		waterShader.use();
		waterShader.setIntUni("reflection", 0);
		waterShader.setFloatUni("waterLevel", Chunk.WATER_LEVEL);
		waterShader.setFloatUni("viewDistance", viewDistance);
		waterShader.setVec2Uni("center", 125.0f, 125.0f);
		
		vegetationShader = vegetation;
		vegetation.use();
		vegetation.setFloatUni("viewDistance", viewDistance);
		vegetation.setVec2Uni("center", 125.0f, 125.0f);
		
		heightGen = new HeightGenerator();
		//chunkLoader = new ChunkLoader( chunks, toLoad, heightGen);
		//chunkLoader.start();
		//Load the Chunks
		buffer = new Vector2f(0.0f);
		for(float i = -1; i < WIDTH-1 ; i++) {
			for(float j = -1; j< WIDTH-1; j++) {
				Vector2f pos = new Vector2f(i, j);
				Chunk chunk = Chunk.generateFromSeed(pos, buffer, heightGen, staticModels);
				chunks.put(pos, chunk);
				connectChunks();
				chunk.generateBuffers();
				chunk.generateMesh();
				chunk.addEntityModelsInBatch();
			}
		}
		
	}

	public void destroy() {
		//Destroy Water frameBuffers
		waterTextures.destroy();
		///DESTROY Chunks
		chunks.forEach((position, chunk) -> {chunk.destroy();});
		//chunkLoader.stopThread();
	}
	
	///RENDERING
	//rendering Terrain
	public void renderTerrain() {
			terrainShader.use();
			chunks.forEach((position, chunk) -> {chunk.renderSolid();});
	}
	
	//rendering Water
	public void renderWater() {
			waterShader.use();
			chunks.forEach((position, chunk) -> {chunk.renderWater(waterTextures);});
		
	}
	
	//rendering Vegetation
	public void renderVegetation() {
			vegetationShader.use();
			chunks.forEach((position, chunk) -> {chunk.renderVegetation();});
	}
	
	//General Rendering
	public void render(Camera camera) {

		//Running Systems
		systems.run();
		
		//Water Reflection Frame Buffer
		waterTextures.bindWaterReflectFBO();
		glClearColor(0.7f, 0.85f, 0.9f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		float distance = camera.getPosition().y - Chunk.WATER_LEVEL;
		if(distance>0) { camera.getPosition().y -= 2*distance; }
		else { camera.getPosition().y += 2*distance; }
		camera.invertPitch();
		camera.update();
		terrainShader.use();
		terrainShader.setFloatUni("currentTime", (float)GLFW.glfwGetTime());
		terrainShader.setTerrainShader(camera);
		terrainShader.setVec4Uni("plane", 0, 1, 0, -(Chunk.WATER_LEVEL)+0.1f);
		renderTerrain();
		vegetationShader.use();
		vegetationShader.setFloatUni("time", (float)GLFW.glfwGetTime());
		vegetationShader.setStaticModelShader(camera);
		renderVegetation();
		if(distance>0) { camera.getPosition().y += 2*distance; }
		else { camera.getPosition().y -= 2*distance; }
		camera.invertPitch();
		camera.update();
		waterTextures.unBindCurrentFBO();


		vegetationShader.use();
		vegetationShader.setStaticModelShader(camera);
		renderVegetation();
		
		//Finally Render the whole scene
		terrainShader.use();
		terrainShader.setTerrainShader(camera);
		//terrainShader.setVec2Uni("center", camera.getPosition().x, camera.getPosition().z);
		terrainShader.setVec4Uni("plane", 0, 1, 0, 0);
		waterShader.use();
		//waterShader.setVec2Uni("center", camera.getPosition().x, camera.getPosition().z);
		waterShader.setWaterShader(camera);
		renderTerrain();
		glDisable(GL_CULL_FACE);
		renderWater();	
		glEnable(GL_CULL_FACE);
		
		

	}
	
	private void connectChunks() {
		chunks.forEach((position, chunk) -> {
			buffer.set(position.x-1, position.y);
			if(chunks.containsKey(buffer)) { chunk.setLeft(chunks.get(buffer)); }
			buffer.set(position.x+1, position.y);
			if(chunks.containsKey(buffer)) { chunk.setRight(chunks.get(buffer)); }
			buffer.set(position.x, position.y-1);
			if(chunks.containsKey(buffer)) { chunk.setFront(chunks.get(buffer)); }
			buffer.set(position.x, position.y+1);
			if(chunks.containsKey(buffer)) { chunk.setBack(chunks.get(buffer)); }
		});
	}
	
	private Map<String, PolyModel> loadPolyModels() {
		Map<String, PolyModel> models = new HashMap<>();
		
		//Loading Vegetation
		
		models.put(new String("Grass"), new PolyModel("Grass", "Plants"));
		models.put(new String("Oak"), new PolyModel("Oak", "Plants"));
		
		return models;
	}
	
}
