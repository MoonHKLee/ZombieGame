import java.util.*;
public class horse  extends Thread{
	static int horse1=0;
	static int horse2=0;
	static int horse3=0;
	static int horse4=0;
	static int horse5=0;
	
	public static void main(String[] args) {
		HorseChange s = new HorseChange();
		s.start();
		while (true) {
			System.out.println("===================================================");
			System.out.print("1.");
			for (int i = 0; i < horse1; i++) {
				System.out.print("*");
			}
			System.out.println("");
			System.out.print("2.");
			for (int i = 0; i < horse2; i++) {
				System.out.print("*");
			}
			System.out.println("");
			System.out.print("3.");
			for (int i = 0; i < horse3; i++) {
				System.out.print("*");
			}
			System.out.println("");
			System.out.print("4.");
			for (int i = 0; i < horse4; i++) {
				System.out.print("*");
			}
			System.out.println("");
			System.out.print("5.");
			for (int i = 0; i < horse5; i++) {
				System.out.print("*");
			}
			System.out.println("");
			System.out.println("===================================================");
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
