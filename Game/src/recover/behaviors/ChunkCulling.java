package recover.behaviors;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;

/**
 * This behavior acts as a turn on/off function for batch rendering of the chunk 
 * Frustrum Culling system decides whether the object with this component should
 * be drawn or not
 * @author louis
 *
 */
public class ChunkCulling extends EngineBehavior{

	/** Visibility indicator */
	private boolean isVisible;
	/** World position */
	private WorldConnector connector;
	
	/**
	 * Constructor of the Chunk Culling Behavior
	 * @param connector
	 */
	public ChunkCulling(WorldConnector connector) {
		super(BehaviorType.CHUNK_CULLING);
		this.connector = connector;
		this.isVisible = true;
	}
	
	/**
	 * Getter for the visibility of the entity that have this behavior
	 * @return true if the entity is Visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Set the visibility of the entity to false
	 */
	public void hide() {
		this.isVisible = false;
	}
	
	/** 
	 * Set the visibility of the object to true
	 */
	public void show() {
		this.isVisible = true;
	}
	
	/**
	 * @return the world connector component holding the object world position
	 */
	public WorldConnector connector() {
		return connector;
	}
	
	@Override
	public boolean generateSceneEvent() {return false;}
}
