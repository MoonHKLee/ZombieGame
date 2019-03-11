
public class Infection extends Thread {
	static boolean safe=false;
	
	public void run() {
		System.out.println("공기중의 좀비바이러스에 감염되었습니다. '병원'에서 200초 내에 '미완성 백신'을 찾지 못하면 좀비로 변하여 게임이 끝납니다.");
		Timer timer = new Timer(200);
		timer.start();//200초 카운트 시작
		do {//1초에 한번씩 백신 획득했는지 확인
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (safe == true) {// 성공했으면
				timer.success = true;// 성공
			}	
		} while (safe != true);
		timer.end=true;
	}
}
