#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 texPos;

out vec2 TexPos;
out vec3 Color;

uniform mat4 projection;

void main(){

	TexPos.x = texPos.x;
	TexPos.y = 1.0f-texPos.y;
	Color = color;
	gl_Position =  projection*vec4(position.x, position.y, 0.0f, 1.0f);

}
