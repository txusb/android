package com.orange.blelibrary.blelibrary.Server;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


import com.orange.blelibrary.blelibrary.BleActivity;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.BIND_AUTO_CREATE;
import static com.orange.blelibrary.blelibrary.Server.BluetoothLeService.EXTRA_DATA;
import static com.orange.blelibrary.blelibrary.tool.FormatConvert.StringHexToByte;

public class BleServiceControl {
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private final UUID RXUUID=UUID.fromString("00008D81-0000-1000-8000-00805F9B34FB");
    private final UUID TXUUID=UUID.fromString("00008D82-0000-1000-8000-00805F9B34FB");
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<BluetoothGattCharacteristic> mGattCharacteristics =
            new ArrayList<>();
    public boolean isconnect=false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private String mDeviceAddress;
    public byte[] getData=new byte[10];
    public BleActivity act;
    public  ServiceConnection   mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            mBluetoothLeService.act=act;
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
   public boolean first=true;
    public void connect(final String mDeviceAddress){
        try{
            if(first){
                first=false;
                try{act.unregisterReceiver(mGattUpdateReceiver);}catch (Exception r){}
                this.mDeviceAddress=mDeviceAddress;
                if(mBluetoothLeService!=null){ mBluetoothLeService.connect(mDeviceAddress);}
                if(!EventBus.getDefault().isRegistered(act)){EventBus.getDefault().register(act);}
                act.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                Intent gattServiceIntent = new Intent(act, BluetoothLeService.class);
                act.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);}else{
                this.mDeviceAddress=mDeviceAddress;
                mBluetoothLeService.connect(mDeviceAddress);
            }
        }catch (Exception e){e.printStackTrace();}
    }
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "unknownServiceString";
        String unknownCharaString = "unknownCharaString";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<>();
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            Log.d("uuid",uuid);
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                mGattCharacteristics.add(gattCharacteristic);
                if(RXUUID.equals(gattCharacteristic.getUuid())){
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                }
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }
    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                isconnect=true;
Log.w("s","連線");
                act.ConnectSituation(true);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.w("s","斷線");
                isconnect=false;
                act.ConnectSituation(false);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.w("s","發現服務");
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                getData=intent.getByteArrayExtra(EXTRA_DATA);
            }
        }
    };
    public void ReadCmd(String uuid){
        for(BluetoothGattCharacteristic a:mGattCharacteristics){
            Log.w("char",""+a.getUuid());
            if(UUID.fromString(uuid).equals(a.getUuid())){
                mBluetoothLeService.readCharacteristic(a);
                break;
            }
        }
    }

    public boolean WriteCmd(String write,int check){
        act.setRXDATA("");
for(BluetoothGattCharacteristic a:mGattCharacteristics){
    if(TXUUID.equals(a.getUuid())){
        mBluetoothLeService.check=check;
        mBluetoothLeService.tmp="";
        mNotifyCharacteristic=a;
        mNotifyCharacteristic.setValue(StringHexToByte(write));
        mBluetoothLeService.writeCharacteristic(mNotifyCharacteristic);
        return true;
    }
}
return false;
    }
    public void disconnect(){
        if(mBluetoothLeService!=null){mBluetoothLeService.disconnect();}
        }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

}
