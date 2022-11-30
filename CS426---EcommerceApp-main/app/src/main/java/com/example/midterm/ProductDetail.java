package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.midterm.Common.Common;
import com.example.midterm.Helper.CartManagement;
import com.example.midterm.model.AllProduct;
import com.example.midterm.model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.Objects;

public class ProductDetail extends AppCompatActivity implements RatingDialogListener {

    private TextView addCartButton;
    private TextView title, fee, description, numInCart, btnShowComment;
    private ImageView imageProduct, plus, minus, backButton, cartButton, fav, btnRating;
    private AllProduct object;
    int numOfOrder = 0;
    private CartManagement cartManagement;
    RatingBar ratingBar;
    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        foodId = (String) getIntent().getStringExtra("ProductID");

        cartManagement = new CartManagement(this);
        initView();
        getBundle();
        FavManagement();
        getRatingFood(foodId);
    }

    private void getRatingFood(String foodId) {

        Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Rating item = dataSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FavManagement() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User")
                .child(Common.currentUser.getPhone())
                .child("fav")
                .child(object.getKey());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.requireNonNull
                        (snapshot.getValue()).toString().equals("true")){
                    fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            reference.setValue("false");
                        }
                    });
                }
                else {
                    fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fav.setImageResource(R.drawable.ic_baseline_favorite_24);
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

    private void getBundle() {
        object = (AllProduct) getIntent().getSerializableExtra("object");

        int drawableResourceID = this.getResources().getIdentifier(object.getImage(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourceID)
                .into(imageProduct);
        title.setText(object.getName());
        fee.setText(String.valueOf(object.getPrice()));
        description.setText(object.getDescription());
        numInCart.setText(String.valueOf(object.getNumInCart()));
        //numInCart.setText(String.valueOf(numOfOrder));

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfOrder = numOfOrder + 1;
                numInCart.setText(String.valueOf(numOfOrder));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numOfOrder > 1) {
                    numOfOrder = numOfOrder - 1;
                }
                numInCart.setText(String.valueOf(numOfOrder));
            }
        });

        addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumInCart(numOfOrder);
                Log.d("CART", object.getName());
                cartManagement.insertProduct(object);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetail.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetail.this, Cart.class));
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });


        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ShowComment.class);
                intent.putExtra(Common.INTENT_FOOD_ID, object.getKey());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ResourceType")
    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Good", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this product")
                .setDescription("Please select your rating and give us your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescription(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ProductDetail.this)
                .show();
    }

    private void initView() {

        addCartButton = findViewById(R.id.AddCartBtn);
        title = findViewById(R.id.detailText);
        fee = findViewById(R.id.detailFee);
        description = findViewById(R.id.detailDescription);
        numInCart = findViewById(R.id.detailNumInCart);
        imageProduct = findViewById(R.id.detailImage);
        plus = findViewById(R.id.plusBtn);
        minus = findViewById(R.id.minusBtn);
        backButton = (ImageView) findViewById(R.id.backArrowAllProduct);
        cartButton = (ImageView) findViewById(R.id.cartButtonAllProduct);
        fav = (ImageView) findViewById(R.id.favFoodDetail);
        fav = (ImageView) findViewById(R.id.favFoodDetail);
        btnRating = (FloatingActionButton) findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnShowComment = (TextView) findViewById(R.id.btnShowComment);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NonNull String comments) {
        //get rating and upload to firebase
        Rating rating = new Rating(Common.currentUser.getPhone(),
                foodId,
                String.valueOf(value),
                comments);

        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ProductDetail.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();

                    }
                });

        /*ratingTbl.child(Common.currentUser.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(Common.currentUser.getPhone()).exists()){
                            //Remove old value
                            ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                            //update
                            ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                        }
                        else {
                            ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                        }
                        Toast.makeText(ProductDetail.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

         */

    }
}