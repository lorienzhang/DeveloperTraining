package com.lorienzhang.networkconnect;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 *
 * headless Fragment, 没有UI元素，负责封装网络请求逻辑，并且处理生命周期回调。
 */
public class NetworkFragment extends Fragment {

    public static final String URL_KEY = "url_key";
    public static final String TAG = "NetwrokFragment_Tag";

    private String mUrlString;
    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;

    public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) {
        //如果Activity重构，恢复之前的Fragment；
        //这一步很重要，因为之前的Fragment可能有正在执行的Task；
        //Fragment可以恢复，是因为在onCreate()回调中调用了setRetainInstance(true);
        NetworkFragment networkFragment =
                (NetworkFragment) fragmentManager.findFragmentByTag(TAG);
        if(networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);
            fragmentManager
                    .beginTransaction()
                    .add(networkFragment, TAG)
                    .commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrlString = getArguments().getString(URL_KEY);

        //如果Activity重构，可以保持之前的Fragment不丢失；
        setRetainInstance(true);
    }

    //获得对Activity的引用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (DownloadCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getName()
                    + "必须实现DownloadCallback接口!!!");
        }
    }

    //清除对Activity的引用，防止Activity内存泄漏
    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //当fragment被销毁，取消下载任务
        cancelDownload();
    }

    public void cancelDownload() {
        if(mDownloadTask != null) {
            mDownloadTask.cancel(true);
            mDownloadTask = null;
        }
    }

    public void startDownload() {
        cancelDownload();
        mDownloadTask = new DownloadTask();
        mDownloadTask.execute(mUrlString);

    }

    private class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {

        //对结果进行封装，当下载任务完成了，mResultValue和mException有一个是null，
        //UI拿到正常结果或者异常结果，UI需要简单的判断即可，
        //这样可以将下载过程中产生的异常传递到UI线程中；
        class Result {
            public String mResultValue;
            public Exception mException;

            public Result(String mResultValue) {
                this.mResultValue = mResultValue;
            }

            public Result(Exception mException) {
                this.mException = mException;
            }
        }

        //进入下载之前，检查网络状态是否可用
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();

                //检查网络状态，如果网络不可用，直接取消任务，将一个null数据回调出去
                if(networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI &&
                        networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {

                    mCallback.updateFromDownload(null);
                    cancel(true);

                }
            }
        }

        @Override
        protected DownloadTask.Result doInBackground(String... urls) {
            Result result = null;

            if(!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    String resultString = downloadUrl(url);
                    //这里对返回结果进行非空判断，如果为空，向上抛异常
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received!");
                    }
                } catch (Exception e) {
                    result = new Result(e);
                }
            }

            return result;
        }

        //设计接口时，方法的命名和AsynTask中方法名称是一直的
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (values.length >= 2 && mCallback != null) {
                mCallback.onProgressUpdate(values[0], values[1]);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);

            //通知外部，取下载结果
            if(result.mException != null) {
                mCallback.updateFromDownload(result.mException.getMessage());
            } else if (result.mResultValue != null) {
                mCallback.updateFromDownload(result.mResultValue);
            }

            //通知外部，下载结束
            mCallback.finishDownloading();
        }

        //通过http请求，下载url对应的数据内容
        //这里的一切处理，如果有异常直接向上抛，因为上面对异常做了处理。
        private String downloadUrl(URL url) throws Exception{

            InputStream is = null;
            HttpURLConnection connection = null;
            String result = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                //设置http请求参数，并且connect
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.connect();

                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS, 0);

                int responseCode = connection.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }

                is = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if(is != null) {
                    //从流里面读出数据
                    InputStreamReader reader = new InputStreamReader(is);
                    char[] buf = new char[1024];
                    int len;
                    len = reader.read(buf);
                    result = new String(buf, 0, len);

                    publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                if(is != null) {
                    is.close();
                }
                if(connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }
    }

}
