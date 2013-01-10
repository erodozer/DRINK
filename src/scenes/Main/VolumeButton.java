package scenes.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import core.DataDirs;

public class VolumeButton
{
	Sprite on;
	Sprite off; 
	Music music;

	boolean checked;
	
	public VolumeButton()
	{
		Texture sprite = new Texture(Gdx.files.internal(DataDirs.ImageDir.path+"sprites.png"));
		on = new Sprite(new TextureRegion(sprite, 136, 0, 13, 9));
		off = new Sprite(new TextureRegion(sprite, 144, 0, 5, 9));
		on.setX(136);
		off.setX(144);
		on.setY(90);
		off.setY(90);
	}

	
	public void draw(SpriteBatch batch)
	{
		if (checked)
		{
			off.draw(batch);
		}
		else
		{
			on.draw(batch);
		}
	}

	public void setMusic(Music music)
	{
		this.music = music;
	}
	
	public void toggle()
	{
		checked = !checked;
		if (this.music != null)
		{
			if (checked)
			{
				this.music.stop();
			}
			else
			{
				this.music.play();
			}
		}
	}
}
