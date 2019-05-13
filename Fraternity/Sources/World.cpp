#include "World.h"
#include "Chunk.h"
#include "Perlin.h"
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <glad.h>
#include <glfw3.h>
//Constructor
World::World(std::string worldName, std::string characterName) : directoryPath(worldName), seed(), pChunk(glm::vec2(0.0f, 0.0f)), noiseGen(), renderDistance(8.0f), toLoad()
{
	//Path to the World File Info
	std::string path = "Save/" + directoryPath + "/WorldData/" + directoryPath + ".dat";
	std::string buffer = " ";
	//Accessing to the file
	std::ifstream worldFile(path, std::ios::in);
	//If the world Exists
	if (worldFile)
	{
		while (buffer != "="){worldFile >> buffer;}
		worldFile >> buffer;
		seed = std::stoi(buffer);

		worldFile.close();
	}
	else { /* Throw Exception */}
}

//Destructor
World::~World(){}

///Methods
//Method that returns the identifier of the chunks to load
void World::setChunksToLoad()
{
	glfwSetTime(0.0f);
	//Clearing the vector cointaining the indices of the chunks to load
	toLoad.clear();
	//X is the norm of a vector from 0 to the renderDistance
	float x;
	//Looping to fill the vector toLoad
	for (float i = -renderDistance; i < renderDistance + 1.0f; i++)
	{
		for (float j = -renderDistance; j < renderDistance + 1.0f; j++)
		{
			if (i != 0.0f) { x = cos(atan(j / i)); }
			else { x = 0.f; }
			//Case i > 0
			if (i > 0 && i< renderDistance*x)
			{
				toLoad.push_back(glm::vec2(i + pChunk.x , j + pChunk.y )); 
			}//Case i < 0
			else if (i < 0 && abs(i) < renderDistance*x)
			{
				toLoad.push_back(glm::vec2(i + pChunk.x, j + pChunk.y));
			}//Case i == 0
			else if (i == 0 && j > -renderDistance && j < renderDistance)
			{
				toLoad.push_back(glm::vec2(i + pChunk.x, j + pChunk.y));
			}
		}
	}

	//std::cout << "Vetor of chunks to load Charged in " << glfwGetTime() << " containing " << toLoad.size() << " elements " << std::endl;
}