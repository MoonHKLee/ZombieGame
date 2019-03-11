import java.util.*;

public class Fight {
	Character character;
	Zombie zombie;

	public Fight(Character character, Zombie zombie) {
		this.character = character;
		this.zombie = zombie;
	}

	public void attackCharToZom() {// ĳ���Ͱ� ���� ����
		Random random = new Random();
		int rate = random.nextInt(10);// Ȯ���� ����
		if (rate < 1) {// ���� ������ ȸ����<---
			System.out.println("���� ������ ȸ���ߴ�.");
		} else if (rate >= 1 && rate < 2) {// ���񿡰� ġ��Ÿ�� ����<---
			if (character.attack_point - zombie.guard_point > 1) {// ��ȿ������ 1 �̻��̸�
				zombie.hp = zombie.hp - 2 * (character.attack_point - zombie.guard_point);
				System.out.println("����" + zombie.name + "�� �޼Ҹ� �����Ͽ� "
						+ 2 * (character.attack_point - zombie.guard_point) + "�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			} else {// ��ȿ������ -�̰ų� 0�̸� ����2
				zombie.hp -= 2;
				System.out.println("����" + zombie.name + "�� �޼Ҹ� �����Ͽ� 2�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			}
		} else {// ����� ��Ȳ������ ���� ����<---
			if (character.attack_point - zombie.guard_point > 1) {// ��ȿ������ 1 �̻��̸�
				zombie.hp = zombie.hp - (character.attack_point - zombie.guard_point);
				System.out.println(
						"����" + zombie.name + "����" + (character.attack_point - zombie.guard_point) + "�� ���ظ� ������.");
			} else {// ��ȿ������ -�̰ų� 0�̸� ����1
				zombie.hp--;
				System.out.println("����" + zombie.name + "���� 1�� ���ظ� ������.");
			}
		}
	}

	public void attackZomToChar() {// ���� ĳ���͸� ����
		Random random = new Random();
		int rate = random.nextInt(10);// Ȯ���� ����
		if (rate < 1) {// ���� ���� ������ ȸ����<---
			System.out.println("���� �پ ������� ������ ������ ȸ���ߴ�.");
		} else if (rate >= 1 && rate < 2) {// ���� ������ ġ��Ÿ�� ����<---
			if (zombie.attack_point - character.guard_point > 1) {
				character.hp = character.hp - 2 * (zombie.attack_point - character.guard_point);
				System.out.println(zombie.name + "�� ���� �޼Ҹ� �����Ͽ� " + 2 * (zombie.attack_point - character.guard_point)
						+ "�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			} else {// ��ȿ������ -�̰ų� 0�̸� ���� 2
				character.hp -= 2;
				System.out.println(zombie.name + "�� ���� �޼Ҹ� �����Ͽ� 2�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			}
		} else {//���� ���� ��Ȳ<---
			if (zombie.attack_point - character.guard_point > 1) {
				character.hp = character.hp - (zombie.attack_point - character.guard_point);
				System.out
						.println(zombie.name + "�� ������" + (zombie.attack_point - character.guard_point) + "�� ���ظ� ������.");
			} else {// ��ȿ������ -�̰ų� 0�̸� ���� 1
				character.hp--;
				System.out.println(zombie.name + "�� ������ 1�� ���ظ� ������.");
			}
		}
	}

	public void showFightDislpay() {
		System.out.println("==============================    �� �� �� Ȳ       =================================");
		System.out.println(
				"    ���� ����                                                                                                  "
						+ zombie.name + "�� ����");
		System.out.println("");
		System.out.println("����HP:" + character.hp + "                                          ����HP:" + zombie.hp);
		System.out.println("���ݷ�:" + character.attack_point + "                                           ���ݷ�:"
				+ zombie.attack_point);
		System.out.println("����:" + character.guard_point + "                                            ����:"
				+ zombie.guard_point);
		System.out.println("=============================================================================");
	}

	public int startFight() {
		showFightDislpay();// ���� ���� ����â�� �����ش�
		do {
			attackCharToZom();
			showFightDislpay();
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "�� �׾���.�������� �¸��ߴ�.");
				System.out.println("����ġ�� " + zombie.exp + "ȹ���ߴ�.");
				break;
			}
			attackZomToChar();
			showFightDislpay();
			character.checkDead();
			if (character.dead == true)
				break;
		} while (true);
		returnZombie();// ���� �ʱ�ȭ
		return zombie.exp;
	}

	public void returnZombie() {// ���� �츮�� ü���ʱ�ȭ
		zombie.hp = zombie.hp_;
		zombie.dead = false;
	}

}
