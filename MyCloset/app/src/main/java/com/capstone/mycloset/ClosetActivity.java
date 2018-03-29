package com.capstone.mycloset;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClosetActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Uri mCurrentPhotoUri;

    private static final int MSG_TIMER_EXPIRED = 1;
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private static final int PICK_FROM_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_closet);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_closet);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(grantExternalCameraPermission()) {
//                    mCurrentPhotoUri = createImageFile();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }
                else {
                    Snackbar.make(view, "카메라 권한이 필요합니다", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_closet).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    private Uri createImageFile(){
//        String imageFileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//
//        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageFileName));
//        return uri;
//    }

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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment(), "ONE");
        adapter.addFrag(new Fragment(), "TWO");
        adapter.addFrag(new Fragment(), "THREE");
        adapter.addFrag(new Fragment(), "FOUR");
        adapter.addFrag(new Fragment(), "FIVE");
        adapter.addFrag(new Fragment(), "SIX");
        adapter.addFrag(new Fragment(), "SEVEN");
        adapter.addFrag(new Fragment(), "EIGHT");
        adapter.addFrag(new Fragment(), "NINE");
        adapter.addFrag(new Fragment(), "TEN");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mIsBackKeyPressed == false) {
                mIsBackKeyPressed = true;

                mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();
                Toast.makeText(this, "'이전'버튼을 한 번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
                startTimer();
            } else {
                mIsBackKeyPressed = false;

                if (Calendar.getInstance().getTimeInMillis() <=
                        (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) {
                    finish();
                }
            }
        }
    }

    private void startTimer() {
        mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMER_EXPIRED: {
                    mIsBackKeyPressed = false;
                }
                break;
            }
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.closet, menu);
//        return true;
//    }
//
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
            // Now Activity
        } else if (id == R.id.nav_suggest) {
            Intent intent = new Intent(getApplicationContext() , RecommendationActivity.class);
            intent.putExtra("TypeCode", 0);
            startActivity(intent);
        } else if (id == R.id.nav_bookmark) {
            Intent intent = new Intent(getApplicationContext() , RecommendationActivity.class);
            intent.putExtra("TypeCode", 1);
            startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent(getApplicationContext() , ImageSelectActivity.class);
                intent.putExtra("Image", mCurrentPhotoUri);
                startActivity(intent);
//                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
//                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
//
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(mImageCaptureUri, "image/*");
//
//                // Crop한 이미지를 저장할 Path
//                intent.putExtra("output", mImageCaptureUri);
//
//                // Return Data를 사용하면 번들 용량 제한으로 크기가 큰 이미지는
//                // 넘겨 줄 수 없다.
//                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }
//            default:
//            {
//                File f = new File(mCurrentPhotoUri.getPath());
//                if(f.exists()) {
//                    f.delete();
//                }
//            }
        }
    }

    private boolean grantExternalCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }else{
            return true;
        }

    }
}
