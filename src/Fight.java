import java.util.*;

public class Fight {
	Character character;
	Zombie zombie;

	public Fight(Character character, Zombie zombie) {
		this.character = character;
		this.zombie = zombie;
	}

	public void attackCharToZom() {// 캐릭터가 좀비를 공격
		Random random = new Random();
		int rate = random.nextInt(10);// 확률값 변수
		if (rate < 1) {// 좀비가 공격을 회피함<---
			System.out.println("좀비가 공격을 회피했다.");
		} else if (rate >= 1 && rate < 2) {// 좀비에게 치명타를 입힘<---
			if (character.attack_point - zombie.guard_point > 1) {// 유효공격이 1 이상이면
				zombie.hp = zombie.hp - 2 * (character.attack_point - zombie.guard_point);
				System.out.println("나는" + zombie.name + "의 급소를 공격하여 "
						+ 2 * (character.attack_point - zombie.guard_point) + "의 치명타 피해를 입혔다!!!!!!!!");
			} else {// 유효공격이 -이거나 0이면 피해2
				zombie.hp -= 2;
				System.out.println("나는" + zombie.name + "의 급소를 공격하여 2의 치명타 피해를 입혔다!!!!!!!!");
			}
		} else {// 평범한 상황에서의 좀비 공격<---
			if (character.attack_point - zombie.guard_point > 1) {// 유효공격이 1 이상이면
				zombie.hp = zombie.hp - (character.attack_point - zombie.guard_point);
				System.out.println(
						"나는" + zombie.name + "에게" + (character.attack_point - zombie.guard_point) + "의 피해를 입혔다.");
			} else {// 유효공격이 -이거나 0이면 피해1
				zombie.hp--;
				System.out.println("나는" + zombie.name + "에게 1의 피해를 입혔다.");
			}
		}
	}

	public void attackZomToChar() {// 좀비가 캐릭터를 공격
		Random random = new Random();
		int rate = random.nextInt(10);// 확률값 변수
		if (rate < 1) {// 내가 좀비 공격을 회피함<---
			System.out.println("나는 뛰어난 몸놀림으로 좀비의 공격을 회피했다.");
		} else if (rate >= 1 && rate < 2) {// 좀비가 나에게 치명타를 가함<---
			if (zombie.attack_point - character.guard_point > 1) {
				character.hp = character.hp - 2 * (zombie.attack_point - character.guard_point);
				System.out.println(zombie.name + "는 나의 급소를 공격하여 " + 2 * (zombie.attack_point - character.guard_point)
						+ "의 치명타 피해를 입혔다!!!!!!!!");
			} else {// 유효공격이 -이거나 0이면 피해 2
				character.hp -= 2;
				System.out.println(zombie.name + "는 나의 급소를 공격하여 2의 치명타 피해를 입혔다!!!!!!!!");
			}
		} else {//평상시 공격 상황<---
			if (zombie.attack_point - character.guard_point > 1) {
				character.hp = character.hp - (zombie.attack_point - character.guard_point);
				System.out
						.println(zombie.name + "는 나에게" + (zombie.attack_point - character.guard_point) + "의 피해를 입혔다.");
			} else {// 유효공격이 -이거나 0이면 피해 1
				character.hp--;
				System.out.println(zombie.name + "는 나에게 1의 피해를 입혔다.");
			}
		}
	}

	public void showFightDislpay() {
		System.out.println("==============================    전 투 상 황       =================================");
		System.out.println(
				"    나의 상태                                                                                                  "
						+ zombie.name + "의 상태");
		System.out.println("");
		System.out.println("현재HP:" + character.hp + "                                          현재HP:" + zombie.hp);
		System.out.println("공격력:" + character.attack_point + "                                           공격력:"
				+ zombie.attack_point);
		System.out.println("방어력:" + character.guard_point + "                                            방어력:"
				+ zombie.guard_point);
		System.out.println("=============================================================================");
	}

	public int startFight() {
		showFightDislpay();// 먼저 현재 상태창을 보여준다
		do {
			attackCharToZom();
			showFightDislpay();
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "가 죽었다.전투에서 승리했다.");
				System.out.println("경험치를 " + zombie.exp + "획득했다.");
				break;
			}
			attackZomToChar();
			showFightDislpay();
			character.checkDead();
			if (character.dead == true)
				break;
		} while (true);
		returnZombie();// 좀비 초기화
		return zombie.exp;
	}

	public void returnZombie() {// 좀비 살리고 체력초기화
		zombie.hp = zombie.hp_;
		zombie.dead = false;
	}

}
