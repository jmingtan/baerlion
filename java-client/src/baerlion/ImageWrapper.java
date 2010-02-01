package baerlion;

import org.newdawn.slick.Image;

public class ImageWrapper {
	public final Image image;
	public int x;
	public int y;

	public ImageWrapper(Image image) {
		this(image, 0, 0);

	}

	public ImageWrapper(Image image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	public void draw() {
		image.draw(x, y);
	}
}
