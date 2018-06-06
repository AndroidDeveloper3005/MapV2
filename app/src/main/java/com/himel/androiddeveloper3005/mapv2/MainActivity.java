package com.himel.androiddeveloper3005.mapv2;

import android.app.Dialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="MainActivity" ;
    private static  final int ERROR_DIALOG_REQUEST = 9001;
    private Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isServiceOk()){
            init();
        }
    }

    private void init() {
        mapBtn = findViewById(R.id.map_button);
        mapBtn.setOnClickListener(this);

    }

    public boolean isServiceOk(){
        Log.d(TAG,"isServiceOK : checking google service version");
        int avialbale = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (avialbale == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServiceOK : google service is Working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(avialbale)){
            Log.d(TAG,"isServiceOK :  google service is not working!!! An Error Occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,avialbale,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "We can not make map request", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    @Override
    public void onClick(View v) {
        if (v == mapBtn){
            startActivity(new Intent( this,MapActivity.class));
        }
    }
}
