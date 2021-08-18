package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class Categories {
    private static final String APPLICABLE_EVENTS = "true_events";
    private  Context mContext;
    private  String joinedCurrentCategories;
    private SharedPreferences mPreferences;

    /**
     * Constructor with context and preferences as parameters indicates getting events from Preferences
     */
    public Categories(Context context, SharedPreferences preference) {
        this.mContext = context;
        this.mPreferences = preference;
        this.joinedCurrentCategories = mPreferences.getString(APPLICABLE_EVENTS, "");
    }

    public Categories(Context context) {
        this.mContext = context;
    }


    public String getJoinedCategories() {
        return joinedCurrentCategories;
    }

    public String[] getIndividualCategories() {
        String[] s = joinedCurrentCategories.split(",");
        List<String> list = new ArrayList<String>(Arrays.asList(s));
        list.removeAll(Arrays.asList("", null));
        String[] s2 = list.toArray(new String[0]);
        return s2;
    }

    public void removeFromCategories(int finalI) {
        List<String> wordList = stringListOfCurrentCategories();
        wordList.remove(finalI);
        saveInPreference(wordList);
    }
    public void addNewCategory(String input) {
        List<String> wordList = stringListOfCurrentCategories();
        wordList.add(input);
        saveInPreference(wordList);
    }
    private List<String> stringListOfCurrentCategories() {
        List<String> currentCategoriesList = Arrays.asList(joinedCurrentCategories.split(","));
        return new ArrayList<>(currentCategoriesList);
    }

    private void saveInPreference(List<String> wordList) {
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString(APPLICABLE_EVENTS, stringListToString(wordList));
        mEditor.apply();
    }

    private String stringListToString(List<String> allCategories) {
        String[] applicableCategories = {};
        applicableCategories = allCategories.toArray(applicableCategories);
        StringBuilder combinedCategories = new StringBuilder();
        for (String string : applicableCategories)
            combinedCategories.append(string).append(",");

        return combinedCategories.toString();
    }

    public String[] addStringToArray(String string, String[] strings) {
        List<String> mylist = new LinkedList<>(Arrays.asList(strings));
        mylist.add(string);
        return mylist.toArray(new String[0]);
    }
    public String getMediaPathFromUri(Uri mMediaUri) {

        String imageEncoded = "";
        Uri contentUri;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String[] selectionArgs = getSelectionArgumentsForCursor(mMediaUri);
        String selection = "_id=?";
        contentUri = getSpecificContentUri(mMediaUri);

        Cursor cursor = mContext.getContentResolver().query(contentUri, filePathColumn, selection, selectionArgs, null);
        if(cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
            imageEncoded = cursor.getString(columnIndex);
            cursor.close();
        }
        return imageEncoded;
    }

    private Uri getSpecificContentUri(Uri mImageUri) {
        if (isImage(mImageUri))
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else
            return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    private boolean isImage(Uri mImageUri) {
        return getMimeType(mImageUri).startsWith("image");
    }

    private String getMimeType(Uri uri) {
        String mimeType;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private String[] getSelectionArgumentsForCursor(Uri mImageUri) {
        String docId = DocumentsContract.getDocumentId(mImageUri);
        String[] split = docId.split(":");
        return new String[]{split[1]};
    }

    public Uri getMediaUriOf(String mediaPath) {
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", new File(mediaPath));
    }
}
