import java.util.*;
public class Time extends Thread{
	static boolean stop;
	static boolean time_day;//현재 낮인지 밤인지 구분, 좀비의 선빵 구분에 사용
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
		do {//무한루프
			if (time == 0) {//0이면 낮
				night.suspend();
				System.out.println("낮이 되었습니다. 좀비의 체력, 공격력, 경험치, 출현확률이 감소합니다.");
				Time.time_day=true;
				Zombie.zombie_hp=0;//낮밤에 따라 증감되는 체력
				Zombie.zombie_attack=0;//낮밤공격력
				Zombie.zombie_exp=0;//낮밤방어력
				Zombie.zombie_appearance_rate=0;//낮밤발견확률
				time++;//담에는 밤
			} else {//1이면 밤
				night.resume();
				System.out.println("밤이 되었습니다. 좀비의 체력, 공격력, 경험치, 출현확률이 증가합니다.");
				Time.time_day=false;
				Zombie.zombie_hp=5;//낮밤에 따라 증감되는 체력
				Zombie.zombie_attack=1;//낮밤공격력
				Zombie.zombie_exp=2;//낮밤경험치
				Zombie.zombie_appearance_rate=1;//낮밤발견확률
				time--;//담에는 낮
			}
			try {
				sleep(15000);//5분에 한번씩 낮밤변경
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				do {
					sleep(500);//0.5초에 한번씩 스탑인지 진행인지 확인. 스탑이 들어오면 멈춤
				}while(Time.stop!=false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}
}
