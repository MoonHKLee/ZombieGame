import java.util.*;
public class HorseChange extends Thread {
	
	public void run() {
		Random random = new Random();
		while (true) {
			horse.horse1 += random.nextInt(3);
			horse.horse2 += random.nextInt(3);
			horse.horse3 += random.nextInt(3);
			horse.horse4 += random.nextInt(3);
			horse.horse5 += random.nextInt(3);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
