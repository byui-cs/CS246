import java.awt.Color;

/**
*  Plants sit there and look green, waiting to be eaten. They are represented by green circles.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* @see Creature
*/
public class Plant extends Creature {
	
	/**
	* Creates a plant with 1 health point.
	*/
	public Plant() {
		_health = 1;
	}
	
	// No javadocs are necessary for these methods because they will inherit the 
	// documentation from the superclass. We only need to add docs here if we are
	// doing something non-obvious in our overridden version.

	public Shape getShape() {
		return Shape.Circle;
	}
	
	public Color getColor() {
		return new Color(0, 132, 72);
	}	
	
	public Boolean isAlive() {
		return _health > 0;
	}
}