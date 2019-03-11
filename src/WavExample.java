import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class WavExample extends JFrame implements ActionListener, LineListener {
	/* Swing 디자인 부분 */
	private JPanel pan = new JPanel();
	private JButton btnStart = new JButton("시작");
	private JButton btnEnd = new JButton("종료");
	
	/* 음악 재생을 돕는 녀석 */
	private Clip clip;
	
	/* 스탑버튼 클릭여부 */
	private boolean isStop = false;
	
	/* wav 파일 지정*/
	private File wavFile = new File("C:\\\\Users\\\\mf311\\\\OneDrive\\\\Desktop\\\\Music\\\\main_music.wav");

	/* wav 파일을 Stream으로 만들어 주는 녀석*/
	private AudioInputStream ais;
	
	public WavExample() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(100, 100, 200, 80);
		this.setTitle("WavExample");

		/* Swing 디자인 부분 */
		pan.add(btnStart);
		pan.add(btnEnd);
		btnStart.addActionListener(this);
		btnEnd.addActionListener(this);
		this.add(pan);
		
		try {
			/* wav파일 stream으로 로딩 */
			ais = AudioSystem.getAudioInputStream(wavFile);
			/* clip 가져옴 */
			clip = AudioSystem.getClip();
			
			/* AudioInputStream 로딩 */
			clip.open(ais);
			
			/* 무한 반복을 위해 LineEvent 등록 > update 호출함*/
			clip.addLineListener(this);
		} catch (Exception e) {
		}
		
		this.setVisible(true);
	}

	@Override
	public void update(LineEvent le) {
		/* Stop 버튼을 누르지 않았을 경우, 그리고 STOP 되었을 경우 */
		if(!isStop && le.getType() == LineEvent.Type.STOP ) {
			try {
				/* wav파일 시작위치 맨 앞으로 이동 */
				clip.setMicrosecondPosition(0);
				/* wav 시작 */
				clip.start();
			} catch(Exception e) {
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// start 버튼을 늘렸을 경우
		if(ae.getSource() == btnStart) {
			isStop = false;
			clip.start();
		} 
		// 정지버튼을 눌렸을 경우
		else if(ae.getSource() == btnEnd) {
			isStop = true;
			clip.setMicrosecondPosition(0);
			clip.stop();
		}
	}
}