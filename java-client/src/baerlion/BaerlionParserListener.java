package baerlion;

import baerlion.models.Villager;

public interface BaerlionParserListener {
	public void villagerParsed(String name, String location, String action);
	public void timeParsed(int day, int step);
}
