package world;


public class ChunkLoader extends Thread{
	/*
	private Map<Vector2f, Chunk> chunksMap;
	private Vector2f position;
	private HeightGenerator heightGen;
	private Vector2f buffer;
	private boolean Running = true;
	
	public ChunkLoader( Map<Vector2f, Chunk> chunks, Vector2f Position, HeightGenerator heightGen) {
		
		this.chunksMap = chunks;
		this.position = Position;
		this.heightGen = heightGen;
		this.buffer = new Vector2f();
		this.setPriority(Thread.MIN_PRIORITY);
	}
	
	public void run() {
		if(checkChunkExistence()) {
			generateChunkFromFile();
		}else {
			generateChunkFromSeed();
		}
	}
	
	private void generateChunkFromSeed() {
		Chunk chunk = Chunk.generateFromSeed();
		chunk.connectChunk();
		this.generateChunkBuffers();
		this.insertChunkInMap();
		
	}
	
	private boolean checkChunkExistence() {
		return false;
	}
	
	private void generateChunkFromFile() {
		//Load the Chunk From it's file
	}
	
	public void connectChunk() {
		
		Vector2f adjacent = new Vector2f(0.0f, 0.0f);
		
		//Test if a Front chunk exist
		adjacent.set(position.x, position.y+1);
		if(chunksMap.containsKey(adjacent)) {	position.setFront(chunksMap.get(adjacent));	}
		
		//Test if a Back chunk exist
		adjacent.set(position.x, position.y-1);
		if(chunksMap.containsKey(adjacent)) {	position.setBack(chunksMap.get(adjacent));	}
		
		//Test if a Right chunk exist
		adjacent.set(position.x+1, position.y);
		if(chunksMap.containsKey(adjacent)) {	currentChunk.setRight(chunksMap.get(adjacent));	}
		
		//Test if a Left chunk exist
		adjacent.set(position.x-1, position.y);
		if(chunksMap.containsKey(adjacent)) {	currentChunk.setLeft(chunksMap.get(adjacent));	}
		
	}
	
	public void insertChunkInMap() {
		synchronized(this.chunksMap) {
			chunksMap.put(currentPosition, currentChunk);
			currentChunk = null;
		}
	}
		
	*/
}
