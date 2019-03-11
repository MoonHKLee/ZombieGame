import java.util.*;

public class ZombieAttack extends Thread {
	Character character;
	boolean stop;
	Thread main;

	public ZombieAttack(Character character,Thread main) {
		this.character = character;
		this.stop = false; 
		this.main = main;
	}

	public void run() {
		Random random = new Random();
		
		while(true) {//���� ���� ���ѹݺ�, ����� 10�ʿ� �ѹ��� ������ ���� �ְ�, �������� �������� �ִ�.
			try {//20�� ���
				sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			// ���񿡼� �����ϴ� ���õ� ��ų �� �����۵�.
			Scanner scan = new Scanner(System.in);
			Item ticket = new Item("����ö Ƽ��",0,20);
			Skill throw_rib=new Skill(10, 2, 0, true, "��������");
			Weapon bone = new Weapon("�����",10,100,3,null);//ġ��Ÿ
			Weapon rib = new Weapon("�����",5,100,1,throw_rib);
			Armor dirty_jacket = new Armor("����������", 5, 100, 1,null);//ȸ����
			
			//�����
			Zombie menzombie = new Zombie(20, 3, 1, "��������", 25, 20, 4,null,null,ticket);// ü�� ���ݷ� ���� �̸� Ȯ�� ����ü�� ����ġ ���� ��
			Zombie womenzombie = new Zombie(15, 2, 1, "��������", 25, 15, 2,rib,null,ticket);
			Zombie animalzombie = new Zombie(10, 3, 1, "��������", 25, 10, 2,null,null,ticket);
			Zombie lordzombie = new Zombie(30, 3, 1, "��������", 25, 30, 10,bone,null,ticket);
			int bigcity_monster_appearance_rate = menzombie.appearance_rate + womenzombie.appearance_rate// �뵵�� ���� ���� Ȯ��
					+ animalzombie.appearance_rate + lordzombie.appearance_rate;
			//////////////////////////////////////////////////////////////////////// �� ��������
			//////////////////////////////////////////////////////////////////////// �Ʒ� �ʵ�����
			Zombie dirtyzombie = new Zombie(15, 2, 1, "����������", 20, 15, 2,null,dirty_jacket,null);
			Zombie stunzombie = new Zombie(20, 3, 2, "��������", 15, 20, 5,null,null,null);
			Zombie bombzombie = new Zombie(20, 2, 1, "��������", 20, 20, 3,null,null,null);
			Zombie stenchzombie = new Zombie(30, 2, 3, "��������", 15, 30, 13,null,null,null);
			Zombie mutationzombie = new Zombie(15, 10, 1, "��������", 15, 15, 15,null,null,null);
			Zombie giantzombie = new Zombie(150, 1, 1, "�Ŵ�����", 15, 150, 30,null,null,null);
			
			//���������
			Zombie crowdzombie = new Zombie(300, 5, 1, "���񹫸�", 2, 300, 30,null,null,null);
			Zombie lastzombie = new Zombie(150, 8, 2, "��������", 2, 150, 1000000,null,null,null);
			int uptown_monster_appearance_rate = dirtyzombie.appearance_rate + stunzombie.appearance_rate// �ܰ� ���� ���� Ȯ��
					+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
					+ giantzombie.appearance_rate;
			
			//System.out.println("id of the thread isz " + main.getId());   
			//�����϶� ������ ����
			if(PlayZombieGame2.is_city==true&&Time.stop==false&&PlayZombieGame2.stop==false) {//���� ĳ���Ͱ� ���ÿ��ְ�, �ð��� ����ǰ��ִٸ�
				//���ν����� �Ͻ�����
				PlayZombieGame2.play_nowz=false;
				
				if (random.nextInt(100) < (bigcity_monster_appearance_rate)) {// ����Ȯ��
					Time.stop=true;
					Weather.stop=true;
					System.out.println("!!!!!!!!!!���� ���� �߰��Ͽ����ϴ�. ������ �߻��մϴ�.!!!!!!!!!!!");
					System.out.println("'1'�� �ι� �Է��ϸ� ������� ������ ����˴ϴ�.");
					int num2=scan.nextInt();
					int num = random.nextInt(bigcity_monster_appearance_rate);
					if (num < menzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(menzombie);
					} else if (num >= menzombie.appearance_rate
							&& num < menzombie.appearance_rate + womenzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(womenzombie);
					} else if (num >= menzombie.appearance_rate + womenzombie.appearance_rate
							&& num < menzombie.appearance_rate + womenzombie.appearance_rate
									+ animalzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(animalzombie);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie);
					}
					PlayZombieGame2.play_nowz=true;
					Time.stop=false;
					Weather.stop=false;
				} else {
					//������ �Ͼ�� �ʾ� �ƹ��ϵ� �߻����� ����.
				}
				character.fightEndCheck();//���� ����� ���� ������ �� ����(�������/����ġ/ü��/������/�����ð�)
				
				//���ν����� �ٽ� ���
			}
			// �ܰ��϶�
			if (PlayZombieGame2.is_city == false && Time.stop == false&&PlayZombieGame2.stop==false) {
				//���ν����� �Ͻ�����
				PlayZombieGame2.play_nowz=false;
				
				if (random.nextInt(100) < uptown_monster_appearance_rate) {// ����Ȯ��
					System.out.println("!!!!!!!!!!���� ���� �߰��Ͽ����ϴ�. ������ �߻��մϴ�.!!!!!!!!!!!");
					System.out.println("'1'�� �ι� �Է��ϸ� ������� ������ ����˴ϴ�.");
					int num2=scan.nextInt();
					Time.stop = true;
					Weather.stop = true;
					int num = random.nextInt(uptown_monster_appearance_rate);
					if (num < dirtyzombie.appearance_rate) {// ���������� ����
						Character.exp += character.startFight(dirtyzombie);
					} else if (num >= dirtyzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stunzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(bombzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(stenchzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate + stenchzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate
									+ mutationzombie.appearance_rate) {// �������� ����
						Character.exp += character.startFight(mutationzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate
									+ mutationzombie.appearance_rate + giantzombie.appearance_rate) {// �Ŵ����� ����
						Character.exp += character.startFight(giantzombie);
					} else {// �������� ����
						Character.exp += character.startFight(lordzombie);
					}
					PlayZombieGame2.play_nowz=true;
					Time.stop = false;
					Weather.stop = false;
				} else {
					// �ƹ� �ϵ� �Ͼ������.
				}
			}
			character.fightEndCheck();
		}
	}
}
