package baerlion;

import java.util.List;
import java.util.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.InputAdapter;

import baerlion.ui.Button;

public class BaerlionGame extends InputAdapter implements Game {
	final BaerlionClient client;
	final BaerlionView view;
	final List<Button> buttons;
	final List<Image> uiImages;
	UnicodeFont hudFont = null;
	UnicodeFont consoleFont = null;

	public BaerlionGame() {
		client = new BaerlionClient();
		view = new BaerlionView();
		buttons = new Vector<Button>();
		uiImages = new Vector<Image>();
	}
 
	public void init(GameContainer gc) throws SlickException {
		client.init();
		hudFont = getFont("GoudyBookletter1911.otf", "goudy.hiero");
		consoleFont = getFont("mplus-1p-regular.ttf", "mplus.hiero");
		createButton("control_play_blue.png", 200, 10, 30, 30);
	}
 
	public void update(GameContainer gc, int delta) throws SlickException {
		String response = client.run();
		if (response != null)
			view.parse(response);
	}

	public UnicodeFont getFont(String fontFile, String hieroFile) throws SlickException {
		UnicodeFont font = new UnicodeFont(fontFile, hieroFile);
		font.addAsciiGlyphs();
		font.loadGlyphs();
		return font;
	}

	public void createButton(String filename, int x, int y, int w, int h) throws SlickException {
		buttons.add(new Button("play", x, y, w, h));
		uiImages.add(new Image(filename));
	}

	public void fillRect(Graphics g, Color color, int x, int y, int w, int h) {
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, w, h);
		g.setColor(oldColor);
	}
 
	public void render(GameContainer gc, Graphics g) throws SlickException {
		fillRect(g, Color.green, 0, 0, gc.getWidth(), gc.getHeight());
		for (ImageWrapper i : view.renderList)
			i.draw();
		int count = 0;
		for (Button b : buttons)
			uiImages.get(count++).draw(b.x, b.y, b.w, b.h);
		String timeString = "Day " + view.day + ", Step " + view.step;
		hudFont.drawString(400, 10, timeString, Color.red);
		consoleFont.drawString(100, 400, "testString", Color.red);
	}

	public String getTitle() {
		return "SlickTest";
	}

	public boolean closeRequested() {
		hudFont.destroy();
		consoleFont.destroy();
		client.close();
		return true;
	}

	protected void command(String command) {
		if (command.equals("play"))
			client.command("step");
	}
 
	@Override public void mousePressed(int button, int x, int y) {
		for (Button b : buttons)
			if (b.click(x, y))
				command(b.name);
	}

	public static void main(String[] args) throws SlickException {
		new AppGameContainer(new BaerlionGame()).start();
	}
}
