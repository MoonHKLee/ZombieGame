import java.util.*;
import java.io.*;
import javazoom.jl.player.*;

public class Character extends Unit {
	static int attack_point;
	static int hp;
	static int guard_point;
	static int left_time;// �����ð�
	static int move_point;// �ൿ��(������ 1�ൿ�� �پ��� �ð��� ������)
	static int satiety;// ������(0�Ǹ� ����)
	static int weight_limit;// �����ѵ�(��â�ѵ�)
	static int weight;// ���繫��
	static int hp_;// ����ü��
	static int satiety_;// ���� ������
	static int lv;// ����
	static int exp;// ���� ����ġ
	static int exp_limit;// �ʿ����ġ
	static int attack_point_;// ����â�� ǥ���Ǵ� ĳ���� ��ü ���ݷ�
	static int avoid_rate;//ȸ����
	static int critical_rate;//ġ��Ÿ��
	static int mp_;//�ִ븶��
	static int mp;//���縶��
	static int replace_attack;//��ų�� ���ݷ� �����
	static int replace_guard;//��ų�� ���� �����
	
	static boolean doctor_set_on;
	static boolean police_set_on;
	static boolean baseball_set_on;
	
	static boolean ticket_have;
	
	static Weapon weapon;// ���� �����ϰ��ִ� ����
	static Armor armor;// ���� �����ϰ� �ִ� ��
	static Bag bag;// ���� �����ϰ��ִ� ����
	static Vehicle vehicle;// ���� �����ϰ��ִ� �̵�����
	static HealItem band;// ���� �����ϰ� �ִ� �ش�
	static HealItem medicine;// ������ �׻���
	static HealItem medic_kit;// ������ ŰƮ
	static Food bread;// ������ ��
	static Food dry_meat;// ������ ����
	static Food spam;// ������ ����
	static ArrayList<Skill> skill_list;//��ų���
	static Skill recovery;//��Ʈ������ ȿ��1
	static Skill bind;//��Ʈ������ ȿ��2
	static Skill homerun;//��Ʈ������ȿ��3
	static Skill clutch_hit;//��Ʈ������ȿ��3

	public Character(/* int hp, int attack_point, int guard_point */) {
		super(100, 2, 0);
		attack_point=super.attack_point;
		hp=super.hp;
		guard_point=super.guard_point;
		left_time = 10000;
		move_point = 0;
		satiety = 100;
		weight_limit = 200;
		weight = 0;
		hp_ = hp;
		satiety_ = 100;
		lv = 1;
		exp = 0;
		exp_limit = 100;
		attack_point_ = 2;
		avoid_rate=10;
		critical_rate=10;
		mp=100;
		mp_=mp;
		replace_attack=0;
		replace_guard=0;
		doctor_set_on=false;
		police_set_on=false;
		baseball_set_on=false;
		ticket_have=true;
	}
	
	
	
	public void setSkillList(ArrayList<Skill> skill_list) {//��ų����Ʈ�� ĳ���Ϳ� ���
		Character.skill_list=skill_list;
	}
	
	public void setSkill(Skill recovery, Skill bind, Skill homerun, Skill clutch_hit) {//��ų�� ĳ���Ϳ� ���. ���Ŀ� �߰��� ���� ����
		Character.recovery=recovery;
		Character.bind=bind;
		Character.homerun=homerun;
		Character.clutch_hit=clutch_hit;
	}
	
	public void addSkill(Skill skill) {//��ų��Ͽ� ��ų����
		skill_list.add(skill);
	}
	
	public void clearSkill(Skill skill) {//��ų��Ͽ��� ��ų ����
		skill_list.remove(skill);
	}
	
	public void showSkillList() {//��ų��� �����ֱ�
		System.out.println("==================��ų���===================");
		for(int i=0;i<skill_list.size();i++) {
			System.out.println((i+1)+"."+skill_list.get(i).name+"     �Ҹ�mp:"+skill_list.get(i).mp+"      ��Ÿ��:"+skill_list.get(i).cooltime);
		}
		System.out.println("==========================================");
	}
	public void lvUpCheck() {
		if (exp >= exp_limit)//���� ����ġ�� �ִ� ����ġ�� �����ϸ� ������
			lvUp();
	}

	public void lvUp() {
		attack_point_++;//����â�� ǥ���Ǵ� �⺻���ݷ� ����
		attack_point++;//���ݷ� ����
		hp = 100;// ü��ȸ��
		exp = 0;// ����ġ
		exp_limit += 50;// ����ġ�� ����
		lv++;
		System.out.println("-------������ �ö����ϴ�-------");
		System.out.println("hp�� ��� ȸ���ǰ� ���ݷ��� �����մϴ�.");
	}

	public void showInventory() {//���� �κ��丮 �����ֱ�
		int num_2;
		Scanner scan = new Scanner(System.in);
		System.out.println("���� ���� �Һ�������� Ȯ���մϴ�.");
		do {
			showConsumptionItem();// �Һ������â �����ֱ�
			System.out.println("����� �������� �����ϼ���.");
			System.out.println("1.�ش�     2.�׻���     3.�Ƿ��ŰƮ     4.��     5.����     6.��������     0.�κ��丮 �ݱ�");
			num_2 = scan.nextInt();
			switch (num_2) {

			case 1://�ش���
				if (band.count > 0) {
					consumeBand();
					System.out.println("�ش븦 ����մϴ�. HP��" + band.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�ش븦 ����� �� �����ϴ�.");
				}
				break;

			case 2://�׻��� ���
				if (medicine.count > 0) {
					consumeMedicine();
					System.out.println("�׻����� ����մϴ�. HP�� " + medicine.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�׻����� ����� �� �����ϴ�.");
				}
				break;

			case 3://�Ƿ�� ŰƮ ���
				if (medic_kit.count > 0) {
					consumeMedicKit();
					System.out.println("�Ƿ�� ŰƮ�� ����մϴ�. HP��" + medic_kit.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�Ƿ�� ŰƮ�� ����� �� �����ϴ�.");
				}
				break;

			case 4:
				if (bread.count > 0) {// �� ���
					consumeBread();
					System.out.println("���� ����մϴ�. ��������" + bread.satiety_point + "����մϴ�.");
					System.out.println("���� ������(" + satiety + "/100)");
				} else {
					System.out.println("���� ����� �� �����ϴ�.");
				}
				break;

			case 5:
				if (dry_meat.count > 0) {// �������
					consumeDryMeat();
					System.out.println("������ ����մϴ�. ��������" + dry_meat.satiety_point + "����մϴ�.");
					System.out.println("���� ������(" + satiety + "/100)");
				} else {
					System.out.println("������ ����� �� �����ϴ�.");
				}
				break;

			case 6:
				if (spam.count > 0) {// ���Ի��
					consumeSpam();
					System.out.println("������ ����մϴ�. ��������" + spam.satiety_point + "����մϴ�.");
					System.out.println("���� ������(" + satiety + "/100)");
				} else {
					System.out.println("������ ����� �� �����ϴ�.");
				}
				break;

			case 0:
				System.out.println("�κ��丮�� �ݽ��ϴ�.");
				break;
			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_2 != 0);
	}
	
	public void showFightInventory() {//���������� �κ��丮 �����ֱ�
		Scanner scan = new Scanner(System.in);
		int num_2;
		showFightConsumptionItem();// �Һ������â �����ֱ�
		do {
			System.out.println("����� �������� �����ϼ���.");
			System.out.println("1.�ش�     2.�׻���     3.�Ƿ��ŰƮ");
			num_2 = scan.nextInt();
			switch (num_2) {

			case 1:// �ش���
				if (band.count > 0) {
					consumeBand();
					System.out.println("�ش븦 ����մϴ�. HP��" + band.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�ش븦 ����� �� �����ϴ�.");
				}
				break;

			case 2:// �׻��� ���
				if (medicine.count > 0) {
					consumeMedicine();
					System.out.println("�׻����� ����մϴ�. HP�� " + medicine.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�׻����� ����� �� �����ϴ�.");
				}
				break;

			case 3:// �Ƿ�� ŰƮ ���
				if (medic_kit.count > 0) {
					consumeMedicKit();
					System.out.println("�Ƿ�� ŰƮ�� ����մϴ�. HP��" + medic_kit.heal_point + "����մϴ�.");
					System.out.println("���� ü��(" + hp + "/100)");
				} else {
					System.out.println("�Ƿ�� ŰƮ�� ����� �� �����ϴ�.");
				}
				break;
			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_2 != 1 && num_2 != 2 && num_2 != 3);
	}

	public void showConsumptionItem() {//�Һ������â ��Ȳ ������
		System.out.println("==================ġ�� ������ ��Ȳ===================");
		System.out.println("                   -�ش�-  hp  +" + band.heal_point);
		System.out.println("                   " + band.count + "��");
		System.out.println("");
		System.out.println("                   -�׻���-  hp  +" + medicine.heal_point);
		System.out.println("                   " + medicine.count + "��");
		System.out.println("");
		System.out.println("                   -�Ƿ��ŰƮ-  hp  +" + medic_kit.heal_point);
		System.out.println("                   " + medic_kit.count + "��");
		System.out.println("================================================");
		System.out.println("==================��ǰ ������ ��Ȳ===================");
		System.out.println("                   -��-  ������  +" + bread.satiety_point);
		System.out.println("                   " + bread.count + "��");
		System.out.println("");
		System.out.println("                   -����-  ������  +" + dry_meat.satiety_point);
		System.out.println("                   " + dry_meat.count + "��");
		System.out.println("");
		System.out.println("                   -��������-  ������  +" + spam.satiety_point);
		System.out.println("                   " + spam.count + "��");
		System.out.println("================================================");
		System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
	}
	
	public void showFightConsumptionItem() {//�������� �Һ������â
		System.out.println("==================ġ�� ������ ��Ȳ===================");
		System.out.println("                   -�ش�-  hp  +" + band.heal_point);
		System.out.println("                   " + band.count + "��");
		System.out.println("");
		System.out.println("                   -�׻���-  hp  +" + medicine.heal_point);
		System.out.println("                   " + medicine.count + "��");
		System.out.println("");
		System.out.println("                   -�Ƿ��ŰƮ-  hp  +" + medic_kit.heal_point);
		System.out.println("                   " + medic_kit.count + "��");
		System.out.println("================================================");
	}

	public void showEquipedItem() {
		System.out.println("���� ������ ���������� Ȯ���մϴ�.");
		System.out.println("==================����������=======================");
		System.out.println("                   -����-");
		System.out.println("                                        " + weapon.name + "");
		System.out.println("                                        ���ݷ�:" + weapon.attack_point);
		System.out.println("                                        ����:" + weapon.weight);
		System.out.println("");
		System.out.println("                   -��-");
		System.out.println("                                        " + armor.name + "");
		System.out.println("                                        ����:" + armor.guard_point);
		System.out.println("                                        ����:" + armor.weight);
		System.out.println("");
		System.out.println("                   -����-");
		System.out.println("                                        " + bag.name + "");
		System.out.println("                                        �����ѵ�:" + bag.weight_limit);
		System.out.println("");
		System.out.println("                   -�̵�����-");
		System.out.println("                                        " + vehicle.name + "");
		System.out.println("                                        �ൿ��:" + vehicle.move_point);
		System.out.println("                                        ����:" + vehicle.weight);
		System.out.println("================================================");
	}

	public void fightEndCheck() {//������ Ž�� ����� �߻��ϴ� �޼ҵ�
		// �����ð� ����
		left_time -= 4;
		left_time += move_point;// �ൿ�� ����
		// ������ ����
		satiety -= 3;
		// ���üũ
		checkCharacterDead();
		if (dead == true) {
			System.out.println("����Ͽ����ϴ�.");
			System.exit(0);
		}
		// ������ üũ
		lvUpCheck();
		mpRestore();//����ȸ��
	}
	
	public void setItemCheck() {//��Ʈ������ ȿ���� Ȯ���Ѵ�.
		if(weapon.name=="������޽�"&&armor.name=="�ǻ簡��"&&doctor_set_on==false) {//��Ʈ�� �ϼ��Ǹ�
			doctor_set_on=true;
			skill_list.add(recovery);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("��Ʈ������(�ǻ�)�� ȿ���� ��ų \"ȸ��\"�� ����� �� �ְ� �˴ϴ�!!!!! ");
		}else if(weapon.name=="������޽�"&&armor.name=="�ǻ簡��"&&doctor_set_on==true) {
			//�ƹ��ϵ� ���Ͼ
		}else {
			if(skill_list.contains(recovery)==true) {
				skill_list.remove(recovery);
				System.out.println("��Ʈ������(�ǻ�)�� ȿ���� ������ϴ�.");
				doctor_set_on=false;
			}
		}
		if(weapon.name=="��"&&armor.name=="��������"&&police_set_on==false) {//��Ʈ�ϼ��Ǹ� ��ų���
			police_set_on=true;
			skill_list.add(bind);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("��Ʈ������(����)�� ȿ���� ��ų \"�ӹ�\"�� ����� �� �ְ� �˴ϴ�!!!!! ");
		}else if(weapon.name=="��"&&armor.name=="��������"&&police_set_on==true) {
			//�ƹ��ϵ� ���Ͼ
		}else {
			if(skill_list.contains(bind)==true) {
				skill_list.remove(bind);
				System.out.println("��Ʈ������(����)�� ȿ���� ������ϴ�.");
				police_set_on=false;
			}
		}
		if(weapon.name=="�߱������"&&armor.name=="�߱����"&&baseball_set_on==false) {//��Ʈ�ϼ��Ǹ� ��ų���
			baseball_set_on=true;
			skill_list.add(homerun);
			skill_list.add(clutch_hit);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("��Ʈ������(�߱�����)�� ȿ���� ��ų \"Ȩ��\"�� ����� �� �ְ� �˴ϴ�!!!!! ");
			System.out.println("��Ʈ������(�߱�����)�� ȿ���� ��ų \"����Ÿ\"�� ����� �� �ְ� �˴ϴ�!!!!! ");
		}else if(weapon.name=="�߱������"&&armor.name=="�߱����"&&baseball_set_on==true) {
			//�ƹ��ϵ� ���Ͼ
		}else {
			if(skill_list.contains(homerun)==true) {
				skill_list.remove(homerun);
				skill_list.remove(clutch_hit);
				System.out.println("��Ʈ������(�߱�����)�� ȿ���� ������ϴ�.");
				baseball_set_on=false;
			}
		}
	}
	
	public void mpRestore() {//����ȸ��
		if(mp<mp_)
			mp+=3;
		if(mp>=mp_)
			mp=mp_;
	}
	
	public void doBag(Bag bag) {//���� �߽߰� �߻��ϴ� �޼ҵ�
		int num_3;
		do {
			Scanner scan = new Scanner(System.in);
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// �����Ѵ�.
				if (Character.weight > Character.weight_limit - Character.bag.weight_limit
						+ bag.weight_limit) {
					System.out.println("���� �����ϰ� �ִ� �������� �ʹ� ���� ������ ��ü�� �� �����ϴ�.");
				} else {
					clearBag(Character.bag);// �������� ������ ����
					equipBag(bag);// �߰��� ������ ����
					setItemCheck();
					System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
					break;
				}
			case 2:// ���� ���Ѵ�.
				System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
				break;
			default:
				System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}
	
	public void doWeapon(Weapon weapon) {// ���� �߽߰� �߻��ϴ� �޼ҵ�
		int num_3;
		int num_4;
		int num_5;
		do {
			Scanner scan = new Scanner(System.in);
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// �����Ѵ�.
				if (weight_limit >= weight - Character.weapon.weight + weapon.weight) {// ���� �ѵ� üũ
					clearWeapon(Character.weapon);// �������� ������ ����
					equipWeapon(weapon);// �߰��� ������ ����
					setItemCheck();
					System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
				} else {// ������ �����ѵ� �Ѿ��
					System.out.println("���� �ѵ��� �ʰ��մϴ�. �������� �Һ��Ͽ� �κ��丮�� ���ðڽ��ϱ�?");
					System.out.println("1.��        2.�ƴϿ�");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// �������� ����Ͽ� �κ��丮�� ����.
							showConsumptionItem();// �Һ������â �����ֱ�
							do {
								System.out.println("����� �������� �����ϼ���.");
								System.out.println(
										"1.�ش�     2.�׻���     3.�Ƿ��ŰƮ     4.��     5.����     6.��������     0.�κ��丮 �ݱ�");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1://�ش���
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����մϴ�. HP��" + band.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2://�׻��� ���
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����մϴ�. HP�� " + medicine.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3://�Ƿ�ŰƮ ���
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����մϴ�. HP��" + medic_kit.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// �� ���
										consumeBread();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����մϴ�. ��������" + bread.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// �������
										consumeDryMeat();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + dry_meat.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// ���Ի��
										consumeSpam();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + spam.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("�κ��丮�� �ݽ��ϴ�.");
									break;
								default:
									System.out.println("���ڸ� �ٽ� �Է����ּ���.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + weapon.weight) {// ���� �ѵ� üũ
								clearWeapon(Character.weapon);// �������� ������ ����
								equipWeapon(weapon);// �߰��� ������ ����
								setItemCheck();
								System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
							} else {
								System.out.println("���Ը� �ʰ��մϴ�. �߰��� �������� �����ϴ�.");
							}
							break;
						case 2:// ����� �ʴ´�.
							System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
							break;
						default:
							System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// ���� ���Ѵ�.
				System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
				break;
			default:
				System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void doArmor(Armor armor) {// ����� ȹ��� �������μ���
		Scanner scan = new Scanner(System.in);
		int num_3;
		int num_4;
		int num_5;
		do {
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// �����Ѵ�.
				if (weight_limit >= weight - Character.armor.weight + armor.weight) {// ����Ȯ��
					clearArmor(Character.armor);// �������� ������ ����
					equipArmor(armor);// �߰��� ������ ����
					setItemCheck();
					System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
				} else {// ������ �����ѵ� �Ѿ��
					System.out.println("���� �ѵ��� �ʰ��մϴ�. �������� �Һ��Ͽ� �κ��丮�� ���ðڽ��ϱ�?");
					System.out.println("1.��        2.�ƴϿ�");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// �������� ����Ͽ� �κ��丮�� ����.
							showConsumptionItem();// �Һ������â �����ֱ�
							do {
								System.out.println("����� �������� �����ϼ���.");
								System.out.println(
										"1.�ش�     2.�׻���     3.�Ƿ��ŰƮ     4.��     5.����     6.��������     0.�κ��丮 �ݱ�");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1://�ش���
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����մϴ�. HP��" + band.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2://�׻��� ���
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����մϴ�. HP�� " + medicine.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3://�Ƿ�ŰƮ ���
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����մϴ�. HP��" + medic_kit.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// �� ���
										consumeBread();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����մϴ�. ��������" + bread.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// �������
										consumeDryMeat();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + dry_meat.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// ���Ի��
										consumeSpam();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + spam.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("�κ��丮�� �ݽ��ϴ�.");
									break;
								default:
									System.out.println("���ڸ� �ٽ� �Է����ּ���.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + armor.weight) {// ���� �ѵ��� ���� ���Կ� ȹ������� ������ ���պ��� ũ��
								clearArmor(Character.armor);// �������� ������ ����
								equipArmor(armor);// �߰��� ������ ����
								setItemCheck();
								System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
							} else {
								System.out.println("���Ը� �ʰ��մϴ�. �߰��� �������� �����ϴ�.");
							}
							break;
						case 2:// ����� �ʴ´�.
							System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
							break;
						default:
							System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// ���� ���Ѵ�.
				System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
				break;
			default:
				System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void doVehicle(Vehicle vehicle) {
		Scanner scan = new Scanner(System.in);
		int num_3;
		int num_4;
		int num_5;
		do {
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// �����Ѵ�.
				if (weight_limit >= weight - Character.vehicle.weight + vehicle.weight) {// ���� �ѵ��� ���� ���Կ�
					// ȹ������� ������
					// ���պ��� ũ��
					clearVehicle(Character.vehicle);// �������� ������ ����
					equipVehicle(vehicle);// �߰��� ������ ����
					setItemCheck();
					System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
				} else {// ������ �����ѵ� �Ѿ��
					System.out.println("���� �ѵ��� �ʰ��մϴ�. �������� �Һ��Ͽ� �κ��丮�� ���ðڽ��ϱ�?");
					System.out.println("1.��        2.�ƴϿ�");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// �������� ����Ͽ� �κ��丮�� ����.
							showConsumptionItem();// �Һ������â �����ֱ�
							do {
								System.out.println("����� �������� �����ϼ���.");
								System.out.println(
										"1.�ش�     2.�׻���     3.�Ƿ��ŰƮ     4.��     5.����     6.��������     0.�κ��丮 �ݱ�");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1:
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����մϴ�. HP��" + band.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�ش븦 ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2:
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����մϴ�. HP�� " + medicine.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�׻����� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3:
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����մϴ�. HP��" + medic_kit.heal_point + "����մϴ�.");
										System.out.println("���� ü��(" + hp + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("�Ƿ�� ŰƮ�� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// �� ���
										consumeBread();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����մϴ�. ��������" + bread.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("���� ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// �������
										consumeDryMeat();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + dry_meat.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// ���Ի��
										consumeSpam();
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����մϴ�. ��������" + spam.satiety_point + "����մϴ�.");
										System.out.println("���� ������(" + satiety + "/100)");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// �Һ������â �����ֱ�
										System.out.println("������ ����� �� �����ϴ�.");
										System.out.println("���� ����(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("�κ��丮�� �ݽ��ϴ�.");
									break;
								default:
									System.out.println("���ڸ� �ٽ� �Է����ּ���.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + vehicle.weight) {// ���� ���� üũ
								clearVehicle(Character.vehicle);// �������� ������ ����
								equipVehicle(vehicle);// �߰��� ������ ����
								setItemCheck();
								System.out.println("�߰��� �������� �����Ͽ����ϴ�.");
							} else {
								System.out.println("���Ը� �ʰ��մϴ�. �߰��� �������� �����ϴ�.");
							}
							break;
						case 2:// ����� �ʴ´�.
							System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
							break;
						default:
							System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// ���� ���Ѵ�.
				System.out.println("�߰��� �������� �������� �ʽ��ϴ�.");
				break;
			default:
				System.out.println("��ȣ�� �ٽ� �Է����ּ���.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void equipWeapon(Weapon weapon) {// ���� ����
		Character.weapon = weapon;
		Character.attack_point += weapon.attack_point;
		Character.weight += weapon.weight;
		//�������������� ��ų���
		if(weapon.name=="�׵�") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="�����") {
			critical_rate+=30;
		}
		if(weapon.name=="������޽�") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="��") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="�����") {
			skill_list.add(weapon.skill);
		}
	}

	public void equipArmor(Armor armor) {// �� ����
		Character.armor = armor;
		Character.guard_point += armor.guard_point;
		Character.weight += armor.weight;
		//�������������� ��ų���
		if(armor.name=="��������") {
			skill_list.add(armor.skill);
		}
		if(armor.name=="����������") {
			avoid_rate+=30;
		}
	}

	public void equipVehicle(Vehicle vehicle) {// �̵����� ����
		Character.vehicle = vehicle;
		Character.move_point += vehicle.move_point;
		Character.weight += vehicle.weight;
	}

	public void equipBag(Bag bag) {// ���� ����
		Character.bag = bag;
		Character.weight_limit += bag.weight_limit;
	}

	public void clearWeapon(Weapon weapon) {// ���� ����
		Character.attack_point -= weapon.attack_point;
		Character.weight -= weapon.weight;
		Character.weapon = null;
		//�������������� ��ų����
		if(weapon.name=="�׵�") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="�����") {
			critical_rate-=30;
		}
		if(weapon.name=="������޽�") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="��") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="�����") {
			skill_list.remove(weapon.skill);
		}
	}

	public void clearArmor(Armor armor) {// �� ����
		Character.guard_point -= armor.guard_point;
		Character.weight -= armor.weight;
		Character.armor = null;
		//�������������� ��ų���
		if(armor.name=="��������") {
			skill_list.remove(armor.skill);
		}
		if(armor.name=="����������") {
			avoid_rate-=30;
		}
	}

	public void clearVehicle(Vehicle vehicle) {// �̵����� ����
		Character.move_point -= vehicle.move_point;
		Character.weight -= vehicle.weight;
		Character.vehicle = null;
	}

	public void clearBag(Bag bag) {// ���� ����
		Character.weight_limit -= bag.weight_limit;
		Character.bag = null;
	}

	public void equipBand(HealItem healitem) {// �ش� ĳ���Ϳ� ����
		Character.band = healitem;
		band.count += 5;
		weight += (band.weight * 5);
	}

	public void equipMedicine(HealItem healitem) {// �׻��� ĳ���Ϳ� ����
		Character.medicine = healitem;
	}

	public void equipMedicKit(HealItem healitem) {// ŰƮ ĳ���Ϳ� ����
		Character.medic_kit = healitem;
	}

	public void equipBread(Food food) {// �� ĳ���Ϳ� ����
		Character.bread = food;
		bread.count += 5;
		weight += (bread.weight * 5);
	}

	public void equipDryMeat(Food food) {// ���� ĳ���Ϳ� ����
		Character.dry_meat = food;
	}

	public void equipSpam(Food food) {// ���� ĳ���Ϳ� ����
		Character.spam = food;
	}

	public void consumeBand() {// �ش� ���
		Character.band.count--;
		Character.hp += band.heal_point;
		Character.weight -= band.weight;
		if (hp > hp_)// ü���� ����ü�º��� �������� ����ü������ ü�� ����
			hp = hp_;
	}

	public void consumeMedicine() {// �׻��� ���
		Character.medicine.count--;
		Character.hp += medicine.heal_point;
		Character.weight -= band.weight;
		if (hp > hp_)
			hp = hp_;
	}

	public void consumeMedicKit() {// ŰƮ ���
		Character.medic_kit.count--;
		Character.hp += medic_kit.heal_point;
		Character.weight -= medic_kit.weight;
		if (hp > hp_)
			hp = hp_;
	}

	public void consumeBread() {// �� ���
		Character.bread.count--;
		Character.satiety += bread.satiety_point;
		Character.weight -= bread.weight;
		if (satiety > satiety_)// �������� �������������� �������� �������������� ü�� ����
			satiety = satiety_;
	}

	public void consumeDryMeat() {// ���� ���
		Character.dry_meat.count--;
		Character.satiety += dry_meat.satiety_point;
		Character.weight -= dry_meat.weight;
		if (satiety > satiety_)
			satiety = satiety_;
	}

	public void consumeSpam() {// ���� ���
		Character.spam.count--;
		Character.satiety += spam.satiety_point;
		Character.weight -= spam.weight;
		if (satiety > satiety_)
			satiety = satiety_;
	}

	public void checkCharacterDead() {
		if (left_time <= 0) // �����ð��� 0�Ǹ� ���
			dead = true;
		if (hp <= 0)// ����ü���� 0�Ǹ� ���
			dead = true;
		if (satiety <= 0)// ���� �������� 0�Ǹ� ���
			dead = true;
	}

	public int startFight(Zombie zombie) {//������ �����Ѵ�.
		Scanner scan= new Scanner(System.in);
		Random random = new Random();
		replace_attack = attack_point;//���� ���ݷ� ����
		replace_guard = guard_point;//���� ���� ����
		int replace_police_attack=0;
		int replace_critical_rate=critical_rate;
		int num;
		int num_1 = 0;
		int bindcount=0;//�ӹ�ī��Ʈ
		int police_power_count=0;//���������� ī��Ʈ
		int stun_count=0;//����ī��Ʈ
		int weakness_count=0;//��ȭ ī��Ʈ
		int blood_point=0;
		int critical_count=0;
		boolean escape=false;
		boolean curse=false;
		boolean zombie_blood=false;
		
		showFightDislpay(zombie);// ���� ���� ����â�� �����ش�.
		Fight:
		do {
			do {
				//��ų ��Ÿ�� ����
				if (num_1 != 0) {
					for (int i = 0; i < skill_list.size(); i++) {
						if (skill_list.get(i).cooltime > 0)
							skill_list.get(i).cooltime--;
					}
				}
				if(police_power_count>0&&num_1!=0) {//���� ������ ���ȿ������
					System.out.println("������ ������ "+(police_power_count)+"�� ���ҽ��ϴ�.");
					attack_point+=replace_guard;
					guard_point-=replace_guard;
				}
				if (stun_count > 0) {//���� �������̸�
					System.out.println("���� �Ҵ� �����Դϴ�.���� Ƚ��:" + stun_count);
					stun_count--;
				} else {//������ Ǯ��������
					System.out.println("1.�⺻����      2.��ų���      3.�Һ������ Ȯ��      4.����");
					num_1 = scan.nextInt();
					switch (num_1) {
					case 1:// �⺻����
						if(critical_count>0) {//���ػ�� ����Ǿ�����
							critical_rate=100;
							attackCharToZom(zombie);
							critical_count--;
							System.out.println("���ػ����  "+critical_count+"ȸ ���ҽ��ϴ�.");
						}else {
							critical_rate=replace_critical_rate;
							attackCharToZom(zombie);
						}
						break;
					case 2:// ��ų���
						showSkillList();
						System.out.println("����� ��ų�� ��ȣ�� �Է��ϼ���.");
						num = scan.nextInt();
						try {
							if (skill_list.get(num - 1).name == "�ߵ���") {// ��ų �����ϰ� ���
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									BladeBgm blade = new BladeBgm();
									blade.start();
									System.out.println("�ߵ����� ����մϴ�.");
									zombie.hp -= attack_point * 2;// ���ݷ�*2 ��ŭ�� �������ظ� ������.
									System.out.println(
											"�ߵ����� ����Ͽ� " + zombie.name + "���� " + attack_point * 2 + "�� �������ظ� ������!!!!!");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "ȸ��") {
								int addhp = random.nextInt(11);
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								CureBgm cure = new CureBgm();
								cure.start();
								if (skill_list.get(num - 1).canuse == true && hp < 100) {// ��� �����ϸ�
									System.out.println("ȸ���� ����մϴ�.");
									hp += addhp;// ������ ü���� ȸ���Ѵ�.
									System.out.println("ȸ���� ����Ͽ� " + addhp + "�� ü���� ȸ���ߴ�!!!!!");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
									if (hp > 100) {// ��������
										hp = 100;
									}
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "�ӹ�") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									BindBgm bind = new BindBgm();
									bind.start();
									System.out.println("�ӹ��� ����մϴ�.");
									bindcount += 3;
									System.out.println("������ 3�ϵ��� �ൿ�� ������ ���մϴ�.");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "������ ����") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									PoliceBgm police = new PoliceBgm();
									police.start();
									System.out.println("������ ������ ����մϴ�.");
									System.out.println("5�ϵ��� ������ ���ݷ����� ��ȯ�մϴ�.");
									police_power_count = 6;
									attack_point += replace_guard;
									guard_point -= replace_guard;
									replace_police_attack=attack_point;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "Ȩ��") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									HomerunBgm homerun = new HomerunBgm();
									homerun.start();
									System.out.println("Ȩ���� ����մϴ�.");
									if(random.nextInt(100)<50) {
										zombie.hp=0;
										System.out.println("Ȩ���� �����Ͽ����ϴ�!!!!!!!!!!");
									}else {
										System.out.println("Ȩ���� �����Ͽ����ϴ�........");
									}
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "����Ÿ") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									HomerunBgm homerun = new HomerunBgm();
									homerun.start();
									System.out.println("����Ÿ�� ����մϴ�.");
									System.out.println("������ ü���� �������� �پ��ϴ�.");
									zombie.hp-=zombie.hp/2;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "��ó����") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									MessBgm mess = new MessBgm();
									mess.start();
									System.out.println("��ó���⸦ ����մϴ�.");
									System.out.println("����� ���������� �߰� ���� ���ظ� �Խ��ϴ�.");
									zombie_blood=true;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "���ػ��") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									GunAttackBgm gun = new GunAttackBgm();
									gun.start();
									System.out.println("���ػ���� ����մϴ�.");
									System.out.println("������ 3���� �⺻������ ġ��Ÿ ������ �޽��ϴ�..");
									critical_count=3;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "��������") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("�������⸦ ����մϴ�.");
									zombie.hp-=(attack_point+1);
									System.out.println((attack_point+1)+"�� ���ظ� �����ϴ�.");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							}
						} catch (IndexOutOfBoundsException e) {
							System.out.println("�ùٸ� ��ų�� �ƴմϴ�. ����ȭ������ ���ư��ϴ�.");
							showFightDislpay(zombie);
							num_1 = 0;// �ٽ�
						}
						break;
					case 3:// �Һ������ Ȯ��
						showFightInventory();
						break;
					case 4:// ��������
						System.out.println("�������� ����ģ��.�ð��� 20 �Ҹ��մϴ�.");
						left_time-=20;
						escape=true;//����ģ�� Ȯ��
						break Fight;
					default:
						System.out.println();
					}
					if (Character.armor.name=="����������") {
						avoid_rate=40;
					}else {
						avoid_rate=10;
					}
				}
			} while (num_1 != 1 && num_1 != 2 && num_1 != 3 && num_1 != 4);
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(zombie_blood==true) {
				int blood=random.nextInt(attack_point);
				System.out.println("����� "+blood+"�� ���� �������� �Խ��ϴ�.");
				zombie.hp-=blood;
			}
			if(blood_point>0) {//�������¿� �ִٸ�
				hp-=blood_point;
				System.out.println("�������� ������"+blood_point+"��ŭ�� �������� �Խ��ϴ�.");
				blood_point--;
			}
			showFightDislpay(zombie);
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "�� �׾���.�������� �¸��ߴ�.");
				if(zombie.name=="��������"&&random.nextInt(100)<zombie.weapon.find_rate) {//����� ������ ���
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="����������"&&random.nextInt(100)<zombie.armor.find_rate) {//����� ������ ���
					showFoundArmor(zombie.armor);
					doArmor(zombie.armor);
				}
				if(zombie.name=="��������"&&random.nextInt(100)<zombie.weapon.find_rate) {//����� ������ ���
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="��������"||zombie.name=="��������"||zombie.name=="��������"||zombie.name=="��������") {//����� ������ ���
					if(random.nextInt(100)<zombie.ticket.find_rate) {
						ticket_have=true;
						System.out.println("����ö Ƽ���� ȹ���Ͽ����ϴ�!!!! ����ö�� �����Ͻ� �� �ֽ��ϴ�.");
					}
				}
				if(curse==false) {
					System.out.println("����ġ�� " + zombie.exp + "ȹ���ߴ�.");
				}else {
					System.out.println("����ġ�� ȹ���� �� �����ϴ�.");
				}
				break;
			}
			if(police_power_count>0)//�������� ����
				police_power_count--;
			if(police_power_count==0&&weakness_count==0) {//�������� �������� �ʱ�ȭ
				attack_point = replace_attack;
				guard_point = replace_guard;
				police_power_count--;
			}
			if(police_power_count==0&&weakness_count>0) {//�������� �������� �ʱ�ȭ
				attack_point -= replace_guard;
				guard_point += replace_guard;
				police_power_count--;
			}
			
			
			
			if(bindcount==0) {//������
				if(zombie.name=="��������") {//��������� ���� �����Ѵ�.
					System.out.println("���� �����մϴ�. 20�� ���� �������� �Խ��ϴ�.");
					hp-=20;
					break;
				}else {//�������� �ƴϸ� ���� �� �������� �����̻� ���
					zombie.attackZomToChar(this);//������ ���ݹ� ����������ų
				}
				if(zombie.name=="����������"&&random.nextInt(100)<30) {//������������ �� 30%Ȯ���� ����� �Ǵ�.
					System.out.println("������ ������ �к��� ���� ������ ������ �����մϴ�.");
					curse=true;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<20) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� �Ҹ���ġ�� ��Ҹ��� ���� ���� �ٴ� ���·� ����ϴ�.");
					System.out.println("������ ȸ���� �� �����ϴ�.");
					avoid_rate=0;
					stun_count=3;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<20) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� ���� ������ ���� ��ȭ ���·� ����ϴ�.");
					System.out.println("���ݷ��� 1 �����մϴ�.");
					attack_point--;
					if(attack_point<0)
						attack_point=0;
					
				}
				if(zombie.name=="��������"&&random.nextInt(100)<20) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� ������ ������ ���� ���� ���·� ����ϴ�.");
					System.out.println("�ð��� ������ ���� ���� �������� �پ��ϴ�..");
					blood_point=3;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<50) {//���������� �� 50%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� �˼� ���� ������ ���� ����� ���Ѵ´�.");
					System.out.println("mp�� 5 �����մϴ�.");
					mp-=5;
					if(mp<0)
						mp=0;
				}
				
				
				
				
			}else {
				bindcount--;
				System.out.println("���� �ӹڵǾ� �ൿ�� ���� �� �����ϴ�. ����Ƚ��:"+bindcount);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showFightDislpay(zombie);
			if (police_power_count > 0) {//������ ���� ���� ���������� �ٽ��ʱ�ȭ
				attack_point -= replace_guard;
				guard_point += replace_guard;
			}
			checkDead();
			if (dead == true)
			break;
		} while (true);
		attack_point = replace_attack;//��������
		guard_point = replace_guard;//��������
		zombie.returnZombie();// ���� �ʱ�ȭ
		if(curse==true||escape==true) {//���ֿ� �ɷȰų� ���������� ����ġ 0
			return 0;
		}else {//���� �Ȱɷ��� �ÿ� ����ġȹ��
			return zombie.exp;
		}
	}
	
	public void attackCharToZom(Zombie zombie) {// ĳ���Ͱ� ���� ����
		Random random = new Random();
		int rate = random.nextInt(100);// Ȯ���� ����
		if (rate < 10) {// ���� ������ ȸ����<---
			System.out.println("���� ������ ȸ���ߴ�.");
		} else if (rate >= 10 && rate < 10+critical_rate) {// ���񿡰� ġ��Ÿ�� ����<---
			CriticalBgm cri = new CriticalBgm();
			cri.start();
			if (attack_point - zombie.guard_point > 1) {// ��ȿ������ 1 �̻��̸�
				zombie.hp = zombie.hp - 2 * (attack_point - zombie.guard_point);
				System.out.println("����" + zombie.name + "�� �޼Ҹ� �����Ͽ� "
						+ 2 * (attack_point - zombie.guard_point) + "�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			} else {// ��ȿ������ -�̰ų� 0�̸� ����2
				zombie.hp -= 2;
				System.out.println("����" + zombie.name + "�� �޼Ҹ� �����Ͽ� 2�� ġ��Ÿ ���ظ� ������!!!!!!!!");
			}
		} else {// ����� ��Ȳ������ ���� ����<---
			AttackBgm attack = new AttackBgm();
			attack.start();
			if (attack_point - zombie.guard_point > 1) {// ��ȿ������ 1 �̻��̸�
				zombie.hp = zombie.hp - (attack_point - zombie.guard_point);
				System.out.println(
						"����" + zombie.name + "����" + (attack_point - zombie.guard_point) + "�� ���ظ� ������.");
			} else {// ��ȿ������ -�̰ų� 0�̸� ����1
				zombie.hp--;
				System.out.println("����" + zombie.name + "���� 1�� ���ظ� ������.");
			}
		}
	}
	
	public void showFightDislpay(Zombie zombie) {
		System.out.println("==============================    �� �� �� Ȳ       =================================");
		System.out.println(
				"    ���� ����                                                                                                  "
						+ zombie.name + "�� ����");
		System.out.println("");
		System.out.println("����HP:" + hp + "       ���� MP:"+mp+"                         ����HP:" + zombie.hp);
		System.out.println("���ݷ�:" + attack_point + "                                           ���ݷ�:"
				+ zombie.attack_point);
		System.out.println("����:" + guard_point + "                                            ����:"
				+ zombie.guard_point);
		System.out.println("=============================================================================");
	}
	
	public void showFoundWeapon(Weapon weapon) {//����������� �߰����� �� �޽���
		System.out.println(weapon.name+"(��)�� �߰��Ͽ����ϴ�.");
		System.out.println("==========�߰��� ������====================���� ���� ������==============");
		System.out.println("      " + weapon.name
				+ "                                                            " + Character.weapon.name);
		System.out.println("   ���ݷ�   " + weapon.attack_point + "                              "
				+ Character.weapon.attack_point);
		System.out.println("   ����   " + weapon.weight + "                              "
				+ Character.weapon.weight);
		System.out.println("===============================================================");
		System.out.println("�߰��� �������� �����Ͻðڽ��ϱ�?");
		System.out.println("1.�����Ѵ�        2.�������� �ʴ´�.");
	}
	
	public void showFoundArmor(Armor armor) {//�� �������� �߰����� �� �޽���
		System.out.println(armor.name+"(��)�� �߰��Ͽ����ϴ�.");
		System.out.println("==========�߰��� ������====================���� ���� ������==============");
		System.out.println("      " + armor.name
				+ "                                                            " + Character.armor.name);
		System.out.println("   ����   " + armor.guard_point
				+ "                              " + Character.armor.guard_point);
		System.out.println("   ����   " + armor.weight + "                              "
				+ Character.armor.weight);
		System.out.println("===============================================================");
		System.out.println("�߰��� �������� �����Ͻðڽ��ϱ�?");
		System.out.println("1.�����Ѵ�        2.�������� �ʴ´�.");
	}
	
	public void showFoundBag(Bag bag) {//���� �������� �߰����� �� �޽���
		System.out.println(bag.name+"(��)�� �߰��Ͽ����ϴ�.");
		System.out.println("==========�߰��� ������====================���� ���� ������==============");
		System.out.println("      " + bag.name
				+ "                                                  " + Character.bag.name);
		System.out.println(" �����ѵ�   " + bag.weight_limit + "                              "
				+ Character.bag.weight_limit);
		System.out.println("===============================================================");
		System.out.println("�߰��� �������� �����Ͻðڽ��ϱ�?");
		System.out.println("1.�����Ѵ�        2.�������� �ʴ´�.");
	}
	
	public void showFoundVehicle(Vehicle vehicle) {
		System.out.println(vehicle.name+"(��)�� �߰��Ͽ����ϴ�.");
		System.out.println("==========�߰��� ������====================���� ���� ������==============");
		System.out.println(
				"      " + vehicle.name + "                                                  "
						+ Character.vehicle.name);
		System.out.println("   �ൿ��   " + vehicle.move_point + "                              "
				+ Character.vehicle.move_point);
		System.out.println("   ����   " + vehicle.weight + "                              "
				+ Character.vehicle.weight);
		System.out.println("===============================================================");
		System.out.println("�߰��� �������� �����Ͻðڽ��ϱ�?");
		System.out.println("1.�����Ѵ�        2.�������� �ʴ´�.");
	}
	
	public void help() {//����
		System.out.println("============================Ư�� ������ ����=============================");
		System.out.println("1.��������: ������ ���� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("2.����������: ȸ������ 30 �����մϴ�.");
		System.out.println("3.������޽�: ��ó���� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("4.�׵�: �ߵ��� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("5.��: ���� ��� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("6.�����: ġ��Ÿ���� 30 �����մϴ�.");
		System.out.println("7.�����: �������� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("============================��ų ����=================================");
		System.out.println("1.�ߵ���: ���� ���ݷ��� 2��� �������ظ� �����ϴ�.");
		System.out.println("2.ȸ��: 0-10������ ü���� ȸ���մϴ�.");
		System.out.println("3.�ӹ�: ������ �������� 3�� �����մϴ�.");
		System.out.println("4.������ ����: 5�ϵ��� ������ ���ݷ����� ��ȯ�մϴ�");
		System.out.println("5.Ȩ��: 50%�� Ȯ���� ������ ����ŵ�ϴ�.");
		System.out.println("6.����Ÿ: ������ ü���� 50% ���ҽ�ŵ�ϴ�.");
		System.out.println("7.��ó����: ���񿡰� (0-���ݷ�)�� �������� ���ظ� �����ϴ�.");
		System.out.println("8.���ػ��: 3�ϵ��� �⺻������ ġ��Ÿ�� ��ȯ�˴ϴ�.");
		System.out.println("9.��������: �⺻���ݷ�+1�� ���ظ� ���񿡰� �����ϴ�.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("========================��Ʈ������ ȿ��=================================");
		System.out.println("1.�ǻ�: ȸ�� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("2.����: �ӹ� ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("3.�߱�: Ȩ�� ��ų�� ����Ÿ ��ų�� ����� �� �ְ� �˴ϴ�.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("=============================�����̻�=================================");
		System.out.println("1.����: ���������� �������� �߰������� �԰Ե˴ϴ�. �ð��� �帧�� ���� ���ط��� �����մϴ�.");
		System.out.println("2.�����Ҵ�: ���� �ð����� �ڵ����� ���� �Һ��մϴ�. �ൿ�� ���� �� �����ϴ�.");
		System.out.println("3.���������� ����: ���� �׿��� ����ġ�� ȹ������ ���մϴ�.");
		System.out.println("4.��ȭ: ���ݷ��� 1 �����մϴ�.��ȭ���´� ������ ���������� �����˴ϴ�.");
		System.out.println("===================================================================");
		System.out.println();
		
		
		
	}
	
	public int bossFight(Zombie zombie) {//������ �����Ѵ�.
		Scanner scan= new Scanner(System.in);
		Random random = new Random();
		replace_attack = attack_point;//���� ���ݷ� ����
		replace_guard = guard_point;//���� ���ݷ� ����
		int replace_police_attack=0;
		int replace_critical_rate=critical_rate;
		int num;
		int num_1 = 0;
		int bindcount=0;//�ӹ�ī��Ʈ
		int police_power_count=0;//���������� ī��Ʈ
		int stun_count=0;//����ī��Ʈ
		int weakness_count=0;//��ȭ ī��Ʈ
		int blood_point=0;
		int critical_count=0;
		boolean escape=false;
		boolean curse=false;
		boolean zombie_blood=false;
		
		showFightDislpay(zombie);// ���� ���� ����â�� �����ش�.
		do {
			do {
				//��ų ��Ÿ�� ����
				if (num_1 != 0) {
					for (int i = 0; i < skill_list.size(); i++) {
						if (skill_list.get(i).cooltime > 0)
							skill_list.get(i).cooltime--;
					}
				}
				if(police_power_count>0) {//���� ������ ���ȿ������
					System.out.println("������ ������ "+(police_power_count)+"�� ���ҽ��ϴ�.");
					attack_point+=replace_guard;
					guard_point-=replace_guard;
				}
				if (stun_count > 0) {//���� �������̸�
					System.out.println("���� �Ҵ� �����Դϴ�.���� Ƚ��:" + stun_count);
					stun_count--;
				} else {//������ Ǯ��������
					System.out.println("1.�⺻����      2.��ų���      3.�Һ������ Ȯ��      4.����");
					num_1 = scan.nextInt();
					switch (num_1) {
					case 1:// �⺻����
						if(critical_count>0) {//���ػ�� ����Ǿ�����
							critical_rate=100;
							attackCharToZom(zombie);
							critical_count--;
							System.out.println("���ػ����  "+critical_count+"ȸ ���ҽ��ϴ�.");
						}else {
							critical_rate=replace_critical_rate;
							attackCharToZom(zombie);
						}
						break;
					case 2:// ��ų���
						showSkillList();
						System.out.println("����� ��ų�� ��ȣ�� �Է��ϼ���.");
						num = scan.nextInt();
						try {
							if (skill_list.get(num - 1).name == "�ߵ���") {// ��ų �����ϰ� ���
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("�ߵ����� ����մϴ�.");
									zombie.hp -= attack_point * 2;// ���ݷ�*2 ��ŭ�� �������ظ� ������.
									System.out.println(
											"�ߵ����� ����Ͽ� " + zombie.name + "���� " + attack_point * 2 + "�� �������ظ� ������!!!!!");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "ȸ��") {
								int addhp = random.nextInt(11);
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true && hp < 100) {// ��� �����ϸ�
									System.out.println("ȸ���� ����մϴ�.");
									hp += addhp;// ������ ü���� ȸ���Ѵ�.
									System.out.println("ȸ���� ����Ͽ� " + addhp + "�� ü���� ȸ���ߴ�!!!!!");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
									if (hp > 100) {// ��������
										hp = 100;
									}
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "�ӹ�") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("�ӹ��� ����մϴ�.");
									bindcount += 3;
									System.out.println("������ 3�ϵ��� �ൿ�� ������ ���մϴ�.");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "������ ����") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("������ ������ ����մϴ�.");
									System.out.println("5�ϵ��� ������ ���ݷ����� ��ȯ�մϴ�.");
									police_power_count = 6;
									attack_point += replace_guard;
									guard_point -= replace_guard;
									replace_police_attack=attack_point;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "Ȩ��") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("Ȩ���� ����մϴ�.");
									if(random.nextInt(100)<50) {
										zombie.hp=0;
										System.out.println("Ȩ���� �����Ͽ����ϴ�!!!!!!!!!!");
									}else {
										System.out.println("Ȩ���� �����Ͽ����ϴ�........");
									}
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "����Ÿ") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("����Ÿ�� ����մϴ�.");
									System.out.println("������ ü���� �������� �پ��ϴ�.");
									zombie.hp-=zombie.hp/2;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "��ó����") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("��ó���⸦ ����մϴ�.");
									System.out.println("����� ���������� �߰� ���� ���ظ� �Խ��ϴ�.");
									zombie_blood=true;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "���ػ��") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("���ػ���� ����մϴ�.");
									System.out.println("������ 3���� �⺻������ ġ��Ÿ ������ �޽��ϴ�..");
									critical_count=3;
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							} else if (skill_list.get(num - 1).name == "��������") {
								skill_list.get(num - 1).checkCanUseSkill(this);// ��ų ��밡������ ���� Ȯ���ϰ�
								if (skill_list.get(num - 1).canuse == true) {// ��� �����ϸ�
									System.out.println("�������⸦ ����մϴ�.");
									zombie.hp-=(attack_point+1);
									System.out.println((attack_point+1)+"�� ���ظ� �����ϴ�.");
									skill_list.get(num - 1).useSkill(this);// ��ų�� �ߵ��Ѵ�.
								} else {// ��� �Ұ����ϸ�
									System.out.println("��ų�� ����� �� �����ϴ�. ����ȭ������ �ǵ��ư��ϴ�.");
									showFightDislpay(zombie);
									num_1 = 0;// �ٽ�
								}
							}
						} catch (IndexOutOfBoundsException e) {
							System.out.println("�ùٸ� ��ų�� �ƴմϴ�. ����ȭ������ ���ư��ϴ�.");
							showFightDislpay(zombie);
							num_1 = 0;// �ٽ�
						}
						break;
					case 3:// �Һ������ Ȯ��
						showFightInventory();
						break;
					case 4:// ��������
						System.out.println("������ �� �����ϴ�.");
						break;
					default:
						System.out.println();
					}
					if (Character.armor.name=="����������") {
						avoid_rate=40;
					}else {
						avoid_rate=10;
					}
				}
			} while (num_1 != 1 && num_1 != 2 && num_1 != 3 && num_1 != 4);
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(zombie_blood==true) {
				int blood=random.nextInt(attack_point);
				System.out.println("����� "+blood+"�� ���� �������� �Խ��ϴ�.");
				zombie.hp-=blood;
			}
			if(blood_point>0) {//�������¿� �ִٸ�
				hp-=blood_point;
				System.out.println("�������� ������"+blood_point+"��ŭ�� �������� �Խ��ϴ�.");
				blood_point--;
			}
			showFightDislpay(zombie);
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "�� �׾���.�������� �¸��ߴ�.");
				if(zombie.name=="��������"&&random.nextInt(100)<zombie.weapon.find_rate) {//����� ������ ���
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="����������"&&random.nextInt(100)<zombie.armor.find_rate) {//����� ������ ���
					showFoundArmor(zombie.armor);
					doArmor(zombie.armor);
				}
				if(zombie.name=="��������"&&random.nextInt(100)<zombie.weapon.find_rate) {//����� ������ ���
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="��������"||zombie.name=="��������"||zombie.name=="��������"||zombie.name=="��������") {//����� ������ ���
					if(random.nextInt(100)<zombie.ticket.find_rate) {
						ticket_have=true;
						System.out.println("����ö Ƽ���� ȹ���Ͽ����ϴ�!!!! ����ö�� �����Ͻ� �� �ֽ��ϴ�.");
					}
				}
				if(curse==false) {
					System.out.println("����ġ�� " + zombie.exp + "ȹ���ߴ�.");
				}else {
					System.out.println("����ġ�� ȹ���� �� �����ϴ�.");
				}
				break;
			}
			if(police_power_count>0)//�������� ����
				police_power_count--;
			if(police_power_count==0&&weakness_count==0) {//�������� �������� �ʱ�ȭ
				attack_point = replace_attack;
				guard_point = replace_guard;
				police_power_count--;
			}
			if(police_power_count==0&&weakness_count>0) {//�������� �������� �ʱ�ȭ
				attack_point -= replace_guard;
				guard_point += replace_guard;
				police_power_count--;
			}
			
			
			
			if(bindcount==0) {//������
		
				zombie.attackZomToChar(this);//������ ���ݹ� ����������ų

				if(zombie.name=="��������"&&random.nextInt(100)<30) {//������������ �� 30%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� �к��� ���� ������ ������ �����մϴ�.");
					curse=true;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<20) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� �Ҹ���ġ�� ��Ҹ��� ���� ���� �ٴ� ���·� ����ϴ�.");
					System.out.println("������ ȸ���� �� �����ϴ�.");
					avoid_rate=0;
					stun_count=3;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<10) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� ���� ������ ���� ��ȭ ���·� ����ϴ�.");
					System.out.println("���ݷ��� 1 �����մϴ�.");
					attack_point--;
					if(attack_point<0)
						attack_point=0;
					
				}
				if(zombie.name=="��������"&&random.nextInt(100)<20) {//���������� �� 20%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� ������ ������ ���� ���� ���·� ����ϴ�.");
					System.out.println("�ð��� ������ ���� ���� �������� �پ��ϴ�..");
					blood_point=3;
				}
				if(zombie.name=="��������"&&random.nextInt(100)<50) {//���������� �� 50%Ȯ���� ����� �Ǵ�.
					System.out.println("���������� �˼� ���� ������ ���� ����� ���Ѵ´�.");
					System.out.println("mp�� 5 �����մϴ�.");
					mp-=5;
				}
				
				
				
				
			}else {
				bindcount--;
				System.out.println("���� �ӹڵǾ� �ൿ�� ���� �� �����ϴ�. ����Ƚ��:"+bindcount);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showFightDislpay(zombie);
			if (police_power_count > 0) {//������ ���� ���� ���������� �ٽ��ʱ�ȭ
				attack_point -= replace_guard;
				guard_point += replace_guard;
			}
			checkDead();
			if (dead == true)
			break;
		} while (true);
		attack_point = replace_attack;//��������
		guard_point = replace_guard;//��������
		zombie.returnZombie();// ���� �ʱ�ȭ
		if(curse==true||escape==true) {//���ֿ� �ɷȰų� ���������� ����ġ 0
			return 0;
		}else {//���� �Ȱɷ��� �ÿ� ����ġȹ��
			return zombie.exp;
		}
	}	
}
