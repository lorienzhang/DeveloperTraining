package com.lorienzhang.asyncloader;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * 查询Contacts表，获得所有联系人信息：DISPLAY_NAME
 * 使用Android的数据异步Loader机制，想ContentProvider做出查询；
 * 将查询结果显示在ListView中。
 */
public class ContactsFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private CursorAdapter mCursorAdapter;

    private ContactInteractionListener mContactInteractionListener;

    //联系人表中姓名列
    private static final String[] FROM_COLUMNS = {ContactsContract.Contacts.DISPLAY_NAME};
    //显示姓名的TextView
    private static final int[] TO_IDS = {android.R.id.text1};

    /**
     * Fragment需要一个空的构造函数
     */
    public ContactsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        //创建adapter实例。。。
        mCursorAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS,
                TO_IDS,
                0);
        getListView().setAdapter(mCursorAdapter);

        //设置list item点击监听器
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mContactInteractionListener = (ContactInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ContactInteractionListener");
        }
    }

    /**
     * 获得某个联系人的详细信息
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mCursorAdapter.getCursor();
        cursor.moveToPosition(position);

        //getLookupUri(): 返回某一个联系人的content uri
        Uri contactUri = ContactsContract.Contacts.getLookupUri(
                cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));

        mContactInteractionListener.onContactSelected(contactUri);
    }

    //查找Contacts表中的指定列
    private static String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //开始查询，提供对ContentProvier执行查询所需要的一系列完整信息
        return new CursorLoader(getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //复位Loader，清空adapter数据
        mCursorAdapter.swapCursor(null);
    }

    /**
     * 回调接口：实现Fragment和Activity的交互
     */
    public interface ContactInteractionListener {
        void onContactSelected(Uri uri);
    }
}
