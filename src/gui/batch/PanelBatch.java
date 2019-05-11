package gui.batch;

import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import gui.GUIPanel;
import utils.Utilities;

public class PanelBatch {
	
	/** Capacity of the buffer*/
	private int capacity;
	/** Limit of the buffer*/
	private int limit;
	
	/** Vertex array buffer*/
	private int VAO;
	/** Vertex Buffer object */
	private int VBO;
	/** GUIPanel List */
	private List<GUIPanel> panels;
	
	/**
	 * Text Batch constructor taking the default capacity in parameter
	 * @param capacity
	 * @param limit
	 */
	public PanelBatch(int capacity) {
		this.capacity = capacity;
		this.limit = 0;
		this.panels = new ArrayList<>();
		//Creating a new vertex array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		//Creating a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, capacity*Utilities.FLOATSIZE, GL_DYNAMIC_DRAW);
		
		//Setting the vertex Array attribute pointers
		//Vertex Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 6*Utilities.FLOATSIZE, 0);	
		//Vertex Color
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 6*Utilities.FLOATSIZE, 2*Utilities.FLOATSIZE);	
		
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
			
	}
	
	public void add(GUIPanel panel) {
		//Adding the new Element to the buffer
		panels.add(panel);
		if((limit + 6)*GUIPanel.VERTEX_SIZE*Utilities.FLOATSIZE <= capacity) {
			//The model offset in the batch is now set to the end of the buffer
				panel.offset = limit*GUIPanel.VERTEX_SIZE*Utilities.FLOATSIZE;
				limit += 6;
				glBindBuffer(GL_ARRAY_BUFFER, VBO);
				glBufferSubData(GL_ARRAY_BUFFER, panel.offset, panel.buffer());
				glBindBuffer(GL_ARRAY_BUFFER, 0);
			}
			else {System.err.println("Not Enough Space");}
	}
	
	public void render(){
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, limit);
		glBindVertexArray(0);
	}
	
	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
}
