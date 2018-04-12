package com.capstone.mycloset;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by kdwoo on 2017-09-28.
 */

public class BookmarkRecyclerAdapter extends RecyclerView.Adapter<BookmarkRecyclerAdapter.ViewHolder> {
    Context context;
    List<BookmarkItem> items;
    int item_layout;
    private boolean first;

    public BookmarkRecyclerAdapter(Context context, List<BookmarkItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;

        first = true;
    }

    @Override
    public BookmarkRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_fashion, null);

        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BookmarkItem item = items.get(position);
        Drawable drawable = context.getResources().getDrawable(item.getImage());
        holder.image.setImageDrawable(drawable);
        holder.title.setText(item.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderActivity.class);
//                intent.putExtra("Title", item.getTitle());
//                context.startActivity(intent);
            }
        });
        holder.favoriteButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        holder.favoriteButton.setChecked(false);
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.favoriteButton.isChecked()) {
                    holder.favoriteButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                } else {
                    holder.favoriteButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardView;
        ToggleButton favoriteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.fashion_image);
            title = (TextView)itemView.findViewById(R.id.fashion_title);
            cardView = (CardView)itemView.findViewById(R.id.card_view_fashion);
            favoriteButton = (ToggleButton)itemView.findViewById(R.id.favorite_button);
        }
    }
}