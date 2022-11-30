package com.example.midterm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm.AllProducts;
import com.example.midterm.Common.Common;
import com.example.midterm.ProductDetail;
import com.example.midterm.R;
import com.example.midterm.model.AllProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    Context context;
    ArrayList<AllProduct> list;

    public PopularAdapter(Context context, ArrayList<AllProduct> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.popluar_list_items, parent, false);

        return new PopularAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AllProduct model = list.get(position);
        holder.popularName.setText(model.getName());
        holder.popularFee.setText(String.valueOf(model.getPrice()));

        int drawableResourceId = holder.itemView.getContext().getResources().
                getIdentifier(model.getImage(),
                        "drawable",
                        holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.popularPic);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("object", model);
                intent.putExtra("ProductID", model.getKey());
                context.startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User")
                .child(Common.currentUser.getPhone())
                .child("fav")
                .child(model.getKey());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.requireNonNull
                        (snapshot.getValue()).toString().equals("true")){
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    holder.fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            reference.setValue("false");
                        }
                    });
                }
                else {
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    holder.fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                            reference.setValue("true");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView popularName, popularFee, addButton;
        ImageView popularPic, fav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            popularName = (TextView) itemView.findViewById(R.id.popularItemName);
            popularFee = (TextView) itemView.findViewById(R.id.popularItemFee);
            addButton = (TextView) itemView.findViewById(R.id.popularItemAddButton);
            popularPic = (ImageView) itemView.findViewById(R.id.popularItemImage);
            fav = (ImageView) itemView.findViewById(R.id.favFoodPopular);
        }
    }

}
