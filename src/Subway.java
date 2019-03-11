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
		System.out.print("다음 도시로 이동중입니다.");
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
		
		//로딩끝나고 지하철 미니게임 시작
		System.out.println("==================================NOTICE================================");
		System.out.println("무인지하철에 오류가 생겨 작동을  멈췄습니다. 정지한 열차로 좀비들이 달려들고 있습니다. 다시 시동을 걸어야 합니다.");
		System.out.println("나오는 문제들의 정답을 1분내에 5번 맞추면 열차의 시동이 걸리며 다시 출발합니다.");
		System.out.println("시동을 거는데 실패하면 캐릭터는 사망합니다. 5초후에 게임이 시작됩니다.");
		System.out.println("========================================================================");
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
		
		//1번문제
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("1. "+random1+" + "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1+random2)) {//정답이 틀렸으면
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
			System.exit(0);
		}else {
			System.out.println("정답!");
			count++;
		}
		
		//2번문제
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("2. "+random1+" + "+random2+" + "+random3+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1+random2+random3)) {//정답이 틀렸으면
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
			System.exit(0);
		}else {
			System.out.println("정답!");
			count++;
		}
		
		//3번문제
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("3. "+random1+" - "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1-random2)) {//정답이 틀렸으면
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
			System.exit(0);
		}else {
			System.out.println("정답!");
			count++;
		}
		
		//4번문제
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		random3=random.nextInt(100);
		System.out.println("4. "+random1+" - "+random2+" - "+random3+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1-random2-random3)) {//정답이 틀렸으면
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
			System.exit(0);
		}else {
			System.out.println("정답!");
			count++;
		}
		
		//5번문제
		random1=random.nextInt(100);
		random2=random.nextInt(100);
		System.out.println("5. "+random1+" X "+random2+"= ?");
		answer=scan.nextInt();
		if(answer!=(random1*random2)) {//정답이 틀렸으면
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
			System.exit(0);
		}else {
			System.out.println("정답!");
			count++;
		}
		
		
		
		
		if(count==5) {//성공했으면
			timer.success=true;//성공
			System.out.println("다시 열차를 작동시키는데 성공하였습니다. 다음 도시로 이동합니다.");
		}else {
			System.out.println("열차를 다시 작동시키는데 실패하였습니다. 좀비떼에 둘러싸여 사망합니다.");
		}
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.end=true;
		System.out.println("지하철에서 내립니다.");
		game_stop=false;
		
	}
}
