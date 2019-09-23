package recover.systems;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.Shader;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import engine.utils.ShaderException;
import recover.behaviors.batch.VegetationBatch;

public class SystemVegetationRenderer extends EngineSystem<VegetationBatch>{

	
	public SystemVegetationRenderer() {
		super(BehaviorType.VEGETATION_BATCH);
		
	}

	@Override
	protected void onCreate(SceneEvent event) {	}

	@Override
	protected void onDestroy(SceneEvent event) { }

	@Override
	protected void onAdd(EngineBehavior behavior) {	
		VegetationBatch batch = (VegetationBatch) behavior;
		batch.generate();
		batch.activate();
	}

	@Override
	protected void onRemove(EngineBehavior behavior) { 
		VegetationBatch batch = (VegetationBatch) behavior;
		batch.destroy();
	}

	@Override
	protected void onChange(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRun() {

		// Entity Drawing
		Shader shader;
		try {
			shader = shaderManager().get("Vegetation");
			shader.use();
			shader.setMat4Uni("projectionView", camera().projectionView());
			shader.setFloatUni("time", (float) GLFW.glfwGetTime());
			shader.setVec4Uni("plane", 0, 1, 0, 0);
			VegetationBatch batch = null;
			ArrayList<EngineBehavior> list= behaviorManager().list(BehaviorType.VEGETATION_BATCH);
			for(int i = 0; i< list.size(); i++) {
				batch = (VegetationBatch) list.get(i);
				batch.calculateOffset(camera().position());
				shader.setVec3Uni("offset", batch.offset());
				if(batch.isVisible()) {
					batch.draw();
				}
			}
		} catch (ShaderException e) {
			e.printStackTrace();
		}

		
	}
}
