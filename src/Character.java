import java.util.*;
import java.io.*;
import javazoom.jl.player.*;

public class Character extends Unit {
	static int attack_point;
	static int hp;
	static int guard_point;
	static int left_time;// 남은시간
	static int move_point;// 행동력(높으면 1행동당 줄어드는 시간이 적어짐)
	static int satiety;// 포만감(0되면 죽음)
	static int weight_limit;// 무게한도(템창한도)
	static int weight;// 현재무게
	static int hp_;// 고정체력
	static int satiety_;// 고정 포만감
	static int lv;// 레벨
	static int exp;// 현재 경험치
	static int exp_limit;// 필요경험치
	static int attack_point_;// 스탯창에 표현되는 캐릭터 자체 공격력
	static int avoid_rate;//회피율
	static int critical_rate;//치명타율
	static int mp_;//최대마나
	static int mp;//현재마나
	static int replace_attack;//스킬용 공격력 저장소
	static int replace_guard;//스킬용 방어력 저장소
	
	static boolean doctor_set_on;
	static boolean police_set_on;
	static boolean baseball_set_on;
	
	static boolean ticket_have;
	
	static Weapon weapon;// 현재 장착하고있는 무기
	static Armor armor;// 현재 장착하고 있는 방어구
	static Bag bag;// 현재 장착하고있는 가방
	static Vehicle vehicle;// 현재 장착하고있는 이동수단
	static HealItem band;// 현재 소유하고 있는 붕대
	static HealItem medicine;// 현소유 항생제
	static HealItem medic_kit;// 현소유 키트
	static Food bread;// 현소유 빵
	static Food dry_meat;// 현소유 육포
	static Food spam;// 현소유 스팸
	static ArrayList<Skill> skill_list;//스킬목록
	static Skill recovery;//세트아이템 효과1
	static Skill bind;//세트아이템 효과2
	static Skill homerun;//세트아이템효과3
	static Skill clutch_hit;//세트아이템효과3

	public Character(/* int hp, int attack_point, int guard_point */) {
		super(100, 2, 0);
		attack_point=super.attack_point;
		hp=super.hp;
		guard_point=super.guard_point;
		left_time = 10000;
		move_point = 0;
		satiety = 100;
		weight_limit = 200;
		weight = 0;
		hp_ = hp;
		satiety_ = 100;
		lv = 1;
		exp = 0;
		exp_limit = 100;
		attack_point_ = 2;
		avoid_rate=10;
		critical_rate=10;
		mp=100;
		mp_=mp;
		replace_attack=0;
		replace_guard=0;
		doctor_set_on=false;
		police_set_on=false;
		baseball_set_on=false;
		ticket_have=true;
	}
	
	
	
	public void setSkillList(ArrayList<Skill> skill_list) {//스킬리스트를 캐릭터에 등록
		Character.skill_list=skill_list;
	}
	
	public void setSkill(Skill recovery, Skill bind, Skill homerun, Skill clutch_hit) {//스킬을 캐릭터에 등록. 추후에 추가될 수도 있음
		Character.recovery=recovery;
		Character.bind=bind;
		Character.homerun=homerun;
		Character.clutch_hit=clutch_hit;
	}
	
	public void addSkill(Skill skill) {//스킬목록에 스킬저장
		skill_list.add(skill);
	}
	
	public void clearSkill(Skill skill) {//스킬목록에서 스킬 삭제
		skill_list.remove(skill);
	}
	
	public void showSkillList() {//스킬목록 보여주기
		System.out.println("==================스킬목록===================");
		for(int i=0;i<skill_list.size();i++) {
			System.out.println((i+1)+"."+skill_list.get(i).name+"     소모mp:"+skill_list.get(i).mp+"      쿨타임:"+skill_list.get(i).cooltime);
		}
		System.out.println("==========================================");
	}
	public void lvUpCheck() {
		if (exp >= exp_limit)//현재 경험치가 최대 경험치에 도달하면 레벨업
			lvUp();
	}

	public void lvUp() {
		attack_point_++;//스탯창에 표현되는 기본공격력 증가
		attack_point++;//공격력 증가
		hp = 100;// 체력회복
		exp = 0;// 경험치
		exp_limit += 50;// 경험치통 증가
		lv++;
		System.out.println("-------레벨이 올랐습니다-------");
		System.out.println("hp가 모두 회복되고 공격력이 증가합니다.");
	}

	public void showInventory() {//현재 인벤토리 보여주기
		int num_2;
		Scanner scan = new Scanner(System.in);
		System.out.println("현재 가진 소비아이템을 확인합니다.");
		do {
			showConsumptionItem();// 소비아이템창 보여주기
			System.out.println("사용할 아이템을 선택하세요.");
			System.out.println("1.붕대     2.항생제     3.의료용키트     4.빵     5.육포     6.통조림햄     0.인벤토리 닫기");
			num_2 = scan.nextInt();
			switch (num_2) {

			case 1://붕대사용
				if (band.count > 0) {
					consumeBand();
					System.out.println("붕대를 사용합니다. HP가" + band.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("붕대를 사용할 수 없습니다.");
				}
				break;

			case 2://항생제 사용
				if (medicine.count > 0) {
					consumeMedicine();
					System.out.println("항생제를 사용합니다. HP가 " + medicine.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("항생제를 사용할 수 없습니다.");
				}
				break;

			case 3://의료용 키트 사용
				if (medic_kit.count > 0) {
					consumeMedicKit();
					System.out.println("의료용 키트를 사용합니다. HP가" + medic_kit.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("의료용 키트를 사용할 수 없습니다.");
				}
				break;

			case 4:
				if (bread.count > 0) {// 빵 사용
					consumeBread();
					System.out.println("빵을 사용합니다. 포만감이" + bread.satiety_point + "상승합니다.");
					System.out.println("현재 포만감(" + satiety + "/100)");
				} else {
					System.out.println("빵을 사용할 수 없습니다.");
				}
				break;

			case 5:
				if (dry_meat.count > 0) {// 육포사용
					consumeDryMeat();
					System.out.println("육포를 사용합니다. 포만감이" + dry_meat.satiety_point + "상승합니다.");
					System.out.println("현재 포만감(" + satiety + "/100)");
				} else {
					System.out.println("육포를 사용할 수 없습니다.");
				}
				break;

			case 6:
				if (spam.count > 0) {// 스팸사용
					consumeSpam();
					System.out.println("스팸을 사용합니다. 포만감이" + spam.satiety_point + "상승합니다.");
					System.out.println("현재 포만감(" + satiety + "/100)");
				} else {
					System.out.println("스팸을 사용할 수 없습니다.");
				}
				break;

			case 0:
				System.out.println("인벤토리를 닫습니다.");
				break;
			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_2 != 0);
	}
	
	public void showFightInventory() {//전투도중의 인벤토리 보여주기
		Scanner scan = new Scanner(System.in);
		int num_2;
		showFightConsumptionItem();// 소비아이템창 보여주기
		do {
			System.out.println("사용할 아이템을 선택하세요.");
			System.out.println("1.붕대     2.항생제     3.의료용키트");
			num_2 = scan.nextInt();
			switch (num_2) {

			case 1:// 붕대사용
				if (band.count > 0) {
					consumeBand();
					System.out.println("붕대를 사용합니다. HP가" + band.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("붕대를 사용할 수 없습니다.");
				}
				break;

			case 2:// 항생제 사용
				if (medicine.count > 0) {
					consumeMedicine();
					System.out.println("항생제를 사용합니다. HP가 " + medicine.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("항생제를 사용할 수 없습니다.");
				}
				break;

			case 3:// 의료용 키트 사용
				if (medic_kit.count > 0) {
					consumeMedicKit();
					System.out.println("의료용 키트를 사용합니다. HP가" + medic_kit.heal_point + "상승합니다.");
					System.out.println("현재 체력(" + hp + "/100)");
				} else {
					System.out.println("의료용 키트를 사용할 수 없습니다.");
				}
				break;
			default:
				System.out.println("숫자를 다시 입력해주세요.");
			}
		} while (num_2 != 1 && num_2 != 2 && num_2 != 3);
	}

	public void showConsumptionItem() {//소비아이템창 현황 보여줌
		System.out.println("==================치료 아이템 현황===================");
		System.out.println("                   -붕대-  hp  +" + band.heal_point);
		System.out.println("                   " + band.count + "개");
		System.out.println("");
		System.out.println("                   -항생제-  hp  +" + medicine.heal_point);
		System.out.println("                   " + medicine.count + "개");
		System.out.println("");
		System.out.println("                   -의료용키트-  hp  +" + medic_kit.heal_point);
		System.out.println("                   " + medic_kit.count + "개");
		System.out.println("================================================");
		System.out.println("==================식품 아이템 현황===================");
		System.out.println("                   -빵-  포만감  +" + bread.satiety_point);
		System.out.println("                   " + bread.count + "개");
		System.out.println("");
		System.out.println("                   -육포-  포만감  +" + dry_meat.satiety_point);
		System.out.println("                   " + dry_meat.count + "개");
		System.out.println("");
		System.out.println("                   -통조림햄-  포만감  +" + spam.satiety_point);
		System.out.println("                   " + spam.count + "개");
		System.out.println("================================================");
		System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
	}
	
	public void showFightConsumptionItem() {//전투중의 소비아이템창
		System.out.println("==================치료 아이템 현황===================");
		System.out.println("                   -붕대-  hp  +" + band.heal_point);
		System.out.println("                   " + band.count + "개");
		System.out.println("");
		System.out.println("                   -항생제-  hp  +" + medicine.heal_point);
		System.out.println("                   " + medicine.count + "개");
		System.out.println("");
		System.out.println("                   -의료용키트-  hp  +" + medic_kit.heal_point);
		System.out.println("                   " + medic_kit.count + "개");
		System.out.println("================================================");
	}

	public void showEquipedItem() {
		System.out.println("현재 장착한 장비아이템을 확인합니다.");
		System.out.println("==================장착아이템=======================");
		System.out.println("                   -무기-");
		System.out.println("                                        " + weapon.name + "");
		System.out.println("                                        공격력:" + weapon.attack_point);
		System.out.println("                                        무게:" + weapon.weight);
		System.out.println("");
		System.out.println("                   -방어구-");
		System.out.println("                                        " + armor.name + "");
		System.out.println("                                        방어력:" + armor.guard_point);
		System.out.println("                                        무게:" + armor.weight);
		System.out.println("");
		System.out.println("                   -가방-");
		System.out.println("                                        " + bag.name + "");
		System.out.println("                                        무게한도:" + bag.weight_limit);
		System.out.println("");
		System.out.println("                   -이동수단-");
		System.out.println("                                        " + vehicle.name + "");
		System.out.println("                                        행동력:" + vehicle.move_point);
		System.out.println("                                        무게:" + vehicle.weight);
		System.out.println("================================================");
	}

	public void fightEndCheck() {//전투및 탐색 종료시 발생하는 메소드
		// 남은시간 감소
		left_time -= 4;
		left_time += move_point;// 행동력 보정
		// 포만감 감소
		satiety -= 3;
		// 사망체크
		checkCharacterDead();
		if (dead == true) {
			System.out.println("사망하였습니다.");
			System.exit(0);
		}
		// 레벨업 체크
		lvUpCheck();
		mpRestore();//마나회복
	}
	
	public void setItemCheck() {//세트아이템 효과를 확인한다.
		if(weapon.name=="수술용메스"&&armor.name=="의사가운"&&doctor_set_on==false) {//세트가 완성되면
			doctor_set_on=true;
			skill_list.add(recovery);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("세트아이템(의사)의 효과로 스킬 \"회복\"을 사용할 수 있게 됩니다!!!!! ");
		}else if(weapon.name=="수술용메스"&&armor.name=="의사가운"&&doctor_set_on==true) {
			//아무일도 안일어남
		}else {
			if(skill_list.contains(recovery)==true) {
				skill_list.remove(recovery);
				System.out.println("세트아이템(의사)의 효과가 사라집니다.");
				doctor_set_on=false;
			}
		}
		if(weapon.name=="총"&&armor.name=="경찰조끼"&&police_set_on==false) {//세트완성되면 스킬등록
			police_set_on=true;
			skill_list.add(bind);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("세트아이템(경찰)의 효과로 스킬 \"속박\"을 사용할 수 있게 됩니다!!!!! ");
		}else if(weapon.name=="총"&&armor.name=="경찰조끼"&&police_set_on==true) {
			//아무일도 안일어남
		}else {
			if(skill_list.contains(bind)==true) {
				skill_list.remove(bind);
				System.out.println("세트아이템(경찰)의 효과가 사라집니다.");
				police_set_on=false;
			}
		}
		if(weapon.name=="야구방망이"&&armor.name=="야구잠바"&&baseball_set_on==false) {//세트완성되면 스킬등록
			baseball_set_on=true;
			skill_list.add(homerun);
			skill_list.add(clutch_hit);
			SetItemBgm set = new SetItemBgm();
			set.start();
			System.out.println("세트아이템(야구선수)의 효과로 스킬 \"홈런\"을 사용할 수 있게 됩니다!!!!! ");
			System.out.println("세트아이템(야구선수)의 효과로 스킬 \"적시타\"를 사용할 수 있게 됩니다!!!!! ");
		}else if(weapon.name=="야구방망이"&&armor.name=="야구잠바"&&baseball_set_on==true) {
			//아무일도 안일어남
		}else {
			if(skill_list.contains(homerun)==true) {
				skill_list.remove(homerun);
				skill_list.remove(clutch_hit);
				System.out.println("세트아이템(야구선수)의 효과가 사라집니다.");
				baseball_set_on=false;
			}
		}
	}
	
	public void mpRestore() {//마나회복
		if(mp<mp_)
			mp+=3;
		if(mp>=mp_)
			mp=mp_;
	}
	
	public void doBag(Bag bag) {//가방 발견시 발생하는 메소드
		int num_3;
		do {
			Scanner scan = new Scanner(System.in);
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// 착용한다.
				if (Character.weight > Character.weight_limit - Character.bag.weight_limit
						+ bag.weight_limit) {
					System.out.println("현재 소지하고 있는 아이템이 너무 많아 가방을 교체할 수 없습니다.");
				} else {
					clearBag(Character.bag);// 착용중인 아이템 해제
					equipBag(bag);// 발견한 아이템 장착
					setItemCheck();
					System.out.println("발견한 아이템을 착용하였습니다.");
					break;
				}
			case 2:// 착용 안한다.
				System.out.println("발견한 아이템을 착용하지 않습니다.");
				break;
			default:
				System.out.println("번호를 다시 입력해주세요.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}
	
	public void doWeapon(Weapon weapon) {// 무기 발견시 발생하는 메소드
		int num_3;
		int num_4;
		int num_5;
		do {
			Scanner scan = new Scanner(System.in);
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// 착용한다.
				if (weight_limit >= weight - Character.weapon.weight + weapon.weight) {// 가방 한도 체크
					clearWeapon(Character.weapon);// 착용중인 아이템 해제
					equipWeapon(weapon);// 발견한 아이템 장착
					setItemCheck();
					System.out.println("발견한 아이템을 착용하였습니다.");
				} else {// 장착시 무게한도 넘어가면
					System.out.println("무게 한도를 초과합니다. 아이템을 소비하여 인벤토리를 비우시겠습니까?");
					System.out.println("1.예        2.아니오");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// 아이템을 사용하여 인벤토리를 비운다.
							showConsumptionItem();// 소비아이템창 보여주기
							do {
								System.out.println("사용할 아이템을 선택하세요.");
								System.out.println(
										"1.붕대     2.항생제     3.의료용키트     4.빵     5.육포     6.통조림햄     0.인벤토리 닫기");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1://붕대사용
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용합니다. HP가" + band.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2://항생제 사용
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용합니다. HP가 " + medicine.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3://의료키트 사용
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용합니다. HP가" + medic_kit.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// 빵 사용
										consumeBread();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용합니다. 포만감이" + bread.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// 육포사용
										consumeDryMeat();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용합니다. 포만감이" + dry_meat.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// 스팸사용
										consumeSpam();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용합니다. 포만감이" + spam.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("인벤토리를 닫습니다.");
									break;
								default:
									System.out.println("숫자를 다시 입력해주세요.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + weapon.weight) {// 가방 한도 체크
								clearWeapon(Character.weapon);// 착용중인 아이템 해제
								equipWeapon(weapon);// 발견한 아이템 장착
								setItemCheck();
								System.out.println("발견한 아이템을 착용하였습니다.");
							} else {
								System.out.println("무게를 초과합니다. 발견한 아이템을 버립니다.");
							}
							break;
						case 2:// 비우지 않는다.
							System.out.println("발견한 아이템을 착용하지 않습니다.");
							break;
						default:
							System.out.println("번호를 다시 입력해주세요.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// 착용 안한다.
				System.out.println("발견한 아이템을 착용하지 않습니다.");
				break;
			default:
				System.out.println("번호를 다시 입력해주세요.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void doArmor(Armor armor) {// 방어템 획득시 착용프로세스
		Scanner scan = new Scanner(System.in);
		int num_3;
		int num_4;
		int num_5;
		do {
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// 착용한다.
				if (weight_limit >= weight - Character.armor.weight + armor.weight) {// 무게확인
					clearArmor(Character.armor);// 착용중인 아이템 해제
					equipArmor(armor);// 발견한 아이템 장착
					setItemCheck();
					System.out.println("발견한 아이템을 착용하였습니다.");
				} else {// 장착시 무게한도 넘어가면
					System.out.println("무게 한도를 초과합니다. 아이템을 소비하여 인벤토리를 비우시겠습니까?");
					System.out.println("1.예        2.아니오");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// 아이템을 사용하여 인벤토리를 비운다.
							showConsumptionItem();// 소비아이템창 보여주기
							do {
								System.out.println("사용할 아이템을 선택하세요.");
								System.out.println(
										"1.붕대     2.항생제     3.의료용키트     4.빵     5.육포     6.통조림햄     0.인벤토리 닫기");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1://붕대사용
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용합니다. HP가" + band.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2://항생제 사용
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용합니다. HP가 " + medicine.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3://의료키트 사용
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용합니다. HP가" + medic_kit.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// 빵 사용
										consumeBread();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용합니다. 포만감이" + bread.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// 육포사용
										consumeDryMeat();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용합니다. 포만감이" + dry_meat.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// 스팸사용
										consumeSpam();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용합니다. 포만감이" + spam.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("인벤토리를 닫습니다.");
									break;
								default:
									System.out.println("숫자를 다시 입력해주세요.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + armor.weight) {// 가방 한도가 현재 무게와 획득아이템 무게의 총합보다 크면
								clearArmor(Character.armor);// 착용중인 아이템 해제
								equipArmor(armor);// 발견한 아이템 장착
								setItemCheck();
								System.out.println("발견한 아이템을 착용하였습니다.");
							} else {
								System.out.println("무게를 초과합니다. 발견한 아이템을 버립니다.");
							}
							break;
						case 2:// 비우지 않는다.
							System.out.println("발견한 아이템을 착용하지 않습니다.");
							break;
						default:
							System.out.println("번호를 다시 입력해주세요.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// 착용 안한다.
				System.out.println("발견한 아이템을 착용하지 않습니다.");
				break;
			default:
				System.out.println("번호를 다시 입력해주세요.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void doVehicle(Vehicle vehicle) {
		Scanner scan = new Scanner(System.in);
		int num_3;
		int num_4;
		int num_5;
		do {
			num_3 = scan.nextInt();
			switch (num_3) {
			case 1:// 착용한다.
				if (weight_limit >= weight - Character.vehicle.weight + vehicle.weight) {// 가방 한도가 현재 무게와
					// 획득아이템 무게의
					// 총합보다 크면
					clearVehicle(Character.vehicle);// 착용중인 아이템 해제
					equipVehicle(vehicle);// 발견한 아이템 장착
					setItemCheck();
					System.out.println("발견한 아이템을 착용하였습니다.");
				} else {// 장착시 무게한도 넘어가면
					System.out.println("무게 한도를 초과합니다. 아이템을 소비하여 인벤토리를 비우시겠습니까?");
					System.out.println("1.예        2.아니오");
					do {
						num_4 = scan.nextInt();
						switch (num_4) {
						case 1:// 아이템을 사용하여 인벤토리를 비운다.
							showConsumptionItem();// 소비아이템창 보여주기
							do {
								System.out.println("사용할 아이템을 선택하세요.");
								System.out.println(
										"1.붕대     2.항생제     3.의료용키트     4.빵     5.육포     6.통조림햄     0.인벤토리 닫기");
								num_5 = scan.nextInt();
								switch (num_5) {

								case 1:
									if (band.count > 0) {
										consumeBand();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용합니다. HP가" + band.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("붕대를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 2:
									if (medicine.count > 0) {
										consumeMedicine();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용합니다. HP가 " + medicine.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("항생제를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 3:
									if (medic_kit.count > 0) {
										consumeMedicKit();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용합니다. HP가" + medic_kit.heal_point + "상승합니다.");
										System.out.println("현재 체력(" + hp + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("의료용 키트를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 4:
									if (bread.count > 0) {// 빵 사용
										consumeBread();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용합니다. 포만감이" + bread.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("빵을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 5:
									if (dry_meat.count > 0) {// 육포사용
										consumeDryMeat();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용합니다. 포만감이" + dry_meat.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("육포를 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 6:
									if (spam.count > 0) {// 스팸사용
										consumeSpam();
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용합니다. 포만감이" + spam.satiety_point + "상승합니다.");
										System.out.println("현재 포만감(" + satiety + "/100)");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									} else {
										showConsumptionItem();// 소비아이템창 보여주기
										System.out.println("스팸을 사용할 수 없습니다.");
										System.out.println("현재 무게(" + weight + "/" + weight_limit + ")");
									}
									break;

								case 0:
									System.out.println("인벤토리를 닫습니다.");
									break;
								default:
									System.out.println("숫자를 다시 입력해주세요.");
								}
							} while (num_5 != 0);
							if (weight_limit >= weight + vehicle.weight) {// 가방 무게 체크
								clearVehicle(Character.vehicle);// 착용중인 아이템 해제
								equipVehicle(vehicle);// 발견한 아이템 장착
								setItemCheck();
								System.out.println("발견한 아이템을 착용하였습니다.");
							} else {
								System.out.println("무게를 초과합니다. 발견한 아이템을 버립니다.");
							}
							break;
						case 2:// 비우지 않는다.
							System.out.println("발견한 아이템을 착용하지 않습니다.");
							break;
						default:
							System.out.println("번호를 다시 입력해주세요.");
						}
					} while (num_4 != 1 && num_4 != 2);
				}
				break;
			case 2:// 착용 안한다.
				System.out.println("발견한 아이템을 착용하지 않습니다.");
				break;
			default:
				System.out.println("번호를 다시 입력해주세요.");
			}
		} while (num_3 != 1 && num_3 != 2);
	}

	public void equipWeapon(Weapon weapon) {// 무기 장착
		Character.weapon = weapon;
		Character.attack_point += weapon.attack_point;
		Character.weight += weapon.weight;
		//아이템으로인한 스킬등록
		if(weapon.name=="죽도") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="대퇴골") {
			critical_rate+=30;
		}
		if(weapon.name=="수술용메스") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="총") {
			skill_list.add(weapon.skill);
		}
		if(weapon.name=="갈비뼈") {
			skill_list.add(weapon.skill);
		}
	}

	public void equipArmor(Armor armor) {// 방어구 장착
		Character.armor = armor;
		Character.guard_point += armor.guard_point;
		Character.weight += armor.weight;
		//아이템으로인한 스킬등록
		if(armor.name=="경찰방패") {
			skill_list.add(armor.skill);
		}
		if(armor.name=="더러운조끼") {
			avoid_rate+=30;
		}
	}

	public void equipVehicle(Vehicle vehicle) {// 이동수단 장착
		Character.vehicle = vehicle;
		Character.move_point += vehicle.move_point;
		Character.weight += vehicle.weight;
	}

	public void equipBag(Bag bag) {// 가방 장착
		Character.bag = bag;
		Character.weight_limit += bag.weight_limit;
	}

	public void clearWeapon(Weapon weapon) {// 무기 해제
		Character.attack_point -= weapon.attack_point;
		Character.weight -= weapon.weight;
		Character.weapon = null;
		//아이템으로인한 스킬해제
		if(weapon.name=="죽도") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="대퇴골") {
			critical_rate-=30;
		}
		if(weapon.name=="수술용메스") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="총") {
			skill_list.remove(weapon.skill);
		}
		if(weapon.name=="갈비뼈") {
			skill_list.remove(weapon.skill);
		}
	}

	public void clearArmor(Armor armor) {// 방어구 해제
		Character.guard_point -= armor.guard_point;
		Character.weight -= armor.weight;
		Character.armor = null;
		//아이템으로인한 스킬등록
		if(armor.name=="경찰방패") {
			skill_list.remove(armor.skill);
		}
		if(armor.name=="더러운조끼") {
			avoid_rate-=30;
		}
	}

	public void clearVehicle(Vehicle vehicle) {// 이동수단 해제
		Character.move_point -= vehicle.move_point;
		Character.weight -= vehicle.weight;
		Character.vehicle = null;
	}

	public void clearBag(Bag bag) {// 가방 해제
		Character.weight_limit -= bag.weight_limit;
		Character.bag = null;
	}

	public void equipBand(HealItem healitem) {// 붕대 캐릭터에 연결
		Character.band = healitem;
		band.count += 5;
		weight += (band.weight * 5);
	}

	public void equipMedicine(HealItem healitem) {// 항생제 캐릭터에 연결
		Character.medicine = healitem;
	}

	public void equipMedicKit(HealItem healitem) {// 키트 캐릭터에 연결
		Character.medic_kit = healitem;
	}

	public void equipBread(Food food) {// 빵 캐릭터에 연결
		Character.bread = food;
		bread.count += 5;
		weight += (bread.weight * 5);
	}

	public void equipDryMeat(Food food) {// 육포 캐릭터에 연결
		Character.dry_meat = food;
	}

	public void equipSpam(Food food) {// 스팸 캐릭터에 연결
		Character.spam = food;
	}

	public void consumeBand() {// 붕대 사용
		Character.band.count--;
		Character.hp += band.heal_point;
		Character.weight -= band.weight;
		if (hp > hp_)// 체력이 고정체력보다 높아지면 고정체력으로 체력 맞춤
			hp = hp_;
	}

	public void consumeMedicine() {// 항생제 사용
		Character.medicine.count--;
		Character.hp += medicine.heal_point;
		Character.weight -= band.weight;
		if (hp > hp_)
			hp = hp_;
	}

	public void consumeMedicKit() {// 키트 사용
		Character.medic_kit.count--;
		Character.hp += medic_kit.heal_point;
		Character.weight -= medic_kit.weight;
		if (hp > hp_)
			hp = hp_;
	}

	public void consumeBread() {// 빵 사용
		Character.bread.count--;
		Character.satiety += bread.satiety_point;
		Character.weight -= bread.weight;
		if (satiety > satiety_)// 포만감이 고정포만감보다 높아지면 고정포만감으로 체력 맞춤
			satiety = satiety_;
	}

	public void consumeDryMeat() {// 육포 사용
		Character.dry_meat.count--;
		Character.satiety += dry_meat.satiety_point;
		Character.weight -= dry_meat.weight;
		if (satiety > satiety_)
			satiety = satiety_;
	}

	public void consumeSpam() {// 스팸 사용
		Character.spam.count--;
		Character.satiety += spam.satiety_point;
		Character.weight -= spam.weight;
		if (satiety > satiety_)
			satiety = satiety_;
	}

	public void checkCharacterDead() {
		if (left_time <= 0) // 남은시간이 0되면 사망
			dead = true;
		if (hp <= 0)// 남은체력이 0되면 사망
			dead = true;
		if (satiety <= 0)// 남은 포만감이 0되면 사망
			dead = true;
	}

	public int startFight(Zombie zombie) {//전투를 시작한다.
		Scanner scan= new Scanner(System.in);
		Random random = new Random();
		replace_attack = attack_point;//현재 공격력 저장
		replace_guard = guard_point;//현재 방어력 저장
		int replace_police_attack=0;
		int replace_critical_rate=critical_rate;
		int num;
		int num_1 = 0;
		int bindcount=0;//속박카운트
		int police_power_count=0;//경찰의의지 카운트
		int stun_count=0;//스턴카운트
		int weakness_count=0;//약화 카운트
		int blood_point=0;
		int critical_count=0;
		boolean escape=false;
		boolean curse=false;
		boolean zombie_blood=false;
		
		showFightDislpay(zombie);// 먼저 현재 상태창을 보여준다.
		Fight:
		do {
			do {
				//스킬 쿨타임 감소
				if (num_1 != 0) {
					for (int i = 0; i < skill_list.size(); i++) {
						if (skill_list.get(i).cooltime > 0)
							skill_list.get(i).cooltime--;
					}
				}
				if(police_power_count>0&&num_1!=0) {//버프 유지시 사용효과적용
					System.out.println("경찰의 의지가 "+(police_power_count)+"턴 남았습니다.");
					attack_point+=replace_guard;
					guard_point-=replace_guard;
				}
				if (stun_count > 0) {//스턴 진행중이면
					System.out.println("전투 불능 상태입니다.남은 횟수:" + stun_count);
					stun_count--;
				} else {//스턴이 풀려있으면
					System.out.println("1.기본공격      2.스킬사용      3.소비아이템 확인      4.도망");
					num_1 = scan.nextInt();
					switch (num_1) {
					case 1:// 기본공격
						if(critical_count>0) {//조준사격 적용되었으면
							critical_rate=100;
							attackCharToZom(zombie);
							critical_count--;
							System.out.println("조준사격이  "+critical_count+"회 남았습니다.");
						}else {
							critical_rate=replace_critical_rate;
							attackCharToZom(zombie);
						}
						break;
					case 2:// 스킬사용
						showSkillList();
						System.out.println("사용할 스킬의 번호를 입력하세요.");
						num = scan.nextInt();
						try {
							if (skill_list.get(num - 1).name == "발도술") {// 스킬 구분하고 사용
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									BladeBgm blade = new BladeBgm();
									blade.start();
									System.out.println("발도술을 사용합니다.");
									zombie.hp -= attack_point * 2;// 공격력*2 만큼의 고정피해를 입힌다.
									System.out.println(
											"발도술을 사용하여 " + zombie.name + "에게 " + attack_point * 2 + "의 고정피해를 입혔다!!!!!");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "회복") {
								int addhp = random.nextInt(11);
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								CureBgm cure = new CureBgm();
								cure.start();
								if (skill_list.get(num - 1).canuse == true && hp < 100) {// 사용 가능하면
									System.out.println("회복을 사용합니다.");
									hp += addhp;// 랜덤의 체력을 회복한다.
									System.out.println("회복을 사용하여 " + addhp + "의 체력을 회복했다!!!!!");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
									if (hp > 100) {// 만피제한
										hp = 100;
									}
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "속박") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									BindBgm bind = new BindBgm();
									bind.start();
									System.out.println("속박을 사용합니다.");
									bindcount += 3;
									System.out.println("상대방은 3턴동안 행동을 취하지 못합니다.");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "경찰의 의지") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									PoliceBgm police = new PoliceBgm();
									police.start();
									System.out.println("경찰의 의지를 사용합니다.");
									System.out.println("5턴동안 방어력을 공격력으로 전환합니다.");
									police_power_count = 6;
									attack_point += replace_guard;
									guard_point -= replace_guard;
									replace_police_attack=attack_point;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "홈런") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									HomerunBgm homerun = new HomerunBgm();
									homerun.start();
									System.out.println("홈런을 사용합니다.");
									if(random.nextInt(100)<50) {
										zombie.hp=0;
										System.out.println("홈런에 성공하였습니다!!!!!!!!!!");
									}else {
										System.out.println("홈런에 실패하였습니다........");
									}
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "적시타") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									HomerunBgm homerun = new HomerunBgm();
									homerun.start();
									System.out.println("적시타를 사용합니다.");
									System.out.println("좀비의 체력이 절반으로 줄어듭니다.");
									zombie.hp-=zombie.hp/2;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "상처내기") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									MessBgm mess = new MessBgm();
									mess.start();
									System.out.println("상처내기를 사용합니다.");
									System.out.println("좀비는 지속적으로 추가 출혈 피해를 입습니다.");
									zombie_blood=true;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "조준사격") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									GunAttackBgm gun = new GunAttackBgm();
									gun.start();
									System.out.println("조준사격을 사용합니다.");
									System.out.println("앞으로 3턴의 기본공격은 치명타 판정을 받습니다..");
									critical_count=3;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "뼈던지기") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("뼈던지기를 사용합니다.");
									zombie.hp-=(attack_point+1);
									System.out.println((attack_point+1)+"의 피해를 입힙니다.");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							}
						} catch (IndexOutOfBoundsException e) {
							System.out.println("올바른 스킬이 아닙니다. 전투화면으로 돌아갑니다.");
							showFightDislpay(zombie);
							num_1 = 0;// 다시
						}
						break;
					case 3:// 소비아이템 확인
						showFightInventory();
						break;
					case 4:// 도망간다
						System.out.println("전투에서 도망친다.시간을 20 소모합니다.");
						left_time-=20;
						escape=true;//도망친것 확인
						break Fight;
					default:
						System.out.println();
					}
					if (Character.armor.name=="더러운조끼") {
						avoid_rate=40;
					}else {
						avoid_rate=10;
					}
				}
			} while (num_1 != 1 && num_1 != 2 && num_1 != 3 && num_1 != 4);
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(zombie_blood==true) {
				int blood=random.nextInt(attack_point);
				System.out.println("좀비는 "+blood+"의 출혈 데미지를 입습니다.");
				zombie.hp-=blood;
			}
			if(blood_point>0) {//출혈상태에 있다면
				hp-=blood_point;
				System.out.println("지속적인 출혈로"+blood_point+"만큼의 데미지를 입습니다.");
				blood_point--;
			}
			showFightDislpay(zombie);
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "가 죽었다.전투에서 승리했다.");
				if(zombie.name=="대장좀비"&&random.nextInt(100)<zombie.weapon.find_rate) {//사망시 아이템 드랍
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="더러운좀비"&&random.nextInt(100)<zombie.armor.find_rate) {//사망시 아이템 드랍
					showFoundArmor(zombie.armor);
					doArmor(zombie.armor);
				}
				if(zombie.name=="여자좀비"&&random.nextInt(100)<zombie.weapon.find_rate) {//사망시 아이템 드랍
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="여자좀비"||zombie.name=="남자좀비"||zombie.name=="동물좀비"||zombie.name=="대장좀비") {//사망시 아이템 드랍
					if(random.nextInt(100)<zombie.ticket.find_rate) {
						ticket_have=true;
						System.out.println("지하철 티켓을 획득하였습니다!!!! 지하철에 입장하실 수 있습니다.");
					}
				}
				if(curse==false) {
					System.out.println("경험치를 " + zombie.exp + "획득했다.");
				}else {
					System.out.println("경험치를 획득할 수 없습니다.");
				}
				break;
			}
			if(police_power_count>0)//경찰버프 감소
				police_power_count--;
			if(police_power_count==0&&weakness_count==0) {//경찰버프 끝났으면 초기화
				attack_point = replace_attack;
				guard_point = replace_guard;
				police_power_count--;
			}
			if(police_power_count==0&&weakness_count>0) {//경찰버프 끝났으면 초기화
				attack_point -= replace_guard;
				guard_point += replace_guard;
				police_power_count--;
			}
			
			
			
			if(bindcount==0) {//좀비턴
				if(zombie.name=="폭발좀비") {//폭발좀비면 먼저 폭발한다.
					System.out.println("좀비가 폭발합니다. 20의 폭발 데미지를 입습니다.");
					hp-=20;
					break;
				}else {//폭발좀비 아니면 공격 후 여러가지 상태이상 등등
					zombie.attackZomToChar(this);//좀비의 공격및 여러가지스킬
				}
				if(zombie.name=="더러운좀비"&&random.nextInt(100)<30) {//더러운좀비일 때 30%확률로 디버프 건다.
					System.out.println("더러운 좀비의 분비물이 전투 경험의 축적을 방해합니다.");
					curse=true;
				}
				if(zombie.name=="괴성좀비"&&random.nextInt(100)<20) {//괴성좀비일 때 20%확률로 디버프 건다.
					System.out.println("괴성좀비의 소름끼치는 목소리가 나를 전투 붙능 상태로 만듭니다.");
					System.out.println("공격을 회피할 수 없습니다.");
					avoid_rate=0;
					stun_count=3;
				}
				if(zombie.name=="악취좀비"&&random.nextInt(100)<20) {//악취좀비일 때 20%확률로 디버프 건다.
					System.out.println("악취좀비의 썩은 냄새가 나를 약화 상태로 만듭니다.");
					System.out.println("공격력이 1 감소합니다.");
					attack_point--;
					if(attack_point<0)
						attack_point=0;
					
				}
				if(zombie.name=="변이좀비"&&random.nextInt(100)<20) {//변이좀비일 때 20%확률로 디버프 건다.
					System.out.println("변이좀비의 강력한 공격이 나를 출혈 상태로 만듭니다.");
					System.out.println("시간이 지남에 따라 출혈 데미지가 줄어듭니다..");
					blood_point=3;
				}
				if(zombie.name=="대장좀비"&&random.nextInt(100)<50) {//대장좀비일 때 50%확률로 디버프 건다.
					System.out.println("대장좀비의 알수 없는 공격이 나의 기운을 빼앗는다.");
					System.out.println("mp가 5 감소합니다.");
					mp-=5;
					if(mp<0)
						mp=0;
				}
				
				
				
				
			}else {
				bindcount--;
				System.out.println("좀비가 속박되어 행동을 취할 수 없습니다. 남은횟수:"+bindcount);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showFightDislpay(zombie);
			if (police_power_count > 0) {//경찰의 의지 버프 남아있으면 다시초기화
				attack_point -= replace_guard;
				guard_point += replace_guard;
			}
			checkDead();
			if (dead == true)
			break;
		} while (true);
		attack_point = replace_attack;//버프해제
		guard_point = replace_guard;//버프해제
		zombie.returnZombie();// 좀비 초기화
		if(curse==true||escape==true) {//저주에 걸렸거나 도망쳤으면 경험치 0
			return 0;
		}else {//저주 안걸렸을 시에 경험치획득
			return zombie.exp;
		}
	}
	
	public void attackCharToZom(Zombie zombie) {// 캐릭터가 좀비를 공격
		Random random = new Random();
		int rate = random.nextInt(100);// 확률값 변수
		if (rate < 10) {// 좀비가 공격을 회피함<---
			System.out.println("좀비가 공격을 회피했다.");
		} else if (rate >= 10 && rate < 10+critical_rate) {// 좀비에게 치명타를 입힘<---
			CriticalBgm cri = new CriticalBgm();
			cri.start();
			if (attack_point - zombie.guard_point > 1) {// 유효공격이 1 이상이면
				zombie.hp = zombie.hp - 2 * (attack_point - zombie.guard_point);
				System.out.println("나는" + zombie.name + "의 급소를 공격하여 "
						+ 2 * (attack_point - zombie.guard_point) + "의 치명타 피해를 입혔다!!!!!!!!");
			} else {// 유효공격이 -이거나 0이면 피해2
				zombie.hp -= 2;
				System.out.println("나는" + zombie.name + "의 급소를 공격하여 2의 치명타 피해를 입혔다!!!!!!!!");
			}
		} else {// 평범한 상황에서의 좀비 공격<---
			AttackBgm attack = new AttackBgm();
			attack.start();
			if (attack_point - zombie.guard_point > 1) {// 유효공격이 1 이상이면
				zombie.hp = zombie.hp - (attack_point - zombie.guard_point);
				System.out.println(
						"나는" + zombie.name + "에게" + (attack_point - zombie.guard_point) + "의 피해를 입혔다.");
			} else {// 유효공격이 -이거나 0이면 피해1
				zombie.hp--;
				System.out.println("나는" + zombie.name + "에게 1의 피해를 입혔다.");
			}
		}
	}
	
	public void showFightDislpay(Zombie zombie) {
		System.out.println("==============================    전 투 상 황       =================================");
		System.out.println(
				"    나의 상태                                                                                                  "
						+ zombie.name + "의 상태");
		System.out.println("");
		System.out.println("현재HP:" + hp + "       현재 MP:"+mp+"                         현재HP:" + zombie.hp);
		System.out.println("공격력:" + attack_point + "                                           공격력:"
				+ zombie.attack_point);
		System.out.println("방어력:" + guard_point + "                                            방어력:"
				+ zombie.guard_point);
		System.out.println("=============================================================================");
	}
	
	public void showFoundWeapon(Weapon weapon) {//무기아이템을 발견했을 때 메시지
		System.out.println(weapon.name+"(을)를 발견하였습니다.");
		System.out.println("==========발견한 아이템====================착용 중인 아이템==============");
		System.out.println("      " + weapon.name
				+ "                                                            " + Character.weapon.name);
		System.out.println("   공격력   " + weapon.attack_point + "                              "
				+ Character.weapon.attack_point);
		System.out.println("   무게   " + weapon.weight + "                              "
				+ Character.weapon.weight);
		System.out.println("===============================================================");
		System.out.println("발견한 아이템을 착용하시겠습니까?");
		System.out.println("1.착용한다        2.착용하지 않는다.");
	}
	
	public void showFoundArmor(Armor armor) {//방어구 아이템을 발견했을 때 메시지
		System.out.println(armor.name+"(을)를 발견하였습니다.");
		System.out.println("==========발견한 아이템====================착용 중인 아이템==============");
		System.out.println("      " + armor.name
				+ "                                                            " + Character.armor.name);
		System.out.println("   방어력   " + armor.guard_point
				+ "                              " + Character.armor.guard_point);
		System.out.println("   무게   " + armor.weight + "                              "
				+ Character.armor.weight);
		System.out.println("===============================================================");
		System.out.println("발견한 아이템을 착용하시겠습니까?");
		System.out.println("1.착용한다        2.착용하지 않는다.");
	}
	
	public void showFoundBag(Bag bag) {//가방 아이템을 발견했을 때 메시지
		System.out.println(bag.name+"(을)를 발견하였습니다.");
		System.out.println("==========발견한 아이템====================착용 중인 아이템==============");
		System.out.println("      " + bag.name
				+ "                                                  " + Character.bag.name);
		System.out.println(" 소지한도   " + bag.weight_limit + "                              "
				+ Character.bag.weight_limit);
		System.out.println("===============================================================");
		System.out.println("발견한 아이템을 착용하시겠습니까?");
		System.out.println("1.착용한다        2.착용하지 않는다.");
	}
	
	public void showFoundVehicle(Vehicle vehicle) {
		System.out.println(vehicle.name+"(을)를 발견하였습니다.");
		System.out.println("==========발견한 아이템====================착용 중인 아이템==============");
		System.out.println(
				"      " + vehicle.name + "                                                  "
						+ Character.vehicle.name);
		System.out.println("   행동력   " + vehicle.move_point + "                              "
				+ Character.vehicle.move_point);
		System.out.println("   무게   " + vehicle.weight + "                              "
				+ Character.vehicle.weight);
		System.out.println("===============================================================");
		System.out.println("발견한 아이템을 착용하시겠습니까?");
		System.out.println("1.착용한다        2.착용하지 않는다.");
	}
	
	public void help() {//도움말
		System.out.println("============================특수 아이템 설명=============================");
		System.out.println("1.경찰방패: 경찰의 의지 스킬을 사용할 수 있게 됩니다.");
		System.out.println("2.더러운조끼: 회피율이 30 증가합니다.");
		System.out.println("3.수술용메스: 상처내기 스킬을 사용할 수 있게 됩니다.");
		System.out.println("4.죽도: 발도술 스킬을 사용할 수 있게 됩니다.");
		System.out.println("5.총: 조준 사격 스킬을 사용할 수 있게 됩니다.");
		System.out.println("6.대퇴골: 치명타율이 30 증가합니다.");
		System.out.println("7.갈비뼈: 뼈던지기 스킬을 사용할 수 있게 됩니다.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("============================스킬 설명=================================");
		System.out.println("1.발도술: 현재 공격력의 2배로 고정피해를 입힙니다.");
		System.out.println("2.회복: 0-10사이의 체력을 회복합니다.");
		System.out.println("3.속박: 좀비의 움직임을 3턴 제한합니다.");
		System.out.println("4.경찰의 의지: 5턴동안 방어력을 공격력으로 전환합니다");
		System.out.println("5.홈런: 50%의 확률로 상대방을 즉사시킵니다.");
		System.out.println("6.적시타: 좀비의 체력을 50% 감소시킵니다.");
		System.out.println("7.상처내기: 좀비에게 (0-공격력)의 지속적인 피해를 입힙니다.");
		System.out.println("8.조준사격: 3턴동안 기본공격이 치명타로 전환됩니다.");
		System.out.println("9.뼈던지기: 기본공격력+1의 피해를 좀비에게 입힙니다.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("========================세트아이템 효과=================================");
		System.out.println("1.의사: 회복 스킬을 사용할 수 있게 됩니다.");
		System.out.println("2.경찰: 속박 스킬을 사용할 수 있게 됩니다.");
		System.out.println("3.야구: 홈런 스킬과 적시타 스킬을 사용할 수 있게 됩니다.");
		System.out.println("===================================================================");
		System.out.println("");
		System.out.println("=============================상태이상=================================");
		System.out.println("1.출혈: 지속적으로 데미지를 추가적으로 입게됩니다. 시간의 흐름에 따라 피해량이 감소합니다.");
		System.out.println("2.전투불능: 일정 시간동안 자동으로 턴을 소비합니다. 행동을 취할 수 없습니다.");
		System.out.println("3.경험축적의 방해: 좀비를 죽여도 경험치를 획득하지 못합니다.");
		System.out.println("4.약화: 공격력이 1 감소합니다.약화상태는 전투가 끝날때까지 누적됩니다.");
		System.out.println("===================================================================");
		System.out.println();
		
		
		
	}
	
	public int bossFight(Zombie zombie) {//전투를 시작한다.
		Scanner scan= new Scanner(System.in);
		Random random = new Random();
		replace_attack = attack_point;//현재 공격력 저장
		replace_guard = guard_point;//현재 공격력 저장
		int replace_police_attack=0;
		int replace_critical_rate=critical_rate;
		int num;
		int num_1 = 0;
		int bindcount=0;//속박카운트
		int police_power_count=0;//경찰의의지 카운트
		int stun_count=0;//스턴카운트
		int weakness_count=0;//약화 카운트
		int blood_point=0;
		int critical_count=0;
		boolean escape=false;
		boolean curse=false;
		boolean zombie_blood=false;
		
		showFightDislpay(zombie);// 먼저 현재 상태창을 보여준다.
		do {
			do {
				//스킬 쿨타임 감소
				if (num_1 != 0) {
					for (int i = 0; i < skill_list.size(); i++) {
						if (skill_list.get(i).cooltime > 0)
							skill_list.get(i).cooltime--;
					}
				}
				if(police_power_count>0) {//버프 유지시 사용효과적용
					System.out.println("경찰의 의지가 "+(police_power_count)+"턴 남았습니다.");
					attack_point+=replace_guard;
					guard_point-=replace_guard;
				}
				if (stun_count > 0) {//스턴 진행중이면
					System.out.println("전투 불능 상태입니다.남은 횟수:" + stun_count);
					stun_count--;
				} else {//스턴이 풀려있으면
					System.out.println("1.기본공격      2.스킬사용      3.소비아이템 확인      4.도망");
					num_1 = scan.nextInt();
					switch (num_1) {
					case 1:// 기본공격
						if(critical_count>0) {//조준사격 적용되었으면
							critical_rate=100;
							attackCharToZom(zombie);
							critical_count--;
							System.out.println("조준사격이  "+critical_count+"회 남았습니다.");
						}else {
							critical_rate=replace_critical_rate;
							attackCharToZom(zombie);
						}
						break;
					case 2:// 스킬사용
						showSkillList();
						System.out.println("사용할 스킬의 번호를 입력하세요.");
						num = scan.nextInt();
						try {
							if (skill_list.get(num - 1).name == "발도술") {// 스킬 구분하고 사용
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("발도술을 사용합니다.");
									zombie.hp -= attack_point * 2;// 공격력*2 만큼의 고정피해를 입힌다.
									System.out.println(
											"발도술을 사용하여 " + zombie.name + "에게 " + attack_point * 2 + "의 고정피해를 입혔다!!!!!");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "회복") {
								int addhp = random.nextInt(11);
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true && hp < 100) {// 사용 가능하면
									System.out.println("회복을 사용합니다.");
									hp += addhp;// 랜덤의 체력을 회복한다.
									System.out.println("회복을 사용하여 " + addhp + "의 체력을 회복했다!!!!!");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
									if (hp > 100) {// 만피제한
										hp = 100;
									}
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "속박") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("속박을 사용합니다.");
									bindcount += 3;
									System.out.println("상대방은 3턴동안 행동을 취하지 못합니다.");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "경찰의 의지") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("경찰의 의지를 사용합니다.");
									System.out.println("5턴동안 방어력을 공격력으로 전환합니다.");
									police_power_count = 6;
									attack_point += replace_guard;
									guard_point -= replace_guard;
									replace_police_attack=attack_point;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "홈런") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("홈런을 사용합니다.");
									if(random.nextInt(100)<50) {
										zombie.hp=0;
										System.out.println("홈런에 성공하였습니다!!!!!!!!!!");
									}else {
										System.out.println("홈런에 실패하였습니다........");
									}
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "적시타") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("적시타를 사용합니다.");
									System.out.println("좀비의 체력이 절반으로 줄어듭니다.");
									zombie.hp-=zombie.hp/2;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "상처내기") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("상처내기를 사용합니다.");
									System.out.println("좀비는 지속적으로 추가 출혈 피해를 입습니다.");
									zombie_blood=true;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "조준사격") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("조준사격을 사용합니다.");
									System.out.println("앞으로 3턴의 기본공격은 치명타 판정을 받습니다..");
									critical_count=3;
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							} else if (skill_list.get(num - 1).name == "뼈던지기") {
								skill_list.get(num - 1).checkCanUseSkill(this);// 스킬 사용가능한지 상태 확인하고
								if (skill_list.get(num - 1).canuse == true) {// 사용 가능하면
									System.out.println("뼈던지기를 사용합니다.");
									zombie.hp-=(attack_point+1);
									System.out.println((attack_point+1)+"의 피해를 입힙니다.");
									skill_list.get(num - 1).useSkill(this);// 스킬을 발동한다.
								} else {// 사용 불가능하면
									System.out.println("스킬을 사용할 수 없습니다. 전투화면으로 되돌아갑니다.");
									showFightDislpay(zombie);
									num_1 = 0;// 다시
								}
							}
						} catch (IndexOutOfBoundsException e) {
							System.out.println("올바른 스킬이 아닙니다. 전투화면으로 돌아갑니다.");
							showFightDislpay(zombie);
							num_1 = 0;// 다시
						}
						break;
					case 3:// 소비아이템 확인
						showFightInventory();
						break;
					case 4:// 도망간다
						System.out.println("도망갈 수 없습니다.");
						break;
					default:
						System.out.println();
					}
					if (Character.armor.name=="더러운조끼") {
						avoid_rate=40;
					}else {
						avoid_rate=10;
					}
				}
			} while (num_1 != 1 && num_1 != 2 && num_1 != 3 && num_1 != 4);
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(zombie_blood==true) {
				int blood=random.nextInt(attack_point);
				System.out.println("좀비는 "+blood+"의 출혈 데미지를 입습니다.");
				zombie.hp-=blood;
			}
			if(blood_point>0) {//출혈상태에 있다면
				hp-=blood_point;
				System.out.println("지속적인 출혈로"+blood_point+"만큼의 데미지를 입습니다.");
				blood_point--;
			}
			showFightDislpay(zombie);
			zombie.checkDead();
			if (zombie.dead == true) {
				System.out.println(zombie.name + "가 죽었다.전투에서 승리했다.");
				if(zombie.name=="대장좀비"&&random.nextInt(100)<zombie.weapon.find_rate) {//사망시 아이템 드랍
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="더러운좀비"&&random.nextInt(100)<zombie.armor.find_rate) {//사망시 아이템 드랍
					showFoundArmor(zombie.armor);
					doArmor(zombie.armor);
				}
				if(zombie.name=="여자좀비"&&random.nextInt(100)<zombie.weapon.find_rate) {//사망시 아이템 드랍
					showFoundWeapon(zombie.weapon);
					doWeapon(zombie.weapon);
				}
				if(zombie.name=="여자좀비"||zombie.name=="남자좀비"||zombie.name=="동물좀비"||zombie.name=="대장좀비") {//사망시 아이템 드랍
					if(random.nextInt(100)<zombie.ticket.find_rate) {
						ticket_have=true;
						System.out.println("지하철 티켓을 획득하였습니다!!!! 지하철에 입장하실 수 있습니다.");
					}
				}
				if(curse==false) {
					System.out.println("경험치를 " + zombie.exp + "획득했다.");
				}else {
					System.out.println("경험치를 획득할 수 없습니다.");
				}
				break;
			}
			if(police_power_count>0)//경찰버프 감소
				police_power_count--;
			if(police_power_count==0&&weakness_count==0) {//경찰버프 끝났으면 초기화
				attack_point = replace_attack;
				guard_point = replace_guard;
				police_power_count--;
			}
			if(police_power_count==0&&weakness_count>0) {//경찰버프 끝났으면 초기화
				attack_point -= replace_guard;
				guard_point += replace_guard;
				police_power_count--;
			}
			
			
			
			if(bindcount==0) {//좀비턴
		
				zombie.attackZomToChar(this);//좀비의 공격및 여러가지스킬

				if(zombie.name=="최종좀비"&&random.nextInt(100)<30) {//더러운좀비일 때 30%확률로 디버프 건다.
					System.out.println("최종좀비의 분비물이 전투 경험의 축적을 방해합니다.");
					curse=true;
				}
				if(zombie.name=="최종좀비"&&random.nextInt(100)<20) {//괴성좀비일 때 20%확률로 디버프 건다.
					System.out.println("최종좀비의 소름끼치는 목소리가 나를 전투 붙능 상태로 만듭니다.");
					System.out.println("공격을 회피할 수 없습니다.");
					avoid_rate=0;
					stun_count=3;
				}
				if(zombie.name=="최종좀비"&&random.nextInt(100)<10) {//악취좀비일 때 20%확률로 디버프 건다.
					System.out.println("최종좀비의 썩은 냄새가 나를 약화 상태로 만듭니다.");
					System.out.println("공격력이 1 감소합니다.");
					attack_point--;
					if(attack_point<0)
						attack_point=0;
					
				}
				if(zombie.name=="최종좀비"&&random.nextInt(100)<20) {//변이좀비일 때 20%확률로 디버프 건다.
					System.out.println("최종좀비의 강력한 공격이 나를 출혈 상태로 만듭니다.");
					System.out.println("시간이 지남에 따라 출혈 데미지가 줄어듭니다..");
					blood_point=3;
				}
				if(zombie.name=="최종좀비"&&random.nextInt(100)<50) {//대장좀비일 때 50%확률로 디버프 건다.
					System.out.println("최종좀비의 알수 없는 공격이 나의 기운을 빼앗는다.");
					System.out.println("mp가 5 감소합니다.");
					mp-=5;
				}
				
				
				
				
			}else {
				bindcount--;
				System.out.println("좀비가 속박되어 행동을 취할 수 없습니다. 남은횟수:"+bindcount);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showFightDislpay(zombie);
			if (police_power_count > 0) {//경찰의 의지 버프 남아있으면 다시초기화
				attack_point -= replace_guard;
				guard_point += replace_guard;
			}
			checkDead();
			if (dead == true)
			break;
		} while (true);
		attack_point = replace_attack;//버프해제
		guard_point = replace_guard;//버프해제
		zombie.returnZombie();// 좀비 초기화
		if(curse==true||escape==true) {//저주에 걸렸거나 도망쳤으면 경험치 0
			return 0;
		}else {//저주 안걸렸을 시에 경험치획득
			return zombie.exp;
		}
	}	
}
