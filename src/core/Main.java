package core;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import sugdk.core.GameFrame;
import sugdk.engine.GameEngine;
import sugdk.graphics.RenderManager;

public class Main extends GameFrame {

	private static Main instance = null;
	
	public static Main getInstance()
	{
		if (instance == null)
			instance = new Main();
		return instance;
	}
	
	public Main()
	{
		super("DRINK", 25, true);
		engine = Engine.getInstance();
		display = RenderManager.getInstance();
		display.setParent(this);
		
		engine.getSceneManager().addSceneManagerListener(display);
		engine.getSceneManager().addScene(scenes.Main.MainScene.class);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		arg0.consume();
		
		engine.getSceneManager().getCurrentScene().keyPressed(key);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	protected void gameUpdate() {
		engine.getSceneManager().getCurrentScene().update();
	}

	@Override
	protected void gameRender(Graphics g) {
		display.paint(g);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main m = Main.getInstance();
		m.startGame();
	}

}
