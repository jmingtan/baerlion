package baerlion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import baerlion.models.Villager;

public class BaerlionViewUnitTest {
	private BaerlionView object = null;

	@Before public void before() {
		object = new BaerlionView();
	}

	@Test public void testParseDay() {
		int expectedDay = 1;
		int expectedStep = 4;
		String testString = "Day {"+expectedDay+"}, Step {"+expectedStep+"}";

		object.parseDay(testString);

		assertEquals(
				"Day value not set correctly",
				expectedDay, object.day);
		assertEquals(
				"Step value not set correctly",
				expectedStep, object.step);
	}

	@Test public void testParseVillager() {
		String name = "Villager #2";
		String location = name + "'s field";
		String action = "sowing";
		String testString = "{"+name+"} is at {"+location+"} and is {"+action+"}.";
		Villager expected = new Villager(name, location, action);

		object.parseVillager(testString);

		assertEquals(
				"Name does not match",
				name, object.villagers.get(0).name);
		assertEquals(
				"Location does not match",
				location, object.villagers.get(0).location);
		assertEquals(
				"Action does not match",
				action, object.villagers.get(0).action);
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
