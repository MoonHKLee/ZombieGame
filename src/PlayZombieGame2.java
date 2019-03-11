import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
import javazoom.jl.player.Player;

public class PlayZombieGame2 extends Thread{
	static boolean is_city=true;
	static boolean play_now = true;
	static boolean stop=false;
	static boolean play_nowz = true;
	
	

	public void run() {
		int num_1 = 0;// �Է°� 1
		int num_2 = 0;// �Է°� 2
		int num_3 = 0;// �Է°� 3
		int now_map = 0;// �� ���� ��Ȳ
		int infection_count=0;
		boolean game_stop=false;//�������̺�Ʈ�� �߻��ϴ� ���� ���ξ����� ��������
		Random random = new Random();
	
		//��ų ��ü ����
		Skill blade_attack = new Skill(15, 3, 0, true, "�ߵ���");// ����/ ��Ÿ��/ ������Ÿ��/ ��밡��/ �̸�
		Skill recovery = new Skill(18, 3, 0, true, "ȸ��");
		Skill bind = new Skill(20, 5, 0, true, "�ӹ�");
		Skill power_armor = new Skill(30, 10, 0, true, "������ ����");
		Skill homerun=new Skill(70, 10, 0, true, "Ȩ��");
		Skill clutch_hit=new Skill(20, 3, 0, true, "����Ÿ");
		Skill blood=new Skill(10, 2, 0, true, "��ó����");
		Skill aimed_shot=new Skill(10, 2, 0, true, "���ػ��");
		Skill throw_rib=new Skill(10, 2, 0, true, "��������");
		
		//Ƽ�ϻ���
		Item ticket = new Item("����ö Ƽ��",0,20);
		
		// �� ��ü ����
		Armor baseball_jacket = new Armor("�߱����", 10, 20, 1,null);// �̸�/����/�߰�Ȯ��/����/������ų
		Armor police_armor = new Armor("��������", 20, 3, 3,null);
		Armor leather_jacket = new Armor("������Ŷ", 10, 3, 2,null);
		Armor doll_clothes = new Armor("������", 15, 3, 2,null);
		Armor police_shield = new Armor("��������", 30, 1, 5,power_armor);
		Armor doctor_jacket = new Armor("�ǻ簡��", 5, 10, 1,null);
		Armor dirty_jacket = new Armor("����������", 5, 100, 1,null);//ȸ����
		Armor suit = new Armor("�纹", 0, 0, 0,null);// �⺻

		// ���� ��ü����
		Weapon bat = new Weapon("�߱������", 10, 20, 2,null);// �̸�/����/�߰�Ȯ��/���ݷ�/������ų
		Weapon mop = new Weapon("��ɷ��ڷ�", 7, 5, 1,null);
		Weapon aed = new Weapon("����������", 20, 2, 3,null);
		Weapon mess = new Weapon("������޽�", 5, 3, 2,blood);
		Weapon bambooblade = new Weapon("�׵�", 10, 5, 2,blade_attack);
		Weapon knife = new Weapon("��Į", 5, 2, 3,null);
		Weapon axe = new Weapon("����", 15, 1, 4,null);
		Weapon gun = new Weapon("��", 5, 1, 5,aimed_shot);
		Weapon electricgun = new Weapon("������ݱ�", 5, 2, 3,null);
		Weapon hand = new Weapon("�Ǽ�", 0, 0, 0,null);
		Weapon bone = new Weapon("�����",10,100,3,null);//ġ��Ÿ
		Weapon rib = new Weapon("�����",5,100,1,throw_rib);

		// Ż�� ��ü����
		Vehicle shoes = new Vehicle("�ȭ", 0, 0, 0);// �̸�/����/�߰�Ȯ��/�ൿ��
		Vehicle inline = new Vehicle("�ζ��� ������Ʈ", 10, 5, 1);
		Vehicle bicycle = new Vehicle("������", 20, 3, 2);
		Vehicle bike = new Vehicle("�������", 50, 1, 3);

		// ���� ��ü����
		Bag vinyl = new Bag("��Һ���", 20, 20);// �̸�/�߰�Ȯ��/�����ѵ�
		Bag backpack = new Bag("����", 10, 50);
		Bag police_bag = new Bag("��������", 5, 100);
		Bag paper_bag = new Bag("��������", 0, 0);

		// ���� ��ü����
		HealItem band = new HealItem("�ش�", 3, 20, 3);// �̸�/����/Ȯ��/����
		HealItem medicine = new HealItem("�׻���", 3, 10, 5);
		HealItem medic_kit = new HealItem("������Ű��", 5, 3, 10);

		// ��ǰ ��ü����
		Food bread = new Food("��", 3, 10, 50);// �̸�/����/����������
		Food dry_meat = new Food("����", 1, 5, 30);
		Food spam = new Food("����", 1, 3, 50);

		Scanner scan = new Scanner(System.in);
		Character character = new Character();
		
		
		//��ų����� ĳ���Ϳ� ���
		ArrayList<Skill> skill_list = new ArrayList<>();
		
		
		
		
		// ���� ��ü ����
		Zombie menzombie = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
		Zombie womenzombie = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
		Zombie animalzombie = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
		Zombie lordzombie = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
		int bigcity_monster_appearance_rate = menzombie.appearance_rate + womenzombie.appearance_rate// �뵵�� ���� ���� Ȯ��
				+ animalzombie.appearance_rate + lordzombie.appearance_rate;
		//////////////////////////////////////////////////////////////////////// �� ��������
		//////////////////////////////////////////////////////////////////////// �Ʒ� �ʵ�����
		Zombie dirtyzombie = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
		Zombie stunzombie = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
		Zombie bombzombie = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
		Zombie stenchzombie = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
		Zombie mutationzombie = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
		Zombie giantzombie = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
		Zombie crowdzombie = new Zombie(30, 5, 1, "���񹫸�", 2, 30, 30,null,null,null);
		Zombie lastzombie = new Zombie(30, 8, 2, "��������", 1, 30, 1000000,null,null,null);
		int uptown_monster_appearance_rate = dirtyzombie.appearance_rate + stunzombie.appearance_rate// �ܰ� ���� ���� Ȯ��
				+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
				+ giantzombie.appearance_rate;
		
		//����
		//��Ʈ������ȿ���� ĳ���Ϳ� ���
		character.setSkillList(skill_list);
		character.setSkill(recovery,bind,homerun,clutch_hit);
		// �⺻����
		//character.equipWeapon(hand);
		//character.equipWeapon(mess);
		//character.equipWeapon(bat);
		character.equipWeapon(gun);
		//character.equipArmor(suit);
		//character.equipArmor(doctor_jacket);
		character.equipArmor(police_armor);
		//character.equipArmor(baseball_jacket);
		character.equipBag(paper_bag);
		character.equipVehicle(shoes);
		character.equipBand(band);
		character.equipMedicine(medicine);
		character.equipMedicKit(medic_kit);
		character.equipBread(bread);
		character.equipDryMeat(dry_meat);
		character.equipSpam(spam);
		//��Ʈ������ Ȯ��
		character.setItemCheck();
		
		
		
		//���� ����
		System.out.println("2018�� 08�� 30��. ������ ���� ���̷����� �ڵ�����, ������ ������ ã�ƿԴ�.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("�׽ð� ����ϰ� ������ ���� �����忡�� �ǰ��� ��ٸ��� �ִ� ��ö��.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("���۽����� ���̴�ģ ����� ������ �������� �Ƽ������� �ǰ� �� �ڽŵ� ����� ���� ���Ѵ�.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("���� �Ѱ���� ���� ������ ��ö���� ó���� �ֺ���Ȳ�� ����� ��ġ ���Ѵ�.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("�׽ð� �������� �����ǿ� û�ʹ� �뺯���� ��ǥ�� �ϰ��־���. ��ǥ������ ������ ����.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("���� ���ѹα��� ���� ���� ������ �����ϴ� �Ǽ��� ������̷����� â���Ͽ���");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("��û�� �αٿ� ����κ����� ������ ���� ���ǼҰ� �ϼ��Ǿ����� �����ڵ��� ���Ǽҷ� �̵��϶�� ��.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print("��ö���� ����� ���� ������ ���Ǽҷ� �̵��� ����� �ϰ� ���Ǽҷ� ���ϴµ�.");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(".");
		
		//���㾲���� ����
		Time time = new Time(false);
		time.start();
		
		//���������� ����
		Weather weather = new Weather(false,character);
		weather.start();
		
		BgmMain bgmmain = new BgmMain();
		bgmmain.start();
		
		//���º��� ������ ����
		MpPlus mpplus = new MpPlus();
		mpplus.start();
		
		//������ݽ����� ����
		ZombieAttack zbat = new ZombieAttack(character,PlayZombieGame2.currentThread());
		try {
		zbat.start();
		
		
		try {//������ ���
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		First_City:
		do {
			System.out.println(
					"===================================================================================================");
			System.out.println("���� ��ġ:�뵵�� �߽ɰ�");
			System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
			System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
			System.out.println("ȸ����:" + Character.avoid_rate);
			System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9:�������      0:����");
			//��������
			if(Infection.safe==false&&infection_count==0) {
				infection_count++;
				Infection infection = new Infection();
				infection.start();
			}
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
			if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
				scan1=null;
				do {
					sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("���� ��ġ:�뵵�� �߽ɰ�");
				System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
				System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
				System.out.println("ȸ����:" + Character.avoid_rate);
				System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9:�������      0:����");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			
			switch (num_1) {
			case 1:// �ֺ� �ǹ� Ž��
				Zombie menzombie1 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
				Zombie womenzombie1 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// �뵵�� ���� ���� Ȯ��
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// ������Ȳ�߻�or�̹߻�
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(animalzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();//���� ����� ���� ������ �� ����(�������/����ġ/ü��/������/�����ð�)
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:�뵵�� �ǹ� �ֺ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
					System.out.println("1:�б�      2:����      3:������      4:������Ʈ      5.����ö      0:�뵵�� �߽ɰ��� �̵�");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan2=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:�뵵�� �ǹ� �ֺ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
						System.out.println("1:�б�      2:����      3:������      4:������Ʈ      5.����ö      0:�뵵�� �߽ɰ��� �̵�");
						Scanner scan2_1 = new Scanner(System.in);
						num_2 = scan2_1.nextInt();
						stop=false;
					}
					
					switch (num_2) {
					case 1:// �б��� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("�б��� Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (bat.find_rate + mop.find_rate + bambooblade.find_rate
								+ baseball_jacket.find_rate + backpack.find_rate + 20)) {// �б� ������ �߰�Ȯ��

							int num = random.nextInt(bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate + backpack.find_rate + 20);
							if (num < bat.find_rate) {// �߱������ �߰�
								character.showFoundWeapon(bat);
								character.doWeapon(bat);
							} else if (num >= bat.find_rate && num < bat.find_rate + mop.find_rate) {// ��ɷ� �߰�
								character.showFoundWeapon(mop);
								character.doWeapon(mop);
							} else if (num >= bat.find_rate + mop.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate) {// �׵� �߰�
								character.showFoundWeapon(bambooblade);
								character.doWeapon(bambooblade);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate) {// �߱���� �߰�
								character.showFoundArmor(baseball_jacket);
								character.doArmor(baseball_jacket);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate + backpack.find_rate) {// ���� �߰�
								character.showFoundBag(backpack);
								character.doBag(backpack);
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();//���� ����� �߻��ϴ� �޼ҵ�
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// ������ Ž��
						System.out.println("������ Ž���մϴ�.");
						time.timeStop();
						weather.weatherStop();
						//������ �̿ϼ������ ã���� �ִ�.
						if(Infection.safe==false&&random.nextInt(100)<20) {//20�� Ȯ���� ���ã�� �� ����
							System.out.println("�̿ϼ� ����� ã�Ƴ½��ϴ�. �������¿��� �����˴ϴ�.");
							Infection.safe=true;
							try {//���������� ���� ���ð�
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						// Ȯ������
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20 )) {// ���� ������ �߰�Ȯ��
																									// 20=���� �߰�Ȯ��

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// ���������� �߰�
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// ������޽� �߰�
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// �ǻ簡�� �߰�-
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// �ش� �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// �׻��� �߰�
								System.out.println("�׻����� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("�׻����� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// �Ƿ�ŰƮ �߰�
								System.out.println("�Ƿ�� ŰƮ�� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("�Ƿ�� ŰƮ�� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ����� ����
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// �������� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("�������� Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (gun.find_rate + electricgun.find_rate + police_armor.find_rate
								+ police_shield.find_rate + police_bag.find_rate + 20)) {// ������ ������ �߰�Ȯ��

							int num = random.nextInt(gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate + 20);
							if (num < gun.find_rate) {// �� �߰�
								character.showFoundWeapon(gun);
								character.doWeapon(gun);
							} else if (num >= gun.find_rate && num < gun.find_rate + electricgun.find_rate) {// ������ݱ� �߰�
								character.showFoundWeapon(electricgun);
								character.doWeapon(electricgun);

							} else if (num >= gun.find_rate + electricgun.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate) {// �������� �߰�
								character.showFoundArmor(police_armor);
								character.doArmor(police_armor);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate) {// �������� �߰�
								character.showFoundArmor(police_shield);
								character.doArmor(police_shield);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate) {// ���� �Ƿ��� �߰�
								character.showFoundBag(police_bag);
								character.doBag(police_bag);
							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate + bike.find_rate) {// ��Ż����ũ
																												// �߰�
								character.showFoundVehicle(bike);
								character.doVehicle(bike);
							} else {// ����� ����
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// ������Ʈ�� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("������Ʈ�� Ž���մϴ�.");
						if (random.nextInt(100) < (knife.find_rate + axe.find_rate + doll_clothes.find_rate
								+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
								+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20)) {// ������Ʈ ������ �߰�Ȯ��

							int num = random.nextInt(knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20);
							if (num < knife.find_rate) {// �� �߰�
								character.showFoundWeapon(knife);
								character.doWeapon(knife);
							} else if (num >= knife.find_rate && num < knife.find_rate + axe.find_rate) {// ������ݱ� �߰�
								character.showFoundWeapon(axe);
								character.doWeapon(axe);

							} else if (num >= knife.find_rate + axe.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate) {// �������� �߰�
								character.showFoundArmor(doll_clothes);
								character.doArmor(doll_clothes);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate) {// ������Ŷ �߰�
								character.showFoundArmor(leather_jacket);
								character.doArmor(leather_jacket);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate) {// ��Һ��� �߰�
								character.showFoundBag(vinyl);
								character.doBag(vinyl);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate) {// �ζ��� �߰�
								character.showFoundVehicle(inline);
								character.doVehicle(inline);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate) {// ������ �߰�
								character.showFoundVehicle(bicycle);
								character.doVehicle(bicycle);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate) {// �� �߰�
								System.out.println("���� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("���� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate) {// ���� �߰�
								System.out.println("������ �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + dry_meat.weight) {
									dry_meat.count++;
									Character.weight += dry_meat.weight;
									System.out.println("������ ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate
											+ spam.find_rate) {// ���� �߰�
								System.out.println("������ �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + spam.weight) {
									spam.count++;
									Character.weight += spam.weight;
									System.out.println("������ ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ����� ����
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}

							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					
					case 5://��������ö�� �̵�
						time.timeStop();
						weather.weatherStop();
						System.out.println("���� ����ö�� �̵��մϴ�.");
						System.out.println("������� óġ�ϴٺ��� ����ö Ƽ���� �����ϸ� ����ö Ƽ���� �̿��ϸ� ���� ����ö�� �̿��� �� �ֽ��ϴ�.");
						System.out.println("���� ����ö�� �̿��ϸ� ���ÿܰ��� ��ġ�� �ʰ� �ٷ� ���� ���÷� �̵��� �� �ֽ��ϴ�.");
						if (Infection.safe == true) {
							if (Character.ticket_have == true) {
								System.out.println("����ö Ƽ���� ����մϴ�.");
								System.out.println("����ö�� ž���մϴ�.");
								System.out.println("���� ���ñ��� ������ �̵��մϴ�.");
								now_map = 29;
								time.timeStart();
								weather.weatherStart();
								// ����öƼ�� ȸ���� �̴ϰ��� �����ϰ� ȸ��
								break First_City;// ���� Ż��
							} else {
								System.out.println("����ö Ƽ���� �����ϴ�. ���� ����ö�� �̿��Ͻ� �� �����ϴ�.");
								time.timeStart();
								weather.weatherStart();
								break;
							}
						}else {
							System.out.println("�������¿����� ����ö�� �̿��� �� �����ϴ�. �������¸� ���� �����ؾ��մϴ�.");
							time.timeStart();
							weather.weatherStart();
							break;
						}
						
					case 0:// �뵵�� �߽ɰ��� �̵�
						System.out.println("�뵵�� �߽ɰ��� �ǵ��ư��ϴ�.");
						break;
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_2 != 0);
				break;

			case 2:// ����ǰ Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStop();
				weather.weatherStop();
				break;

			case 3:// ���Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStop();
				weather.weatherStop();
				break;

			case 4:// ���� ��ҷ� �̵�
				System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
				break;
				
			case 9://����� (on/off)
				try {
					if (play_now==true) {//����
						play_now=false;
						bgmmain.suspend();
						System.out.println("������� Off");
					} else {//�ѱ�
						bgmmain.resume();
						System.out.println("������� On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// ����
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStop();
				weather.weatherStop();
				break;

			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_1 != 4);
		now_map++;// ���൵ +
		
		
		//��������ö ���α׷�
		time.timeStop();
		time.night.suspend();
		weather.rain2.suspend();
		weather.weatherStop();
		if (Character.ticket_have == true&&now_map==30) {//Ƽ�ϰ����� ������ ����ö ����
			game_stop=true;
			Character.ticket_have = false;//Ƽ�� �Һ�
			Subway subway = new Subway(game_stop);
			subway.start();
			do {//���� �������� ���� ����
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(subway.game_stop!=false);
		}
		time.timeStart();
		time.night.resume();
		weather.rain2.resume();
		weather.weatherStart();
		
		
		
		
		
		
		// �뵵�� ���. �ʵ�� �̵�
		if (now_map > 0 && now_map <= 29) {
			is_city=false;//�ܰ�������
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// �ܰ� ���� ���� Ȯ��
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("���ø� �����.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// ���������� ����
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// �Ŵ����� ����
						Character.exp += character.startFight(giantzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();

				// ���ÿ��� ���������µ� ����
				// ���ú��� �����ϰ� �پ��� ���͵��� ����
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:���ÿܰ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+"
							+ Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"���� �ð�:" + Character.left_time + "               �ൿ��:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ൿ�� �Է��Ͻÿ�.");
					System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan1=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:���ÿܰ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ൿ�� �Է��Ͻÿ�.");
						System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// �ֺ� Ž��
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// �ܰ� ���� ���� Ȯ��
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// ����Ȯ��
							System.out.println("������ �߻��Ͽ����ϴ�.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// ���������� ����
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// �Ŵ����� ����
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// ���߰�
								System.out.println("���� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("���� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// �ش�
																													// �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// �������� ����
								Character.exp += character.startFight(lordzombie);
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 2:// ����ǰ Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// ���Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// ���� ��ҷ� �̵�
						System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
						break;
						
					case 9://����� (on/off)
						try {
							if (play_now==true) {//����
								play_now=false;
								bgmmain.suspend();
								System.out.println("������� Off");
							} else {//�ѱ�
								bgmmain.resume();
								System.out.println("������� On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;
						
					case 0:// ����
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;
						
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 30);// �̵��Ÿ� 29���� �ݺ� 30�� �Ǹ� �߼ҵ��ÿ� ����
		}
		
		
		
		
		// �߼ҵ��� ����
		do {
			is_city=true;//����������
			System.out.println(
					"===================================================================================================");
			System.out.println("���� ��ġ:�߼ҵ��� �߽ɰ�");
			System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
			System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
			System.out.println("ȸ����:" + Character.avoid_rate);
			System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9.�������      0.����");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
			if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
				scan1=null;
				do {
					sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("���� ��ġ:�߼ҵ��� �߽ɰ�");
				System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
				System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
				System.out.println("ȸ����:" + Character.avoid_rate);
				System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9:�������      0:����");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// �ֺ� �ǹ� Ž��
				Zombie menzombie1 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
				Zombie womenzombie1 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// �뵵�� ���� ���� Ȯ��
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// ������Ȳ�߻�or�̹߻�
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(animalzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();//���� ����� ���� ������ �� ����(�������/����ġ/ü��/������/�����ð�)
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:�߼ҵ��� �ǹ� �ֺ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
					System.out.println("1:�б�      2:����      0:�߼ҵ��� �߽ɰ��� �̵�");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan2=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:�߼ҵ��� �ǹ� �ֺ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
						System.out.println("1:�б�      2:����      0:�߼ҵ��� �߽ɰ��� �̵�");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// �б��� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("�б��� Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (bat.find_rate + mop.find_rate + bambooblade.find_rate
								+ baseball_jacket.find_rate + backpack.find_rate + 20)) {// �б� ������ �߰�Ȯ��

							int num = random.nextInt(bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate + backpack.find_rate + 20);
							if (num < bat.find_rate) {// �߱������ �߰�
								character.showFoundWeapon(bat);
								character.doWeapon(bat);
							} else if (num >= bat.find_rate && num < bat.find_rate + mop.find_rate) {// ��ɷ� �߰�
								character.showFoundWeapon(mop);
								character.doWeapon(mop);
							} else if (num >= bat.find_rate + mop.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate) {// �׵� �߰�
								character.showFoundWeapon(bambooblade);
								character.doWeapon(bambooblade);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate) {// �߱���� �߰�
								character.showFoundArmor(baseball_jacket);
								character.doArmor(baseball_jacket);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate + backpack.find_rate) {// ���� �߰�
								character.showFoundBag(backpack);
								character.doBag(backpack);
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();//���� ����� �߻��ϴ� �޼ҵ�
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// ������ Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("������ Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// ���� ������ �߰�Ȯ��
																									// 20=���� �߰�Ȯ��

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// ���������� �߰�
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// ������޽� �߰�
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// �ǻ簡�� �߰�
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// �ش� �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// �׻��� �߰�
								System.out.println("�׻����� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("�׻����� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// �Ƿ�ŰƮ �߰�
								System.out.println("�Ƿ�� ŰƮ�� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("�Ƿ�� ŰƮ�� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 0:// �߼ҵ��� �߽ɰ��� �̵�
						System.out.println("�߼ҵ��� �߽ɰ��� �ǵ��ư��ϴ�.");
						break;
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_2 != 0);
				break;

			case 2:// ����ǰ Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// ���Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// ���� ��ҷ� �̵�
				System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
				break;
				
			case 9://����� (on/off)
				try {
					if (play_now==true) {//����
						play_now=false;
						bgmmain.suspend();
						System.out.println("������� Off");
					} else {//�ѱ�
						bgmmain.resume();
						System.out.println("������� On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;	
		
			case 0:// ����
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_1 != 4);
		now_map++;
		
		
		
		
		//�߼ҵ��� 1����� ���ÿܰ�
		if (now_map > 30 && now_map <= 59) {
			is_city=false;
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// �ܰ� ���� ���� Ȯ��
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("���ø� �����.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// ���������� ����
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// �Ŵ����� ����
						Character.exp += character.startFight(giantzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();

				// ���ÿ��� ���������µ� ����
				// ���ú��� �����ϰ� �پ��� ���͵��� ����
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:���ÿܰ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+"
							+ Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"���� �ð�:" + Character.left_time + "               �ൿ��:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ൿ�� �Է��Ͻÿ�.");
					System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan1=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:���� �ܰ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ൿ�� �Է��Ͻÿ�.");
						System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// �ֺ� Ž��
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// �ܰ� ���� ���� Ȯ��
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// ����Ȯ��
							System.out.println("������ �߻��Ͽ����ϴ�.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// ���������� ����
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// �Ŵ����� ����
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// ���߰�
								System.out.println("���� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("���� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// �ش�
																													// �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// �������� ����
								Character.exp += character.startFight(lordzombie);
							}
							time.timeStart();
							weather.weatherStart();
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						break;
					case 2:// ����ǰ Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// ���Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// ���� ��ҷ� �̵�
						System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
						break;
						
					case 9://����� (on/off)
						try {
							if (play_now==true) {//����
								play_now=false;
								bgmmain.suspend();
								System.out.println("������� Off");
							} else {//�ѱ�
								bgmmain.resume();
								System.out.println("������� On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;
						
					case 0:// ����
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;

					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 60);// �̵��Ÿ� 59���� �ݺ� 60�� �Ǹ� �߼ҵ��ÿ� ����
		}

		// �߼ҵ��÷� �̵�
		Third_city:
		do {
			is_city=true;
			System.out.println(
					"===================================================================================================");
			System.out.println("���� ��ġ:�߼ҵ��� �߽ɰ�");
			System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
			System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
			System.out.println("ȸ����:" + Character.avoid_rate);
			System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9.�������      0.����");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
			if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
				scan1=null;
				do {
					sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("���� ��ġ:�߼ҵ��� �߽ɰ�");
				System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
				System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
				System.out.println("ȸ����:" + Character.avoid_rate);
				System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9.�������      0.����");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// �ֺ� �ǹ� Ž��
				Zombie menzombie1 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
				Zombie womenzombie1 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// �뵵�� ���� ���� Ȯ��
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// ������Ȳ�߻�or�̹߻�
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(animalzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();//���� ����� ���� ������ �� ����(�������/����ġ/ü��/������/�����ð�)

				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:�߼ҵ��� �ǹ� �ֺ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
					System.out.println("1:����      2:������Ʈ      3.����ö      0:�߼ҵ��� �߽ɰ��� �̵�");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan2=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:�߼ҵ��� �ǹ� �ֺ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
						System.out.println("1:����      2:������Ʈ      3.����ö      0:�߼ҵ��� �߽ɰ��� �̵�");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// ������ Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("������ Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// ���� ������ �߰�Ȯ��
																									// 20=���� �߰�Ȯ��

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// ���������� �߰�
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// ������޽� �߰�
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// �ǻ簡�� �߰�
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// �ش� �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// �׻��� �߰�
								System.out.println("�׻����� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("�׻����� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// �Ƿ�ŰƮ �߰�
								System.out.println("�Ƿ�� ŰƮ�� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("�Ƿ�� ŰƮ�� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// ������Ʈ�� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("������Ʈ�� Ž���մϴ�.");
						if (random.nextInt(100) < (knife.find_rate + axe.find_rate + doll_clothes.find_rate
								+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
								+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20)) {// ������Ʈ ������ �߰�Ȯ��

							int num = random.nextInt(knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20);
							if (num < knife.find_rate) {// �� �߰�
								character.showFoundWeapon(knife);
								character.doWeapon(knife);
							} else if (num >= knife.find_rate && num < knife.find_rate + axe.find_rate) {// ������ݱ� �߰�
								character.showFoundWeapon(axe);
								character.doWeapon(axe);

							} else if (num >= knife.find_rate + axe.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate) {// �������� �߰�
								character.showFoundArmor(doll_clothes);
								character.doArmor(doll_clothes);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate) {// ������Ŷ �߰�
								character.showFoundArmor(leather_jacket);
								character.doArmor(leather_jacket);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate) {// ��Һ��� �߰�
								character.showFoundBag(vinyl);
								character.doBag(vinyl);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate) {// �ζ��� �߰�
								character.showFoundVehicle(inline);
								character.doVehicle(inline);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate) {// ������ �߰�
								character.showFoundVehicle(bicycle);
								character.doVehicle(bicycle);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate) {// �� �߰�
								System.out.println("���� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("���� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate) {// ���� �߰�
								System.out.println("������ �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + dry_meat.weight) {
									dry_meat.count++;
									Character.weight += dry_meat.weight;
									System.out.println("������ ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate
											+ spam.find_rate) {// ���� �߰�
								System.out.println("������ �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + spam.weight) {
									spam.count++;
									Character.weight += spam.weight;
									System.out.println("������ ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
						
					case 3://��������ö�� �̵�
						System.out.println("���� ����ö�� �̵��մϴ�.");
						System.out.println("������� óġ�ϴٺ��� ����ö Ƽ���� �����ϸ� ����ö Ƽ���� �̿��ϸ� ���� ����ö�� �̿��� �� �ֽ��ϴ�.");
						System.out.println("���� ����ö�� �̿��ϸ� ���ÿܰ��� ��ġ�� �ʰ� �ٷ� ���� ���÷� �̵��� �� �ֽ��ϴ�.");
						if(Character.ticket_have==true) {
							System.out.println("����ö Ƽ���� ����մϴ�.");
							System.out.println("����ö�� ž���մϴ�.");
							System.out.println("���� ���ñ��� ������ �̵��մϴ�.");
							now_map=89;
							//����öƼ�� ȸ���� �̴ϰ��� �����ϰ� ȸ��
							break Third_city;//���� Ż��
						}else {
							System.out.println("����ö Ƽ���� �����ϴ�. ���� ����ö�� �̿��Ͻ� �� �����ϴ�.");
						}
						
					case 0:// �߼ҵ��� �߽ɰ��� �̵�
						System.out.println("�߼ҵ��� �߽ɰ��� �ǵ��ư��ϴ�.");
						break;
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_2 != 0);
				break;

			case 2:// ����ǰ Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// ���Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// ���� ��ҷ� �̵�
				System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
				break;
				
			case 9://����� (on/off)
				try {
					if (play_now==true) {//����
						play_now=false;
						bgmmain.suspend();
						System.out.println("������� Off");
					} else {//�ѱ�
						bgmmain.resume();
						System.out.println("������� On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// ����
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_1 != 4);
		now_map++;
		
		
		//��������ö ���α׷�
		time.timeStop();
		time.night.suspend();
		weather.rain2.suspend();
		weather.weatherStop();
		if (Character.ticket_have == true&&now_map==90) {//Ƽ�ϰ����� ������ ����ö ����
			game_stop=true;
			Character.ticket_have = false;//Ƽ�� �Һ�
			Subway subway = new Subway(game_stop);
			subway.start();
			do {//���� �������� ���� ����
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(subway.game_stop!=false);
		}
		time.timeStart();
		time.night.resume();
		weather.rain2.resume();
		weather.weatherStart();

		
		
		
		
		// �ʵ�� �̵�
		if (now_map > 60 && now_map <= 89) {
			is_city=false;
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// �ܰ� ���� ���� Ȯ��
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("���ø� �����.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// ���������� ����
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// �Ŵ����� ����
						Character.exp += character.startFight(giantzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();

				// ���ÿ��� ���������µ� ����
				// ���ú��� �����ϰ� �پ��� ���͵��� ����
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:���ÿܰ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+"
							+ Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"���� �ð�:" + Character.left_time + "               �ൿ��:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ൿ�� �Է��Ͻÿ�.");
					System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan1=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:���ÿܰ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ൿ�� �Է��Ͻÿ�.");
						System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// �ֺ� Ž��
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "��������", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "��������", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "��������", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "��������", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// �ܰ� ���� ���� Ȯ��
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// ����Ȯ��
							System.out.println("������ �߻��Ͽ����ϴ�.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// ���������� ����
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// �������� ����
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// �Ŵ����� ����
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// ���߰�
								System.out.println("���� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("���� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// �ش�
																													// �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// �������� ����
								Character.exp += character.startFight(lordzombie);
							}
							time.timeStart();
							weather.weatherStart();
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						break;
					case 2:// ����ǰ Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// ���Ȯ��
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// ���� ��ҷ� �̵�
						System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
						break;

					case 9://����� (on/off)
						try {
							if (play_now==true) {//����
								play_now=false;
								bgmmain.suspend();
								System.out.println("������� Off");
							} else {//�ѱ�
								bgmmain.resume();
								System.out.println("������� On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;	
						
					case 0:// ����
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;
						
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 90);// �̵��Ÿ� 89���� �ݺ� 90�� �Ǹ� �߼ҵ��ÿ� ����
		}

		// ���������÷� �̵�
		do {
			is_city=true;
			System.out.println(
					"===================================================================================================");
			System.out.println("���� ��ġ:������ ���� �߽ɰ�");
			System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
			System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
			System.out.println("ȸ����:" + Character.avoid_rate);
			System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9.�������      0.����");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
			if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
				scan1=null;
				do {
					sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("���� ��ġ:������ ���� �߽ɰ�");
				System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
				System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
				System.out.println("ȸ����:" + Character.avoid_rate);
				System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("�ൿ�� �Է��Ͻÿ�.");
				System.out.println("1:�ֺ� �ǹ� Ž��      2:�κ��丮 Ȯ��      3:�������� ��� Ȯ��     4:������ҷ� �̵�      9.�������      0.����");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// �ֺ� �ǹ� Ž��
				Zombie menzombie1 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
				Zombie womenzombie1 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// �뵵�� ���� ���� Ȯ��
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// ������Ȳ�߻�or�̹߻�
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// ����Ȯ��
					System.out.println("������ �߻��Ͽ����ϴ�.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// �������� ����
						Character.exp += character.startFight(animalzombie1);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("�ƹ� �ϵ� �߻����� �ʾҽ��ϴ�.");
				}
				character.fightEndCheck();//���� ����� ���� ������ �� ����(�������/����ġ/ü��/������/�����ð�)

				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:������ ���� �ǹ� �ֺ�");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
					System.out.println("1:����      2:������     0:�߼ҵ��� �߽ɰ��� �̵�");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
					if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
						scan2=null;
						do {
							sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("���� ��ġ:������ ���� �ǹ� �ֺ�");
						System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
						System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
						System.out.println("ȸ����:" + Character.avoid_rate);
						System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("�ֺ� �ǹ��� Ž���Ѵ�. ��� �ǹ��� Ž���Ͻðڽ��ϱ�?");
						System.out.println("1:����      2:������     0:�߼ҵ��� �߽ɰ��� �̵�");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// ������ Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("������ Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// ���� ������ �߰�Ȯ��
																									// 20=���� �߰�Ȯ��

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// ���������� �߰�
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// ������޽� �߰�
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// �ǻ簡�� �߰�
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// �ش� �߰�
								System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// �׻��� �߰�
								System.out.println("�׻����� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("�׻����� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// �Ƿ�ŰƮ �߰�
								System.out.println("�Ƿ�� ŰƮ�� �߰��Ͽ����ϴ�.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("�Ƿ�� ŰƮ�� ȹ���Ͽ����ϴ�.");
								} else
									System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// �������� Ž��
						time.timeStop();
						weather.weatherStop();
						System.out.println("�������� Ž���մϴ�.");
						// Ȯ������
						if (random.nextInt(100) < (gun.find_rate + electricgun.find_rate + police_armor.find_rate
								+ police_shield.find_rate + police_bag.find_rate + 20)) {// ������ ������ �߰�Ȯ��

							int num = random.nextInt(gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate + 20);
							if (num < gun.find_rate) {// �� �߰�
								character.showFoundWeapon(gun);
								character.doWeapon(gun);
							} else if (num >= gun.find_rate && num < gun.find_rate + electricgun.find_rate) {// ������ݱ� �߰�
								character.showFoundWeapon(electricgun);
								character.doWeapon(electricgun);

							} else if (num >= gun.find_rate + electricgun.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate) {// �������� �߰�
								character.showFoundArmor(police_armor);
								character.doArmor(police_armor);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate) {// �������� �߰�
								character.showFoundArmor(police_shield);
								character.doArmor(police_shield);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate) {// ���� �Ƿ��� �߰�
								character.showFoundBag(police_bag);
								character.doBag(police_bag);
							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate + bike.find_rate) {// ��Ż����ũ
																												// �߰�
								character.showFoundVehicle(bike);
								character.doVehicle(bike);
							} else {// ���� �߰�
								Zombie menzombie2 = new Zombie(20, 3, 1, "��������", 10, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
								Zombie womenzombie2 = new Zombie(15, 2, 1, "��������", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "��������", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "��������", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// �뵵�� ���� ���� Ȯ��
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("������ �߻��Ͽ����ϴ�.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// �������� ����
									Character.exp += character.startFight(animalzombie2);
								} else {// �������� ����
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 0:// ������ ���� �߽ɰ��� �̵�
						System.out.println("������ ���� �߽ɰ��� �ǵ��ư��ϴ�.");
						break;
					default:
						System.out.println("���ڸ� �ٽ� �Է����ּ���.");
					}
				} while (num_2 != 0);
				break;

			case 2:// ����ǰ Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// ���Ȯ��
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// ���� ��ҷ� �̵�
				System.out.println("�ð��� �����ϴ�. ���� ���� ��ҷ� �̵��մϴ�.");
				break;
			
			case 9://����� (on/off)
				try {
					if (play_now==true) {//����
						play_now=false;
						bgmmain.suspend();
						System.out.println("������� Off");
					} else {//�ѱ�
						bgmmain.resume();
						System.out.println("������� On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// ����
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("���ڸ� �ٽ� �Է����ּ���.");
			}
		} while (num_1 != 4);
		now_map++;
		// �ʵ�� �̵�
		do {
			is_city=false;
			Zombie dirtyzombie1 = new Zombie(15, 2, 1, "����������", 20, 15, 2, null, dirty_jacket, null);
			Zombie stunzombie1 = new Zombie(20, 3, 2, "��������", 8, 20, 5, null, null, null);
			Zombie bombzombie1 = new Zombie(20, 2, 1, "��������", 13, 20, 3, null, null, null);
			Zombie stenchzombie1 = new Zombie(30, 2, 3, "��������", 8, 30, 13, null, null, null);
			Zombie mutationzombie1 = new Zombie(15, 10, 1, "��������", 6, 15, 15, null, null, null);
			Zombie giantzombie1 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30, null, null, null);
			int uptown_monster_appearance_rate1 
			= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// �ܰ� ����
																											// ���� Ȯ��
					+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
					+ giantzombie1.appearance_rate;
			System.out.println("���ø� �����.");
			if (random.nextInt(100) < uptown_monster_appearance_rate1) {// ����Ȯ��
				System.out.println("������ �߻��Ͽ����ϴ�.");
				time.timeStop();
				weather.weatherStop();
				int num = random.nextInt(uptown_monster_appearance_rate1);
				if (num < dirtyzombie1.appearance_rate) {// ���������� ����
					Character.exp += character.startFight(dirtyzombie1);
				} else if (num >= dirtyzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// �������� ����
					Character.exp += character.startFight(stunzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate) {// �������� ����
					Character.exp += character.startFight(bombzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// �������� ����
					Character.exp += character.startFight(stenchzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
								+ mutationzombie1.appearance_rate) {// �������� ����
					Character.exp += character.startFight(mutationzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
								+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// �Ŵ����� ����
					Character.exp += character.startFight(giantzombie1);
				} else {// �������� ����
					Character.exp += character.startFight(lordzombie);
				}
				time.timeStart();
				weather.weatherStart();
			} else {
				System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
			}
			character.fightEndCheck();

			// ���ÿ��� ���������µ� ����
			// ���ú��� �����ϰ� �پ��� ���͵��� ����
			do {
				System.out.println(
						"===================================================================================================");
				System.out.println("���� ��ġ:���Ǽҷ� ���� ��");
				System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
				System.out.println(
						"������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
				System.out.println("ȸ����:" + Character.avoid_rate);
				System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/"
						+ Character.weight_limit + ")");
				System.out.println(
						"���� �ð�:" + Character.left_time + "               �ൿ��:0+" + Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("�ൿ�� �Է��Ͻÿ�.");
				System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
				Scanner scan1 = new Scanner(System.in);
				num_1 = scan1.nextInt();
				//������ ��� ���ߴ� ���� ���� �����ð����� �����Ϸ� ����
				if(play_nowz==false) {//���� �����ϰԵǸ� ���ߴµ�
					scan1=null;
					do {
						sleep(500);//1�ʸ��� ���� �������� üũ�Ѵ�.
					}while(play_nowz!=true);
					stop=true;
					System.out.println(
							"===================================================================================================");
					System.out.println("���� ��ġ:���Ǽҷ� ���� ��");
					System.out.println("����:" + Character.lv + "                    ����ġ:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    ���ݷ�:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    ġ��Ÿ��:" + Character.critical_rate);
					System.out.println("������:" + Character.satiety + "                ����:0+" + Character.armor.guard_point);
					System.out.println("ȸ����:" + Character.avoid_rate);
					System.out.println("���� �̵��Ÿ�:" + now_map + "              ����:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("���� �ð�:" + Character.left_time + "               �ൿ��:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("�ൿ�� �Է��Ͻÿ�.");
					System.out.println("1.�ֺ� Ž��      2.����ǰ Ȯ��      3.��� Ȯ��      4.���� ��ҷ� �̵�      9.�������      0.����");
					Scanner scan1_1 = new Scanner(System.in);
					num_1 = scan1_1.nextInt();
					stop=false;
				}
				switch (num_1) {
				case 1:// �ֺ� Ž��
					Zombie dirtyzombie2 = new Zombie(15, 2, 1, "����������", 20, 15, 2, null, dirty_jacket, null);
					Zombie stunzombie2 = new Zombie(20, 3, 2, "��������", 8, 20, 5, null, null, null);
					Zombie bombzombie2 = new Zombie(20, 2, 1, "��������", 13, 20, 3, null, null, null);
					Zombie stenchzombie2 = new Zombie(30, 2, 3, "��������", 8, 30, 13, null, null, null);
					Zombie mutationzombie2 = new Zombie(15, 10, 1, "��������", 6, 15, 15, null, null, null);
					Zombie giantzombie2 = new Zombie(150, 1, 1, "�Ŵ�����", 3, 150, 30, null, null, null);
					int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// �ܰ�
																													// ����
																													// ����
																													// Ȯ��
							+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
							+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate;
					if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// ����Ȯ��
						System.out.println("������ �߻��Ͽ����ϴ�.");
						time.timeStop();
						weather.weatherStop();
						int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
						if (num < dirtyzombie2.appearance_rate) {// ���������� ����
							Character.exp += character.startFight(dirtyzombie2);
						} else if (num >= dirtyzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// �������� ����
							Character.exp += character.startFight(stunzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate) {// �������� ����
							Character.exp += character.startFight(bombzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// �������� ����
							Character.exp += character.startFight(stenchzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate) {// �������� ����
							Character.exp += character.startFight(mutationzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// �Ŵ�����
																											// ����
							Character.exp += character.startFight(giantzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate + 10
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// ���߰�
							System.out.println("���� �߰��Ͽ����ϴ�.");
							if (Character.weight_limit > Character.weight + bread.weight) {
								bread.count++;
								Character.weight += bread.weight;
								System.out.println("���� ȹ���Ͽ����ϴ�.");
							} else
								System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate + 20
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// �ش�
																												// �߰�
							System.out.println("�ش븦 �߰��Ͽ����ϴ�.");
							if (Character.weight_limit > Character.weight + band.weight) {
								band.count++;
								Character.weight += band.weight;
								System.out.println("�ش븦 ȹ���Ͽ����ϴ�.");
							} else
								System.out.println("���� �ѵ��� �ʰ��Ͽ����ϴ�.");
						} else {// �������� ����
							Character.exp += character.startFight(lordzombie);
						}
						time.timeStart();
						weather.weatherStart();
					} else {
						System.out.println("�ƹ��ϵ� �߻����� �ʾҽ��ϴ�.");
					}
					character.fightEndCheck();
					break;
				case 2:// ����ǰ Ȯ��
					time.timeStop();
					weather.weatherStop();
					character.showInventory();
					time.timeStart();
					weather.weatherStart();
					break;

				case 3:// ���Ȯ��
					time.timeStop();
					weather.weatherStop();
					character.showEquipedItem();
					time.timeStart();
					weather.weatherStart();
					break;

				case 4:// ���� ��ҷ� �̵�
					System.out.println("���� ��ҷ� �̵��մϴ�.");
					break;
				
				case 9://����� (on/off)
					try {
						if (play_now==true) {//����
							play_now=false;
							bgmmain.suspend();
							System.out.println("������� Off");
						} else {//�ѱ�
							bgmmain.resume();
							System.out.println("������� On");
							play_now=true;
						}
					} catch (Exception e) {
						System.out.print("Error : ");
						System.out.println(e);
					}
					break;
					
				case 0:// ����
					time.timeStop();
					weather.weatherStop();
					character.help();
					time.timeStart();
					weather.weatherStart();
					break;

				default:
					System.out.println("���ڸ� �ٽ� �Է����ּ���.");
				}
			} while (num_1 != 4);
			now_map++;
		} while (now_map < 100);// 99���� �ݺ� 100�Ǹ�
		
		time.timeStop();
		weather.weatherStop();
		//��������� ���� ���
		System.out.println("���ǼҰ� ���񶼿� �ѷ��ο��ִ�.���Ǽҿ� ���� ���ؼ� ���񶼸� ���� �޷����.");
		character.startFight(crowdzombie);
		//���üũ
		character.checkCharacterDead();
		if (character.dead == true) {
			System.out.println("����Ͽ����ϴ�.");
			return;
		}else {//���� ������
			System.out.println("���񶼸� ����ġ�� �����غ��̴� �Ѹ����� ���� ���� ���Ƽ���. �̳� ������ ���Ǽ� ���η� �� �� �ִ�.");
			character.bossFight(lastzombie);
			if (character.dead == true) {
				System.out.println("����Ͽ����ϴ�.");
				return;
			}
			System.out.println("���Ǽҿ� ������ �����Ͽ����ϴ�.");
			System.out.println("--------The End-------");
			//������ ���� ������ ���� ����Ű�� �Է��ؼ� ������ Ÿ�� �̴ϰ���
			System.out.println("������ ȥ�����¿� �������ϴ�. ������ Ÿ���� ���ϼ���");
			System.out.println("ȭ�鿡 ������ ��縦 �Է��ϸ� ������ Ÿ���� ���� �� �ֽ��ϴ�.");
			BossLastAttack attack = new BossLastAttack();
			attack.start();

		}
		}catch(InterruptedException e){
			System.out.println("��� ���߰ٽ��ϴ�~~~~");
			try {
				zbat.join();//���ͷ�Ʈ ���� ��� ��ٸ��� �輼��~
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.out.println("���ξ����� �����~");
			}
		
	}
}
}

