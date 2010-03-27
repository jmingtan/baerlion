package baerlion.models;

public class Villager {
	public String name;
	public String location;
	public String action;

	public Villager(String name, String location, String action) {
		this.name = name;
		this.location = location;
		this.action = action;
	}

	@Override
	public String toString() {
		return name + " is at " + location + " and is " + action;
	}

	public boolean equals(Villager v) {
		return name.equals(v.name);
	}
}
