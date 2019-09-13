package engine.scene;

import java.util.ArrayList;

import org.joml.Vector2i;

import engine.EngineTime;
import engine.ShaderManager;
import engine.behaviors.BehaviorManager;
import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.entities.EngineEntity;
import engine.entities.EntityManager;
import engine.opengl.camera.Camera;
import engine.opengl.framebuffers.EngineFramebuffer;
import engine.systems.SystemManager;

/*
 * Engine Scene contains 3d sceneObjects such as terrain, water, entities etc...
 */
public class EngineScene {


	/** size of the Scene which is also the Engine size */
	protected final Vector2i engineSize;
	/** Camera of the Scene */
	protected final Camera camera;
	/** Engine time */
	protected final EngineTime engineTime;
	
	/** Event pool */
	protected final SceneEventManager events;
	
	/** Scene Shaders */
	protected final ShaderManager shaders;
	/** Scene FrameBuffers */
	protected final FramebufferManager framebuffers;
	/** Scene Entities  */
	protected final EntityManager entities;
	/** Scene Behaviors */
	protected final BehaviorManager behaviors;
	/** Scene Systems */
	protected final SystemManager systems;

	
	/**
	 * Creates the Engine scene object containing all the scene Objects
	 * @param engineSize
	 */
	public EngineScene(Vector2i engineSize, EngineTime engineTime, ShaderManager shaders, Camera camera) {
		this.engineSize = engineSize;
		this.engineTime = engineTime;
		this.camera = camera;
		this.shaders = shaders;
		this.entities = new EntityManager();
		this.events = new SceneEventManager();
		this.framebuffers = new FramebufferManager();
		this.behaviors = new BehaviorManager();
		this.systems = new SystemManager(this.entities, this.behaviors, this.shaders, this.framebuffers, this.events, this.camera);
	}
	
	/**
	 * Destroy the scene by destroying gl component
	 */
	public void destroy() {
		shaders.destroy();
		framebuffers.destroy();
	}

	/**
	 * Add an entity, its children and all their behaviors to the Scene
	 * @param entity to add to the scene
	 */
	public final void addEntity(EngineEntity entity) {
		// Putting the entity into the entity map
		entities.add(entity);
		//System.out.println("Adding Entity " + entity.id());
		// Putting all its behavior to the behavior map
		ArrayList<EngineBehavior> b = entity.behaviors();
		for(int i = 0; i< b.size(); i++) {
			behaviors.add(b.get(i));
			if(b.get(i).generateSceneEvent()) {
				EngineSystem<? extends EngineBehavior> system = this.systems.get(b.get(i).type());
				if(system != null) {
					system.onAdd(b.get(i));;
				}
			}
		}
		// Putting all its children into the entity map using recursive call
		ArrayList<EngineEntity> children = entity.children();
		for(int i = 0; i< children.size(); i++) {
			addEntity(children.get(i)); 
		}
	}
	
	/**
	 * Remove an entity, its children and all their behaviors from the Scene
	 * @param entity to remove from the scene
	 */
	public final EngineEntity removeEntity(Integer id) {
		// Get the entity
		EngineEntity entity = entities.remove(id);
		// Putting all its behavior to the behavior map
		ArrayList<EngineBehavior> b  = entity.behaviors();
		for(int i = 0; i< b.size(); i++) {
			behaviors.remove(b.get(i));
			if(b.get(i).generateSceneEvent()) {
				EngineSystem<? extends EngineBehavior> system = this.systems.get(b.get(i).type());
				if(system != null) {
					system.onRemove(b.get(i));
				}
			}
		}
		// Putting all its children into the entity map using recursive call
		ArrayList<EngineEntity> children = entity.children();
		for(int i = 0; i< children.size(); i++) {
			removeEntity(children.get(i).id()); 
		}
		return entity;
	}
	
	/** Add a system to the system map */
	public void addSystem(EngineSystem<?> system) {
		systems.add(system);
	}
	
	public void addFramebuffer(EngineFramebuffer framebuffer) {
		this.framebuffers.add(framebuffer);
	}
	
	/** 
	 * Execute the system associated to the behavior 
	 * @param type associated to the system
	 * @return true if the system ran successfully
	 */
	public boolean executeSystem(BehaviorType type) {
		return systems.execute(type);
	}
	
	public void dispatchEvents() {
		while(!this.events.isEmpty()) {
			SceneEvent event = this.events.pollEvent();
			EngineSystem<?> system = systems.get(event.destination().type());
			if(system != null && event != null){
				system.pushEvent(event);
			}
		}
	}
	
	
	/** process camera movements 
	 * @param window the Engine window 
	 */
	public final void processCamera(long window) {
		camera.processInput(window, engineTime.getDeltaTime());
		camera.updateView();
	}
	
	/**
	 * Get the camera of the scene
	 * @return Camera of the scene
	 */
	public final Camera camera() { return this.camera; }

	/**
	 * Engine size getter
	 * @return the engine screen size as Vector2i
	 */
	public final Vector2i size() {
		return engineSize;
	}
	
	public ShaderManager shaders() {
		return this.shaders;
	}

	public EngineSystem<?> getSystem(BehaviorType type) {
		return systems.get(type);
	}

	

	
}
