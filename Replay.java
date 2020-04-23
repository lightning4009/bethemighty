import java.util.ArrayList;

public class Replay {
	private ArrayList<Integer> playerOneActions;
	private ArrayList<Integer> playerTwoActions;
	private Fighter playerOneCharacter;
	private Fighter playerTwoCharacter;
	
	Replay (ArrayList<Integer> playerOneActions, ArrayList<Integer> playerTwoActions, Fighter playerOneCharacter, Fighter playerTwoCharacter){
		this.playerOneActions = playerOneActions;
		this.playerTwoActions = playerTwoActions;
		this.playerOneCharacter = playerOneCharacter;
		this.playerTwoCharacter = playerTwoCharacter;
		
	}
	
	Replay(){
		
	}

	public ArrayList<Integer> getPlayerOneActions() {
		return playerOneActions;
	}

	public void setPlayerOneActions(ArrayList<Integer> playerOneActions) {
		this.playerOneActions = playerOneActions;
	}

	public ArrayList<Integer> getPlayerTwoActions() {
		return playerTwoActions;
	}

	public void setPlayerTwoActions(ArrayList<Integer> playerTwoActions) {
		this.playerTwoActions = playerTwoActions;
	}

	public Fighter getPlayerOneCharacter() {
		return playerOneCharacter;
	}

	public void setPlayerOneCharacter(Fighter playerOneCharacter) {
		this.playerOneCharacter = playerOneCharacter;
	}

	public Fighter getPlayerTwoCharacter() {
		return playerTwoCharacter;
	}

	public void setPlayerTwoCharacter(Fighter playerTwoCharacter) {
		this.playerTwoCharacter = playerTwoCharacter;
	}
	
	public String toString() {
		String returnString = "";
		for (int i = 0; i < playerOneActions.size(); i++) {
			if (!(i == playerOneActions.size() - 1)) {
				returnString += playerOneActions.get(i) + " ";
			}
			else {
				returnString += playerOneActions.get(i) + "";
			}
		}
		returnString += ",";
		
		for (int i = 0; i < playerTwoActions.size(); i++) {
			if (!(i == playerTwoActions.size() - 1)) {
				returnString += playerTwoActions.get(i) + " ";
			}
			else {
				returnString += playerTwoActions.get(i) + "";
			}
		}
		
		returnString += "," + playerOneCharacter.getName() + "," + playerTwoCharacter.getName();
		
		return returnString;
	}
	
	
}
