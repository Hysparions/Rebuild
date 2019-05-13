#ifndef FRATERNITYGUI_H
#define FRATERNITYGUI_H

#include <string>
#include <glad.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "stb/stb_image.h"
#include "FraternityGL.h"

namespace Fra {

	class ScaledButton {
	private:
		glm::vec2 position;
		glm::vec4 rectangle;
		float scale;

	public:
		ScaledButton(glm::vec2 Position, glm::vec4 rectTexture) :position(Position), rectangle(rectTexture), scale(3.0f){}
		~ScaledButton(){}

		bool Draw(Sprite &s, glm::vec2 mousePos)
		{
			s.setScale(scale);
			s.setPosition(position.x, position.y);
			s.setRectangle(rectangle.x, rectangle.y, rectangle.z, rectangle.w);
			if (mousePos.x > position.x - ((scale*rectangle.z) / 2.0f) && mousePos.x < position.x + ((scale*rectangle.z) / 2.0f) && mousePos.y < position.y + ((scale*rectangle.w) / 2.0f) &&  mousePos.y > position.y - ((scale*rectangle.w) / 2.0f))
			{
				scale = 3.3f;
			}
			else
			{
				scale = 3.0f;
			}
			s.Draw();
			return false;
		}
	};

}

#endif //FRATERNITYGUI_H