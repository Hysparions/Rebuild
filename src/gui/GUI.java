package gui;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;

import org.joml.Vector2f;
import org.joml.Vector4f;

import gui.batch.PanelBatch;
import gui.batch.TextBatch;
import gui.font.Font;
import openGL.Shader;

public class GUI {
	
	/** These fields are the default capacities for both text and panel batches */
	private static int TEXT_CAPACITY = 10000;
	private static int PANEL_CAPACITY = 1000;
	
	
	/** General container for the GUI components **/
	private GUIContainer container;
	
	/** General batches for rendering */
	private TextBatch textRenderer;
	private PanelBatch panelRenderer;
	
	/** Font of the batch*/
	private Font font;
	
	/** Shader of the different GUI batchs */
	private Shader textShader;
	private Shader panelShader;
	
	/**
	 * Basic constructor for the GUI Class
	 */
	public GUI(String font, int SCR_W, int SCR_H) {
		//The first offset vector is 0.0f because the container contains all the screen space
		//That's why the second surface vector is set to 1.0f (to make the surface of the whole screen)
		this.container = new GUIContainer(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));
		//Initialization the GUI font with the one passed as argument
		this.font = new Font(font);
		//Creation of the Batches for rendering
		this.textRenderer = new TextBatch(TEXT_CAPACITY, this.font);
		this.panelRenderer = new PanelBatch(PANEL_CAPACITY);
		
		//TEXT SHADER
		this.textShader = new Shader();
		if(textShader.create("GUIText") == false) {System.out.println("Failed to create Shader");}
		textShader.setOrthoProjection(SCR_W, SCR_H);
		textShader.setIntUni("font", 0);
				
		//PANEL SHADER
		this.panelShader = new Shader();
		if(panelShader.create("GUIPanel") == false) {System.out.println("Failed to create Shader");}
	}
	
	public void add(GUIComponent component) {
		container.add(component);
		container.reSize();
		if(component.type() == GUIType.PANEL) {panelRenderer.add((GUIPanel)component);}
	}
	
	public void destroy() {
		textRenderer.destroy();
		panelRenderer.destroy();
		font.destroy();
	}
	
	
	public void render() {
		glDisable(GL_DEPTH_TEST); 
		panelShader.use();
		panelRenderer.render();
		textShader.use();
		textRenderer.render();
		glEnable(GL_DEPTH_TEST); 
	}
	
	public static GUI createBasicGUI(String font, int SCR_W, int SCR_H) {
		GUI gui = new GUI(font, SCR_W, SCR_H);
		//gui.add(new GUIText(new Vector2f(0.35f, 0.0f),new Vector2f(0.3f, 0.08f),  new Vector4f(1.0f, 1.0f, 1.0f)));
		gui.add(new GUIPanel(new Vector2f(-1.0f, -1.0f),new Vector2f(2.0f, 0.06f), new Vector4f(0.25f, 0.25f, 0.25f, 1.0f)));
		gui.add(new GUIPanel(new Vector2f(-0.2f, -1.0f),new Vector2f(0.4f, 0.08f), new Vector4f(0.2f, 0.2f, 0.2f, 1.0f)));
		//Create components and add them
		return gui;
	}
}
