import java.util.*;
import java.io.*;
import java.util.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
public class Weather extends Thread {
	static boolean stop;
	static boolean rain;
	Character character;
	RainBgm rain2 = new RainBgm();

	public Weather(Boolean stop,Character character) {
		Weather.stop=false;
		Weather.rain=false;
		this.character=character;
	}

	public void weatherStop() {
		Weather.stop = true;
	}

	public void weatherStart() {
		Weather.stop = false;
	}

	public void run() {
		rain2.start();
		do {// ���ѷ���
			Random random = new Random();
		int weather = random.nextInt(2);
			if (weather == 0) {// 0�̸� ��
				if(Weather.rain==false) {
					System.out.println("�� �����ϴ�. �������� �������ϴ�. �����ð��� �� ������ �����մϴ�.");
					System.out.println("ü���� �������ϴ�. hp�� ���������� �����մϴ�.");
					rain2.resume();
					
					int count=0;
					while(count<2) {//5�п� �ѹ� ��������
						try {
							sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
					}
					Weather.rain=true;
				}
				else {
					//�ƹ��ϵ� ���Ͼ
					int count=0;
					while(count<2) {//5�п� �ѹ� ��������
						try {
							sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
					}
					
				}
			} else {// 1�̸� ����
				if(Weather.rain==true) {
					System.out.println("�� ��Ĩ�ϴ�. ��� ���°� �������� �ǵ��ƿɴϴ�.");
					rain2.suspend();
					int count=0;
					while(count<1) {
						try {
							sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
					}
					Weather.rain=false;
				}else {
					//�ƹ��ϵ� ���Ͼ
					rain2.suspend();
					int count=0;
					while(count<1) {//5�п� �ѹ� ��������
						try {
							sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
					}
				}
			}
			try {
				do {
					sleep(500);// 0.5�ʿ� �ѹ��� ��ž���� �������� Ȯ��. ��ž�� ������ ����
				} while (Weather.stop != false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}
}
