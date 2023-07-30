package com.wu.ipc.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.text.TextUtils;
import com.wu.ipc.utils.Remote;
import com.wu.ipc.utils.SdkAppGlobal;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2023/7/30
 * <p>
 * 用途:Sdk 基类封装
 */


public abstract class Sdk<T extends IInterface> {
    private T mProxy;
    private Application mApplication;
    private static final String THREAD_NAME = "bindServiceThread";
    private Handler mChildThread;
    private final Runnable mBindServiceTask = this::bindService;
    private final LinkedBlockingQueue<Runnable> mTaskQueue = new LinkedBlockingQueue();
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Sdk.this.mProxy = Sdk.this.asInterface(service);
            Remote.tryExec(() -> {
                if (service != null) {
                    service.linkToDeath(Sdk.this.mDeathRecipient, 0);
                }

            });
            if (Sdk.this.mServiceStateListener != null) {
                Sdk.this.mServiceStateListener.onServiceConnected();
            }

            Sdk.this.handleTask();
            Sdk.this.mChildThread.removeCallbacks(Sdk.this.mBindServiceTask);
        }

        public void onServiceDisconnected(ComponentName name) {
            Sdk.this.mProxy = null;
            if (Sdk.this.mServiceStateListener != null) {
                Sdk.this.mServiceStateListener.onServiceDisconnected();
            }

        }
    };
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        public void binderDied() {
            if (Sdk.this.mServiceStateListener != null) {
                Sdk.this.mServiceStateListener.onBinderDied();
            }

            if (Sdk.this.mProxy != null) {
                Sdk.this.mProxy.asBinder().unlinkToDeath(Sdk.this.mDeathRecipient, 0);
                Sdk.this.mProxy = null;
            }

            Sdk.this.toRebindService();
        }
    };
    ShareDataServiceStateListener mServiceStateListener;
    //    service Name
    String pageName;
    //Service ClassName
    String className;
    //    Service Action
    String action;
    //断开连接 重连的时间
    long mRetryBindTimeMillTime = 2000;

    public void init(String pageName, String className, String action, long mRetryBindTimeMillTime) {
        if (mApplication == null) {
            this.mApplication = SdkAppGlobal.getApplication();
            HandlerThread thread = new HandlerThread("bindServiceThread", 6);
            thread.start();
            this.mChildThread = new Handler(thread.getLooper());
            this.pageName = pageName;
            this.className = className;
            this.action = action;
            this.mRetryBindTimeMillTime = mRetryBindTimeMillTime;
        }
        if (!isServiceConnected()) {
            this.bindService();
        }
    }


    private void bindService() {
        if (TextUtils.isEmpty(pageName) || TextUtils.isEmpty(className)) return;
        if (this.mProxy == null) {
            ComponentName name = new ComponentName(pageName, className);
            Intent intent = new Intent();
            if (!TextUtils.isEmpty(action))
                intent.setAction(action);
            intent.setComponent(name);
//            if (Build.VERSION.SDK_INT >= 26) {
//                this.mApplication.startForegroundService(intent);
//            } else {
//                this.mApplication.startService(intent);
//            }
            boolean connected = this.mApplication.bindService(intent, this.mServiceConnection, mApplication.BIND_AUTO_CREATE);
            if (!connected) {
                this.toRebindService();
            }
        }

    }

    protected void toRebindService() {
        if (this.mChildThread != null && this.mBindServiceTask != null) {
            this.mChildThread.postDelayed(this.mBindServiceTask, mRetryBindTimeMillTime);
        }

    }

    protected void handleTask() {
        Runnable task;
        while ((task = (Runnable) this.mTaskQueue.poll()) != null) {
            if (this.mChildThread != null) {
                this.mChildThread.post(task);
            }
        }

    }

    protected T getProxy() {
        return this.mProxy;
    }

    protected LinkedBlockingQueue<Runnable> getTaskQueue() {
        return this.mTaskQueue;
    }

    public void addServiceStateListener(ShareDataServiceStateListener serviceStateListener) {
        this.mServiceStateListener = serviceStateListener;
    }

    public void removeServiceStateListener() {
        this.mServiceStateListener = null;
    }

    public void release() {
        if (this.isServiceConnected()) {
            if (this.mProxy != null) {
                this.mProxy.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            }

            this.mProxy = null;
            if (this.mApplication != null) {
                this.mApplication.unbindService(this.mServiceConnection);
            }

            this.mServiceStateListener = null;
        }

    }

    public boolean isServiceConnected() {
        return this.mProxy != null;
    }

    public void isRetryConnected() {
        this.toRebindService();
    }

    protected abstract T asInterface(IBinder service);


}
