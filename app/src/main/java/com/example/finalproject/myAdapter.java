package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodRecipe> myFoodList;

    public myAdapter(Context mContext, List<FoodRecipe> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recipe, parent, false);

        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {

        //To get Image from database
        //Load function takes URL of myFoodList from database
        Glide.with(mContext)
                .load(myFoodList.get(position).getItemImage())
                .into(holder.imageView);

//        holder.imageView.setImageResource(myFoodList.get(position).getItemImage());
        holder.mTitle.setText(myFoodList.get(position).getItemTitle());
        holder.mDescription.setText(myFoodList.get(position).getItemDescription());
        holder.mPrice.setText(myFoodList.get(position).getItemPrice());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, foodDetails.class);
                intent.putExtra("Image", myFoodList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("Title", myFoodList.get(holder.getAdapterPosition()).getItemTitle());
                intent.putExtra("Description", myFoodList.get(holder.getAdapterPosition()).getItemDescription());
                intent.putExtra("Ingredient", myFoodList.get(holder.getAdapterPosition()).getItemIngredient());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder {               //sub-class

    ImageView imageView;
    TextView mTitle, mDescription, mPrice;
    CardView mCardView;

    public FoodViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.itemImage);
        mTitle = itemView.findViewById(R.id.txtTitle);
        mDescription = itemView.findViewById(R.id.textDescription);
        mPrice = itemView.findViewById(R.id.txtPrice);
        mCardView = itemView.findViewById(R.id.myCardView);

    }
}
