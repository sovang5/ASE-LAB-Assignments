package com.example.hp.assignment4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class testApi extends AppCompatActivity {
    String API_URL = "http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment" + "?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8" + "&outputMode=json&text=";
    TextView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
    }
    public void log_out(View v) {
        Intent i = new Intent(testApi.this, login_new.class);
        startActivity(i);
    }
    public void convertText(View v){
        EditText source = (EditText) findViewById(R.id.editText_msg);
        resultView= (TextView) findViewById(R.id.textView_res);
        String msg = source.getText().toString();
        System.out.println(msg);
        String getURL = API_URL + source.getText().toString();//The API service URL
        final String response1 = "";
        System.out.println(getURL);

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(getURL)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final String result = response.body().string();
                    try {
                        jsonResult = new JSONObject(result);
                        JSONObject convertedText = jsonResult.getJSONObject("docSentiment");
                        System.out.println(convertedText);

                        final String Result1 = convertedText.get("score").toString();
                        final String Result2 = convertedText.get("type").toString();
                        Log.d("okHttp", jsonResult.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultView.setText("Sentiment of Your Text:  " + Result2 + "                 " + "Score of your  Text:   " + Result1);

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception ex) {
            resultView.setText(ex.getMessage());

        }



    }
}
