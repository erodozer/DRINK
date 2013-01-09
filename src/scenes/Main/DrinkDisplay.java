package scenes.Main;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.lwjgl.opengl.Display;

import logic.Nick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;

import core.DataDirs;
import core.Engine;
import core.Input;

import sugdk.scenes.GameDisplay;

/**
 * 
 * @author nhydock
 *
 */
public class DrinkDisplay extends GameDisplay<DrinkSystem> {

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
	
	private boolean gameover;
	
	private String score;
	private String time;
	
	private BitmapFont font;

	@Override
	public void init()
	{
		bg = new Sprite(new Texture(Gdx.files.internal(DataDirs.ImageDir.path+"bg.png")));
		
		batch = new SpriteBatch();
		/*batch.setTransformMatrix(new Matrix4(new float[]
									{0f,0f,0f,0f,
									 (float)(Display.getWidth()/bg.getWidth()), (float)(Display.getHeight()/bg.getHeight()), 1, 0,
									 0f, 0f, 0f, 0f,
									 0f, 0f, 0f, 0f}));*/
		camera = new OrthographicCamera();
		camera.setToOrtho(false, bg.getWidth(), bg.getHeight());
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(DataDirs.FontDir.path + "default.ttf"));
		font = gen.generateFont(35);
		font.setScale(.20f);
		font.setColor(1f, 1f, 1f, 1f);
		font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
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
		font.setColor(0f, 0f, 0f, .25f);
		//font.setAlignment(SFont.LEFT);
		font.drawMultiLine(batch, "TIME: \n"+time, 2, 88, 0, HAlignment.LEFT);
		//font.setAlignment(SFont.RIGHT);
		font.drawMultiLine(batch, "FL Dranked\n"+score, 148, 88, 0,  HAlignment.RIGHT);
		
		//now draw properly
		font.setColor(1f, 1f, 1f, 1f);
		//font.setAlignment(SFont.LEFT);
		font.drawMultiLine(batch, "TIME: \n"+time, 2, 89, 0, HAlignment.LEFT);
		//font.setAlignment(SFont.RIGHT);
		font.drawMultiLine(batch, "FL Dranked\n"+score, 148, 89, 0,  HAlignment.RIGHT);
		
		//font.setAlignment(SFont.CENTER);
		font.drawMultiLine(batch, (gameover)?"YOU HAVE BEEN CAUGHT!":"TAP " + Input.DRINK.key + " TO CHUG OJ", 75, 10, 0, BitmapFont.HAlignment.CENTER);
		font.setColor(0f, 0f, 0f, 1f);
		font.drawMultiLine(batch, (gameover)?"PRESS " + Input.RESET.key + " TO RESET GAME":"", 75, 97, 0, BitmapFont.HAlignment.CENTER);	
	
		batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

}
