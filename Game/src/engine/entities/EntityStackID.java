package engine.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityStackID {

	/** ID Amount */
	private static int ID_AMOUNT = 0;
	/** Static List of the ids used */
	private final static List<Integer> idList = Collections.synchronizedList(new ArrayList<>());

	/** adds the id to the list of ids */
	public static synchronized int getID() {
		if(idList.isEmpty()) {
			ID_AMOUNT++;
			return ID_AMOUNT;
		}
		else {
			return idList.remove(idList.size()-1);
		}
	}
	
	/** Free an ID */
	public static synchronized void freeID(int id) {
		idList.add(id);
	}
}
