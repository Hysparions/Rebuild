package world.systems;

import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import world.Chunk;
import world.behavior.Behavior;
import world.behavior.SpreadingBiome;
import world.entities.Entity;

public class Systems {
	
	//Chunk
	Map<Vector2f, Chunk> chunks;
	Vector2f buffer;
	
	//Systems AI 
	SpreadingBiomeSystem spreadingBiome;
	
	//Systems Rendering
	ModelSystem model;
	
	public Systems(Map<Vector2f, Chunk> chunks) {
		
		//Linking chunks map
		this.chunks = chunks;
		this.buffer = new Vector2f();
		
		//Linking all system
		this.spreadingBiome = new SpreadingBiomeSystem();
		this.model= null;
	}
	
	public void run() {
		float time = (float) GLFW.glfwGetTime();
		chunks.forEach((position, chunk) -> {
			//Acquiring the entity list
			List<Entity> entities = chunk.getEntities();
			//Run through each Entity
			for(int i = 0; i < entities.size(); i++) {
				//Run throught different behaviors of each entities while running adequate systems
				List<Behavior> behaviors = entities.get(i).getBehaviors();
				for(int j = 0; j < behaviors.size(); j++) {
					Behavior behavior = behaviors.get(j);
					if(behavior.isActive()) {
						switch(behavior.getType()) {
						// If the behavior is a spreadingBiomeAI
						case SPREADINGBIOME:
							
							spreadingBiome.run((SpreadingBiome)behavior, chunks, time);
							
						default:
							break;
						}
					}
					
				}
			}

		});
		
		chunks.forEach((position, chunk) -> {
			//Checking if the Chunk needs to update its terrain mesh Color
			if(chunk.needsTerrainUpdate()) {
				chunk.updateTerrainBuffer();
				chunk.getTerrain().updateGPUBuffer();
				chunk.setTerrainUpdate(false);
		}});
	}
	
}
