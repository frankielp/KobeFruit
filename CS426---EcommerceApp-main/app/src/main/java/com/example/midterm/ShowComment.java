package com.example.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.midterm.Common.Common;
import com.example.midterm.adapter.ShowCommentAdapter;
import com.example.midterm.model.Rating;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerAdapter<Rating, ShowCommentAdapter> adapter;

    String foodId = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        //Or implement this for api 29 and above
        else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .build());
        setContentView(R.layout.activity_show_comment);

        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent()!=null){
                    foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                }
                if(!foodId.isEmpty() && foodId != null) {
                    Query query = ratingTbl.orderByChild("foodId").equalTo(foodId);

                    FirebaseRecyclerOptions<Rating> options
                            = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentAdapter>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentAdapter holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());
                        }

                        @NonNull
                        @Override
                        public ShowCommentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout, parent, false);
                            return new ShowCommentAdapter(view);
                        }
                    };
                    
                    loadComment(foodId);
                }
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                if(getIntent()!=null){
                    foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                }
                if(!foodId.isEmpty() && foodId != null) {
                    Query query = ratingTbl.orderByChild("foodId").equalTo(foodId);

                    FirebaseRecyclerOptions<Rating> options
                            = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentAdapter>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentAdapter holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());
                        }

                        @NonNull
                        @Override
                        public ShowCommentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout, parent, false);
                            return new ShowCommentAdapter(view);
                        }
                    };

                    loadComment(foodId);
                }
            }
        });
    }

    private void loadComment(String foodId) {

        adapter.startListening();

        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);


    }
}