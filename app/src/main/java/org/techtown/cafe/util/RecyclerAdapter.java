package org.techtown.cafe.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.techtown.cafe.R;
import org.techtown.cafe.activity.OrderActivity;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<CafeMenuItem> cafeMenuItemArrayList;
    Activity activity;

    public RecyclerAdapter(Context context_, ArrayList<CafeMenuItem> cafeMenuItemArrayList, Activity activity){
        this.context=context_;
        this.cafeMenuItemArrayList=cafeMenuItemArrayList;
        this.activity=activity;

    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button hotButton;
        Button iceButton;

        @SuppressLint("NewApi")
        public ViewHolder(View v)
        {
            super(v);
            imageView =v.findViewById(R.id.imageView);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
            textView=v.findViewById(R.id.textView);
            hotButton=v.findViewById(R.id.hot);
            iceButton=v.findViewById(R.id.ice);

            hotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OrderActivity.class);
                    String name_and_temperature="Hot"+cafeMenuItemArrayList.get(getAdapterPosition()).getName();
                    intent.putExtra("name",name_and_temperature);
                    activity.startActivityForResult(intent,11);
                }
            });

            iceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OrderActivity.class);
                    String name_and_temperature="Ice"+cafeMenuItemArrayList.get(getAdapterPosition()).getName();
                    intent.putExtra("name",name_and_temperature);
                    activity.startActivityForResult(intent,11);
                    //데이터 전달
                }
            });

        }
    }


    @NonNull
    @Override
    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    //position에 해아하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CafeMenuItem cafeMenuItem=cafeMenuItemArrayList.get(position);
        Glide.with(this.context)
                .load(cafeMenuItem.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.textView.setText(cafeMenuItem.getName());
    }

    @Override
    public int getItemCount() {
        return cafeMenuItemArrayList.size();
    }

    public void addItem(ArrayList<CafeMenuItem> cafeMenuItemArrayList,String name,int img){
        CafeMenuItem item=new CafeMenuItem();

        item.setName(name);
        item.setImg(img);

        cafeMenuItemArrayList.add(item);
    }
}
