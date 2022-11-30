package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Common.Common;
import com.example.midterm.adapter.AllProductAdapter;
import com.example.midterm.adapter.CategoryAdapter;
import com.example.midterm.model.AllProduct;
import com.example.midterm.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class AllProducts extends AppCompatActivity {

    RecyclerView allProductRecyclerView;
    AllProductAdapter allProductAdapter;
    ImageView backArrow, addCart;
    private String categoryIndex;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<AllProduct> list;

    //SEARCH FUNCTIONALITY

    AllProductAdapter searchAdapter;
    DatabaseReference searchProduct;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        allProductRecyclerView = (RecyclerView) findViewById(R.id.allProductRecycler);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Product");
        searchProduct = database.getReference("Product");

        allProductRecyclerView.setHasFixedSize(true);
        allProductRecyclerView.setLayoutManager
                (new GridLayoutManager(AllProducts.this, 2));

        list = new ArrayList<>();

        allProductAdapter = new AllProductAdapter(this, list);
        allProductRecyclerView.setAdapter(allProductAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AllProduct product = dataSnapshot.getValue(AllProduct.class);
                    product.setKey(dataSnapshot.getKey().toString());
                    categoryIndex = getIntent().getStringExtra("CategoryId");
                    DatabaseReference favProd = database.getReference("User")
                            .child(Common.currentUser.getPhone())
                            .child("fav").child(product.getKey());
                    switch (categoryIndex){
                        case "01":
                            if(product.getCategoryId().equals("01")) {
                                list.add(product);
                                suggestList.add(product.getName());
                            }
                            break;

                        case "02":
                            if(product.getCategoryId().equals("02")) {
                                list.add(product);
                                suggestList.add(product.getName());
                            }
                            break;

                        case "03":
                            if(product.getCategoryId().equals("03")) {
                                list.add(product);
                                suggestList.add(product.getName());
                            }
                            break;

                        case "04":
                            assert product != null;
                            if(product.getCategoryId().equals("04")) {
                                list.add(product);
                                suggestList.add(product.getName());
                            }
                            break;

                        case "600":
                            list.add(product);
                            suggestList.add(product.getName());
                            break;

                        case "05":
                            Log.d("FAV", favProd.getKey());
                            favProd.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        if((task.getResult().getValue()).equals("true")){
                                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                            list.add(product);
                                            suggestList.add(product.getName());
                                        }
                                    }
                                    allProductAdapter.notifyDataSetChanged();
                                }
                            });
                            break;
                    }

                }

                allProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllProducts.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        addCart = findViewById(R.id.cartButton);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllProducts.this, Cart.class));
            }
        });

        //SEARCH
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        materialSearchBar.setHint("What product do you want to find?");
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for(String search : suggestList) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    allProductRecyclerView.setAdapter(allProductAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        ArrayList<AllProduct> searchResult = new ArrayList<>();

        searchAdapter = new AllProductAdapter(this, searchResult);

        for(AllProduct allProduct : list){
            if (allProduct.getName().equals(text.toString())){
                searchResult.add(allProduct);
            }
        }

        allProductRecyclerView.setAdapter(searchAdapter);
    }
}

/*public class AllProducts extends AppCompatActivity {

    RecyclerView allProductRecycler;
    AllProductAdapter allProductAdapter;
    ImageView backArrow;
    ImageView addCart;
    private String categoryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        ArrayList<AllProduct> allProductArrayList = new ArrayList<>();

        allProductArrayList.add(new AllProduct("Tomato", "tomato", 4.00, "Red and Juicy"));
        allProductArrayList.add(new AllProduct("Blueberry Smoothie", "blueberrysmoothie", 4.25, "Sweet"));
        allProductArrayList.add(new AllProduct("Banana", "banana", 8.70, "Yellow and Juicy"));
        allProductArrayList.add(new AllProduct("Strawberry", "strawberry", 4.00, "Fresh"));
        allProductArrayList.add(new AllProduct("Spinach", "spinach", 2.15, "Eat clean to lose weight"));
        allProductArrayList.add(new AllProduct("Orange Juice", "orangejuice", 6.55, "Yummy yummy"));
        allProductArrayList.add(new AllProduct("Blueberry", "blueberry", 4.00, "Clean"));
        allProductArrayList.add(new AllProduct("Carrot", "carrot", 7.00, "Newly harvested carrots"));
        allProductArrayList.add(new AllProduct("Broccoli", "broccoli", 4.00, "From New Zealand"));
        allProductArrayList.add(new AllProduct("Lemonade", "lemonade", 12.22, "Mlem mlem"));

        categoryIndex = getIntent().getStringExtra("CategoryId");

        switch (categoryIndex){
            case "01":
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(2);
                allProductArrayList.remove(2);
                allProductArrayList.remove(4);
                break;
            case "02":
                allProductArrayList.remove(0);
                allProductArrayList.remove(0);
                allProductArrayList.remove(2);
                allProductArrayList.remove(2);
                allProductArrayList.remove(3);
                allProductArrayList.remove(3);
                allProductArrayList.remove(3);
                break;
            case "03":
                allProductArrayList.remove(0);
                allProductArrayList.remove(0);
                allProductArrayList.remove(0);
                allProductArrayList.remove(0);
                allProductArrayList.remove(0);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                break;
            case "04":
                allProductArrayList.remove(0);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                allProductArrayList.remove(1);
                break;
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        allProductRecycler = findViewById(R.id.allProduct);
        allProductRecycler.setLayoutManager(layoutManager);
        allProductAdapter = new AllProductAdapter(allProductArrayList);
        allProductRecycler.setAdapter(allProductAdapter);

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllProducts.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        addCart = findViewById(R.id.cartButton);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllProducts.this,Cart.class));
                finish();
            }
        });
    }
}*/