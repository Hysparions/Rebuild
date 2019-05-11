package world.behavior;

public abstract class Behavior {

	protected BehaviorType type;
	//Boolean used to know if the apply method should run
	protected boolean isActive;
	
	public Behavior(BehaviorType type, boolean active) {
		this.type = type;
		this.isActive = active;
	}
	
	public BehaviorType getType() {
		return type;
	}
	
	public abstract void update();
	
	public boolean isActive() {return isActive;}
	public void activate() {this.isActive = true;}
	public void Disactivate() {this.isActive = false;}
}
