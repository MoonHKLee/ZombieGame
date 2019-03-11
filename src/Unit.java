
public class Unit extends Thread {
	int hp;//««≈Î
	int attack_point;
	int guard_point;
	boolean dead;
	
	public Unit(int hp,int attack_point,int guard_point) {
		this.hp=hp;
		this.attack_point=attack_point;
		this.guard_point=guard_point;
		this.dead=false;
	}
	
	public void checkDead() {
		if(this.hp<=0)
			dead=true;
	}
	
}
