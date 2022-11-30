package com.example.midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Common.Common;
import com.example.midterm.Helper.CartManagement;
import com.example.midterm.Helper.TinyDB;
import com.example.midterm.Interface.ChangeNumberListener;
import com.example.midterm.adapter.CartListAdapter;
import com.example.midterm.model.AllProduct;
import com.example.midterm.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private CartManagement managementCart;
    TextView totalFeeTxt, appTxt, deliveryTxt,totalTxt,emptyTxt;
    private double AppFee;
    private ScrollView scrollView;
    ImageView backButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference requests = database.getReference("Requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        managementCart = new CartManagement(this);
        initView();
        initList();
        CalculateCart();
        TextView checkout = (TextView) this.findViewById(R.id.checkOutButton);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlerDialog();
            }
        });
//
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cart.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void showAlerDialog() {

        TextView totalPrice = (TextView) findViewById(R.id.TotalTxt);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Fill in your adress: ");

        final EditText editAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(lp);
        alertDialog.setView(editAddress);
        alertDialog.setIcon(R.drawable.shoppingcart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAddress.getText().toString(),
                        totalPrice.getText().toString()
                );

                requests.child(Common.currentUser.getPhone())
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                managementCart.deleteAll(new ChangeNumberListener() {
                    @Override
                    public void changed() {
                        emptyTxt.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                });

                Toast.makeText(Cart.this, "Order Place Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void initView() {
        recyclerViewList=findViewById(R.id.recyclerView);
        totalFeeTxt=findViewById(R.id.TotalFeeTxt);
        appTxt =findViewById(R.id.AppTxt);
        deliveryTxt=findViewById(R.id.DeliveryTxt);
        totalTxt=findViewById(R.id.TotalTxt);
        scrollView=findViewById(R.id.ScrollView3);
        emptyTxt=findViewById(R.id.EmptyTxt);
        backButton = findViewById(R.id.backArrowCart);

    }
    private void initList(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        /*managementCart.deleteAll(new ChangeNumberListener() {
            @Override
            public void changed() {
                emptyTxt.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        });*/
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });
        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }

    }
    private void CalculateCart(){
        double percentTax=0.02;
        double delivery=10;
        AppFee=Math.round((managementCart.getTotalFee()*percentTax)*100)/100;

        double total=Math.round((managementCart.getTotalFee()+AppFee+delivery)*100)/100;
        double itemTotal=Math.round(managementCart.getTotalFee()*100)/100;

        if (itemTotal==0) {
            delivery = 0;
            total = 0;
            if(managementCart.getListCart().isEmpty()) {
                emptyTxt.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
            else {
                emptyTxt.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }
        }
        totalFeeTxt.setText("$"+itemTotal);
        totalTxt.setText("$"+total);
        deliveryTxt.setText("$"+delivery);
        appTxt.setText("$"+AppFee);
    }
}