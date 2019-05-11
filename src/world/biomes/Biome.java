package world.biomes;

import org.joml.Vector3f;
import org.joml.Vector4f;

import world.Chunk;

public enum Biome {
	DEEP_WATER ( "Deep Water", new Vector3f(0.607f, 0.447f, 0.141f), 7),
	WATER ( "Coast",  new Vector3f(0.870f, 0.729f, 0.462f), 7),
	PLAINS ("GrassLand", new Vector3f(0.356f, 0.575f, 0.2f), 7),
	FOREST ("Forest", new Vector3f(0.235f, 0.286f, 0.129f), 7),
	ROCKS ("Rocks", new Vector3f(0.411f, 0.239f, 0.047f), 7),
	SNOWY_MOUNTAINS ("Snowy Mountains", new Vector3f(0.950f, 0.950f, 0.950f), 7);
	
	public String name = "";
	public Vector3f color = null;
	public int strenght = 5;
	
	private static Vector4f heightInfluence = new Vector4f();
	
	Biome(String name, Vector3f color, int strenght) {
		this.name = name;
		this.color = color;
		this.strenght = strenght;
	}
	
	public static void getPolyColor(Vector3f color, float height) {
		calculateHeightInfluence(height/Chunk.HEIGHT);
		float r = 0.0f , g = 0.0f, b = 0.0f;
		r = DEEP_WATER.color.x * heightInfluence.x + WATER.color.x * heightInfluence.y + ROCKS.color.x * heightInfluence.z + SNOWY_MOUNTAINS.color.x * heightInfluence.w;
		g = DEEP_WATER.color.y * heightInfluence.x + WATER.color.y * heightInfluence.y + ROCKS.color.y * heightInfluence.z + SNOWY_MOUNTAINS.color.y * heightInfluence.w;
		b = DEEP_WATER.color.z * heightInfluence.x + WATER.color.z * heightInfluence.y + ROCKS.color.z * heightInfluence.z + SNOWY_MOUNTAINS.color.z * heightInfluence.w;
		color.set(r, g, b); 
		//System.out.println("Biome 1 " + moistureWeight.x +  " Biome 2 " + moistureWeight.y + " Biome 3 " + moistureWeight.z +" Moisture " + w);
	}
	
	/*
	---    ---    ---
	   \  /   \  /
	    \/     \/
	    /\     /\
	   /  \   /  \
	---    ---    ---
	*/
	
	
	public static void calculateHeightInfluence ( float height){
		if (height < 0.35f) { heightInfluence.set( -height/0.35f + 1.0f, height/0.35f, 0.0f, 0.0f ); } 
		else if (height < 0.45f) { heightInfluence.set( 0.0f , -10.0f*height+4.5f, 10*height - 3.5f, 0.0f ); } 
		else if (height < 0.95f) { heightInfluence.set( 0.0f , 0.0f , -2.f * height + 1.9f , 2.f*height - 0.9f ); } 
		else  { heightInfluence.set( 0.0f , 0.0f, 0.0f, 1.0f ); } 
	}
}
