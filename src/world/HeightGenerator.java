package world;

import org.joml.Vector2f;
import org.joml.Vector4f;

import utils.Noise;

public class HeightGenerator {
	
	public static final float FREQUENCY = 12.0f;
	public static final int SEED = 340;
	public static final int WORLD_CENTER = 1048576;
	private Noise perlin;
	
	public HeightGenerator() {
		perlin = new Noise(1, 100, SEED, FREQUENCY);
	}
	
	public float[][]getPoints(Vector2f position) {
		float[][] points = new float[Chunk.WIDTH + 1][Chunk.WIDTH + 1];
		float posX = position.x + WORLD_CENTER;
		float posY = position.y + WORLD_CENTER;
		
		/*	//Geting the Four
		 	|AA -- -- AB|
		 	|-- -- -- --|
		 	|-- -- -- --|
		 	|BA -- -- BB|		 
		 */
		float AA = (float) perlin.generate(posX*Chunk.WIDTH, posY*Chunk.WIDTH, 0.5);
		float AB = (float) perlin.generate((posX+1)*Chunk.WIDTH, posY*Chunk.WIDTH, 0.5);
		float BA = (float) perlin.generate(posX*Chunk.WIDTH, (posY+1)*Chunk.WIDTH, 0.5);
		float BB = (float) perlin.generate((posX+1)*Chunk.WIDTH, (posY+1)*Chunk.WIDTH, 0.5);
		
		
		if(AA > 40) { AA = 5.0f; } else if(AA > 37) { AA = 4.0f; } else if(AA > 33) { AA = 3.0f; } else if(AA > 28) { AA = 2.0f; } else if(AA > 24) {AA = 1.0f;} else if(AA < 10) {AA = -3.0f;} else if(AA < 13) {AA = -2.0f;} else if(AA < 18) {AA = -1.0f;}  else { AA = 0;}
		if(AB > 40) { AB = 5.0f; } else if(AB > 37) { AB = 4.0f; } else if(AB > 33) { AB = 3.0f; } else if(AB > 28) { AB = 2.0f; } else if(AB > 24) {AB = 1.0f;} else if(AB < 10) {AB = -3.0f;} else if(AB < 13) {AB = -2.0f;} else if(AB < 18) {AB = -1.0f;}  else { AB = 0;}
		if(BA > 40) { BA = 5.0f; } else if(BA > 37) { BA = 4.0f; } else if(BA > 33) { BA = 3.0f; } else if(BA > 28) { BA = 2.0f; } else if(BA > 24) {BA = 1.0f;} else if(BA < 10) {BA = -3.0f;} else if(BA < 13) {BA = -2.0f;} else if(BA < 18) {BA = -1.0f;}  else { BA = 0;}
		if(BB > 40) { BB = 5.0f; } else if(BB > 37) { BB = 4.0f; } else if(BB > 33) { BB = 3.0f; } else if(BB > 28) { BB = 2.0f; } else if(BB > 24) {BB = 1.0f;} else if(BB < 10) {BB = -3.0f;} else if(BB < 13) {BB = -2.0f;} else if(BB < 18) {BB = -1.0f;}  else { BB = 0;}
		
		Vector4f weight = new Vector4f(0.0f);
		
		for(int x = 0; x < Chunk.WIDTH + 1; x++) {
			for(int z = 0; z < Chunk.WIDTH + 1; z++) {
				//determining AA weight
				weight.x = getWeight((float)x, (float)z);
				//determining AB weight
				weight.y = getWeight((float)Chunk.WIDTH-x-1, (float)z);
				//determining BA weight
				weight.z = getWeight((float)x, (float)Chunk.WIDTH-z-1);
				//determining BB weight
				weight.w = getWeight((float)Chunk.WIDTH-x-1, (float)Chunk.WIDTH-z-1);
				
				//Normalizing Weight
				float total = weight.x + weight.y + weight.z + weight.w;
				if(total != 0.0f) { weight.div(total);};
				///Compute Height
				perlin.setOctave(3);
				float height = (float)Math.floor( (float)(Chunk.HEIGHT/3) + ((float)Chunk.HEIGHT)/10.0f* (AA*weight.x + AB*weight.y + BA*weight.z + BB*weight.w) 
							+ (float) perlin.generate(posX*Chunk.WIDTH + x, posY*Chunk.WIDTH + z, 0.5)/5.0f);
				perlin.setOctave(1);
				points[x][z] = height;
			}	
		}
		
		return points;
	}
	
	

	
	
	private float getWeight(float x, float z) {
		return fade(x)* fade(z);
	}
	
	private float fade(float x) {
		float normX = x/Chunk.WIDTH;
		if(normX <0.25f) { return 1.0f;}
		else if(normX >0.75) {return 0.0f;}
		else if(normX <0.5) {return 1.0f-(normX-0.25f);}
		else { return 1.0f - (normX+0.25f); }
	}
	
}
