package recover.utils;

import java.nio.FloatBuffer;
import java.nio.Buffer;

import org.joml.Vector3f;

public 
class TerrainPoint {

	// Time to swap from old to new Color
	public static float SWAP_TIME = 10.0f;

	/** FloatBuffer of the terrain point */
	private FloatBuffer firstPoint;
	/** Second Floatbuffer if any */
	private FloatBuffer secondPoint;

	// Biome influence
	private float plains;
	private float forest;
	/** Heigth Color */
	private Vector3f heightColor;

	public TerrainPoint(FloatBuffer first, FloatBuffer second, float x, float z) {
		this.firstPoint = first;
		this.secondPoint = second;
		// Set influences to 0
		this.forest = 0.0f;
		this.plains = 0.0f;
		// Set default color to O
		this.heightColor = new Vector3f();
		// Set position
		if(first != null) {
			this.firstPoint.put(0, x);
			this.firstPoint.put(2, z);
		}
		// Set position
		if(second != null) {
			this.secondPoint.put(0, x);
			this.secondPoint.put(2, z);
		}
	}
	
	public void initHeightColor() {
		Biome.getPolyColor(heightColor, this.y());
	}
	
	public Vector3f heightColor() {
		return heightColor;
	}
	
	public void addInfluence(float amount, Biome biome, float time) {
		try {
			if(amount >= 0.0f) {
				switch (biome) {
				case PLAINS:
					this.plains += amount;
					break;
				case FOREST:
					this.forest += amount;
					break;
				default:
					throw new Exception("Unknown Biome" + biome.name());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeInfluence(float amount, Biome biome, float time) {
		try {
			if(amount > 0.0f) {
				switch (biome) {
				case PLAINS:
					this.plains -= amount;
					if(plains < 0.0f) {plains = 0.0f;}
					break;
				case FOREST:
					this.forest -= amount;
					if(forest < 0.0f) {forest = 0.0f;}
					break;
				default:
					throw new Exception("Unknown Biome" + biome.name());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// SETTERS
	/**
	 * Set the height of the tile
	 * @param height
	 */
	public void setHeight(float height) {
		this.firstPoint.put(1, height);
		if(secondPoint != null) {
			this.secondPoint.put(1, height);
		}
	}
	
	public void setOldColorF(float r, float g, float b) {
		((Buffer)this.firstPoint).position(6);
		this.firstPoint.put(r).put(g).put(b);
	}
	
	public void setOldColorS(float r, float g, float b) {
		if(this.secondPoint != null) {
			((Buffer)this.secondPoint).position(6);
			this.secondPoint.put(r).put(g).put(b);
		}
	}
	
	public void setNewColorF(float r, float g, float b) {
		((Buffer)this.firstPoint).position(9);
		this.firstPoint.put(r).put(g).put(b);
	}
	
	public void setNewColorS(float r, float g, float b) {
		if(this.secondPoint != null) {
			((Buffer)this.secondPoint).position(9);
			this.secondPoint.put(r).put(g).put(b);
		}
	}
	
	public void setNormalF(float x, float y, float z) {
		((Buffer)this.firstPoint).position(3);
		this.firstPoint.put(x).put(y).put(z);
	}
	
	public void setNormalS(float x, float y, float z) {
		if(this.secondPoint != null) {
			((Buffer)this.secondPoint).position(3);
			this.secondPoint.put(x).put(y).put(z);
		}
	}
	
	public void setTimeF(float t) {
		this.firstPoint.put(12, t);
	}
	
	public void setTimeS(float t) {
		if(this.secondPoint != null) {
			this.secondPoint.put(12, t);
		}
	}

	// GETTERS
	/** @return X position */
	public float x() { return this.firstPoint.get(0);}
	/** @return Y position */
	public float y() { return this.firstPoint.get(1);}
	/** @return Z position */
	public float z() {return this.firstPoint.get(2);}
	/** @return X Normal first point*/
	public float xNormF() { return this.firstPoint.get(0);}
	/** @return Y Normal  first point */
	public float yNormF() {return this.firstPoint.get(1);}
	/** @return Z Normal  first point */
	public float zNormF() {return this.firstPoint.get(2);}
	/** @return X Normal second point */
	public float xNormS() { if(this.secondPoint != null) {return this.secondPoint.get(0);}return 0.0f;}
	/** @return Y Normal  second point */
	public float yNormS() {if(this.secondPoint != null) {return this.secondPoint.get(1);}return 0.0f;}
	/** @return Z Normal  second point */
	public float zNormS() {if(this.secondPoint != null) {return this.secondPoint.get(2);}return 0.0f;}
	/** @return Red Old color component first point*/
	public float rOldF() { return this.firstPoint.get(6);}
	/** @return Green Old color component first point*/
	public float gOldF() {return this.firstPoint.get(7);}
	/** @return Blue Old color component first point*/
	public float bOldF() {return this.firstPoint.get(8);}
	/** @return Red Old color component Second point*/
	public float rOldS() { if(this.secondPoint != null) {return this.secondPoint.get(6);}return 0.0f;}
	/** @return Green Old color component Second point*/
	public float gOldS() {if(this.secondPoint != null) {return this.secondPoint.get(7);}return 0.0f;}
	/** @return Blue Old color component Second point*/
	public float bOldS() {if(this.secondPoint != null) {return this.secondPoint.get(8);}return 0.0f;}
	/** @return Red New color component first point*/
	public float rNewF() {return this.firstPoint.get(0);}
	/** @return Green New color component first point*/
	public float gNewF() {return this.firstPoint.get(10);}
	/** @return Blue New color component first point*/
	public float bNewF() {return this.firstPoint.get(11);}
	/** @return Red New color component Second point*/
	public float rNewS() {if(this.secondPoint != null) {return this.secondPoint.get(0);}return 0.0f;}
	/** @return Green New color component Second point*/
	public float gNewS() {if(this.secondPoint != null) {return this.secondPoint.get(10);}return 0.0f;}
	/** @return Blue New color component Second point*/
	public float bNewS() {if(this.secondPoint != null) {return this.secondPoint.get(11);}return 0.0f;}
	/** @return Time of previous modification  First point*/
	public float timeF() {return this.firstPoint.get(12);}
	/** @return Time of previous modification  Second point*/
	public float timeS() {if(this.secondPoint != null) {return this.secondPoint.get(12);}return 0.0f;}
	
	/** @return the biome forest influence */
	public float forest() {return forest;}
	/** @return the biome forest influence */
	public float plains() {return plains;}

	


}