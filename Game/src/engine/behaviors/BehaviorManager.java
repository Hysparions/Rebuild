package engine.behaviors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains and manage all insertions and remove operations 
 * of the behaviors active in the scene
 * Behavior Lists mapped by type
 * @author louis
 *
 */
public class BehaviorManager {

	/** HashMap of Behaviors */
	private final HashMap<BehaviorType, ArrayList<EngineBehavior>> behaviors;
	
	/** 
	 * Constructor of the Behavior manager class
	 */
	public BehaviorManager() {
		this.behaviors = new HashMap<>();
	}
	
	/**
	 * Adds a behavior to the behavior map
	 * @param behavior to add
	 */
	public void add(EngineBehavior behavior) {
		if(!behaviors.containsKey(behavior.type())) {
			ArrayList<EngineBehavior> arrayList = new ArrayList<>();
			behaviors.put(behavior.type(), arrayList);
		}
		ArrayList<EngineBehavior> list = behaviors.get(behavior.type());
		list.add(behavior);
	}
	
	/**
	 * Try to find the behavior specified in the global map
	 * @param behavior to find
	 * @return true if the object is contained by the behavior manager
	 */
	public boolean contains(EngineBehavior behavior) {
		if(behaviors.containsKey(behavior.type())) {
			ArrayList<EngineBehavior> list = behaviors.get(behavior.type());
			if(list.contains(behavior)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Try to find the behavior specified in the global map
	 * @param behavior to remove
	 */
	public void remove(EngineBehavior behavior) {
		if(behaviors.containsKey(behavior.type())) {
			ArrayList<EngineBehavior> list = behaviors.get(behavior.type());
			list.remove(behavior);
		}
	}
	
	/**
	 * This method returns the list of all the behaviors associated to the type specified in params
	 * @param type of the behavior
	 * @return the list of behaviors associated to the specified type
	 */
	public ArrayList<EngineBehavior> list(BehaviorType type){
		if(!behaviors.containsKey(type)) {
			ArrayList<EngineBehavior> arrayList = new ArrayList<>();
			behaviors.put(type, arrayList);
		}
		return behaviors.get(type);
	}
}
