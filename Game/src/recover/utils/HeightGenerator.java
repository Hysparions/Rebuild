package recover.utils;

import org.joml.Vector2f;
import org.joml.Vector4f;

import recover.entities.Chunk;

public class HeightGenerator {

	public static final float FREQUENCY = 12.0f;
	public static final int WORLD_CENTER = 500000;
	private Noise perlin;

	public HeightGenerator(int seed) {
		perlin = new Noise(1, 100, seed, FREQUENCY);
	}

	public float[][] getPoints(Vector2f position) {
		float[][] points = new float[Chunk.WIDTH + 1][Chunk.WIDTH + 1];
		float posX = position.x + WORLD_CENTER;
		float posY = position.y + WORLD_CENTER;
		/*
		 * //Geting the Four |AA -- -- AB| |-- -- -- --| |-- -- -- --| |BA -- -- BB|
		 */
		float AA = (float) perlin.generate(posX * Chunk.WIDTH, posY * Chunk.WIDTH, 0.5);
		float AB = (float) perlin.generate((posX + 1) * Chunk.WIDTH, posY * Chunk.WIDTH, 0.5);
		float BA = (float) perlin.generate(posX * Chunk.WIDTH, (posY + 1) * Chunk.WIDTH, 0.5);
		float BB = (float) perlin.generate((posX + 1) * Chunk.WIDTH, (posY + 1) * Chunk.WIDTH, 0.5);

		if (AA > 41) {
			AA = 5.0f;
		} else if (AA > 38) {
			AA = 4.0f;
		} else if (AA > 34) {
			AA = 3.0f;
		} else if (AA > 30) {
			AA = 2.0f;
		} else if (AA > 26) {
			AA = 1.0f;
		} else if (AA < 10) {
			AA = -3.0f;
		} else if (AA < 15) {
			AA = -2.0f;
		} else if (AA < 19) {
			AA = -1.0f;
		} else {
			AA = 0;
		}
		if (AB > 41) {
			AB = 5.0f;
		} else if (AB > 38) {
			AB = 4.0f;
		} else if (AB > 34) {
			AB = 3.0f;
		} else if (AB > 30) {
			AB = 2.0f;
		} else if (AB > 26) {
			AB = 1.0f;
		} else if (AB < 10) {
			AB = -3.0f;
		} else if (AB < 15) {
			AB = -2.0f;
		} else if (AB < 19) {
			AB = -1.0f;
		} else {
			AB = 0;
		}
		if (BA > 41) {
			BA = 5.0f;
		} else if (BA > 38) {
			BA = 4.0f;
		} else if (BA > 34) {
			BA = 3.0f;
		} else if (BA > 30) {
			BA = 2.0f;
		} else if (BA > 26) {
			BA = 1.0f;
		} else if (BA < 10) {
			BA = -3.0f;
		} else if (BA < 15) {
			BA = -2.0f;
		} else if (BA < 19) {
			BA = -1.0f;
		} else {
			BA = 0;
		}
		if (BB > 41) {
			BB = 5.0f;
		} else if (BB > 38) {
			BB = 4.0f;
		} else if (BB > 34) {
			BB = 3.0f;
		} else if (BB > 30) {
			BB = 2.0f;
		} else if (BB > 26) {
			BB = 1.0f;
		} else if (BB < 10) {
			BB = -3.0f;
		} else if (BB < 15) {
			BB = -2.0f;
		} else if (BB < 19) {
			BB = -1.0f;
		} else {
			BB = 0;
		}

		Vector4f weight = new Vector4f(0.0f);

		for (int x = 0; x < Chunk.WIDTH + 1; x++) {
			for (int z = 0; z < Chunk.WIDTH + 1; z++) {
				// determining AA weight
				weight.x = getWeight(x, z);
				// determining AB weight
				weight.y = getWeight((float) Chunk.WIDTH - x - 1, z);
				// determining BA weight
				weight.z = getWeight(x, (float) Chunk.WIDTH - z - 1);
				// determining BB weight
				weight.w = getWeight((float) Chunk.WIDTH - x - 1, (float) Chunk.WIDTH - z - 1);

				// Normalizing Weight
				float total = weight.x + weight.y + weight.z + weight.w;
				if (total != 0.0f) {
					weight.div(total);
				}
				;
				/// Compute Height
				perlin.setOctave(3);
				float height = (float) Math.floor(Chunk.HEIGHT / 3
						+ (Chunk.HEIGHT) / 10.0f
								* (AA * weight.x + AB * weight.y + BA * weight.z + BB * weight.w)
						+ (float) perlin.generate(posX * Chunk.WIDTH + x, posY * Chunk.WIDTH + z, 0.5) / 5.0f);
				perlin.setOctave(1);
				points[x][z] = height;
			}
		}

		return points;
	}

	private float getWeight(float x, float z) {
		return fade(x) * fade(z);
	}

	private float fade(float x) {
		float normX = x / Chunk.WIDTH;
		if (normX < 0.25f) {
			return 1.0f;
		} else if (normX > 0.75) {
			return 0.0f;
		} else if (normX < 0.5) {
			return 1.0f - (normX - 0.25f);
		} else {
			return 1.0f - (normX + 0.25f);
		}
	}

}
