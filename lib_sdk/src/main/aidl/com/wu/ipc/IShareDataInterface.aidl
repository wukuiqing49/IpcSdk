// IShareDataInterface.aidl
package com.wu.ipc;

import com.wu.ipc.IShareDataListener;
//import com.wu.ipc.ShareDataInfo;

interface IShareDataInterface {


    void sendShareData(int key, String values);

    String getShareData(int key) ;

    void registerCallback(int id, IShareDataListener callback) ;

    void unregisterCallback(IShareDataListener callback) ;


}
