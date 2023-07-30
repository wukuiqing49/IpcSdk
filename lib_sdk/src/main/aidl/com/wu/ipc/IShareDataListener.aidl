// IShareDataListener.aidl
package com.wu.ipc;

// 消息变化的aidl 文件
interface IShareDataListener {
    void notifyShareData(int key, String values);
}
