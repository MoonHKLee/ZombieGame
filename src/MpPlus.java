
public class MpPlus extends Thread {

	public void run() {//10초에 한번씩 상태변경
		while(true)
		{	
			if(Time.time_day==false)//낮이면 마나 1참
				Character.mp++;
			if(Weather.rain==true) {//비오면 시간빨리줄고 체력닳음
				Character.left_time-=2;
				Character.hp--;
			}else {//맑으면 시간만닳음
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
