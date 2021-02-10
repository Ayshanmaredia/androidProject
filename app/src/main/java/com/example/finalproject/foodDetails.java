package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class foodDetails extends AppCompatActivity {

    TextView foodTitle, foodDescription, foodIngredient;
    ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        foodImage = (ImageView)findViewById(R.id.foodImage);
        foodTitle = (TextView)findViewById(R.id.textTitle);
        foodDescription = (TextView)findViewById(R.id.textDescription);
        foodIngredient = (TextView)findViewById(R.id.textIngredient);

        Bundle mBundle = getIntent().getExtras();

        if (mBundle!=null) {
//            foodImage.setImageResource(mBundle.getInt("Image"));
            foodTitle.setText(mBundle.getString("Title"));
            foodDescription.setText(mBundle.getString("Description"));
            foodIngredient.setText(mBundle.getString("Ingredient"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(foodImage);
        }
    }
}