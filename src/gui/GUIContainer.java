package gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class GUIContainer extends GUIComponent{
	
	/** List containing others GUI components */
	private List<GUIComponent> components;
	
	/**
	 * Basic constructor for the GUI component element
	 * @param position
	 * @param surface
	 */
	public GUIContainer( Vector2f position, Vector2f surface) {
		super(GUIType.CONTAINER, position, surface);
		components = new ArrayList<>();
	}
	
	/**
	 * Basic constructor for the GUI component element
	 * @param position
	 * @param surface
	 */
	public GUIContainer( Vector2f position, Vector2f surface, List<GUIComponent> components) {
		super(GUIType.CONTAINER, position, surface);
		this.components = components;
	}

	public void reSize()  {
		
		components.forEach((component) -> { 
			component.reSize();
		});
	}
	
	/*
	 * Method adding a component to the container
	 */
	public void add(GUIComponent component) {
		this.components.add(component);
	}
}
