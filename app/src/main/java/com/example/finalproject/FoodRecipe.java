package com.example.finalproject;

public class FoodRecipe {

    private String itemTitle;
    private String itemDescription;
    private String itemPrice;
    private String itemIngredient;
    private String itemImage;            // To get drawable image

    public FoodRecipe(String itemTitle, String itemDescription, String itemPrice, String itemIngredient, String itemImage) {
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemIngredient = itemIngredient;
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemIngredient() { return itemIngredient; }

    public String getItemImage() {
        return itemImage;
    }
}
