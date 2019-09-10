package recover.entities;

import org.joml.Vector2f;

import engine.entities.EngineEntity;
import recover.behaviors.ChunkCulling;
import recover.behaviors.WorldConnector;
import recover.behaviors.batch.TerrainBatch;
import recover.behaviors.model.TerrainModel;
import recover.utils.HeightGenerator;

public class Terrain extends EngineEntity{
	
	public Terrain(Vector2f position, HeightGenerator generator) {
		this.addBehavior(new WorldConnector(position));
		this.addBehavior(new ChunkCulling(connector()));
		this.addBehavior(new TerrainModel(connector(), generator, culling()));
		this.addBehavior(new TerrainBatch(model()));
	}
	
	public WorldConnector connector() {
		return (WorldConnector) this.behaviors().get(0);
	}
	
	public ChunkCulling culling() {
		return (ChunkCulling) this.behaviors().get(1);
	}
	
	public TerrainModel model() {
		return (TerrainModel) this.behaviors().get(2);
	}
	
	public TerrainBatch batch() {
		return (TerrainBatch) this.behaviors().get(3);
	}
}
