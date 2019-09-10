#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec3 Normal;
layout (location = 2) in vec3 OldColor;
layout (location = 3) in vec3 NewColor;
layout (location = 4) in float Time;

flat out vec3 color;
flat out vec3 Norm;
out vec3 FragPos;
out float visibility;

uniform mat4 projectionView;

uniform float viewDistance;
uniform vec3 offset;
uniform vec2 center;
uniform vec4 plane;
uniform float currentTime;
uniform float swapTime;

void main()
{
	vec4 worldPosition = vec4(Position+offset, 1.0f);
	
	float distance = length(worldPosition.xz - center);
	visibility = distance < (viewDistance - 15.f) ? 1.0f : (viewDistance-distance)/15.f;
	
	visibility = visibility > 0.0f ? visibility: 0.0f;
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	float delta = (currentTime-Time)/swapTime;
	delta = delta < 1.0f ? (delta > 0.0f ? delta : 0.0f)  : 1.0f;
	color = mix(OldColor, NewColor, delta);
	FragPos = Position + offset;
	Norm = Normal; 
	gl_Position = projectionView*worldPosition;
}