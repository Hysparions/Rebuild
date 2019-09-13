package recover.behaviors.model;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.GeometryUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import engine.utils.Utilities;
import recover.behaviors.ChunkCulling;
import recover.behaviors.WorldConnector;
import recover.entities.Chunk;
import recover.utils.Biome;
import recover.utils.HeightGenerator;
import recover.utils.TerrainPoint;
/**
 * This class holds the CPU buffer as well as the Terrain Data
 * @author louis
 *
 */
public class TerrainModel extends EngineBehavior{
	
	/** Number of float per vertex in the batch */
	public static final int VERTEX_SIZE_IN_FLOAT = 13;
	/** Number of vertex per Tile */
	public static final int VERTEX_PER_TILE = 6;
	
	/* Position and connection to other terrain */
	private WorldConnector connector;
	/** Terrain Culling */
	private ChunkCulling culling;
	/* vertex indices in the buffer with a bunch of data*/
	private TerrainPoint[][] terrain;
	/** Raw buffer data */
	private ByteBuffer buffer;
	/** Vertex view float data */
	private FloatBuffer vertices;
	/** Index integer data */
	private IntBuffer indices;
	/** dummy vector used for operation */
	private Vector3f vector;
	private Vector3f color;
	
	/**
	 * Constructor of the TerrainModel behavior
	 * @param connector to other terrains
	 */
	public TerrainModel(WorldConnector connector, HeightGenerator generator, ChunkCulling culling) {
		super(BehaviorType.TERRAIN_MODEL);
		this.connector = connector;
		this.culling = culling;
		// Calculating Chunk width
		int width = Chunk.WIDTH*Chunk.SUB_CHUNK_NUMBER;
		// Allocating chunk data control array
		this.terrain = new TerrainPoint[width+1][width+1];
		// Allocating data buffers
		this.buffer =BufferUtils.createByteBuffer(((width+1)*(width+1)+(width-1)*width)*VERTEX_SIZE_IN_FLOAT*Utilities.FLOATSIZE);
		this.vertices = buffer.asFloatBuffer();
		this.indices = BufferUtils.createIntBuffer(width*width*VERTEX_PER_TILE);

		this.vector = new Vector3f();
		this.color = new Vector3f();
		generateIndexBuffer(width);
		generateVertexBuffer(width);
		generateNewTerrain(generator);
		//System.out.println(this.buffer.capacity());
		//System.out.println(this.vertices.capacity());
		//System.out.println(this.indices.capacity());
	}
	
	/**
	 * Generate the indice data 
	 */
	public void generateIndexBuffer(int width) {
		int offsetByLine = 2*width;
		// All lines excepts last
		for(int x = 0; x<width-1; x++) {
			for(int z = 0; z<width ; z++) {
				// First triangle configuration
				if((x%2==0&&z%2==0)||(x%2==1&&z%2==1)) {
					indices.put(x*offsetByLine+(z*2)).put((x+1)*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+(z*2));
					indices.put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+(z*2)+1).put(x*offsetByLine+(z*2));					
				}
				else {
					indices.put(x*offsetByLine+(z*2)).put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+(z*2));
					indices.put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+(z*2));		
				}
			}
		}
		int x = width-1;
		//Last line 
		for(int z = 0; z<width ; z++) {
			// First triangle configuration
			if((x%2==0&&z%2==0)||(x%2==1&&z%2==1)) {
				indices.put(x*offsetByLine+(z*2)).put((x+1)*offsetByLine+z+1).put((x+1)*offsetByLine+z);
				indices.put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+z+1).put(x*offsetByLine+(z*2));					
			}
			else {
				indices.put(x*offsetByLine+(z*2)).put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+z);
				indices.put(x*offsetByLine+(z*2)+1).put((x+1)*offsetByLine+z+1).put((x+1)*offsetByLine+z);		
			}
		}
		((Buffer)indices).flip();
	}
	
	public void generateVertexBuffer(int width) {
		int offset = 0;
		int vertexSize = VERTEX_SIZE_IN_FLOAT*Utilities.FLOATSIZE;
		for(int x = 0; x<width+1; x++) {
			for(int z = 0; z<width+1 ; z++) {
				((Buffer)buffer).position(offset);
				offset+=vertexSize;
				if(z == 0 || z == width || x == width) {
					((Buffer)buffer).limit(offset);
					FloatBuffer firstBuffer = buffer.asFloatBuffer();
					terrain[x][z] = new TerrainPoint(firstBuffer, null, 2*((connector.position().x*width)+x),2*((connector.position().y*width)+z));
				}else {
					((Buffer)buffer).limit(offset);
					FloatBuffer firstBuffer = buffer.asFloatBuffer();
					((Buffer)buffer).position(offset);
					offset+=vertexSize;
					((Buffer)buffer).limit(offset);
					FloatBuffer secondBuffer = buffer.asFloatBuffer();
					terrain[x][z] = new TerrainPoint(firstBuffer, secondBuffer,2*((connector.position().x*width)+x),2*((connector.position().y*width)+z));
				}				
			}
		}
	}
	
	public void generateNewTerrain(HeightGenerator heightGenerator) {
		Vector2f position = connector.position();
		Vector2f buffer = new Vector2f();
		// Generating Chunk Height Map
		float[][] subChunk = null;
		for (int i = 0; i < Chunk.SUB_CHUNK_NUMBER; i++) {
			for (int j = 0; j < Chunk.SUB_CHUNK_NUMBER; j++) {

				// Calculating the sub Chunk Position
				float xOffset = position.x * Chunk.SUB_CHUNK_NUMBER + i;
				float zOffset = position.y * Chunk.SUB_CHUNK_NUMBER + j;

				buffer.set(xOffset, zOffset);
				subChunk = heightGenerator.getPoints(buffer);

				// Putting the sub generated height into the whole terraindata array
				for (int x = 0; x < Chunk.WIDTH + 1; x++) {
					for (int y = 0; y < Chunk.WIDTH + 1; y++) {
						terrain[i * Chunk.WIDTH + x][j * Chunk.WIDTH + y].setHeight( subChunk[x][y]);
						terrain[i * Chunk.WIDTH + x][j * Chunk.WIDTH + y].initHeightColor();
					}
				}
			}
		}
	}
	
	/**
	 * Calculate the normals and the colors of the terrain
	 */
	public void calculateNormalsAndColors() {
		for (int x = 0; x < Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH+1; x++) {
			for (int z = 0; z < Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH+1; z++) {
				calculateNormal(x, z);
				calculateColor(x, z);
			}
		}
	}
	
	/**
	 * Calculate the normals of the point specified in parameter
	 * @param x position coordinate
	 * @param y position coordinate
	 */
	public void calculateNormal(int x, int z) {
		int width = Chunk.WIDTH * Chunk.SUB_CHUNK_NUMBER;
		// Calcul two buffers
		if (z > 0 && z < width && x < width) {
			// First triangle configuration
			if ((x % 2 == 0 && z % 2 == 0) || (x % 2 == 1 && z % 2 == 1)) {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z).x(),
						point(x + 1, z).y(), point(x + 1, z).z(), point(x + 1, z - 1).x(), point(x + 1, z - 1).y(),
						point(x + 1, z - 1).z(), vector);
				// Set First Normal
				point(x, z).setNormalF(vector.x, vector.y, vector.z);
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z + 1).x(),
						point(x + 1, z + 1).y(), point(x + 1, z + 1).z(), point(x + 1, z).x(), point(x + 1, z).y(),
						point(x + 1, z).z(), vector);
				// Set Second normal
				point(x, z).setNormalS(vector.x, vector.y, vector.z);

			} else {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z).x(),
						point(x + 1, z).y(), point(x + 1, z).z(), point(x, z - 1).x(), point(x, z - 1).y(),
						point(x, z - 1).z(), vector);
				point(x, z).setNormalF(vector.x, vector.y, vector.z);

				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x, z + 1).x(),
						point(x, z + 1).y(), point(x, z + 1).z(), point(x + 1, z).x(), point(x + 1, z).y(),
						point(x + 1, z).z(), vector);
				point(x, z).setNormalS(vector.x, vector.y, vector.z);

			}
		}
		// Calcul one buffers
		else if (z == 0 && x < width) {
			// First triangle configuration
			if ((x % 2 == 0 && z % 2 == 0) || (x % 2 == 1 && z % 2 == 1)) {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z + 1).x(),
						point(x + 1, z + 1).y(), point(x + 1, z + 1).z(), point(x + 1, z).x(), point(x + 1, z).y(),
						point(x + 1, z).z(), vector);
				// Set First normal
				point(x, z).setNormalF(vector.x, vector.y, vector.z);

			} else {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x, z + 1).x(),
						point(x, z + 1).y(), point(x, z + 1).z(), point(x + 1, z + 1).x(), point(x + 1, z + 1).y(),
						point(x + 1, z + 1).z(), vector);
				// Set First normal
				point(x, z).setNormalF(vector.x, vector.y, vector.z);

			}
		}
		// Calcul one buffers
		else if (z == width && x < width) {
			// First triangle configuration
			if ((x % 2 == 0 && z % 2 == 0) || (x % 2 == 1 && z % 2 == 1)) {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z).x(),
						point(x + 1, z).y(), point(x + 1, z).z(), point(x + 1, z - 1).x(), point(x + 1, z - 1).y(),
						point(x + 1, z - 1).z(), vector);
				// Set First normal
				point(x, z).setNormalF(vector.x, vector.y, vector.z);

			} else {
				// CalculateNormal
				GeometryUtils.normal(point(x, z).x(), point(x, z).y(), point(x, z).z(), point(x + 1, z).x(),
						point(x + 1, z).y(), point(x + 1, z).z(), point(x, z - 1).x(), point(x, z - 1).y(),
						point(x, z - 1).z(), vector);
				// Set First normal
				point(x, z).setNormalF(vector.x, vector.y, vector.z);

			}
		}
	}

	public void calculateColor(int x, int z) {
		int width = Chunk.WIDTH*Chunk.SUB_CHUNK_NUMBER;
		
		float total = 0.0f;
		float plains = 0.0f;
		float forest = 0.0f;
		
		if (z > 0 && z < width && x < width) {

			// First triangle configuration
			if((x%2==0&&z%2==0)||(x%2==1&&z%2==1)) {
				//Calculate Default First Color
				plains = (point(x, z).plains() + point(x+1, z).plains() + point(x+1, z-1).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x+1, z).forest() + point(x+1, z-1).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x+1, z).heightColor().x() + point(x+1, z-1).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x+1, z).heightColor().y() + point(x+1, z-1).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x+1, z).heightColor().z() + point(x+1, z-1).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);
				
				//Calculate Default Second Color
				plains = (point(x, z).plains() + point(x+1, z).plains() + point(x+1, z+1).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x+1, z).forest() + point(x+1, z+1).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x+1, z).heightColor().x() + point(x+1, z+1).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x+1, z).heightColor().y() + point(x+1, z+1).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x+1, z).heightColor().z() + point(x+1, z+1).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorS(vector.x, vector.y, vector.z);
				point(x, z).setNewColorS(vector.x, vector.y, vector.z);
				

			}
			else {
				//Calculate Default First Color
				plains = (point(x, z).plains() + point(x, z-1).plains() + point(x+1, z).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x, z-1).forest() + point(x+1, z).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x, z-1).heightColor().x() + point(x+1, z).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x, z-1).heightColor().y() + point(x+1, z).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x, z-1).heightColor().z() + point(x+1, z).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);
				
				
				//Calculate Default Second Color
				Biome.getPolyColor(vector, (point(x, z).y()+ point(x, z+1).y()+ point(x+1, z).y())/3.0f);
				//Calculate Default Second Color
				plains = (point(x, z).plains() + point(x, z+1).plains() + point(x+1, z).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x, z+1).forest() + point(x+1, z).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x, z+1).heightColor().x() + point(x+1, z).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x, z+1).heightColor().y() + point(x+1, z).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x, z+1).heightColor().z() + point(x+1, z).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorS(vector.x, vector.y, vector.z);
				point(x, z).setNewColorS(vector.x, vector.y, vector.z);

			}
		}
		// Calcul one buffers
		else if(z==0&&x<width) {
			// First triangle configuration
			if((x%2==0&&z%2==0)||(x%2==1&&z%2==1)) {
				//Calculate Default First Color
				plains = (point(x, z).plains() + point(x+1, z).plains() + point(x+1, z+1).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x+1, z).forest() + point(x+1, z+1).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x+1, z).heightColor().x() + point(x+1, z+1).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x+1, z).heightColor().y() + point(x+1, z+1).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x+1, z).heightColor().z() + point(x+1, z+1).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);
			}
			else {
				plains = (point(x, z).plains() + point(x, z+1).plains() + point(x+1, z).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x, z+1).forest() + point(x+1, z).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x, z+1).heightColor().x() + point(x+1, z).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x, z+1).heightColor().y() + point(x+1, z).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x, z+1).heightColor().z() + point(x+1, z).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);
			}
		}
		// Calcul one buffers
		else if(z==width&&x<width) {
			// First triangle configuration
			if((x%2==0&&z%2==0)||(x%2==1&&z%2==1)) {
				//Calculate Default Second Color
				plains = (point(x, z).plains() + point(x+1, z).plains() + point(x+1, z-1).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x+1, z).forest() + point(x+1, z-1).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x+1, z).heightColor().x() + point(x+1, z-1).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x+1, z).heightColor().y() + point(x+1, z-1).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x+1, z).heightColor().z() + point(x+1, z-1).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);
			}
			else {
				plains = (point(x, z).plains() + point(x, z-1).plains() + point(x+1, z).plains()) / 3.0f;
				forest = (point(x, z).forest() + point(x, z-1).forest() + point(x+1, z).forest()) / 3.0f;
				total = plains + forest;
				if(total >= 1.0f) {
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x)/total;
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y)/total;
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z)/total;
				}else {
					color.x = (point(x, z).heightColor().x() + point(x, z-1).heightColor().x() + point(x+1, z).heightColor().x())/3.0f;
					color.y = (point(x, z).heightColor().y() + point(x, z-1).heightColor().y() + point(x+1, z).heightColor().y())/3.0f;
					color.z = (point(x, z).heightColor().z() + point(x, z-1).heightColor().z() + point(x+1, z).heightColor().z())/3.0f;
					
					vector.x = (forest*Biome.FOREST.color.x + plains*Biome.PLAINS.color.x) + ((1.0f-total) * color.x);
					vector.y = (forest*Biome.FOREST.color.y + plains*Biome.PLAINS.color.y) + ((1.0f-total) * color.y);
					vector.z = (forest*Biome.FOREST.color.z + plains*Biome.PLAINS.color.z) + ((1.0f-total) * color.z);	
				}
				point(x, z).setOldColorF(vector.x, vector.y, vector.z);
				point(x, z).setNewColorF(vector.x, vector.y, vector.z);

			}

		}
	}
	
	/** 
	 * Add the influence of an entity on the terrain
	 * @param x position in the terrain model perspective
	 * @param z position in the terrain model perspective
	 * @param time of the removal
	 * @param amount of influence
	 * @param biome concerned
	 */ 
	public void addInfluence(int x, int z, double time, float amount, Biome biome) {
		TerrainPoint point = this.point(x, z);
		point.addInfluence(amount, biome, (float)time);
	}
	
	/** 
	 * Remove the influence of an entity on the terrain
	 * @param x position in the terrain model perspective
	 * @param z position in the terrain model perspective
	 * @param time of the removal
	 * @param amount of influence
	 * @param biome concerned
	 */ 
	public void removeInfluence(int x, int z, double time, float amount, Biome biome) {
		TerrainPoint point = this.point(x, z);
		point.removeInfluence(amount, biome, (float)time);
	}
	
	/**
	 * Get the vertex float buffer data
	 * @return the vertex data as float buffer
	 */
	public FloatBuffer vertices() {
		return this.vertices;
	}
	
	/**
	 * Get the vertex float buffer data
	 * @return the vertex data as float buffer
	 */
	public IntBuffer indices() {
		return this.indices;
	}
	
	/**
	 * Return the terrain point of the given coordinates
	 * @param x coordinate
	 * @param z coordinate
	 * @return the Terrain point
	 */
	public TerrainPoint point(int x, int z) {
		return terrain[x][z];
	}
	
	/**
	 * @return the world Connector
	 */
	public WorldConnector connector() {
		return this.connector;
	}

	/**
	 * @return true if the batch has been approved by the chunk culling test visible 
 	 */
	public boolean isVisible() {
		return this.culling.isVisible();
	}

	
	@Override
	public boolean generateSceneEvent() {
		// Don't notify adding of this
		return false;
	}


}


