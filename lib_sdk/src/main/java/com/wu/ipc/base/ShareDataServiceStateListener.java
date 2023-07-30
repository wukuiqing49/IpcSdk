package com.wu.ipc.base;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2023/7/30
 * <p>
 * 用途:
 */


public interface ShareDataServiceStateListener {
    void onServiceConnected();

    void onServiceDisconnected();

    void onUnbindService();

    void onBinderDied();
}