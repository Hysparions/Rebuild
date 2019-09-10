#version 330 core

layout (location = 0) in vec4 vertex;

out vec2 TexPos;
out vec4 Color;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 spriteProjection;
uniform vec4 color;

void main(){

	//TexPos = vec2(vertex.z, vertex.w);
	TexPos = vec2(spriteProjection * vec4(vertex.z, vertex.w, 1.0f, 1.0f));
	Color = color;
	gl_Position =  projection*model * vec4(vertex.x, vertex.y, 1.0f, 1.0f);

}
