#ifndef ENTITY_H
#define ENTITY_H

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <string>
#include "OpenGL\Shader.h"
#include "OpenGL\Model.h"
#include "OpenGL\Mesh.h"

class EntityMesh
{
public:
	std::string name;					//Name of the part of the model
	Mesh mesh;							//Mesh of the model
	glm::mat4 model;					//Matrix used to translate/rotate the model to its position
	std::vector<EntityMesh> Children;
	std::vector<glm::vec3> Position;

	EntityMesh(std::string Name, Mesh node);
	~EntityMesh();
};

class Entity
{
private:
	std::string Type;
	glm::vec3 Position;
	glm::vec3 Direction;
	glm::vec3 lookingAt;
	//EntityMesh Base;

public:
	///CONSTRUCTOR & DESTRUCTOR
	Entity(std::string type, glm::vec3 position);
	virtual ~Entity();

	virtual void Draw(Shader &s);
	virtual void DrawNode(Shader &s, EntityMesh &node);
	virtual void ProcessNodeMatrix(EntityMesh &node, glm::vec3 posOffset, double time) = 0;
	std::string getType();
	glm::vec3 getPosition();
	
};

#endif //ENTITY_H
