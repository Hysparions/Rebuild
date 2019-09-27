package recover.gui.views;

import org.joml.Vector2i;

import engine.gui.UIBox;
import engine.gui.UIPanel;
import engine.gui.UIWindow;
import engine.guiwidget.UIButton;
import engine.utils.Color;

/**
 * This Wrapper contains the navigation bar code for 
 * the main menu bar
 * @author louis
 *
 */
public class MainMenuNavigation extends UIWindow{


	private UIPanel borderLeft;
	private UIPanel borderRight;

	private UIPanel left;
	private UIButton play;
	private UIPanel middle1;
	private UIButton world;
	private UIPanel middle2;
	private UIButton option;
	private UIPanel middle3;
	private UIButton exit;
	private UIPanel right;

	/**
	 * Default Constructor of the Main Navigation Menu
	 */
	public MainMenuNavigation() {
		super("Main Menu Navigation");

		this.overlay(50, 50, 50, 200);
		// Border Left
		this.borderLeft = new UIPanel(new UIBox(20, 10, 30, 55));
		this.borderLeft.overlay(30, 150, 30, 200);
		this.add(borderLeft);
		// Left
		this.left = new UIPanel(new UIBox(50, 10, 100, 10));
		this.add(left);
		// Play button
		this.play = new UIButton("Play", "HelvLight", new Color(255, 255, 255, 255), 15, 30);
		this.add(play);
		// Middle 1
		this.middle1 = new UIPanel(new UIBox(50, 10, 70, 10));
		this.add(middle1);
		// World button
		this.world = new UIButton("World", "HelvLight", new Color(255, 255, 255, 255), 15, 30);
		this.add(world);
		// Middle 2
		this.middle2 = new UIPanel(new UIBox(50, 10, 70, 10));
		this.add(middle2);
		// Option button
		this.option = new UIButton("Option", "HelvLight", new Color(255, 255, 255, 255), 15, 30);
		this.add(option);
		// Middle 3
		this.middle3 = new UIPanel(new UIBox(50, 10, 70, 10));
		this.add(middle3);
		// Exit button
		this.exit = new UIButton("Exit", "HelvLight", new Color(255, 255, 255, 255), 15, 30);
		this.add(exit);
		// Right
		this.right = new UIPanel(new UIBox(50, 10, 100, 10));
		this.add(right);
		// Border Right
		this.borderRight = new UIPanel(new UIBox(20, 10, 30, 55));
		this.borderRight.overlay(30, 150, 30, 200);
		this.add(borderRight);


	}

	@Override
	public void updateBox(Vector2i size) {
		this.resize(size.x/1.6f, size.y/10.f );
		this.reposition((size.x()-this.width())/2.0f,size.y -this.height());
		System.out.println(this);
	}
}
