package recover.entities.nature;

import org.joml.Vector3f;

import engine.entities.EngineEntity;
import engine.opengl.models.StaticModel;
import recover.behaviors.Location;
import recover.threads.ModelManager;

/**
 * This class defines the rock entities
 * @author louis
 *
 */
public class Rock extends EngineEntity{

	public static final String SMALL_ROCK = "SmallRock1";
	public static final String ROCK = "Rock3";
	public static final String BOY = "Boy_Backup";
	
	public Rock(Vector3f position, float angle, ModelManager models, String name) {
		// Create and Load Location Behavior
		this.addBehavior(new Location(position, angle));
		this.addBehavior(new StaticModel(models.getPolyModel(name), location()));
	}
	
	public Location location() {
		return (Location) this.behaviors().get(0);
	}
	
	public StaticModel model() {
		return (StaticModel) this.behaviors().get(1);
	}
}
