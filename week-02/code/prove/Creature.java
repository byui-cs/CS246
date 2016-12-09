import java.awt.Color;
import java.awt.Point;

// Since this is an abstract class, it cannot be created using new.
// Instead, we have to create subclasses of this class.

/**
*  Creature is the base class for all creatures in the game. However, unless they implement
*  one of the defined interfaces, they just sit there and do nothing.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
*/
public abstract class Creature {
	
	Point _location;
	int _health;
	
	/**
	* Gets the location of the creature in game coordinates.
	* @return The current location of the creature.
	*/
	public Point getLocation() {
		return _location;
	}
	
	/**
	* Sets the location of the creature in game coordinates.
	* @param newValue The new location of the creature. 
	*/
	public void setLocation(Point newValue) {
		_location = newValue;
	}
	
	/**
	* Allows the creature to take damage if something comes by to attack it.
	* @param damage The amount of damage inflicted by the attacker
	*/
	public void takeDamage(int damage) {
		_health -= damage;
	}
	
	// Since the following methods are marked as abstract, subclasses
	// must implement them.	

	/**
	* Gets the {@link Shape} used to represent this creature.
	* @return The shape drawn for this creature.
	*/
	abstract Shape getShape();

	/**
	* Gets the {@link java.awt.Color} used to represent this creature.
	* @return The color drawn for this creature.
	*/
	abstract Color getColor();

	/**
	* Is the creature alive?
	* @return A boolean indicating if the creature is alive.
	*/
	abstract Boolean isAlive();
}