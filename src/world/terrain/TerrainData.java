package world.terrain;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import world.biomes.Biome;

public class TerrainData {

	//Time to swap from old to new Color
	public static float SWAP_TIME = 10.0f;
	
	//Height Point
	private int height;
	
	//Color of the Point
	private float Time;
	private Vector3f defaultColor;
	private Vector3f OldColor;
	private Vector3f NewColor;
	
	//Biome influence
	private float plains;
	private float forest;
	//Total of influences
	private float total;
	
	public TerrainData() {
		Time = 0.0f;
		plains = 0;
		forest = 0;
		total = 0;
		defaultColor = new Vector3f();
		OldColor = new Vector3f();
		NewColor = new Vector3f();
	}
	
	private void updateColor(float time) {
		calculateOldColor();
		calculateNewColor();
		Time = time;
		
	}
	
	private void calculateOldColor() {
		float DeltaTime = (float)GLFW.glfwGetTime()-Time;
		if(DeltaTime > SWAP_TIME) {OldColor = NewColor;}
		else {
			DeltaTime = DeltaTime/SWAP_TIME;
			float r = NewColor.x()*DeltaTime+OldColor.x()*(1-DeltaTime);
			float g = NewColor.y()*DeltaTime+OldColor.y()*(1-DeltaTime);
			float b = NewColor.z()*DeltaTime+OldColor.z()*(1-DeltaTime);
			OldColor.set(r,g,b);
		}
	}
	
	private void calculateNewColor() {
		float r,g,b;
		r = plains*Biome.PLAINS.color.x + forest*Biome.FOREST.color.x;
		g = plains*Biome.PLAINS.color.y + forest*Biome.FOREST.color.y;
		b = plains*Biome.PLAINS.color.z + forest*Biome.FOREST.color.z;
		
		if(total < 1.0f) {
			r += (1.0f-total)*defaultColor.x;
			g += (1.0f-total)*defaultColor.y;
			b += (1.0f-total)*defaultColor.z;
		}else {
			r /= total;
			g /= total;
			b /= total;
		}
		//System.out.println("New Color Red " + r + " Green " + g + " Blue " + b );
		
		NewColor.set(r,g,b);
	}
	
	//Setters
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setFirstHeight(int height) {
		this.height = height;
		setBaseColor();
	}
	
	public void setBaseColor() {
		Biome.getPolyColor(defaultColor, height);
		OldColor.set(defaultColor);
		NewColor.set(defaultColor);
	}
	
	public void addInfluence(float amount, Biome biome, float time) {

		try {

			switch(biome) {
			case PLAINS:
				this.plains += amount;
				this.total += amount;
				this.updateColor(time);
				break;
			case FOREST:
				this.forest += amount;
				this.total += amount;
				this.updateColor(time);
				break;
			default:
				throw new Exception("Unknown Biome" + biome.name());
			}
		}catch(Exception e) {	
			e.printStackTrace();
		}
	}
	

	
	//Getters
	public int getHeight() {return this.height;}
	public Vector3f getOldColor() {return this.OldColor;}
	public Vector3f getNewColor() {return this.NewColor;}
	public float getTime() {return this.Time;}

	
}
