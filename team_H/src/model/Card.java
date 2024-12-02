package model;

import java.util.List;

public class Card {

	private String name;
	private String effect;
	private List<int[]> range;
	private int damage;
	
	public Card(String name, String effect, List<int[]> range, int damage) {
		this.name = name;
		this.effect = effect;
		this.range = range;
		this.damage = damage;
	}

	
	public String getName() {
		return name;
	}

	public String getEffect() {
		return effect;
	}

	public List<int[]> getRange() {
		return range;
	}

	public int getDamage() {
		return damage;
	}
	
	
}
