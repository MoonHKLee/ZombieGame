import java.util.Random;

public class Zombie extends Unit  {
	
	static int zombie_hp=0;//������ ���� �����Ǵ� ü��
	static int zombie_attack=0;//�������ݷ�
	static int zombie_exp=0;//��������ġ
	static int zombie_appearance_rate=0;//�����߰�Ȯ��
	String name;//���� �̸�
	int appearance_rate;//���� ����Ȯ��
	int hp_;//����ü��
	int exp;//����ġ
	Weapon weapon;//�ش������� ����� ����Ǵ� ������ ���
	Armor armor;
	Item ticket;
	
	public Zombie(int hp, int attack_point, int guard_point, String name, int appearance_rate, int hp_, int exp, Weapon weapon, Armor armor, Item ticket) {
		super(hp+Zombie.zombie_hp, attack_point+Zombie.zombie_attack, guard_point);
		this.name=name;
		this.appearance_rate=appearance_rate+Zombie.zombie_appearance_rate;
		this.hp_=hp_+Zombie.zombie_hp;
		this.exp=exp+Zombie.zombie_exp;
		this.weapon = weapon;
		this.armor=armor;
		this.ticket=ticket;
	}
	
	public void attackZomToChar(Character character) {// ���� ĳ���͸� ����
		Random random = new Random();
		int rate = random.nextInt(100);// Ȯ���� ����
		if (rate < Character.avoid_rate) {// ���� ���� ������ ȸ����<---
			System.out.println("���� �پ ������� ������ ������ ȸ���ߴ�.");
		} else if (rate >= Character.avoid_rate && rate < Character.avoid_rate+10) {// ���� ������ ġ��Ÿ�� ����<---
			if (attack_point - Character.guard_point > 1) {
				Character.hp = Character.hp - 2 * (attack_point - Character.guard_point);
				System.out.println(name + "�� ���� �޼Ҹ� �����Ͽ� " + 2 * (attack_point - Character.guard_point)
						+ "�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			} else {// ��ȿ������ -�̰ų� 0�̸� ���� 2
				Character.hp -= 2;
				System.out.println(name + "�� ���� �޼Ҹ� �����Ͽ� 2�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			}
		} else {//���� ���� ��Ȳ<---
			ZombieAttackBgm attack = new ZombieAttackBgm();
			attack.start();
			if (attack_point - Character.guard_point > 1) {
				Character.hp = Character.hp - (attack_point - Character.guard_point);
				System.out
						.println(name + "�� ������" + (attack_point - Character.guard_point) + "�� ���ظ� ������.");
			} else {// ��ȿ������ -�̰ų� 0�̸� ���� 1
				Character.hp--;
				System.out.println(name + "�� ������ 1�� ���ظ� ������.");
			}
		}
	}
	
	public void returnZombie() {// ���� �츮�� ü���ʱ�ȭ
		hp = hp_;
		dead = false;
	}
	
	public void run() {
		while(true) {
			try {
				sleep(10000);//10�ʸ��� �ѹ��� ���Ͱ� ����
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//���� ���� ����
		}
	}
}
