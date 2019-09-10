package recover.entities;

import engine.entities.EngineEntity;
import recover.behaviors.batch.DecorationBatch;
import recover.behaviors.batch.VegetationBatch;
import recover.behaviors.model.TerrainModel;

public class StaticEntities extends EngineEntity{

	public StaticEntities(TerrainModel terrain) {
		super();
		this.addBehavior(new DecorationBatch(terrain));
		this.addBehavior(new VegetationBatch(terrain));
	}
	
	public DecorationBatch decoration() {
		return (DecorationBatch) this.behaviors().get(0);
	}
	
	public VegetationBatch vegetation() {
		return (VegetationBatch) this.behaviors().get(1);
	}
}
