package com.example.midterm.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.AllProducts;
import com.example.midterm.R;
import com.example.midterm.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> list;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.category_row_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Category model = list.get(position);
        holder.catName.setText(model.getName());

                /*int drawableResourceId = holder.itemView.getContext().getResources().
                        getIdentifier(model.getImage(), "drawable", holder.itemView.getContext().getPackageName());
                Glide.with(holder.itemView.getContext())
                        .load(drawableResourceId)
                        .into(holder.catImage);*/

        Picasso.with(context).load(model.getImage())
                .into(holder.catImage);

        holder.catImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllProducts.class);
                intent.putExtra("CategoryId", model.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView catName;
        ImageView catImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            catImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            catName = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }
}
