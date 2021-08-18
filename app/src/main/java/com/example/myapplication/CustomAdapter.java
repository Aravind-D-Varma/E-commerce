package com.example.myapplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

public class CustomAdapter extends BaseExpandableListAdapter {

    public static final String PRODUCT_NAME = "name_of_product";
    public static final String PRODUCT_MP = "mp_of_product";
    public static final String PRODUCT_SP = "sp_of_product";
    public static final String PRODUCT_CATEGORY = "category_of_product";
    public static final String PRODUCT_DETAILS = "details_of_product";
    private Context context;
    private String[] deptList;

    public CustomAdapter(Context context) {
        this.context = context;
        Categories categories = new Categories(context, PreferenceManager.getDefaultSharedPreferences(context));
        String[] d = categories.getIndividualCategories();
        this.deptList = d;
    }

    @Override
    public int getGroupCount() {
        if(deptList == null)
            return 0;
        return deptList.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ProductInfo> productList = ProductLab.get(context).getProducts();
        int size = 0;
        for(ProductInfo p : productList){
            if(p.getCategory().equals(deptList[groupPosition]))
                size++;
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ProductInfo> productList = ProductLab.get(context).getProducts();
        List<ProductInfo> qualified = new ArrayList<ProductInfo>();
        for(ProductInfo p : productList){
            if(p.getCategory().equals(deptList[groupPosition]))
                qualified.add(p);
        }
        return qualified.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.list_group, parent,false);
        }

        TextView heading = (TextView) view.findViewById(R.id.listHeader);
        heading.setText(deptList[groupPosition]);

        ImageView arrows = (ImageView) view.findViewById(R.id.ivGroupIndicator);
        if (isExpanded) {
            arrows.setImageResource(R.drawable.arrow_down);
            view.setPadding(0,0,30,30);
        } else {
            arrows.setImageResource(R.drawable.arrow_right);
            view.setPadding(0,0,10,10);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ProductInfo detailInfo = (ProductInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.product_list_item, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.cardview_image);
        image.setImageBitmap(null);

        TextView name = (TextView) view.findViewById(R.id.cardview_name);
        name.setText(detailInfo.getName().trim());

        TextView detail = (TextView) view.findViewById(R.id.cardview_details);
        detail.setText(detailInfo.getDescription());

        TextView marketPrice = (TextView) view.findViewById(R.id.cardview_marketPrice);
        marketPrice.setText(Double.toString(detailInfo.getMarketPrice()));
        marketPrice.setPaintFlags(marketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView storePrice = (TextView) view.findViewById(R.id.cardview_storePrice);
        storePrice.setText(Double.toString(detailInfo.getStorePrice()));

        Button edit = (Button) view.findViewById(R.id.cardview_edit);
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(context,addProductPage.class);
            intent.putExtra(PRODUCT_NAME, detailInfo.getName());
            intent.putExtra(PRODUCT_MP,detailInfo.getMarketPrice());
            intent.putExtra(PRODUCT_SP,detailInfo.getStorePrice());
            intent.putExtra(PRODUCT_CATEGORY,detailInfo.getCategory());
            intent.putExtra(PRODUCT_DETAILS,detailInfo.getDescription());
            context.startActivity(intent);
        });

        Button delete = (Button) view.findViewById(R.id.cardview_delete);
        delete.setOnClickListener(v -> {
            ProductLab.get(context).deleteProduct(detailInfo);
            notifyDataSetChanged();
        });

        /*ImageView preview = (ImageView) view.findViewById(R.id.cardview_image);
        final Uri mediaUri = new Categories(context).getMediaUriOf(detailInfo.getImagePath());
        Glide.with(context).load(mediaUri).into(preview);*/

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}