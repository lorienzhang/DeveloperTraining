package com.lorienzhang.asyncloader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements
        ContactsFragment.ContactInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //用户点击某一个联系人的回调方法
    @Override
    public void onContactSelected(Uri uri) {
        if(uri != null) {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.setData(uri);

            startActivity(intent);
        }
    }
}
