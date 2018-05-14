package com.capstone.mycloset;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ClosetFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String RESULT_OK = null;
    private ArrayList<Bitmap> mThumbs;
    private ArrayList<Closet> myCloset;
    private int TYPE_CODE;

    private Uri CONTENT_URI;

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
        int iconSize = convertDipToPixels(106);

        GridView gridview = (GridView) view.findViewById(R.id.gridview_closet);
        gridview.setAdapter(new IconAdapter(getContext(), iconSize));
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Test Long Click: " + position, Toast.LENGTH_SHORT).show();
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
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(temp.getImagePath()));
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
