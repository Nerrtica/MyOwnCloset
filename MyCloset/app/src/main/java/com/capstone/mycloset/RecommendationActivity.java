package com.capstone.mycloset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean isVisibleSearchMenu;
    private int TYPE_CODE;

    private MenuItem searchMenu;
    private TabLayout tabLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        TYPE_CODE = getIntent().getExtras().getInt("TypeCode");
        isVisibleSearchMenu = TYPE_CODE == 1 ? true : false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeTitle();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_recommendation);
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
                        navigationView.getMenu().findItem(R.id.nav_bookmark).setChecked(true);
                    } else {
                        TYPE_CODE = 0;
                        navigationView.getMenu().findItem(R.id.nav_suggest).setChecked(true);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
    }

    private void changeTitle() {
        if(TYPE_CODE == 0) {
            getSupportActionBar().setTitle("오늘의 추천");
        } else {
            getSupportActionBar().setTitle("북마크");
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position); }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment(), "오늘의 추천");
        adapter.addFrag(new Fragment(), "북마크");
        viewPager.setAdapter(adapter);
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
        searchView.setQueryHint("검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                // 완료
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
            Intent intent = new Intent(getApplicationContext() , ClosetActivity.class);
            startActivity(intent);
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
            developerDialog.setMessage("Chung-Ang Uni. CAPSTONE Project.2018");
            developerDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            developerDialog.show();
        } else if (id == R.id.nav_version) {
            AlertDialog.Builder versionDialog = new AlertDialog.Builder(this);
            versionDialog.setTitle("버전");
            versionDialog.setMessage("Version 0.2.182901");
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
}
