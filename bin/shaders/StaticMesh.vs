#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec3 Normal;
layout (location = 2) in vec2 TexCoords;

out vec2 TexPos;
out vec3 Norm;
out vec3 FragPos;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

void main()
{
	TexPos = TexCoords;
	FragPos = vec3(model*vec4(Position, 1.0f));
	Norm = mat3(transpose(inverse(model))) * Normal; 
	gl_Position = projection*view*model*vec4(Position, 1.0f);
}