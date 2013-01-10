package scenes.Main;

import logic.Nick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import core.DataDirs;
import core.Engine;

import sugdk.graphics.SimpleBitmapFont;
import sugdk.scenes.GameDisplay;

/**
 * 
 * @author nhydock
 *
 */
public class DrinkDisplay extends GameDisplay<DrinkSystem> {

	/**
	 * 
	 * @param system
	 */
	public DrinkDisplay(DrinkSystem system)
	{
		super(system);
	}

	SpriteBatch batch;
	OrthographicCamera camera;
	
	/**
	 * The background image
	 */
	private Sprite bg;
	private Sprite inputMessage;
	private Sprite resetMessage;
	private Sprite caughtMessage;
	
	private boolean gameover;
	
	private String score;
	private String time;
	
	private SimpleBitmapFont font;

	@Override
	public void init()
	{
		Texture sprite = new Texture(Gdx.files.internal(DataDirs.ImageDir.path+"sprites.png"));
		bg = new Sprite(new TextureRegion(sprite, 88, 80, 150, 100));
		
		batch = new SpriteBatch();
		/*batch.setTransformMatrix(new Matrix4(new float[]
									{0f,0f,0f,0f,
									 (float)(Display.getWidth()/bg.getWidth()), (float)(Display.getHeight()/bg.getHeight()), 1, 0,
									 0f, 0f, 0f, 0f,
									 0f, 0f, 0f, 0f}));*/
		camera = new OrthographicCamera();
		camera.setToOrtho(false, bg.getWidth(), bg.getHeight());
		font = new SimpleBitmapFont(new TextureRegion(sprite, 88, 58, 72, 6), "0123456789.:", 1);
		
		caughtMessage = new Sprite(sprite, 88, 64, 116, 5);
		resetMessage = new Sprite(sprite, 88, 69, 126, 5);
		inputMessage = new Sprite(sprite, 88, 74, 112, 5);
		
		//center messages
		caughtMessage.setPosition(17, 5);
		resetMessage.setPosition(12, 92);
		inputMessage.setPosition(19, 5);
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		
	}

	@Override
	public void update(float delta) {
		Engine e = Engine.getInstance();
		Nick n = e.getNick();
		gameover = (system.getState() == GameOverState.ID);
		score = String.format("%.2f", n.getScore());
		float t = e.getNick().getTime();
		time = String.format("%02d:%02d:%03d", (int)t/60, (int)t % 60, (int)(t*1000) % 1000);
	}
	
	@Override
	public void render()
	{
		camera.update();
		Engine engine = Engine.getInstance();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		bg.draw(batch);
		engine.getNick().draw(batch);
		engine.getGirard().draw(batch);
		
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

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

}
