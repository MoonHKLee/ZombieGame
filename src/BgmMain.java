import java.io.*;
import java.util.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
public class BgmMain extends Thread {

	public void run() {
		while (true) {
			String path = "C:\\Users\\mf311\\OneDrive\\Desktop\\Music\\main_music.mp3";
			try {
				Player p = new Player(new FileInputStream(path));
				p.play();
			} catch (Exception e) {
				System.out.print("Error : ");
				System.out.println(e);
			}
		}
	}
	
}
