import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
* Handles drawing and the passage of time in the world.
* <p>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* @see CreatureHandler
*/
public class World extends Canvas implements Runnable  {
	
	Dimension _world;
	Dimension _cell;
	int _totalStartingCreatures;
	
	CreatureHandler _handler;
	
	/**
	* Initializes the world, the drawing window, and starts the update thread in the background.
    * @param creatures The {@link Creature} instances that exist at the beginning of the game.
    */
	public World(List<Creature> creatures) {

		_totalStartingCreatures = creatures.size();
		
		// You can adjust the overall dimensions of the world by modifying this line
		_world = new Dimension(400, 400);

		// The size of each cell depends on how many creatures we have and the
		// size of the world.
		_cell = new Dimension(_world.width / _totalStartingCreatures, 
		                      _world.height / _totalStartingCreatures);
		
		// The CreatureHandler class keeps track of where the creatures are,
		// but it needs to know the size of the world.
		int rows = _world.height / _cell.height;
		int cols = _world.width / _cell.width;
		_handler = new CreatureHandler(rows, cols, creatures);
		_handler.setStartingPositions();

		// This is all AWT stuff for setting up the window
		JFrame win = new JFrame("Life with Java");
		win.setSize(_world);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.getContentPane().setBackground(new Color(233, 232, 231));
		win.add( this );
		
		// This strange little construction is called an anonymous inner class.
		// Here, we're using it to handle window resizing because that's how AWT
		// makes us do it.
		// You can read more about anonymous classes here: 
		// https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
		win.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				_world = new Dimension(getWidth(), getHeight());
				_cell = new Dimension(_world.width / _totalStartingCreatures, 
				                      _world.height / _totalStartingCreatures);
				repaint();
			}
		});

		win.setVisible(true);
		
		// Since this class implements the Runnable interface, we can 
		// have the simulation updating happen on a background thread.
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	* This function is called on the background thread by virtue of the {@link java.lang.Runnable}
	* interface.
    * <p>
    * Every time it's called, we update all of the creatures, redraw the window, and take a brief .25
    * sleep so the game doesn't run blindingly fast.
    */
	public void run() {
		
		// This infinite loop will run as long as the simulation is
		// active.
		while(true)
		{
			// Give all of the creatures a chance to respond to their
			// enviroment.
			_handler.updateCreatures();

			// Then redraw everything in its new position
			repaint();
			
			// Pause the background thread for 0.25 seconds so we can watch
			// the simulation unfold. Note how we have to wrap the sleep()
			// call in a try/catch block.
			try {
				Thread.sleep(250);
			} catch (Exception e) {
				
			}
			
		}
	}

	// This function will inherit its docs from the Canvas class, so we'll just
	// add some inline developer notes.
	public void paint( Graphics context ) {

		// Java's enhanced for loop (AKA for-each loop).
		// See: http://stackoverflow.com/a/11685345/28106
		for(Creature c : _handler.getCreatures()) {
			
			// Don't draw things that are dead
			if(!c.isAlive())
				continue;
			
			Point p = c.getLocation();
			int x = p.x * _cell.width;
			int y = p.y * _cell.height;
			
			context.setColor(c.getColor());
			
			// You can add additional creature visualizations by adding 
			// additional values to the Shape enum (in Shape.java)
			// and handling them here. 
			// For details on what kind of drawing you can do, see:
			// https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
			switch (c.getShape()) {
				case Circle:
					context.fillOval(x, y, _cell.width, _cell.height);
					break;
				case Square:
					context.fillRect(x, y, _cell.width, _cell.height);
					break;
				default:
					break;
			}
		}
		
		// Draw the grid lines
		context.setColor(new Color(187, 187, 185));
		int rows = _world.height / _cell.height;
		int cols = _world.width / _cell.width;

		for(int r = 0; r < rows; r++) {
			context.drawLine(0, r * _cell.height, _world.width, r * _cell.height);
		}
		
		for(int c = 0; c < cols; c++) {
			context.drawLine(c * _cell.width, 0, c * _cell.width, _world.height);
		}
		
	}
}