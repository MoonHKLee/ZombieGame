
public class Weapon extends EquipmentItem {
	int attack_point;
	Skill skill;

	public Weapon(String name, int weight, int find_rate, int attack_point,Skill skill) {
		super(name, weight, find_rate);
		this.attack_point=attack_point;
		this.skill=skill;
	}

}
