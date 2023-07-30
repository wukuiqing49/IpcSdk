package com.wu.ipcsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.wu.ipc.IpcSdk;
import com.wu.ipc.listener.ShareDataListener;

public class MainActivity extends AppCompatActivity implements ShareDataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        IpcSdk.getInstance().init(IpcSdkConstant.PKG,IpcSdkConstant.CLASS_NAME,IpcSdkConstant.ACTION,2000);
        findViewById(R.id.bt_connected).setOnClickListener(v -> {
            IpcSdk.getInstance().registerCallback(10086, this);
        });

        findViewById(R.id.bt_send).setOnClickListener(v -> {
            IpcSdk.getInstance().sendShareData(10086, "ClientA 消息");
        });
        findViewById(R.id.bt_get).setOnClickListener(v -> {
          String content= IpcSdk.getInstance().getShareData(10086);
            Log.e("IPCClientA:", " 获取Content:" + content);
        });
        findViewById(R.id.bt_un_register).setOnClickListener(v -> {
            IpcSdk.getInstance().unregisterCallback(this);
        });
    }

    @Override
    public void notifyShareData(int id, String content) {
        Log.e("IPCClientA:", "ID:" + id + " Content:" + content);
    }
}
