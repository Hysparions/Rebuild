package recover.threads;


import org.joml.Vector2f;

import recover.Recover;
import recover.entities.Chunk;
import recover.entities.StaticEntities;
import recover.entities.Terrain;
import recover.entities.Water;
import recover.utils.HeightGenerator;

/**
 * This class load, generate and save the chunk data
 * on an other thread to keep the game smooth !
 * @author louis
 *
 */
public class ChunkIO extends Thread{

	/** The chunkManager object is holding the shared data with the main thread*/
	private ChunkManager chunkManager;
	/** This boolean is set to false when the chunk loader should stop */	
	private boolean running;
	/** HeightGenerator */
	private HeightGenerator heightGenerator;
	/** Entity Generator */
	private EntityGenerator entityGenerator;

	public ChunkIO(ChunkManager data, ModelManager modelManager) {
		super("ChunkManager");
		this.setPriority(Thread.MIN_PRIORITY);
		this.chunkManager = data;
		this.running = true;
		this.heightGenerator = new HeightGenerator(Recover.SEED);
		this.entityGenerator = new EntityGenerator(modelManager);
	}
	
	@Override
	public void run() {
		//int time = 0;
		while(chunkManager.shouldRun()) {
			try {
				
				// Wait until main thread has added and removed precedent chunks
				while((!chunkManager.toAddIsEmpty() || !chunkManager.toRemoveIsEmpty()) && running) {
					running = chunkManager.shouldRun();
					
					
					Thread.sleep(30);
				}
				/*
				time+=1;
				if(time>500) {
					time = 0;
					System.out.println("Run GC");
					System.gc();
				}*/
				
				if(running) {
					// Sleep by default 50 miliseconds between executions
					Thread.sleep(50);

					// Run algorithm and fill to Remove and to Load stacks
					chunkManager.runChunkLoader();

					//Load all the chunks
					while(!chunkManager.toLoadIsEmpty() && running) {
						running = chunkManager.shouldRun();
						
						// Get the position of the chunk to Load
						Vector2f position = chunkManager.popToLoad();

						//Check if it exists in the file and load it in this case 
						Chunk loadedChunk = loadChunkFromFile(position);
						
						// If the chunk doesn't exist in file, create it
						if(loadedChunk == null) { loadedChunk = generateNewChunk(position);}
						
						// Sleep before another execution
						Thread.sleep(30);
						
						this.chunkManager.pushToAdd(loadedChunk);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thread stopped");
	}
	
	private Chunk loadChunkFromFile(Vector2f position) {
		return null;
	}
	
	/**
	 * Generate a new chunk using the position specified in parameters
	 * @param position of the chunk
	 * @return a new generated chunk at this position
	 */
	private Chunk generateNewChunk(Vector2f position) {
		
		Chunk chunk = null;
		
		// Generate new Terrain
		Terrain terrain = new Terrain(position, heightGenerator);
		// Generate new Water
		Water water = new Water(terrain.model());
		// Generate new Decoration
		StaticEntities decoration = new StaticEntities(terrain.model());
		// Fill terrain with new entities
		this.entityGenerator.generateEntities(decoration);
		// Calculates the colors and normals of the terrain after biome spreading
		terrain.model().calculateNormalsAndColors();
		
		chunk = new Chunk(position);
		chunk.addChild(terrain);
		chunk.addChild(water);
		chunk.addChild(decoration);
		return chunk;
	}

}
