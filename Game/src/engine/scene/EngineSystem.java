package engine.scene;

import java.util.ArrayDeque;

import engine.ShaderManager;
import engine.behaviors.BehaviorManager;
import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.entities.EntityManager;
import engine.opengl.camera.Camera;
/**
 * This abstract class defines the system base structure 
 * System are only aware of one behavior and they run different
 * codes in function of the event they get from the other scene 
 * systems running. They process each events such as behavior update
 * adding or removing, and then run another code if necessary
 * at any time you can raise new events in the system functions
 * @author louis
 *
 */
public abstract class EngineSystem<Behavior extends EngineBehavior>{
	
	private ArrayDeque<SceneEvent> eventQueue;
	private SceneEventManager eventManager;
	private BehaviorType type;
	
	/** Entity map manager */
	private EntityManager entityManager;
	/** Shader map manager */
	private ShaderManager shaderManager;
	/** Framebuffer map manager */
	private FramebufferManager framebufferManager;
	/** Behavior map manager */
	private BehaviorManager behaviorManager;
	/** reference to the scene camera */
	private Camera camera;
	/** boolean to know if a system can be executed */
	private boolean isRunnable;
	
	public EngineSystem(BehaviorType type){
		this.type = type;
		this.eventQueue = new ArrayDeque<>();
		this.behaviorManager = null;
		this.entityManager = null;
		this.framebufferManager = null;
		this.isRunnable = false;
	}
	
	
	/** Code to execute When a Creation Event occurred on the
	 *  Listened behaviors
	 * @param event containing the entities
	 */
	protected abstract void onCreate(SceneEvent event);
	
	/** Code to execute When a Destruction Event occurred on the
	 *  Listened behaviors
	 * @param event containing the entities
	 */
	protected abstract void onDestroy(SceneEvent event);
	
	/** Code to execute When a Adding Event occurred on the
	 *  Listened behaviors
	 * @param event containing the entities
	 */
	protected abstract void onAdd(EngineBehavior behavior);
	
	/** Code to execute When a Removing Event occurred on the
	 *  Listened behaviors
	 * @param event containing the entities
	 */
	protected abstract void onRemove(EngineBehavior behavior);


	/** Code to execute When a Modification Event occurred on the
	 *  Listened behaviors
	 * @param event containing the entities
	 */
	protected abstract void onChange(SceneEvent event);
	

	/**This run method is called after the event handler step 
	 * You can do whatever you want here using the 
	 * list of behavior to iterate and run code
	 */
	protected abstract void onRun();
	
	/**
	 * Run the system methods polling each system
	 * @return true if the system has been executed entirely
	 */
	public final boolean execute() {
		if(isRunnable) {
			while(!eventQueue.isEmpty()) {
				SceneEvent event = eventQueue.poll();
				switch(event.type()) {
				case CREATE:
					break;
				case DESTROY:
					this.onDestroy(event);
					break;
				case MODIFY:
					this.onChange(event);
					break;
				default:
					break;
				}
				
			}
			onRun();
			return true;	
		}
		return false;
	}
	
	public void pushEvent(SceneEvent event) {
		this.eventQueue.push(event);
	}
	
	/**
	 * Getter for the system type
	 * @return the system type
	 */
	public BehaviorType type() {
		return type;
	}
	
	/**
	 * Used to set the reference to the behavior map
	 * @param behaviorManager the general behavior manager
	 */
	public void setBehaviorManager(BehaviorManager behaviorManager) {
		this.behaviorManager = behaviorManager;
	}
	
	/**
	 * Used by child to get the behavior manager from general class
	 * @return the behavior manager
	 */
	protected BehaviorManager behaviorManager() {
		return this.behaviorManager;
	}	
	
	/**
	 * Used by child to get the shader manager from general class
	 * @return the shader manager
	 */
	protected ShaderManager shaderManager() {
		return this.shaderManager;
	}
	
	/**
	 * Used to set the reference to the shader map
	 * @param shaderManager the general shader manager
	 */
	public void setShaderManager(ShaderManager shaderManager) {
		this.shaderManager = shaderManager;
	}
	
	/**
	 * Used by child to get the shader manager from general class
	 * @return the shader manager
	 */
	protected FramebufferManager framebufferManager() {
		return this.framebufferManager;
	}
	
	/**
	 * Used to set the reference to the shader map
	 * @param shaderManager the general shader manager
	 */
	public void setFramebufferManager(FramebufferManager framebufferManager) {
		this.framebufferManager = framebufferManager;
	}
	
	/**
	 * Used to set the reference to the entity map
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * Used by child to get the entity manager from general class
	 * @return the entity manager
	 */
	protected EntityManager entityManager() {
		return this.entityManager;
	}
	
	/**
	 * Used to set the reference to the event pool
	 * @param eventManager of the scene
	 */
	public void setEventManager(SceneEventManager eventManager) {
		this.eventManager = eventManager;
	}
	
	/**
	 * Used by child to get the event manager from general class
	 * @return the event manager
	 */
	protected SceneEventManager eventManager() {
		return this.eventManager;
	}
	
	/**
	 * Set the reference to the scene camera
	 * @param camera
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * Get the camera of the scene
	 * @return the scene camera
	 */
	public Camera camera() {
		return this.camera;
	}

	/** Get if the system is runnable
	 * @return the isRunnable
	 */
	public boolean isRunnable() {
		return isRunnable;
	}


	/**
	 * @param isRunnable Set to true when added to the scene
	 */
	public void setRunnable(boolean isRunnable) {
		this.isRunnable = isRunnable;
	}



	
}
