import java.util.*;
public class Subway extends Thread {
	boolean game_stop;
	int count=0;
	
	public Subway(boolean game_stop) {
		this.game_stop=game_stop;
	}
	
	
	public boolean returnStop() {
		return game_stop;
	}
	public void run() {
		System.out.print("���� ���÷� �̵����Դϴ�.");
		Scanner scan=new Scanner(System.in);
		Random random=new Random();
		Timer timer = new Timer(60);
		int random1;
		int random2;
		int random3;
		int answer;
		SubwayMusic music = new SubwayMusic();
		music.start();
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(".");
		
		//�ε������� ����ö �̴ϰ��� ����
		System.out.println("==================================NOTICE================================");
		System.out.println("��������ö�� ������ ���� �۵���  ������ϴ�. ������ ������ ������� �޷���� �ֽ��ϴ�. �ٽ� �õ��� �ɾ�� �մϴ�.");
		System.out.println("������ �������� ������ 1�г��� 5�� ���߸� ������ �õ��� �ɸ��� �ٽ� ����մϴ�.");
		System.out.println("�õ��� �Ŵµ� �����ϸ� ĳ���ʹ� ����մϴ�. 5���Ŀ� ������ ���۵˴ϴ�.");
		System.out.println("========================================================================");
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
		
		//1������
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("1. "+random1+" + "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1+random2)) {//������ Ʋ������
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
			System.exit(0);
		}else {
			System.out.println("����!");
			count++;
		}
		
		//2������
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("2. "+random1+" + "+random2+" + "+random3+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1+random2+random3)) {//������ Ʋ������
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
			System.exit(0);
		}else {
			System.out.println("����!");
			count++;
		}
		
		//3������
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("3. "+random1+" - "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1-random2)) {//������ Ʋ������
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
			System.exit(0);
		}else {
			System.out.println("����!");
			count++;
		}
		
		//4������
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("4. "+random1+" - "+random2+" - "+random3+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1-random2-random3)) {//������ Ʋ������
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
			System.exit(0);
		}else {
			System.out.println("����!");
			count++;
		}
		
		//5������
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		System.out.println("5. "+random1+" X "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1*random2)) {//������ Ʋ������
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
			System.exit(0);
		}else {
			System.out.println("����!");
			count++;
		}
		
		
		
		
		if(count==5) {//����������
			timer.success=true;//����
			System.out.println("�ٽ� ������ �۵���Ű�µ� �����Ͽ����ϴ�. ���� ���÷� �̵��մϴ�.");
		}else {
			System.out.println("������ �ٽ� �۵���Ű�µ� �����Ͽ����ϴ�. ���񶼿� �ѷ��ο� ����մϴ�.");
		}
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.end=true;
		System.out.println("����ö���� �����ϴ�.");
		game_stop=false;
		
	}
}
