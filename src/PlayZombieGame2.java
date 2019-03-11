import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
import javazoom.jl.player.Player;

public class PlayZombieGame2 extends Thread{
	static boolean is_city=true;
	static boolean play_now = true;
	static boolean stop=false;
	static boolean play_nowz = true;
	
	

	public void run() {
		int num_1 = 0;// 입력값 1
		int num_2 = 0;// 입력값 2
		int num_3 = 0;// 입력값 3
		int now_map = 0;// 맵 진행 상황
		int infection_count=0;
		boolean game_stop=false;//쓰레드이벤트가 발생하는 도중 메인쓰레드 정지유무
		Random random = new Random();
	
		//스킬 객체 생성
		Skill blade_attack = new Skill(15, 3, 0, true, "발도술");// 마나/ 쿨타임/ 현재쿨타임/ 사용가능/ 이름
		Skill recovery = new Skill(18, 3, 0, true, "회복");
		Skill bind = new Skill(20, 5, 0, true, "속박");
		Skill power_armor = new Skill(30, 10, 0, true, "경찰의 의지");
		Skill homerun=new Skill(70, 10, 0, true, "홈런");
		Skill clutch_hit=new Skill(20, 3, 0, true, "적시타");
		Skill blood=new Skill(10, 2, 0, true, "상처내기");
		Skill aimed_shot=new Skill(10, 2, 0, true, "조준사격");
		Skill throw_rib=new Skill(10, 2, 0, true, "뼈던지기");
		
		//티켓생성
		Item ticket = new Item("지하철 티켓",0,20);
		
		// 방어구 객체 생성
		Armor baseball_jacket = new Armor("야구잠바", 10, 20, 1,null);// 이름/무게/발견확률/방어력/고유스킬
		Armor police_armor = new Armor("경찰조끼", 20, 3, 3,null);
		Armor leather_jacket = new Armor("가죽재킷", 10, 3, 2,null);
		Armor doll_clothes = new Armor("인형옷", 15, 3, 2,null);
		Armor police_shield = new Armor("경찰방패", 30, 1, 5,power_armor);
		Armor doctor_jacket = new Armor("의사가운", 5, 10, 1,null);
		Armor dirty_jacket = new Armor("더러운조끼", 5, 100, 1,null);//회피율
		Armor suit = new Armor("양복", 0, 0, 0,null);// 기본

		// 무기 객체생성
		Weapon bat = new Weapon("야구방망이", 10, 20, 2,null);// 이름/무게/발견확률/공격력/고유스킬
		Weapon mop = new Weapon("대걸레자루", 7, 5, 1,null);
		Weapon aed = new Weapon("심제세동기", 20, 2, 3,null);
		Weapon mess = new Weapon("수술용메스", 5, 3, 2,blood);
		Weapon bambooblade = new Weapon("죽도", 10, 5, 2,blade_attack);
		Weapon knife = new Weapon("식칼", 5, 2, 3,null);
		Weapon axe = new Weapon("도끼", 15, 1, 4,null);
		Weapon gun = new Weapon("총", 5, 1, 5,aimed_shot);
		Weapon electricgun = new Weapon("전기충격기", 5, 2, 3,null);
		Weapon hand = new Weapon("맨손", 0, 0, 0,null);
		Weapon bone = new Weapon("대퇴골",10,100,3,null);//치명타
		Weapon rib = new Weapon("갈비뼈",5,100,1,throw_rib);

		// 탈것 객체생성
		Vehicle shoes = new Vehicle("운동화", 0, 0, 0);// 이름/무게/발견확률/행동력
		Vehicle inline = new Vehicle("인라인 스케이트", 10, 5, 1);
		Vehicle bicycle = new Vehicle("자전거", 20, 3, 2);
		Vehicle bike = new Vehicle("오토바이", 50, 1, 3);

		// 가방 객체생성
		Bag vinyl = new Bag("비닐봉투", 20, 20);// 이름/발견확률/무게한도
		Bag backpack = new Bag("가방", 10, 50);
		Bag police_bag = new Bag("경찰가방", 5, 100);
		Bag paper_bag = new Bag("서류가방", 0, 0);

		// 힐템 객체생성
		HealItem band = new HealItem("붕대", 3, 20, 3);// 이름/무게/확률/힐량
		HealItem medicine = new HealItem("항생제", 3, 10, 5);
		HealItem medic_kit = new HealItem("수술용키드", 5, 3, 10);

		// 식품 객체생성
		Food bread = new Food("빵", 3, 10, 50);// 이름/무게/포만감증가
		Food dry_meat = new Food("육포", 1, 5, 30);
		Food spam = new Food("스팸", 1, 3, 50);

		Scanner scan = new Scanner(System.in);
		Character character = new Character();
		
		
		//스킬목록을 캐릭터에 등록
		ArrayList<Skill> skill_list = new ArrayList<>();
		
		
		
		
		// 몬스터 객체 생성
		Zombie menzombie = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
		Zombie womenzombie = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
		Zombie animalzombie = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
		Zombie lordzombie = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
		int bigcity_monster_appearance_rate = menzombie.appearance_rate + womenzombie.appearance_rate// 대도시 좀비 등장 확률
				+ animalzombie.appearance_rate + lordzombie.appearance_rate;
		//////////////////////////////////////////////////////////////////////// 위 도시좀비
		//////////////////////////////////////////////////////////////////////// 아래 필드좀비
		Zombie dirtyzombie = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
		Zombie stunzombie = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
		Zombie bombzombie = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
		Zombie stenchzombie = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
		Zombie mutationzombie = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
		Zombie giantzombie = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
		Zombie crowdzombie = new Zombie(30, 5, 1, "좀비무리", 2, 30, 30,null,null,null);
		Zombie lastzombie = new Zombie(30, 8, 2, "최종좀비", 1, 30, 1000000,null,null,null);
		int uptown_monster_appearance_rate = dirtyzombie.appearance_rate + stunzombie.appearance_rate// 외곽 좀비 등장 확률
				+ bombzombie.appearance_rate + stenchzombie.appearance_rate + mutationzombie.appearance_rate
				+ giantzombie.appearance_rate;
		
		//연습
		//세트아이템효과를 캐릭터에 등록
		character.setSkillList(skill_list);
		character.setSkill(recovery,bind,homerun,clutch_hit);
		// 기본셋팅
		//character.equipWeapon(hand);
		//character.equipWeapon(mess);
		//character.equipWeapon(bat);
		character.equipWeapon(gun);
		//character.equipArmor(suit);
		//character.equipArmor(doctor_jacket);
		character.equipArmor(police_armor);
		//character.equipArmor(baseball_jacket);
		character.equipBag(paper_bag);
		character.equipVehicle(shoes);
		character.equipBand(band);
		character.equipMedicine(medicine);
		character.equipMedicKit(medic_kit);
		character.equipBread(bread);
		character.equipDryMeat(dry_meat);
		character.equipSpam(spam);
		//세트아이템 확인
		character.setItemCheck();
		
		
		
		//게임 시작
		System.out.println("2018년 08월 30일. 세상은 좀비 바이러스로 뒤덮였고, 지구의 종말이 찾아왔다.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("그시각 억울하게 누명을 쓰고 재판장에서 판결을 기다리고 있던 김철수.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("갑작스럽게 들이닥친 좀비들 때문에 재판장은 아수라장이 되고 그 자신도 가까스로 몸을 피한다.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("도시 한가운데로 몸을 움직인 김철수는 처참한 주변상황에 경악을 금치 못한다.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("그시각 고층빌딩 전광판에 청와대 대변인이 발표를 하고있었다. 발표내용은 다음과 같다.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("현재 대한민국은 좀비에 대한 연구를 진행하다 실수로 좀비바이러스가 창궐하였고");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("충청도 부근에 좀비로부터의 습격을 막을 대피소가 완성되었으니 생존자들은 대피소로 이동하라는 것.");
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print("김철수는 방송을 보고 빠르게 대피소로 이동할 결심을 하고 대피소로 향하는데.");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print(".");
		try {
			sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(".");
		
		//낮밤쓰레드 시작
		Time time = new Time(false);
		time.start();
		
		//날씨쓰레드 시작
		Weather weather = new Weather(false,character);
		weather.start();
		
		BgmMain bgmmain = new BgmMain();
		bgmmain.start();
		
		//상태변경 쓰레드 시작
		MpPlus mpplus = new MpPlus();
		mpplus.start();
		
		//좀비공격스레드 시작
		ZombieAttack zbat = new ZombieAttack(character,PlayZombieGame2.currentThread());
		try {
		zbat.start();
		
		
		try {//오프닝 대기
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		First_City:
		do {
			System.out.println(
					"===================================================================================================");
			System.out.println("현재 위치:대도시 중심가");
			System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
			System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
			System.out.println("회피율:" + Character.avoid_rate);
			System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9:배경음악      0:도움말");
			//감염시작
			if(Infection.safe==false&&infection_count==0) {
				infection_count++;
				Infection infection = new Infection();
				infection.start();
			}
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
			if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
				scan1=null;
				do {
					sleep(500);//1초마다 전투 끝났는지 체크한다.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("현재 위치:대도시 중심가");
				System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
				System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
				System.out.println("회피율:" + Character.avoid_rate);
				System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9:배경음악      0:도움말");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			
			switch (num_1) {
			case 1:// 주변 건물 탐색
				Zombie menzombie1 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
				Zombie womenzombie1 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// 대도시 좀비 등장 확률
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// 전투상황발생or미발생
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// 남자좀비 등장
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// 여자좀비 등장
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// 동물좀비 등장
						Character.exp += character.startFight(animalzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무 일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();//전투 종료시 각종 데이터 값 변경(사망여부/경험치/체력/포만감/남은시간)
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:대도시 건물 주변");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
					System.out.println("1:학교      2:병원      3:경찰서      4:대형마트      5.지하철      0:대도시 중심가로 이동");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan2=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:대도시 건물 주변");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
						System.out.println("1:학교      2:병원      3:경찰서      4:대형마트      5.지하철      0:대도시 중심가로 이동");
						Scanner scan2_1 = new Scanner(System.in);
						num_2 = scan2_1.nextInt();
						stop=false;
					}
					
					switch (num_2) {
					case 1:// 학교를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("학교를 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (bat.find_rate + mop.find_rate + bambooblade.find_rate
								+ baseball_jacket.find_rate + backpack.find_rate + 20)) {// 학교 아이템 발견확률

							int num = random.nextInt(bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate + backpack.find_rate + 20);
							if (num < bat.find_rate) {// 야구방망이 발견
								character.showFoundWeapon(bat);
								character.doWeapon(bat);
							} else if (num >= bat.find_rate && num < bat.find_rate + mop.find_rate) {// 대걸레 발견
								character.showFoundWeapon(mop);
								character.doWeapon(mop);
							} else if (num >= bat.find_rate + mop.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate) {// 죽도 발견
								character.showFoundWeapon(bambooblade);
								character.doWeapon(bambooblade);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate) {// 야구잠바 발견
								character.showFoundArmor(baseball_jacket);
								character.doArmor(baseball_jacket);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate + backpack.find_rate) {// 가방 발견
								character.showFoundBag(backpack);
								character.doBag(backpack);
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무 일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();//전투 종료시 발생하는 메소드
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// 병원을 탐색
						System.out.println("병원을 탐색합니다.");
						time.timeStop();
						weather.weatherStop();
						//감염시 미완성백신을 찾을수 있다.
						if(Infection.safe==false&&random.nextInt(100)<20) {//20퍼 확률로 백신찾을 수 있음
							System.out.println("미완성 백신을 찾아냈습니다. 감염상태에서 해제됩니다.");
							Infection.safe=true;
							try {//감염쓰레드 끄는 대기시간
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						// 확률배정
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20 )) {// 병원 아이템 발견확률
																									// 20=몬스터 발견확률

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// 심제세동기 발견
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// 수술용메스 발견
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// 의사가운 발견-
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// 붕대 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// 항생제 발견
								System.out.println("항생제를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("항생제를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// 의료키트 발견
								System.out.println("의료용 키트를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("의료용 키트를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비와 만남
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// 경찰서를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("경찰서를 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (gun.find_rate + electricgun.find_rate + police_armor.find_rate
								+ police_shield.find_rate + police_bag.find_rate + 20)) {// 경찰서 아이템 발견확률

							int num = random.nextInt(gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate + 20);
							if (num < gun.find_rate) {// 총 발견
								character.showFoundWeapon(gun);
								character.doWeapon(gun);
							} else if (num >= gun.find_rate && num < gun.find_rate + electricgun.find_rate) {// 전기충격기 발견
								character.showFoundWeapon(electricgun);
								character.doWeapon(electricgun);

							} else if (num >= gun.find_rate + electricgun.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate) {// 경찰조끼 발견
								character.showFoundArmor(police_armor);
								character.doArmor(police_armor);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate) {// 경찰방패 발견
								character.showFoundArmor(police_shield);
								character.doArmor(police_shield);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate) {// 경찰 의류대 발견
								character.showFoundBag(police_bag);
								character.doBag(police_bag);
							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate + bike.find_rate) {// 경탈바이크
																												// 발견
								character.showFoundVehicle(bike);
								character.doVehicle(bike);
							} else {// 좀비와 만남
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// 대형마트를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("대형마트를 탐색합니다.");
						if (random.nextInt(100) < (knife.find_rate + axe.find_rate + doll_clothes.find_rate
								+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
								+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20)) {// 대형마트 아이템 발견확률

							int num = random.nextInt(knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20);
							if (num < knife.find_rate) {// 총 발견
								character.showFoundWeapon(knife);
								character.doWeapon(knife);
							} else if (num >= knife.find_rate && num < knife.find_rate + axe.find_rate) {// 전기충격기 발견
								character.showFoundWeapon(axe);
								character.doWeapon(axe);

							} else if (num >= knife.find_rate + axe.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate) {// 경찰조끼 발견
								character.showFoundArmor(doll_clothes);
								character.doArmor(doll_clothes);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate) {// 가죽재킷 발견
								character.showFoundArmor(leather_jacket);
								character.doArmor(leather_jacket);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate) {// 비닐봉지 발견
								character.showFoundBag(vinyl);
								character.doBag(vinyl);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate) {// 인라인 발견
								character.showFoundVehicle(inline);
								character.doVehicle(inline);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate) {// 자전거 발견
								character.showFoundVehicle(bicycle);
								character.doVehicle(bicycle);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate) {// 빵 발견
								System.out.println("빵을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("빵을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate) {// 육포 발견
								System.out.println("육포를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + dry_meat.weight) {
									dry_meat.count++;
									Character.weight += dry_meat.weight;
									System.out.println("육포를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate
											+ spam.find_rate) {// 스팸 발견
								System.out.println("스팸을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + spam.weight) {
									spam.count++;
									Character.weight += spam.weight;
									System.out.println("스팸을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비와 만남
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}

							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					
					case 5://무인지하철로 이동
						time.timeStop();
						weather.weatherStop();
						System.out.println("무인 지하철로 이동합니다.");
						System.out.println("좀비들을 처치하다보면 지하철 티켓이 등장하며 지하철 티켓을 이용하면 무인 지하철을 이용할 수 있습니다.");
						System.out.println("무인 지하철을 이용하면 도시외곽을 거치지 않고 바로 다음 도시로 이동할 수 있습니다.");
						if (Infection.safe == true) {
							if (Character.ticket_have == true) {
								System.out.println("지하철 티켓을 사용합니다.");
								System.out.println("지하철에 탑승합니다.");
								System.out.println("다음 도시까지 빠르게 이동합니다.");
								now_map = 29;
								time.timeStart();
								weather.weatherStart();
								// 지하철티켓 회수는 미니게임 시작하고 회수
								break First_City;// 도시 탈출
							} else {
								System.out.println("지하철 티켓이 없습니다. 무인 지하철을 이용하실 수 없습니다.");
								time.timeStart();
								weather.weatherStart();
								break;
							}
						}else {
							System.out.println("감염상태에서는 지하철을 이용할 수 없습니다. 감염상태를 먼저 해제해야합니다.");
							time.timeStart();
							weather.weatherStart();
							break;
						}
						
					case 0:// 대도시 중심가로 이동
						System.out.println("대도시 중심가로 되돌아갑니다.");
						break;
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_2 != 0);
				break;

			case 2:// 소지품 확인
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStop();
				weather.weatherStop();
				break;

			case 3:// 장비확인
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStop();
				weather.weatherStop();
				break;

			case 4:// 다음 장소로 이동
				System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
				break;
				
			case 9://배경음 (on/off)
				try {
					if (play_now==true) {//끄기
						play_now=false;
						bgmmain.suspend();
						System.out.println("배경음악 Off");
					} else {//켜기
						bgmmain.resume();
						System.out.println("배경음악 On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// 도움말
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStop();
				weather.weatherStop();
				break;

			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_1 != 4);
		now_map++;// 진행도 +
		
		
		//무인지하철 프로그램
		time.timeStop();
		time.night.suspend();
		weather.rain2.suspend();
		weather.weatherStop();
		if (Character.ticket_have == true&&now_map==30) {//티켓가지고 있으면 지하철 진입
			game_stop=true;
			Character.ticket_have = false;//티켓 소비
			Subway subway = new Subway(game_stop);
			subway.start();
			do {//게임 끝날동안 게임 정지
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(subway.game_stop!=false);
		}
		time.timeStart();
		time.night.resume();
		weather.rain2.resume();
		weather.weatherStart();
		
		
		
		
		
		
		// 대도시 벗어남. 필드로 이동
		if (now_map > 0 && now_map <= 29) {
			is_city=false;//외곽지역임
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// 외곽 좀비 등장 확률
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("도시를 벗어났다.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// 더러운좀비 등장
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// 괴성좀비 등장
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// 폭발좀비 등장
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// 악취좀비 등장
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// 변이좀비 등장
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// 거대좀비 등장
						Character.exp += character.startFight(giantzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();

				// 도시에서 빠져나오는데 성공
				// 도시보다 강력하고 다양한 몬스터들이 출현
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:도시외곽");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+"
							+ Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"남은 시간:" + Character.left_time + "               행동력:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("행동을 입력하시오.");
					System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan1=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:도시외곽");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("행동을 입력하시오.");
						System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// 주변 탐색
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// 외곽 좀비 등장 확률
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// 전투확률
							System.out.println("전투가 발생하였습니다.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// 더러운좀비 등장
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// 괴성좀비 등장
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// 폭발좀비 등장
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// 악취좀비 등장
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// 변이좀비 등장
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// 거대좀비 등장
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// 빵발견
								System.out.println("빵을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("빵을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// 붕대
																													// 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 대장좀비 등장
								Character.exp += character.startFight(lordzombie);
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 2:// 소지품 확인
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// 장비확인
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// 다음 장소로 이동
						System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
						break;
						
					case 9://배경음 (on/off)
						try {
							if (play_now==true) {//끄기
								play_now=false;
								bgmmain.suspend();
								System.out.println("배경음악 Off");
							} else {//켜기
								bgmmain.resume();
								System.out.println("배경음악 On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;
						
					case 0:// 도움말
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;
						
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 30);// 이동거리 29까지 반복 30이 되면 중소도시에 도착
		}
		
		
		
		
		// 중소도시 도착
		do {
			is_city=true;//도시지역임
			System.out.println(
					"===================================================================================================");
			System.out.println("현재 위치:중소도시 중심가");
			System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
			System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
			System.out.println("회피율:" + Character.avoid_rate);
			System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9.배경음악      0.도움말");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
			if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
				scan1=null;
				do {
					sleep(500);//1초마다 전투 끝났는지 체크한다.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("현재 위치:중소도시 중심가");
				System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
				System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
				System.out.println("회피율:" + Character.avoid_rate);
				System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9:배경음악      0:도움말");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// 주변 건물 탐색
				Zombie menzombie1 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
				Zombie womenzombie1 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// 대도시 좀비 등장 확률
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// 전투상황발생or미발생
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// 남자좀비 등장
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// 여자좀비 등장
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// 동물좀비 등장
						Character.exp += character.startFight(animalzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무 일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();//전투 종료시 각종 데이터 값 변경(사망여부/경험치/체력/포만감/남은시간)
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:중소도시 건물 주변");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
					System.out.println("1:학교      2:병원      0:중소도시 중심가로 이동");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan2=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:중소도시 건물 주변");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
						System.out.println("1:학교      2:병원      0:중소도시 중심가로 이동");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// 학교를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("학교를 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (bat.find_rate + mop.find_rate + bambooblade.find_rate
								+ baseball_jacket.find_rate + backpack.find_rate + 20)) {// 학교 아이템 발견확률

							int num = random.nextInt(bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate + backpack.find_rate + 20);
							if (num < bat.find_rate) {// 야구방망이 발견
								character.showFoundWeapon(bat);
								character.doWeapon(bat);
							} else if (num >= bat.find_rate && num < bat.find_rate + mop.find_rate) {// 대걸레 발견
								character.showFoundWeapon(mop);
								character.doWeapon(mop);
							} else if (num >= bat.find_rate + mop.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate) {// 죽도 발견
								character.showFoundWeapon(bambooblade);
								character.doWeapon(bambooblade);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate) {// 야구잠바 발견
								character.showFoundArmor(baseball_jacket);
								character.doArmor(baseball_jacket);

							} else if (num >= bat.find_rate + mop.find_rate + bambooblade.find_rate
									+ baseball_jacket.find_rate
									&& num < bat.find_rate + mop.find_rate + bambooblade.find_rate
											+ baseball_jacket.find_rate + backpack.find_rate) {// 가방 발견
								character.showFoundBag(backpack);
								character.doBag(backpack);
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무 일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();//전투 종료시 발생하는 메소드
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// 병원을 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("병원을 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// 병원 아이템 발견확률
																									// 20=몬스터 발견확률

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// 심제세동기 발견
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// 수술용메스 발견
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// 의사가운 발견
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// 붕대 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// 항생제 발견
								System.out.println("항생제를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("항생제를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// 의료키트 발견
								System.out.println("의료용 키트를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("의료용 키트를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 0:// 중소도시 중심가로 이동
						System.out.println("중소도시 중심가로 되돌아갑니다.");
						break;
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_2 != 0);
				break;

			case 2:// 소지품 확인
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// 장비확인
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// 다음 장소로 이동
				System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
				break;
				
			case 9://배경음 (on/off)
				try {
					if (play_now==true) {//끄기
						play_now=false;
						bgmmain.suspend();
						System.out.println("배경음악 Off");
					} else {//켜기
						bgmmain.resume();
						System.out.println("배경음악 On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;	
		
			case 0:// 도움말
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_1 != 4);
		now_map++;
		
		
		
		
		//중소도시 1벗어나고 도시외곽
		if (now_map > 30 && now_map <= 59) {
			is_city=false;
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// 외곽 좀비 등장 확률
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("도시를 벗어났다.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// 더러운좀비 등장
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// 괴성좀비 등장
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// 폭발좀비 등장
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// 악취좀비 등장
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// 변이좀비 등장
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// 거대좀비 등장
						Character.exp += character.startFight(giantzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();

				// 도시에서 빠져나오는데 성공
				// 도시보다 강력하고 다양한 몬스터들이 출현
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:도시외곽");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+"
							+ Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"남은 시간:" + Character.left_time + "               행동력:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("행동을 입력하시오.");
					System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan1=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:도시 외곽");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("행동을 입력하시오.");
						System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// 주변 탐색
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// 외곽 좀비 등장 확률
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// 전투확률
							System.out.println("전투가 발생하였습니다.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// 더러운좀비 등장
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// 괴성좀비 등장
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// 폭발좀비 등장
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// 악취좀비 등장
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// 변이좀비 등장
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// 거대좀비 등장
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// 빵발견
								System.out.println("빵을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("빵을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// 붕대
																													// 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 대장좀비 등장
								Character.exp += character.startFight(lordzombie);
							}
							time.timeStart();
							weather.weatherStart();
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						break;
					case 2:// 소지품 확인
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// 장비확인
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// 다음 장소로 이동
						System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
						break;
						
					case 9://배경음 (on/off)
						try {
							if (play_now==true) {//끄기
								play_now=false;
								bgmmain.suspend();
								System.out.println("배경음악 Off");
							} else {//켜기
								bgmmain.resume();
								System.out.println("배경음악 On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;
						
					case 0:// 도움말
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;

					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 60);// 이동거리 59까지 반복 60이 되면 중소도시에 도착
		}

		// 중소도시로 이동
		Third_city:
		do {
			is_city=true;
			System.out.println(
					"===================================================================================================");
			System.out.println("현재 위치:중소도시 중심가");
			System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
			System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
			System.out.println("회피율:" + Character.avoid_rate);
			System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9.배경음악      0.도움말");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
			if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
				scan1=null;
				do {
					sleep(500);//1초마다 전투 끝났는지 체크한다.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("현재 위치:중소도시 중심가");
				System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
				System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
				System.out.println("회피율:" + Character.avoid_rate);
				System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9.배경음악      0.도움말");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// 주변 건물 탐색
				Zombie menzombie1 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
				Zombie womenzombie1 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// 대도시 좀비 등장 확률
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// 전투상황발생or미발생
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// 남자좀비 등장
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// 여자좀비 등장
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// 동물좀비 등장
						Character.exp += character.startFight(animalzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무 일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();//전투 종료시 각종 데이터 값 변경(사망여부/경험치/체력/포만감/남은시간)

				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:중소도시 건물 주변");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
					System.out.println("1:병원      2:대형마트      3.지하철      0:중소도시 중심가로 이동");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan2=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:중소도시 건물 주변");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
						System.out.println("1:병원      2:대형마트      3.지하철      0:중소도시 중심가로 이동");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// 병원을 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("병원을 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// 병원 아이템 발견확률
																									// 20=몬스터 발견확률

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// 심제세동기 발견
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// 수술용메스 발견
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// 의사가운 발견
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// 붕대 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// 항생제 발견
								System.out.println("항생제를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("항생제를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// 의료키트 발견
								System.out.println("의료용 키트를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("의료용 키트를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// 대형마트를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("대형마트를 탐색합니다.");
						if (random.nextInt(100) < (knife.find_rate + axe.find_rate + doll_clothes.find_rate
								+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
								+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20)) {// 대형마트 아이템 발견확률

							int num = random.nextInt(knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate + spam.find_rate + 20);
							if (num < knife.find_rate) {// 총 발견
								character.showFoundWeapon(knife);
								character.doWeapon(knife);
							} else if (num >= knife.find_rate && num < knife.find_rate + axe.find_rate) {// 전기충격기 발견
								character.showFoundWeapon(axe);
								character.doWeapon(axe);

							} else if (num >= knife.find_rate + axe.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate) {// 경찰조끼 발견
								character.showFoundArmor(doll_clothes);
								character.doArmor(doll_clothes);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate) {// 가죽재킷 발견
								character.showFoundArmor(leather_jacket);
								character.doArmor(leather_jacket);

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate) {// 비닐봉지 발견
								character.showFoundBag(vinyl);
								character.doBag(vinyl);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate) {// 인라인 발견
								character.showFoundVehicle(inline);
								character.doVehicle(inline);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate) {// 자전거 발견
								character.showFoundVehicle(bicycle);
								character.doVehicle(bicycle);
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate) {// 빵 발견
								System.out.println("빵을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("빵을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate) {// 육포 발견
								System.out.println("육포를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + dry_meat.weight) {
									dry_meat.count++;
									Character.weight += dry_meat.weight;
									System.out.println("육포를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= knife.find_rate + axe.find_rate + doll_clothes.find_rate
									+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate + bicycle.find_rate
									+ bread.find_rate + dry_meat.find_rate
									&& num < knife.find_rate + axe.find_rate + doll_clothes.find_rate
											+ leather_jacket.find_rate + vinyl.find_rate + inline.find_rate
											+ bicycle.find_rate + bread.find_rate + dry_meat.find_rate
											+ spam.find_rate) {// 스팸 발견
								System.out.println("스팸을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + spam.weight) {
									spam.count++;
									Character.weight += spam.weight;
									System.out.println("스팸을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
						
					case 3://무인지하철로 이동
						System.out.println("무인 지하철로 이동합니다.");
						System.out.println("좀비들을 처치하다보면 지하철 티켓이 등장하며 지하철 티켓을 이용하면 무인 지하철을 이용할 수 있습니다.");
						System.out.println("무인 지하철을 이용하면 도시외곽을 거치지 않고 바로 다음 도시로 이동할 수 있습니다.");
						if(Character.ticket_have==true) {
							System.out.println("지하철 티켓을 사용합니다.");
							System.out.println("지하철에 탑승합니다.");
							System.out.println("다음 도시까지 빠르게 이동합니다.");
							now_map=89;
							//지하철티켓 회수는 미니게임 시작하고 회수
							break Third_city;//도시 탈출
						}else {
							System.out.println("지하철 티켓이 없습니다. 무인 지하철을 이용하실 수 없습니다.");
						}
						
					case 0:// 중소도시 중심가로 이동
						System.out.println("중소도시 중심가로 되돌아갑니다.");
						break;
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_2 != 0);
				break;

			case 2:// 소지품 확인
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// 장비확인
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// 다음 장소로 이동
				System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
				break;
				
			case 9://배경음 (on/off)
				try {
					if (play_now==true) {//끄기
						play_now=false;
						bgmmain.suspend();
						System.out.println("배경음악 Off");
					} else {//켜기
						bgmmain.resume();
						System.out.println("배경음악 On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// 도움말
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_1 != 4);
		now_map++;
		
		
		//무인지하철 프로그램
		time.timeStop();
		time.night.suspend();
		weather.rain2.suspend();
		weather.weatherStop();
		if (Character.ticket_have == true&&now_map==90) {//티켓가지고 있으면 지하철 진입
			game_stop=true;
			Character.ticket_have = false;//티켓 소비
			Subway subway = new Subway(game_stop);
			subway.start();
			do {//게임 끝날동안 게임 정지
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(subway.game_stop!=false);
		}
		time.timeStart();
		time.night.resume();
		weather.rain2.resume();
		weather.weatherStart();

		
		
		
		
		// 필드로 이동
		if (now_map > 60 && now_map <= 89) {
			is_city=false;
			do {
				Zombie dirtyzombie1 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
				Zombie stunzombie1 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
				Zombie bombzombie1 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
				Zombie stenchzombie1 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
				Zombie mutationzombie1 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
				Zombie giantzombie1 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
				int uptown_monster_appearance_rate1 = dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// 외곽 좀비 등장 확률
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						+ giantzombie1.appearance_rate;
				System.out.println("도시를 벗어났다.");
				if (random.nextInt(100) < uptown_monster_appearance_rate1) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(uptown_monster_appearance_rate1);
					if (num < dirtyzombie1.appearance_rate) {// 더러운좀비 등장
						Character.exp += character.startFight(dirtyzombie1);
					} else if (num >= dirtyzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// 괴성좀비 등장
						Character.exp += character.startFight(stunzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate) {// 폭발좀비 등장
						Character.exp += character.startFight(bombzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// 악취좀비 등장
						Character.exp += character.startFight(stenchzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate) {// 변이좀비 등장
						Character.exp += character.startFight(mutationzombie1);
					} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
							+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
							&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
									+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
									+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// 거대좀비 등장
						Character.exp += character.startFight(giantzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();

				// 도시에서 빠져나오는데 성공
				// 도시보다 강력하고 다양한 몬스터들이 출현
				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:도시외곽");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+"
							+ Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/"
							+ Character.weight_limit + ")");
					System.out.println(
							"남은 시간:" + Character.left_time + "               행동력:0+" + Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("행동을 입력하시오.");
					System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
					Scanner scan1 = new Scanner(System.in);
					num_1 = scan1.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan1=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:도시외곽");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("행동을 입력하시오.");
						System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
						Scanner scan1_1 = new Scanner(System.in);
						num_1 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_1) {
					case 1:// 주변 탐색
						Zombie dirtyzombie2 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2,null,dirty_jacket,null);
						Zombie stunzombie2 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5,null,null,null);
						Zombie bombzombie2 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3,null,null,null);
						Zombie stenchzombie2 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13,null,null,null);
						Zombie mutationzombie2 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15,null,null,null);
						Zombie giantzombie2 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30,null,null,null);
						int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// 외곽 좀비 등장 확률
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate + mutationzombie2.appearance_rate
								+ giantzombie2.appearance_rate;
						if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// 전투확률
							System.out.println("전투가 발생하였습니다.");
							time.timeStop();
							weather.weatherStop();
							int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
							if (num < dirtyzombie2.appearance_rate) {// 더러운좀비 등장
								Character.exp += character.startFight(dirtyzombie2);
							} else if (num >= dirtyzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// 괴성좀비 등장
								Character.exp += character.startFight(stunzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate) {// 폭발좀비 등장
								Character.exp += character.startFight(bombzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// 악취좀비 등장
								Character.exp += character.startFight(stenchzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate) {// 변이좀비 등장
								Character.exp += character.startFight(mutationzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// 거대좀비 등장
								Character.exp += character.startFight(giantzombie2);
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 10
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// 빵발견
								System.out.println("빵을 발견하였습니다.");
								if (Character.weight_limit > Character.weight + bread.weight) {
									bread.count++;
									Character.weight += bread.weight;
									System.out.println("빵을 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
									+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
									+ mutationzombie2.appearance_rate + 20
									&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
											+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
											+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// 붕대
																													// 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 대장좀비 등장
								Character.exp += character.startFight(lordzombie);
							}
							time.timeStart();
							weather.weatherStart();
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						break;
					case 2:// 소지품 확인
						time.timeStop();
						weather.weatherStop();
						character.showInventory();
						time.timeStart();
						weather.weatherStart();
						break;

					case 3:// 장비확인
						time.timeStop();
						weather.weatherStop();
						character.showEquipedItem();
						time.timeStart();
						weather.weatherStart();
						break;

					case 4:// 다음 장소로 이동
						System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
						break;

					case 9://배경음 (on/off)
						try {
							if (play_now==true) {//끄기
								play_now=false;
								bgmmain.suspend();
								System.out.println("배경음악 Off");
							} else {//켜기
								bgmmain.resume();
								System.out.println("배경음악 On");
								play_now=true;
							}
						} catch (Exception e) {
							System.out.print("Error : ");
							System.out.println(e);
						}
						break;	
						
					case 0:// 도움말
						time.timeStop();
						weather.weatherStop();
						character.help();
						time.timeStart();
						weather.weatherStart();
						break;
						
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_1 != 4);
				now_map++;
			} while (now_map < 90);// 이동거리 89까지 반복 90이 되면 중소도시에 도착
		}

		// 마지막도시로 이동
		do {
			is_city=true;
			System.out.println(
					"===================================================================================================");
			System.out.println("현재 위치:마지막 도시 중심가");
			System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
					+ Character.exp_limit + ")");
			System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
					+ Character.weapon.attack_point);
			System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
			System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
			System.out.println("회피율:" + Character.avoid_rate);
			System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
			System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
					+ Character.vehicle.move_point);
			System.out.println(
					"===================================================================================================");
			System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9.배경음악      0.도움말");
			Scanner scan1 = new Scanner(System.in);
			num_1 = scan1.nextInt();
			//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
			if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
				scan1=null;
				do {
					sleep(500);//1초마다 전투 끝났는지 체크한다.
				}while(play_nowz!=true);
				stop=true;
				System.out.println(
						"===================================================================================================");
				System.out.println("현재 위치:마지막 도시 중심가");
				System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
				System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
				System.out.println("회피율:" + Character.avoid_rate);
				System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
				System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
						+ Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("행동을 입력하시오.");
				System.out.println("1:주변 건물 탐색      2:인벤토리 확인      3:장착중인 장비 확인     4:다음장소로 이동      9.배경음악      0.도움말");
				Scanner scan1_1 = new Scanner(System.in);
				num_1 = scan1_1.nextInt();
				stop=false;
			}
			switch (num_1) {
			case 1:// 주변 건물 탐색
				Zombie menzombie1 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
				Zombie womenzombie1 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
				Zombie animalzombie1 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
				Zombie lordzombie1 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
				int bigcity_monster_appearance_rate1 = menzombie1.appearance_rate + womenzombie1.appearance_rate// 대도시 좀비 등장 확률
						+ animalzombie1.appearance_rate + lordzombie1.appearance_rate;
					// 전투상황발생or미발생
				if (random.nextInt(100) < (bigcity_monster_appearance_rate1)) {// 전투확률
					System.out.println("전투가 발생하였습니다.");
					time.timeStop();
					weather.weatherStop();
					int num = random.nextInt(bigcity_monster_appearance_rate1);
					if (num < menzombie1.appearance_rate) {// 남자좀비 등장
						Character.exp += character.startFight(menzombie1);
					} else if (num >= menzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate) {// 여자좀비 등장
						Character.exp += character.startFight(womenzombie1);
					} else if (num >= menzombie1.appearance_rate + womenzombie1.appearance_rate
							&& num < menzombie1.appearance_rate + womenzombie1.appearance_rate
									+ animalzombie1.appearance_rate) {// 동물좀비 등장
						Character.exp += character.startFight(animalzombie1);
					} else {// 대장좀비 등장
						Character.exp += character.startFight(lordzombie1);
					}
					time.timeStart();
					weather.weatherStart();
				} else {
					System.out.println("아무 일도 발생하지 않았습니다.");
				}
				character.fightEndCheck();//전투 종료시 각종 데이터 값 변경(사망여부/경험치/체력/포만감/남은시간)

				do {
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:마지막 도시 건물 주변");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
					System.out.println("1:병원      2:경찰서     0:중소도시 중심가로 이동");
					Scanner scan2 = new Scanner(System.in);
					num_2 = scan2.nextInt();
					//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
					if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
						scan2=null;
						do {
							sleep(500);//1초마다 전투 끝났는지 체크한다.
						}while(play_nowz!=true);
						stop=true;
						System.out.println(
								"===================================================================================================");
						System.out.println("현재 위치:마지막 도시 건물 주변");
						System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
								+ Character.exp_limit + ")");
						System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
								+ Character.weapon.attack_point);
						System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
						System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
						System.out.println("회피율:" + Character.avoid_rate);
						System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
						System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
								+ Character.vehicle.move_point);
						System.out.println(
								"===================================================================================================");
						System.out.println("주변 건물을 탐색한다. 어느 건물을 탐색하시겠습니까?");
						System.out.println("1:병원      2:경찰서     0:중소도시 중심가로 이동");
						Scanner scan1_1 = new Scanner(System.in);
						num_2 = scan1_1.nextInt();
						stop=false;
					}
					switch (num_2) {
					case 1:// 병원을 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("병원을 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (aed.find_rate + mess.find_rate + doctor_jacket.find_rate
								+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20)) {// 병원 아이템 발견확률
																									// 20=몬스터 발견확률

							int num = random.nextInt(aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									+ band.find_rate + medicine.find_rate + medic_kit.find_rate + 20);
							if (num < aed.find_rate) {// 심제세동기 발견
								character.showFoundWeapon(aed);
								character.doWeapon(aed);
							} else if (num >= aed.find_rate && num < aed.find_rate + mess.find_rate) {// 수술용메스 발견
								character.showFoundWeapon(mess);
								character.doWeapon(mess);

							} else if (num >= aed.find_rate + mess.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate) {// 의사가운 발견
								character.showFoundArmor(doctor_jacket);
								character.doArmor(doctor_jacket);

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate
											+ band.find_rate) {// 붕대 발견
								System.out.println("붕대를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + band.weight) {
									band.count++;
									Character.weight += band.weight;
									System.out.println("붕대를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");

							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate) {// 항생제 발견
								System.out.println("항생제를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medicine.weight) {
									medicine.count++;
									Character.weight += medicine.weight;
									System.out.println("항생제를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else if (num >= aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
									+ medicine.find_rate
									&& num < aed.find_rate + mess.find_rate + doctor_jacket.find_rate + band.find_rate
											+ medicine.find_rate + medic_kit.find_rate) {// 의료키트 발견
								System.out.println("의료용 키트를 발견하였습니다.");
								if (Character.weight_limit > Character.weight + medic_kit.weight) {
									medic_kit.count++;
									Character.weight += medic_kit.weight;
									System.out.println("의료용 키트를 획득하였습니다.");
								} else
									System.out.println("무게 한도를 초과하였습니다.");
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;

					case 2:// 경찰서를 탐색
						time.timeStop();
						weather.weatherStop();
						System.out.println("경찰서를 탐색합니다.");
						// 확률배정
						if (random.nextInt(100) < (gun.find_rate + electricgun.find_rate + police_armor.find_rate
								+ police_shield.find_rate + police_bag.find_rate + 20)) {// 경찰서 아이템 발견확률

							int num = random.nextInt(gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate + 20);
							if (num < gun.find_rate) {// 총 발견
								character.showFoundWeapon(gun);
								character.doWeapon(gun);
							} else if (num >= gun.find_rate && num < gun.find_rate + electricgun.find_rate) {// 전기충격기 발견
								character.showFoundWeapon(electricgun);
								character.doWeapon(electricgun);

							} else if (num >= gun.find_rate + electricgun.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate) {// 경찰조끼 발견
								character.showFoundArmor(police_armor);
								character.doArmor(police_armor);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate) {// 경찰방패 발견
								character.showFoundArmor(police_shield);
								character.doArmor(police_shield);

							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate) {// 경찰 의류대 발견
								character.showFoundBag(police_bag);
								character.doBag(police_bag);
							} else if (num >= gun.find_rate + electricgun.find_rate + police_armor.find_rate
									+ police_shield.find_rate + police_bag.find_rate
									&& num < gun.find_rate + electricgun.find_rate + police_armor.find_rate
											+ police_shield.find_rate + police_bag.find_rate + bike.find_rate) {// 경탈바이크
																												// 발견
								character.showFoundVehicle(bike);
								character.doVehicle(bike);
							} else {// 좀비 발견
								Zombie menzombie2 = new Zombie(20, 3, 1, "남자좀비", 10, 20, 4,null,null,ticket);// 체력 공격력 방어력 이름 확률 고정체력 경험치 무기 방어구
								Zombie womenzombie2 = new Zombie(15, 2, 1, "여자좀비", 10, 15, 2,rib,null,ticket);
								Zombie animalzombie2 = new Zombie(10, 3, 1, "동물좀비", 15, 10, 2,null,null,ticket);
								Zombie lordzombie2 = new Zombie(30, 3, 1, "대장좀비", 5, 30, 10,bone,null,ticket);
								int bigcity_monster_appearance_rate2 = menzombie2.appearance_rate + womenzombie2.appearance_rate// 대도시 좀비 등장 확률
										+ animalzombie2.appearance_rate + lordzombie2.appearance_rate;
								System.out.println("전투가 발생하였습니다.");
								num = random.nextInt(bigcity_monster_appearance_rate2);
								if (num < menzombie2.appearance_rate) {// 남자좀비 등장
									Character.exp += character.startFight(menzombie2);
								} else if (num >= menzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate) {// 여자좀비 등장
									Character.exp += character.startFight(womenzombie2);
								} else if (num >= menzombie2.appearance_rate + womenzombie2.appearance_rate
										&& num < menzombie2.appearance_rate + womenzombie2.appearance_rate
												+ animalzombie2.appearance_rate) {// 동물좀비 등장
									Character.exp += character.startFight(animalzombie2);
								} else {// 대장좀비 등장
									Character.exp += character.startFight(lordzombie2);
								}
							}
						} else {
							System.out.println("아무일도 발생하지 않았습니다.");
						}
						character.fightEndCheck();
						time.timeStart();
						weather.weatherStart();
						break;
					case 0:// 마지막 도시 중심가로 이동
						System.out.println("마지막 도시 중심가로 되돌아갑니다.");
						break;
					default:
						System.out.println("숫자를 다시 입력해주세요.");
					}
				} while (num_2 != 0);
				break;

			case 2:// 소지품 확인
				time.timeStop();
				weather.weatherStop();
				character.showInventory();
				time.timeStart();
				weather.weatherStart();
				break;

			case 3:// 장비확인
				time.timeStop();
				weather.weatherStop();
				character.showEquipedItem();
				time.timeStart();
				weather.weatherStart();
				break;

			case 4:// 다음 장소로 이동
				System.out.println("시간이 없습니다. 빨리 다음 장소로 이동합니다.");
				break;
			
			case 9://배경음 (on/off)
				try {
					if (play_now==true) {//끄기
						play_now=false;
						bgmmain.suspend();
						System.out.println("배경음악 Off");
					} else {//켜기
						bgmmain.resume();
						System.out.println("배경음악 On");
						play_now=true;
					}
				} catch (Exception e) {
					System.out.print("Error : ");
					System.out.println(e);
				}
				break;
				
			case 0:// 도움말
				time.timeStop();
				weather.weatherStop();
				character.help();
				time.timeStart();
				weather.weatherStart();
				break;

			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_1 != 4);
		now_map++;
		// 필드로 이동
		do {
			is_city=false;
			Zombie dirtyzombie1 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2, null, dirty_jacket, null);
			Zombie stunzombie1 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5, null, null, null);
			Zombie bombzombie1 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3, null, null, null);
			Zombie stenchzombie1 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13, null, null, null);
			Zombie mutationzombie1 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15, null, null, null);
			Zombie giantzombie1 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30, null, null, null);
			int uptown_monster_appearance_rate1 
			= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate// 외곽 좀비
																											// 등장 확률
					+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
					+ giantzombie1.appearance_rate;
			System.out.println("도시를 벗어났다.");
			if (random.nextInt(100) < uptown_monster_appearance_rate1) {// 전투확률
				System.out.println("전투가 발생하였습니다.");
				time.timeStop();
				weather.weatherStop();
				int num = random.nextInt(uptown_monster_appearance_rate1);
				if (num < dirtyzombie1.appearance_rate) {// 더러운좀비 등장
					Character.exp += character.startFight(dirtyzombie1);
				} else if (num >= dirtyzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate) {// 괴성좀비 등장
					Character.exp += character.startFight(stunzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate) {// 폭발좀비 등장
					Character.exp += character.startFight(bombzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate) {// 악취좀비 등장
					Character.exp += character.startFight(stenchzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
								+ mutationzombie1.appearance_rate) {// 변이좀비 등장
					Character.exp += character.startFight(mutationzombie1);
				} else if (num >= dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
						+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate + mutationzombie1.appearance_rate
						&& num < dirtyzombie1.appearance_rate + stunzombie1.appearance_rate
								+ bombzombie1.appearance_rate + stenchzombie1.appearance_rate
								+ mutationzombie1.appearance_rate + giantzombie1.appearance_rate) {// 거대좀비 등장
					Character.exp += character.startFight(giantzombie1);
				} else {// 대장좀비 등장
					Character.exp += character.startFight(lordzombie);
				}
				time.timeStart();
				weather.weatherStart();
			} else {
				System.out.println("아무일도 발생하지 않았습니다.");
			}
			character.fightEndCheck();

			// 도시에서 빠져나오는데 성공
			// 도시보다 강력하고 다양한 몬스터들이 출현
			do {
				System.out.println(
						"===================================================================================================");
				System.out.println("현재 위치:대피소로 가는 길");
				System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
						+ Character.exp_limit + ")");
				System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
						+ Character.weapon.attack_point);
				System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
				System.out.println(
						"포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
				System.out.println("회피율:" + Character.avoid_rate);
				System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/"
						+ Character.weight_limit + ")");
				System.out.println(
						"남은 시간:" + Character.left_time + "               행동력:0+" + Character.vehicle.move_point);
				System.out.println(
						"===================================================================================================");
				System.out.println("행동을 입력하시오.");
				System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
				Scanner scan1 = new Scanner(System.in);
				num_1 = scan1.nextInt();
				//대기상태 잠시 멈추는 구간 좀비가 일정시간마다 공격하러 들어옴
				if(play_nowz==false) {//좀비가 공격하게되면 멈추는데
					scan1=null;
					do {
						sleep(500);//1초마다 전투 끝났는지 체크한다.
					}while(play_nowz!=true);
					stop=true;
					System.out.println(
							"===================================================================================================");
					System.out.println("현재 위치:대피소로 가는 길");
					System.out.println("레벨:" + Character.lv + "                    경험치:(" + Character.exp + "/"
							+ Character.exp_limit + ")");
					System.out.println("HP:" + Character.hp + "                    공격력:" + Character.attack_point_ + "+"
							+ Character.weapon.attack_point);
					System.out.println("MP:" + Character.mp + "                    치명타율:" + Character.critical_rate);
					System.out.println("포만감:" + Character.satiety + "                방어력:0+" + Character.armor.guard_point);
					System.out.println("회피율:" + Character.avoid_rate);
					System.out.println("현재 이동거리:" + now_map + "              무게:(" + Character.weight + "/" + Character.weight_limit + ")");
					System.out.println("남은 시간:" + Character.left_time + "               행동력:0+"
							+ Character.vehicle.move_point);
					System.out.println(
							"===================================================================================================");
					System.out.println("행동을 입력하시오.");
					System.out.println("1.주변 탐색      2.소지품 확인      3.장비 확인      4.다음 장소로 이동      9.배경음악      0.도움말");
					Scanner scan1_1 = new Scanner(System.in);
					num_1 = scan1_1.nextInt();
					stop=false;
				}
				switch (num_1) {
				case 1:// 주변 탐색
					Zombie dirtyzombie2 = new Zombie(15, 2, 1, "더러운좀비", 20, 15, 2, null, dirty_jacket, null);
					Zombie stunzombie2 = new Zombie(20, 3, 2, "괴성좀비", 8, 20, 5, null, null, null);
					Zombie bombzombie2 = new Zombie(20, 2, 1, "폭발좀비", 13, 20, 3, null, null, null);
					Zombie stenchzombie2 = new Zombie(30, 2, 3, "악취좀비", 8, 30, 13, null, null, null);
					Zombie mutationzombie2 = new Zombie(15, 10, 1, "변이좀비", 6, 15, 15, null, null, null);
					Zombie giantzombie2 = new Zombie(150, 1, 1, "거대좀비", 3, 150, 30, null, null, null);
					int uptown_monster_appearance_rate2 = dirtyzombie2.appearance_rate + stunzombie2.appearance_rate// 외곽
																													// 좀비
																													// 등장
																													// 확률
							+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
							+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate;
					if (random.nextInt(100) < (uptown_monster_appearance_rate2) + 20) {// 전투확률
						System.out.println("전투가 발생하였습니다.");
						time.timeStop();
						weather.weatherStop();
						int num = random.nextInt(uptown_monster_appearance_rate2 + 20);
						if (num < dirtyzombie2.appearance_rate) {// 더러운좀비 등장
							Character.exp += character.startFight(dirtyzombie2);
						} else if (num >= dirtyzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate) {// 괴성좀비 등장
							Character.exp += character.startFight(stunzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate) {// 폭발좀비 등장
							Character.exp += character.startFight(bombzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate) {// 악취좀비 등장
							Character.exp += character.startFight(stenchzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate) {// 변이좀비 등장
							Character.exp += character.startFight(mutationzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate) {// 거대좀비
																											// 등장
							Character.exp += character.startFight(giantzombie2);
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate + 10
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 10) {// 빵발견
							System.out.println("빵을 발견하였습니다.");
							if (Character.weight_limit > Character.weight + bread.weight) {
								bread.count++;
								Character.weight += bread.weight;
								System.out.println("빵을 획득하였습니다.");
							} else
								System.out.println("무게 한도를 초과하였습니다.");
						} else if (num >= dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
								+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
								+ mutationzombie2.appearance_rate + 20
								&& num < dirtyzombie2.appearance_rate + stunzombie2.appearance_rate
										+ bombzombie2.appearance_rate + stenchzombie2.appearance_rate
										+ mutationzombie2.appearance_rate + giantzombie2.appearance_rate + 20) {// 붕대
																												// 발견
							System.out.println("붕대를 발견하였습니다.");
							if (Character.weight_limit > Character.weight + band.weight) {
								band.count++;
								Character.weight += band.weight;
								System.out.println("붕대를 획득하였습니다.");
							} else
								System.out.println("무게 한도를 초과하였습니다.");
						} else {// 대장좀비 등장
							Character.exp += character.startFight(lordzombie);
						}
						time.timeStart();
						weather.weatherStart();
					} else {
						System.out.println("아무일도 발생하지 않았습니다.");
					}
					character.fightEndCheck();
					break;
				case 2:// 소지품 확인
					time.timeStop();
					weather.weatherStop();
					character.showInventory();
					time.timeStart();
					weather.weatherStart();
					break;

				case 3:// 장비확인
					time.timeStop();
					weather.weatherStop();
					character.showEquipedItem();
					time.timeStart();
					weather.weatherStart();
					break;

				case 4:// 다음 장소로 이동
					System.out.println("다음 장소로 이동합니다.");
					break;
				
				case 9://배경음 (on/off)
					try {
						if (play_now==true) {//끄기
							play_now=false;
							bgmmain.suspend();
							System.out.println("배경음악 Off");
						} else {//켜기
							bgmmain.resume();
							System.out.println("배경음악 On");
							play_now=true;
						}
					} catch (Exception e) {
						System.out.print("Error : ");
						System.out.println(e);
					}
					break;
					
				case 0:// 도움말
					time.timeStop();
					weather.weatherStop();
					character.help();
					time.timeStart();
					weather.weatherStart();
					break;

				default:
					System.out.println("숫자를 다시 입력해주세요.");
				}
			} while (num_1 != 4);
			now_map++;
		} while (now_map < 100);// 99까지 반복 100되면
		
		time.timeStop();
		weather.weatherStop();
		//보스잡기전 좀비떼 사냥
		System.out.println("대피소가 좀비떼에 둘러싸여있다.대피소에 들어가기 위해서 좀비떼를 향해 달려든다.");
		character.startFight(crowdzombie);
		//사망체크
		character.checkCharacterDead();
		if (character.dead == true) {
			System.out.println("사망하였습니다.");
			return;
		}else {//보스 ㄱㄱㄱ
			System.out.println("좀비떼를 물리치니 강력해보이는 한마리의 좀비가 앞을 막아선다. 이놈만 잡으면 대피소 내부로 들어갈 수 있다.");
			character.bossFight(lastzombie);
			if (character.dead == true) {
				System.out.println("사망하였습니다.");
				return;
			}
			System.out.println("대피소에 무사히 도착하였습니다.");
			System.out.println("--------The End-------");
			//마지막 보스 기절한 동안 방향키를 입력해서 마무리 타격 미니게임
			System.out.println("보스가 혼수상태에 빠졌습니다. 마무리 타격을 가하세요");
			System.out.println("화면에 나오는 대사를 입력하면 마무리 타격을 가할 수 있습니다.");
			BossLastAttack attack = new BossLastAttack();
			attack.start();

		}
		}catch(InterruptedException e){
			System.out.println("잠시 멈추겟습니다~~~~");
			try {
				zbat.join();//인터럽트 오면 잠시 기다리구 계세염~
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.out.println("메인쓰레드 대기중~");
			}
		
	}
}
}

