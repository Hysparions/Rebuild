package gui;

import org.joml.Vector2f;

public class GUISprite extends GUIComponent{

	public GUISprite(Vector2f position, Vector2f surface) {
		super(GUIType.SPRITE, position, surface);
	}

	@Override
	public void reSize() {
		// TODO Auto-generated method stub
		
	}

}
