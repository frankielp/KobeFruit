package com.example.midterm.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.midterm.Common.Common;
import com.example.midterm.Interface.ChangeNumberListener;
import com.example.midterm.model.AllProduct;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartManagement {

    private Context context;
    private TinyDB tinyDB;

    public CartManagement(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertProduct(AllProduct item){
        ArrayList<AllProduct> listProduct = getListCart();
        boolean existAlready = false;
        int n = 0;
        for(int i = 0; i < listProduct.size(); i++){
            if(listProduct.get(i).getName().equals(item.getName())){
                existAlready = true;
                n = i;
                break;
            }
        }

        if(existAlready){
            listProduct.get(n).setNumInCart(item.getNumInCart());
        }

        else {
            listProduct.add(item);
        }

        tinyDB.putListObject(Common.currentUser.getPhone(), listProduct);

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Order");
        for(AllProduct itemProd : listProduct){
            reference.child(Common.currentUser.getPhone()).child(itemProd.getKey()).setValue(item);
        }*/

        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<AllProduct> getListCart() {
        return tinyDB.getListObject(Common.currentUser.getPhone());
    }

    public void plusNumberFood(ArrayList<AllProduct>listFood, int position, ChangeNumberListener changeNumberListener){
        listFood.get(position).setNumInCart(listFood.get(position).getNumInCart()+1);
        tinyDB.putListObject(Common.currentUser.getPhone(),listFood);
        changeNumberListener.changed();
    }
    public void minusNumberFood(ArrayList<AllProduct>listFood, int position, ChangeNumberListener changeNumberListener){
        if(listFood.get(position).getNumInCart()<=1)
            listFood.remove(position);
        else
            listFood.get(position).setNumInCart(listFood.get(position).getNumInCart()-1);
        tinyDB.putListObject(Common.currentUser.getPhone(),listFood);
        changeNumberListener.changed();
    }
    public double getTotalFee(){
        ArrayList<AllProduct>listFood=getListCart();
        double fee=0;
        for (int i=0;i<listFood.size();i++)
        {
            fee+= listFood.get(i).getPrice()*listFood.get(i).getNumInCart();
        }
        return fee;
    }

    public void deleteAll(ChangeNumberListener changeNumberListener) {
        ArrayList<AllProduct> listFood = getListCart();
        listFood.removeAll(listFood);
        tinyDB.putListObject(Common.currentUser.getPhone(),listFood);
        changeNumberListener.changed();
    }
}
