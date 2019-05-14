package com.fzm.fzm_port;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android_serialport_api.MyApplication;
import android_serialport_api.SerialPortFinder;
import android_serialport_api.SerialUtilOld;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView receive_tv;
    private Button receive_b;
    private EditText send_et;
    private Button sendt_b;
    private Button stop_b;
    private ReadThread readThread;
    private int size = -1;
    private static final String TAG = "MainActivity";
    private SerialUtilOld serialUtilOld;
    private String path = "/dev/ttyAMA3";//根据当前串口位置改变
    private int baudrate = 115200;//波特率
    private int flags = 0;
    private MyApplication mApplication;
    private SerialPortFinder mSerialPortFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void init() {
        receive_tv = findViewById(R.id.main_recive_tv);
        receive_b = findViewById(R.id.main_recive_b);
        send_et = findViewById(R.id.main_send_et);
        sendt_b = findViewById(R.id.main_send_b);
        stop_b = findViewById(R.id.main_stop_b);
        receive_b.setOnClickListener(this);
        sendt_b.setOnClickListener(this);
        stop_b.setOnClickListener(this);
        mApplication = (MyApplication) getApplication();
        mSerialPortFinder = mApplication.mSerialPortFinder;
//        String[] entries = mSerialPortFinder.getAllDevices();
//        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        //查询支持的串口
//        for (int i = 0; i < entries.length; i++) {
//            Log.e("entries", entries[i] + "    ");
            // ttyGS3 (g_serial)      ttyGS2 (g_serial) ttyGS1 (g_serial)   ttyGS0 (g_serial)    ttyAMA3 (ttyAMA)
            // ttyAMA2 (ttyAMA)   ttyAMA1 (ttyAMA) ttyAMA0 (ttyAMA)
//        }
//        for (int i = 0; i < entryValues.length; i++) {
//            Log.e("entryValues", entryValues[i] + "    ");
            // /dev/ttyGS3   /dev/ttyGS2   /dev/ttyGS1   /dev/ttyGS0   /dev/ttyAMA3    /dev/ttyAMA2
            // /dev/ttyAMA1   /dev/ttyAMA0
//        }
        try {
            //设置串口号、波特率
            serialUtilOld = new SerialUtilOld(path, baudrate, 0);
        } catch (NullPointerException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_recive_b: {
//                if(size!=-1){
                readThread = new ReadThread();
                readThread.start();
//                }
            }
            break;
            case R.id.main_send_b:
                String context = send_et.getText().toString();
                Log.d(TAG, "onClick: " + context);
                try {
                    serialUtilOld.setData(context.getBytes());
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this, "串口设置有误，无法发送", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            break;
            case R.id.main_stop_b:
                //停止接收
                readThread.interrupt();
                receive_tv.setText("");

            break;
        }

    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] data1 = serialUtilOld.getData();
//                    byte[] data = serialUtilOld.getDataByte();
                    int size = serialUtilOld.getSize();
                    String s = bytesToHex(data1,size);
                    Log.e("data", s + " "+size);
                    if (s != null) {
                        onDataReceived(s);
                    }
                } catch (NullPointerException e) {
                    onDataReceived("-1");
                    e.printStackTrace();
                    readThread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                    onDataReceived("-1");
                    readThread.interrupt();
                }
            }
        }
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes,int lenth) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < lenth; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }




    protected void onDataReceived(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //显示出来
                if ("-1".equals(data)) {
                    Toast.makeText(MainActivity.this, "串口设置有误，无法接收", Toast.LENGTH_SHORT).show();
                } else {
                    receive_tv.append(data);
                }
            }
        });
    }

}
