//GameState.java

package model;

import java.awt.Dimension;

public class GameState {

	private Dimension dimension = null;
	
	public GameState(Dimension dimension) {
		
		this.dimension = dimension;
	}

	
	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	
}
