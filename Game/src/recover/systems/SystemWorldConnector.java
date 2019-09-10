package recover.systems;

import java.util.ArrayList;

import org.joml.Vector2f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.scene.EngineSystem;
import engine.scene.SceneEvent;
import engine.scene.SceneEventType;
import recover.behaviors.WorldConnector;

public class SystemWorldConnector extends EngineSystem<WorldConnector>{

	/**
	 * Constructor of the System handling World connectors
	 */
	public SystemWorldConnector() {
		super(BehaviorType.WORLD_CONNECTOR);
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
		WorldConnector connector = (WorldConnector)behavior;
		Vector2f buffer = new Vector2f(0.0f);
		ArrayList<EngineBehavior> connectorList = behaviorManager().list(BehaviorType.WORLD_CONNECTOR);
		boolean notify = false;
		for(int i = 0; i < connectorList.size(); i++) {
			
			if(entityManager().get(connectorList.get(i).parent()).getClass() == entityManager().get(connector.parent()).getClass()) {
				
				WorldConnector connector2 = (WorldConnector) connectorList.get(i);
				// Checking Right 
				buffer.set(connector.position().x+1, connector.position().y);
				if(connector2.position().equals(buffer)) {
					connector.setRight(connector2);
					connector2.setLeft(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Left 
				buffer.set(connector.position().x-1, connector.position().y);
				if(connector2.position().equals(buffer)) {
					connector.setLeft(connector2);
					connector2.setRight(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Back 
				buffer.set(connector.position().x, connector.position().y+1);
				if(connector2.position().equals(buffer)) {
					connector.setBack(connector2);
					connector2.setFront(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Back Right 
				buffer.set(connector.position().x+1, connector.position().y+1);
				if(connector2.position().equals(buffer)) {
					connector.setBackRight(connector2);
					connector2.setFrontLeft(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Back Left 
				buffer.set(connector.position().x-1, connector.position().y+1);
				if(connector2.position().equals(buffer)) {
					connector.setBackLeft(connector2);
					connector2.setFrontRight(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Front 
				buffer.set(connector.position().x, connector.position().y-1);
				if(connector2.position().equals(buffer)) {
					connector.setFront(connector2);
					connector2.setBack(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Front Right 
				buffer.set(connector.position().x+1, connector.position().y-1);
				if(connector2.position().equals(buffer)) {
					connector.setFrontRight(connector2);
					connector2.setBackLeft(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
				// Checking Front Left 
				buffer.set(connector.position().x-1, connector.position().y-1);
				if(connector2.position().equals(buffer)) {
					connector.setFrontLeft(connector2);
					connector2.setBackRight(connector);
					notify = true;
					for(EngineBehavior listener : connector2.listeners()) {
						this.eventManager().pushEvent(connector2, listener, SceneEventType.MODIFY);
					}
				}
			}
		}
		if(notify) {
			for(EngineBehavior listener : connector.listeners()) {
				this.eventManager().pushEvent(connector, listener, SceneEventType.MODIFY);
			}
		}
	}

	@Override
	protected void onRemove(EngineBehavior behavior) {
		// Unload all connected world connectors
		WorldConnector connector = (WorldConnector)behavior;
		if(connector.left() != null) {
			connector.left().setRight(null);
			for(EngineBehavior listener : connector.left().listeners()) {
				this.eventManager().pushEvent(connector.left(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.frontLeft() != null) {
			connector.frontLeft().setBackRight(null);
			for(EngineBehavior listener : connector.frontLeft().listeners()) {
				this.eventManager().pushEvent(connector.frontLeft(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.frontRight() != null) {
			connector.frontRight().setBackLeft(null);
			for(EngineBehavior listener : connector.frontRight().listeners()) {
				this.eventManager().pushEvent(connector.frontRight(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.right() != null) {
			connector.right().setLeft(null);
			for(EngineBehavior listener : connector.right().listeners()) {
				this.eventManager().pushEvent(connector.right(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.back() != null) {
			connector.back().setFront(null);
			for(EngineBehavior listener : connector.back().listeners()) {
				this.eventManager().pushEvent(connector.back(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.backLeft() != null) {
			connector.backLeft().setFrontRight(null);
			for(EngineBehavior listener : connector.backLeft().listeners()) {
				this.eventManager().pushEvent(connector.backLeft(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.backRight() != null) {
			connector.backRight().setFrontLeft(null);
			for(EngineBehavior listener : connector.backRight().listeners()) {
				this.eventManager().pushEvent(connector.backRight(), listener, SceneEventType.MODIFY);
			}
		}
		if(connector.front() != null) {
			connector.front().setBack(null);
			for(EngineBehavior listener : connector.front().listeners()) {
				this.eventManager().pushEvent(connector.front(), listener, SceneEventType.MODIFY);
			}
		}
		for(EngineBehavior listener : connector.listeners()) {
			this.eventManager().pushEvent(connector, listener, SceneEventType.DESTROY);
		}
	}

	@Override
	protected void onChange(SceneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRun() {
		// TODO Auto-generated method stub
		
	}
	
	

}
