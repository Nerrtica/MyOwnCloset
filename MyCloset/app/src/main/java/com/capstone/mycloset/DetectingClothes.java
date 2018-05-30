package com.capstone.mycloset;

import android.content.Context;

import java.util.ArrayList;

public class DetectingClothes<E> {
    private ArrayList<Closet> clothes = new ArrayList<>();

    private ArrayList<Closet> topClothes = new ArrayList<>();
    private ArrayList<Closet> bottomClothes = new ArrayList<>();
    private ArrayList<Closet> shoes = new ArrayList<>();
    private ArrayList<Closet> outerWear = new ArrayList<>();
    private ArrayList<Closet> needOuterWearClothes = new ArrayList<Closet>();

    private ArrayList<BestSet.BestSetItem> bestSetItemArrayList;

    private ArrayList<Coordi> randSetArrayList;

    float weatherMinTem;
    float weatherMaxTem;
    float clothesMaxTem = 30;
    float clothesMinTem = 0;

    public DetectingClothes(Context context, float minTem, float maxTem) {
        DBController controller ;
        controller = new DBController(context);
        this.clothes = controller.FindCloset();
        this.weatherMinTem = minTem;
        this.weatherMaxTem = maxTem;

        classificationClothesType();
        BestSet bestSet = new BestSet(context);
        bestSet.initSet();
        bestSetItemArrayList = bestSet.getBestSetArray();

        makeRandomSet();
    }

    public Coordi getCoordi() {
        return randSetArrayList.get(0);
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
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 4:
                    //스웨터
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 5:
                    //셔츠
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 6:
                    //T-shirt
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 7:
                    //청바지
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 8:
                    //면바지
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
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
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 12:
                    //블라우스
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }
                    break;
                case 13:
                    //치마
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
                    }

                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem, clothesMinTem);
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
            if(weatherMaxTem - clothesMaxTem < 3) {
                boolean OuterWearBoolean = checkBringOuterWear((weatherMaxTem+weatherMinTem)/2);
                if (OuterWearBoolean) {
                    needOuterWearClothes.add(clothes);
                }
                else topClothes.add(clothes);
            }
        }
    }

    private void checkTemBottomClothes(Closet clothes,float clothesMaxTem,float clothesMinTem) {
        if(weatherMinTem - clothesMinTem > -3) {
            if(weatherMaxTem - clothesMaxTem < 3) {
                bottomClothes.add(clothes);
            }
        }
    }

    private boolean checkBringOuterWear(float clothesMidTem) {
        if(weatherMinTem < 12) return true;
        else if(clothesMidTem - weatherMinTem >5)return true;
        else return false;
    }

    private void makeRandomSet() {
        if(randSetArrayList == null) {
            randSetArrayList = new ArrayList<Coordi>();
        }
        for(BestSet.BestSetItem bestSetItem : bestSetItemArrayList) {
            if(bestSetItem.getIncludeOuter()) {
                for (Closet outer : outerWear) {
                    if(bestSetItem.getOuterType() == outer.getType()) {
                        for(Closet top : topClothes) {
                            if(bestSetItem.getTopType() == top.getType()) {
                                for(Closet bottom : bottomClothes) {
                                    if(bestSetItem.getBottomType() == bottom.getType()) {
                                        for(Closet shoes : this.shoes) {
                                            if(bestSetItem.getShoesType() == shoes.getType()) {
                                                randSetArrayList.add(new Coordi(outer.getId(), top.getId(), bottom.getId(), shoes.getId()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                for(Closet top : topClothes) {
                    if(bestSetItem.getTopType() == top.getType()) {
                        for(Closet bottom : bottomClothes) {
                            if(bestSetItem.getBottomType() == bottom.getType()) {
                                for(Closet shoes : this.shoes) {
                                    if(bestSetItem.getShoesType() == shoes.getType()) {
                                        randSetArrayList.add(new Coordi(top.getId(), bottom.getId(), shoes.getId()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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