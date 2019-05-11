#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in float flexibility;

uniform mat4 projection;
uniform mat4 view;
uniform float viewDistance;
uniform vec2 center;
uniform float time;

out vec3 worldPos;
out vec3 Color;
out vec3 Normal;
out float visibility;

const float WaveLength = 1000;
const float amp = 0.05f;
const float PI = 3.242592;

float calculateX(float x, float time, float flexibility){
	float rad = (mod(x*x, 0.1)/WaveLength + time * 0.82314 * mod(x*0.01, 1.321))* PI;
	return sin(rad)*amp*flexibility;
}

float calculateZ(float z, float time, float flexibility){
	float rad = (mod(z*z, 0.123)/WaveLength + time * 0.72178 * mod(z*0.02, 1.213))* PI;
	return sin(rad)*amp*flexibility;
}

void main(){

	float xOffset = calculateX(position.x, time, flexibility);
	float zOffset = calculateZ(position.z, time, flexibility);
	
	float distance = length(position.xz - center);
	if(distance < viewDistance - 15.f) { visibility = 1.0f; }
	else{ visibility = (viewDistance)/15.f - distance/15.f; }
	
	worldPos = position;
	Color = color;
	Normal = normal;
	gl_Position = projection*view*vec4(vec3(position.x+xOffset, position.y, position.z+zOffset), 1.0f);
}