#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec4 color;

uniform mat4 projectionView;
uniform float viewDistance;
uniform float time;
uniform vec3 offset;
uniform vec4 plane;

out vec3 worldPos;
out vec3 Color;
out vec3 Normal;
out float visibility;

vec3 distract(vec3 vertex , float amp)
{

	float radX = fract(sin(dot(vertex.xz ,vec2(12.9898,78.233))) * 8.5453);
	float radY = fract(cos(dot(vertex.xz ,vec2(17.5353,-29.233))) * 18.621897);
	vertex.x += (cos(radY*time+radX) + sin(radY*2*time+radY) )* amp;
	vertex.z += (cos(radX*2*time+radX) + sin(radX*3*time+radY) )* amp;
	return vertex;
}

void main(){

	worldPos = position+offset;

	float distance = length(worldPos.xz);
	
	visibility = distance < (viewDistance - 15.f) ? 1.0f : (viewDistance-distance)/15.f;
	gl_ClipDistance[0] = dot(vec4(worldPos,1.0), plane);	
		
		
	float red = (color.x+128.0f)/255.f;
	float green = (color.y+128.0f)/255.f;
	float blue = (color.z+128.0f)/255.f;
	float alpha = (color.w+128.0f)/255.f;
	Color = vec3(red,green,blue) ;
	Normal = normal;
	
	vec3 finalPos = distract(position, alpha) + offset;
	gl_Position = projectionView*vec4(finalPos, 1.0f);
}