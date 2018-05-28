import java.util.ArrayList;
public class DectingClothes<E> {
	private ArrayList<Closet> clothes = new ArrayList<>();
	private ArrayList<Closet> topClothes = new ArrayList<>();
	private ArrayList<Closet> bottomClothes = new ArrayList<>();
	private ArrayList<Closet> shoes = new ArrayList<>();
	private ArrayList<Closet> outerWear = new ArrayList<>();
	private ArrayList<Closet> needOuterWearClothes = new ArrayList<Closet>();
	float weatherMinTem;
	float weatherMaxTem;
	float clothesMaxTem = 99;
	float clothesMinTem = -20;
	public DectingClothes(ArrayList<Closet> list,float minTem,float maxTem) {
		this.clothes = list;
		classificationClothesType();
		this.weatherMinTem = minTem;
		this.weatherMaxTem = maxTem;
		classificationClothesType();
	}
	private void classificationClothesType() {
		for(int i=0;i<clothes.size();i++) {
			switch(clothes.get(i).getType()) {
			case 0:
				//코트
				outerWear.add(clothes.get(i));
				break;
			case 1:
				//재킷
				outerWear.add(clothes.get(i));
				break;
			case 2:
				//정장
				break;
			case 3:
				//후드티
				outerWear.add(clothes.get(i));
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float) 18.0, (float)9.0);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)18.0);
				}
				break;
			case 4:
				//스웨터
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float)12.0, clothesMinTem);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), (float)27.0, (float)21.0);
				}
				break;
			case 5:
				//셔츠
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float)27.0, clothesMinTem);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)20.0);
				}
				break;
			case 6:
				//T-shirt
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float)25.0, (float)5.0);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)20.0);
				}
				break;
			case 7:
				//청바지
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemBottomClothes(clothes.get(i), (float)27, clothesMinTem);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemBottomClothes(clothes.get(i), clothesMaxTem , (float)21.0);
				}
				break;
			case 8:
				//면바지
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemBottomClothes(clothes.get(i), (float)27, clothesMinTem);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemBottomClothes(clothes.get(i), clothesMaxTem , (float)21.0);
				}
				break;
			case 9:
				shoes.add(clothes.get(i));
				break;
			case 10:
				shoes.add(clothes.get(i));
				break;
			case 11:
				//드레스
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float)22.0,(float)10.0);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), (float)18.0, (float)25.0);
				}
				break;
			case 12:
				//블라우스
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemTopClothes(clothes.get(i), (float)22.0, clothesMinTem);
				}
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
				}
				break;
			case 13:
				//치마
				if(clothes.get(i).getIsLong() ==1) {
					//긴팔
					checkTemBottomClothes(clothes.get(i), (float) 20.0, clothesMinTem);
				}
				
				else if(clothes.get(i).getIsLong() ==0) {
					//짧은팔
					checkTemBottomClothes(clothes.get(i), clothesMaxTem, (float) 12.0);
				}
				break;
			case 14:
				shoes.add(clothes.get(i));
				break;
			}
			
		}
	}
	private void checkTemTopClothes(Closet clothes,float clothesMaxTem,float clothesMinTem) {
		if(weatherMinTem - clothesMinTem > -3) {
			if(weatherMaxTem - clothesMaxTem < 0) {
				boolean OuterWearBoolean = checkBringOuterWear(clothesMinTem);
				if (OuterWearBoolean) {
					needOuterWearClothes.add(clothes);
				}
				else topClothes.add(clothes);
			}
		}
		
		
	}
	private void checkTemBottomClothes(Closet clothes,float clothesMaxTem,float clothesMinTem) {
		if(weatherMinTem - clothesMinTem > 0) {
			if(weatherMaxTem - clothesMaxTem < 0) {
				bottomClothes.add(clothes);
			}
		}
	}
	private boolean checkBringOuterWear(float clothesMinTem) {
		if(weatherMinTem < 12) return true;
		else if(clothesMinTem - weatherMinTem >2) {
			return true;
		}
		else if((weatherMaxTem+weatherMinTem) < 40 &&(weatherMaxTem+weatherMinTem) > 20){
			if((weatherMaxTem - weatherMinTem) >5 ) return true;
		}
		return false;
	}
	public ArrayList<Closet> getTop(){
		return topClothes;
	}
	public ArrayList<Closet> getBottom(){
		return bottomClothes;
	}
	public ArrayList<Closet> getShoes(){
		return shoes;
	}
	public ArrayList<Closet> needOuterWearList() {
		return needOuterWearClothes;
	}
	
}
