package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static com.example.myapplication.CustomAdapter.PRODUCT_CATEGORY;
import static com.example.myapplication.CustomAdapter.PRODUCT_DETAILS;
import static com.example.myapplication.CustomAdapter.PRODUCT_MP;
import static com.example.myapplication.CustomAdapter.PRODUCT_NAME;
import static com.example.myapplication.CustomAdapter.PRODUCT_SP;

public class addProductPage extends AppCompatActivity {
    private static final String[] DECLARED_GETPHOTO_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private ProductInfo mNewProduct;
    private StringBuilder mJoinedFilePaths;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_main_menu, menu);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        mNewProduct = new ProductInfo();

        String nameOfProduct = getIntent().getStringExtra(PRODUCT_NAME);
        double mpOfProduct = getIntent().getDoubleExtra(PRODUCT_MP,0);
        Double spOfProduct = getIntent().getDoubleExtra(PRODUCT_SP,0);
        String categoryOfProduct = getIntent().getStringExtra(PRODUCT_CATEGORY);
        String detailsOfProduct = getIntent().getStringExtra(PRODUCT_DETAILS);

        EditText titleField = (EditText) findViewById(R.id.product_title);
        EditText marketPrice = (EditText) findViewById(R.id.product_marketPrice);
        EditText storePrice = (EditText) findViewById(R.id.product_storePrice);
        EditText detailField = (EditText) findViewById(R.id.product_details);
        EditText category = (EditText) findViewById(R.id.product_category);

        try{
            titleField.setText(nameOfProduct);
            marketPrice.setText(Double.toString(mpOfProduct));
            storePrice.setText(Double.toString(spOfProduct));
            category.setText(categoryOfProduct);
            detailField.setText(detailsOfProduct);
        }catch (NullPointerException ignored){}

        Button cancel = (Button) findViewById(R.id.cancel_button);

        ImageButton preview = (ImageButton) findViewById(R.id.product_upload);
        Intent getImgIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImgIntent.setType("*/*");
        getImgIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        getImgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        preview.setOnClickListener(v -> {
                someActivityResultLauncher.launch(getImgIntent);
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button submit = (Button) findViewById(R.id.submit_area);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allSet = true;
                if(titleField.getText().toString().length()==0) {
                    titleField.setError("Title is required");
                    allSet = false;
                }
                if(marketPrice.getText().toString().length()==0) {
                    marketPrice.setError("Market Price is required");
                    allSet = false;
                }
                if(storePrice.getText().toString().length()==0){
                    storePrice.setError("Store Price is required");
                    allSet = false;
                }
                if(category.getText().toString().length()==0){
                    category.setError("Category is required");
                    allSet = false;
                }
                if(allSet){
                    Categories categories = new Categories(addProductPage.this,
                            PreferenceManager.getDefaultSharedPreferences(addProductPage.this));
                    String[] currentCategories = categories.getIndividualCategories();
                    boolean categoryFound = false;
                    if(currentCategories!=null) {
                        for (String c : currentCategories) {
                            if (c.equals(category.getText().toString().trim())) {
                                mNewProduct.setCategory(c);
                                categoryFound = true;
                                break;
                            }
                        }
                    }
                    if(!categoryFound){
                        String c = category.getText().toString().trim();
                        categories.addNewCategory(c);
                        mNewProduct.setCategory(c);
                    }
                    mNewProduct.setName(titleField.getText().toString());
                    mNewProduct.setDescription(detailField.getText().toString());
                    mNewProduct.setMarketPrice(Double.parseDouble(marketPrice.getText().toString()));
                    mNewProduct.setStorePrice(Double.parseDouble(storePrice.getText().toString()));
                    if(mJoinedFilePaths!=null && mJoinedFilePaths.toString().length()>1)
                        mNewProduct.setImagePath(mJoinedFilePaths.toString());
                }
                ProductLab.get(addProductPage.this).addProduct(mNewProduct);
                onBackPressed();
            }
        });
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getData() != null) {
                            Uri mMediaUri = data.getData();
                            mJoinedFilePaths = new StringBuilder();
                            mJoinedFilePaths.append(new Categories(addProductPage.this).getMediaPathFromUri(mMediaUri));
                        }
                    }
                }
            });
    private boolean hasMediaPermission() {
        int result = ContextCompat.checkSelfPermission(this, DECLARED_GETPHOTO_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}