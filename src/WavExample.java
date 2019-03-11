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
	/* Swing ������ �κ� */
	private JPanel pan = new JPanel();
	private JButton btnStart = new JButton("����");
	private JButton btnEnd = new JButton("����");
	
	/* ���� ����� ���� �༮ */
	private Clip clip;
	
	/* ��ž��ư Ŭ������ */
	private boolean isStop = false;
	
	/* wav ���� ����*/
	private File wavFile = new File("C:\\\\Users\\\\mf311\\\\OneDrive\\\\Desktop\\\\Music\\\\main_music.wav");

	/* wav ������ Stream���� ����� �ִ� �༮*/
	private AudioInputStream ais;
	
	public WavExample() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(100, 100, 200, 80);
		this.setTitle("WavExample");

		/* Swing ������ �κ� */
		pan.add(btnStart);
		pan.add(btnEnd);
		btnStart.addActionListener(this);
		btnEnd.addActionListener(this);
		this.add(pan);
		
		try {
			/* wav���� stream���� �ε� */
			ais = AudioSystem.getAudioInputStream(wavFile);
			/* clip ������ */
			clip = AudioSystem.getClip();
			
			/* AudioInputStream �ε� */
			clip.open(ais);
			
			/* ���� �ݺ��� ���� LineEvent ��� > update ȣ����*/
			clip.addLineListener(this);
		} catch (Exception e) {
		}
		
		this.setVisible(true);
	}

	@Override
	public void update(LineEvent le) {
		/* Stop ��ư�� ������ �ʾ��� ���, �׸��� STOP �Ǿ��� ��� */
		if(!isStop && le.getType() == LineEvent.Type.STOP ) {
			try {
				/* wav���� ������ġ �� ������ �̵� */
				clip.setMicrosecondPosition(0);
				/* wav ���� */
				clip.start();
			} catch(Exception e) {
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// start ��ư�� �÷��� ���
		if(ae.getSource() == btnStart) {
			isStop = false;
			clip.start();
		} 
		// ������ư�� ������ ���
		else if(ae.getSource() == btnEnd) {
			isStop = true;
			clip.setMicrosecondPosition(0);
			clip.stop();
		}
	}
}