package world.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import world.batch.StaticBatch;
import world.behavior.Behavior;
import world.behavior.BehaviorType;
import world.behavior.Location;
import world.behavior.SpreadingBiome;
import world.behavior.StaticModel;
import world.biomes.Biome;
import world.models.PolyModel;

public class Entity {

	//Behavior attributes
	private List<Behavior> behaviors;
	
	//Name
	private String name;
	
	///Constructor
	public Entity(String name) {
		this.name = name;
		behaviors = new ArrayList<>();
	}
	
	//Behavior Adding Function
	public void addBehavior(Behavior behavior) {
		if(behavior == null) { System.out.println("Big error");}
		behaviors.add(behavior);
	}
	
	public void update() {
		
		for(int i = 0; i < behaviors.size(); i++) {
			behaviors.get(i).update();
		}
	}
	
	public void addModels(StaticBatch batch) {
		for(int i = 0; i < behaviors.size(); i++) {
			if (behaviors.get(i).getType() == BehaviorType.STATICMODEL) {
				batch.add((StaticModel) behaviors.get(i));
			}
		}
	}
	
	public void removeModel(StaticBatch batch) {
		for(int i = 0; i < behaviors.size(); i++) {
			if (behaviors.get(i).getType() == BehaviorType.STATICMODEL) {
				batch.remove((StaticModel) behaviors.get(i));
			}
		}
	}
	
	public static Entity createStaticEntityAt(Vector3f location, float Rotation ,String name, Map<String, PolyModel> plyModels) {
		Entity entity = new Entity(name);
		
		//if the entity is grass
		if(name == "Grass") {
			Location position = new Location(location, Rotation, true);
			StaticModel model = new StaticModel(position, plyModels.get(name),true);
			SpreadingBiome spreadingBiome = new SpreadingBiome(position, Biome.PLAINS);
			//Adding previously created behaviors
			entity.addBehavior(position);
			entity.addBehavior(model);
			entity.addBehavior(spreadingBiome);
			entity.update();
			//System.out.println("Creating Grass at" +location.x + " "+ location.z);
		}else if(name == "Oak") {
			Location position = new Location(location, Rotation, true);
			StaticModel model = new StaticModel(position, plyModels.get(name), true);
			SpreadingBiome spreadingBiome = new SpreadingBiome(position, Biome.FOREST);
			//Adding previously created behaviors
			entity.addBehavior(position);
			entity.addBehavior(model);
			entity.addBehavior(spreadingBiome);
			entity.update();
		}
		
		return entity;
	}
	
	public List<Behavior> getBehaviors(){
		return behaviors;
	}
	
	public String getName() {return name;}
}
