#include "Entity.h"
#include <string>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "OpenGL\Shader.h"
#include "OpenGL\Mesh.h"

///CONSTRUCTOR & DESTRUCTOR
EntityMesh::EntityMesh(std::string Name, Mesh node) :name(Name), mesh(node) {}
EntityMesh::~EntityMesh(){}
Entity::Entity(std::string type, glm::vec3 position) : Type(type), Position(position) {}	
Entity::~Entity(){}

///GETTERS
std::string Entity::getType() { return Type; }
glm::vec3 Entity::getPosition() { return Position; }

///FUNCTION
void Entity::Draw(Shader &s) {

	//this->DrawNode(s, Base);
}


void Entity::DrawNode(Shader &s, EntityMesh &Node)
{
	/*Node.mesh.Draw(s);
	for (unsigned int i = 0; i < Node.Children.size(); i++)
	{
		this->DrawNode(s, Node.Children[i]);
	}*/
}