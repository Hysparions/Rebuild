package recover.behaviors;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import recover.behaviors.model.TerrainModel;
import recover.entities.Chunk;
import recover.utils.Biome;

public class SpreadingBiome extends EngineBehavior{
	
	private final int WIDTH = Chunk.WIDTH *Chunk.SUB_CHUNK_NUMBER;

	/** Terrain model of the chunk containing the location */
	private TerrainModel terrain;
	/** Location of the Entity */ 
	private Location location;
	/** Biome spreaded by the behavior */
	private Biome biome;
	/** Radius of the Spreading */
	private int radius;
	/** Array of boolean used to know if the behavior interacts with nearby chunks */
	private boolean[] action;
	/** Time of creation of the entity */
	private double time;
	
	/**
	 * Constructor of the spreading Biome behavior
	 * @param terrain model on which the biome acts
	 * @param location of the Entity
	 * @param biome spreader
	 * @param radius of the algorithm
	 */
	public SpreadingBiome(TerrainModel terrain, Location location, Biome biome, int radius) {
		super(BehaviorType.SPREAD_BIOME);
		this.terrain = terrain;
		this.location = location;
		this.biome = biome;
		if(radius < 1) { radius = 1;}
		this.radius = radius;
		this.time = GLFW.glfwGetTime();
		this.action = new boolean[8];
		determineAction();
		spreadIntern();
		if(actExtern()) {
			terrain.connector().addListeners(this);
		}
	}

	/** @return true if the behavior interacts with front left chunk */
	public boolean frontLeft() {return action[0];}
	/** @return true if the behavior interacts with front  chunk */
	public boolean front() {return action[1];}
	/** @return true if the behavior interacts with front right chunk */
	public boolean frontRight() {return action[2];}
	/** @return true if the behavior interacts with left chunk */
	public boolean left() {return action[3];}
	/** @return true if the behavior interacts with right chunk */
	public boolean right() {return action[4];}
	/** @return true if the behavior interacts with back left chunk */
	public boolean backLeft() {return action[5];}
	/** @return true if the behavior interacts with back chunk */
	public boolean back() {return action[6];}
	/** @return true if the behavior interacts with back Right chunk */
	public boolean backRight() {return action[7];}
	
	/** @return true if the spreading biome behavior acts on extern chunks */
	public boolean actExtern() {
		for(int i = 0; i<8 ;i++) {
			if(action[i] == true) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines the chunk influenced by this behavior
	 */
	private void determineAction() {
		Vector2f chunkPos = terrain.connector().position();
		float minX = ((location.position().x()+1)/2) - (chunkPos.x()*WIDTH) - radius;
		float minZ = ((location.position().z()+1)/2) - (chunkPos.y()*WIDTH) - radius;
		
		float maxX = ((location.position().x()+1)/2) - (chunkPos.x()*WIDTH) + radius;
		float maxZ = ((location.position().z()+1)/2) - (chunkPos.y()*WIDTH) + radius;
		
		// Front Left
		if(minX <= 0 && minZ <= 0) { action[0] = true;} else { action[0] = false;}
		// Front
		if(((minX >= 0 && minX <=(WIDTH)) || (maxX >= 0 && maxX <=(WIDTH))) && minZ <= 0) { action[1] = true;}else { action[1] = false;}
		// Front Right
		if(maxX >= (WIDTH) && minZ <= 0) { action[2] = true;} else { action[2] = false;}
		// Left
		if(((minZ >= 0 && minZ <=(WIDTH)) || (maxZ >= 0 && maxZ <=(WIDTH))) && minX <= 0)  { action[3] = true;} else { action[3] = false;}
		// Right
		if(((minZ >= 0 && minZ <=(WIDTH)) || (maxZ >= 0 && maxZ <=(WIDTH))) && maxX >= (WIDTH))  { action[4] = true;} else { action[4] = false;}
		// Back Left
		if(minX >= (WIDTH) && maxZ <= 0)  { action[5] = true;} else { action[5] = false;}
		// Back
		if(((minX >= 0 && minX <=(WIDTH)) || (maxX >= 0 && maxX <=(WIDTH))) && maxZ >= (WIDTH))  { action[6] = true;} else { action[6] = false;}
		// Back Right
		if(maxX >= (WIDTH) && maxZ >= (WIDTH))  { action[7] = true;} else { action[7] = false;}
		
	}
	
	/**
	 * @return true if all the necessary chunks are here to run the extern system
	 */
	public boolean shouldRunExtern() {
		boolean shouldRun = true;
		if(this.frontLeft() && this.terrain.connector().frontLeft() == null) {shouldRun = false;}
		if(this.front() && this.terrain.connector().front() == null) {shouldRun = false;}
		if(this.frontRight() && this.terrain.connector().frontRight() == null) {shouldRun = false;}
		if(this.left() && this.terrain.connector().left() == null) {shouldRun = false;}
		if(this.right() && this.terrain.connector().right() == null) {shouldRun = false;}
		if(this.backLeft() && this.terrain.connector().backLeft() == null) {shouldRun = false;}
		if(this.back() && this.terrain.connector().back() == null) {shouldRun = false;}
		if(this.backRight() && this.terrain.connector().backRight() == null) {shouldRun = false;}
		return shouldRun;
	}
	
	public void spreadExtern(TerrainModel extern) {
		int xPos = (int) (((location.position().x()+1)/2) - (extern.connector().position().x()*WIDTH));
		int zPos = (int) (((location.position().z()+1)/2) - (extern.connector().position().y()*WIDTH));
		
		int minX = (int) (xPos - radius > 0.0f ? xPos - radius : 0.0f);
		int minZ = (int) (zPos - radius > 0.0f ? zPos - radius : 0.0f);
		
		int maxX = xPos + radius < WIDTH ? xPos + radius : WIDTH;
		int maxZ = zPos + radius < WIDTH ? zPos + radius : WIDTH;
		
		float influence, diffHeight;
		float xSquare, zSquare;
		for(int x = minX ; x<maxX+1 ; x++ ) {
			for(int z = minZ ; z<maxZ+1 ; z++ ) {
				xSquare = x<xPos?((x-xPos)*(x-xPos)):((Math.abs(x-xPos)+1)*(Math.abs(x-xPos)+1));
				zSquare = z<zPos?((z-zPos)*(z-zPos)):((Math.abs(z-zPos)+1)*(Math.abs(z-zPos)+1));
				// Calculating influence
				diffHeight = Math.abs(extern.point(x, z).y()-location.position().y());
				diffHeight = diffHeight < 1.0f ? 1.0f : 1.5f*diffHeight;
				influence = (radius - (float)Math.sqrt(xSquare+zSquare) + (float) Math.sqrt(2)) / radius ;
				influence = influence < 0.0f ? 0.0f : influence/diffHeight;
				extern.addInfluence(x, z, (float)time, influence, biome);
			}			
		}
		
		for(int x = minX-1 ; x<maxX+2 ; x++ ) {
			for(int z = minZ-1 ; z<maxZ+2 ; z++ ) {
				if(x >= 0 && x <= WIDTH && z >= 0 && z <=WIDTH) {
					extern.calculateColor(x, z);
				}
			}			
		}
		
	}
	
	public void unSpreadExtern(TerrainModel extern) {
		int xPos = (int) (((location.position().x()+1)/2) - (extern.connector().position().x()*WIDTH));
		int zPos = (int) (((location.position().z()+1)/2) - (extern.connector().position().y()*WIDTH));
		
		int minX = (int) (xPos - radius > 0.0f ? xPos - radius : 0.0f);
		int minZ = (int) (zPos - radius > 0.0f ? zPos - radius : 0.0f);
		
		int maxX = xPos + radius < WIDTH ? xPos + radius : WIDTH;
		int maxZ = zPos + radius < WIDTH ? zPos + radius : WIDTH;
		
		float influence, diffHeight;
		float xSquare, zSquare;
		for(int x = minX ; x<maxX+1 ; x++ ) {
			for(int z = minZ ; z<maxZ+1 ; z++ ) {
				xSquare = x<xPos?((x-xPos)*(x-xPos)):((Math.abs(x-xPos)+1)*(Math.abs(x-xPos)+1));
				zSquare = z<zPos?((z-zPos)*(z-zPos)):((Math.abs(z-zPos)+1)*(Math.abs(z-zPos)+1));
				// Calculating influence
				diffHeight = Math.abs(extern.point(x, z).y()-location.position().y());
				diffHeight = diffHeight < 1.0f ? 1.0f : 1.5f*diffHeight;
				influence = (radius - (float)Math.sqrt(xSquare+zSquare) + (float) Math.sqrt(2)) / radius ;
				influence = influence < 0.0f ? 0.0f : influence/diffHeight;
				extern.removeInfluence(x, z, (float)time, influence, biome);
			}			
		}
		
		for(int x = minX-1 ; x<maxX+2 ; x++ ) {
			for(int z = minZ-1 ; z<maxZ+2 ; z++ ) {
				if(x >= 0 && x <= WIDTH && z >= 0 && z <=WIDTH) {
					extern.calculateColor(x, z);
				}
			}			
		}
		
	}
	
	public void spreadIntern() {
		Vector2f chunkPos = terrain.connector().position();
		int xPos = (int) (((location.position().x()+1)/2) - (chunkPos.x()*WIDTH));
		int zPos = (int) (((location.position().z()+1)/2) - (chunkPos.y()*WIDTH));
		float influence, diffHeight;
		float xSquare, zSquare;
		for(int x = -radius-1 ; x<radius+1 ; x++ ) {
			for(int z = -radius-1 ; z<radius+1 ; z++ ) {
				if(xPos+x >= 0 && xPos+x <= WIDTH && zPos+z >= 0 && zPos+z <=WIDTH) {
					xSquare = x<0?(x*x):((x+1)*(x+1));
					zSquare = z<0?(z*z):((z+1)*(z+1));
					// Calculating influence
					diffHeight = Math.abs(terrain.point(xPos+x, zPos+z).y()-location.position().y());
					diffHeight = diffHeight < 1.0f ? 1.0f : 1.5f*diffHeight;
					influence = (radius - (float)Math.sqrt(xSquare+zSquare) + (float) Math.sqrt(2)) / radius ;
					influence = influence < 0.0f ? 0.0f : influence/diffHeight;
					terrain.addInfluence(xPos+x, zPos+z, (float)time, influence, biome);
				}
			}			
		}

		
	}
	
	/**
	 * @return the time origin where the entity is created 
	 */
	public double time() {
		return time;
	}
	
	@Override
	public boolean generateSceneEvent() {
		// Do not generate Scene Event
		return false;
	}
	
	/**
	 * @return the chunk position vector of the spreading biome entity
	 */
	public Vector2f position() {
		return terrain.connector().position();
	}

	/**
	 * @return the terrain containing the entity
	 */
	public TerrainModel terrain() {
		return terrain;
	}

}
