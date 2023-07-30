//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.wu.ipc;

import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.wu.ipc.base.Sdk;
import com.wu.ipc.base.SdkInterface;
import com.wu.ipc.listener.ShareDataListener;
import com.wu.ipc.utils.Remote;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2023/7/30
 * <p>
 * 用途: 进程通讯操作的工具类
 */
public class IpcSdk extends Sdk<IShareDataInterface> implements SdkInterface {
    private static IpcSdk instance;
    Set<ShareDataListener> listener = new HashSet();
    IShareDataListener.Stub mCallBackImpl = new IShareDataListener.Stub() {
        public void notifyShareData(int key, String values) {
            if (IpcSdk.this.listener != null) {
                Iterator var3 = IpcSdk.this.listener.iterator();

                while (var3.hasNext()) {
                    ShareDataListener shareDataListener = (ShareDataListener) var3.next();
                    shareDataListener.notifyShareData(key, values);
                }
            }

        }
    };


    public static IpcSdk getInstance() {
        synchronized (IpcSdk.class) {
            if (instance == null) {
                instance = new IpcSdk();
            }
        }

        return instance;
    }

    protected IShareDataInterface asInterface(IBinder service) {
        return IShareDataInterface.Stub.asInterface(service);
    }

    /**
     * 发送消息
     *
     * @param key
     * @param values
     */
    public void sendShareData(int key, String values) {
        Remote.exec(() -> {
            if (!TextUtils.isEmpty(values)) {
                getProxy().sendShareData(key, values);
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * 指定ID 获取发送的消息
     *
     * @param key
     * @return
     */
    public String getShareData(int key) {
        try {
            return getProxy().getShareData(key);
        } catch (RemoteException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    /**
     * 注册监听
     *
     * @param id≤
     * @param callback
     */
    public void registerCallback(int id, ShareDataListener callback) {
        Remote.exec(() -> {
            if (this.isServiceConnected()) {
                this.listener.add(callback);
                getProxy().registerCallback(id, this.mCallBackImpl);
                return true;
            } else {
                this.isRetryConnected();
                this.getTaskQueue().offer(() -> {
                    this.registerCallback(id, callback);
                });
                return false;
            }
        });
    }

    /**
     * 解除监听
     *
     * @param callback
     */
    public void unregisterCallback(ShareDataListener callback) {
        if (this.listener != null) {
            this.listener.remove(callback);
        }

    }
}
