package com.example.myapplication;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductLab {
    private static ProductLab sProductLab;
    private List<ProductInfo> mProducts;

    public static ProductLab get(Context context) {
        if (sProductLab == null)
            sProductLab = new ProductLab(context);
        return sProductLab;
    }
    private ProductLab(Context context) {
        mProducts = new ArrayList<>();
    }
    public List<ProductInfo> getProducts() {
        return mProducts;
    }
    public ProductInfo getProduct(UUID id) {
        for (ProductInfo product : mProducts) {
            if (product.getID().equals(id)) {
                return product;
            }
        }
        return null;
    }
    public void addProduct(ProductInfo p){
        mProducts.add(p);
    }
    public void deleteProduct(ProductInfo p){
        mProducts.remove(p);
    }
}
