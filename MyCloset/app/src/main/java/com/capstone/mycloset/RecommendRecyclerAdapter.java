package com.capstone.mycloset;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by kdwoo on 2017-09-28.
 */

public class RecommendRecyclerAdapter extends RecyclerView.Adapter<RecommendRecyclerAdapter.ViewHolder> {
    Context context;
    List<FashionItem> items;
    String[] colorArray;
    Random random;
    int item_layout;
    private boolean first;

    public RecommendRecyclerAdapter(Context context, List<FashionItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        colorArray = context.getResources().getStringArray(R.array.card_color);
        random = new Random();

        first = true;
    }

    @Override
    public RecommendRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_cloth, null);

        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FashionItem item = items.get(position);
        Drawable drawable = context.getResources().getDrawable(item.getImage());
        holder.image.setImageDrawable(drawable);
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());
        holder.cardView.setCardBackgroundColor(Color.parseColor(colorArray[random.nextInt(colorArray.length)]));
//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderActivity.class);
//                intent.putExtra("Title", item.getTitle());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView summary;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.content_image);
            title = (TextView)itemView.findViewById(R.id.content_title);
            summary = (TextView)itemView.findViewById(R.id.content_summary);
            cardView = (CardView)itemView.findViewById(R.id.card_view_cloth);
        }
    }
}