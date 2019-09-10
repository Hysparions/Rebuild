package recover.behaviors.model;

import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;
import recover.entities.Chunk;

/**
 * This class contains the model and the tiles composing the water on the map
 * This Behavior is exclusive to the water entity chunk
 * @author louis
 *
 */
public class WaterModel extends EngineBehavior{

	/** Height Level of the water level **/
	public static final int WATER_LEVEL = Chunk.HEIGHT / 3 + 4; 
	private ArrayList<WaterTile> tiles;
	private TerrainModel terrain;
	
	/**
	 * Constructor of the Water Model Behavior
	 * @param terrain
	 */
	public WaterModel(TerrainModel terrain) {
		super(BehaviorType.WATER_MODEL);
		this.tiles = new ArrayList<>();
		this.terrain = terrain;
		// water want to be notified of terrain model changes
		this.terrain.addListeners(this);
		// Generate default Water collection
		generateSea();
		
	}
	
	public void generateSea() {
		int width = Chunk.WIDTH*Chunk.SUB_CHUNK_NUMBER;
		Vector2f worldPos = terrain.connector().position();
		for(int x = 0; x<width; x++) {
			for(int z = 0; z<width; z++) {
				if (	terrain.point(x,z).y() + 0.5f <= WATER_LEVEL
					|| 	terrain.point(x+1, z).y() + 0.5f <= WATER_LEVEL
					|| 	terrain.point(x, z+1).y() + 0.5f <= WATER_LEVEL
					|| 	terrain.point(x+1, z+1).y() + 0.5f <= WATER_LEVEL) {
				
					float y0 = terrain.point(x,z).y();
					float y1 = terrain.point(x,z+1).y();
					float y2 = terrain.point(x+1,z).y();
					float y3 = terrain.point(x+1,z+1).y();
					this.addTile((worldPos.x*width)+ x, y0, y1, y2, y3,(worldPos.y*width)+z);
				}

			}
		}
		
	}
		
	/**
	 * Add a new tile to the specified coordinates
	 * @param x position coordinate
	 * @param z position coordinate
	 */
	public void addTile(float x, float y0, float y1, float y2, float y3, float z) {
		tiles.add(new WaterTile(x,  y0, y1, y2, y3, z));
	}
	
	/**
	 * Removes a tile from the list of tile
	 * @param x
	 * @param z
	 */
	public void removeTile(float x, float z) {
		for(int i = 0; i<amount(); i++) {
			if(tiles.get(i).x() == x && tiles.get(i).z() == z) {
				tiles.remove(i);
			}
		}
	}
	
	public WaterTile getTile(float x, float z) {
		for(int i = 0; i<amount(); i++) {
			if(tiles.get(i).x() == x && tiles.get(i).z() == z) {
				return tiles.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Generates and returns a byte Buffer containing the water model data
	 * @return
	 */
	public ByteBuffer getByteBuffer() {
		ByteBuffer bytesBuffer = BufferUtils.createByteBuffer(amount()*WaterTile.SIZE_IN_BYTE);
		for(int i = 0; i<amount(); i++) {
			bytesBuffer.put(tiles.get(i).bytes());
		}
		((Buffer)bytesBuffer).flip();
		
		
		return bytesBuffer;
	}

	/**
	 * @return the tile Amount
	 */
	public int amount() {
		return tiles.size();
	}

	/**
	 * @return true if the batch has been approved by the chunk culling test visible 
 	 */
	public boolean isVisible() {
		return this.terrain.isVisible();
	}
	
	@Override
	public boolean generateSceneEvent() {
		return false;
	}
	
	/**
	 * @return the position of the chunk containing this model
	 */
	public Vector2f position() {
		return terrain.connector().position();
	}
	
}


/**
 * This class holds the byteBuffer of a water Tile made out of two triangles
 * @author louis
 *
 */
class WaterTile {
	/** Water Tile Size in byte 6 Vertices * 2*4bytes floating position + 4 bytes indicators */
	public final static int SIZE_IN_BYTE = 6*(12+4);	
	/** Contains the data of the Water Tile */
	private final ByteBuffer byteBuffer;
	
	/**
	 * Creates a waterTile
	 * @param x
	 * @param z
	 */
	public WaterTile(float x,  float y0, float y1, float y2, float y3, float z) {
		this.byteBuffer = BufferUtils.createByteBuffer(SIZE_IN_BYTE);
		if((x%2==0&&z%2==0)||(Math.abs(x%2)==1&&Math.abs(z%2)==1)) {
			
			// First Triangle
			byteBuffer.putFloat(x*2.0f).putFloat(y0).putFloat(z*2.0f).put((byte) 2).put((byte) 2).put((byte) 2).put((byte) 0);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y3).putFloat((z+1)*2.0f).put((byte) 0).put((byte) -2).put((byte) -2).put((byte) -2);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y2).putFloat(z*2.0f).put((byte) -2).put((byte) 0).put((byte) 0).put((byte) 2);
			// Second Triangle
			byteBuffer.putFloat(x*2.0f).putFloat(y0).putFloat(z*2.0f).put((byte) 0).put((byte) 2).put((byte) 2).put((byte) 2);
			byteBuffer.putFloat(x*2.0f).putFloat(y1).putFloat((z+1)*2.0f).put((byte) 2).put((byte) 0).put((byte) 0).put((byte) -2);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y3).putFloat((z+1)*2.0f).put((byte) -2).put((byte) -2).put((byte) -2).put((byte) 0);
		}else {
			// First Triangle
			byteBuffer.putFloat(x*2.0f).putFloat(y0).putFloat(z*2.0f).put((byte) 0).put((byte) 2).put((byte) 2).put((byte) 0);
			byteBuffer.putFloat(x*2.0f).putFloat(y1).putFloat((z+1)*2.0f).put((byte) 2).put((byte) -2).put((byte) 0).put((byte) -2);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y2).putFloat(z*2.0f).put((byte) -2).put((byte) 0).put((byte) -2).put((byte) 2);
			// Second Triangle
			byteBuffer.putFloat(x*2.0f).putFloat(y1).putFloat((z+1)*2.0f).put((byte) 2).put((byte) 0).put((byte) 2).put((byte) -2);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y3).putFloat((z+1)*2.0f).put((byte) 0).put((byte) -2).put((byte) -2).put((byte) 0);
			byteBuffer.putFloat((x+1)*2.0f).putFloat(y2).putFloat(z*2.0f).put((byte) -2).put((byte) 2).put((byte) 0).put((byte) 2);
		}
		((Buffer)byteBuffer).flip();
	}
	/** @return The x position of the tile*/
	public float x() { return byteBuffer.getFloat(0);}
	/** @return The x position of the tile*/
	public float y() { return byteBuffer.getFloat(4);}
	/** @return The z position of the tile*/
	public float z() { return byteBuffer.getFloat(8);}
	/** @return The bytebuffer of the tile */
	public ByteBuffer bytes() { return byteBuffer;}
	
}