/**
*  Aggressors react to creatures they encounter.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* @see Creature
*/
public interface Aggressor {

	/**
    * @param target The {@link Creature} we've encounterd.
    */
	public void attack(Creature target);
}