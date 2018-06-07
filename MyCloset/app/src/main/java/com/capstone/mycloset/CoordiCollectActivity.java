package com.capstone.mycloset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CoordiCollectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private RecyclerView recyclerView;
    private List<BookmarkItem> bookmarkItemList;
    private BookmarkRecyclerAdapter recyclerAdapter;

    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordi_collect);

        search = getIntent().getStringExtra("Search");

        recyclerView = (RecyclerView) findViewById(R.id.coordi_collect_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        setFashionItemList();
        recyclerAdapter = new BookmarkRecyclerAdapter(this, bookmarkItemList, R.layout.activity_recommendation);
        recyclerView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setSubmitListener(this);
    }

    private void setFashionItemList() {
        bookmarkItemList = new ArrayList<>();
        DBController controller ;
        controller = new DBController(this);

        ArrayList<Coordi> arrayList = controller.FindCoordis(search);

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
        Toast.makeText(this, "Test : " + i, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setResult(String resultOk, Intent result) {
        // TODO Auto-generated method stub

    }
}
