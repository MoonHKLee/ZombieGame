import java.util.*;
public class Time extends Thread{
	static boolean stop;
	static boolean time_day;//���� ������ ������ ����, ������ ���� ���п� ���
	NightBgm night = new NightBgm();
	
	public Time(Boolean stop) {
		Time.stop=false;
		time_day=true;
	}
	
	public void timeStop() {
		Time.stop=true;
	}
	
	public void timeStart() {
		Time.stop=false;
	}
	
	public void run() {
		night.start();
		int time =0;
		do {//���ѷ���
			if (time == 0) {//0�̸� ��
				night.suspend();
				System.out.println("���� �Ǿ����ϴ�. ������ ü��, ���ݷ�, ����ġ, ����Ȯ���� �����մϴ�.");
				Time.time_day=true;
				Zombie.zombie_hp=0;//���㿡 ���� �����Ǵ� ü��
				Zombie.zombie_attack=0;//������ݷ�
				Zombie.zombie_exp=0;//�������
				Zombie.zombie_appearance_rate=0;//����߰�Ȯ��
				time++;//�㿡�� ��
			} else {//1�̸� ��
				night.resume();
				System.out.println("���� �Ǿ����ϴ�. ������ ü��, ���ݷ�, ����ġ, ����Ȯ���� �����մϴ�.");
				Time.time_day=false;
				Zombie.zombie_hp=5;//���㿡 ���� �����Ǵ� ü��
				Zombie.zombie_attack=1;//������ݷ�
				Zombie.zombie_exp=2;//�������ġ
				Zombie.zombie_appearance_rate=1;//����߰�Ȯ��
				time--;//�㿡�� ��
			}
			try {
				sleep(15000);//5�п� �ѹ��� ���㺯��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				do {
					sleep(500);//0.5�ʿ� �ѹ��� ��ž���� �������� Ȯ��. ��ž�� ������ ����
				}while(Time.stop!=false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}
}
