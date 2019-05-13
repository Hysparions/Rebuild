#include <glad.h>
#include <glfw3.h>
#include <iostream>

#include "ProgramHandler.h"

#include "OPENGL\stb\stb_image.h"
#include "OPENGL\Shader.h"

#include <iostream>

int main()
{
	// glfw: initialize and configure
	// ------------------------------
	glfwInit();
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

	
	ProgramHandler p;
	p.RunMenu();


	// glfw: terminate, clearing all previously allocated GLFW resources.
	// ------------------------------------------------------------------
	glfwTerminate();
	return 0;
}
