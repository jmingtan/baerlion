package baerlion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaerlionViewUnitTest {
	private BaerlionView object = null;

	@Before public void before() {
		object = new BaerlionView();
	}

	@Test public void testGetElements() {
		String testString = "{Villager #9's field} has now been {weeded}.";
		String[] expected = {
			"Villager #9's field", "has now been", "weeded", "."
		};
		assertArrayEquals(
				"Expected elements and actual elements do not match",
				expected, object.getElements(testString).toArray(new String[0]));
	}

	@Test public void testHasVariables() {
		String testString = "{Villager #9's field} has now been {weeded}.";
		String testString2 = "A test string";

		assertTrue(
				"Should return true as input string still has variables",
				object.hasVariables(testString));
		assertFalse(
				"Should return false as input string has no variables",
				object.hasVariables(testString2));
	}

	@Test public void testHasElements() {
		String testString = "{Villager #9's field} has now been {weeded}.";
		String testString2 = "";

		assertTrue(
				"Should return true as input string still has elements",
				object.hasElements(testString));
		assertFalse(
				"Should return false as input string has no elements",
				object.hasElements(testString2));
	}

	@Test public void testGetNextElement() {
		String testString = "{Villager #9's field} has now been {weeded}.";
		String[] expected1 = {"Villager #9's field", " has now been {weeded}."};
		String[] expected2 = {"has now been", "{weeded}."};
		String[] expected3 = {"weeded", "."};
		String[] expected4 = {".", ""};

		String[] result1 = object.getNextElement(testString);
		assertArrayEquals(
				"Expected elements and actual elements do not match",
				expected1, result1);
		String[] result2 = object.getNextElement(result1[1]);
		assertArrayEquals(
				"Expected elements and actual elements do not match",
				expected2, result2);
		String[] result3 = object.getNextElement(result2[1]);
		assertArrayEquals(
				"Expected elements and actual elements do not match",
				expected3, result3);
		String[] result4 = object.getNextElement(result3[1]);
		assertArrayEquals(
				"Expected elements and actual elements do not match",
				expected4, result4);
	}
}
