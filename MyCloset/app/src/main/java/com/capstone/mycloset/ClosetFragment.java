package com.capstone.mycloset;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ClosetFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String RESULT_OK = null;
    private ArrayList<Bitmap> mThumbs;
    private ArrayList<Closet> myCloset;
    private int iconSize;
    private int TYPE_CODE;

    public static ClosetFragment newInstance(int typeCode) {
        ClosetFragment closetFragment = new ClosetFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("TYPE_CODE", typeCode);
        closetFragment.setArguments(args);

        return closetFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TYPE_CODE = getArguments().getInt("TYPE_CODE");
        View view = inflater.inflate(R.layout.fragment_closet, container, false);

        super.onCreate(savedInstanceState);
        iconSize = convertDipToPixels(106);

        final GridView gridview = (GridView) view.findViewById(R.id.gridview_closet);
        gridview.setAdapter(new IconAdapter(getContext(), iconSize));
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder versionDialog = new AlertDialog.Builder(getContext());
                versionDialog.setTitle("옷 삭제");
                versionDialog.setMessage("정말로 옷을 삭제하시겠습니까?");
                versionDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Test Long Click: " + position, Toast.LENGTH_SHORT).show();
                        Closet closet = myCloset.get(position);
                        DBController controller ;
                        controller = new DBController(getContext());
                        controller.deleteCloset(closet.getId());
                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
                versionDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                versionDialog.show();
                return true;
            }
        });
//        CONTENT_URI=Uri.parse("content://" + IconsProvider.class.getCanonicalName());
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        String icon = adapterView.getItemAtPosition(i).toString();
//        Intent result = new Intent(null, Uri.withAppendedPath(CONTENT_URI,icon));
//        setResult(RESULT_OK, result);
        Toast.makeText(getContext(), "Test : " + i, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext() , ImageCheckActivity.class);
        intent.putExtra("ImagePath", myCloset.get(i).getImagePath());
        startActivity(intent);
    }

    private void setResult(String resultOk, Intent result) {
        // TODO Auto-generated method stub

    }

    private void finish() {
        // TODO Auto-generated method stub

    }

    private class IconAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        private Context mContext;
        private int mIconSize;
        public IconAdapter(Context mContext, int iconsize) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mContext = mContext;
            this.mIconSize = iconsize;
            loadIcon();
        }

        @Override
        public int getCount() {
            return mThumbs.size();
        }

        @Override
        public Object getItem(int position) {
            return mThumbs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                int padding = convertDipToPixels(4);
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(mIconSize, mIconSize));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundColor(Color.GRAY);
            } else {
                imageView = (ImageView) convertView;
            }
            Drawable d = new BitmapDrawable(getResources(), mThumbs.get(position));
            imageView.setImageDrawable(d);
//            imageView.setImageResource(mThumbs.get(position));
            return imageView;
        }

        private void loadIcon() {
            mThumbs = new ArrayList<Bitmap>();
            DBController controller ;
            controller = new DBController(getContext());

            myCloset = controller.FindCloset(TYPE_CODE);
            if(!myCloset.isEmpty()) {
                for(Closet temp : myCloset) {
                    Bitmap photo = null;
                    String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String fileName = temp.getImagePath();
                    int idx = fileName.indexOf("MyCloset/");
                    fileName = fileName.substring(idx + 9);
                    fileName = "thum_" + fileName;
                    String thumFile = folder + "/MyCloset/" + fileName;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                                Uri.parse(temp.getImagePath().toString().substring(0, idx + 9) + fileName));
                    } catch (FileNotFoundException e) {
                        OutputStream out = null;
                        try {
                            File file = new File(thumFile);
                            file.createNewFile();
                            out = new FileOutputStream(file);
                            photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(temp.getImagePath()));
                            photo = photo.createScaledBitmap(photo, iconSize, iconSize, true);
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

//                            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                        } catch (Exception error) {
                            error.printStackTrace();
                        } finally {
                            try {
                                out.flush();
                                out.close();
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        finish();
                    }
                    mThumbs.add(photo);
                }
            }
//            for (Closet temp : controller.FindCloset()) {
//                if(temp != null && temp.getType().compareTo("1") == 0)
//                    mThumbs.add(temp.getImage());
//            }

//            mThumbs = new ArrayList<Integer>();
//
//            for(int i = 0; i < TYPE_CODE; i++) {
//                mThumbs.add(R.drawable.ic_empty);
//                mThumbs.add(R.drawable.ic_empty);
//                mThumbs.add(R.drawable.ic_empty);
//            }

//            final Resources resources = getResources();
//            final String packageName = getActivity().getApplication().getPackageName();
//
//            final String[] extras = resources.getStringArray(R.array.icon_pack);
//            for (String extra : extras) {
//                int res = resources.getIdentifier(extra, "drawable", packageName);
//                if (res != 0) {
//                    final int thumbRes = resources.getIdentifier(extra,"drawable", packageName);
//                    if (thumbRes != 0) {
//                        mThumbs.add(thumbRes);
//                    }
//                }
//            }

        }
    }

    private int convertDipToPixels(float dips)
    {
        return (int) (dips * this.getResources().getDisplayMetrics().density + 0.5f);
    }
}
