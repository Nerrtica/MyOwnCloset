package com.capstone.mycloset;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecommendationFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Runnable loadFinishRunnable;
    private Handler loadingHandler;

    private ToggleButton favoriteButton;

    private List<FashionItem> fashionItemList;

    private int OUTER_ID, TOP_ID, BOTTOM_ID, SHOES_ID;
    private boolean addDB;

    public static boolean likeBtn = false;

//    public static RecommendationFragment newInstance(int typeCode) {
//        RecommendationFragment recommendationFragment = new RecommendationFragment();
//
//        // Supply index input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("TYPE_CODE", typeCode);
//        recommendationFragment.setArguments(args);
//
//        return recommendationFragment;
//    }

    public static RecommendationFragment newInstance(int outer, int top, int bottom, int shoes) {
        RecommendationFragment recommendationFragment = new RecommendationFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("OUTER_ID", outer);
        args.putInt("TOP_ID", top);
        args.putInt("BOTTOM_ID", bottom);
        args.putInt("SHOES_ID", shoes);
        recommendationFragment.setArguments(args);

        return recommendationFragment;
    }

    public static RecommendationFragment newInstance(int top, int bottom, int shoes) {
        RecommendationFragment recommendationFragment = new RecommendationFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("OUTER_ID", -1);
        args.putInt("TOP_ID", top);
        args.putInt("BOTTOM_ID", bottom);
        args.putInt("SHOES_ID", shoes);
        recommendationFragment.setArguments(args);

        return recommendationFragment;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        addDB = false;

        OUTER_ID = getArguments().getInt("OUTER_ID");
        TOP_ID = getArguments().getInt("TOP_ID");
        BOTTOM_ID = getArguments().getInt("BOTTOM_ID");
        SHOES_ID = getArguments().getInt("SHOES_ID");

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        super.onCreate(savedInstanceState);

        final TextView titleTextView = (TextView) view.findViewById(R.id.fashion_title);
        titleTextView.setText(getDayTitle());

        favoriteButton = (ToggleButton) view.findViewById(R.id.favorite_button);
        if (likeBtn) {
            favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }
        favoriteButton.setChecked(true);
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!likeBtn) {
                    if (isChecked) {
//                    favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    } else {
                        if (!addDB) {
                            favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                            DBController controller;
                            controller = new DBController(getContext());
                            if (OUTER_ID != -1) {
                                controller.InsertCoordi(titleTextView.getText().toString(), OUTER_ID, TOP_ID, BOTTOM_ID, SHOES_ID);
                            } else {
                                controller.InsertCoordi(titleTextView.getText().toString(), TOP_ID, BOTTOM_ID, SHOES_ID);
                            }

//                        RecommendationActivity.refresh = true;
                            onSubmitListener.onLikeSubmit();
                            addDB = true;
                        }
                    }
                }
            }
        });
//        favoriteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(favoriteButton.isChecked()) {
//                    favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
//                } else {
//                    favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
//                    DBController controller ;
//                    controller = new DBController(getContext());
//                    if(OUTER_ID != -1) {
//                        controller.InsertCoordi(titleTextView.getText().toString(), OUTER_ID, TOP_ID, BOTTOM_ID, SHOES_ID);
//                    }
//                    else {
//                        controller.InsertCoordi(titleTextView.getText().toString(), TOP_ID, BOTTOM_ID, SHOES_ID);
//                    }
//                }
//            }
//        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recommendation_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        setFashionItemList();
        recyclerView.setAdapter(new RecommendRecyclerAdapter(getContext(), fashionItemList, R.layout.activity_recommendation));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    public interface SubmitListener {
        void onLikeSubmit();
        void onRefreshSubmit();
    }

    private SubmitListener onSubmitListener;

    public void setSubmitListener(SubmitListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public SubmitListener getOnSubmitListener(){
        return onSubmitListener;
    }

    private String getDayTitle() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1;
        int date = cal.get(cal.DATE);

        StringBuilder dayString = new StringBuilder();
        dayString.append(year);
        dayString.append("-");
        if(month < 10) {
            dayString.append(0);
        }
        dayString.append(month);
        dayString.append("-");
        if(date < 10) {
            dayString.append(0);
        }
        dayString.append(date);

        return dayString.toString();
    }

    private void setFashionItemList() {
        fashionItemList = new ArrayList<>();
        DBController controller ;
        controller = new DBController(getContext());

        Bitmap outerPhoto = null;
        Bitmap topPhoto = null;
        Bitmap bottomPhoto = null;
        Bitmap shoesPhoto = null;

        Closet outerCloset;
        if(OUTER_ID != -1) {
            outerCloset = controller.FindClosetFromID(OUTER_ID);
            try {
                outerPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
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
            topPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                    Uri.parse(getThumFilePath(topCloset.getImagePath())));
            bottomPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                    Uri.parse(getThumFilePath(bottomCloset.getImagePath())));
            shoesPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        String icon = adapterView.getItemAtPosition(i).toString();
//        Intent result = new Intent(null, Uri.withAppendedPath(CONTENT_URI,icon));
//        setResult(RESULT_OK, result);
        Toast.makeText(getContext(), "Test : " + i, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRefresh() {
        loadFinishRunnable = new Runnable() {
            @Override
            public void run() {
                onSubmitListener.onRefreshSubmit();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };

        loadingHandler = new Handler();
        loadingHandler.postDelayed(loadFinishRunnable, 2000);
    }

    private void setResult(String resultOk, Intent result) {
        // TODO Auto-generated method stub

    }

    private void finish() {
        // TODO Auto-generated method stub

    }
}
