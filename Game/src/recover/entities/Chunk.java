package recover.entities;

import org.joml.Vector2f;

import engine.entities.EngineEntity;
import recover.behaviors.WorldConnector;

/**
 * Chunk Entity
 * @param position
 */
public class Chunk extends EngineEntity{
	
	public static final int ENTITY_RADIUS = 1; // Distance From the Grid where entities can spawn
	public static final int SUB_CHUNK_NUMBER = 10; // Number of sub generated part of the chunk
	public static final int WIDTH = 5; // Number of point per sub Chunk
	public static final int ENTITY_MAX_HEIGHT = 60; // Height limiting entity spawning
	public static final int HEIGHT = 100; // Height max
	public static final int DEFAULT_CAPACITY = 10000000; // Default capacity of the entity buffer
	
	public Chunk(Vector2f position) {
		super();
		this.addBehavior(new WorldConnector(position));
	}

	public WorldConnector connector() {
		return (WorldConnector)this.behaviors().get(0);
	}
	
}
