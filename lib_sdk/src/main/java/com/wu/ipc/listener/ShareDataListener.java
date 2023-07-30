package com.wu.ipc.listener;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2023/7/30
 * <p>
 * 用途:消息更新的接口
 */


public interface ShareDataListener {
    void notifyShareData(int id, String content);
}