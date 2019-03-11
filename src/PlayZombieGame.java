import java.util.ArrayList;
import java.util.Scanner;

public class PlayZombieGame extends Thread{
	public static void main(String[] args) {
		PlayZombieGame2 player1 = new PlayZombieGame2();
		player1.start();
	}
}