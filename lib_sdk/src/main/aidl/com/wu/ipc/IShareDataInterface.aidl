// IShareDataInterface.aidl
package com.wu.ipc;

import com.wu.ipc.IShareDataListener;
//import com.wu.ipc.ShareDataInfo;

// 服务连接的操作的主AIDL文件
interface IShareDataInterface {
//发送消息
    void sendShareData(int key, String values);
    //获取指定的消息
    String getShareData(int key) ;
    //注册消息监听
    void registerCallback(int id, IShareDataListener callback) ;
    //解绑消息监听
    void unregisterCallback(IShareDataListener callback) ;

}
