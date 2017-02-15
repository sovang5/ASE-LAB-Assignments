package com.example.hp.assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClickReg(View v){
        EditText editText_name=(EditText)findViewById(R.id.editText_name) ;
        EditText editText_emailid=(EditText)findViewById(R.id.editText_emailid) ;
        EditText editText_password=(EditText)findViewById(R.id.editText_password) ;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", editText_name.getText().toString());
        editor.putString("emailid",editText_emailid.getText().toString());
        editor.putString("password",editText_password.getText().toString());
        editor.commit();
        Intent redirect = new Intent(MainActivity.this, login_new.class);
        startActivity(redirect);
    }
}
