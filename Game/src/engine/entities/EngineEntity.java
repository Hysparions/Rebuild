package engine.entities;

import java.util.ArrayList;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;


/**
 * This abstract class defines the base of entities and how they are linked
 * to behaviors and child entities
 * @author louis
 *
 */
public abstract class EngineEntity{

	/** Unique identifier given by the stack allocator */
	private int identifier;
	/** parent Entity */
	private EngineEntity parent;
	/** children Entity */
	private ArrayList<EngineEntity> children;
	
	/** behaviors of the Entity */
	private ArrayList<EngineBehavior> behaviors;
	
	/**
	 * Constructor of the Entity Class
	 */
	public EngineEntity() {
		this.identifier = EntityStackID.getID();
		this.parent = null;
		this.children = new ArrayList<>();
		this.behaviors = new ArrayList<>();
	}

	/**
	 * Getter for the identifier of the Entity
	 * @return the Entity id
	 */
	public int id() {
		return identifier;
	}

	/**
	 * Check if entity contains a behavior of the specified behavior type
	 * @param type the behavior
	 * @return true if the entity contains it
	 */
	public final boolean contains(BehaviorType type) {
		for(EngineBehavior behavior : behaviors) {
			if(type == behavior.type()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getter for the behavior list
	 * @return behaviors list
	 */
	public ArrayList<EngineBehavior> behaviors(){
		return this.behaviors;
	}
	
	/**
	 * Get the Entity containing this one
	 * @return parent Entity
	 */
	public EngineEntity parent() {
		return this.parent;
	}
	
	/**
	 * Set the parent entity
	 * @param parent Entity
	 */
	public void setParent(EngineEntity parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the list of child entities 
	 * @return children entity list
	 */
	public ArrayList<EngineEntity> children() {
		return this.children;
	}
	
	/**
	 * Add the behavior to the behaviors list
	 * @param behavior to add
	 */
	public final void addBehavior(EngineBehavior behavior) {
		this.behaviors.add(behavior);
		behavior.setParent(this.identifier);
	}
	
	/**
	 * Add the child entity to the children list
	 * @param behavior to add
	 */
	public final void addChild(EngineEntity child) {
		this.children.add(child);
		child.setParent(this);
	}
}
