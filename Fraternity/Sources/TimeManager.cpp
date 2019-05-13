#include "TimeManager.h"

#include <string>
#include <sstream>
#include <iostream>
//Constructor
TimeManager::TimeManager(unsigned int fraction, unsigned int day, unsigned int season, unsigned int year) : buffer(0.0f), Fraction(fraction), Day(day), Season(season), Year(year){}
//Destructor
TimeManager::~TimeManager(){}

void TimeManager::add(float deltaTime)
{
	buffer += deltaTime;
	if ( buffer > 0.1f )
	{
		buffer -= 0.1f;
		Fraction += 1;
		if (Fraction == 14400) { Fraction = 0; Day += 1; }
		if (Day == 31) { Day = 1;  Season += 1; }
		if (Season == 4) { Season = 0; Year += 1; }
	}
}

std::string TimeManager::getTime()
{
	std::stringstream ss;
	std::string s;
	if (Season == 0) { s = "Spring"; }
	else if (Season == 1) { s = "Summer"; }
	else if (Season == 2) { s = "Autumn"; }
	else if (Season == 3) { s = "Winter"; }
	ss << "The Actual Date is:  Day " << Day << " of " << s << " of year " << Year << " and the time is " << Fraction / 600 << ":" << ((Fraction%600)/10)%60 << " (" << Fraction << ")";
	return ss.str();
}