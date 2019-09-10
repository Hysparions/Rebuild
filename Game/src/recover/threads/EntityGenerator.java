package recover.threads;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import recover.Recover;
import recover.behaviors.model.TerrainModel;
import recover.behaviors.model.WaterModel;
import recover.entities.Chunk;
import recover.entities.StaticEntities;
import recover.entities.nature.Grass;
import recover.entities.nature.Rock;
import recover.entities.nature.Tree;
import recover.utils.Biome;

/**
 * This class Generates all the entity when a chunk is generated
 * @author louis
 *
 */
public class EntityGenerator {

	private final int WIDTH = Chunk.WIDTH;
	private final int SUB_CHUNK_NUMBER = Chunk.SUB_CHUNK_NUMBER;
	
	/** Number of attempt to place entities on the map by sub chunk */
	private final int AMOUNT = 2;
	/** tiles used to know if an entity has been already set on this place */
	private boolean tiles[];
	/** Position of the top left corner of the sub chunk*/
	private Vector2i position;
	/** Random number generator*/
	private Random randomGenerator;
	/** Model map */
	private ModelManager modelManager;
	
	public EntityGenerator(ModelManager modelManager) {
		this.tiles = new boolean[WIDTH*WIDTH];
		this.position = new Vector2i();
		this.randomGenerator = new Random(Recover.SEED);
		this.modelManager = modelManager;
	}
	
	/**
	 * Generates entities and add them as children of the static entites
	 * @param staticEntities
	 * @param model
	 */
	public void generateEntities(StaticEntities staticEntities) {
		
		int random, random2;
		// Getting terrain model
		TerrainModel terrainModel  = staticEntities.decoration().terrain();
		
		// Loop on each sub Chunks
		for(int x = 0; x < SUB_CHUNK_NUMBER ; x++) {
			for(int z = 0; z < SUB_CHUNK_NUMBER ; z++) {
				// First reset the tile array
				resetTiles();
				// Attempt the amount times to insert new One tiled Entity
				for(int attempt = 0; attempt < AMOUNT; attempt++) {
					random = Math.abs(randomGenerator.nextInt())%(WIDTH*WIDTH);
					random2 = Math.abs(randomGenerator.nextInt())%100;
					
					// Set the position of the tile
					position.set(x*WIDTH + (random/WIDTH), z*WIDTH+(random%WIDTH));
					
					Float height = terrainModel.point(position.x, position.y).y();
					if(height > WaterModel.WATER_LEVEL && height < 60) {
						if(terrainModel.point(position.x, position.y+1).y() == height
						&& terrainModel.point(position.x+1, position.y).y() == height	
						&& terrainModel.point(position.x+1, position.y+1).y() == height	) {
							
							Vector3f location = new Vector3f();
							Vector2f world = staticEntities.decoration().terrain().connector().position();
							location.set((world.x*WIDTH*SUB_CHUNK_NUMBER+position.x)*2 + 1, height, (world.y*WIDTH*SUB_CHUNK_NUMBER+position.y)*2 + 1);
							if(random2 < 30) {
								// Test if there is some place
								if(tiles[random] == false) {
									tiles[random] = true;
									Rock entity = new Rock(location, random, modelManager, Rock.ROCK);
									staticEntities.addChild(entity);
									staticEntities.decoration().add(entity.model(), false);
								}							
							}else if (random2 < 50){
								// Test if there is some place
								if(tiles[random] == false) {
									tiles[random] = true;
									Tree entity = new Tree(Tree.OAK,location, random, terrainModel, modelManager, Biome.FOREST);
									
									staticEntities.addChild(entity);
									staticEntities.vegetation().add(entity.model(), false);
								}
							
							}else if (random2 < 80){
								// Test if there is some place
								if(tiles[random] == false) {
									tiles[random] = true;
									Grass entity = new Grass(Grass.GRASS,location, random, terrainModel, modelManager, Biome.PLAINS);
									staticEntities.addChild(entity);
									staticEntities.vegetation().add(entity.model(), false);
								}
							}
						}
					}
					
				}
			}
		}
		
		
	}
	
	/** Reset the tiles marker of the generator */
	private void resetTiles() {
		for(int i = 0; i<tiles.length; i++) {
			tiles[i] = false;
		}
	}
}
