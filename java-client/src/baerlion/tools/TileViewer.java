package baerlion.tools;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.InputAdapter;
 
public class TileViewer extends InputAdapter implements Game {

	SpriteSheet spriteSheet = null;
	int mouseX, mouseY;
	int sheetX, sheetY;
	int tileWidth, tileHeight;
	int selectedX, selectedY;
	boolean mouseClicked = false;

	public void init(GameContainer gc) throws SlickException {
		tileWidth = 64;
		tileHeight = 64;
		spriteSheet = new SpriteSheet("iso-64x64-outside.png", tileWidth, tileHeight);
	}

	protected Image getSprite() {
		return spriteSheet.getSprite(1, 0);
	}
 
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i=0; i<spriteSheet.getHorizontalCount(); i++)
			for (int j=0; j<spriteSheet.getVerticalCount(); j++) {
				g.drawRect(offsetX(i), offsetY(j), tileWidth, tileHeight);
				spriteSheet.getSprite(i, j).draw(offsetX(i), offsetY(j));
			}
		drawRect(g, Color.red, offsetX(selectedX), offsetY(selectedY), tileWidth, tileHeight);
		String statusLine = "x: "+mouseX+" y: "+mouseY;
		statusLine += " selected: "+selectedX+", "+selectedY;
		g.drawString(statusLine, 300, 0);
	}

	public void update(GameContainer gc, int delta) throws SlickException { }
 
	public String getTitle() { return "Tileset Viewer"; }

	public boolean closeRequested() { return true; }

	public void drawRect(Graphics g, Color c, int x, int y, int w, int h) {
		Color old = g.getColor();
		g.setColor(c);
		g.drawRect(x, y, w, h);
		g.setColor(old);
	}

	public boolean isWithin(int targetX, int targetY, Image image, int offsetX, int offsetY) {
		return (targetX >= offsetX && targetX <= offsetX + image.getWidth()) &&
			(targetY >= offsetY && targetY <= offsetY + image.getHeight());
	}

	public int offsetX(int index) { return sheetX + index * tileWidth; }

	public int offsetY(int index) { return sheetY + index * tileHeight; }

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		this.mouseX = newx;
		this.mouseY = newy;
		if (mouseClicked) {
			this.sheetX += newx - oldx;
			this.sheetY += newy - oldy;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		this.mouseClicked = true;
		if (isWithin(x, y, spriteSheet, sheetX, sheetY))
			for (int i=0; i<spriteSheet.getHorizontalCount(); i++)
				for (int j=0; j<spriteSheet.getVerticalCount(); j++)
					if (isWithin(x, y, spriteSheet.getSprite(i, j), offsetX(i), offsetY(j))) {
						selectedX = i;
						selectedY = j;
						break;
					}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		this.mouseClicked = false;
	}

	public static void main(String[] args) throws SlickException {
		new TileViewerContainer(new TileViewer()).start();
	}
}

class TileViewerContainer extends AppGameContainer {
	public TileViewerContainer(Game game) throws SlickException {
		super(game, 1024, 1024, false);
		setShowFPS(false);
	}
}
