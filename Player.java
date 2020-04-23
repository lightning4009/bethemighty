import java.util.ArrayList;

//Player class: class that handles all player attributes, such as health and selected character, and player actions, attack, grab, and counter
public class Player {
	
	//ID is player ID which is used to more easily identify the player
	private int ID;
	
	//Health is player health, based on character Hp value
	private int health;
	
	//Keeps track of total damage taken
	private int totalDamageTaken;
	
	//Gives player a Fighter
	private Fighter character;
	
	//Identifies if player is human or artificial intelligence
	private boolean isAI;
	
	//Grants player Characters class
	private Characters characterList;
	
	//Identifies if player is currently atacking
	public boolean isAttacking;
	
	//Identifies if player is currently countering
	public boolean isCountering;
	
	//Identifies if character is currently grabbing
	public boolean isGrabbing;
	
	public boolean isHealthCritical;
	
	//Default constructor for player, sets ID to invalid -1 and sets null fighter for character
	Player () {
		ID = -1;
		character = null;
	}
	
	//Player constructor. Takes int ID, Fighter character and Characters characterList
	Player(int ID, Fighter character, Characters characterList){
		
		//Sets ID to parameter ID, character to parameter character, and health to parameter character's Hp value
		this.ID = ID;
		this.character = character;
		this.health = character.getHp();
		
		//This is here to grant the player access to the full character list, necessary for certain specials;
		this.characterList = characterList;
		
		//Checks if ID is 0, if it is, player is identified as an artificial intelligence
		if (ID == 0) {
			isAI = true;
		}
		
		//Defaults to player not being in an attacking, countering, grabbing, or attack reduction state
		this.isAttacking = false;
		this.isCountering = false;
		this.isGrabbing = false;
		isHealthCritical = false;
		
		//Sets player's total damage taken to 0
		totalDamageTaken = 0;
	}
	
	//Allows player to "attack", player is put into an attacking state and taken out of counter state and grab state
	public void attack() {
		isAttacking = true;
		isCountering = false;
		isGrabbing = false;
	}
	
	//Allows player to "grab", player is put into a grab state and taken out of attack state and counter state
	public void grab() {
		isGrabbing = true;
		isAttacking = false;
		isCountering = false;
		
	}
	
	
	//Allows player to "counter", player is put into counter state and taken out of attack state and grab state
	public void counter() {
		isCountering = true;
		isAttacking = false;
		isGrabbing = false;
	}
	
	//Allows player to lose health, health reduction is equal to damage integer parameter, increases total damage taken
	public void loseHealth(int damage) {
		health -= damage;
		totalDamageTaken += damage;
	}
	
	//Resets player health, sets player health to character's hp stat, subtracts total damage taken from health, necessary for some specials
	public void healthRefresh() {
		health = character.getHp();
		health -= totalDamageTaken;
	}
	
	//Returns player's current remaining health
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth() {
		this.health = character.getHp();
	}
	
	//Allows player to use character specific special attacks. Effect varies based on character ID
	public int special() {
		switch(this.character.getId()) {
		case 0: return ursaSpecial();
		case 1: return gataSpecial(characterList);
		case 2: return gataSpecial(characterList);
		case 3: return sonarSpecial();
		case 4: return slushSpecial(characterList);
		case 6: return billySpecial();
		case 7: return ottoSpecial();
		default: return 0;
		}
	}
	
	//Major Ursa's special move, attack that deals 30 damage, returns 30
	private int ursaSpecial() {
		return 30;
	}
	
	//Sonic Sonar's special move, attack that deals 25 damage, returns 25
	private int sonarSpecial() {
		return 25;
	}
	
	//Billy the Kid's special move, attack that deals 20 damage, returns 20
	private int billySpecial() {
		return 20;
	}
	
	//Los Gatas Grandes special move, switches Calientigressa and Heleona, changes player's loaded character to Heleona (!D: 2) or Calientigressa (ID: 1) if the other is currently the player's character
	private int gataSpecial(Characters characterList) {
		if (character.getId() == 1) {
			character = characterList.findCharacter(2);
			healthRefresh();
		}
		else if (character.getId() == 2) {
			character = characterList.findCharacter(1);
		}
		return 0;
	}
	
	//Slush Puppy's special move, transforms into Hecksicle Hound, stronger form, loads in hecksicle hound as the fighter in place of slush puppy
	private int slushSpecial(Characters characterList) {
		character = characterList.findCharacter(5);
		return 0;
	}
	
	private int ottoSpecial() {
		return 30;
	}
	
	//Returns player's character
	public Fighter getCharacter() {
		return character;
	}
	
	public void setCharacter(Fighter character) {
		this.character = character;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setCharList(Characters charList) {
		characterList = charList;
	}
	
	public int getID() {
		return this.ID;
	}

	

	
	//Artificial intelligence function, takes an integer arrayList of all player choices as parameters
	public void AI(ArrayList<Integer> playerChoices) {
		
		//Initializes number of oponent's attacks, grabs, and counters
		int attacks = 0;
		int grabs = 0;
		int counters = 0;
		
		//Loops through playerChoices
		for (int i = 0; i < playerChoices.size(); i++) {
			
			//Increases attacks, grabs, or counters based on playerChoices
			switch(playerChoices.get(i)) {
			case 0:
				attacks++;
				break;
			case 1: 
				grabs++;
				break;
			case 2:
				counters++;
				break;
			}
		}
		
		//Initalizes randomBias, variable that determines how the AI is biased, and attackGrabcounter, which determines what the AI does
		int randomBias = 0;
		int attackGrabcounter = 0;
		
		//If the opponent has historically attacked more than anything else...
		//Makes the AI more likely to counter and less likely to grab
		if (attacks > grabs && attacks > counters) {
			
			//If player health is greater than one third of the player's character's Hp value
			if (!isHealthCritical) {
				//attackGrabcounter is set to a random number between 0 and 100
				attackGrabcounter = (int)(Math.random() * 100);
				
				//If attackGrabcounter is betweem 27 and 72 (45% chance)...the AI will counter
				if (attackGrabcounter > 27 && attackGrabcounter < 72) {
					counter();
				}
				
				//Otherwise (55% chance)...
				else {
					
					//If character's attack value is greater than grab value, randomBias is set to -15, makes AI more likely to attack
					if(character.getAttack() > character.getGrab()) {
						randomBias = -15;
					}
					
					//Otherwise, if character's attack value is less than grab value, randomBias is set to 15, makes AI more likely to grab
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 15;
					}
					
					//attackGrabcounter is set to random value between 0 and 99
					attackGrabcounter = (int)(Math.random()*100);
					
					//randomBias is added to attackGrabcounter; if attack > grab, attackGrabcounter will tend more towards lower numbers and will be between -15 and 84; if grab > attack, attackGrabcounter will tend more towards larger numbers and will be between 15 and 114
					attackGrabcounter += randomBias;
					
					//If attackGrabcounter is less than 30, AI attacks; if attack > grab, 45% chance; if grab > attack, 15% chance, AI is biased towards attacking
					if (attackGrabcounter < 30) {
						attack();
					}
					
					//If attackGrabcounter is greater than 70, AI grabs; if attack > grab, 4% chance; if grab > attack, 34% chance, AI is biased towards grabbing
					else if (attackGrabcounter > 80) {
						grab();
					}
					
					//If attackGrabcounter is greater than or equal to 30 and less than or equal to 69, 40% chance
					else {
						counter();
					}
				}
			}
			//If player health is less than or equal to 1/3 of character Hp value
			else {
				
				//Sets random bias to 20
				randomBias = 20;
				
				//Sets attackGrabcounter to random integer between 0 and 99 and adds 20, effectively attackGrabcounter is 20 to 119
				attackGrabcounter = (int)(Math.random() * 100);
				attackGrabcounter += randomBias;
				
				//If attackGrabcounter is more than or equal to 50, counter, 69% chance, counter
				if (attackGrabcounter >= 50) {
					counter();
				}
				
				//Otherwise...
				else {
					
					//If character's attack value is greater than character's grab value, set randomBias to -10
					if (character.getAttack() > character.getGrab()) {
						randomBias = -10;
					}
					
					//If character's grab value is greater than attack value, set randomBias to -10
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 10;
					}
					
					//Set attackGrabcounter to a random integer between 0 and 99
					attackGrabcounter = (int)(Math.random() * 100);
					
					//Adds random bias to attackGrabcounter, if attack > grab, attackGrabcounter will be between -10 and 89; if attack < grab, attackGrabcounter will be between 10 and 109 
					attackGrabcounter += randomBias;
					
					//If attackGrabcounter is less than 30, 40% chance when attack > grab, 20% chance when grab > attack, attack
					if (attackGrabcounter < 30) {
						attack();
					}
					
					//Other wise, if attackGrabcounter is more than 69, 14% chance when attack > grab, 34% chance when grab > attack, grab
					else if (attackGrabcounter > 75) {
						grab();
					}
					
					//Otherwise, 54% chance when attack > grab and 46% chance when grab > attack, counter
					else {
						counter();
					}
				}
			}
		}
		
		//Otherwise, if, grab is greater than attack and counter...
		//Adjusts AI behavior if opponent grabs more than anything else, makes AI attack more and counter less
		else if (grabs > attacks && grabs > counters) {
			//If player health is more than 1/3 of character hp value...
			if (!isHealthCritical) {
				attackGrabcounter = (int)(Math.random() * 100);
				//If attackGrabcounter is between 39 and 60, 21% chance, counter
				if (attackGrabcounter > 39 && attackGrabcounter < 60) {
					counter();
				}
				else {
					if(character.getAttack() > character.getGrab()) {
						randomBias = -15;
					}
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 15;
					}
					
					attackGrabcounter = (int)(Math.random()*100);
					attackGrabcounter += randomBias;
					
					//If attackGrabcounter is less than 70, 85% chance when attack > grab and 55% chance when grab > attack, attack 
					if (attackGrabcounter < 70) {
						attack();
					}
					
					//Otherwise, If attackGrabcounter is greater than 70, 14% chance when attack > grab and 44% chance when grab > attack, grab
					else if (attackGrabcounter > 70) {
						grab();
					}
					
					//Otherwise, 1% chance, counter
					else {
						counter();
					}
				}
			}
			
			//Otherwise...
			else {
				
				//Sets randomBias to 10
				randomBias = 10;
				attackGrabcounter = (int)(Math.random() * 100);
				attackGrabcounter += randomBias;
				
				//If attackGrabcounter is more than or equal to 50, 59% chance, counter
				if (attackGrabcounter >= 50) {
					counter();
				}
				
				//Otherwise
				else {
					if (character.getAttack() > character.getGrab()) {
						randomBias = -10;
					}
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 10;
					}
					
					attackGrabcounter = (int)(Math.random() * 100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter < 70) {
						attack();
					}
					else if (attackGrabcounter > 70) {
						grab();
					}
					else {
						counter();
					}
				}
			}
		}
		
		else if (counters > grabs && counters > attacks) {
			if (!isHealthCritical) {
				attackGrabcounter = (int)(Math.random() * 100);
				if (attackGrabcounter > 39 && attackGrabcounter < 60) {
					counter();
				}
				else {
					if(character.getAttack() > character.getGrab()) {
						randomBias = -15;
					}
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 15;
					}
					
					attackGrabcounter = (int)(Math.random()*100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter < 30) {
						attack();
					}
					
					else if (attackGrabcounter > 30) {
						grab();
					}
					
					else {
						counter();
					}
				}
			}
			else {
				randomBias = 15;
				attackGrabcounter = (int)(Math.random() * 100);
				attackGrabcounter += randomBias;
				
				if (attackGrabcounter >= 50) {
					counter();
				}
				
				else {
					if (character.getAttack() > character.getGrab()) {
						randomBias = -10;
					}
					else if (character.getAttack() < character.getGrab()) {
						randomBias = 10;
					}
					
					attackGrabcounter = (int)(Math.random() * 100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter < 30) {
						attack();
					}
					else if (attackGrabcounter > 30) {
						grab();
					}
					else {
						counter();
					}
				}
			}
		}
		else {
			int lastOpponentOption = playerChoices.get(playerChoices.size() - 1);
			attacks = 0;
			grabs = 0;
			counters = 0;
			
			for (int i = 0; i < playerChoices.size(); i++) {
				int nextPlayerOption = 0;
				if (playerChoices.get(i) == lastOpponentOption) {
					if (i + 1 < playerChoices.size()) {
						 nextPlayerOption = playerChoices.get(i + 1);
						 switch(nextPlayerOption) {
						 case 0:
							 attacks++;
							 break;
						 case 1:
							 grabs++;
							 break;
						 case 2:
							 counters++;
							 break;
						 }
					}
				}
			}
			if (attacks > grabs && attacks > counters) {
				if (!isHealthCritical) {
					attackGrabcounter = (int)(Math.random() * 100);
					if (attackGrabcounter > 27 && attackGrabcounter < 72) {
						counter();
					}
					else {
						if(character.getAttack() > character.getGrab()) {
							randomBias = -15;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 15;
						}
						
						attackGrabcounter = (int)(Math.random()*100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter < 30) {
							attack();
						}
						
						else if (attackGrabcounter > 70) {
							grab();
						}
						
						else {
							counter();
						}
					}
				}
				else {
					randomBias = 20;
					attackGrabcounter = (int)(Math.random() * 100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter >= 50) {
						counter();
					}
					
					else {
						if (character.getAttack() > character.getGrab()) {
							randomBias = -10;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 10;
						}
						
						attackGrabcounter = (int)(Math.random() * 100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter > 30) {
							attack();
						}
						else if (attackGrabcounter < 70) {
							grab();
						}
						else {
							counter();
						}
					}
				}
			}
			
			else if (grabs > attacks && grabs > counters) {
				if (!isHealthCritical) {
					attackGrabcounter = (int)(Math.random() * 100);
					if (attackGrabcounter > 39 && attackGrabcounter < 60) {
						counter();
					}
					else {
						if(character.getAttack() > character.getGrab()) {
							randomBias = -15;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 15;
						}
						
						attackGrabcounter = (int)(Math.random()*100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter < 70) {
							attack();
						}
						
						else if (attackGrabcounter > 70) {
							grab();
						}
						
						else {
							counter();
						}
					}
				}
				else {
					randomBias = 15;
					attackGrabcounter = (int)(Math.random() * 100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter >= 50) {
						counter();
					}
					
					else {
						if (character.getAttack() > character.getGrab()) {
							randomBias = -10;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 10;
						}
						
						attackGrabcounter = (int)(Math.random() * 100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter < 70) {
							attack();
						}
						else if (attackGrabcounter > 70) {
							grab();
						}
						else {
							counter();
						}
					}
				}
			}
			
			else if (counters > grabs && counters > attacks) {
				if (!isHealthCritical) {
					attackGrabcounter = (int)(Math.random() * 100);
					if (attackGrabcounter > 39 && attackGrabcounter < 60) {
						counter();
					}
					else {
						if(character.getAttack() > character.getGrab()) {
							randomBias = -15;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 15;
						}
						
						attackGrabcounter = (int)(Math.random()*100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter < 40) {
							attack();
						}
						
						else if (attackGrabcounter > 40) {
							grab();
						}
						
						else {
							counter();
						}
					}
				}
				else {
					randomBias = 15;
					attackGrabcounter = (int)(Math.random() * 100);
					attackGrabcounter += randomBias;
					
					if (attackGrabcounter >= 50) {
						counter();
					}
					
					else {
						if (character.getAttack() > character.getGrab()) {
							randomBias = -10;
						}
						else if (character.getAttack() < character.getGrab()) {
							randomBias = 10;
						}
						
						attackGrabcounter = (int)(Math.random() * 100);
						attackGrabcounter += randomBias;
						
						if (attackGrabcounter < 40) {
							attack();
						}
						else if (attackGrabcounter > 40) {
							grab();
						}
						else {
							counter();
						}
					}
				}
			}
			
		}
		
	}


	@Override
	public String toString() {
		return "Player " + ID + ", Character: " + character.getName();
	}
	
	
}
