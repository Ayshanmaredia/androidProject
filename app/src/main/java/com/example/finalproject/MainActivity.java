package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<FoodRecipe> myFoodList;
    FoodRecipe mFoodData;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);

        // To show items in form of Grids or Rows
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading items...");

        myFoodList = new ArrayList<>();

        final myAdapter myAdapter = new myAdapter(MainActivity.this, myFoodList);
        mRecyclerView.setAdapter(myAdapter);

        //we will get our instance from database
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();

        //We will now retrieve data from database using event listener
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFoodList.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    //we will get data and store it to object
                    //All data from database will be stored in foodRecipe object;

//                    FoodRecipe foodRecipe = itemSnapshot.getValue(FoodRecipe.class);

                    String itemTitle = itemSnapshot.child("itemTitle").getValue().toString();
                    String itemDescription = itemSnapshot.child("itemDescription").getValue().toString();
                    String itemPrice = itemSnapshot.child("itemPrice").getValue().toString();
                    String itemIngredient = itemSnapshot.child("itemIngredient").getValue().toString();
                    String itemImage = itemSnapshot.child("itemImage").getValue().toString();

                    FoodRecipe foodRecipe = new FoodRecipe(itemTitle, itemDescription, itemPrice, itemIngredient, itemImage);

                    myFoodList.add(foodRecipe);
                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    // To go from Main activity to Add Recipe Activity
    public void btn_AddActivity(View view) {
        startActivity(new Intent(this, add_Recipe.class));
    }

    public void btn_logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}