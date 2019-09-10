package recover.systems;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import recover.behaviors.ChunkCulling;
import recover.entities.Chunk;

public class SystemChunkCulling extends EngineSystem<ChunkCulling>{
	
	private static final float width = Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH*2;

	private Vector4f pointA;
	private Vector4f pointB;
	private Vector4f pointC;
	private Vector4f pointD;
	private Vector4f pointE;
	private Vector4f pointF;
	private Vector4f pointG;
	private Vector4f pointH;

	private Vector4f frustrumBox;
	
	float maxX;
	float maxZ;
	float minX;
	float minZ;
	
	private Matrix4f invertMatrix;
	
	/**
	 * Constructor of the Chunk Culling System
	 */
	public SystemChunkCulling() {
		super(BehaviorType.CHUNK_CULLING);
		this.pointA = new Vector4f(0.0f);
		this.pointB = new Vector4f(0.0f);
		this.pointC = new Vector4f(0.0f);
		this.pointD = new Vector4f(0.0f);
		this.pointE = new Vector4f(0.0f);
		this.pointF = new Vector4f(0.0f);
		this.pointG = new Vector4f(0.0f);
		this.pointH = new Vector4f(0.0f);
		
		this.frustrumBox = new Vector4f(0.0f);
		
		this.invertMatrix = new Matrix4f();
	}

	@Override
	protected void onCreate(SceneEvent event) {}

	@Override
	protected void onDestroy(SceneEvent event) {}

	@Override
	protected void onAdd(EngineBehavior behavior) {}

	@Override
	protected void onRemove(EngineBehavior behavior) {}

	@Override
	protected void onChange(SceneEvent event) {}

	@Override
	protected void onRun() {
		
		// Creates a box representing screen space
		this.pointA.set(-1, -1, -1, 1);
		this.pointB.set(-1, 1, -1, 1);
		this.pointC.set(1, 1, -1, 1);
		this.pointD.set(1, -1, -1, 1);
		this.pointE.set(-1, -1, 1, 1);
		this.pointF.set(-1, 1, 1, 1);
		this.pointG.set(1, 1, 1, 1);
		this.pointH.set(1, -1, 1, 1);
		
		// Get the invert projection View Matrix !
		this.invertMatrix = this.camera().projectionView().invert(this.invertMatrix);
		
		// Apply the matrix transform to each vertices of the screen space to get the projection view frustrum vertices in world space
		this.pointA.mul(this.invertMatrix);
		this.pointA.div(this.pointA.w);
		this.pointB.mul(this.invertMatrix);
		this.pointB.div(this.pointB.w);
		this.pointC.mul(this.invertMatrix);
		this.pointC.div(this.pointC.w);
		this.pointD.mul(this.invertMatrix);
		this.pointD.div(this.pointD.w);
		this.pointE.mul(this.invertMatrix);
		this.pointE.div(this.pointE.w);
		this.pointF.mul(this.invertMatrix);
		this.pointF.div(this.pointF.w);
		this.pointG.mul(this.invertMatrix);
		this.pointG.div(this.pointG.w);
		this.pointH.mul(this.invertMatrix);
		this.pointH.div(this.pointH.w);
		
		// Getting Maximum and minimum point in world space
		maxX = pointA.x;	maxZ = pointA.z;	minX = pointA.x;	minZ = pointA.z;
		
		if(pointB.x>maxX) { maxX = pointB.x; } else if(pointB.x<minX) { minX = pointB.x; }
		if(pointB.z>maxZ) { maxZ = pointB.z; } else if(pointB.z<minZ) { minZ = pointB.z; }
		
		if(pointC.x>maxX) { maxX = pointC.x; } else if(pointC.x<minX) { minX = pointC.x; }
		if(pointC.z>maxZ) { maxZ = pointC.z; } else if(pointC.z<minZ) { minZ = pointC.z; }
		
		if(pointD.x>maxX) { maxX = pointD.x; } else if(pointD.x<minX) { minX = pointD.x; }
		if(pointD.z>maxZ) { maxZ = pointD.z; } else if(pointD.z<minZ) { minZ = pointD.z; }
		
		if(pointE.x>maxX) { maxX = pointE.x; } else if(pointE.x<minX) { minX = pointE.x; }
		if(pointE.z>maxZ) { maxZ = pointE.z; } else if(pointE.z<minZ) { minZ = pointE.z; }
		
		if(pointF.x>maxX) { maxX = pointF.x; } else if(pointF.x<minX) { minX = pointF.x; }
		if(pointF.z>maxZ) { maxZ = pointF.z; } else if(pointF.z<minZ) { minZ = pointF.z; }
		
		if(pointG.x>maxX) { maxX = pointG.x; } else if(pointG.x<minX) { minX = pointG.x; }
		if(pointG.z>maxZ) { maxZ = pointG.z; } else if(pointG.z<minZ) { minZ = pointG.z; }
		
		if(pointH.x>maxX) { maxX = pointH.x; } else if(pointH.x<minX) { minX = pointH.x; }
		if(pointH.z>maxZ) { maxZ = pointH.z; } else if(pointH.z<minZ) { minZ = pointH.z; }
		
		// set the frustrum box
		frustrumBox.set((float)Math.floor((minX+ camera().position().x())/width),(float)Math.floor((minZ+ camera().position().z())/width),(float)Math.floor((maxX+ camera().position().x())/width),(float)Math.floor((maxZ+camera().position().z())/width));

		// Run the algorithm on chunk culling behaviors
		ArrayList<EngineBehavior> list = this.behaviorManager().list(BehaviorType.CHUNK_CULLING);
		ChunkCulling chunk;
		for(int i = 0; i< list.size(); i++) {
			chunk = (ChunkCulling) list.get(i);
			Vector2f position = chunk.connector().position();
			if(position.x>=frustrumBox.x && position.x<=frustrumBox.z && position.y>=frustrumBox.y && position.y<=frustrumBox.w) {
				chunk.show();
			}else {
				chunk.hide();
			}
		}
		
		
		//System.out.println(frustrumBox);
		
		
	}
}
