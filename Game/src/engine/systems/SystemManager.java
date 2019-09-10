package engine.systems;

import java.util.HashMap;

import engine.ShaderManager;
import engine.behaviors.BehaviorManager;
import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.entities.EntityManager;
import engine.opengl.camera.Camera;
import engine.scene.EngineSystem;
import engine.scene.FramebufferManager;
import engine.scene.SceneEventManager;

public class SystemManager {

	/** Map of the Systems */
	private HashMap<BehaviorType, EngineSystem<? extends EngineBehavior>> systemManager;
	/** Entities manager */
	private EntityManager entityManager;
	/** Behavior manager */
	private BehaviorManager behaviorManager;
	/** Framebuffer manager */
	private FramebufferManager framebufferManager;
	/** Shader manager */
	private ShaderManager shaderManager;
	/** Shader manager */
	private SceneEventManager eventManager;
	/** Camera of the scene */
	private Camera camera;
	
	/**
	 * Constructor of the Scene Systems Class manager
	 * @param events 
	 */
	public SystemManager(EntityManager entityManager, BehaviorManager behaviorManager, ShaderManager shaderManager, FramebufferManager framebufferManager, SceneEventManager eventManager, Camera camera) {
		this.systemManager = new HashMap<>();
		this.entityManager = entityManager;
		this.behaviorManager = behaviorManager;
		this.shaderManager = shaderManager;
		this.framebufferManager = framebufferManager;
		this.eventManager = eventManager;
		this.camera = camera;
	}
	
	/**
	 * Puts the system into the map
	 * @param system
	 */
	public void add(EngineSystem<? extends EngineBehavior> system) {
		this.systemManager.put(system.type(), system);
		system.setEntityManager(entityManager);
		system.setBehaviorManager(behaviorManager);
		system.setShaderManager(shaderManager);
		system.setEventManager(eventManager);
		system.setFramebufferManager(framebufferManager);
		system.setCamera(this.camera);
		system.setRunnable(true);
	}
	
	/** 
	 * Get a system by the behavior type it deals with
	 * @param type of the behavior processed by the system
	 */
	public EngineSystem<? extends EngineBehavior> get(BehaviorType type) {
		return this.systemManager.get(type);
	}
	
	/** 
	 * Check existence of a system to handle the behavior type
	 * @param type of the behavior processed by the system
	 */
	public boolean contains(BehaviorType type) {
		return this.systemManager.containsKey(type);
	}
	
	/** 
	 * Check existence of a system in the map
	 * @param system to look up
	 */
	public boolean contains(EngineSystem<? extends EngineBehavior> system) {
		return this.systemManager.containsValue(system);
	}
	
	/** 
	 * Execute the system associated with the type
	 * @param type of the system to execute
	 * @return true if the system exists and has been executed, false otherwise
	 */
	public boolean execute(BehaviorType type) {
		EngineSystem<?> system = systemManager.get(type);
		if(system != null) {
			return system.execute();
		}
		return false;
	}
}
