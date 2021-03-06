package com.capstone.mycloset;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by kdwoo on 2017-09-28.
 */

public class BookmarkRecyclerAdapter extends RecyclerView.Adapter<BookmarkRecyclerAdapter.ViewHolder> {
    Context context;
    List<BookmarkItem> items;
    int item_layout;

    private boolean removeDB;

    public BookmarkRecyclerAdapter(Context context, List<BookmarkItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;

        removeDB = false;
    }

    @Override
    public BookmarkRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_fashion, null);

        return new ViewHolder(v);
    }

    public interface BookmarkListener {
        void onRefreshSubmit();
    }

    private BookmarkListener onSubmitListener;

    public void setSubmitListener(BookmarkListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public BookmarkListener getOnSubmitListener(){
        return onSubmitListener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BookmarkItem item = items.get(position);
//        Drawable drawable = context.getResources().getDrawable(item.getImage());
//        holder.image.setImageDrawable(drawable);
        DBController controller;
        controller = new DBController(context);

        Coordi coordi = controller.FindCoordi(item.getCoordiID());
        int OUTER_ID, TOP_ID, BOTTOM_ID, SHOES_ID;
        OUTER_ID = coordi.getOuter();
        TOP_ID = coordi.getTop();
        BOTTOM_ID = coordi.getBottom();
        SHOES_ID = coordi.getShoes();

        try {
            if(OUTER_ID != 0) {
                holder.outerImg.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                        Uri.parse(getThumFilePath(controller.FindClosetFromID(OUTER_ID).getImagePath()))));
            }
            holder.topImg.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.parse(getThumFilePath(controller.FindClosetFromID(TOP_ID).getImagePath()))));
            holder.buttomImg.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.parse(getThumFilePath(controller.FindClosetFromID(BOTTOM_ID).getImagePath()))));
            holder.shoesImg.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.parse(getThumFilePath(controller.FindClosetFromID(SHOES_ID).getImagePath()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.title.setText(item.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final EditText input = new EditText(context);
                input.setText(holder.title.getText());
                builder.setTitle("코디명 수정");
                builder.setView(input);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBController controller;
                                controller = new DBController(context);
                                controller.changeCoordiNme(item.getCoordiID(), input.getText().toString());
                                holder.title.setText(input.getText().toString());
//                                onSubmitListener.onRefreshSubmit();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoordiActivity.class);
                intent.putExtra("ID", item.getCoordiID());
                context.startActivity(intent);
            }
        });
        holder.favoriteButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        holder.favoriteButton.setChecked(false);
        holder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.favoriteButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    DBController controller;
                    controller = new DBController(context);

                    controller.deleteCoordi(item.getCoordiID());

//                    RecommendationActivity.refresh = true;

                    holder.cardView.setVisibility(View.GONE);
//                  onSubmitListener.onRefreshSubmit();
                } else {
//                    holder.favoriteButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
        });
    }

    private String getThumFilePath(String imagePath) {
        String fileName = imagePath;
        int idx = fileName.indexOf("MyCloset/");
        String folder = fileName.substring(0, idx + 9);
        fileName = fileName.substring(idx + 9);
        fileName = "thum_" + fileName;

        return folder + fileName;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView outerImg, topImg, buttomImg, shoesImg;
        TextView title;
        CardView cardView;
        ToggleButton favoriteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            outerImg = (ImageView)itemView.findViewById(R.id.fashion_image_outer);
            topImg = (ImageView) itemView.findViewById(R.id.fashion_image_top);
            buttomImg = (ImageView) itemView.findViewById(R.id.fashion_image_buttom);
            shoesImg = (ImageView) itemView.findViewById(R.id.fashion_image_shoes);

            title = (TextView)itemView.findViewById(R.id.fashion_title);
            cardView = (CardView)itemView.findViewById(R.id.card_view_fashion);
            favoriteButton = (ToggleButton)itemView.findViewById(R.id.favorite_button);
        }
    }
}