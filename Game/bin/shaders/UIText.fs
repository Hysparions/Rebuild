#version 330 core

in vec2 TexPos;
in vec3 Color;

out vec4 FragColor;

uniform sampler2D font;

const float edge = 0.1;
const float width = 0.55;

void main(){
	float distance = 1.0f- texture(font, TexPos).a;
	float alpha = 1.0f- smoothstep(width, width+edge, distance);
 	FragColor = vec4(Color ,alpha); 
}