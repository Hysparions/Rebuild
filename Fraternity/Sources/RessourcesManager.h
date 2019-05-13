#ifndef RESSOURCES_MANAGER_H
#define RESSOURCES_MANAGER_H

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <map>
#include <stack>
#include <vector>
#include "Chunk.h"
#include "OpenGL\Model.h"
#include "OpenGL\Shader.h"

class RessourcesManager
{
private:
	//A map that contains the Chunks with a vec2 key containing their position
	std::map<glm::vec2, Chunk> loadedChunk;
	//Stack Containing the coordinates of the Chunks to Load
	std::stack<glm::vec2> toLoad;
	std::stack<glm::vec2> toDiscard;
	//Vector containing the models
	std::vector<Model> models;



public:
	///CONSTRUCTORS AND DESTRUCTORS
	RessourcesManager();
	~RessourcesManager();

	///METHODS
	void getChunkToLoad(std::vector<glm::vec2> &v);
	void getChunkToDiscard(std::vector<glm::vec2> &v);
	void LoadModels();
	void Draw(Shader &shader, glm::mat4 projection, glm::mat4 view);
	unsigned int getVAOof(unsigned int i);

};

#endif //RESSOURCES_MANAGER_H