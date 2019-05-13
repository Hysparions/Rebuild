#ifndef FRATERNITYGL_H
#define FRATERNITYGL_H

#include <string>
#include <iostream>
#include <glad.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "stb/stb_image.h"

namespace Fra
{
	/*
		Texture Class used to Load or Create a Texture into an OpenGL context.
		The class stores the pointer (ID) to the texture for rendering, and it
		stores its height and width. The file path is also stored to know if a
		texture has already been loaded.
	*/
	class Texture {
	private:
		std::string filePath;	//Texture FilePath
		unsigned int id;		//Texture ID
		int width;				//Texture Width
		int height;				//Texture Height

	public:
		///Constructors and destructor
		Texture() :filePath(""), id(0), width(0), height(0)
		{
			glGenTextures(1, &id);
		}
		~Texture() { glDeleteTextures(1, &id); }

		void Load(const std::string &FilePath)
		{
			//Storing the file path for later use
			filePath = FilePath;
			glBindTexture(GL_TEXTURE_2D, id);
			glGenerateMipmap(GL_TEXTURE_2D);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			// load image, create texture and generate mipmaps
			int nrChannels;
			// The FileSystem::getPath(...) is part of the GitHub repository so we can find files on any IDE/platform; replace it with your own image path.
			unsigned char *data = stbi_load(filePath.c_str(), &width, &height, &nrChannels, STBI_rgb_alpha);
			if (data)
			{
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
				glGenerateMipmap(GL_TEXTURE_2D);
			}
			else
			{
				std::cout << "Failed to load texture" << std::endl;
			}
			stbi_image_free(data);
			std::cout << "Texture " <<id << " is " <<  width << " " << height << std::endl;
		}

		std::string getPath() { return filePath; }
		unsigned int getID() { return id; }
		int getW() { return width; }
		int getH() { return height; }
		//void Create(unsigned int w, unsigned int h);
	};

	/*
		The Sprite class is used to draw the textures within a rectangle defined in the attributes.
		It's useful to render small parts of a big texture in a 2D format on the screen
	*/

	class Sprite {
	private:
		Texture * texture;
		Shader shader;
		glm::vec2 position;
		glm::vec4 rectangle;
		float scale;
		
		unsigned int VAO;
		void initRenderData()
		{
			unsigned int VBO;
			float vertices[] = {
				0.0f, 1.0f,  0.0f, 1.0f,
				1.0f, 0.0f,  1.0f, 0.0f,
				0.0f, 0.0f,  0.0f, 0.0f,

				0.0f, 1.0f,  0.0f, 1.0f,
				1.0f, 1.0f,  1.0f, 1.0f,
				1.0f, 0.0f,  1.0f, 0.0f

			};

			glGenVertexArrays(1, &this->VAO);

			glGenBuffers(1, &VBO);
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
			glBindVertexArray(this->VAO);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 4 * sizeof(GLfloat), (void*)0);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 4 * sizeof(GLfloat), (void*)(2 * sizeof(float)));
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			this->shader.use();
			this->shader.setInt("texture1", 0);
		}

	public:
		Sprite() :texture(nullptr), shader(), position(0.0f, 0.0f), rectangle(0.0f, 0.0f, 1.0f, 1.0f), scale(1.0f) { initRenderData(); }
		Sprite(glm::vec2 p, Shader &s, Texture *t) : texture(t), shader(s), position(p), rectangle(0.0f, 0.0f, (float)t->getW(), (float)t->getH()), scale(1.0f) { initRenderData(); }
		Sprite(glm::vec2 p, glm::vec4 r, Shader &s, Texture *t) : texture(t), shader(s), position(p), rectangle(r), scale(1.0f) { initRenderData(); }
		~Sprite(){}

		void Draw()
		{
			this->shader.use();
			glm::mat4 model;
			model = glm::translate(model, glm::vec3(position.x - ((rectangle.z*scale) /2.0f), position.y - ((rectangle.w*scale) / 2.0f), 0.0f));
			model = glm::scale(model, glm::vec3(rectangle.z * scale, rectangle.w * scale, 1.0f));

			glm::mat4 spriteProjection;
			spriteProjection = glm::translate(spriteProjection, glm::vec3(rectangle.x / (float)texture->getW(), rectangle.y / (float)texture->getH(), 0.0f));
			spriteProjection = glm::scale(spriteProjection ,glm::vec3(rectangle.z / (float)texture->getW(), rectangle.w / (float)texture->getH(), 1.0f));
			this->shader.setMat4("model", model);
			this->shader.setMat4("spriteProjection", spriteProjection);

			// bind Texture
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture->getID());

			// render container
			glBindVertexArray(this->VAO);
			glDrawArrays(GL_TRIANGLES, 0, 6);
		}

		void setRectangle(float x, float y, float w, float h) { rectangle = glm::vec4(x, y, w, h); }	//Method to set a new rectangle for the texture
		void setPosition(float x, float y) { position = glm::vec2(x, y); }								//Method to set a new position for the sprite
		void setScale(float s) { scale = s; }															//Method to set the scale of the sprite
	};

	/*
		This class simply draws a rectangle in a specified color
	*/

	class RectRenderer {
	private:
		unsigned int VAO;
		Shader shader;
		void initRenderData()
		{
			unsigned int VBO;
			float vertices[] = {
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f,

				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
			};

			glGenVertexArrays(1, &this->VAO);

			glGenBuffers(1, &VBO);
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
			glBindVertexArray(this->VAO);
			glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(GLfloat), (void*)0);
			glEnableVertexAttribArray(0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			glDeleteBuffers(1, &VBO);
		}

	public:
		RectRenderer() {}
		RectRenderer(Shader &s) :shader(s) { this->initRenderData(); }
		~RectRenderer(){}

		void Draw(float x, float y, float w, float h, glm::vec4 color)
		{
			this->shader.use();
			glm::mat4 model;
			model = glm::translate(model, glm::vec3(x, y, 0.0f));
			model = glm::scale(model, glm::vec3(w, h, 1.0f));

			this->shader.setMat4("model", model);
			this->shader.setVec4("color", color.x, color.y, color.z, color.w);

			// render container
			glBindVertexArray(this->VAO);
			glDrawArrays(GL_TRIANGLES, 0, 6);
		}
	};

}

#endif //FRATERNITYGL