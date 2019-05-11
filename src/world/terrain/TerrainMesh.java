package world.terrain;


import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import utils.Utilities;

public class TerrainMesh {

	public static final int VERTEX_SIZE_IN_FLOAT = 13;
	
	private int VAO;
	private int VBO;
	private int EBO;
	
	private FloatBuffer vertices;
	private IntBuffer indices;
	
	private int vertexCount;
	
	
	
	public TerrainMesh(int verticesAmount, int indicesAmount) {
		
		vertices = GenerateVertexBuffer(verticesAmount);
		indices = GenerateIndicesBuffer(indicesAmount);
		VAO = 0;
		VBO = 0;
		EBO = 0;
	}
	
	public void Generate()
	{
		//Generating and binding a new Vertex Array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		//Generating and binding a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
		//Generating and binding a new Element Buffer 
		EBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		vertexCount = indices.capacity();
		
		//Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 13*Utilities.FLOATSIZE, 0);
		//Normal
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 13*Utilities.FLOATSIZE, 3*Utilities.FLOATSIZE);
		//OldColor
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 13*Utilities.FLOATSIZE, 6*Utilities.FLOATSIZE);
		//NewColor
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 13*Utilities.FLOATSIZE, 9*Utilities.FLOATSIZE);
		//Time
		glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 1, GL_FLOAT, false, 13*Utilities.FLOATSIZE, 12*Utilities.FLOATSIZE);
		
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}
	
	public void draw() {
		if(vertices != null && indices != null) {
			glBindVertexArray(VAO);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
			glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
			
			glBindVertexArray(0);
		}
	}
	
	public void updateGPUBuffer() {
		if(vertices != null && indices != null) {
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	public void destroy() {
		glDeleteBuffers(EBO);
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
	
	private FloatBuffer GenerateVertexBuffer(int size) {
		this.vertices = BufferUtils.createFloatBuffer(size);
		return vertices;
	}
	private IntBuffer GenerateIndicesBuffer(int size) {
		this.indices = BufferUtils.createIntBuffer(size);
		return indices;
	}

	public FloatBuffer getVertexBuffer() {return vertices;}
	public IntBuffer getIndicesBuffer() {return indices;}	
	
}
