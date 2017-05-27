package com.example.logan1436.chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class Login extends AppCompatActivity
{
    public static String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("click", "Login");
                SendHttpRequestTask task = new SendHttpRequestTask();
                task.execute();
            }
        });
    }

    private void switchToIdle() {
        EditText account_string;
        account_string = (EditText) findViewById(R.id.theaccount);
        Intent _intent = new Intent();
        _intent.setClass(Login.this, Idle.class);
        Bundle _bundle = new Bundle();
        _bundle.putString("msg", msg);
        _bundle.putString("account_string",account_string.getText().toString());
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }

    private class SendHttpRequestTask extends AsyncTask<Object, Object, String>
    {
        private String urlStr = "http://140.112.18.199:8000/login/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... urls)
        {
            msg = sendHttpRequest();
            Log.v("test",msg);
            return msg;
        }
        //private void onProgressUpdate(Integer... values) { }

        @Override
        protected void onPostExecute(String result)
        {
            msg = result;
            Log.v("msg", msg);
            if (!msg.equals("account exists")) {
                switchToIdle();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("此帳號已經有人囉~");

                builder.setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            }


        private String sendHttpRequest()
        {
            String _linestr_from_server;
            HttpURLConnection urlConnection1 = null;
            EditText account_string;
            EditText passwd_string;
            account_string = (EditText) findViewById(R.id.theaccount);
            passwd_string = (EditText) findViewById(R.id.thepasswd);

            try {
                String json;

                // build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("theaccount", account_string.getText().toString());
                jsonObject.accumulate("thepasswd", passwd_string.getText().toString());

                // convert JSONObject to JSON to String
                json = jsonObject.toString();

                urlConnection1 = (HttpURLConnection) new URL(urlStr).openConnection();
                urlConnection1.setDoOutput(true);
                urlConnection1.setDoInput(true);
                urlConnection1.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setConnectTimeout(3000);
                urlConnection1.connect();

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection1.getOutputStream());
                writer.write(json);
                writer.flush();
                writer.close();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException|JSONException e) { // for openconnection
                e.printStackTrace();
                e.printStackTrace();
            }

            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream(), "utf-8"));

                while ((_linestr_from_server = reader.readLine()) != "") {
                    Log.v("look",_linestr_from_server);
                    return _linestr_from_server;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
    }

}

