package recover.systems;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import recover.behaviors.SpreadingBiome;
import recover.behaviors.model.TerrainModel;
import recover.entities.Terrain;

public class SystemSpreadingBiome extends EngineSystem<SpreadingBiome>{
	
	
	public SystemSpreadingBiome() {
		super(BehaviorType.SPREAD_BIOME);
		
	}

	@Override
	protected void onCreate(SceneEvent event) {}

	@Override
	protected void onDestroy(SceneEvent event) {
		SpreadingBiome spreaderBiome = (SpreadingBiome)event.destination();
		// Front Left
		if(spreaderBiome.frontLeft()) {
			if(spreaderBiome.terrain().connector().frontLeft() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontLeft().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// Front
		if(spreaderBiome.front()) {
			if(spreaderBiome.terrain().connector().front() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().front().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// Front Right
		if(spreaderBiome.frontRight()) {
			if(spreaderBiome.terrain().connector().frontRight() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontRight().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// Left
		if(spreaderBiome.left()) {
			if(spreaderBiome.terrain().connector().left() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().left().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// Right
		if(spreaderBiome.right()) {
			if(spreaderBiome.terrain().connector().right() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().right().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// BackLeft
		if(spreaderBiome.backLeft()) {
			if(spreaderBiome.terrain().connector().backLeft() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backLeft().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// Back
		if(spreaderBiome.back()) {
			if(spreaderBiome.terrain().connector().back() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().back().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
		// BackLeft
		if(spreaderBiome.backRight()) {
			if(spreaderBiome.terrain().connector().backRight() != null) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backRight().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.unSpreadExtern(model);
			}
		}
	}

	@Override
	protected void onAdd(EngineBehavior behavior) {}

	@Override
	protected void onRemove(EngineBehavior behavior) {}

	@Override
	protected void onChange(SceneEvent event) {
		SpreadingBiome spreaderBiome = (SpreadingBiome)event.destination();
		// If all chunk are connected and the spread hasn't occured
		if(spreaderBiome.isActive() && spreaderBiome.shouldRunExtern()) {
			// Front Left
			if(spreaderBiome.frontLeft()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontLeft().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// Front
			if(spreaderBiome.front()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().front().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// Front Right
			if(spreaderBiome.frontRight()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontRight().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// Left
			if(spreaderBiome.left()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().left().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// Right
			if(spreaderBiome.right()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().right().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// BackLeft
			if(spreaderBiome.backLeft()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backLeft().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// Back
			if(spreaderBiome.back()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().back().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			// BackLeft
			if(spreaderBiome.backRight()) {
				Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backRight().parent());
				TerrainModel model = terrain.model();
				terrain.batch().activate();
				spreaderBiome.spreadExtern(model);
			}
			spreaderBiome.deactivate();
		}
		// If the spread occured but one of the chunk is removed
		else if(!spreaderBiome.isActive() && !spreaderBiome.shouldRunExtern()) {
			// Front Left
			if(spreaderBiome.frontLeft()) {
				if(spreaderBiome.terrain().connector().frontLeft() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontLeft().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// Front
			if(spreaderBiome.front()) {
				if(spreaderBiome.terrain().connector().front() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().front().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// Front Right
			if(spreaderBiome.frontRight()) {
				if(spreaderBiome.terrain().connector().frontRight() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().frontRight().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// Left
			if(spreaderBiome.left()) {
				if(spreaderBiome.terrain().connector().left() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().left().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// Right
			if(spreaderBiome.right()) {
				if(spreaderBiome.terrain().connector().right() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().right().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// BackLeft
			if(spreaderBiome.backLeft()) {
				if(spreaderBiome.terrain().connector().backLeft() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backLeft().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// Back
			if(spreaderBiome.back()) {
				if(spreaderBiome.terrain().connector().back() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().back().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			// BackLeft
			if(spreaderBiome.backRight()) {
				if(spreaderBiome.terrain().connector().backRight() != null) {
					Terrain terrain = (Terrain)entityManager().get(spreaderBiome.terrain().connector().backRight().parent());
					TerrainModel model = terrain.model();
					terrain.batch().activate();
					spreaderBiome.unSpreadExtern(model);
				}
			}
			spreaderBiome.activate();
		}
	}

	@Override
	protected void onRun() {}

}
