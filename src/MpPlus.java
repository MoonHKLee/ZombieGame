
public class MpPlus extends Thread {

	public void run() {//10�ʿ� �ѹ��� ���º���
		while(true)
		{	
			if(Time.time_day==false)//���̸� ���� 1��
				Character.mp++;
			if(Weather.rain==true) {//����� �ð������ٰ� ü�´���
				Character.left_time-=2;
				Character.hp--;
			}else {//������ �ð�������
				Character.left_time--;
			}
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
