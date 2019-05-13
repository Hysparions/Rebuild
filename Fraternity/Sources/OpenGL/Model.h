#ifndef MODEL_H
#define MODEL_H

#include <glad.h> 

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "stb/stb_image.h"
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>

#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>

#include "Mesh.h"
#include "Shader.h"

unsigned int TextureFromFile(const char *path, const std::string &directory, bool gamma = false);


class Model 
{
private:

	//Methods
	void loadModel(std::string Directory)
	{
		Assimp::Importer importer;
		const aiScene *Scene = importer.ReadFile(Directory, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_CalcTangentSpace);
		//Check if the .obj has been loaded
		if (!Scene || Scene->mFlags & AI_SCENE_FLAGS_INCOMPLETE || !Scene->mRootNode) { std::cout << "ERROR::ASSIMP:: " << importer.GetErrorString() << std::endl; return; }
		// retrieve the directory path of the filepath
		directoryPath = Directory.substr(0, Directory.find_last_of('/'));
		// process ASSIMP's root node recursively
		processNode(Scene->mRootNode, Scene);
	}

	void processNode(aiNode *node, const aiScene *scene)
	{
		for (unsigned int i = 0; i < node->mNumMeshes; i++)		// process all the node's meshes (if any)
		{
			aiMesh *mesh = scene->mMeshes[node->mMeshes[i]];	
			meshes.push_back(processMesh(mesh, scene));			//Add the Mesh converted from the Assimp::Mesh
		}
		for (unsigned int i = 0; i < node->mNumChildren; i++)	//Then do the same for each of its children
		{
			processNode(node->mChildren[i], scene);
		}
	}

	Mesh processMesh(aiMesh *mesh, const aiScene *scene)
	{
		std::vector<Vertex> vertices;
		std::vector<unsigned int> indices;
		std::vector<Texture> textures;

		/// VERTICES

		glm::vec3 vector;					//glm vec containing data
		glm::vec2 tvector;
		Vertex vertex;						//Vertex to add
		for (unsigned int i = 0; i < mesh->mNumVertices; i++)
		{
			vector.x = mesh->mVertices[i].x;
			vector.y = mesh->mVertices[i].y;
			vector.z = mesh->mVertices[i].z;
			vertex.Position = vector;			//Vertex Position

			vector.x = mesh->mNormals[i].x;
			vector.y = mesh->mNormals[i].y;
			vector.z = mesh->mNormals[i].z;
			vertex.Normal = vector;				//Vertex Normal

			if (mesh->mTextureCoords[0]) // does the mesh contain texture coordinates?
			{
				tvector.x = mesh->mTextureCoords[0][i].x;
				tvector.y = mesh->mTextureCoords[0][i].y;
				vertex.TextCoords = tvector;	//Vertex Texture Coordinates
			}
			else{ vertex.TextCoords = glm::vec2(0.0f, 0.0f); }
				
			vertices.push_back(vertex);
		}

		///INDICES

		for (unsigned int i = 0; i < mesh->mNumFaces; i++)
		{
			aiFace face = mesh->mFaces[i];
			for (unsigned int j = 0; j < face.mNumIndices; j++)
				indices.push_back(face.mIndices[j]);
		}

		///TEXTURES

		if (mesh->mMaterialIndex >= 0)
		{
			aiMaterial *material = scene->mMaterials[mesh->mMaterialIndex];

			std::vector<Texture> diffuseMaps = loadMaterialTextures(material, aiTextureType_DIFFUSE, "texture_diffuse");
			textures.insert(textures.end(), diffuseMaps.begin(), diffuseMaps.end());

			std::vector<Texture> specularMaps = loadMaterialTextures(material,aiTextureType_SPECULAR, "texture_specular");
			textures.insert(textures.end(), specularMaps.begin(), specularMaps.end());
		}

		// return a mesh object created from the extracted mesh data
		return Mesh(vertices, indices, textures);

	}

	std::vector<Texture> loadMaterialTextures(aiMaterial *mat, aiTextureType type, std::string typeName)
	{
		std::vector<Texture> textures;
		for (unsigned int i = 0; i < mat->GetTextureCount(type); i++)
		{
			aiString str;
			mat->GetTexture(type, i, &str);
			// check if texture was loaded before and if so, continue to next iteration: skip loading a new texture
			bool skip = false;
			for (unsigned int j = 0; j < textures_loaded.size(); j++)
			{
				if (std::strcmp(textures_loaded[j].path.data(), str.C_Str()) == 0)
				{
					textures.push_back(textures_loaded[j]);
					skip = true; // a texture with the same filepath has already been loaded, continue to next one. (optimization)
					break;
				}
			}
			if (!skip)
			{   // if texture hasn't been loaded already, load it
				Texture texture;
				texture.id = TextureFromFile(str.C_Str(), this->directoryPath);
				texture.type = typeName;
				texture.path = str.C_Str();
				textures.push_back(texture);
				textures_loaded.push_back(texture);  // store it as texture loaded for entire model, to ensure we won't unnecesery load duplicate textures.
			}
		}
		return textures;
	}

public:
	//Attributes
	std::vector<Texture> textures_loaded;
	std::vector<Mesh> meshes;	//Vector containing all the meshes of the model
	std::string directoryPath;	//directory path of the object
	bool gammaCorrection;

	//Constructor
	Model(std::string const &Directory, bool gamma = false) : gammaCorrection(gamma)
	{
		loadModel(Directory);
	}

	//Method
	void Draw(Shader shader)	//Draw the Model and all its meshes
	{
		for (unsigned int i = 0 ; i < meshes.size(); i++)
		{
			meshes[i].Draw(shader);	
		}
	}
};



#endif //MODEL_H