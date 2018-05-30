package com.capstone.mycloset;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class BestSet {
    private ArrayList<BestSetItem> bestSetArrayList;
    private String gender;

    public BestSet(Context context) {
        bestSetArrayList = new ArrayList<BestSetItem>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        gender = preferences.getString("key_gender", "-1");
    }

    public void initSet() {
        bestSetArrayList.add(new BestSetItem(3, 7, 9));
        bestSetArrayList.add(new BestSetItem(4, 8, 10));
        bestSetArrayList.add(new BestSetItem(5, 8, 10));
        bestSetArrayList.add(new BestSetItem(6, 7, 9));
//        bestSetArrayList.add(new BestSetItem(0, 4, 8, 10));
//        bestSetArrayList.add(new BestSetItem(1, 6, 7, 9));
//        bestSetArrayList.add(new BestSetItem(3, 6, 7, 9));
        if(gender.compareTo("1") == 0) {
            bestSetArrayList.add(new BestSetItem(4, 13, 9));
            bestSetArrayList.add(new BestSetItem(4, 13, 10));
            bestSetArrayList.add(new BestSetItem(4, 13, 14));
            bestSetArrayList.add(new BestSetItem(5, 13, 9));
            bestSetArrayList.add(new BestSetItem(5, 13, 10));
            bestSetArrayList.add(new BestSetItem(5, 13, 14));
            bestSetArrayList.add(new BestSetItem(11, 8, 9));
            bestSetArrayList.add(new BestSetItem(11, 8, 10));
            bestSetArrayList.add(new BestSetItem(11, 8, 14));
            bestSetArrayList.add(new BestSetItem(12, 7, 9));
            bestSetArrayList.add(new BestSetItem(12, 7, 10));
            bestSetArrayList.add(new BestSetItem(12, 8, 9));
            bestSetArrayList.add(new BestSetItem(12, 8, 10));
        }
    }

    public ArrayList<BestSetItem> getBestSetArray() {
        return this.bestSetArrayList;
    }

    public class BestSetItem {
        private int OUTER_TYPE, TOP_TYPE, BOTTOM_TYPE, SHOES_TYPE;
        //    private boolean TOP_LONG, BOTTOM_LONG;
        private boolean INCLUDE_OUTER;

        public BestSetItem(int TOP_TYPE, int BOTTOM_TYPE, int SHOES_TYPE) {
            this.TOP_TYPE = TOP_TYPE;
            this.BOTTOM_TYPE = BOTTOM_TYPE;
            this.SHOES_TYPE = SHOES_TYPE;
            this.INCLUDE_OUTER = false;
        }

        public BestSetItem(int OUTER_TYPE, int TOP_TYPE, int BOTTOM_TYPE, int SHOES_TYPE) {
            this.OUTER_TYPE = OUTER_TYPE;
            this.TOP_TYPE = TOP_TYPE;
            this.BOTTOM_TYPE = BOTTOM_TYPE;
            this.SHOES_TYPE = SHOES_TYPE;
            this.INCLUDE_OUTER = true;
        }

        public boolean getIncludeOuter() {
            return INCLUDE_OUTER;
        }

        public int getOuterType() {
            return OUTER_TYPE;
        }

        public int getTopType() {
            return TOP_TYPE;
        }

        public int getBottomType() {
            return BOTTOM_TYPE;
        }

        public int getShoesType() {
            return SHOES_TYPE;
        }
    }
}
