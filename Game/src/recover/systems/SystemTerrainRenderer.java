package recover.systems;


import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.Shader;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import engine.utils.UnexistingShaderException;
import recover.behaviors.batch.TerrainBatch;

public class SystemTerrainRenderer extends EngineSystem<TerrainBatch>{
	
	public SystemTerrainRenderer() {
		super(BehaviorType.TERRAIN_BATCH);
	}

	@Override
	protected void onCreate(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAdd(EngineBehavior behavior) {
		TerrainBatch batch = (TerrainBatch) behavior;
		batch.generate();
		batch.deactivate();
	}

	@Override
	protected void onRemove(EngineBehavior behavior) {
		TerrainBatch batch = (TerrainBatch) behavior;
		batch.destroy();	
	}

	@Override
	protected void onChange(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRun() {		
		// Terrain Drawing
		Shader shader;
		try {	
			
			shader = shaderManager().get("Terrain");
			shader.use();
			shader.setFloatUni("currentTime", (float) GLFW.glfwGetTime());
			shader.setMat4Uni("projectionView", camera().projectionView());
			shader.setVec4Uni("plane", 0, 1, 0, 0);
			TerrainBatch batch = null;
			ArrayList<EngineBehavior> list= behaviorManager().list(BehaviorType.TERRAIN_BATCH);
			for(int i = 0; i< list.size(); i++) {
				
				batch = (TerrainBatch) list.get(i);
				// If batch needs update
				if( batch.isActive()) {
					batch.updateGPUBuffer();
					batch.deactivate();
				}
				
				batch.calculateOffset(camera().position());
				shader.setVec3Uni("offset", batch.offset());
				if(batch.isVisible()) {
					batch.draw();
				}
			}
		} catch (UnexistingShaderException e) {
			e.printStackTrace();
		}	
	}

}
