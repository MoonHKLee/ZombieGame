
public class Bag extends EquipmentItem {
	int weight_limit;//�����ѵ�

	public Bag(String name, int find_rate,int weight_limit) {
		super(name, 0, find_rate);
		this.weight_limit=weight_limit;
	}

}
