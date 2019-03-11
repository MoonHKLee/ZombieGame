import java.util.Random;

public class Zombie extends Unit  {
	
	static int zombie_hp=0;//날씨에 따라 증감되는 체력
	static int zombie_attack=0;//날씨공격력
	static int zombie_exp=0;//날씨경험치
	static int zombie_appearance_rate=0;//날씨발견확률
	String name;//좀비 이름
	int appearance_rate;//좀비 등장확률
	int hp_;//고정체력
	int exp;//경험치
	Weapon weapon;//해당좀비의 사망시 드랍되는 아이템 등록
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
	
	public void attackZomToChar(Character character) {// 좀비가 캐릭터를 공격
		Random random = new Random();
		int rate = random.nextInt(100);// 확률값 변수
		if (rate < Character.avoid_rate) {// 내가 좀비 공격을 회피함<---
			System.out.println("나는 뛰어난 몸놀림으로 좀비의 공격을 회피했다.");
		} else if (rate >= Character.avoid_rate && rate < Character.avoid_rate+10) {// 좀비가 나에게 치명타를 가함<---
			if (attack_point - Character.guard_point > 1) {
				Character.hp = Character.hp - 2 * (attack_point - Character.guard_point);
				System.out.println(name + "는 나의 급소를 공격하여 " + 2 * (attack_point - Character.guard_point)
						+ "의 치명타 피해를 입혔다!!!!!!!!");
			} else {// 유효공격이 -이거나 0이면 피해 2
				Character.hp -= 2;
				System.out.println(name + "는 나의 급소를 공격하여 2의 치명타 피해를 입혔다!!!!!!!!");
			}
		} else {//평상시 공격 상황<---
			ZombieAttackBgm attack = new ZombieAttackBgm();
			attack.start();
			if (attack_point - Character.guard_point > 1) {
				Character.hp = Character.hp - (attack_point - Character.guard_point);
				System.out
						.println(name + "는 나에게" + (attack_point - Character.guard_point) + "의 피해를 입혔다.");
			} else {// 유효공격이 -이거나 0이면 피해 1
				Character.hp--;
				System.out.println(name + "는 나에게 1의 피해를 입혔다.");
			}
		}
	}
	
	public void returnZombie() {// 좀비 살리고 체력초기화
		hp = hp_;
		dead = false;
	}
	
	public void run() {
		while(true) {
			try {
				sleep(10000);//10초마다 한번씩 몬스터가 등장
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//몬스터 등장 구현
		}
	}
}
