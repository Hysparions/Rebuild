package engine.scene;

import java.util.ArrayDeque;

import engine.behaviors.EngineBehavior;

/**
 * This contains the Event queue and the SceneEvent Creation 
 * @author louis
 *
 */
public class SceneEventManager {

	/** Event stack */
	private ArrayDeque<SceneEvent> events;
	
	/** 
	 * Constructor of the Scene Event Queue
	 * @param entities the general entity map
	 */
	public SceneEventManager() {
		this.events = new ArrayDeque<>();
	}
	
	/**
	 * Push a new event into the stack that will be dispatched into systems queues
	 * @param id the id of the entity raising event
	 * @param behavior the behavior of this entity that raises an event
	 * @param type the type of event
	 */
	public void pushEvent(EngineBehavior source, EngineBehavior destination, SceneEventType type) {
		this.events.push(new SceneEvent(source, destination, type));
	}
	
	/**
	 * Poll the stack of event
	 */
	public SceneEvent pollEvent() {
		return this.events.poll();
	}

	/**
	 * Check if there is no events left in the pool
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return this.events.isEmpty();
	}
	

	
}
