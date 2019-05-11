package gui;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class GUIPanel extends GUIComponent{

	/** Vertex Size of the Panel Component*/
	public static final int VERTEX_SIZE = 6; 
	
	/** Color */
	private Vector4f color;
	/** Offset of the panel */
	public long offset;
	
	/** Buffer */
	private FloatBuffer quadBuffer;
	
	
	/** The constructor of the Panel GUI component*/
	public GUIPanel(Vector2f relativePosition, Vector2f relativeSurface, Vector4f color) {
		super(GUIType.PANEL, relativePosition, relativeSurface);
		this.color = color;
		this.quadBuffer = BufferUtils.createFloatBuffer(6*VERTEX_SIZE);
	}

	/** The calculation of the absolute position of the panel and the update of the buffer*/
	public void reSize() {	
		updateBuffer();
	}
	
	/** This method is used to build the buffer of the GUI component */
	public void updateBuffer() {

		
		quadBuffer.put(position.x); quadBuffer.put(position.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z); quadBuffer.put(color.w);

		quadBuffer.put(position.x+surface.x); quadBuffer.put(position.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z); quadBuffer.put(color.w);
		
		quadBuffer.put(position.x); quadBuffer.put(position.y+surface.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z); quadBuffer.put(color.w);

		quadBuffer.put(position.x); quadBuffer.put(position.y+surface.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z); quadBuffer.put(color.w);
		
		quadBuffer.put(position.x+surface.x); quadBuffer.put(position.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z); quadBuffer.put(color.w);
		
		quadBuffer.put(position.x+surface.x); quadBuffer.put(position.y+surface.y);
		quadBuffer.put(color.x); quadBuffer.put(color.y); quadBuffer.put(color.z);  quadBuffer.put(color.w);
		
		quadBuffer.flip();
	}
	
	/** Get the color value of the Panel */
	public Vector4f color() {return color;}
	/** Get the panel buffer */
	public FloatBuffer buffer() {return quadBuffer;}
}
