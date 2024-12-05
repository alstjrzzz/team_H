package model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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

	// JSON으로 변환하는 메서드
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("effect", effect);
        
        JSONArray rangeArray = new JSONArray();
        for (int[] r : range) {
            rangeArray.put(new JSONArray(r));
        }
        json.put("range", rangeArray);
        
        json.put("damage", damage);
        return json;
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
