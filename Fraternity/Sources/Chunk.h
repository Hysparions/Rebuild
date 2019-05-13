#ifndef CHUNK_H
#define CHUNK_H
/*
	The chunk class Generate, and load 40*40 block zone.
	Loaded Chunks are stored and mamnaged by the class World.
*/
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "Perlin.h"


class Chunk
{
private:
	glm::vec3 position;
	//This array of int contains the id of the block contained at a given position in the chunk
	unsigned int blocks[40][40][40];
	//Model of the chunk generated using the previous stuff
	//Model model;
	
public:
	///CONSTRUCTORS & DESTRUCTORS
	Chunk(int x, int y, int z);
	~Chunk();
	
	///METHODS
	//Method Generating using Perlin Noise
	void Generate(int seed, Perlin &p);
	//Method Loading already Generated terrain in memory
	void Load();
	//Method that set the VisibleBlocks
	bool setVisibleBlocks();

};

#endif //CHUNK_H
