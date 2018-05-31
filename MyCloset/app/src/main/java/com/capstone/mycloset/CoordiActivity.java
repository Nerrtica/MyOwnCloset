package com.capstone.mycloset;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoordiActivity extends AppCompatActivity {

    private List<FashionItem> fashionItemList;
    private int OUTER_ID, TOP_ID, BOTTOM_ID, SHOES_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordi);

        int coordiID = getIntent().getIntExtra("ID", 0);
        DBController controller ;
        controller = new DBController(this);
        Coordi coordi = controller.FindCoordi(coordiID);
        OUTER_ID = coordi.getOuter();
        TOP_ID = coordi.getTop();
        BOTTOM_ID = coordi.getBottom();
        SHOES_ID = coordi.getShoes();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.coordi_recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        setFashionItemList();
        mRecyclerView.setAdapter(new RecommendRecyclerAdapter(this, fashionItemList, R.layout.activity_recommendation));
    }

    private void setFashionItemList() {
        fashionItemList = new ArrayList<>();
        DBController controller ;
        controller = new DBController(this);

        Bitmap outerPhoto = null;
        Bitmap topPhoto = null;
        Bitmap bottomPhoto = null;
        Bitmap shoesPhoto = null;

        Closet outerCloset;
        if(OUTER_ID > 0) {
            outerCloset = controller.FindClosetFromID(OUTER_ID);
            try {
                outerPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        Uri.parse(getThumFilePath(outerCloset.getImagePath())));
                fashionItemList.add(new FashionItem("외투", getClosetTypeName(outerCloset.getType()), outerPhoto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Closet topCloset = controller.FindClosetFromID(TOP_ID);
        Closet bottomCloset = controller.FindClosetFromID(BOTTOM_ID);
        Closet shoesCloset = controller.FindClosetFromID(SHOES_ID);

        try {
            topPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    Uri.parse(getThumFilePath(topCloset.getImagePath())));
            bottomPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    Uri.parse(getThumFilePath(bottomCloset.getImagePath())));
            shoesPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    Uri.parse(getThumFilePath(shoesCloset.getImagePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fashionItemList.add(new FashionItem("상의", getClosetTypeName(topCloset.getType()), topPhoto, topCloset.getColor()));
        fashionItemList.add(new FashionItem("하의", getClosetTypeName(bottomCloset.getType()), bottomPhoto, bottomCloset.getColor()));
        fashionItemList.add(new FashionItem("신발", getClosetTypeName(shoesCloset.getType()), shoesPhoto, shoesCloset.getColor()));
    }

    private String getThumFilePath(String imagePath) {
        String fileName = imagePath;
        int idx = fileName.indexOf("MyCloset/");
        String folder = fileName.substring(0, idx + 9);
        fileName = fileName.substring(idx + 9);
        fileName = "thum_" + fileName;

        return folder + fileName;
    }

    private String getClosetTypeName(int type) {
        String[] array = getResources().getStringArray(R.array.closet_type);

        return array[type];
    }
}
