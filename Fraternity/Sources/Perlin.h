#ifndef PERLIN_H
#define PERLIN_H
#include <vector>

class Perlin
{
private:
	///Private Attribute
	std::vector<int> p;
	const float frequency;
	const unsigned int octave;
	unsigned int amplitude;

	///Private methods
	//Fade function for Perlin algorithm
	double fade(double t);
	//Calculating function for perlin algorithm
	double lerp(double t, double a, double b);
	//Gradient function for perlin algorithm
	double grad(int hash, double x, double y, double z);

public:
	//Constructors and destructor
	Perlin();
	Perlin(unsigned int seed, const float Frequency, const unsigned int Octave, unsigned int Amplitude);
	~Perlin();
	// Get a noise value, for 2D images z can have any value
	double noise(double x, double y, double z);
	//Getters for Octave and Frequency
	const float getFrequency();
	unsigned int getAmplitude();
	unsigned int getOctave();

};


#endif // !PERLIN_H
