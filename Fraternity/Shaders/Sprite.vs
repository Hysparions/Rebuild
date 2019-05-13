#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoords;
out vec4 Color;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 spriteProjection;
uniform vec4 color;

void main()
{
	gl_Position = projection *model *vec4(aPos, 0.0f, 1.0f);
	vec4 buffer = vec4(aTexCoord.x, aTexCoord.y, 1.0f, 1.0f);
	TexCoords = vec2(spriteProjection * buffer);
	Color = color;
}