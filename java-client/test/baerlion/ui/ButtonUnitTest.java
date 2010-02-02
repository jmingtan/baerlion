package baerlion.ui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ButtonUnitTest {
	private Button object = null;

	@Before public void before() {
		object = new Button(0, 0, 0, 0);
	}

	@Test public void testClick() {
		int testX1 = 25;
		int testY1 = 48;
		int testX2 = 23;
		int testY2 = 25;
		int x = 20, y = 20;
		int w = 10, h = 10;
		object = new Button(x, y, w, h);

		assertFalse(
				"Button should not have been clicked",
				object.click(testX1, testY1));
		assertTrue(
				"Button should have been clicked",
				object.click(testX2, testY2));
	}
}

