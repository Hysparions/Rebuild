#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Points;

uniform mat4 projectionView;
uniform vec3 offset;
uniform float time;
uniform float waterLevel;
uniform vec2 center;
uniform float viewDistance;

out vec2 ndc;
out vec3 normal;
out vec3 toCamera;
out vec4 WorldPos;
out float visibility;
out float amp;

const float WaveLength = 1000;
const float transparency = 5;
const float PI = 3.14159265358979323846264;


vec3 distract(vec3 vertex , float amp)
{

	float radX = fract(sin(dot(vertex.xz ,vec2(12.9898,78.233))) * 43758.5453);
	float radY = fract(cos(dot(vertex.xz ,vec2(17.5353,-29.233))) * 19248.621897);

	vertex.y += (cos(radX*6*time+radY) + sin(radY*5*time+radX) )*0.25* amp;
	vertex.x += (cos(radY*4*time+radX) + sin(radY*7*time+radY) )*0.15* amp;
	vertex.z += (cos(radX*2*time+radX) + sin(radX*3*time+radY) )*0.15* amp;
	return vertex;
}

vec3 calculateNormal(vec3 vertex0, vec3 vertex1, vec3 vertex2){
	return cross(vertex1 - vertex0, vertex2 - vertex0);
}

void main()
{ 
	amp = (waterLevel - Position.y)/transparency;
	amp = clamp(amp, 0.0f, 1.0f);

	// Generate the vertices
	vec3 vertex0 = vec3(Position.x, 0.0f, Position.z);
	vec3 vertex1 = vertex0 + vec3(Points.x, 0.0, Points.y);
	vec3 vertex2 = vertex0 + vec3(Points.z, 0.0, Points.w);
	
	vec4 ClipSpace = projectionView*vec4(vertex0+offset, 1.0f);
	ndc = ((ClipSpace.xy/ClipSpace.w)/2.0 + 0.5);
	
	// Distract Height position of the vertices
	vertex0 = distract(vertex0, amp);
	vertex1 = distract(vertex1, amp);
	vertex2 = distract(vertex2, amp);

	vec4 WorldPos = vec4(vertex0+offset, 1.0f);	
	
	// Pass to Camera vector
	toCamera = - WorldPos.xyz;
		
	// Visibility circle
	float distance = length(WorldPos.xz);
	visibility = distance < (viewDistance - 15.f) ? 1.0f : (viewDistance/15.f - distance/15.f);
	
	// Calculating normals
	normal =calculateNormal(vertex0, vertex1, vertex2);
	

	// Compute vertex Position
	gl_Position = projectionView*WorldPos;
	
}