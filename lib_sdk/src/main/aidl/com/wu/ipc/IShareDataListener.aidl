// IShareDataListener.aidl
package com.wu.ipc;

interface IShareDataListener {
    void notifyShareData(int key, String values);
}
