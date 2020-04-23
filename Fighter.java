

public class Fighter {
	
	private String name;
	private int id;
	private int attack;
	private int grab;
	private int hp;
	private boolean selectable;
	private int specialMeterCeiling;
	
	
	Fighter(){
		name = "Dummy";
		id = 0;
		attack = 7;
		grab = 7;
		hp = 70;
		specialMeterCeiling = 50;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAttack() {
		return attack;
	}


	public void setAttack(int attack) {
		this.attack = attack;
	}


	public int getGrab() {
		return grab;
	}


	public void setGrab(int grab) {
		this.grab = grab;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getSpecialMeterCeiling() {
		return specialMeterCeiling;
	}


	public void setSpecialMeterCeiling(int specialMeterCeiling) {
		this.specialMeterCeiling = specialMeterCeiling;
	}
	

	public String toString() {
		return this.getName(); 
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
}