package sugdk.core;

//==============================================================================
// Date Created:		19 December 2011
// Last Updated:		1 August 2012
//
// File Name:			GameFrame.java
// File Author:			M Matthew Hydock
//                      Nicholas Hydock
//
// File Description:	An abstract class representing a generic game frame. It
//						supports full screen and windowed mode, regulates frame
//						updates, and listens to window events (if in windowed
//						mode). Certain methods are left abstract, for descendant
//						classes to implement.
//==============================================================================

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

import sugdk.engine.GameEngine;

public abstract class GameFrame extends JFrame implements WindowListener, Runnable, KeyListener
{
//==============================================================================
// Constants and regulators.
//==============================================================================
	public static final long NANO_PER_SEC	= 1000000000L;
	public static final int NANO_PER_MSEC	= 1000000;
	public static final int MSEC_PER_SEC	= 1000;
	// Because I keep losing track of zeros in frame rate calculations.
	
	private static final int NUM_DELAYS_PER_YIELD = 16;
	// Number of frames with a delay of 0 ms before the animation thread yields
	// to other running threads.

	private static int DEFAULT_FPS = 30;
	// Default frames per second.
	
	private static final int MAX_FRAME_SKIPS = 5;
	// Number of frames that can be skipped in any one animation loop,
	// i.e the games state is updated but not rendered
	
	private long period;                			// Period between drawing, in nanosecs.
	private long currTime;							// Time at the beginning of the game loop iteration.
	private long timeDiff;							// Time between frames, for FPS calculation.

	private int lastFPS = -1;						// Last average frames per second.
	private int currFPS = 0;						// Current accumulated frames per second.
	private long timeFPS = 0L;						// Accumulated time per update.
	
	private long pauseLength = 0;
//==============================================================================


//==============================================================================
// Game control variables.
//==============================================================================
	private Thread animator;						// The thread that performs the animation.
	protected volatile boolean running = false;		// Used to stop the animation thread.
	protected volatile boolean isPaused = false;	// Used to pause the animation thread.
	protected volatile boolean isSuspended = false;	// Used when the game is minimized, to
													// stop everything, even drawing.
	
	// Used at game termination.
	protected volatile boolean gameOver = false;

	// Information about the graphic environment.
	private GraphicsEnvironment ge;
	private GraphicsDevice gd;

	// Improve support for drawing in windowed mode.
	protected volatile boolean isWindowed;
	protected volatile Canvas canvas;

	// Double buffering support.
	private BufferStrategy bufferStrategy;
	private Graphics buffer;
//==============================================================================
	
	
//==============================================================================
// Initialization methods.
//==============================================================================
	public GameFrame(String name, int fps, boolean windowed)
	{
		super(name);

		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();

//		setLayout(null);

		isWindowed = windowed;

		if (!isWindowed)
		{
			if (!gd.isFullScreenSupported())
			// If the display doesn't support full screen, display a warning,
			// and then start the game in windowed mode.
			{
				System.out.println("Full-screen exclusive mode not supported");
				initWindowed();
			}
			else
			// Otherwise, the user has requested full screen, and it is
			// supported, so start the game in full screen mode.
				initFullScreen();
		}
		else
			initWindowed();
		
		setFPS(fps);

		setBufferStrategy();

		// Make this panel receive key events.
		setFocusable(true);
		requestFocus();
	}
	
	private void initWindowed()
	// Set up the frame for windowed mode.
	{
		canvas = new Canvas();
		add(canvas);
		
		setUndecorated(true);
		addWindowListener(this);
		setIgnoreRepaint(true);					// Turn off all paint events.
		setResizable(false);					// Prevent frame resizing.
		setVisible(true);
	}
	
	private void initFullScreen()
	// Set up the frame to be full screen.
	{
		setUndecorated(true);					// No menu bar, borders, etc.
		setIgnoreRepaint(true);					// Turn off all paint events.
		setResizable(false);					// Prevent frame resizing.
		
		gd.setFullScreenWindow(this);			// Switch on full-screen exclusive mode
	}
	
	private void setBufferStrategy()
	// Attempt to set the BufferStrategy (for double buffering).
	{
		try
		// Try to create a buffer strategy. Wait until it has been made.
		{
			EventQueue.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					if (isWindowed)
						canvas.createBufferStrategy(2);
					else
						createBufferStrategy(2);
				}
			});
		}
		catch (Exception e)
		// Whoops! Something happened and a buffer strategy couldn't be made.
		{
			System.out.println("Error while creating buffer strategy");
			System.exit(0);
		}
		
		try
		// Sleep to give time for the buffer strategy to be carried out.
		{
			Thread.sleep(500); // 0.5 sec
		}
		catch(InterruptedException ex){}

		if (isWindowed)
			bufferStrategy = canvas.getBufferStrategy();
		else
			bufferStrategy = getBufferStrategy();
	}
	
	protected void startGame()
	// Initialize and start the thread. 
	{ 
		if (animator == null || !running)
		{
			animator = new Thread(this);
			animator.start();
		}
	}
//==============================================================================


//==============================================================================
// Game life cycle methods, called by the JFrame's window listener methods.
//==============================================================================
	public void suspendGame()
	// Called when the JFrame is deactivated/iconified. Pauses the game, but now
	// even the buffer isn't updated, as it is likely not visible.
	{
		isSuspended = true;
	}
	
	public void resumeGame()
	// Called when the JFrame is activated/deiconified. Unpauses the game.
	{
		if (!isSuspended)
			isPaused = false;
		else
			isSuspended = false;
	} 

	/**
	 * Tells GameFrame instances to pause engine calculations for awhile
	 * @param i length of time to pause the game in milliseconds
	 *  -1 will pause the game indefinitely
	 *  0 will unpause
	 */
	public void pauseGame(int i) {
		pauseLength = i*NANO_PER_MSEC;
		isPaused = (pauseLength != 0);
	}
	
	/**
	 * Called when the game needs to be paused, but the renderer is still active.
	 */
	public void pauseGame()
	{
		pauseGame(-1);
	} 

	/**
	 * Called when the JFrame is closing, to end the game loop.
	 */
	public void stopGame() 
	{
		running = false;
	}
//==============================================================================


//==============================================================================
// Game loop and game object update/render methods. Specifics are left abstract,
// for descendent classes to implement.
//==============================================================================
	@Override
	public void run()
	// The frames of the animation are drawn inside the while loop.
	{
		// Initialize the timers and counters.
		long beforeTime, sleepTime;
		int noDelays = 0;
		long excess = 0L;

		beforeTime = System.nanoTime();

		running = true;

		while(running)
		// Updating and rendering loop.
		{
			//do not update the game if it is paused
			if (pauseLength == 0)
				gameUpdate();				// Update the game data.
			paintScreen();					// Render/Display the frame.

			currTime	= System.nanoTime();
			timeDiff	= currTime - beforeTime;
			sleepTime	= period - timeDiff;

			if (!isSuspended)
			// Make sure not to update the current FPS if game is suspended.
			{
				timeFPS += timeDiff;
				currFPS++;

				if (timeFPS > NANO_PER_SEC)
				// One second has passed, update the real FPS.
				{
					lastFPS = currFPS;
					currFPS = 0;
					timeFPS = 0L;
				}
			}
			
			if (sleepTime > 0)
			// Some time left in this cycle, sleep for a bit.
			{
				try 
				{
					//System.out.println("period (nano): " + period);
					//System.out.println("timeDiff (nano): " + timeDiff);
					//System.out.println("sleep time (nano): " + sleepTime);
					//System.out.println("sleep time (msec): " + sleepTime/NANO_PER_MSEC);
					
					Thread.sleep(sleepTime/NANO_PER_MSEC);  // nano -> ms
				}
				catch(InterruptedException ex)
				{
					System.out.println("OH SHIT EXCEPTION");
				}
			}
			else
			// The frame took longer than desired to render. 
			{
				excess -= sleepTime;  // store excess time value

				if (++noDelays >= NUM_DELAYS_PER_YIELD)
				{
					Thread.yield();   // give another thread a chance to run
					noDelays = 0;
				}
			}

			//trim down the pause timer
			if (pauseLength > 0)
				pauseLength = Math.max(0,  pauseLength - (System.nanoTime() - beforeTime));
			
			beforeTime = System.nanoTime();

			if (pauseLength == 0)
			{
				// If frame animation is taking too long, update the game state
				// without rendering it, to get the updates/sec nearer to the
				// required FPS.
				int skips = 0;
				while((excess > period) && (skips < MAX_FRAME_SKIPS))
				{
					excess -= period;
					gameUpdate();
					skips++;
				}
			}
		}
		
		// I really don't like this here, as it doesn't feel thread safe, but in
		// the off chance the game is being run in full screen, the JFrame won't
		// have the capability to close itself...
		System.exit(0);
	}
	
	/**
	 * Use active rendering to draw to a back buffer and then place the buffer on-screen.
	 */
	public void paintScreen()
	{
		try
		{
			buffer = bufferStrategy.getDrawGraphics();
			gameRender(buffer);
			buffer.dispose();
			
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents Lost");

			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();

//			System.out.println("frame updated");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			running = false;
		}
	}
	
	/**
	 *  Update the game objects.
	 */
	protected abstract void gameUpdate();
	
	/**
	 *  Draw the game objects to a graphics context.
	 * @param g
	 */
	protected abstract void gameRender(Graphics g);
//==============================================================================


//==============================================================================
// Getters and setters. Some override JFrame methods, some don't.
//==============================================================================
	@Override
	public void setSize(int w, int h)
	// Set the size of the drawing area (canvas if windowed, frame if not).
	{
		if (isWindowed)
		{
			canvas.setPreferredSize(new Dimension(w,h));
			pack();
		}
		else
			super.setSize(w,h);
	}

	public void setWidth(int w)
	// Set the width of the drawing area (canvas if windowed, frame if not).

	{
		if (isWindowed)
		{
			canvas.setPreferredSize(new Dimension(w,canvas.getHeight()));
			pack();
		}
		else
			super.setSize(w,super.getHeight());
	}

	@Override
	public int getWidth()
	// Get the width of the drawing area (canvas if windowed, frame if not).
	{
		if (isWindowed)
			return canvas.getWidth();

		return ImageObserver.WIDTH;
	}

	public void setHeight(int h)
	// Set the height of the drawing area (canvas if windowed, frame if not).
	{
		if (isWindowed)
		{
			canvas.setPreferredSize(new Dimension(canvas.getWidth(),h));
			pack();
		}
		else
		{
			super.setSize(super.getWidth(),h);
		}
	}

	@Override
	public int getHeight()
	// Get the height of the drawing area (canvas if windowed, frame if not).
	{
		if (isWindowed)
			return canvas.getHeight();

		return ImageObserver.HEIGHT;
	}

	public void setFPS(int fps)
	{
		// Time per frame, in nanosecs.
		if (fps > 0)
			period = NANO_PER_SEC/fps;
		else
			period = NANO_PER_SEC/DEFAULT_FPS;
	}

	public int getCurrFPS()
	// Return the current frames per second.
	{
		return lastFPS;
	}

	public long getCurrTime()
	// Get the time at the beginning of the current game loop.
	{
		return currTime;
	}

	public long getTimeDiff()
	// Get the time difference between the last time check and the time before.
	{
		return timeDiff;
	}
//==============================================================================


//==============================================================================
// Window listener methods.
//==============================================================================
	@Override
	public void windowActivated(WindowEvent e)
	// What to do when the window is made active (was previously unfocused, but
	// the user has clicked on it).
	{
		resumeGame();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	// What to do when the window is made inactive (had focus, but the user has
	// changed tasks).
	{
		suspendGame();
	}

	@Override
	public void windowDeiconified(WindowEvent e) 
	// What to do if the window is restored from being minimized.
	{
		resumeGame();
	}

	@Override
	public void windowIconified(WindowEvent e)
	// What to do if the window has been minimized.
	{
		suspendGame();
	}

	@Override
	public void windowClosing(WindowEvent e)
	// What to do when the window has been asked to close.
	{
		stopGame();
		
		System.exit(0);
	}

	// Here to complete the interface.
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
//==============================================================================

	public GameEngine getEngine() {
		// TODO Auto-generated method stub
		return null;
	}

}
