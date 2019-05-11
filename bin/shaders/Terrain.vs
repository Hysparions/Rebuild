#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec3 Normal;
layout (location = 2) in vec3 OldColor;
layout (location = 3) in vec3 NewColor;
layout (location = 4) in float Time;

out vec3 color;
out vec3 Norm;
out vec3 FragPos;
out float visibility;

uniform mat4 projection;
uniform mat4 view;

uniform float viewDistance;
uniform vec2 center;
uniform vec4 plane;
uniform float currentTime;
uniform float swapTime;

void main()
{
	vec4 worldPosition = vec4(Position, 1.0f);
	
	float distance = length(worldPosition.xz - center);
	if(distance < viewDistance - 15.f) { visibility = 1.0f; }
	else{ visibility = (viewDistance)/15.f - distance/15.f; }
	
	if(visibility < 0.0f) {visibility = 0.0f;}
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	float delta = (currentTime-Time)/swapTime;
	if(delta > 1){delta = 1;}
	else if(delta<0){delta = 0;}
	color = mix(OldColor, NewColor, delta);
	FragPos = Position;
	Norm = Normal; 
	gl_Position = projection*view*vec4(Position, 1.0f);
}