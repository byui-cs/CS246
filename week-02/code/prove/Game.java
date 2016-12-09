import java.util.ArrayList;
import java.util.List;

/**
* The starting class of the game. This class initializes the game world and all
* of thre creatures within it.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* @see Creature
*/
public class Game {
	
	World _theWorld;
	
	/**
    * Launches the game.
    */
	public void run()
	{
		// Create our initial batch of creatures
		List<Creature> creatures = new ArrayList<Creature>();
		
		// Add some plants to the world
		for(int i = 0; i < 25; i++) {
			Plant p = new Plant();
			creatures.add(p);
		}
		
		// Add some animals to the world
		for(int i = 0; i < 25; i++) {
			Animal a = new Animal();
			creatures.add(a);
		}
		
		// Create the world
		_theWorld = new World(creatures);
	}
	
	/**
	* main() simply creates an instance of the Game class and calls its run() function.
    * @param args Unused.
    */
	public static void main(String[] args) {
		// It all starts here.
		new Game().run();
	}
}