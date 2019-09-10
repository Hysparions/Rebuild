package engine.behaviors;

import java.util.LinkedList;

/**
 * This class defines the general behavior master class inherited by each behaviors in the game
 * @author louis
 *
 */
public abstract class EngineBehavior {

	/** Integer holding the entity parent id */
	protected Integer parentID;
	/** Type of the Behavior used to map it*/
	protected BehaviorType type;
	/** boolean used to turn on/off system run on this behavior*/
	protected boolean isActive;
	/** List of Listener */
	protected LinkedList<EngineBehavior> listeners;
	
	/**
	 * Constructor of the behavior abstract class
	 * @param type of the behavior
	 */
	public EngineBehavior(BehaviorType type) {
		this.type = type;
		this.isActive = true;
		this.listeners = null;
	}
	
	/**
	 * Set the parent entity id
	 * @param identifier of the parent
	 */
	public void setParent(Integer identifier) {
		this.parentID = identifier;
	}
	
	/**
	 * Get the parent entity id
	 * @return parent entity id
	 */
	public Integer parent() {
		return this.parentID;
	}
	
	/**
	 * Add a behavior to the list of the behavior listening for change events on this behavior
	 * @param behavior listening this object modifications
	 */
	public final void addListeners(EngineBehavior behavior) {
		if(this.listeners == null) {
			this.listeners = new LinkedList<>();
		}
		this.listeners.add(behavior);
	}
	
	/**
	 * Gets the list of listeners of the behavior
	 * @return
	 */
	public final LinkedList<EngineBehavior> listeners() {
		if(this.listeners == null) {
			this.listeners = new LinkedList<>();
		}
		return this.listeners;
	}
	
	/**
	 * Override this method and return true if you want to generate an adding/removing event 
	 * when the behavior is added / removed to the scene
	 * @return true if an event should be generated when adding this behavior type to the scene
	 */
	public abstract boolean generateSceneEvent();
	

	
	/**
	 * Get the type of the Behavior
	 * @return the behavior's type
	 */
	public final BehaviorType type() {
		return type;
	}

	/**
	 * This method is used to know if the behavior is Activated
	 * Activation Boolean can be set for various purpose when iterating through list in system
	 * for example when you don't want to act on a specified behavior, you can deactivate it
	 * @return True if the behavior is active
	 */
	public final boolean isActive() {
		return isActive;
	}

	/**
	 * This method is used to set the behavior to active state
	 * Activation Boolean can be set for various purpose when iterating through list in system
	 * for example when you don't want to act on a specified behavior, you can deactivate it
	 */
	public final void activate() {
		this.isActive = true;
	}
	
	/**
	 * This method is used to set the behavior to inactive state
	 * Activation Boolean can be set for various purpose when iterating through list in system
	 * for example when you don't want to act on a specified behavior, you can deactivate it
	 */
	public final void deactivate() {
		this.isActive = false;
	}

}
