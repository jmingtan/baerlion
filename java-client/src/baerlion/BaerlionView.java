package baerlion;

import java.util.List;
import java.util.Vector;

import baerlion.models.Villager;

public class BaerlionView implements BaerlionParserListener {
	public final List<ImageWrapper> renderList;
	public final List<Villager> villagers;
	public int day;
	public int step;

	public BaerlionView() {
		renderList = new Vector<ImageWrapper>();
		villagers = new Vector<Villager>();
	}

	public void timeParsed(int day, int step) {
		this.day = day;
		this.step = step;
	}

	public void villagerParsed(String name, String location, String action) {
		Villager villager = new Villager(name, location, action);
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
