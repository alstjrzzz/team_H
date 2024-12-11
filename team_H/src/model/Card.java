package model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Card {

	private String name;
	private String category;
	private LinkedList<int[]> range;
	private int value;
	private int priority;
	
	public Card(String name, String category, LinkedList<int[]> range, int value, int priority) {
		
		this.name = name;
		this.category = category;
		this.range = range;
		this.value = value;
		this.priority = priority;
	}

	// JSON으로 변환하는 메서드
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("category", category);
        
        JSONArray rangeArray = new JSONArray();
        for (int[] r : range) {
            rangeArray.put(new JSONArray(r));
        }
        json.put("range", rangeArray);
        
        json.put("value", value);
        json.put("priority", priority);
        return json;
    }
    
    
	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public List<int[]> getRange() {
		return range;
	}

	public int getValue() {
		return value;
	}

	public int getPriority() {
		return priority;
	}
	
	
}
