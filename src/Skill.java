
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
	
	public void checkCanUseSkill(Character character) {//각종 상황을 판단해서 스킬사용가능여부를 참으로 만들어놓음 
		if (Character.mp>=mp&&cooltime<=0) {
			canuse=true;
		}else {
			canuse=false;
		}
	}
	
	public void useSkill(Character character) {
		cooltime=cooltime_;//쿨타임 돌리고
		Character.mp-=mp;//마나사용
		canuse=false;//사용 불가처리
	}
	
}
