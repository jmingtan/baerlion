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

	@Test public void testParseVillager() {
		String name = "Villager #2";
		String location = "Location 1";
		String location2 = "Location 2";
		String action = "sowing";
		String action2 = "action2";
		Villager testVillager = new Villager(name, location, action);
		Villager testVillager2 = new Villager(name, location2, action2);
		object.villagerParsed(name, location, action);
		assertEquals(
				"Villager list should have only 1 villager",
				1, object.villagers.size());
		assertTrue(
				"Villager does not match expected value",
				testVillager.equals(object.villagers.get(0)));
		object.villagerParsed(name, location2, action2);
		assertEquals(
				"Villager list should have only 1 villager",
				1, object.villagers.size());
		assertTrue(
				"Villager does not match expected value",
				testVillager2.equals(object.villagers.get(0)));
	}

	@Test public void testTimeParsed() {
		int testDay = 1;
		int testStep = 2;
		object.timeParsed(testDay, testStep);
		assertEquals(
				"Day should be " + testDay,
				testDay, object.day);
		assertEquals(
				"Step should be " + testStep,
				testStep, object.step);
	}
}
