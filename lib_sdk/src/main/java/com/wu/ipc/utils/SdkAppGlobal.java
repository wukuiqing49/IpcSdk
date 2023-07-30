//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.wu.ipc.utils;

import android.app.Application;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SdkAppGlobal {
    public static final String TAG_SDK = "TAG_SDK";
    private static final String TAG = "TAG_SDK" + SdkAppGlobal.class.getSimpleName();
    public static final String CLASS_FOR_NAME = "android.app.ActivityThread";
    public static final String CURRENT_APPLICATION = "currentApplication";
    public static final String GET_INITIAL_APPLICATION = "getInitialApplication";

    private SdkAppGlobal() {
    }

    public static Application getApplication() {
        Application application = null;

        Class atClass;
        Method method;
        try {
            atClass = Class.forName("android.app.ActivityThread");
            method = atClass.getDeclaredMethod("currentApplication");
            method.setAccessible(true);
            application = (Application)method.invoke((Object)null);
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException var4) {
            Log.w(TAG, "getApplication: " + var4);
        }

        if (application != null) {
            return application;
        } else {
            try {
                atClass = Class.forName("android.app.ActivityThread");
                method = atClass.getDeclaredMethod("getInitialApplication");
                method.setAccessible(true);
                application = (Application)method.invoke((Object)null);
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException var3) {
                Log.w(TAG, "getApplication: " + var3);
            }

            return application;
        }
    }
}
