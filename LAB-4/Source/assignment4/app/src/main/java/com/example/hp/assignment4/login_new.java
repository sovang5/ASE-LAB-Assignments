package com.example.hp.assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login_new extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
    }
    public void onClickLog(View v)
    {
        EditText editText_emailid=(EditText)findViewById(R.id.editText_emailid) ;
        EditText editText_password=(EditText)findViewById(R.id.editText_password) ;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        String email_id=pref.getString("emailid", null);
        String pass=pref.getString("password",null);
        if(editText_emailid.getText().toString().equals(email_id))
        {
            if(editText_password.getText().toString().equals(pass))
            {
                Intent redirect = new Intent(login_new.this, testApi.class);
                startActivity(redirect);
            }
            else
            {
                Toast.makeText(login_new.this, "Wrong id or password",Toast.LENGTH_LONG).show();
            }
        }


    }
    public void onClickRegis(View v){
        Intent redirect = new Intent(login_new.this, MainActivity.class);
        startActivity(redirect);
    }
}
