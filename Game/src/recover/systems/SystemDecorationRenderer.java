package recover.systems;

import java.util.ArrayList;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.opengl.Shader;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import engine.utils.ShaderException;
import recover.behaviors.batch.DecorationBatch;

public class SystemDecorationRenderer extends EngineSystem<DecorationBatch>{

	public SystemDecorationRenderer() {
		super(BehaviorType.DECORATION_BATCH);
		
	}

	@Override
	protected void onCreate(SceneEvent event) {	}

	@Override
	protected void onDestroy(SceneEvent event) { }

	@Override
	protected void onAdd(EngineBehavior behavior) {	
		DecorationBatch batch = (DecorationBatch) behavior;
		batch.generate();
		batch.activate();
	}

	@Override
	protected void onRemove(EngineBehavior behavior) { 
		DecorationBatch batch = (DecorationBatch) behavior;
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
			shader = shaderManager().get("Decoration");
			shader.use();
			shader.setMat4Uni("projectionView", camera().projectionView());
			DecorationBatch batch = null;
			ArrayList<EngineBehavior> list= behaviorManager().list(BehaviorType.DECORATION_BATCH);
			for(int i = 0; i< list.size(); i++) {
				batch = (DecorationBatch) list.get(i);
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
