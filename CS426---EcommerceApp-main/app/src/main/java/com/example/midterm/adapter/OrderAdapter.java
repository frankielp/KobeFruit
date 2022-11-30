package com.example.midterm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.midterm.Common.Common;
import com.example.midterm.Interface.ItemClickListener;
import com.example.midterm.Order;
import com.example.midterm.R;
import com.example.midterm.model.Request;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class OrderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderID, orderAddress, orderPhone, orderStatus;
    public CardView orderItem;

    private ItemClickListener itemClickListener;

    public OrderAdapter(@NonNull View itemView) {
        super(itemView);

        orderID = (TextView) itemView.findViewById(R.id.order_id);
        orderAddress = (TextView) itemView.findViewById(R.id.order_address);
        orderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        orderStatus = (TextView) itemView.findViewById(R.id.order_status);
        orderItem = (CardView) itemView.findViewById(R.id.cardView);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getBindingAdapterPosition(), false);
    }
}
