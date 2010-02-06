package baerlion;

import java.util.List;
import java.util.Vector;

public class BaerlionParser {
	public final List<BaerlionParserListener> listeners;

	public BaerlionParser() {
		listeners = new Vector<BaerlionParserListener>();
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
		for (BaerlionParserListener listener : listeners)
			listener.timeParsed(Integer.parseInt(elements[1]), Integer.parseInt(elements[3]));
	}

	protected void parseVillager(String villagerString) {
		String[] elements = getElements(villagerString).toArray(new String[0]);
		for (BaerlionParserListener listener : listeners)
			listener.villagerParsed(elements[0], elements[2], elements[4]);
	}

}
