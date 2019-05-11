package world.behavior;

import org.joml.Vector3f;

import world.biomes.Biome;

public class SpreadingBiome extends Behavior{

	private Location location;
	private Biome biome;
	
	public SpreadingBiome(Location location, Biome biome) {
		super(BehaviorType.SPREADINGBIOME, true);
		this.location = location;
		this.biome = biome;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public Vector3f getPosition() {
		return location.getPosition();
	}
	
	public Biome getBiome() {return biome;}
}
