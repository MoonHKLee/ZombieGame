
public class Skill {
	int mp;
	int cooltime_;
	int cooltime;
	boolean canuse;
	String name;
	
	public Skill(int mp, int cooltime_,int cooltime, boolean canuse,String name) {
		this.mp=mp;
		this.cooltime_=cooltime_;
		this.cooltime=cooltime;
		this.canuse=canuse;
		this.name=name;
	}
	
	public void checkCanUseSkill(Character character) {//���� ��Ȳ�� �Ǵ��ؼ� ��ų��밡�ɿ��θ� ������ �������� 
		if (Character.mp>=mp&&cooltime<=0) {
			canuse=true;
		}else {
			canuse=false;
		}
	}
	
	public void useSkill(Character character) {
		cooltime=cooltime_;//��Ÿ�� ������
		Character.mp-=mp;//�������
		canuse=false;//��� �Ұ�ó��
	}
	
}
