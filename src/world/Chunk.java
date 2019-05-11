package world;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.GeometryUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import world.water.WaterBuffers;
import world.water.WaterMesh;
import world.water.WaterVertex;
import world.batch.StaticBatch;
import world.entities.Entity;
import world.models.PolyModel;
import world.terrain.TerrainMesh;
import world.terrain.TerrainData;

public class Chunk {

	public static final int ENTITY_RADIUS = 1;				//Distance From the Grid where entities can spawn
	public static final int SUB_CHUNK_NUMBER = 25;			//Number of sub generated part of the chunk 
	public static final int WIDTH = 5;						//Number of point per sub Chunk
	public static final int ENTITY_MAX_HEIGHT = 60;			//Height limiting entity spawning
	public static final int HEIGHT = 100;					//Height max
	public static final int WATER_LEVEL = HEIGHT/3+4;		//Height Level of the water level
	public static final int DEFAULT_CAPACITY = 10000000;	//Default capacity of the entity buffer
	
	//Chunk Mesh
	private TerrainMesh terrain;
	private WaterMesh Water;
	
	//Entity List
	private List<Entity> entities;
	
	//Entity Static Batch
	private long vegetationCapacity;
	private int vegetationSize; 
	private StaticBatch Vegetation;
	
	//Chunk Buffers
	FloatBuffer waterVertices;
	IntBuffer waterIndices;
	
	//Position
	public Vector2f position;
	
	//Adjacent Chunks
	private Chunk front;
	private Chunk back;
	private Chunk right;
	private Chunk left;

	//private float[][] points;
	private TerrainData[][] terrainData;
	
	//Utils
	private boolean terrainUpdate;
	
	public Chunk(TerrainData[][] terrainData, Vector2f pos, List<Entity> Entities) {

		//Meshes
		terrain = new TerrainMesh((SUB_CHUNK_NUMBER*WIDTH)*(SUB_CHUNK_NUMBER*WIDTH)*6*TerrainMesh.VERTEX_SIZE_IN_FLOAT, (SUB_CHUNK_NUMBER*WIDTH)*(SUB_CHUNK_NUMBER*WIDTH)*6);
		Water = new WaterMesh();
		
		//Entity List
		this.entities = Entities;
		
		//Vegetation Batch
		vegetationCapacity = DEFAULT_CAPACITY;
		vegetationSize = 0;
	    Vegetation = new StaticBatch(vegetationCapacity, vegetationSize);
	    
		//Height Grid
		this.terrainData = terrainData;
		position = pos;
		
		//VerticesBuffer
		waterVertices = null;
		waterIndices = null;
		
		//Adjacent Chunks
		front = null;
		back = null;
		right = null;
		left = null;
		
		//Utils
		terrainUpdate = false;
	}
	

	/**
	 *  This method generate the random height map for the chunk and make smartly spawns entities on it with randomness
	 * @param position
	 * @param buffer
	 * @param heightGen
	 * @param staticModels
	 * @return A whole new generated chunk with all data included but the terrain mesh isn't generated yet
	 */
	public static Chunk generateFromSeed(Vector2f position, Vector2f buffer, HeightGenerator heightGen, Map<String, PolyModel> staticModels) {
		//Generate a whole new Chunk
		int chunkWidth = Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH;
		TerrainData[][] terrainData  = new TerrainData[chunkWidth+1][chunkWidth+1];
		//Creating containing Entities
		List<Entity> Entities = new ArrayList<>();
		
		//Generating Chunk Height Map
		float[][] subChunk = null;
		for(int i = 0; i<Chunk.SUB_CHUNK_NUMBER; i++) {
			for(int j = 0; j<Chunk.SUB_CHUNK_NUMBER; j++) {
				
				//Calculating the sub Chunk Position
				float xOffset = position.x * Chunk.SUB_CHUNK_NUMBER + i;
				float zOffset = position.y * Chunk.SUB_CHUNK_NUMBER + j;
				
				buffer.set(xOffset, zOffset);
				subChunk = heightGen.getPoints(buffer);
				
				//Putting the sub generated height into the whole terraindata array
				for(int x = 0; x < Chunk.WIDTH + 1 ; x++) {					
					for(int y = 0; y < Chunk.WIDTH + 1 ; y++) {
						terrainData[i*Chunk.WIDTH + x][j*Chunk.WIDTH + y] = new TerrainData();
						terrainData[i*Chunk.WIDTH + x][j*Chunk.WIDTH + y].setFirstHeight((int)subChunk[x][y]);
					}
				}
			}
		}
		
		//Creating permutation array
		int[] array = new int[(2*ENTITY_RADIUS)+1];
		for(int i = 0; i < (2*ENTITY_RADIUS)+1 ; i++) {array[i] = i-ENTITY_RADIUS;}
		
		int x, y, xOff, yOff, posX, posY;
		//Generating Chunks Entities
		for(int i = ENTITY_RADIUS+1; i < chunkWidth - Chunk.ENTITY_RADIUS-1; i += 2*Chunk.ENTITY_RADIUS+2) {
			for(int j = ENTITY_RADIUS+1; j < chunkWidth - Chunk.ENTITY_RADIUS-1; j += 2*Chunk.ENTITY_RADIUS+2) {
				x = (int)position.x*2*chunkWidth+(2*i);
				y = (int)position.y*2*chunkWidth+(2*j);
				xOff = array[Math.abs(x*y*i*212323019)%13%(2*Chunk.ENTITY_RADIUS+1)];
				yOff = array[Math.abs(x*y*j*129387217)%7%(2*Chunk.ENTITY_RADIUS+1)];
				posX = i+xOff;
				posY = j+yOff;
				if(terrainData[posX][posY].getHeight() > Chunk.WATER_LEVEL && terrainData[posX][posY].getHeight() < Chunk.ENTITY_MAX_HEIGHT) {
					//System.out.println("Generation of Grass at " + (x+xOff)+ " " +xOff + " " + (y+yOff)+" " +yOff);
					if(		terrainData[posX+1][posY].getHeight() == terrainData[posX][posY].getHeight()
							&& terrainData[posX-1][posY].getHeight() == terrainData[posX][posY].getHeight()
							&& terrainData[posX+1][posY+1].getHeight() == terrainData[posX][posY].getHeight() 
							&& terrainData[posX+1][posY-1].getHeight() == terrainData[posX][posY].getHeight()
							&& terrainData[posX][posY+1].getHeight() == terrainData[posX][posY].getHeight()
							&& terrainData[posX][posY-1].getHeight() == terrainData[posX][posY].getHeight() 
							&& terrainData[posX-1][posY+1].getHeight() == terrainData[posX][posY].getHeight() 
							&& terrainData[posX-1][posY-1].getHeight() == terrainData[posX][posY].getHeight())
					if(terrainData[posX][posY].getHeight()%2 == 0) {
						Entities.add(Entity.createStaticEntityAt(new Vector3f((x+(2*xOff)), terrainData[i+xOff][j+yOff].getHeight(),  (y+(2*yOff))), x*y, "Oak", staticModels ));
					}else {Entities.add(Entity.createStaticEntityAt(new Vector3f((x+(2*xOff)), terrainData[i+xOff][j+yOff].getHeight(),  (y+(2*yOff))), x*y, "Grass", staticModels ));}
					
				}
			}
		}
		
		return new Chunk(terrainData, position, Entities);
	}
	
	public void generateTerrainBuffer() {
		
		//First generate the terrainMesh FloatBuffer
		FloatBuffer terrainVertices = terrain.getVertexBuffer();
		//System.out.println(terrainVertices.size)
		IntBuffer terrainIndices = terrain.getIndicesBuffer();
		
		Vector3f pointA = new Vector3f();
		Vector3f pointB = new Vector3f();
		Vector3f pointC = new Vector3f();
		Vector3f normal = new Vector3f();
		Vector3f oldColor = new Vector3f(0.5f);
		Vector3f newColor = new Vector3f(0.5f);
		float xOffset, zOffset;
		int vertexCount = 0;
		for(int x = 0; x < SUB_CHUNK_NUMBER*WIDTH ; x++) {
				for(int z = 0; z < SUB_CHUNK_NUMBER*WIDTH ; z++) {
					
					//Calculating tile position offset
					xOffset = (position.x * 2 * SUB_CHUNK_NUMBER*WIDTH) + (x * 2);
					zOffset = (position.y * 2 * SUB_CHUNK_NUMBER*WIDTH) + (z * 2);
					if(Math.abs((xOffset/2)%2) == Math.abs((zOffset/2)%2))
					{
					//First Triangle
						
						//Calculating Color
						oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z].getOldColor().x)/3.0f;
						oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z].getOldColor().y)/3.0f;
						oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z].getOldColor().z)/3.0f;
						newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z].getNewColor().x)/3.0f;
						newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z].getNewColor().y)/3.0f;
						newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z].getNewColor().z)/3.0f;
						//Calculating Normal
						pointA.set(xOffset, terrainData[x][z].getHeight(), zOffset);
						pointB.set(xOffset, terrainData[x][z+1].getHeight(), zOffset+ 2.0f);
						pointC.set(xOffset + 2.0f, terrainData[x+1][z].getHeight(), zOffset);
						GeometryUtils.normal(pointA, pointB, pointC, normal);
						
						//First Vertex
						addPointToTerrainBuffer( pointA, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Second Vertex
						addPointToTerrainBuffer( pointB, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Third Vertex
						addPointToTerrainBuffer( pointC, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						
					//Second Triangle
						
						//Calculating Color
						oldColor.x =(terrainData[x+1][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
						oldColor.y =(terrainData[x+1][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
						oldColor.z =(terrainData[x+1][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
						newColor.x =(terrainData[x+1][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z+1].getNewColor().x)/3.0f;
						newColor.y =(terrainData[x+1][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z+1].getNewColor().y)/3.0f;
						newColor.z =(terrainData[x+1][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z+1].getNewColor().z)/3.0f;
					
						//Calculating Normal
						pointA.set(xOffset + 2.0f, terrainData[x+1][z].getHeight(), zOffset);
						pointB.set(xOffset, terrainData[x][z+1].getHeight(), zOffset+ 2.0f);
						pointC.set(xOffset + 2.0f, terrainData[x+1][z+1].getHeight(),zOffset+ 2.0f);
						GeometryUtils.normal(pointA, pointB, pointC, normal);
						
						//First Vertex
						addPointToTerrainBuffer( pointA, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Second Vertex
						addPointToTerrainBuffer( pointB, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Third Vertex
						addPointToTerrainBuffer( pointC, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						

					}
					else
					{
						oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
						oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
						oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
						newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z+1].getNewColor().x)/3.0f;
						newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z+1].getNewColor().y)/3.0f;
						newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z+1].getNewColor().z)/3.0f;
						
						//First Triangle
						pointA.set((float)xOffset, terrainData[x][z].getHeight(), (float)zOffset);
						pointB.set((float)xOffset, terrainData[x][z+1].getHeight(), (float)zOffset+ 2.0f);
						pointC.set((float)xOffset + 2.0f, terrainData[x+1][z+1].getHeight(), (float)zOffset+ 2.0f);
						GeometryUtils.normal(pointA, pointB, pointC, normal);
						
						//First Vertex
						addPointToTerrainBuffer( pointA, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Second Vertex
						addPointToTerrainBuffer( pointB, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Third Vertex
						addPointToTerrainBuffer( pointC, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						
						oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x+1][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
						oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x+1][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
						oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x+1][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
						newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x+1][z+1].getNewColor().x + terrainData[x+1][z].getNewColor().x)/3.0f;
						newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x+1][z+1].getNewColor().y + terrainData[x+1][z].getNewColor().y)/3.0f;
						newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x+1][z+1].getNewColor().z + terrainData[x+1][z].getNewColor().z)/3.0f;
						
						//Second Triangle
						pointA.set((float)xOffset, terrainData[x][z].getHeight(), (float)zOffset);
						pointB.set((float)xOffset + 2.0f, terrainData[x+1][z+1].getHeight(), (float)zOffset+ 2.0f);
						pointC.set((float)xOffset + 2.0f, terrainData[x+1][z].getHeight(), (float)zOffset);
						GeometryUtils.normal(pointA, pointB, pointC, normal);
						
						//First Vertex
						addPointToTerrainBuffer( pointA, normal, oldColor, newColor, 0.0f, terrainVertices);					
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Second Vertex
						addPointToTerrainBuffer( pointB, normal, oldColor, newColor, 0.0f, terrainVertices);
						terrainIndices.put(vertexCount);
						vertexCount++;
						//Third Vertex
						addPointToTerrainBuffer( pointC, normal, oldColor, newColor, 0.0f, terrainVertices);			
						terrainIndices.put(vertexCount);
						vertexCount++;
					}
				}
		}
		terrainVertices.flip();
		terrainIndices.flip();
	}
	
	public void updateTerrainBuffer() {
		//First generate the terrainMesh FloatBuffer
				FloatBuffer terrainVertices = terrain.getVertexBuffer();
				
				Vector3f oldColor = new Vector3f(0.5f);
				Vector3f newColor = new Vector3f(0.5f);
				float xOffset, zOffset;
				int bufPos =0;
				for(int x = 0; x < SUB_CHUNK_NUMBER*WIDTH ; x++) {
						for(int z = 0; z < SUB_CHUNK_NUMBER*WIDTH ; z++) {
							//Calculating tile position offset
							xOffset = (position.x * 2 * SUB_CHUNK_NUMBER*WIDTH) + (x * 2);
							zOffset = (position.y * 2 * SUB_CHUNK_NUMBER*WIDTH) + (z * 2);
							
							if(Math.abs((xOffset/2)%2) == Math.abs((zOffset/2)%2))
							{
							//First Triangle
								
								oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z].getOldColor().x)/3.0f;
								oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z].getOldColor().y)/3.0f;
								oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z].getOldColor().z)/3.0f;
								newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z].getNewColor().x)/3.0f;
								newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z].getNewColor().y)/3.0f;
								newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z].getNewColor().z)/3.0f;
								
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								
							//Second Triangle
								
								//Calculating Color
								oldColor.x =(terrainData[x+1][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
								oldColor.y =(terrainData[x+1][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
								oldColor.z =(terrainData[x+1][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
								newColor.x =(terrainData[x+1][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z+1].getNewColor().x)/3.0f;
								newColor.y =(terrainData[x+1][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z+1].getNewColor().y)/3.0f;
								newColor.z =(terrainData[x+1][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z+1].getNewColor().z)/3.0f;
								
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
							}
							else
							{
							//First Triangle
								
								//Calculating Color
								oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
								oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
								oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
								newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x][z+1].getNewColor().x + terrainData[x+1][z+1].getNewColor().x)/3.0f;
								newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x][z+1].getNewColor().y + terrainData[x+1][z+1].getNewColor().y)/3.0f;
								newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x][z+1].getNewColor().z + terrainData[x+1][z+1].getNewColor().z)/3.0f;
								
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								
							//Second Triangle
								
								//Calculating Color	
								oldColor.x =(terrainData[x][z].getOldColor().x + terrainData[x+1][z+1].getOldColor().x + terrainData[x+1][z+1].getOldColor().x)/3.0f;
								oldColor.y =(terrainData[x][z].getOldColor().y + terrainData[x+1][z+1].getOldColor().y + terrainData[x+1][z+1].getOldColor().y)/3.0f;
								oldColor.z =(terrainData[x][z].getOldColor().z + terrainData[x+1][z+1].getOldColor().z + terrainData[x+1][z+1].getOldColor().z)/3.0f;
								newColor.x =(terrainData[x][z].getNewColor().x + terrainData[x+1][z+1].getNewColor().x + terrainData[x+1][z].getNewColor().x)/3.0f;
								newColor.y =(terrainData[x][z].getNewColor().y + terrainData[x+1][z+1].getNewColor().y + terrainData[x+1][z].getNewColor().y)/3.0f;
								newColor.z =(terrainData[x][z].getNewColor().z + terrainData[x+1][z+1].getNewColor().z + terrainData[x+1][z].getNewColor().z)/3.0f;
								
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
								updateTerrainBuffer(bufPos,oldColor, newColor, terrainData[x][z].getTime(), terrainVertices);
								bufPos+=13;
							}
						}
				}
	}
	
	public void generateWaterBuffer() {

		//Buffer container for vertex data while the number of float in the vertexData buffer isn't known
		List<WaterVertex> vertexBuffer = new ArrayList<>();
		List<Integer> indiceBuffer = new ArrayList<>();
		Vector2f pointA = new Vector2f();
		Vector2f pointB = new Vector2f();
		Vector2f pointC = new Vector2f();
		
		for(int x = 0; x < SUB_CHUNK_NUMBER*WIDTH ; x++) {
			for(int z = 0; z < SUB_CHUNK_NUMBER*WIDTH ; z++) {
				
				//Calculating tile position offset
				float xOffset = (position.x * 2 * SUB_CHUNK_NUMBER*WIDTH) + (x * 2);
				float zOffset = (position.y * 2 * SUB_CHUNK_NUMBER*WIDTH) + (z * 2);
		
					//Calculating if water Tile should appear.
					if(terrainData[x][z].getHeight()+0.5f <= WATER_LEVEL || terrainData[x+1][z].getHeight()+0.5f <= WATER_LEVEL || terrainData[x][z+1].getHeight()+0.5f <= WATER_LEVEL || terrainData[x+1][z+1].getHeight()+0.5f <= WATER_LEVEL) {
						if( (xOffset/2)%2 == (zOffset/2)%2 ) {
							//First Triangle
							pointA.set((float)xOffset, (float)zOffset);
							pointB.set((float)xOffset,  (float)zOffset+ 2.0f);
							pointC.set((float)xOffset + 2.0f, (float)zOffset);
							
							WaterVertex AA = new WaterVertex(pointA, pointB, pointC, terrainData[x][z].getHeight());
							WaterVertex AB = new WaterVertex(pointB, pointC, pointA, terrainData[x][z+1].getHeight());
							WaterVertex AC = new WaterVertex(pointC, pointA, pointB, terrainData[x+1][z].getHeight());
							
							//Second Triangle
							pointA.set((float)xOffset + 2.0f,  (float)zOffset);
							pointB.set((float)xOffset, (float)zOffset+ 2.0f);
							pointC.set((float)xOffset + 2.0f,  (float)zOffset+ 2.0f);
							
							WaterVertex BA = new WaterVertex(pointA, pointB, pointC, terrainData[x+1][z].getHeight());
							WaterVertex BB = new WaterVertex(pointB, pointC, pointA, terrainData[x][z+1].getHeight());
							WaterVertex BC = new WaterVertex(pointC, pointA, pointB, terrainData[x+1][z+1].getHeight());
							
							int vertexCount = vertexBuffer.size();
							vertexBuffer.add(AA);
							vertexBuffer.add(AB);
							vertexBuffer.add(AC);
							vertexBuffer.add(BA);
							vertexBuffer.add(BB);
							vertexBuffer.add(BC);

							indiceBuffer.add(vertexCount);
							indiceBuffer.add(vertexCount+1);
							indiceBuffer.add(vertexCount+2);
							indiceBuffer.add(vertexCount+3);
							indiceBuffer.add(vertexCount+4);
							indiceBuffer.add(vertexCount+5);
						}
						else
						{
							//First Triangle
							pointA.set((float)xOffset,  (float)zOffset);
							pointB.set((float)xOffset, (float)zOffset+ 2.0f);
							pointC.set((float)xOffset + 2.0f, (float)zOffset+ 2.0f);
							
							WaterVertex AA = new WaterVertex(pointA, pointB, pointC, terrainData[x][z].getHeight());
							WaterVertex AB = new WaterVertex(pointB, pointC, pointA, terrainData[x][z+1].getHeight());
							WaterVertex AC = new WaterVertex(pointC, pointA, pointB, terrainData[x+1][z+1].getHeight());
							
							//Second Triangle
							pointA.set((float)xOffset, (float)zOffset);
							pointB.set((float)xOffset + 2.0f, (float)zOffset+ 2.0f);
							pointC.set((float)xOffset + 2.0f, (float)zOffset);
							
							WaterVertex BA = new WaterVertex(pointA, pointB, pointC, terrainData[x][z].getHeight());
							WaterVertex BB = new WaterVertex(pointB, pointC, pointA, terrainData[x+1][z+1].getHeight());
							WaterVertex BC = new WaterVertex(pointC, pointA, pointB, terrainData[x+1][z].getHeight());
							
							int vertexCount = vertexBuffer.size();
							vertexBuffer.add(AA);
							vertexBuffer.add(AB);
							vertexBuffer.add(AC);
							vertexBuffer.add(BA);
							vertexBuffer.add(BB);
							vertexBuffer.add(BC);
							
							indiceBuffer.add(vertexCount);
							indiceBuffer.add(vertexCount+1);
							indiceBuffer.add(vertexCount+2);
							indiceBuffer.add(vertexCount+3);
							indiceBuffer.add(vertexCount+4);
							indiceBuffer.add(vertexCount+5);
						}
					}
				}
		}
		//Generating the floatBuffer containing solid tiles data
		waterVertices = BufferUtils.createFloatBuffer(vertexBuffer.size()*7);
		for(int i = 0; i< vertexBuffer.size(); i++) {
			waterVertices.put(vertexBuffer.get(i).position.x);
			waterVertices.put(vertexBuffer.get(i).position.y);
			waterVertices.put(vertexBuffer.get(i).position.z);
			waterVertices.put(vertexBuffer.get(i).point1.x);
			waterVertices.put(vertexBuffer.get(i).point1.y);
			waterVertices.put(vertexBuffer.get(i).point2.x);
			waterVertices.put(vertexBuffer.get(i).point2.y);
		}
		waterVertices.flip();
		
	
		//Generating the floatBuffer containing solid tiles data
		waterIndices = BufferUtils.createIntBuffer(indiceBuffer.size());
		for(int i = 0; i< indiceBuffer.size(); i++) {
			waterIndices.put(indiceBuffer.get(i));
		}
		waterIndices.flip();

		
	}

	public void generateBuffers() {
		this.generateTerrainBuffer();
		this.generateWaterBuffer();
	}
	

	
	public void generateMesh() {
		terrain.Generate();
		Water.Generate(waterVertices, waterIndices);
	}
	
	public void destroy() {
		//Destroy Mesh
		terrain.destroy();
		Water.destroy();
		Vegetation.destroy();
	}
	
	public void renderSolid() {
		terrain.draw();
	}
	
	public void renderWater( WaterBuffers waterTextures) {

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, waterTextures.getReflectionTextureID());
		Water.draw();
		
	}
	
	public void renderVegetation() {
		Vegetation.render();
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void addEntityModelsInBatch() {
		for(int i = 0 ; i < entities.size(); i++) {
			entities.get(i).addModels(Vegetation);
		}
		//Entities.get(0).removeModel(Vegetation);
	}
	
	private void addPointToTerrainBuffer(Vector3f point, Vector3f normal, Vector3f oldColor, Vector3f newColor, float time, FloatBuffer terrainVertices) {
		terrainVertices.put(point.x);		terrainVertices.put(point.y);		terrainVertices.put(point.z);	//Position
		terrainVertices.put(normal.x);		terrainVertices.put(normal.y);		terrainVertices.put(normal.z);	//Normal
		terrainVertices.put(oldColor.x);	terrainVertices.put(oldColor.y);	terrainVertices.put(oldColor.z);//OldColor
		terrainVertices.put(newColor.x);	terrainVertices.put(newColor.y);	terrainVertices.put(newColor.z);//NewColor
		terrainVertices.put(5.0f);	//Time	
	}
	
	public void updateTerrainBuffer(int bufPos, Vector3f oldColor, Vector3f newColor, float time, FloatBuffer terrainVertices) {
		terrainVertices.put(bufPos+6, oldColor.x);	terrainVertices.put(bufPos+7, oldColor.y);	terrainVertices.put(bufPos+8, oldColor.z);
		terrainVertices.put(bufPos+9, newColor.x);	terrainVertices.put(bufPos+10, newColor.y);	terrainVertices.put(bufPos+11, newColor.z);
		terrainVertices.put(bufPos+12, time);	
	}
	
	//Getter | Setter
	public void setFront(Chunk c) { front = c; };
	public Chunk getFront() { return front; };
	public void setBack(Chunk c) { back = c; };
	public Chunk getBack() { return back; };
	public void setRight(Chunk c) { right = c; };
	public Chunk getRight() { return right; };
	public void setLeft(Chunk c) { left = c; };
	public Chunk getLeft() { return left; };
	public float getHeight(int x, int y) {return terrainData[x][y].getHeight();}
	public StaticBatch getStaticBatch() { return Vegetation;}
	public List<Entity> getEntities() {return entities;}
	public TerrainData getTerrainData(int i, int j) {return terrainData[i][j];}
	public TerrainData[][] getTerrainData() {return terrainData;}
	public TerrainMesh getTerrain() {return terrain;}
	public void setTerrainUpdate(boolean b) { terrainUpdate = b;}
	public boolean needsTerrainUpdate() { return terrainUpdate;}




}
