package baerlion;

import java.util.List;
import java.util.Vector;

import baerlion.models.Villager;

public class BaerlionView {
	public final List<ImageWrapper> renderList;
	public final List<Villager> villagers;
	public int day;
	public int step;

	public BaerlionView() {
		renderList = new Vector<ImageWrapper>();
		villagers = new Vector<Villager>();
	}

	protected List<String> getElements(String response) {
		List<String> elems = new Vector<String>();
		while (hasElements(response)) {
			String[] nextElem = getNextElement(response);
			elems.add(nextElem[0]);
			response = nextElem[1];
		}
		return elems;
	}

	protected String[] getNextElement(String testString) {
		String[] result = new String[2];
		if (hasVariables(testString)) {
			if (testString.startsWith("{")) {
				result[0] = testString.substring(1, testString.indexOf("}"));
				result[1] = testString.substring(testString.indexOf("}") + 1);
			}
			else {
				result[0] = testString.substring(0, testString.indexOf("{")).trim();
				result[1] = testString.substring(testString.indexOf("{"));
			}
		}
		else {
			result[0] = testString;
			result[1] = "";
		}
		return result;
	}

	protected boolean hasVariables(String testString) {
		return testString.indexOf("{") >= 0 && testString.indexOf("}") >= 0;
	}

	protected boolean hasElements(String testString) {
		return testString.length() > 0;
	}

	public void parse(String response) {
		System.out.println("Response: " + response);
		if (response.startsWith("Day"))
			parseDay(response);
		else if (response.contains("is at") && response.contains("and is"))
			parseVillager(response);
	}

	protected void parseDay(String dayString) {
		String[] elements = getElements(dayString).toArray(new String[0]);
		this.day = Integer.parseInt(elements[1]);
		this.step = Integer.parseInt(elements[3]);
	}

	protected void parseVillager(String villagerString) {
		String[] elements = getElements(villagerString).toArray(new String[0]);
		addOrReplaceVillager(new Villager(elements[0], elements[2], elements[4]));
	}

	protected void addOrReplaceVillager(Villager villager) {
		boolean found = false;
		for (Villager v : villagers)
			if (v.equals(villager)) {
				villagers.set(villagers.indexOf(v), villager);
				found = true;
				break;
			}
		if (!found) villagers.add(villager);
	}
}
