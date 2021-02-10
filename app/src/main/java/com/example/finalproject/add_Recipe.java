package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class add_Recipe extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText txt_title, txt_description, txt_price, txt_ingredient;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__recipe);

        recipeImage = (ImageView)findViewById(R.id.addImage);
        txt_title = (EditText)findViewById(R.id.addRecipeName);
        txt_description = (EditText)findViewById(R.id.addDescription);
        txt_price = (EditText)findViewById(R.id.addPrice);
        txt_ingredient = (EditText)findViewById(R.id.addIngredient);

    }

    public void btnSelectImage(View view) {

        //To pick Image From Gallery
        Intent choosePhoto = new Intent(Intent.ACTION_PICK);

        //Setting the file to be selected with this extension
        choosePhoto.setType("image/*");
        startActivityForResult(choosePhoto, 1);
    }

    //We will get the URI and set the data/image in ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == RESULT_OK) {

                uri = data.getData();

                //we will set the image here from URI
                recipeImage.setImageURI(uri);
            } else Toast.makeText(this, "You haven't picked image", Toast.LENGTH_LONG).show();
        }

        public void uploadImage() {

        //Storage Reference
            //RecipeImages is the name of the folder in the storage section of firebase
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("RecipeImages").child(uri.getLastPathSegment());

            //Creating a dialog box while uploading the Recipe
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Recipe Uploading...");
            progressDialog.show();

            //to upload images on firebase
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //We will store download URI into string and we will save it to database
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult(); //urlImage this is the download url in the database with information of images
                    imageUrl = urlImage.toString();

                    //We call this function because, We need to get all data(recipe data) when uploaded
                    uploadRecipe();

                    //To dismiss the progress
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });

        }

    public void btnAddRecipe(View view) {
        //Validation to confirm that no fields are empty
        if(uri != null
            && txt_title != null && !txt_title.getText().toString().isEmpty()
            && txt_description != null && !txt_description.getText().toString().isEmpty()
            && txt_price != null && !txt_price.getText().toString().isEmpty()
            && txt_ingredient != null && !txt_ingredient.getText().toString().isEmpty()) {
            uploadImage();
        }
        else{
            Toast.makeText(add_Recipe.this, "Please fill all the details with image.", Toast.LENGTH_LONG).show();
        }

    }

    //We will upload all the data in Firebase
    public void uploadRecipe() {

        //Creating object for each field
        FoodRecipe foodRecipe = new FoodRecipe(
                txt_title.getText().toString(),
                txt_description.getText().toString(),
                txt_price.getText().toString(),
                txt_ingredient.getText().toString(),
                imageUrl
        );

        //To store date and time in the string
        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(myCurrentDateTime).setValue(foodRecipe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(add_Recipe.this, "Recipe Uploaded", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
            // To Dismiss the Dialog box, When it fails to upload the data on database
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_Recipe.this, e.getMessage(), Toast.LENGTH_LONG).show();  //e.getMessage().toString() for exception message
            }
        });
    }
}