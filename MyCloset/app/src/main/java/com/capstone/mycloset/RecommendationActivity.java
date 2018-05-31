package com.capstone.mycloset;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecommendationFragment.SubmitListener {
    private boolean isVisibleSearchMenu;
    private int TYPE_CODE;

    private FloatingActionButton fab;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private MenuItem searchMenu;
    private TabLayout tabLayout;
    private NavigationView navigationView;

    private DetectingClothes detectingClothes;
    private Coordi coordi;

    public static boolean refresh;
    private String maxTemp, minTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        refresh = false;

        maxTemp = getIntent().getStringExtra("MaxTemp");
        minTemp = getIntent().getStringExtra("MinTemp");

        detectingClothes = new DetectingClothes(this, new Float(maxTemp), new Float(minTemp));
        coordi = detectingClothes.getCoordi();

        TYPE_CODE = getIntent().getExtras().getInt("TypeCode");
        isVisibleSearchMenu = TYPE_CODE == 1 ? true : false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeTitle();

        viewPager = (ViewPager) findViewById(R.id.viewpager_recommendation);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_recommendation);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isVisibleSearchMenu = tab.getPosition() == 1 ? true : false;
                if(searchMenu != null) {
                    searchMenu.setVisible(isVisibleSearchMenu);
                }
                if(navigationView != null) {
                    if (navigationView.getMenu().findItem(R.id.nav_suggest).isChecked()) {
                        TYPE_CODE = 1;
                        if(refresh) {
                            refresh = false;
                        }
                        navigationView.getMenu().findItem(R.id.nav_bookmark).setChecked(true);
                        fab.setVisibility(View.GONE);
                    } else {
                        TYPE_CODE = 0;
                        navigationView.getMenu().findItem(R.id.nav_suggest).setChecked(true);
                        fab.setVisibility(View.VISIBLE);
                    }
                }
                changeTitle();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(TYPE_CODE).select();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(isVisibleSearchMenu) {
            fab.setVisibility(View.GONE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(TYPE_CODE == 0) {
            navigationView.getMenu().findItem(R.id.nav_suggest).setChecked(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_bookmark).setChecked(true);
        }
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView temperatureTextView = (TextView) headerLayout.findViewById(R.id.temperatureText);
        StringBuilder tempString = new StringBuilder();
        tempString.append("최고 ");
        tempString.append(maxTemp);
        tempString.append("º · 최저 ");
        tempString.append(minTemp);
        tempString.append("º");
        temperatureTextView.setText(tempString.toString());

        TextView locationTextView = (TextView) headerLayout.findViewById(R.id.locationText);
        locationTextView.setText(getIntent().getStringExtra("Address"));

        ImageView weatherImageView = (ImageView) headerLayout.findViewById(R.id.weatherImageView);
        weatherImageView.setImageDrawable(getDrawable(getIntent().getIntExtra("Icon", R.drawable.wi_unknown)));
    }

    @Override
    protected void onStop() {
        RecommendationFragment.likeBtn = false;
        super.onStop();
    }

    private void changeTitle() {
        if(TYPE_CODE == 0) {
            getSupportActionBar().setTitle("오늘의 추천");
        } else {
            getSupportActionBar().setTitle("북마크");
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof RecommendationFragment) {
                return POSITION_NONE;
            } else {
                return super.getItemPosition(object);
            }
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position); }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        BookmarkFragment bookmarkFrag = new BookmarkFragment();
        RecommendationFragment recommenationFrag = null;
        if(coordi.getOuter() == -1) {
            recommenationFrag = new RecommendationFragment().newInstance(-1, coordi.getTop(),
                    coordi.getBottom(), coordi.getShoes());
        } else {
            recommenationFrag = new RecommendationFragment().newInstance(coordi.getOuter(), coordi.getTop(),
                    coordi.getBottom(), coordi.getShoes());
        }
        adapter.addFrag(recommenationFrag, "오늘의 추천");
        adapter.addFrag(bookmarkFrag, "북마크");
        viewPager.setAdapter(adapter);
        recommenationFrag.setSubmitListener(this);
    }

    public interface SubmitListener {
        void onLikeSubmit();
        void onRefreshSubmit();
    }

    private RecommendationFragment.SubmitListener onSubmitListener;

    public void setSubmitListener(RecommendationFragment.SubmitListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public RecommendationFragment.SubmitListener getOnSubmitListener(){
        return onSubmitListener;
    }

    @Override
    public void onLikeSubmit() {
        RecommendationFragment.likeBtn = true;
        setupViewPager(viewPager);
    }

    @Override
    public void onRefreshSubmit() {
        RecommendationFragment.likeBtn = false;
        coordi = detectingClothes.getCoordi();
        setupViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        if(navigationView != null) {
            if (TYPE_CODE == 0) {
                navigationView.getMenu().findItem(R.id.nav_suggest).setChecked(true);
            } else {
                navigationView.getMenu().findItem(R.id.nav_bookmark).setChecked(true);
            }
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        searchMenu = menu.findItem(R.id.menu_search);
        searchMenu.setVisible(isVisibleSearchMenu);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setMaxWidth(convertDipToPixels(10000));
        searchView.setQueryHint("검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                // 완료
                DBController controller;
                controller = new DBController(getApplicationContext());

                Coordi coordi = controller.FindCoordi(s);

                if(coordi != null) {
                    Intent intent = new Intent(getApplicationContext(), CoordiActivity.class);
                    intent.putExtra("ID", coordi.getId());
                    getApplicationContext().startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 입력
                return false;
            }
        });

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_closet) {
            finish();
        } else if (id == R.id.nav_suggest) {
            if(TYPE_CODE != 0) {
                TYPE_CODE = 0;
                tabLayout.getTabAt(TYPE_CODE).select();
                item.setChecked(true);
            }
        } else if (id == R.id.nav_bookmark) {
            if(TYPE_CODE != 1) {
                TYPE_CODE = 1;
                tabLayout.getTabAt(TYPE_CODE).select();
                item.setChecked(true);
            }
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext() , SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_us) {
            AlertDialog.Builder developerDialog = new AlertDialog.Builder(this);
            developerDialog.setTitle("개발자");
            developerDialog.setMessage("Chung-Ang Uni. CAPSTONE Project.2018\n" +
                    "GitHub : https://github.com/Nerrtica/MyOwnCloset");
            developerDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            developerDialog.show();
        } else if (id == R.id.nav_version) {
            AlertDialog.Builder versionDialog = new AlertDialog.Builder(this);
            versionDialog.setTitle("버전");
            versionDialog.setMessage("Version 0.8.6");
            versionDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            versionDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int convertDipToPixels(float dips)
    {
        return (int) (dips * this.getResources().getDisplayMetrics().density + 0.5f);
    }

//    private boolean grantExternalLocationPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//
//            if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                return true;
//            }else{
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                return false;
//            }
//        }else{
//            return true;
//        }
//    }
}
