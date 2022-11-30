package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Common.Common;
import com.example.midterm.adapter.CategoryAdapter;
import com.example.midterm.adapter.DiscountedProductAdapter;
import com.example.midterm.adapter.PopularAdapter;
import com.example.midterm.model.AllProduct;
import com.example.midterm.model.Category;
import com.example.midterm.model.DiscountedProducts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    ImageView allProduct, maps, signout, cart;
    FloatingActionButton order;
    TextView greeting;
    FloatingActionButton phone;
//    TextView phone;
    FirebaseDatabase database;

    /* ///////// INITIALIZE FOR DISPLAYING DISCOUNTED BANNER ///////////////// */

    RecyclerView discountRecyclerView;
    DiscountedProductAdapter discountedProductAdapter;
    List<DiscountedProducts> discountedProductsList;

    /* ///////// INITIALIZE FOR DISPLAYING CATEGORY ///////////////// */

    RecyclerView categoryRecyclerView;
    DatabaseReference category;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> list;

    /* ///////// INITIALIZE FOR DISPLAYING POPULAR ITEM ///////////////// */

    RecyclerView popularRecyclerView;
    DatabaseReference popular;
    PopularAdapter popularAdapter;
    ArrayList<AllProduct> popularProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        InitialRecyclerView();
        bottomNavigation();
        Phone();

    }
    private void Phone(){
        phone=findViewById(R.id.Phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "0795649717";
                if (number!=null)
                    dialPhoneNumber(number);
            }
        });
    }
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    @SuppressLint("SetTextI18n")
    private void bottomNavigation(){
        cart=findViewById(R.id.cartIcon);
        allProduct = findViewById(R.id.allProductImage);
        maps = findViewById(R.id.mapsImage);
        order = (FloatingActionButton) findViewById(R.id.fab);
        greeting = (TextView) findViewById(R.id.textView);

        greeting.setText("Hello " + Common.currentUser.getName());

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Order.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Cart.class));
            }
        });

        allProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AllProducts.class);
                i.putExtra("CategoryId", "600");
                startActivity(i);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        signout = findViewById(R.id.LogoutButton);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Delete remembering user & password
                Paper.book().destroy();

                Intent i = new Intent(MainActivity.this, IntroInterface.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }


    private void InitialRecyclerView() {
        loadDiscountedBanner();
        loadCategory();
        loadPopularProduct();

    }

    private void loadDiscountedBanner() {
        discountRecyclerView = findViewById(R.id.discountedRecycler);
        discountedProductsList = new ArrayList<>();

        discountedProductsList.add(new DiscountedProducts(1, R.drawable.discount1));
        discountedProductsList.add(new DiscountedProducts(2, R.drawable.discount2));
        discountedProductsList.add(new DiscountedProducts(3, R.drawable.discount3));
        discountedProductsList.add(new DiscountedProducts(4, R.drawable.discount4));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductAdapter = new DiscountedProductAdapter(this, discountedProductsList);
        discountRecyclerView.setAdapter(discountedProductAdapter);
    }

    private void loadCategory() {

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecycler);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager
                (new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        list = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(this, list);
        categoryRecyclerView.setAdapter(categoryAdapter);

        category.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Category categoryItem = dataSnapshot.getValue(Category.class);
                    list.add(categoryItem);
                }

                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPopularProduct() {
        popularRecyclerView = (RecyclerView) findViewById(R.id.popularRecycler);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Product");

        popularRecyclerView.setHasFixedSize(true);
        popularRecyclerView.setLayoutManager
                (new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        popularProduct = new ArrayList<>();

        popularAdapter = new PopularAdapter(this, popularProduct);
        popularRecyclerView.setAdapter(popularAdapter);

        category.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AllProduct popularItem = dataSnapshot.getValue(AllProduct.class);
                    popularItem.setKey(dataSnapshot.getKey().toString());
                    assert popularItem != null;
                    if(popularItem.getPopular().equals("1")) {
                        popularProduct.add(popularItem);
                    }
                }

                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /*private void loadPopularProduct() {
        ArrayList<AllProduct> popularArrayList = new ArrayList<>();

        popularArrayList.add(new AllProduct("Tomato", "tomato", 4.00, 0, "Red and Juicy"));
        popularArrayList.add(new AllProduct("Strawberry", "strawberry", 4.00, 0, "Fresh"));
        popularArrayList.add(new AllProduct("Blueberry", "blueberry", 4.00, 0, "Clean"));
        popularArrayList.add(new AllProduct("Broccoli", "broccoli", 4.00, 0, "From New Zealand"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView = findViewById(R.id.popularRecycler);
        popularRecyclerView.setLayoutManager(layoutManager);
        popularAdapter = new PopularAdapter(popularArrayList);
        popularRecyclerView.setAdapter(popularAdapter);
    }*/

}