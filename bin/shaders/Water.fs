#version 330 core

in vec2 ndc;
in vec3 toCamera;
in vec3 normal;
in vec3 viewPosition;
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
uniform sampler2D refraction;
uniform sampler2D depth;
uniform vec2 nearFar;
uniform vec2 center;

uniform DirLight Sun;

const float shininess = 4.0f;
const float borderSoftness = 5.0f;
const float troubleFactor = 15.0f;
const float troubleMin = 0.0f;
const float troubleMax = 0.9f;
const vec3 waterColor = vec3(0.654f, 0.854f, 0.8f);

float toLinear(float z){
	float near = nearFar.x;
	float far = nearFar.y;
	return 2.0f*(near*far) / (far+near -(2.0f*z -1.0f) * (far-near));
}

float calculateWaterAmount(vec2 texCoords){
	return toLinear(texture(depth, texCoords ).r) - toLinear(gl_FragCoord.z);
}

float calculateFog(){
return 1.0f;
}

vec3 trouble(vec3 refractColor, float waterAmount){
	return mix(refractColor, waterColor, troubleMin + smoothstep(0.0f, troubleFactor, waterAmount)*(troubleMax-troubleMin));
}

vec2 clampTexPos(float x, float y){
	vec2 texPos = vec2(x, y);
	return clamp(texPos, 0.001, 0.999); 
}

void main(){

	if(visibility < 0.0f){discard;} 

	//Texture Coordinates Calculations
	vec2 reflectTexCoords = clampTexPos(ndc.x , (1-ndc.y));
	vec2 refractTexCoords = clampTexPos(ndc.x , (ndc.y) );
	
	//Depth Testing
	float waterAmount = calculateWaterAmount(refractTexCoords);

	//Texture Fragment Color
	vec3 reflectionColor = texture(reflection, reflectTexCoords).rgb;
	vec3 refractionColor = trouble(texture(refraction, refractTexCoords).rgb, waterAmount);
	
	vec3 viewVector = normalize(toCamera);
	
	//Calculate Ambient Lighting
	vec3 AmbientReflect = Sun.ambient * reflectionColor;
	vec3 AmbientRefract = Sun.ambient * refractionColor;

    //Calculate Diffuse Lighting 
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(-Sun.direction);  
    float diffuse = max(dot(norm, lightDir), 0.0);
    
    vec3 DiffuseReflect = Sun.diffuse  *diffuse * reflectionColor;  
    vec3 DiffuseRefract = Sun.diffuse  *diffuse * refractionColor;  
    
    //Calaculate Specular Lighting
    vec3 viewDir = normalize(viewPosition - WorldPos.xyz);
    vec3 reflectDir = reflect(-lightDir, norm);  
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 4);
    vec3 SpecularReflect = Sun.specular * spec * reflectionColor;  
    vec3 SpecularRefract = Sun.specular * spec * refractionColor;  
    
    //Compute final result
    vec4 ResultReflect = vec4(AmbientReflect + DiffuseReflect + SpecularReflect, 1.0);
    vec4 ResultRefract = vec4(AmbientRefract + DiffuseRefract + SpecularRefract, 1.0);
	
	//Output both color and alpha results
	FragColor =  ResultReflect;
	float alpha = amp;
	if(alpha <=0.5f){alpha = 0.5;}
	FragColor.a =  alpha* visibility* (1.0f-(0.7*dot(viewVector, vec3(0.0, 1.0, 0.0))));
}
