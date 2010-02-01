package baerlion;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
 
public class BaerlionGame implements Game {
	BaerlionClient client = null;
	SpriteSheet spriteSheet = null;
	Image sprite = null;
	Image sprite2 = null;

	public BaerlionGame() {
		client = new BaerlionClient();
	}
 
	public void init(GameContainer gc) throws SlickException {
		client.init();
		spriteSheet = new SpriteSheet("iso-64x64-outside.png", 64, 64);
		sprite = spriteSheet.getSprite(0, 0);
		sprite2 = spriteSheet.getSprite(5, 0);
	}
 
	public void update(GameContainer gc, int delta) throws SlickException {
		client.run();
	}
 
	public void render(GameContainer gc, Graphics g) throws SlickException {
		sprite.draw(0, 0);
		sprite2.draw(32, 16);
		sprite2.draw(64, 0);
		sprite.draw(0, 32);
		sprite2.draw(64, 32);
	}

	public String getTitle() {
		return "SlickTest";
	}

	public boolean closeRequested() {
		client.close();
		return true;
	}
 
	public static void main(String[] args) throws SlickException {
		new AppGameContainer(new BaerlionGame()).start();
	}
}
