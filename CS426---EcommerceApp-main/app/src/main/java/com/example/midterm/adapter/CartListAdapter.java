package com.example.midterm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm.Helper.CartManagement;
import com.example.midterm.Interface.ChangeNumberListener;
import com.example.midterm.R;
import com.example.midterm.model.AllProduct;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    ArrayList<AllProduct> allProducts;
    CartManagement cartManagement;
    ChangeNumberListener changeNumberListener;

    public CartListAdapter(@NonNull ArrayList<AllProduct> allProducts, Context context, ChangeNumberListener changeNumberListener) {
        this.allProducts = allProducts;
        this.cartManagement = new CartManagement(context);
        this.changeNumberListener = changeNumberListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.title.setText(allProducts.get(position).getName());
        holder.feeEachItem.setText(String.valueOf(allProducts.get(position).getPrice()));
        holder.num.setText(String.valueOf(allProducts.get(position).getNumInCart()));
        holder.totalEachItem.setText(String.valueOf(Math.round((allProducts.get(position).getPrice()* allProducts.get(position).getNumInCart()*100)/100)));

        int drawableResourceId=holder.itemView.getContext().getResources().getIdentifier(
                allProducts.get(position).getImage(),"drawable",holder.itemView.getContext().getPackageName()
        );
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.pic);
        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManagement.plusNumberFood(allProducts, holder.getAdapterPosition(), new ChangeNumberListener(){
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberListener.changed();
                    }

                });
            }
        });
        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManagement.minusNumberFood(allProducts,holder.getAdapterPosition(),new ChangeNumberListener(){
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberListener.changed();
                    }

                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,feeEachItem;
        ImageView pic,plusItem,minusItem;
        TextView totalEachItem,num;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleTxt);
            feeEachItem=itemView.findViewById(R.id.feeEachItem);
            pic=itemView.findViewById(R.id.picCart);
            plusItem=itemView.findViewById(R.id.plusCartBtn);
            minusItem=itemView.findViewById(R.id.minusCartBtn);
            totalEachItem=itemView.findViewById(R.id.totalEachItem);
            num=itemView.findViewById(R.id.numberItemTxt);
        }
    }
}

