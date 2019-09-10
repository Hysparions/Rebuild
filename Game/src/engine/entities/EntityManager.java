package engine.entities;

import java.util.HashMap;
/**
 * This class contains and manage all insertions and remove operations 
 * of the entities active in the scene
 * Behavior Lists mapped by type
 * @author louis
 *
 */
public class EntityManager {
	
	/** HashMap of Entities */
	private final HashMap<Integer, EngineEntity> entities;
	
	/** 
	 * Constructor of the Entity manager class
	 */
	public EntityManager() {
		this.entities = new HashMap<>();
	}
	
	/**
	 * Adds an entity to the entity map
	 * @param entity to add
	 */
	public void add(EngineEntity entity) {
		if(entities.containsKey(entity.id())) {
			System.err.print("Entity have the same id");
		}
		entities.put(entity.id(), entity);
	}
	
	/**
	 * Try to find the entity specified in the global map
	 * @param entity to find
	 */
	public boolean contains(EngineEntity entity) {
		return entities.containsKey(entity.id());
	}
	
	/**
	 * Try to find the entity specified in the global map
	 * @param identifier of the entity to find
	 */
	public boolean contains(Integer identifier) {
		return entities.containsKey(identifier);
	}
	
	/**
	 * Remove and returns associated with the identifier specified
	 * @param identifier of the entity
	 * @return the entity
	 */
	public EngineEntity remove(Integer identifier) {
		return entities.remove(identifier);
	}
	
	/**
	 * Returns the Entity associated with the identifier specified
	 * @param identifier of the entity
	 * @return the entity
	 */
	public EngineEntity get(Integer identifier){
		return entities.get(identifier);
	}
}
