package baerlion;

import java.util.List;
import java.util.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.InputAdapter;

import baerlion.ui.Button;

public class BaerlionGame extends InputAdapter implements Game {
	final BaerlionClient client;
	final BaerlionView view;
	final List<Button> buttons;
	final List<Image> uiImages;

	public BaerlionGame() {
		client = new BaerlionClient();
		view = new BaerlionView();
		buttons = new Vector<Button>();
		uiImages = new Vector<Image>();
	}
 
	public void init(GameContainer gc) throws SlickException {
		client.init();
		createButton("control_play_blue.png", 200, 10, 30, 30);
	}
 
	public void update(GameContainer gc, int delta) throws SlickException {
		String response = client.run();
		if (response != null)
			view.parse(response);
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
	}

	public String getTitle() {
		return "SlickTest";
	}

	public boolean closeRequested() {
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
