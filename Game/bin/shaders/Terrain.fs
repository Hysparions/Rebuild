#version 330 core

flat in vec3 Norm;
in vec3 FragPos;
flat in vec3 color;
in float visibility;

//Structure defining the directional Light for the Sun
struct DirLight{

	vec3 direction;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform DirLight Sun;

out vec4 FragColor;

void main(){

	if(visibility < 0.0f){discard;} 

	//Blinn-Phong Vectors
    vec3 lightDir = normalize(-Sun.direction); 
    vec3 viewDir = normalize(-FragPos); 
    vec3 halfway = normalize(lightDir+viewDir);

	// ambient
    vec3 Ambient = Sun.ambient * color;
    
    // diffuse 
    float diff = max(dot(Norm, lightDir), 0.0);
    vec3 Diffuse = Sun.diffuse  *diff* color;  
    
    // specular
    float spec = pow(max(dot(Norm, halfway), 0.0), 4);
    vec3 Specular = Sun.specular * spec * color;  
        
    vec3 result = Ambient + Diffuse + Specular;
    FragColor = vec4(result, 1.0f * visibility);
}
