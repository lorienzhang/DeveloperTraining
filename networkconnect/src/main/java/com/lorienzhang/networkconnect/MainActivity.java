package com.lorienzhang.networkconnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DownloadCallback{

    private TextView mDataText;
    private boolean mIsDowndloading = false;
    private NetworkFragment mNetworkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataText = (TextView) findViewById(R.id.data_text);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(),
                "https://www.baidu.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.activity_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_fetch:
                startDownload();
                return true;

            //当点击clear菜单，可能处于正在下载的情况。。。
            case R.id.action_clear:
                //结束下载
                finishDownloading();
                //清空TextView
                mDataText.setText("");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDownload() {
        if(!mIsDowndloading && mNetworkFragment != null) {
            mNetworkFragment.startDownload();
            mIsDowndloading = true;
        }
    }

    //获取网络状态信息
    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }


    @Override
    public void updateFromDownload(String result) {
        //1.设备问题：网络连接不可用，result=null
        //2.服务器问题：result=exception
        //3.一切正常，result拿到服务器正常返回结果
        if(result == null) {
            mDataText.setText(R.string.connection_error);
        } else {
            mDataText.setText(result);
        }

    }

    //根据下载过程中各种状态，更新UI
    @Override
    public void onProgressUpdate(int progressCode, int percerntDownload) {
        switch (progressCode) {
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mIsDowndloading = false;
        mNetworkFragment.cancelDownload();

    }
}
