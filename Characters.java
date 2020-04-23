import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;



public class Characters {
	public ArrayList<Fighter> characters = new ArrayList<>();
	
	
	Characters(){
		Path filePath = Paths.get("resources\\Fighters.csv");
		
		try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.US_ASCII)){	
			
			String line = reader.readLine();
			line = reader.readLine();
			
			while (line != null) {
				String [] fighterData = line.split(",");
				
				if (fighterData.length > 0) {
					Fighter dummy = new Fighter();
					dummy.setId(Integer.parseInt(fighterData[0]));
					dummy.setName(fighterData[1]);
					dummy.setAttack(Integer.parseInt(fighterData[2]));
					dummy.setGrab(Integer.parseInt(fighterData[3]));
					dummy.setHp(Integer.parseInt(fighterData[4]));
					dummy.setSpecialMeterCeiling(Integer.parseInt(fighterData[5]));
					dummy.setSelectable(Boolean.parseBoolean(fighterData[6]));
					
					characters.add(dummy.getId(), dummy);
					
					line = reader.readLine();
				}
			}
			
		}
		catch(IOException ioe) {
			System.out.print("no");
		}
	}
	
	public Fighter findCharacter(int id) {
		return (Fighter) characters.get(id);
	}

	@Override
	public String toString() {
		String returnString = "";
		for (int i = 0; i < characters.size(); i++) {
			if (i != 5) {
				returnString += characters.get(i) + "\n";
			}
		}
		return returnString;
	}
	
	public Fighter getCharacterByName(String name) {
		Fighter returnFighter = null;
		
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).getName().contentEquals(name)) {
				returnFighter = characters.get(i);
			}
		}
		
		return returnFighter;
	}
	
	
}
