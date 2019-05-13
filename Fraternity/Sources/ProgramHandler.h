#ifndef PROGRAMHANDLER_H
#define PROGRAMHANDLER_H
#include <glad.h>
#include <glfw3.h>
#include <iostream>
#include <string>

#include "World.h"
#include "RessourcesManager.h"
#include "TimeManager.h"
#include "OpenGL\Shader.h"
#include "OpenGL\FraternityGL.h"

class ProgramHandler
{
private:
	///OPEN GL MANAGEMENT
	GLFWwindow * window;
	Shader spriteShader;
	Shader modelShader;
	Shader colorShader;
	Fra::RectRenderer rectRenderer;

	///Time of the Game
	TimeManager time;
	///PLAYER DATA HANDLING
	std::string PlayerName;

public:
	///CONSTRUCTORS & DESTRUCTORS
	ProgramHandler();
	~ProgramHandler();

	///METHODS
	//Method to display the Menu and select a World to go on
	bool RunMenu();
	//Method to run the Selected World with the Selected Character
	void RunWorld();

};

void framebuffer_size_callback(GLFWwindow* window, int width, int height);
void mouse_callback(GLFWwindow* window, double xpos, double ypos);
void scroll_callback(GLFWwindow* window, double xoffset, double yoffset);
void processInput(GLFWwindow *window);
Shader LoadShader(std::string ShaderName);


#endif //PROGRAMHANDLER_H