import java.util.*;
public class BossLastAttack extends Thread {
	char up;
	char down;
	char left;
	char right;
	
	public void run() {
		Timer timer = new Timer(60);//60�� �ð����ѵ��� ȭ��ǥ������ ��Ȯ�� �Է��Ͽ� ������ Ÿ���� ���ϼ���
		Scanner scan = new Scanner(System.in);
		
		String str="";
		//���ӽ���
		int count=5;
		try {//�д°� ���
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//ī��Ʈ�ٿ� 5
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//�д°� ���
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//ī��Ʈ�ٿ� 4
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//�д°� ���
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//ī��Ʈ�ٿ� 3
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//�д°� ���
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//ī��Ʈ�ٿ� 2
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//�д°� ���
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//ī��Ʈ�ٿ� 1
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Game Start!");
		timer.start();
		System.out.println("�̰����� ������ �����ϼ̽��ϴ�.");
		String str1=scan.next();
		
		System.out.println(str1);
		if(str1!="�̰����� ������ �����ϼ̽��ϴ�.") {
			System.out.println("������ ����ġ�µ� �����Ͽ����ϴ�1");

		}
		
		System.out.println("��������");
		str=scan.next();
		if(str!="dawsdwd") {
			System.out.println("������ ����ġ�µ� �����Ͽ����ϴ�2");
			System.exit(0);
		}
		
		System.out.println("�����������");
		str=scan.next();
		if(str!="dawsdwwsdd") {
			System.out.println("������ ����ġ�µ� �����Ͽ����ϴ�3");
			System.exit(0);
		}
		
		System.out.println("��� ������� ����ġ�� ������ ���س� ���. ����� ���� ������ �����˰� �ƴ� \"����� ������\"�Դϴ�.");
		System.out.println("---------------------The end--------------------");
		
	}
}
