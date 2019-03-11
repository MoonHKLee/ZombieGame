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
		do {// 무한루프
			Random random = new Random();
		int weather = random.nextInt(2);
			if (weather == 0) {// 0이면 비
				if(Weather.rain==false) {
					System.out.println("비가 내립니다. 움직임이 느려집니다. 남은시간이 더 빠르게 감소합니다.");
					System.out.println("체온이 떨어집니다. hp가 지속적으로 감소합니다.");
					rain2.resume();
					
					int count=0;
					while(count<2) {//5분에 한번 날씨변경
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
					//아무일도 안일어남
					int count=0;
					while(count<2) {//5분에 한번 날씨변경
						try {
							sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
					}
					
				}
			} else {// 1이면 맑음
				if(Weather.rain==true) {
					System.out.println("비가 그칩니다. 모든 상태가 정상으로 되돌아옵니다.");
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
					//아무일도 안일어남
					rain2.suspend();
					int count=0;
					while(count<1) {//5분에 한번 날씨변경
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
					sleep(500);// 0.5초에 한번씩 스탑인지 진행인지 확인. 스탑이 들어오면 멈춤
				} while (Weather.stop != false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}
}
