package io.github.zhang.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import io.github.zhang.aidl.ICalculateInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private ICalculateInterface mICalculateInterface;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.bind_btn).setOnClickListener(this);
    findViewById(R.id.unbind_btn).setOnClickListener(this);
    findViewById(R.id.add_calculate).setOnClickListener(this);
    findViewById(R.id.sub_calculate).setOnClickListener(this);
  }

  private ServiceConnection mConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      mICalculateInterface = ICalculateInterface.Stub.asInterface(service);
      Toast.makeText(MainActivity.this, "Service Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override public void onServiceDisconnected(ComponentName name) {
      mICalculateInterface = null;
      Toast.makeText(MainActivity.this, "Service Disconnected!", Toast.LENGTH_SHORT).show();
    }
  };

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bind_btn:
        Intent intent = new Intent("io.github.zhang.aidl.CalculateService");
        intent.setPackage("io.github.zhang.aidl");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        break;
      case R.id.unbind_btn:
        unbindService(mConnection);
        break;
      case R.id.add_calculate:
        if (mICalculateInterface == null) {
          Toast.makeText(this, "Service Disconnected!", Toast.LENGTH_SHORT).show();
          return;
        }
        try {
          int result = mICalculateInterface.add(8, 9);
          Toast.makeText(this, "result " + result, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      case R.id.sub_calculate:
        if (mICalculateInterface == null) {
          Toast.makeText(this, "Service Disconnected!", Toast.LENGTH_SHORT).show();
          return;
        }
        try {
          int result = mICalculateInterface.sub(10, 4);
          Toast.makeText(this, "result " + result, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
    }
  }
}
