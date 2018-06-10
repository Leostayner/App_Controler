package com.example.leo_v.mesaled;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int SOLICITA_CONEXAO = 2;

    Button btConnect;
    ImageButton btTop, btLeft, btBot, btRigth;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice;
    BluetoothSocket mSocket;
    UUID M_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ConnectedThread connectedThread;
    private String MAC = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Vibrator vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        btTop = (ImageButton) findViewById(R.id.imageButtonTop);
        btLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
        btBot = (ImageButton) findViewById(R.id.imageButtonBot);
        btRigth = (ImageButton) findViewById(R.id.imageButtonRigth);

       btTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vb.vibrate(100);
                try {
                    connectedThread.write("1\n");
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Dispositivo não Conectado",Toast.LENGTH_LONG).show();
                }
             }
        });

        btRigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vb.vibrate(100);
                try {
                    connectedThread.write("2\n");

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Dispositivo não Conectado",Toast.LENGTH_LONG).show();
                }
            }
        });

        btBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vb.vibrate(100);
                try {
                    connectedThread.write("3\n");
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Dispositivo não Conectado",Toast.LENGTH_LONG).show();
                }
            }
        });

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vb.vibrate(100);
                try {
                    connectedThread.write("4\n");
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Dispositivo não Conectado",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connect:
                conectBluetooth();
                return true;

            case R.id.reset:
                try {
                    connectedThread.write("9\n");
                    Toast.makeText(getApplicationContext(),"Jogo Resetado",Toast.LENGTH_LONG).show();

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Dispositivo não Conectado",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void openActivity(){
        Intent intent = new Intent(this, PereadDevice.class);
        startActivityForResult(intent,SOLICITA_CONEXAO);
    }

    private void conectBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Dispositivo não possui Bluetooth",Toast.LENGTH_LONG).show();
        }

        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else{
            openActivity();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    openActivity();
                    Toast.makeText(getApplicationContext(),"Bluetooth ativado",Toast.LENGTH_LONG).show();
                }

                else if(resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Falha ao ativar bluetooth",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(PereadDevice.ENDERECO_MAC);
                    connectClient();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Erro ao solicitar conexão",Toast.LENGTH_LONG).show();
                }
        }
    }

    void connectClient(){
        try{
            mDevice = mBluetoothAdapter.getRemoteDevice(MAC);
            mSocket = mDevice.createRfcommSocketToServiceRecord(M_UUID);
            mSocket.connect();
            Toast.makeText(getApplicationContext(),"Conectado",Toast.LENGTH_LONG).show();

            connectedThread = new ConnectedThread(mSocket);
            connectedThread.start();

        }catch (IOException e){
            Toast.makeText(getApplicationContext(),"Erro ao Conectar",Toast.LENGTH_LONG).show();
        }
    }



    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

        }

        public void write(String data) {
            byte[] buffer = data.getBytes();
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) { }
        }

    }


}

