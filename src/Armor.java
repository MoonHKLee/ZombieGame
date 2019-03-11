
public class Armor extends EquipmentItem {
	int guard_point;
	Skill skill;

	public Armor(String name, int weight, int find_rate,int guard_point, Skill skill) {
		super(name, weight, find_rate);
		this.guard_point=guard_point;
		this.skill=skill;
	}

}
