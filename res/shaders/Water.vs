#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Points;

uniform mat4 projection;
uniform mat4 view;
uniform vec3 viewPos;
uniform float time;
uniform float waterLevel;
uniform vec2 center;
uniform float viewDistance;

out vec2 ndc;
out vec3 normal;
out vec3 toCamera;
out vec4 WorldPos;
out vec3 viewPosition;
out float visibility;
out float amp;

const float WaveLength = 1000;
const float transparency = 5;
const float PI = 3.242592;

float calculateOffset(float x, float z, float amp)
{
	float radX = (mod((x+z)*x, 0.1)/WaveLength + time * 0.4381f * mod(x*0.01*z, 1.5)) * 2.0 * PI;
	float radY = (mod(2*x*z, 0.2)/WaveLength + time * 0.64372f * mod(z*0.01*x, 0.8)) * 2.0 * PI;

	return (sin(radX) + cos(radY) )*0.2 * amp;
}

vec3 calculateNormal(float amp){
	vec3 point0 = vec3(Position.x, (waterLevel + calculateOffset(Position.x, Position.z, amp)), Position.z);
	vec3 point1 = vec3(Points.x, (waterLevel + calculateOffset(Points.x, Points.y, amp)), Points.y);
	vec3 point2 = vec3(Points.z, (waterLevel + calculateOffset(Points.z, Points.w, amp)), Points.w); 
	return cross(point1 - point0, point2 - point0);
}

void main()
{ 
	amp = (waterLevel - Position.y)/transparency;
	if(amp > 1.0f){ amp = 1.0f;}
	else if(amp < 0.0f){amp = 0.0f;}

	float yOffset = calculateOffset(Position.x, Position.z, amp);
	vec3 pos = vec3(Position.x, waterLevel + yOffset ,Position.z);
	vec4 WorldPos = vec4(pos, 1.0f);	
	toCamera = viewPos - WorldPos.xyz;
	
	
		
	float distance = length(WorldPos.xz - center);
	if(distance < viewDistance - 15.f) { visibility = 1.0f; }
	else{ visibility = (viewDistance)/15.f - distance/15.f; }
	
	
	
	normal =calculateNormal( amp);
	viewPosition = viewPos;
	vec4 ClipSpace = projection*view*vec4(Position.x, waterLevel, Position.z, 1.0f);
	ndc = ((ClipSpace.xy/ClipSpace.w)/2.0 + 0.5);
	gl_Position = projection*view*WorldPos;
	
}