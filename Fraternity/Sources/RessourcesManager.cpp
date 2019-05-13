#include "RessourcesManager.h"
#include "OpenGL\Model.h"
#include "OpenGL\Shader.h"
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

///CONSTRUCTOR
RessourcesManager::RessourcesManager() : loadedChunk(), toLoad(), toDiscard(), models()
{
	
}
///DESTRUCTOR
RessourcesManager::~RessourcesManager()
{

}

///METHODS
void RessourcesManager::getChunkToLoad(std::vector<glm::vec2> &v)
{

}

void RessourcesManager::getChunkToDiscard(std::vector<glm::vec2> &v)
{
	
}

void RessourcesManager::LoadModels()
{
	models.push_back(Model("Ressources/Objects/Blocks/0010/0010.obj"));
}

void RessourcesManager::Draw(Shader &shader, glm::mat4 projection, glm::mat4 view)
{
	for (unsigned int i = 0; i < models.size(); i++)
	{
		shader.setMat4("projection", projection);
		shader.setMat4("view", view);

		//render the loaded model
		glm::mat4 model;
		model = glm::translate(model, glm::vec3(0.0f, -1.75f, 0.0f)); // translate it down so it's at the center of the scene
		model = glm::scale(model, glm::vec3(0.2f, 0.2f, 0.2f));	// it's a bit too big for our scene, so scale it down
		shader.setMat4("model", model);
		models[i].Draw(shader);
	}
}

unsigned int RessourcesManager::getVAOof(unsigned int i)
{
	return models[i].meshes[0].VAO;
}