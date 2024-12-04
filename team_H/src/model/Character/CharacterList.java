package model.Character;

import java.util.HashMap;

public class CharacterList {

	private HashMap<String, Character> characterList;
	
	public CharacterList() {
		
		this.characterList = new HashMap<>();
		addCharacters();
	}
	
	
	
	private void addCharacters() {
		
		characterList.put("ActionMan", new ActionMan());
		//characterList.put("Zed", new Zed());
		//characterList.put("Garen", new Garen());
		// ...
	}
}
