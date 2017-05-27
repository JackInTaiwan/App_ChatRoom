package com.example.logan1436.chatroom;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class Chat extends AppCompatActivity {
    public String username, who;
    public String chat_info = "" ;
    public String chat_info_check ="";
    private Handler handler= new Handler();
    public ChatInfo chatInfo = new ChatInfo();
    public String msg_send ="";
    public boolean stop_chat_box = false;
    public String list_msg ="";

    public Chat() throws JSONException {
    }

     //!!!!!!!要回到idle必須在此寫function 不可直接在oncreat裡面寫
    private void switchbackToidle()
    {
        Bundle _bundle = new Bundle();
        _bundle.putString("msg",list_msg);
        _bundle.putString("account_string",username);
        _bundle.putString("who",username);
        Intent _intent = new Intent();
        _intent.putExtras(_bundle);
        _intent.setClass(Chat.this, Idle.class);
        startActivity(_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle _bundle = getIntent().getExtras();
        username = _bundle.getString("username");
        who = _bundle.getString("who");
        list_msg = _bundle.getString("msg");

        chatInfo = new ChatInfo(username, who);


        setContentView(R.layout.activity_chat);

        ArrayList<ChatData> _chat_list = new ArrayList<>();

        // ChatData: I don't know where the chat String is,
        for (int i =1 ; i<=10 ;i++)
        {
            if (!chatInfo.get_con(i).equals(""))
            {
                Log.v("test","list used");
                ChatData data = new ChatData(who, username, chatInfo.get_con(i));
                _chat_list.add(data);
            }
        }
        ListView _chat_list_view;
        _chat_list_view = (ListView) findViewById(R.id.listView_chat);
        UserChatAdapter _user_chat_adapter = new UserChatAdapter(this, R.layout.item_user_list, _chat_list); //利用UserLostAdapter這個class
        _chat_list_view.setAdapter(_user_chat_adapter);


        SendHttpRequestTask_chatbox task_chatbox = new SendHttpRequestTask_chatbox();
        task_chatbox.execute();
        Runnable get_info_check = new Runnable() {
            @Override
            public void run() {
                chat_info_check = chat_info;
            }
        };
        handler.postDelayed(get_info_check,1500);



        final Runnable sendToCheckMsg= new Runnable() {
            @Override
            public void run() {
                Log.v("test","1");
                chat_info_check = chat_info;
                SendHttpRequestTask_chatbox task_chatbox = new SendHttpRequestTask_chatbox();
                task_chatbox.execute();

                Runnable read = new Runnable() {
                    @Override
                    public void run() {
                        Log.v("tes2",chat_info_check);
                        Log.v("test2",chat_info);
                        //重複判斷chat_info是否有改變,若有則read_chat_info
                        if ((chat_info_check.equals(chat_info)==false)&&stop_chat_box ==false) {
                            Log.v("test","check used");

                            try {
                                read_chat_info();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.v("test_read","yy");
                            changeListView();

                        }
                    }
                };
                handler.removeCallbacks(read);
                handler.postDelayed(read,2000);
                if (stop_chat_box == false) {
                    handler.postDelayed(this, 1500);
                }
            }
        };
        handler.removeCallbacks(sendToCheckMsg);
        handler.postDelayed(sendToCheckMsg,1000);


        //Buttons for sending and leaving
        Button btn_send = (Button) findViewById(R.id.send);
        Button btn_leave = (Button) findViewById(R.id.btn_leave);
        final EditText msg_edi = (EditText) findViewById(R.id.editText);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_send = username+" : "+msg_edi.getText().toString();
                SendHttpRequestTask_sendmsg task_sendmsg = new SendHttpRequestTask_sendmsg();
                task_sendmsg.execute();
            }
        });
        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_chat_box = true;
                //switch to 上一層

                switchbackToidle();


            }
        });
    /////////////////

    }


    private void read_chat_info() throws JSONException {
        Log.v("test_read","0");
        Log.v("test_read","0.5");
        JSONObject j = new JSONObject(chat_info);
        Log.v("test_read","1");
        if (j.getString("Con1").equals("")==false){
            Log.v("test_read","2");
            chatInfo.change(1, j.getString("Con1"));
        }
        if (!j.getString("Con2").equals("")){
            chatInfo.change(2, j.getString("Con2"));
        }
        if (!j.getString("Con3").equals("")){
            chatInfo.change(3, j.getString("Con3"));
        }
        if (!j.getString("Con4").equals("")){
            chatInfo.change(4, j.getString("Con4"));
        }
        if (!j.getString("Con5").equals("")){
            chatInfo.change(5, j.getString("Con5"));
        }
        if (!j.getString("Con6").equals("")){
            chatInfo.change(6, j.getString("Con6"));
        }
        if (!j.getString("Con7").equals("")){
            chatInfo.change(7, j.getString("Con7"));
        }
        if (!j.getString("Con8").equals("")){
            chatInfo.change(8, j.getString("Con8"));
        }
        if (!j.getString("Con9").equals("")){
            chatInfo.change(9, j.getString("Con9"));
        }
        if (!j.getString("Con10").equals("")){
            chatInfo.change(10, j.getString("Con10"));
        }
    }



    private void changeListView()
    {
        ArrayList<ChatData> _chat_list = new ArrayList<>();

        // ChatData: I don't know where the chat String is,
        for (int i =1 ; i<=10 ;i++)
        {
            Log.v("test_con(i)","used");
            Log.v("test_con(i)",chatInfo.get_con(i));
            if (!chatInfo.get_con(i).equals(""))
            {
                ChatData data = new ChatData(who, username, chatInfo.get_con(i));
                _chat_list.add(data);
                Log.v("test","use here");
            }
        }
        ListView _chat_list_view;
        _chat_list_view = (ListView) findViewById(R.id.listView_chat);
        UserChatAdapter _user_chat_adapter = new UserChatAdapter(this, R.layout.item_chat_list, _chat_list); //利用UserLostAdapter這個class
        _chat_list_view.setAdapter(_user_chat_adapter);
        Log.v("test","use adapter");
    }



    private  class SendHttpRequestTask_sendmsg extends  AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... urls)
        {
            String urlStr = "http://140.112.18.199:8000/chat_sendmsg/";
            sendHttpRequest_chatbox(urlStr, username, who, msg_send);

            return "";
        }

        void sendHttpRequest_chatbox(String urlStr, String user, String who, String msg){
            String _linestr_from_server = "";
            HttpURLConnection urlConnection1 = null;
            try{
                JSONObject json_sendtoser = new JSONObject();
                json_sendtoser.accumulate("user",user);
                json_sendtoser.accumulate("who",who);
                json_sendtoser.accumulate("msg",msg);
                String str_sendtoser = json_sendtoser.toString();

                urlConnection1 = (HttpURLConnection) new URL(urlStr).openConnection();
                urlConnection1.setDoOutput(true);
                urlConnection1.setDoInput(true);
                urlConnection1.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setConnectTimeout(10000);
                urlConnection1.connect();

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection1.getOutputStream());
                writer.write(str_sendtoser);
                writer.flush();
                writer.close();

            } catch (JSONException | ProtocolException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                assert urlConnection1 != null;
                final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream(), "utf-8"));
                while ((_linestr_from_server = reader.readLine()) != null) {
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendHttpRequestTask_chatbox extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... urls)
        {
            String urlStr = "http://140.112.18.199:8000/chat_box/";
            sendHttpRequest_chatbox(urlStr, username, who);
            return "";
        }

        void sendHttpRequest_chatbox(String urlStr, String user, String who){
            String _linestr_from_server = "";
            HttpURLConnection urlConnection1 = null;
            try{
                JSONObject json_sendtoser = new JSONObject();
                json_sendtoser.accumulate("user",user);
                json_sendtoser.accumulate("who",who);
                String str_sendtoser = json_sendtoser.toString();

                urlConnection1 = (HttpURLConnection) new URL(urlStr).openConnection();
                urlConnection1.setDoOutput(true);
                urlConnection1.setDoInput(true);
                urlConnection1.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setConnectTimeout(10000);
                urlConnection1.connect();

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection1.getOutputStream());
                writer.write(str_sendtoser);
                writer.flush();
                writer.close();

            } catch (JSONException | MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                assert urlConnection1 != null;
                final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream(), "utf-8"));
                while ((_linestr_from_server = reader.readLine()) != null) {
                    Log.v("test_lis",_linestr_from_server);
                    chat_info = _linestr_from_server;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
