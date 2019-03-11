
public class Timer extends Thread {
	int count;
	boolean end;
	boolean success;

	public Timer(int count) {
		this.count = count;
		this.end=false;
		success=false;
	}

	public void run() {
		do {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count--;
			if(count%5==0)
				System.out.println("남은시간: "+count+" 초");
		} while (count != 0&&end!=true);
		if(success==false) {
			System.out.println("사망하였습니다.");
			System.exit(0);
		}
		
	}
}
