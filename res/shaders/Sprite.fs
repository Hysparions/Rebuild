#version 330 core

in vec2 TexPos;
in vec4 Color;

out vec4 FragColor;

uniform sampler2D image;

void main(){
	
	FragColor = Color * texture(image, TexPos); 
}