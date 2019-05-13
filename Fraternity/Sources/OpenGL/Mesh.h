#ifndef MESH_H
#define MESH_H

#include <glad.h>

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "Shader.h"

#include <string>
#include <fstream>
#include <iostream>
#include <vector>

/*
	In this Class we define the mesh object used by the model to render their different parts,
	Data structure for vertices and textures are also defined here to render the model meshes.
*/

struct Vertex {
	glm::vec3 Position; //Position of the Vertex in the local Space
	glm::vec3 Normal; //Norlmal Vector of the vertex
	glm::vec2 TextCoords; //Texture Coordinates
};

struct Texture {
	unsigned int id;
	std::string type; //Path From
	std::string path; //Texture Path;
};

class Mesh
{

private:
	//OPENGL Buffer Objects for rendering
	unsigned int VBO, EBO;
	
	//Methods
	void Generate()
	{
		glGenVertexArrays(1, &VAO); //Generating a vertex array
		glGenBuffers(1, &VBO);		//Generating a vertex buffer
		glGenBuffers(1, &EBO);		//Generating an element buffer
		
		glBindVertexArray(VAO);					//Binding the Vertex array Buffer
		glBindBuffer(GL_ARRAY_BUFFER, VBO);		//Binding the Vertex Buffer Object
		glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices[0], GL_STATIC_DRAW);	//Setting the Buffer Data
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);		//Binding the Element Buffer Object
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size()* sizeof(unsigned int), &indices[0], GL_STATIC_DRAW);//Setting the Buffer Data

		glEnableVertexAttribArray(0);	//Vertex Position
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)0);	//Set The Data format
		glEnableVertexAttribArray(1);	//Vertex Normal
		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, Normal)); //Set the data format
		glEnableVertexAttribArray(4);	//Vertex TextCoords
		glVertexAttribPointer(4, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, TextCoords));

		glBindVertexArray(0);
	}

public:
	//Vertex Array Object public for instancing
	unsigned int  VAO;
	//Mesh data public for fast access
	std::vector<Vertex> vertices;
	std::vector<unsigned int> indices;
	std::vector<Texture> textures;

	//Constructor
	Mesh(std::vector<Vertex> vertices, std::vector<unsigned int> indices, std::vector<Texture> textures)
	{
		this->vertices = vertices;		//Instantiation of the vertices vector
		this->indices = indices;		//Instantiation of the indices vector
		this->textures = textures;		//Instantiation of the textures vector
		Generate();					//Call of the setup mesh which create the appropriate buffer objects
	}
	
	///Methods
	void Draw(Shader shader)
	{
		unsigned int diffuse = 1;
		unsigned int specular = 1;
		for (unsigned int i = 0; i < textures.size(); i++)
		{
			glActiveTexture(GL_TEXTURE0 + i); // activate proper texture unit before binding
											  // retrieve texture number (the N in diffuse_textureN)
			std::string n;
			std::string name = textures[i].type;
			if (name == "texture_diffuse")
				n = std::to_string(diffuse++);
			else if (name == "texture_specular")
				n = std::to_string(specular++);

			shader.setFloat(("material." + name + n).c_str(), (float)i);
			glBindTexture(GL_TEXTURE_2D, textures[i].id);
		}
		glActiveTexture(GL_TEXTURE0);
		// draw mesh
		glBindVertexArray(VAO);
		//glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
		glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);

		glBindVertexArray(0);
	}

};

#endif //MESH_H