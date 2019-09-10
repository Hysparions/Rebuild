package recover.behaviors;

import org.joml.Vector2f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;

/**
 * This Behavior is used to make connections between chunks
 * It is used and listened by the Behavior SpreadingBiome
 * @author louis
 *
 */
public class WorldConnector extends EngineBehavior{
	
	/** Position of the Behavior */
	private Vector2f position;
	
	/** Front Adjacent Behavior */
	private WorldConnector front;
	/** Front Left Adjacent Behavior */
	private WorldConnector frontLeft;
	/** Front Right Adjacent Behavior */
	private WorldConnector frontRight;
	/** Back Adjacent Behavior */
	private WorldConnector back;
	/** Back Left Adjacent Behavior */
	private WorldConnector backLeft;
	/** Back Right Adjacent Behavior */
	private WorldConnector backRight;
	/** Right Adjacent Behavior */
	private WorldConnector right;
	/** Left Adjacent Behavior */
	private WorldConnector left;
	

	/**
	 * Constructor of the chunk Behavior
	 * @param position of the world Connector
	 */
	public WorldConnector(Vector2f position) {
		super(BehaviorType.WORLD_CONNECTOR);
		this.position = position;
		this.front = null;
		this.frontLeft = null;
		this.frontRight = null;
		this.back = null;
		this.backLeft = null;
		this.backRight = null;
		this.right = null;
		this.left = null;
	}

	/** Getter for the position
	 * @return the position
	 */
	public Vector2f position() {
		return position;
	}
	
	/** Getter for the front chunk id
	 * @return the front id
	 */
	public WorldConnector front() {
		return front;
	}
	
	/** Set the front connector
	 * @param front connector to set
	 */
	public void setFront(WorldConnector front) {
		this.front = front;
	}
	
	/** Getter for the front Left chunk id
	 * @return the front id
	 */
	public WorldConnector frontLeft() {
		return frontLeft;
	}
	
	/** Set the front Left connector
	 * @param frontLeft connector to set
	 */
	public void setFrontLeft(WorldConnector frontLeft) {
		this.frontLeft = frontLeft;
	}

	/** Getter for the front Right chunk id
	 * @return the front Right id
	 */
	public WorldConnector frontRight() {
		return frontRight;
	}

	/** Set the front Right connector
	 * @param frontRight connector to set
	 */
	public void setFrontRight(WorldConnector frontRight) {
		this.frontRight = frontRight;
	}
	
	/** Getter for back  connector
	 * @return the back connector
	 */
	public WorldConnector back() {
		return back;
	}
	
	/** Set the back connector
	 * @param back connector to set
	 */
	public void setBack(WorldConnector back) {
		this.back = back;
	}
	
	/** Getter for back Left connector
	 * @return the back connector
	 */
	public WorldConnector backLeft() {
		return backLeft;
	}
	
	/** Set the back connector
	 * @param backLeft connector to set
	 */
	public void setBackLeft(WorldConnector backLeft) {
		this.backLeft = backLeft;
	}

	/** Getter for back right connector
	 * @return the back right connector
	 */
	public WorldConnector backRight() {
		return backRight;
	}

	/** Set the back Right connector
	 * @param backRight connector to set
	 */
	public void setBackRight(WorldConnector backRight) {
		this.backRight = backRight;
	}

	/** Getter for the right connector
	 * @return the right connector
	 */
	public WorldConnector right() {
		return right;
	}

	/** Set the right connector
	 * @param right connector to set
	 */
	public void setRight(WorldConnector right) {
		this.right = right;
	}

	/** Getter for the left connector
	 * @return the left connector
	 */
	public WorldConnector left() {
		return left;
	}

	/** Set the left connector
	 * @param left connector to set
	 */
	public void setLeft(WorldConnector left) {
		this.left = left;
	}

	@Override
	public boolean generateSceneEvent() {
		// Notify the adding of this to connect it on adding 
		return true;
	}

	
}
