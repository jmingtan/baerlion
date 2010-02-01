package test;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
 
public class IsoSample implements Game {
	SpriteSheet spriteSheet = null;

	public void init(GameContainer gc) throws SlickException {
		spriteSheet = new SpriteSheet("iso-64x64-outside.png", 64, 64);
	}

	protected Image getSprite() {
		return spriteSheet.getSprite(1, 0);
	}
 
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int j=0; j<10; j++)
			for (int i=0; i<10; i++) {
				getSprite().draw(i * 64, j * 32);
				getSprite().draw(32 + (i * 64), 16 + (j * 32));
			}
	}

	public void update(GameContainer gc, int delta) throws SlickException { }
 
	public String getTitle() { return "Iso map test"; }

	public boolean closeRequested() { return true; }
 
	public static void main(String[] args) throws SlickException {
		new AppGameContainer(new IsoSample()).start();
	}
}
