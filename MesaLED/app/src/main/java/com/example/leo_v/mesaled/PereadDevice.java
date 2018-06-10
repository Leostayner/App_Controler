package com.example.leo_v.mesaled;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class PereadDevice extends ListActivity {
    static String ENDERECO_MAC = null;
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peread_device);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        pareadDevice();
        }

    private void pareadDevice(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            setListAdapter(arrayAdapter);
        }
        else{
            Toast.makeText(getApplicationContext(),"Nenhum dispositivo pareado",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String infoBt = ((TextView) v).getText().toString();

        String enderecoMAC = infoBt.substring(infoBt.length() - 17);
        Intent returnMac = new Intent();
        returnMac.putExtra(ENDERECO_MAC, enderecoMAC);
        setResult(RESULT_OK, returnMac);
        finish();

    }
}