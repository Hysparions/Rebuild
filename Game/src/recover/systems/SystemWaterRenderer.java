package recover.systems;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.Shader;
import engine.opengl.framebuffers.EngineFramebuffer;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import engine.utils.UnexistingShaderException;
import recover.behaviors.batch.TerrainBatch;
import recover.behaviors.batch.VegetationBatch;
import recover.behaviors.batch.WaterBatch;
import recover.behaviors.model.WaterModel;
import static org.lwjgl.opengl.GL30.*;


public class SystemWaterRenderer extends EngineSystem<WaterBatch>{

	
	/**
	 * Creates a water renderer with the specified Width and height for the reflection texture
	 * @param width
	 * @param height
	 */
	public SystemWaterRenderer() {
		super(BehaviorType.WATER_BATCH);
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
		WaterBatch batch = (WaterBatch) behavior;
		batch.generate();
		batch.activate();
	}

	@Override
	protected void onRemove(EngineBehavior behavior) {
		WaterBatch batch = (WaterBatch) behavior;
		batch.destroy();
	}

	@Override
	protected void onChange(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRun() {
		try {

			glEnable(GL_CLIP_DISTANCE0);
			
			// Getting shaders
			Shader terrainShader = this.shaderManager().get("Terrain");
			Shader waterShader = this.shaderManager().get("Water");
			Shader vegetationShader = this.shaderManager().get("Vegetation");
			// Getting FBO
			EngineFramebuffer waterFBO = this.framebufferManager().get("WaterReflection");
		
			
			// Binding Water Reflection Frame Buffer  Color Attachment 0 texture unit 0
			waterFBO.bindToRender(0,0);
			// Clear the color of the buffer
			glClearColor(0.234f, 0.739f, 0.805f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			float distance = camera().position().y - WaterModel.WATER_LEVEL;
			if (distance > 0) {
				camera().position().y -= 2 * distance;
				camera().invertPitch();
			} else {
				camera().position().y += 2 * distance;
				camera().invertPitch();
			}
			camera().updateView();
			
			terrainShader.use();
			terrainShader.setFloatUni("currentTime", (float) GLFW.glfwGetTime());
			terrainShader.setMat4Uni("projectionView", camera().projectionView());
			terrainShader.setVec4Uni("plane", 0, 1, 0, -WaterModel.WATER_LEVEL+camera().position().y + 0.25f);
			
			TerrainBatch terrain = null;
			ArrayList<EngineBehavior> list= behaviorManager().list(BehaviorType.TERRAIN_BATCH);
			for(int i = 0; i< list.size(); i++) {
				terrain = (TerrainBatch) list.get(i);
				terrain.calculateOffset(camera().position());
				terrainShader.setVec3Uni("offset", terrain.offset());
				if(terrain.isVisible()) {
					terrain.draw();
				}
			}
			
			
			vegetationShader.use();
			vegetationShader.setFloatUni("time", (float) GLFW.glfwGetTime());
			vegetationShader.setMat4Uni("projectionView", camera().projectionView());
			vegetationShader.setVec4Uni("plane", 0, 1, 0, -WaterModel.WATER_LEVEL+camera().position().y + 0.25f);
			
			VegetationBatch vegetation = null;
			list= behaviorManager().list(BehaviorType.VEGETATION_BATCH);
			for(int i = 0; i< list.size(); i++) {
				vegetation = (VegetationBatch) list.get(i);
				vegetation.calculateOffset(camera().position());
				vegetationShader.setVec3Uni("offset", vegetation.offset());
				if(vegetation.isVisible()) {
					vegetation.draw();
				}
			}
			
			
			
			
			if (distance > 0) {
				camera().position().y += 2 * distance;
				camera().invertPitch();
			} else {
				camera().position().y -= 2 * distance;
				camera().invertPitch();
			}
			
			camera().updateView();
			waterFBO.unbind();
			
			waterShader.use();
			waterShader.setMat4Uni("projectionView", camera().projectionView());
			waterShader.setFloatUni("time", (float) GLFW.glfwGetTime());
			
			waterFBO.bindTexture(0, 0);
			WaterBatch water = null;
			ArrayList<EngineBehavior> listWater = behaviorManager().list(BehaviorType.WATER_BATCH);
			


			glDisable(GL_CULL_FACE);
			for(int i = 0; i< listWater.size(); i++) {
				water = (WaterBatch) listWater.get(i);
				water.calculateOffset(camera().position());
				waterShader.setVec3Uni("offset", water.offset());
				if(water.isVisible())
				water.draw();
			}
			glEnable(GL_CULL_FACE);
			glDisable(GL_CLIP_DISTANCE0);
		} catch (UnexistingShaderException e) {
			e.printStackTrace();
		}		
	}

}
