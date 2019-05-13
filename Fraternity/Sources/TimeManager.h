#ifndef TIME_MANAGER_H
#define TIME_MANAGER_H
#include <string>

class TimeManager {

private:
	/// Buffer for time in millisecond
	float buffer;
	/// 0 - 14399 Counter that counts the fraction of the day 
	unsigned int Fraction;
	/// 1 - 30 Counter of the Days
	unsigned int Day;
	/// Season
	unsigned int Season;
	/// Counter of the Year
	unsigned int Year;

public:
	//Constructor & Destructor
	TimeManager(unsigned int fraction, unsigned int day, unsigned int season, unsigned int year);
	~TimeManager();

	void add(float deltaTime);
	std::string getTime();
};

#endif //TIME_MANAGER_H