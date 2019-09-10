package engine.behaviors;

/**
 * This enum holds the name of every existing behaviors
 * @author louis
 *
 */
public enum BehaviorType {
	
	/** Behavior defining position, rotation and world space matrix of an entity */
	LOCATION,
	/** Behavior defining the cycle of existence of an entity like vegetation */
	LIFE_CYCLE,
	/** Behavior spreading a biome over the entity on the terrainModel behavior */
	SPREAD_BIOME,
	/** Behavior defining a static model data, in world space, which is added to a batch */
	STATIC_MODEL,
	/** Behavior used to connect some entities with other chunks */
	WORLD_CONNECTOR, 
	/** Behavior used to cull Chunk based on world connector position */ 
	CHUNK_CULLING, 
	/** Behavior used to define the Terrain Batch data and OpenGL buffers */
	TERRAIN_BATCH,
	/** Behavior representing a chunk of terrain */
	TERRAIN_MODEL,
	/** Water Batch holds the water vertex dat and OpenGL buffers used for water rendering*/
	WATER_BATCH,
	/** Water model holds the water tiles data */
	WATER_MODEL, 
	/** Batch used for static object with light glowing alpha shading effect */	
	DECORATION_BATCH,
	/** Batch used for static object with flexibility alpha shading*/
	VEGETATION_BATCH;
}
