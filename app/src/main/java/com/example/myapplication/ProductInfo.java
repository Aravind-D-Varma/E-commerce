package com.example.myapplication;

import java.util.UUID;

public class ProductInfo {

    private String mName;
    private String mDescription;
    private double mStorePrice;
    private double mMarketPrice;
    private String mCategory;

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    private String mImagePath;

    public UUID getID() {
        return mID;
    }

    public final UUID mID;

    public ProductInfo(){
        this(UUID.randomUUID());
    }
    public ProductInfo(UUID uuid){
        mID = uuid;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getStorePrice() {
        return mStorePrice;
    }

    public void setStorePrice(double storePrice) {
        mStorePrice = storePrice;
    }

    public double getMarketPrice() {
        return mMarketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        mMarketPrice = marketPrice;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

}
