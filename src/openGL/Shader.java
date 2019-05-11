package openGL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.*;
import org.joml.Math;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import openGL.camera.Camera;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL30.*;

public class Shader {
	private int vertexShader, fragmentShader, program;
	
	//Basic Constructor for the Shader class
	public Shader() {
		vertexShader = 0;
		fragmentShader = 0;
		program = 0;
	}
	
	//Shader Creation using the getCode function to get the Shader's code
	public boolean create(String shader) {
		int success;
		
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, getCode(shader + ".vs"));
		glCompileShader(vertexShader);
		
		success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.err.println("Vertex: \n" + glGetShaderInfoLog(vertexShader));
			return false;
		}
		
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, getCode(shader + ".fs"));
		glCompileShader(fragmentShader);
		
		success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.err.println("Fragment: \n" + glGetShaderInfoLog(fragmentShader));
			return false;
		}
		
		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		
		glLinkProgram(program);
		success = glGetProgrami(program, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			System.err.println("Program Link: \n" + glGetProgramInfoLog(program));
			return false;
		}
		glValidateProgram(program);
		success = glGetProgrami(program, GL_VALIDATE_STATUS);
		if (success == GL_FALSE) {
			System.err.println("Program Validate: \n" + glGetProgramInfoLog(program));
			return false;
		}

		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

		
		return true;
	}
	
	
	public void destroy() {

		glDeleteProgram(program);
	}
	
	//Set the shader as the current program in GPU
	public void use() {
		glUseProgram(program);
	}
	
	//Set a float uniform inside the shader using its name
	public void setFloatUni(String name, float data) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform1f(Location, data);
	}
	
	//Set an integer uniform inside the shader using its name
	public void setIntUni(String name, int data) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform1i(Location, data);
	}
	//Set a Vector2f uniform inside the shader using its name
	public void setVec2Uni(String name, Vector2f data) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform2f(Location, data.x, data.y);
	}
	//Set a Vector2f uniform inside the shader using its name
	public void setVec2Uni(String name, float x, float y) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform2f(Location, x, y);
	}
	//Set a Vector3f uniform inside the shader using its name
	public void setVec3Uni(String name, Vector3f data) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform3f(Location, data.x, data.y, data.z);
	}
	//Set a Vector4f uniform inside the shader using its name
	public void setVec4Uni(String name, Vector4f data) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform4f(Location, data.x, data.y, data.z, data.w);
	}
	//Set a Vector4f uniform inside the shader using its name
	public void setVec4Uni(String name, float x, float y, float z, float w) {
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniform4f(Location, x, y, z, w);
	}
	
	//Set a Matrix4 uniform inside the shader using its name
	public void setMat4Uni(String name, Matrix4f data) {
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		data.get(matrixBuffer);
		int Location = glGetUniformLocation(program, name);
		if(Location == -1){System.err.println("ERROR : Uniform with name " + name + " not found!");}
		glUniformMatrix4fv(Location, false, matrixBuffer);
	}

	public void setOrthoProjection(int Scr_W, int Scr_H) {
		this.use();
		Matrix4f ortho = new Matrix4f();
		ortho.ortho(0.0f, Scr_W, 0.0f, Scr_H, -1.0f, 1.0f);
		this.setMat4Uni("projection",ortho);
	}
	
	public void setPerspectiveProjection(float zoom, int Scr_W, int Scr_H, float near, float far) {
		this.use();
		Matrix4f persp = new Matrix4f();
		persp.perspective((float)Math.toRadians(zoom), ((float)Scr_W)/((float)Scr_H), near, far);
		this.setMat4Uni("projection",persp);
	}
	
	public void setTerrainShader(Camera camera) {
		this.setMat4Uni("view", camera.getView());
		this.setVec3Uni("viewPos", camera.getPosition());
	}
	
	public void setStaticModelShader(Camera camera) {
		this.setMat4Uni("view", camera.getView());
		this.setVec3Uni("viewPos", camera.getPosition());
	}
	
	public void setWaterShader(Camera camera) {
		this.setMat4Uni("view", camera.getView());
		this.setVec3Uni("viewPos", camera.getPosition());
		this.setFloatUni("time", (float)GLFW.glfwGetTime());
	}
	
	public void setDirLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular) {
		
		this.use();
		this.setVec3Uni("Sun.direction", direction);
		this.setVec3Uni("Sun.ambient", ambient);
		this.setVec3Uni("Sun.diffuse", diffuse);
		this.setVec3Uni("Sun.specular", specular);
		
	}
	
	public int getProgramId() {
		return program;
	}
	
	private String getCode(String file) {
		BufferedReader reader = null;
		StringBuilder sourceBuilder = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/shaders/" + file)));
			String line;
			
			while ((line = reader.readLine()) != null) {
				sourceBuilder.append(line + "\n");
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sourceBuilder.toString();
	}
}
