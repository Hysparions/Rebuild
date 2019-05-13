#include "ProgramHandler.h"
#include <glad.h>
#include <glfw3.h>
#include <iostream>
#include "OpenGL\Camera.h"
#include "OpenGL\Shader.h"
#include "OpenGL\Model.h"
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
#include "OpenGL\FraternityGL.h"
#include "OpenGL\FraternityGUI.h"
#include <cmath>

// camera
Camera camera(glm::vec3(0.0f, 10.0f, 50.0f));
bool cameraIsActive = false;
float lastX = 1600 / 2.0f;
float lastY = 900 / 2.0f;
bool firstMouse = true;

// timing
float deltaTime = 0.0f;
float lastFrame = 0.0f;

//SCREEN SIZE
float SCREEN_W = 1600;
float SCREEN_H = 900;
float lastw = 1600;
float lasth = 900;

//mouse
int mousex = 0;
int mousey = 0;
int OldState;

///CONSTRUCTORS AND DESTRUCTORS
ProgramHandler::ProgramHandler() : window(NULL), spriteShader(), modelShader(), colorShader(), rectRenderer(), time(0, 0, 0, 0), PlayerName("")
{
	///OPENGL LOAD
	window = glfwCreateWindow(SCREEN_W, SCREEN_H, "LearnOpenGL", NULL, NULL);
	if (window == NULL) { glfwTerminate(); }										///Return error if the window couldn't be created
	glfwMakeContextCurrent(window);													///Bound GLFW Context to the window
	if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress)) { glfwTerminate(); }	///Initialization of the function pointers for OPENGL

	///FUNCTION BINDING
	glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);
	glfwSetCursorPosCallback(window, mouse_callback);
	glfwSetScrollCallback(window, scroll_callback);
	///MOUSE
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	///FACE CULLING
	glEnable(GL_CULL_FACE);

	///SHADERS
	spriteShader = LoadShader("Sprite");
	modelShader = LoadShader("Model");
	colorShader = LoadShader("Color");
	///2D RECT RENDERER
	rectRenderer = Fra::RectRenderer(colorShader);
	//glfwSwapInterval(0); ///UNLIMIT FPS

}

ProgramHandler::~ProgramHandler()
{

}

bool ProgramHandler::RunMenu()
{

	
	
	Model chest("Ressources/Objects/Entities/Players/Character.obj");
	Fra::Texture t, back;
	t.Load("Ressources/Interface/Menu/GUI.png");
	back.Load("Ressources/Interface/Menu/Back.png");
	Fra::Sprite gui(glm::vec2(SCREEN_W/6, SCREEN_H/6), spriteShader, &t);
	Fra::Sprite Back(glm::vec2(SCREEN_W/2, SCREEN_H/2), spriteShader, &back);
	float currentFrame = 0.0f;

	Fra::ScaledButton NewWorld(glm::vec2(SCREEN_W / 6, SCREEN_H / 8), glm::vec4(0, 0, 147, 59));
	Fra::ScaledButton LoadWorld(glm::vec2(SCREEN_W / 6, 3 * SCREEN_H / 8), glm::vec4(0, 59, 147, 59));
	Fra::ScaledButton JoinWorld(glm::vec2(SCREEN_W / 6, 5 * SCREEN_H / 8), glm::vec4(0, 118, 147, 59));
	
	// RENDERING
	while (!glfwWindowShouldClose(window))
	{
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glm::mat4 orthoProjection;
		orthoProjection = glm::ortho(0.0f, SCREEN_W, SCREEN_H, 0.0f, -1.0f, 1.0f);
		this->spriteShader.use();
		this->spriteShader.setMat4("projection", orthoProjection);
		this->spriteShader.setVec4("color", 1.0f, 1.0f, 1.0f, 1.0f);
		this->colorShader.use();
		this->colorShader.setMat4("projection", orthoProjection);

		//Camera processing
		currentFrame = (float)glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;

		// input
		processInput(window);

		// render
		glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glDisable(GL_DEPTH_TEST);
		Back.Draw();
		glEnable(GL_DEPTH_TEST);

		this->modelShader.use();
		this->modelShader.setVec3("light.direction",0.1f, -1.0f, 0.1f);
		glm::mat4 projection = glm::perspective(glm::radians(camera.Zoom), SCREEN_W / SCREEN_H , 0.1f, 500.0f);
		glm::mat4 view = camera.GetViewMatrix();
		modelShader.setMat4("projection", projection);
		modelShader.setMat4("view", view);

		//render the loaded model
		glm::mat4 model;
		model = glm::rotate(model, glm::radians(90*currentFrame), glm::vec3(0.0f, 1.0f, 0.0f));
		modelShader.setMat4("model", model);
		chest.Draw(modelShader);


		///SPRITE RENDERING
		glDisable(GL_DEPTH_TEST);
		rectRenderer.Draw(0.0f, 0.0f, SCREEN_W / 3, SCREEN_H, glm::vec4(0.2f, 0.2f, 0.2f, 0.5f));			//Rendering the button transparent background
		
		NewWorld.Draw(gui, glm::vec2(mousex, mousey));
		LoadWorld.Draw(gui, glm::vec2(mousex, mousey));
		JoinWorld.Draw(gui, glm::vec2(mousex, mousey));
		glEnable(GL_DEPTH_TEST);

		//SCREEN SWAP & EVENT POLLING
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	return false;
}


void ProgramHandler::RunWorld()
{
	/*glfwSetTime(0.0f);
	std::vector<glm::mat4> model;
	for (float i = 0.0f; i < 320.0f; i++) {
	for (float j = 0.0f; j < 320.0f; j++) {
	glm::mat4 m;
	m = glm::translate(m, glm::vec3(i, (float)floor(p.noise(i, j, 0.1f)), j));
	model.push_back(m);
	}
	}
	std::cout << glfwGetTime() << std::endl;


	unsigned int buffer;
	glGenBuffers(1, &buffer);
	glBindBuffer(GL_ARRAY_BUFFER, buffer);
	glBufferData(GL_ARRAY_BUFFER, 102400 * sizeof(glm::mat4), &model[0], GL_STATIC_DRAW);

	unsigned int VAO = ressources.getVAOof(0);
	glBindVertexArray(VAO);
	// vertex Attributes
	GLsizei vec4Size = sizeof(glm::vec4);
	glEnableVertexAttribArray(3);
	glVertexAttribPointer(3, 4, GL_FLOAT, GL_FALSE, 4 * vec4Size, (void*)0);
	glEnableVertexAttribArray(4);
	glVertexAttribPointer(4, 4, GL_FLOAT, GL_FALSE, 4 * vec4Size, (void*)(vec4Size));
	glEnableVertexAttribArray(5);
	glVertexAttribPointer(5, 4, GL_FLOAT, GL_FALSE, 4 * vec4Size, (void*)(2 * vec4Size));
	glEnableVertexAttribArray(6);
	glVertexAttribPointer(6, 4, GL_FLOAT, GL_FALSE, 4 * vec4Size, (void*)(3 * vec4Size));

	glVertexAttribDivisor(3, 1);
	glVertexAttribDivisor(4, 1);
	glVertexAttribDivisor(5, 1);
	glVertexAttribDivisor(6, 1);

	glBindVertexArray(0);

	float currentFrame;
	///Main Loop:
	while (!glfwWindowShouldClose(window))
	{
		// per-frame time logic
		// --------------------
		currentFrame = (float)glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;

		time.add(deltaTime);

		// input
		processInput(window);
		//Rendering
		glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// view/projection transformations
		glm::mat4 projection = glm::perspective(glm::radians(camera.Zoom), 1920.0f / 1080.0f, 0.1f, 500.0f);
		glm::mat4 view = camera.GetViewMatrix();
		ressources.Draw(shader, projection, view);

		// glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
		// -------------------------------------------------------------------------------
		glfwSwapBuffers(window);
		glfwPollEvents();
	}*/
}

void processInput(GLFWwindow *window)
{
	if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
		glfwSetWindowShouldClose(window, true);

	if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
		camera.ProcessKeyboard(FORWARD, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
		camera.ProcessKeyboard(BACKWARD, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
		camera.ProcessKeyboard(LEFT, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
		camera.ProcessKeyboard(RIGHT, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
		camera.ProcessKeyboard(UP, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
		camera.ProcessKeyboard(DOWN, deltaTime);		
}

// glfw: whenever the window size changed (by OS or user resize) this callback function executes
// ---------------------------------------------------------------------------------------------
void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
		glViewport(0, 0, width, height);
		SCREEN_W = width;
		SCREEN_H = height;
}

// glfw: whenever the mouse moves, this callback is called
// -------------------------------------------------------
void mouse_callback(GLFWwindow* window, double xpos, double ypos)
{
	if (firstMouse)
	{
		lastX = (float)xpos;
		lastY = (float)ypos;
		firstMouse = false;
	}
	
	mousex = xpos;
	mousey = ypos;

	if (cameraIsActive)
	{
		float xoffset = (float)xpos - lastX;
		float yoffset = lastY - (float)ypos; // reversed since y-coordinates go from bottom to top

		lastX = (float)xpos;
		lastY = (float)ypos;

		camera.ProcessMouseMovement(xoffset, yoffset);
	}
}

// glfw: whenever the mouse scroll wheel scrolls, this callback is called
// ----------------------------------------------------------------------
void scroll_callback(GLFWwindow* window, double xoffset, double yoffset)
{
	camera.ProcessMouseScroll((float)yoffset);
}

//LoadShader function
Shader LoadShader(std::string ShaderName)
{
	if (ShaderName == "Model")	 // Model/Mesh Shader
	{
		Shader s("Shaders/Model.vs", "Shaders/Model.fs");
	
		s.use();
		s.setVec3("light.direction", 0.05f, -1.0f, -0.5f);
		s.setVec3("viewPos", camera.Position);
		// Directional Light properties
		s.setVec3("light.ambient", 0.4f, 0.4f, 0.4f);
		s.setVec3("light.diffuse", 0.7f, 0.7f, 0.7f);
		s.setVec3("light.specular", 1.0f, 1.0f, 1.0f);
		// material properties
		s.setFloat("material.shininess", 8.0f);
		return s;
	}
	else if (ShaderName == "Sprite")
	{
		Shader s("Shaders/Sprite.vs", "Shaders/Sprite.fs");
		return s;
	}
	else if (ShaderName == "Color")
	{
		Shader s("Shaders/Color.vs", "Shaders/Color.fs");
		return s;
	}
	return Shader();
}

