package recover.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import recover.behaviors.WorldConnector;
import recover.entities.Chunk;

/**
 * This class holds the synchronized data between Engine and Chunk Manager
 * @author louis
 *
 */
public class ChunkManager {
	
	private static int CHUNK_VIEW = 4;
	
	Vector3f position;
	List<Vector2f> toLoad;
	List<Chunk> toAdd;
	List<Integer> toRemove;
	List<Chunk> toSave;
	List<LoadedChunk> loadedChunks;
	Boolean run;
	
	/**
	 * Constructor of the Chunk data class
	 */
	public ChunkManager() {
		this.position = new Vector3f();
		this.toLoad = Collections.synchronizedList(new ArrayList<Vector2f>());
		this.toAdd = Collections.synchronizedList(new ArrayList<Chunk>());
		this.toRemove = Collections.synchronizedList(new ArrayList<Integer>());
		this.toSave = Collections.synchronizedList(new ArrayList<Chunk>());
		this.loadedChunks = Collections.synchronizedList(new ArrayList<LoadedChunk>());
		this.run = true;
	}


	/**
	 * This algorithm determines which chunks needs to be loaded and removed from the scene
	 */
	public void runChunkLoader() {
		// Getting chunk position
		Vector3f worldPos = getPosition();
		Vector2f chunkPos = new Vector2f();
		worldPos.x /= (Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH*2);
		chunkPos.x = (float) Math.floor(worldPos.x);
		worldPos.z /= (Chunk.SUB_CHUNK_NUMBER*Chunk.WIDTH*2);
		chunkPos.y = (float) Math.floor(worldPos.z);
		
		resetLoadedChunkFlags();
		Vector2f bufferPos = new Vector2f();
		// Adding each chunks needed to be drawn
		for(float x = chunkPos.x-CHUNK_VIEW; x < chunkPos.x + CHUNK_VIEW +1; x++) {
			for(float z = chunkPos.y-CHUNK_VIEW; z < chunkPos.y + CHUNK_VIEW +1; z++) {
				//For each chunk in the visibility Square
					bufferPos.set(x, z);
					// If chunk doesn't exist, add its position to the loading stack
					// If chunk exists mark it to keep it in the scene
					if(needChunkLoad(bufferPos)) {
						pushToLoad(new Vector2f(bufferPos));
					}
				
			}
		}
		synchronized(loadedChunks) {
			for(int i = 0; i< loadedChunks.size(); i++) {
				if(!loadedChunks.get(i).flag()) {
					pushToRemove(loadedChunks.get(i).id());
				}
			}
		}
		
		
		
	}
	
	
	/**
	 * Synchronized Setter of the position parameter
	 * @param the position to set 
	 */
	public void setPosition(Vector3f position) {
		synchronized(this.position) {
			this.position.set(position);
		}
	}
	
	/**
	 * Synchronized Getter of the position parameter
	 * @param the position to set 
	 */
	public Vector3f getPosition() {
		synchronized(this.position) {
			return new Vector3f(this.position);
		}
	}
	
	/**
	 * Synchronized push for Chunk to Add List
	 * @param chk the Chunk to add
	 */
	public void pushToAdd(Chunk chk) {
		// Adding the chunk to the list of the chunk to add to the scene
		synchronized(this.toAdd) {
			toAdd.add(chk);
		}
	}
	
	/**
	 * Synchronized pop chunk from the to Add List
	 * @return the chunk to add
	 */
	public Chunk popToAdd() {
		Chunk chk = null;
		if(!toAddIsEmpty()) {
			// Getting the top Chunk to add
			synchronized(this.toAdd) {
				chk = toAdd.remove(toAdd.size()-1);
			}
			// adding the chunk to the list of loaded chunk
			WorldConnector behaviorChunk = chk.connector();
			putLoadedChunk(new Vector2f(behaviorChunk.position()), chk.id());

		}
		return chk;
	}
	
	
	/**
	 * Synchronized getter to know if there are still chunk waiting to be added to the scene
	 * @return true if there are no chunks to add to the scene
	 */
	public boolean toAddIsEmpty() {
		synchronized(toAdd) {
			return toAdd.isEmpty();
		}
	}
	
	
	/**
	 * Synchronized push for Chunk to remove from scene
	 * @param position of the chunk to remove
	 */
	public void pushToRemove(int identifier) {
		synchronized(this.toRemove) {
			toRemove.add(identifier);
		}
	}
	
	/**
	 * Synchronized pop for Chunk to Remove List
	 * @return the identifier of the chunk to Remove
	 */
	public Integer popToRemove() {
		Integer identifier = null;
		if(!toRemoveIsEmpty()) {
			synchronized(this.toRemove) {
				identifier = toRemove.remove(toRemove.size()-1);
			}
			removeLoadedChunk(identifier);
		}
		return identifier;
	}
	
	/**
	 * Synchronized getter to know if there are still chunk waiting to be removed of the scene
	 * @return true if there are no chunk to remove
	 */
	public boolean toRemoveIsEmpty() {
		synchronized(toRemove) {
			return toRemove.isEmpty();
		}
	}
	
	
	/**
	 * Synchronized push for Chunk to Load by ChunkIO Thread
	 * @param position of the chunk to load
	 */
	public void pushToLoad(Vector2f position) {
		synchronized(this.toLoad) {
			toLoad.add(position);
		}
	}
	
	/**
	 * Synchronized pop for Chunk to Load List
	 * @return the position of the chunk to load
	 */
	public Vector2f popToLoad() {
		Vector2f v= null;
		if(!toLoadIsEmpty()) {
			synchronized(this.toLoad) {
				v = toLoad.remove(toLoad.size()-1);
			}
		}
		return v;
	}
	
	/**
	 * Synchronized getter to know if there are still chunk waiting to be loaded by chunkIO
	 * @return true if there are no vector to Load
	 */
	public boolean toLoadIsEmpty() {
		synchronized(toLoad) {
			return toLoad.isEmpty();
		}
	}
	
	/**
	 * Synchronized push for Chunk to Save by ChunkIO Thread
	 * @param chunk to save
	 */
	public void pushToSave(Chunk chunk) {
		synchronized(this.toSave) {
			toSave.add(chunk);
		}
	}
	
	/**
	 * Synchronized pop for Chunk to save List
	 * @return the position of the chunk to Save
	 */
	public Chunk popToSave() {
		Chunk chunk = null;
		if(!toLoadIsEmpty()) {
			synchronized(this.toSave) {
				chunk = toSave.remove(toSave.size()-1);
			}
		}
		return chunk;
	}
	
	/**
	 * Synchronized getter to know if there are still chunk waiting to be saved by chunkIO
	 * @return true if there are no chunks to save
	 */
	public boolean toSaveIsEmpty() {
		synchronized(toLoad) {
			return toLoad.isEmpty();
		}
	}
	
	/**
	 * Put a new loaded chunk to map referencing them
	 * @param position
	 * @param identifier
	 */
	public void putLoadedChunk(Vector2f position, Integer identifier) {
		if(position!=null && identifier != 0) {
			synchronized(loadedChunks) {
				loadedChunks.add(new LoadedChunk(position, identifier));
			}
		}else {
			System.err.print("Invalid chunk input for loaded chunks");
		}
	}
	
	/**
	 * Removes a loaded chunk from the map referencing them
	 * @param position
	 * @param identifier
	 */
	public void removeLoadedChunk(Integer identifier) {
		if(position!=null) {
			synchronized(loadedChunks) {
				LoadedChunk chk = null;
				for(int i = 0; i<loadedChunks.size(); i++) {
					if(loadedChunks.get(i).id() == identifier) {
						chk = loadedChunks.get(i);
						break;
					}
				}
				loadedChunks.remove(chk);
			}
		}else {
			System.err.print("Invalid chunk input for loaded chunks");
		}
	}
	
	
	/**
	 * Set all loaded flag to 0
	 */
	public void resetLoadedChunkFlags() {
		synchronized(loadedChunks) {
			for(int i = 0; i < loadedChunks.size(); i++) {
				loadedChunks.get(i).setFlag(false);
			}
		}

	}
	
	/**
	 * Get the id of a loaded chunk in the map referencing them
	 * @param position of the chunk
	 */
	public Integer getLoadedChunk(Vector2f position) {
		synchronized(loadedChunks) {
			for(int i = 0; i < loadedChunks.size(); i++) {
				if(loadedChunks.get(i).position().equals(position)) {
					return loadedChunks.get(i).id();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Determines if a chunk needs to be loaded or generated at this position
	 * @param position
	 * @return
	 */
	public boolean needChunkLoad(Vector2f position) {
		synchronized(loadedChunks) {
			for(int i = 0; i < loadedChunks.size(); i++) {
				if(loadedChunks.get(i).position().equals(position)) {
					loadedChunks.get(i).setFlag(true);
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * stop the chunk loader to create new chunks
	 */
	public void stop() {
		synchronized(this.run) {
			this.run = false;
		}
	}
	
	/**
	 * Should run 
	 */
	public boolean shouldRun() {
		synchronized(this.run) {
			return this.run;
		}
	}


	public ArrayList<Integer> getLoadedChunksID() {
		ArrayList<Integer> list = new ArrayList<>();
		synchronized(loadedChunks) {
			for(int i = 0; i< loadedChunks.size(); i++) {
				list.add(loadedChunks.get(i).id());
			}
		}
		return list;
	}
}

/**
 * This class is used to store data about loaded chunk to do the chunk loading algorithm
 * @author louis
 *
 */
class LoadedChunk{
	private Vector2f position;
	private int id;
	private boolean flag;
	
	public LoadedChunk(Vector2f position, int id) {
		this.position = position;
		this.id = id; 
		this.flag = false;
	}

	/** @return the position*/
	public Vector2f position() {	return position;}

	/** @return the id*/
	public int id() {return id;}
	
	/** @return the flag */
	public boolean flag() {return flag;}

	/** @param flag value */
	public void setFlag(boolean flag) {this.flag = flag;}

}
