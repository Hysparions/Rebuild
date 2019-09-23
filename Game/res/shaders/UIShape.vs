#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;

out vec4 Color;

uniform mat4 projection;
uniform mat4 model;

void main(){

	float red = (color.x+128.0f)/255.f;
	float green = (color.y+128.0f)/255.f;
	float blue = (color.z+128.0f)/255.f;
	float alpha = (color.w+128.0f)/255.f;
	Color = vec4(red,green,blue, alpha) ;
	gl_Position = projection * model * vec4(position.x, position.y, 0.0f, 1.0f);

}
