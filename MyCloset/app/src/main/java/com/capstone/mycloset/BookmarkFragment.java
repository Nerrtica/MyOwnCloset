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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment implements AdapterView.OnItemClickListener, BookmarkRecyclerAdapter.BookmarkListener {

    private List<BookmarkItem> bookmarkItemList;
    private BookmarkRecyclerAdapter recyclerAdapter;
    private  RecyclerView recyclerView;
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

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.bookmark_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        setFashionItemList();
        recyclerAdapter = new BookmarkRecyclerAdapter(getContext(), bookmarkItemList, R.layout.activity_recommendation);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setSubmitListener(this);

        return view;
    }

    @Override
    public void onRefreshSubmit() {
        setFashionItemList();
        recyclerAdapter = new BookmarkRecyclerAdapter(getContext(), bookmarkItemList, R.layout.activity_recommendation);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public interface BookmarkListener {
        void onRefreshSubmit();
    }

    private BookmarkRecyclerAdapter.BookmarkListener onSubmitListener;

    public void setSubmitListener(BookmarkRecyclerAdapter.BookmarkListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public BookmarkRecyclerAdapter.BookmarkListener getOnSubmitListener(){
        return onSubmitListener;
    }

    private void setFashionItemList() {
        bookmarkItemList = new ArrayList<>();
        DBController controller ;
        controller = new DBController(getContext());

        ArrayList<Coordi> arrayList = controller.FindCoordi();

        for(Coordi coordi : arrayList) {
            bookmarkItemList.add(new BookmarkItem(coordi.getId(), coordi.getTitle(), R.drawable.ic_empty));
        }
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
//        bookmarkItemList.add(new BookmarkItem("Title", R.drawable.ic_empty));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        String icon = adapterView.getItemAtPosition(i).toString();
//        Intent result = new Intent(null, Uri.withAppendedPath(CONTENT_URI,icon));
//        setResult(RESULT_OK, result);
        Toast.makeText(getContext(), "Test : " + i, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setResult(String resultOk, Intent result) {
        // TODO Auto-generated method stub

    }

    private void finish() {
        // TODO Auto-generated method stub

    }
}
