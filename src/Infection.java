
public class Infection extends Thread {
	static boolean safe=false;
	
	public void run() {
		System.out.println("�������� ������̷����� �����Ǿ����ϴ�. '����'���� 200�� ���� '�̿ϼ� ���'�� ã�� ���ϸ� ����� ���Ͽ� ������ �����ϴ�.");
		Timer timer = new Timer(200);
		timer.start();//200�� ī��Ʈ ����
		do {//1�ʿ� �ѹ��� ��� ȹ���ߴ��� Ȯ��
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (safe == true) {// ����������
				timer.success = true;// ����
			}	
		} while (safe != true);
		timer.end=true;
	}
}
