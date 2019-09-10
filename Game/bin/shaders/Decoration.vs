#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec4 color;

uniform mat4 projectionView;
uniform float viewDistance;
uniform vec3 offset;

out vec3 worldPos;
out vec3 Color;
out vec3 Normal;
out float visibility;

void main(){
	worldPos = position+offset;
	float distance = length(worldPos.xz);
	visibility = distance < (viewDistance - 15.f) ? 1.0f : (viewDistance-distance)/15.f;
	
	float red = color.x+128.0f;
	float green = color.y+128.0f;
	float blue = color.z+128.0f;
	Color = vec3(red/255.f,green/255.f,blue/255.f) ;
	Normal = normal;
	gl_Position = projectionView*vec4(worldPos, 1.0f);
}