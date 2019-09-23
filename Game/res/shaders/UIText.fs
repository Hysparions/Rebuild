#version 330 core

in vec2 TexPos;
in vec4 Color;

out vec4 FragColor;

uniform sampler2D font;
uniform int fontSize;

uniform float edge;
const float width = 0.45;

void main(){

	// Distance field calculation
	float distance = 1.0f- texture(font, TexPos).a;
	float opacity= 1.0f- smoothstep(width, width+edge, distance);
	
	// Compute final color
 	FragColor = vec4(Color.x, Color.y, Color.z, Color.w*opacity); 
}