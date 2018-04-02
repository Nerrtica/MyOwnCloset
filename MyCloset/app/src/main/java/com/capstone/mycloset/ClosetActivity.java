package com.capstone.mycloset;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
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
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClosetActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Uri photoUri;
    private NavigationView navigationView;

    private static final int MSG_TIMER_EXPIRED = 1;
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int CROP_FROM_CAMERA = 1;
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        Toast.makeText(ClosetActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(ClosetActivity.this,
                                "com.capstone.mycloset.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_closet).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "tmp_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/MyCloset/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
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
    protected void onResume() {
        if(navigationView != null) {
            navigationView.getMenu().findItem(R.id.nav_closet).setChecked(true);
        }
        super.onResume();
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
                cropImage();
                MediaScannerConnection.scanFile(ClosetActivity.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                        new String[]{photoUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                break;
            }
            case CROP_FROM_CAMERA:
            {
                try {
                    Intent intent = new Intent(getApplicationContext() , ImageSelectActivity.class);
                    intent.putExtra("Image", photoUri);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
                break;
            }
        }
    }

    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 4);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/MyCloset/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(ClosetActivity.this,
                    "com.capstone.mycloset.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
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
