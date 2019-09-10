package recover.entities;

import engine.entities.EngineEntity;
import recover.behaviors.batch.WaterBatch;
import recover.behaviors.model.TerrainModel;
import recover.behaviors.model.WaterModel;

/**
 * This Batch behavior is used to display nice low poly water on the world
 * @author louis
 *
 */
public class Water extends EngineEntity{
	
	/**
	 * Constructor by default for the water entity class
	 * @param terrain
	 */
	public Water(TerrainModel terrain) {
		this.addBehavior(new WaterModel(terrain));
		this.addBehavior(new WaterBatch(waterModel()));
	}
	
	/**
	 * @return the WaterModel of this water entity
	 */
	public WaterModel waterModel() {
		return (WaterModel) this.behaviors().get(0);
	}
	
	/**
	 * @return the Water Batch of this water entity
	 */
	public WaterModel waterBatch() {
		return (WaterModel) this.behaviors().get(1);
	}
}
