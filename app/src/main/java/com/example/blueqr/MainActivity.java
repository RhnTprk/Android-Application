package com.example.blueqr;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button tara_btn;
    private Button button_ac;
    private static final String TAG = "MainActivity";
    BluetoothAdapter bluetoothAdapter;
    Button enablingButton;
    Button bondedDevicesButton;
    Button availableDevicesButton;
    OpenActivityClass openActivityClass;
    int requestCode = 0;

    BluetoothAdapter myBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enablingButton = (Button) findViewById(R.id.enablingButton);
        bondedDevicesButton = (Button) findViewById(R.id.bondedDevicesButton);
        availableDevicesButton = (Button) findViewById(R.id.availableDevicesButton);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        tara_btn = (Button) findViewById(R.id.tara_btn);
        final Activity activity = this;
        tara_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });



        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "Telefonunuzda Bluetooth bulunmamaktadır.", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                enablingButton.setText("BLUETOOTH KAPAT");
                enablingButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bondedDevicesButton.setEnabled(true);
                availableDevicesButton.setEnabled(true);
                Log.d(TAG, "Bluetooth açık");
                Toast.makeText(MainActivity.this, "BLUETOOTH AÇIK", Toast.LENGTH_SHORT).show();
            } else {
                enablingButton.setText("BLUETOOTH AÇ");
                enablingButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bondedDevicesButton.setEnabled(false);
                availableDevicesButton.setEnabled(false);
                Log.d(TAG, "Bluetooth kapalı");
                Toast.makeText(MainActivity.this, "BLUETOOTH KAPALI", Toast.LENGTH_SHORT).show();
            }
        }

        enablingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Telefonunuzda Bluetooth bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                } else {
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                        enablingButton.setText("BLUETOOTH AÇ");
                        enablingButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        bondedDevicesButton.setEnabled(false);
                        availableDevicesButton.setEnabled(false);
                        Log.d(TAG, "Bluetooth kapalı");
                        Toast.makeText(MainActivity.this, "BLUETOOTH KAPALI", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableAdapter, requestCode);
                    }
                }
            }
        });

        bondedDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityClass = new OpenActivityClass(MainActivity.this, BondedDevicesActivity.class);
                openActivityClass.openActivityWithoutSendingAddress();
            }
        });

        availableDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityClass = new OpenActivityClass(MainActivity.this, AvailableDevicesActivity.class);
                openActivityClass.openActivityWithoutSendingAddress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Taramayı bitirdiniz", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            enablingButton.setText("BLUETOOTH KAPAT");
            enablingButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            bondedDevicesButton.setEnabled(true);
            availableDevicesButton.setEnabled(true);
            Log.d(TAG, "Bluetooth açık");
            Toast.makeText(MainActivity.this, "BLUETOOTH AÇIK", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            enablingButton.setText("BLUETOOTH AÇ");
            enablingButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            bondedDevicesButton.setEnabled(false);
            availableDevicesButton.setEnabled(false);
            Log.d(TAG, "Bluetooth kapalı");
            Toast.makeText(MainActivity.this, "BLUETOOTH BAĞLANTISI REDDEDİLDİ", Toast.LENGTH_SHORT).show();
        }
    }
}