import java.io.*;
import javazoom.jl.player.*;

public class BladeBgm extends Thread {

	public void run() {
		String path = "C:\\Users\\mf311\\OneDrive\\Desktop\\Music\\blade.mp3";
		try {
			Player p = new Player(new FileInputStream(path));
			p.play();
		} catch (Exception e) {
			System.out.print("Error : ");
			System.out.println(e);
		}
	}

}
