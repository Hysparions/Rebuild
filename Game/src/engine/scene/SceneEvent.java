package engine.scene;

import engine.behaviors.EngineBehavior;

/**
 * The Scene event Class holds references to the entity affected by the changed, the associated behavior and the event type
 * @author louis
 *
 */
public class SceneEvent{
	
	private EngineBehavior source;
	private EngineBehavior destination;
	private SceneEventType type;
	
	public SceneEvent(EngineBehavior source, EngineBehavior destination, SceneEventType type) {
		
		this.source = source;
		this.destination = destination;
		this.type = type;
	}

	
	/**
	 * @return the entity
	 */
	public EngineBehavior source() {
		return this.source;
	}

	/**
	 * @return the destination behavior
	 */
	public EngineBehavior destination() {
		return destination;
	}
	
	/**
	 * @return the type of the event
	 */
	public SceneEventType type() {
		return type;
	}


}
