//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.wu.ipc.utils;

import android.os.RemoteException;
import android.util.Log;

public abstract class Remote {
    public Remote() {
    }

    public static <V> V exec(Remote.RemoteFunction<V> func) {
        try {
            return func.call();
        } catch (RemoteException var2) {
            throw new IllegalArgumentException("Failed to execute remote call" + var2);
        }
    }

    public static void tryExec(Remote.RemoteVoidFunction func) {
        try {
            func.call();
        } catch (RemoteException var2) {
            Log.e("IPC服务异常:", var2.toString());
        }

    }

    public interface RemoteFunction<V> {
        V call() throws RemoteException;
    }

    public interface RemoteVoidFunction {
        void call() throws RemoteException;
    }
}
