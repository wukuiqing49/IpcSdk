package com.wu.ipc.base;

import com.wu.ipc.listener.ShareDataListener;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2023/7/30
 * <p>
 * 用途: 基类接口
 */


public interface SdkInterface {
    void sendShareData(int key, String values);

    String getShareData(int key);

    void registerCallback(int id, ShareDataListener callback);

    void unregisterCallback(ShareDataListener callback);
}