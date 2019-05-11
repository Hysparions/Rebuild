package openGL;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class StaticMesh {

	private int VAO;
	private int VBO;
	private int EBO;
	
	private int vertexCount;
	private int shininess;
	
	private List<Texture> textures;
	
	public StaticMesh() {
		VAO = 0;
		VBO = 0;
		EBO = 0;
		shininess = 8;
		textures = null;
	}
	
	public void Generate(FloatBuffer vertices, IntBuffer indices, List<Texture> t)
	{
		//Generating and binding a new Vertex Array
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		//Generating and binding a new Vertex Buffer
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		//Generating and binding a new Element Buffer 
		EBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		vertexCount = indices.capacity();
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*(Float.SIZE/Byte.SIZE), 0);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*(Float.SIZE/Byte.SIZE), 3*(Float.SIZE/Byte.SIZE));
		
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*(Float.SIZE/Byte.SIZE), 6*(Float.SIZE/Byte.SIZE));
		
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		textures = t;
	}
	
	public void draw(Shader shader) {
		
		shader.setFloatUni("material.shininess", shininess);
		Integer diffuse = 1;
		for(int i = 0; i < textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i);
			if(textures.get(i).getType() == "diffuse") {
				shader.setIntUni("material.diffuse" + diffuse.toString(), i);
				glBindTexture(GL_TEXTURE_2D, textures.get(i).ID());
				diffuse++;
			}
			else
			{
				System.err.println("ERROR: Texture defined with type '" + textures.get(i).getType() + "'" );
			}
		}
		glBindVertexArray(VAO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
		
		glBindVertexArray(0);
	}
	
	public void destroy() {
		glDeleteBuffers(EBO);
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
		
		for(int i = 0 ; i < textures.size(); i++) {
			textures.get(i).destroy();
		}
	}
	
	void addTexture(Texture t) {
		textures.add(t);
	}
	
	
}
