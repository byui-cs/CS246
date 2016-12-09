/**
*  Aware creatures can sense others creatures around them.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* @see Creature
*/
public interface Aware {
	
	/**
	* This method is called every frame and indicates what creatures are nearby.
	* If there is no creature in a particular location, that value will be null.
    * @param above The {@link Creature} directly above us.
    * @param below The {@link Creature} directly below us.
    * @param left The {@link Creature} directly to the left of us.
    * @param right The {@link Creature} directly to the right of us.
    */
	public void senseNeighbors(Creature above, Creature below, Creature left, Creature right);
}