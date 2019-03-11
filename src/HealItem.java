
public class HealItem extends ConsumptionItem {
	int heal_point;

	HealItem(String name, int weight, int find_rate, int heal_point ) {
		super(name, weight, find_rate);
		this.heal_point=heal_point;
	}

}
