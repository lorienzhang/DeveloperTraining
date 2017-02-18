package com.lorienzhang.networkconnect;

import android.net.NetworkInfo;

/**
 * Created by lorienzhang on 2017/2/18.
 *
 * 定义网络请求过程中的lifecycle events
 */

public interface DownloadCallback {

    //下载进度标记码
    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }

    //从外部获得设备的网络状态，方法名称的设计和API对应的方法同名
    NetworkInfo getActiveNetworkInfo();

    //通知调用者，取下载结果
    void updateFromDownload(String result);

    //通知调用者，下载过程中一系列状态，状态信息在上面Progress接口中定义
    void onProgressUpdate(int progressCode, int percerntDownload);

    //通知调用者，下载结束；注意：即使下载没有完成，也有可能调用这个方法，用户可能手动停止下载！！！
    void finishDownloading();
}
