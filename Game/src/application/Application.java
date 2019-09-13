package application;

import recover.Recover;
/**
 * Class containing the main Function of the program
 * @author louis
 *
 */
public class Application {

	/**
	 * Main function
	 * @param args Entry arguments
	 */
	public static void main(String[] args) {
		

		// Creation Launch of the game engine
		Recover game = new Recover(1600, 900, "Recover - The Last Summer");
		game.launch();
		

	}
}
