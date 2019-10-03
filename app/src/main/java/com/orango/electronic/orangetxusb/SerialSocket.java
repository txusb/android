package com.orango.electronic.orangetxusb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;

import android.util.Log;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import java.io.IOException;
import java.util.concurrent.Executors;

import static com.orango.electronic.orangetxusb.TestPackage.FormatConvert.StringHexToByte;
import static com.orango.electronic.orangetxusb.TestPackage.FormatConvert.bytesToHex;


public class SerialSocket implements SerialInputOutputManager.Listener {

    private static final int WRITE_WAIT_MILLIS = 115200; // 0 blocked infinitely on unprogrammed arduino

    private final BroadcastReceiver disconnectBroadcastReceiver;
private int Long=0;
    private Context context;
    private SerialListener listener;
    private UsbDeviceConnection connection;
    private UsbSerialPort serialPort;
    private SerialInputOutputManager ioManager;

    public SerialSocket() {
        disconnectBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (listener != null)
                    listener.onSerialIoError(new IOException("background disconnect"));
                disconnect(); // disconnect now, else would be queued until UI re-attached
            }
        };
    }

    public void connect(Context context, SerialListener listener, UsbDeviceConnection connection, UsbSerialPort serialPort, int baudRate) throws IOException {
        if(this.serialPort != null)
            throw new IOException("already connected");
        this.context = context;
        this.listener = listener;
        this.connection = connection;
        this.serialPort = serialPort;
        context.registerReceiver(disconnectBroadcastReceiver, new IntentFilter(Constants.INTENT_ACTION_DISCONNECT));
        serialPort.open(connection);
        serialPort.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        serialPort.setDTR(true); // for arduino, ...
        serialPort.setRTS(true);
        ioManager = new SerialInputOutputManager(serialPort, this);
        Executors.newSingleThreadExecutor().submit(ioManager);
    }

    public void disconnect() {
        listener = null; // ignore remaining data and errors
        if (ioManager != null) {
            ioManager.setListener(null);
            ioManager.stop();
            ioManager = null;
        }
        if (serialPort != null) {
            try {
                serialPort.setDTR(false);
                serialPort.setRTS(false);
            } catch (Exception ignored) {
            }
            try {
                serialPort.close();
            } catch (Exception ignored) {
            }
            serialPort = null;
        }
        if(connection != null) {
            connection.close();
            connection = null;
        }
        try {
            context.unregisterReceiver(disconnectBroadcastReceiver);
        } catch (Exception ignored) {
        }
    }
String tmp="";
    public void write(String data,int Long) throws IOException {
        if(serialPort == null)
            throw new IOException("not connected");
        Log.d("write",(data));

        this.Long=Long;
        tmp="";
        serialPort.write(StringHexToByte(data), WRITE_WAIT_MILLIS);
    }

    @Override
    public void onNewData(byte[] data) {
        if(listener != null)
        tmp=tmp+bytesToHex(data);
        Log.d("writereback",tmp);
        if(tmp.length()==Long||Long==0){
            Log.d("writereback",tmp);
            listener.onSerialRead(StringHexToByte(tmp));
        }

    }

    @Override
    public void onRunError(Exception e) {
        if (listener != null)
            listener.onSerialIoError(e);
    }
}
