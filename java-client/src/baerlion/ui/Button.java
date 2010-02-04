package baerlion.ui;

public class Button {
	public final String name;
	public int x;
	public int y;
	public int w;
	public int h;

	public Button(String name, int x, int y, int w, int h) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public boolean click(int targetX, int targetY) {
		return (targetX >= x && targetX <= x + w) &&
			(targetY >= y && targetY <= y + h);
	}
}
