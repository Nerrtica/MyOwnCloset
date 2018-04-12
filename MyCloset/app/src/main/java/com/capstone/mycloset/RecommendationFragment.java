package com.capstone.mycloset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class RecommendationFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Runnable loadFinishRunnable;
    private Handler loadingHandler;

    private ToggleButton favoriteButton;

    private List<FashionItem> fashionItemList;
//
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        super.onCreate(savedInstanceState);

        favoriteButton = (ToggleButton) view.findViewById(R.id.favorite_button);
        favoriteButton.setChecked(true);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriteButton.isChecked()) {
                    favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                } else {
                    favoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
        });

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

    private void setFashionItemList() {
        fashionItemList = new ArrayList<>();

        fashionItemList.add(new FashionItem("상의", "니트", R.drawable.ic_empty));
        fashionItemList.add(new FashionItem("하의", "슬렉스", R.drawable.ic_empty));
        fashionItemList.add(new FashionItem("상의", "니트", R.drawable.ic_empty));
        fashionItemList.add(new FashionItem("하의", "슬렉스", R.drawable.ic_empty));
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
