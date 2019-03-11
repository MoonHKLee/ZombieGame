
public class StopThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		
		Thread thr1 = new Thread(new BgmMain());
		thr1.interrupt();

		
		if(Thread.currentThread().isInterrupted()) {
			System.out.println("¹Ùº¸");
			
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("¤»");
		}
		
	}
	
}
