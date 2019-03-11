import java.util.*;

public class ZombieAttack extends Thread {
	Character character;
	boolean stop;
	Thread main;

	public ZombieAttack(Character character,Thread main) {
		this.character = character;
		this.stop = false; 
		this.main = main;
	}

	public void run() {
		Random random = new Random();
		
		while(true) {//게임 내내 무한반복, 좀비는 10초에 한번씩 등장할 수도 있고, 등장하지 않을수도 있다.
			try {//20초 대기
				sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			// 좀비에서 등장하는 관련된 스킬 및 아이템들.
			Scanner scan = new Scanner(System.in);
			Item ticket = new Item("지하철 티켓",0,20);
			Skill throw_rib=new Skill(10, 2, 0, true, "뼈던지기");
			Weapon bone = new Weapon("대퇴골",10,100,3,null);//치명타
			Weapon rib = new Weapon("갈비뼈",5,100,1,throw_rib);
			Armor dirty_jacket = new Armor("더러운조끼", 5, 100, 1,null);//회피율
			
			//좀비들
			Zombie menzombie = new Zombie(20, 3, 1, "남자좀비", 25, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
			Zombie womenzombie = new Zombie(15, 2, 1, "여자좀비", 25, 15, 2,rib,null,ticket);
			Zombie animalzombie = new Zombie(10, 3, 1, "동물좀비", 25, 10, 2,null,null,ticket);
			Zombie lordzombie = new Zombie(30, 3, 1, "대장좀비", 25, 30, 10,bone,null,ticket);
			int bigcity_monster_appearance_rate = menzombie.appearance_rate + womenzombie.appearance_rate// 대도시 좀비 등장 확률
					+ animalzombie.appearance_rate + lordzombie.appearance_rate;
			//////////////////////////////////////////////////////////////////////// 위 도시좀비
			//////////////////////////////////////////////////////////////////////// 아래 필드좀비
			Zombie dirtyzombie = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
			Zombie stunzombie = new Zombie(20, 3, 2, "괴성좀비", 15, 20, 5,null,null,null);
			Zombie bombzombie = new Zombie(20, 2, 1, "폭발좀비", 20, 20, 3,null,null,null);
			Zombie stenchzombie = new Zombie(30, 2, 3, "악취좀비", 15, 30, 13,null,null,null);
			Zombie mutationzombie = new Zombie(15, 10, 1, "변이좀비", 15, 15, 15,null,null,null);
			Zombie giantzombie = new Zombie(150, 1, 1, "거대좀비", 15, 150, 30,null,null,null);
			
			//보스좀비들
			Zombie crowdzombie = new Zombie(300, 5, 1, "좀비무리", 2, 300, 30,null,null,null);
			Zombie lastzombie = new Zombie(150, 8, 2, "최종좀비", 2, 150, 1000000,null,null,null);
			int uptown_monster_appearance_rate = dirtyzombie.appearance_rate + stunzombie.appearance_rate// 외곽 좀비 등장 확률
					+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
					+ giantzombie.appearance_rate;
			
			//System.out.println("id of the thread isz " + main.getId());   
			//도시일때 만나는 좀비
			if(PlayZombieGame2.is_city==true&&Time.stop==false&&PlayZombieGame2.stop==false) {//현재 캐릭터가 도시에있고, 시간이 진행되고있다면
				//메인스레드 일시정지
				PlayZombieGame2.play_nowz=false;
				
				if (random.nextInt(100) < (bigcity_monster_appearance_rate)) {// 전투확률
					Time.stop=true;
					Weather.stop=true;
					System.out.println("!!!!!!!!!!좀비가 나를 발견하였습니다. 전투가 발생합니다.!!!!!!!!!!!");
					System.out.println("'1'을 두번 입력하면 좀비와의 전투가 진행됩니다.");
					int num2=scan.nextInt();
					int num = random.nextInt(bigcity_monster_appearance_rate);
					if (num < menzombie.appearance_rate) {// 남자좀비 등장
						Character.exp += character.startFight(menzombie);
					} else if (num >= menzombie.appearance_rate
							&& num < menzombie.appearance_rate + womenzombie.appearance_rate) {// 여자좀비 등장
						Character.exp += character.startFight(womenzombie);
					} else if (num >= menzombie.appearance_rate + womenzombie.appearance_rate
							&& num < menzombie.appearance_rate + womenzombie.appearance_rate
									+ animalzombie.appearance_rate) {// 동물좀비 등장
						Character.exp += character.startFight(animalzombie);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie);
					}
					PlayZombieGame2.play_nowz=true;
					Time.stop=false;
					Weather.stop=false;
				} else {
					//전투가 일어나지 않아 아무일도 발생하지 않음.
				}
				character.fightEndCheck();//전투 종료시 각종 데이터 값 변경(사망여부/경험치/체력/포만감/남은시간)
				
				//메인스레드 다시 재생
			}
			// 외곽일때
			if (PlayZombieGame2.is_city == false && Time.stop == false&&PlayZombieGame2.stop==false) {
				//메인스레드 일시정지
				PlayZombieGame2.play_nowz=false;
				
				if (random.nextInt(100) < uptown_monster_appearance_rate) {// 전투확률
					System.out.println("!!!!!!!!!!좀비가 나를 발견하였습니다. 전투가 발생합니다.!!!!!!!!!!!");
					System.out.println("'1'을 두번 입력하면 좀비와의 전투가 진행됩니다.");
					int num2=scan.nextInt();
					Time.stop = true;
					Weather.stop = true;
					int num = random.nextInt(uptown_monster_appearance_rate);
					if (num < dirtyzombie.appearance_rate) {// 더러운좀비 등장
						Character.exp += character.startFight(dirtyzombie);
					} else if (num >= dirtyzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate) {// 괴성좀비 등장
						Character.exp += character.startFight(stunzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate) {// 폭발좀비 등장
						Character.exp += character.startFight(bombzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate) {// 악취좀비 등장
						Character.exp += character.startFight(stenchzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate + stenchzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate
									+ mutationzombie.appearance_rate) {// 변이좀비 등장
						Character.exp += character.startFight(mutationzombie);
					} else if (num >= dirtyzombie.appearance_rate + stunzombie.appearance_rate
							+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
							&& num < dirtyzombie.appearance_rate + stunzombie.appearance_rate
									+ bombzombie.appearance_rate + stenchzombie.appearance_rate
									+ mutationzombie.appearance_rate + giantzombie.appearance_rate) {// 거대좀비 등장
						Character.exp += character.startFight(giantzombie);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie);
					}
					PlayZombieGame2.play_nowz=true;
					Time.stop = false;
					Weather.stop = false;
				} else {
					// 아무 일도 일어나지않음.
				}
			}
			character.fightEndCheck();
		}
	}
}
