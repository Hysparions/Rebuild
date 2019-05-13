#ifndef TEXTURE_H
#define TEXTURE_H

#include <string>
#include <glad.h>
#include <glfw3.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>

class Texture {
private:
	string filePath;
	unsigned int id;		//Texture ID
	unsigned int width;		//Texture Width
	unsigned int height;	//Texture Height

public:
	///Constructors and destructor
	Texture() :filePath(""), id(0), width(0), height(0)
	{
		
	}
	~Texture(){}

	Load(const string &FilePath)
	{
		
	}

	Create(unsigned int w, unsigned int h, );

};

#endif //TEXTURE_H