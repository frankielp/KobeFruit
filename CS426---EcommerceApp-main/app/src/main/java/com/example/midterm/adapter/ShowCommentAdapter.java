package com.example.midterm.adapter;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.R;

public class ShowCommentAdapter extends RecyclerView.ViewHolder {

    public TextView txtUserPhone, txtComment;
    public RatingBar ratingBar;

    public ShowCommentAdapter(@NonNull View itemView) {
        super(itemView);
        txtComment = (TextView) itemView.findViewById(R.id.txtComment);
        txtUserPhone = (TextView) itemView.findViewById(R.id.txtUserPhone);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarComment);
    }
}
