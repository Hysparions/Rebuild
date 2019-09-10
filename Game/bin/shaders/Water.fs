#version 330 core

in vec2 ndc;
in vec3 toCamera;
in vec3 normal;
in vec4 WorldPos;
in float visibility;
in float amp;
out vec4 FragColor;

struct DirLight{

	vec3 direction;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform sampler2D reflection;
uniform vec2 nearFar;
uniform vec2 center;

uniform DirLight Sun;

const float shininess = 4.0f;

vec2 clampTexPos(float x, float y){
	vec2 texPos = vec2(x, y);
	return clamp(texPos, 0.001, 0.999); 
}

void main(){


	//Texture Coordinates Calculations
	vec2 reflectTexCoords = clampTexPos(ndc.x , (1-ndc.y));

	//Texture Fragment Color
	vec3 reflectionColor = texture(reflection, reflectTexCoords).rgb;
	
	
	vec3 viewVector = normalize(toCamera);
	
	//Calculate Ambient Lighting
	vec3 AmbientReflect = Sun.ambient * reflectionColor;

    //Calculate Diffuse Lighting 
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(-Sun.direction);  
    float diffuse = max(dot(norm, lightDir), 0.0);
    
    vec3 DiffuseReflect = Sun.diffuse  *diffuse * reflectionColor;  
    
    //Calaculate Specular Lighting
    vec3 viewDir = normalize(-WorldPos.xyz);
    vec3 halfway = vec3(lightDir+viewDir);  
    float spec = pow(max(dot(norm, halfway), 0.0), 4);
    vec3 SpecularReflect = Sun.specular * spec * reflectionColor;  
   
    
    //Compute final result
    vec4 ResultReflect = vec4(AmbientReflect + DiffuseReflect + SpecularReflect, 1.0);
    
	
	//Output both color and alpha results
	FragColor =  ResultReflect;
	float alpha = amp <= 0.7f ? 0.7f : amp;
	FragColor.a =  alpha* visibility* (1.0f-(0.8*abs(dot(viewVector, vec3(0.0, 1.0, 0.0)))));
}
