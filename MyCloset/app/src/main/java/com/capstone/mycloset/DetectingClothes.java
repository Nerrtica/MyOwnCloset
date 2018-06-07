package com.capstone.mycloset;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DetectingClothes<E> {
    private ArrayList<Closet> clothes = new ArrayList<>();

    private ArrayList<Closet> topClothes = new ArrayList<>();
    private ArrayList<Closet> bottomClothes = new ArrayList<>();
    private ArrayList<Closet> shoes = new ArrayList<>();
    private ArrayList<Closet> outerWear = new ArrayList<>();
    private ArrayList<Closet> needOuterWearClothes = new ArrayList<Closet>();

    private ArrayList<BestSet.BestSetItem> bestSetItemArrayList;

    private ArrayList<Coordi> randSetArrayList;

    private Integer FirsttearColor[] = {3,8,10};
    private Integer SecondtearColor[] = {9,7};
    private Integer ThirdtearColor[] = {0,1,2,5};
    private Integer FourthtearColor[] = {4,6};

    private boolean needOuter;

    float weatherMinTem;
    float weatherMaxTem;
    float clothesMaxTem = 99;
    float clothesMinTem = -20;

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

        needOuter = false;

        makeRandomSet();
        FilterColor();
        FilterPattern();
    }

    public Coordi getCoordi() {
        Random random = new Random();

        if(randSetArrayList.size() == 0) {
            return null;
        }
        return randSetArrayList.get(random.nextInt(randSetArrayList.size()));
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
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float) 18.0, (float)9.0);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)18.0);
                    }
                    break;
                case 4:
                    //스웨터
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float)12.0, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), (float)27.0, (float)21.0);
                    }
                    break;
                case 5:
                    //셔츠
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float)27.0, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)20.0);
                    }
                    break;
                case 6:
                    //T-shirt
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float)25.0, (float)5.0);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), clothesMaxTem, (float)20.0);
                    }
                    break;
                case 7:
                    //청바지
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemBottomClothes(clothes.get(i), (float)27, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemBottomClothes(clothes.get(i), clothesMaxTem , (float)21.0);
                    }
                    break;
                case 8:
                    //면바지
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemBottomClothes(clothes.get(i), (float)27, clothesMinTem);
                    }
                    else if(clothes.get(i).isLong() ==0) {
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
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float)22.0,(float)10.0);
                    }
                    else if(clothes.get(i).isLong() ==0) {
                        //짧은팔
                        checkTemTopClothes(clothes.get(i), (float)18.0, (float)25.0);
                    }
                    break;
                case 12:
                    //블라우스
                    if(clothes.get(i).isLong() ==1) {
                        //긴팔
                        checkTemTopClothes(clothes.get(i), (float)22.0, clothesMinTem);
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
                        checkTemBottomClothes(clothes.get(i), (float) 20.0, clothesMinTem);
                    }

                    else if(clothes.get(i).isLong() ==0) {
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

    private int returnNowClothesColor(int id) {
        for(int i=0;i<clothes.size();i++) {
            if(clothes.get(i).getId() == id) return clothes.get(i).getColor();
        }
        return -1;
    }

    private int returnNowClothesPattern(int id) {
        for(int i=0;i<clothes.size();i++) {
            if(clothes.get(i).getId() == id) return clothes.get(i).getPattern();
        }
        return -1;
    }

    private void FilterPattern() {
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        for (int i = randSetArrayList.size() - 1; i >= 0; i--) {
            Coordi now = randSetArrayList.get(i);

            int topPattern = returnNowClothesPattern(now.getTop());
            int buttomPattern = returnNowClothesPattern(now.getBottom());
            if(topPattern == 0 || buttomPattern == 0) {
                continue;
            } else {
                if(topPattern != buttomPattern) {
                    indexList.add(i);
                }
            }
        }
        for(int i = 0; i < indexList.size(); i++) {
            int idx = indexList.get(i);
            randSetArrayList.remove(idx);
        }
    }

    private void FilterColor() {
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        List firstColor = Arrays.asList(FirsttearColor);
        List secondColor = Arrays.asList(SecondtearColor);
        List thirdColor = Arrays.asList(ThirdtearColor);

        for (int i = randSetArrayList.size() - 1; i >= 0; i--) {
            Coordi now = randSetArrayList.get(i);
            int nowTopColor = returnNowClothesColor(now.getTop());
            int nowBottomColor = returnNowClothesColor(now.getBottom());
            int nowOuterWearColor = returnNowClothesColor(now.getOuter());

            if (now.getOuter() == -1) {
                if (firstColor.contains(nowTopColor)) {
                    //when top color is same in bottom color
                    if (nowTopColor== nowBottomColor) {
                        indexList.add(i);
                    } else if (firstColor.contains(nowBottomColor)) {
                    } else if (secondColor.contains(nowBottomColor)) {
                    } else {
                        indexList.add(i);
                    }
                } else if (secondColor.contains(nowTopColor)) {
                    //when top color is same in bottom color
                    if (nowTopColor == nowBottomColor) {
                        indexList.add(i);
                    } else if (firstColor.contains(nowBottomColor)) {
                    } else if (secondColor.contains(nowBottomColor)) {
                    } else {
                        indexList.add(i);
                    }
                } else if (thirdColor.contains(nowTopColor)) {
                    if (firstColor.contains(nowBottomColor)) {

                    } else if (secondColor.contains(nowBottomColor)) {

                    } else {
                        indexList.add(i);
                    }
                }

                //4tear color
                else if (Arrays.asList(FourthtearColor).contains(nowTopColor)) {
                    if (firstColor.contains(nowBottomColor)) {

                    } else {
                        indexList.add(i);
                    }
                }
            }
            //outerWear is needed
            else {
                //outerWeae is white
                if (firstColor.contains(nowOuterWearColor)) {
                    if (nowOuterWearColor == 8) {
                        indexList.add(i);
                    }
                    //outer color is same in top color
                    else if (nowOuterWearColor == nowTopColor) {
                        indexList.add(i);
                    }
                    //outer wear color is first tear
                    else if (firstColor.contains(nowOuterWearColor)) {
                        if (firstColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        } else if (secondColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        } else if (thirdColor.contains(nowTopColor)) {
                            if (firstColor.contains(nowBottomColor)) {

                            } else if (secondColor.contains(nowBottomColor)) {

                            } else {
                                indexList.add(i);
                            }
                        }

                        //4tear color
                        else if (Arrays.asList(FourthtearColor).contains(nowTopColor)) {
                            if (firstColor.contains(nowBottomColor)) {

                            } else {
                                indexList.add(i);
                            }
                        }
                    }
                    //outer wear color is Second tear
                    else if (secondColor.contains(nowOuterWearColor)) {
                        if (firstColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        } else if (secondColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        } else if (thirdColor.contains(nowTopColor)) {
                            if (firstColor.contains(nowBottomColor)) {

                            } else if (secondColor.contains(nowBottomColor)) {

                            } else {
                                indexList.add(i);
                            }
                        }

                    }
                    //outer wear color is Third tear
                    else if (thirdColor.contains(nowOuterWearColor)) {
                        if (firstColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        } else if (secondColor.contains(nowTopColor)) {
                            //when top color is same in bottom color
                            if (nowTopColor == nowBottomColor) {
                                indexList.add(i);
                            } else if (firstColor.contains(nowBottomColor)) {
                            } else if (secondColor.contains(nowBottomColor)) {
                            } else {
                                indexList.add(i);
                            }
                        }

                    }
                }
            }
        }
        for(int i = 0; i < indexList.size(); i++) {
            int idx = indexList.get(i);
            randSetArrayList.remove(idx);
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
        if (weatherMinTem < 12) {
            needOuter = true;
            return true;
        }
        else if (clothesMinTem - weatherMinTem > 2) {
            needOuter = true;
            return true;
        } else if ((weatherMaxTem + weatherMinTem) < 40 && (weatherMaxTem + weatherMinTem) > 20) {
            if ((weatherMaxTem - weatherMinTem) > 10) {
                return true;
            }
        }
        return false;
    }

    private void makeRandomSet() {
        if(randSetArrayList == null) {
            randSetArrayList = new ArrayList<Coordi>();
        }
        for(BestSet.BestSetItem bestSetItem : bestSetItemArrayList) {
            if(needOuter) {
                if (bestSetItem.getIncludeOuter()) {
                    for (Closet outer : outerWear) {
                        if (bestSetItem.getOuterType() == outer.getType()) {
                            for (Closet top : topClothes) {
                                if (bestSetItem.getTopType() == top.getType()) {
                                    for (Closet bottom : bottomClothes) {
                                        if (bestSetItem.getBottomType() == bottom.getType()) {
                                            for (Closet shoes : this.shoes) {
                                                if (bestSetItem.getShoesType() == shoes.getType()) {
                                                    randSetArrayList.add(new Coordi(outer.getId(), top.getId(), bottom.getId(), shoes.getId()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Closet top : topClothes) {
                        if (bestSetItem.getTopType() == top.getType()) {
                            for (Closet bottom : bottomClothes) {
                                if (bestSetItem.getBottomType() == bottom.getType()) {
                                    for (Closet shoes : this.shoes) {
                                        if (bestSetItem.getShoesType() == shoes.getType()) {
                                            randSetArrayList.add(new Coordi(top.getId(), bottom.getId(), shoes.getId()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (bestSetItem.getIncludeOuter()) {
                    for (Closet outer : outerWear) {
                        if (bestSetItem.getOuterType() == outer.getType()) {
                            for (Closet top : needOuterWearClothes) {
                                if (bestSetItem.getTopType() == top.getType()) {
                                    for (Closet bottom : bottomClothes) {
                                        if (bestSetItem.getBottomType() == bottom.getType()) {
                                            for (Closet shoes : this.shoes) {
                                                if (bestSetItem.getShoesType() == shoes.getType()) {
                                                    randSetArrayList.add(new Coordi(outer.getId(), top.getId(), bottom.getId(), shoes.getId()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Closet top : topClothes) {
                        if (bestSetItem.getTopType() == top.getType()) {
                            for (Closet bottom : bottomClothes) {
                                if (bestSetItem.getBottomType() == bottom.getType()) {
                                    for (Closet shoes : this.shoes) {
                                        if (bestSetItem.getShoesType() == shoes.getType()) {
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