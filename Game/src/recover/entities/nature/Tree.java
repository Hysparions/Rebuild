package recover.entities.nature;

import org.joml.Vector3f;

import engine.entities.EngineEntity;
import engine.opengl.models.StaticModel;
import recover.behaviors.Location;
import recover.behaviors.SpreadingBiome;
import recover.behaviors.model.TerrainModel;
import recover.threads.ModelManager;
import recover.utils.Biome;

/**
 * This class defines the tree entity archetype
 * @author louis
 *
 */
public class Tree extends EngineEntity{

	public static final int BIOME_SPREAD = 6;
	
	public static final String OAK = "Oak";
	
	/**
	 * Construct a new Tree with the specified information
	 * @param name of the tree, should use static Tree fields
	 * @param position of the tree
	 * @param angle rotation of the tree
	 * @param models manager
	 */
	public Tree( String name, Vector3f position, float angle, TerrainModel terrain, ModelManager models, Biome biome) {
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
