#version 330 core

in vec3 Normal;
in vec3 worldPos;
in vec3 Color;
in float visibility;

struct Material{
	float shininess;
};

//Structure defining the directional Light for the Sun
struct DirLight{

	vec3 direction;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform Material material;
uniform DirLight Sun;

out vec4 FragColor;

void main(){

	//Blinn-Phong Vectors
    vec3 lightDir = normalize(-Sun.direction); 
    vec3 viewDir = normalize(-worldPos); 
    vec3 halfway = normalize(lightDir+viewDir);

	// ambient
    vec3 Ambient = Sun.ambient * Color;
    
    // diffuse 
    float diff = max(dot(Normal, lightDir), 0.0);
    vec3 Diffuse = Sun.diffuse  *diff* Color;  
    
    // specular
    float spec = pow(max(dot(Normal, halfway), 0.0), material.shininess);
    vec3 Specular = Sun.specular * spec * Color;  
    
    
    vec3 result = Ambient + Diffuse + Specular;
    FragColor = vec4(result, 1.0f*visibility);
}
