package recover.threads;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector4i;

import engine.opengl.models.collada.ColladaModel;
import engine.opengl.models.stanford.PolyModel;

/** This class Loads Models of the game */
public class ModelManager {
	
	/** Poly Format Models Map */
	private Map<String, PolyModel> plyMap;
	/** Collada Format Models Map */
	private Map<String, ColladaModel> daeMap;
	
	
	public ModelManager() {
		this.plyMap = Collections.synchronizedMap(new HashMap<String, PolyModel>());
		this.daeMap = Collections.synchronizedMap(new HashMap<String, ColladaModel>());
		// Loading Poly
		this.plyMap.put("Rock3", new PolyModel("Rock3", "Decoration"));		
		this.plyMap.put("Oak", new PolyModel("Oak", "Plants", new Vector4i(67, 90, 37, 25)));	
		this.plyMap.put("Grass", new PolyModel("Grass", "Plants", new Vector4i(66, 102, 34, 5)));	
		// Loading Collada
		this.daeMap.put("Boy", new ColladaModel("Boy", "Humans"));
	}
	
	/**
	 * Synchronized get method for a poly model
	 * @param name of the model
	 * @return the model if it exists
	 */
	public PolyModel getPolyModel(String name) {
		synchronized(plyMap) {
			return this.plyMap.get(name);
		}
	}
}
