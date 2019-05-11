#version 330 core

in vec3 Norm;
in vec3 FragPos;
in vec3 color;
in float visibility;

struct Material{

	sampler2D diffuse1;
};

//Structure defining the directional Light for the Sun
struct DirLight{

	vec3 direction;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform vec3 viewPos;
uniform Material material;
uniform DirLight Sun;

out vec4 FragColor;

void main(){

	if(visibility < 0.0f){discard;} 

    // ambient
    vec3 Ambient = Sun.ambient * color;
    
    // diffuse 
    vec3 norm = normalize(Norm);
    vec3 lightDir = normalize(-Sun.direction);  
    float diff = max(dot(Norm, lightDir), 0.0);
    vec3 Diffuse = Sun.diffuse  *diff* color;  
    
    // specular
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);  
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 4.0f);
    vec3 Specular = Sun.specular * spec * color;  
        
    vec3 result = Ambient + Diffuse + Specular;
    FragColor = vec4(result, 1.0f * visibility);
}
