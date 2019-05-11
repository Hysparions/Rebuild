package world.systems;

import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import world.Chunk;
import world.behavior.SpreadingBiome;
import world.biomes.Biome;

/*
 *This Class provides all IA algorithm in relation with the Biome spreading Behavior
 *The Run Method launch the algorthm and is used by the general Systems Class that holds every systems 
 */


public class SpreadingBiomeSystem {

	//Utils for the algorithm
	private static int chunkSize = Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH+1;
	private Vector2f buffer;
	private Chunk currentChunk = null;
	
	public SpreadingBiomeSystem(){
		buffer  = new Vector2f();
	}
	
	//This Method runs the Spreading biome Behavior passed in parameters to the function
	public void run(SpreadingBiome spreadingBiome, Map<Vector2f, Chunk> chunks, float time) {
		
		//Getting the position of the entity via the spreadingBiome behavior passed to the run method
		Vector3f position = spreadingBiome.getPosition();
		//Transforming Absolute position into Tiled position
		int posX = (int)(position.x/2);
		int posZ = (int)(position.z/2);
		
		//Used to store position values over iteration
		int x;
		int height = (int)position.y;
		int z;
		//Used to store spreading strength amount over iteration
		float amount;
		//Used to store the height delta
		float delta;
		
		//Looping on the Biome strength to actualize the Terrain Color Data
		for(int i = -spreadingBiome.getBiome().strenght; i < spreadingBiome.getBiome().strenght; i++) {
			for(int j = -spreadingBiome.getBiome().strenght; j < spreadingBiome.getBiome().strenght; j++) {
				//Calculating the position of the Tile
				x = i+posX;
				z = j+posZ;
				//Calculating the Chunk postition
				buffer.set((x>=0 ? x/chunkSize : (x/chunkSize)-1), (z>=0 ? z/chunkSize : (z/chunkSize)-1));
				x = (x>=0 ? x%chunkSize : chunkSize-1+(x%chunkSize));
				z = (z>=0 ? z%chunkSize : chunkSize-1+(z%chunkSize));
				
				//Checking the existence of the chunk in which the biome should be spread
				if(chunks.containsKey(buffer)) {
					currentChunk = chunks.get(buffer);
					currentChunk.setTerrainUpdate(true);
					delta = Math.abs(height - currentChunk.getTerrainData(x, z).getHeight());
					if (delta == 0) {delta = 1;}
					amount = interpolation((float)Math.sqrt((i*i)+(j*j)), spreadingBiome.getBiome().strenght)/(delta*delta);
					if(amount != 0) {
						addInfluence(currentChunk, x, z, amount, spreadingBiome.getBiome(), time);
					}
				}
			}	
		}
		
		//After Calculations, disactivate the spreadingBiome Behavior 
		spreadingBiome.Disactivate();
	}
	
	private float interpolation(float length, int strenght) {
		length = 1.0f - (length/strenght);
		if(length < 0) {length = 0;}
		return length;
	}
	
	private void addInfluence(Chunk currentChunk, int x, int z, double d, Biome biome, float time) {
		
		//Chunk Border Modification Test
		
		//if x = 0, setting the terrainData of the left chunk
		if(x == 0) {
			if(currentChunk.getLeft() != null) { currentChunk.getLeft().getTerrainData(chunkSize-1, z).addInfluence((float)d, biome, time); }
		}
		//if x = chunkSize-1 setting the terrainData of the right Chunk
		else if(x == chunkSize-1) {
			if(currentChunk.getRight() != null) { currentChunk.getRight().getTerrainData(0, z).addInfluence((float)d, biome, time); }
		}
		//if z = 0, setting the terrainData of the back chunk
		if(z == chunkSize-1) {
			if(currentChunk.getBack() != null) { currentChunk.getBack().getTerrainData(x, 0).addInfluence((float)d, biome, time); }
		}
		//if z = chunkSize-1 setting the terrainData of the front Chunk
		else if(z == 0) {
			if(currentChunk.getFront() != null) { currentChunk.getFront().getTerrainData(x, chunkSize-1).addInfluence((float)d, biome, time); }
		}
		
		
		//Finally change terrain data in the current chunk
		currentChunk.getTerrainData(x, z).addInfluence((float)d, biome, time);
		//CurrentChunk.getTerrain().updateBuffer(i, j, CurrentChunk.getTerrainData(i, j), Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH+1);
	}
	
}
