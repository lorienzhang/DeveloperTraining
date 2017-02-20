package com.lorienzhang.asyncloader;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * 根据传过来的联系人Uri，查询Contacts.Data表，用来获取联系人的详细信息。
 * 通过Android的数据异步Loader机制，查询Content Provider中的数据。
 */
public class ContactDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mContactUri;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        mTv = (TextView) findViewById(android.R.id.text1);

        //获取传过来参数：联系人Uri
        Intent intent = getIntent();
        mContactUri = intent.getData();

        //初始化Loader
        getLoaderManager().initLoader(0, null, this);
    }


    //Contacts.Data表中想要查询的列名
    private static final String[] PROJECTION = {
            StructuredPostal._ID,
            StructuredPostal.FORMATTED_ADDRESS,
            StructuredPostal.TYPE,
            StructuredPostal.LABEL,
    };
    //对查询结果进行过滤。。。
    private static final String SELECTION = ContactsContract.Data.MIMETYPE + "='"
            + StructuredPostal.CONTENT_ITEM_TYPE + "'";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //对应的表：Contacts.Data，查询这个表来获取联系人详细信息；
        Uri uri = Uri.withAppendedPath(mContactUri,
                ContactsContract.Contacts.Data.CONTENT_DIRECTORY);

        //查询
        return new CursorLoader(this,
                uri,
                PROJECTION,
                SELECTION,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //对cursor查询的一般编码风格。
        if (data.moveToFirst()) {
            do {
                //这里的columnIndex是和上面PROJECTION数组一一对应
                String address = data.getString(1);
                int type = data.getInt(2);
                String label = data.getString(3);
                //地址的标签：单位或者住宅
                CharSequence typeLabel = StructuredPostal.getTypeLabel(getResources(),
                        type,
                        label);
                String addrResult = typeLabel + ": " + address;

                TextView tv = new TextView(this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(25);
                tv.setText(addrResult);
                ((ViewGroup) findViewById(R.id.activity_contact_detail))
                        .addView(tv);
            } while (data.moveToNext());
        } else {
            TextView tv = new TextView(this);
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            tv.setTextSize(40);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setText("No addresses found");
            ((ViewGroup) findViewById(R.id.activity_contact_detail))
                    .addView(tv);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //do nothing
        //没有使用adapter，这里没有清空adapter的操作
    }
}
