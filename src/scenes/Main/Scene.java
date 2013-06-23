package scenes.Main;

import util.SimpleBitmapFont;
import logic.Girard;
import logic.Nick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Matrix4;

import core.Engine;

/**
 * Main game scene for drink.  Everything involving gameplay will happen here
 * @author nhydock
 *
 */
public class Scene implements Screen, InputProcessor {

	boolean ready;  //assets have all been loaded and the scene has been created
	
	Music bgm;
	
	Nick nick;
	Girard girard;
	
	/**
	 * The background image
	 */
	private Sprite background;
	private Sprite inputMessage;
	private Sprite resetMessage;
	private Sprite caughtMessage;
	
	SpriteBatch batch;
	
	Matrix4 viewport;

	private boolean gameover;
	
	private String score;
	private String time;
	
	private SimpleBitmapFont font;
	
	@Override
	public void dispose() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		// pause music
		bgm.pause();
	}

	@Override
	public void render(float delta) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//load assets
		if (!Engine.assets.update())
		{
			//draw loading scene
			return;
		}
		
		if (!ready)
		{
			create();
			ready = true;
		}
		
		if (!this.gameover)
		{
			nick.update(delta);
			girard.update(delta);
			if (nick.getRate() > 0 && girard.isAware())
			{
				this.gameover = true;
				girard.surprise();
			}
		}
		
		//draw scene
		batch.setProjectionMatrix(viewport);
		
		batch.begin();
		background.draw(batch);
		nick.draw(batch);
		girard.draw(batch);
		
		score = String.format("%.2f", nick.getScore());
		float t = nick.getTime();
		time = String.format("%02d:%02d:%03d", (int)t/60, (int)t % 60, (int)(t*1000) % 1000);
	
		//draw the shadows
		font.draw(batch, time, 2, 75, HAlignment.LEFT);
		font.draw(batch, score, 149, 75, HAlignment.RIGHT);
		
		//font.setAlignment(SFont.CENTER);
		if (!gameover)
		{
			inputMessage.draw(batch);
		}
		else
		{
			resetMessage.draw(batch);
			caughtMessage.draw(batch);
		}
		batch.end();
	}
	
	private void load()
	{
		Engine.assets.load("data/sprites.png", Texture.class);
		Engine.assets.load("data/bgm.mp3", Music.class);
	}

	private void create() {
		
		Texture sprite = Engine.assets.get("data/sprites.png", Texture.class);
		background = new Sprite(sprite, 88, 80, 150, 100);
		
		caughtMessage = new Sprite(sprite, 88, 64, 116, 5);
		resetMessage = new Sprite(sprite, 88, 69, 126, 5);
		inputMessage = new Sprite(sprite, 88, 74, 112, 6);
		//center messages
		caughtMessage.setPosition(17, 5);
		resetMessage.setPosition(12, 92);
		inputMessage.setPosition(19, 5);
		
		nick = new Nick();
		girard = new Girard();
		nick.setup();
		girard.setup();
		viewport = new Matrix4();
		viewport.setToOrtho2D(0, 0, background.getWidth(), background.getHeight());
		
		bgm = Engine.assets.get("data/bgm.mp3", Music.class);
		bgm.play();
		
		font = new SimpleBitmapFont(new TextureRegion(sprite, 88, 58, 72, 6), "0123456789.:", 1);
		
		batch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void resize(int width, int height) {
		// DO NOTHING
	}

	@Override
	public void resume() {
		// unpause music
		bgm.play();
	}

	@Override
	public void show() {
		load();
	}

	@Override
	public boolean keyDown(int arg0) {return false;}

	@Override
	public boolean keyTyped(char arg0) {return false;}

	@Override
	public boolean keyUp(int keycode) {
		if (!this.gameover)
		{
			if (keycode == Input.Keys.SPACE)
			{
				nick.increaseRate();
			}
		}
		else
		{
			if (keycode == Input.Keys.ENTER)
			{
				nick.setup();
				girard.setup();
				this.gameover = false;
			}
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {return false;}

	@Override
	public boolean scrolled(int arg0) {return false;}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {return false;}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {return false;}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {return false;}
}
