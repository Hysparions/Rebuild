package recover.entities.nature;

import org.joml.Vector3f;

import engine.entities.EngineEntity;
import engine.opengl.models.StaticModel;
import recover.behaviors.Location;
import recover.behaviors.SpreadingBiome;
import recover.behaviors.model.TerrainModel;
import recover.threads.ModelManager;
import recover.utils.Biome;

public class Grass extends EngineEntity{

	public static final int BIOME_SPREAD = 6;
	
	public static final String GRASS = "Grass";
	
	/**
	 * Construct a new Grass or Flower with the specified information
	 * @param name of the tree, should use static Tree fields
	 * @param position of the tree
	 * @param angle rotation of the tree
	 * @param models manager
	 */
	public Grass( String name, Vector3f position, float angle, TerrainModel terrain, ModelManager models, Biome biome) {
		// Create and Load Location Behavior
		this.addBehavior(new Location(position, angle));
		this.addBehavior(new StaticModel(models.getPolyModel(name), location()));
		this.addBehavior(new SpreadingBiome(terrain, location(), biome, BIOME_SPREAD));
	}
	
	/**
	 * Getter for the Location Behavior
	 * @return the location of the tree
	 */
	public Location location() {
		return (Location) this.behaviors().get(0);
	}
	
	/**
	 * Getter for the Model Behavior of the tree
	 * @return the model of the tree
	 */
	public StaticModel model() {
		return (StaticModel) this.behaviors().get(1);
	}
	
	/**
	 * Getter for the spreadingBiome method
	 * @return spreading Biome behavior
	 */
	public SpreadingBiome spreader() {
		return (SpreadingBiome) this.behaviors().get(2);
	}

}
