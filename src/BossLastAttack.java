import java.util.*;
public class BossLastAttack extends Thread {
	char up;
	char down;
	char left;
	char right;
	
	public void run() {
		Timer timer = new Timer(60);//60초 시간제한동안 화살표방향을 정확히 입력하여 마무리 타격을 가하세요
		Scanner scan = new Scanner(System.in);
		
		String str="";
		//게임시작
		int count=5;
		try {//읽는것 대기
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//카운트다운 5
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//읽는것 대기
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//카운트다운 4
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//읽는것 대기
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//카운트다운 3
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//읽는것 대기
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//카운트다운 2
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {//읽는것 대기
			System.out.println(count);
			sleep(1000);
			count--;
		} catch (InterruptedException e) {//카운트다운 1
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Game Start!");
		timer.start();
		System.out.println("이곳까지 오느라 수고하셨습니다.");
		String str1=scan.next();
		
		System.out.println(str1);
		if(str1!="이곳까지 오느라 수고하셨습니다.") {
			System.out.println("보스를 물리치는데 실패하였습니다1");

		}
		
		System.out.println("→←↑↓→↑→");
		str=scan.next();
		if(str!="dawsdwd") {
			System.out.println("보스를 물리치는데 실패하였습니다2");
			System.exit(0);
		}
		
		System.out.println("→←↑↓→↑↑↓→→");
		str=scan.next();
		if(str!="dawsdwwsdd") {
			System.out.println("보스를 물리치는데 실패하였습니다3");
			System.exit(0);
		}
		
		System.out.println("모든 좀비들을 물리치고 가족을 구해낸 당신. 당신은 이제 범죄자 김유죄가 아닌 \"좀비왕 김좀비\"입니다.");
		System.out.println("---------------------The end--------------------");
		
	}
}
