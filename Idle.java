package com.example.logan1436.chatroom;

/*
 * Created by jack on 2017/3/28.
 *
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.Objects;

public class Idle extends AppCompatActivity {

    public String username_public ;
    private Handler handler = new Handler();
    public String msgeachtime = "";   //從server讀取的所有名單
    public User who = new User();                  //欲聊天對象
    public String whoWantToTalkToMe = ""; //誰想要跟我聊天
    public Boolean stop = false;      //讓換到其他activity則停止idle的重複sendtask
    public Boolean whetherChangeToChat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        Bundle _bundle = getIntent().getExtras();
        ArrayList<User> _user_list = new ArrayList<User>();
        msgeachtime = (String) _bundle.get("msg");
        Log.v("iii",msgeachtime);
        username_public = (String) _bundle.get("account_string");

        try {
            JSONArray jsonArray = new JSONArray(msgeachtime);
            for(int i = 0; i < jsonArray.length(); ++i)
            {
                JSONObject j = jsonArray.getJSONObject(i);
                if (!j.getString("state").equals("out"))
                {
                    String UserName = j.getString("name");
                    String UserStatus = j.getString("state");
                    String User_chat = j.getString("chat_with");
                    User user = new User(UserName, UserStatus, User_chat);
                    _user_list.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //*List View
        ListView _user_list_view;
        _user_list_view = (ListView) findViewById(R.id.listView);
        final UserListAdapter _user_list_adapter = new UserListAdapter(this, R.layout.item_user_list, _user_list); //利用UserLostAdapter這個class
        _user_list_view.setAdapter(_user_list_adapter);


        //按下listview其中一個item
        _user_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                who = _user_list_adapter.getItem(position);  //此function可以利用position// 讀取item
                String whostatus = who.getStatus();

                if (whostatus.equals("idle"))
                {
                    SendHttpRequestTask3 task_3 = new SendHttpRequestTask3();
                    task_3.execute();
                    switchToChat(who.getID());
                }
            }

        });

        //logout
        Button logout;
        logout = (Button) findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                switchToLogin();
            }
        });

        //***Doinbackground:Send message to server each minute
        //***senghttprequest_toser each one minute!!!!!!!!計時重複方法
        //設定定時要執行的方法
        Runnable sendtask = new Runnable() {
            @Override
            public void run() {
                Log.v("test","9");
                SendHttpRequestTask2 task = new SendHttpRequestTask2();
                final String msgeachtime_check = msgeachtime;
                task.execute();

                Runnable sendtask_changeListView = new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        if (!msgeachtime_check.equals(msgeachtime)){
                            changeListView();
                        }
                    }
                };
                handler.postDelayed(sendtask_changeListView, 1500);
                if (!stop && !whetherChangeToChat) {
                    handler.postDelayed(this, 2500);     //接下來不斷呼叫形成重複
                }
            }
        };
        handler.removeCallbacks(sendtask);     //設定接下來要執行什麼
        handler.postDelayed(sendtask, 2500);    //設定執行Delay的時間



    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeListView()
    {
        ArrayList<User> _user_list = new ArrayList<User>();
        String msggg = msgeachtime ;
        try
        {
            JSONArray jsonArray = new JSONArray(msggg);
            for(int i = 0; i < jsonArray.length(); ++i)
            {
                JSONObject j = jsonArray.getJSONObject(i);
                if (!Objects.equals(j.getString("state"), "out"))
                {
                    String UserName = j.getString("name");
                    String UserStatus = j.getString("state");
                    String User_chat = j.getString("chat_with");

                    User user = new User(UserName, UserStatus, User_chat);
                    _user_list.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //*List View
        ListView _user_list_view;
        _user_list_view = (ListView) findViewById(R.id.listView);
        UserListAdapter _user_list_adapter = new UserListAdapter(this, R.layout.item_user_list, _user_list); //利用UserLostAdapter這個class
        _user_list_view.setAdapter(_user_list_adapter);
    }


    private void switchToChat(String who)
    {
        stop = true ;                                //stop讓syntask終止
        Intent _intent = new Intent();
        _intent.setClass(Idle.this, Chat.class);
        Bundle _bundle = new Bundle();
        _bundle.putString("username", username_public);
        _bundle.putString("who",who);
        _bundle.putString("msg",msgeachtime);
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }


    private void switchToLogin()
    {
        Intent _intent = new Intent();
        _intent.setClass(Idle.this, Login.class);
        startActivity(_intent);
    }



    private class SendHttpRequestTask2 extends AsyncTask<Object, Object, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Object... urls)
        {
            //***senghttprequest_toser each one minute!!!!!!!!計時重複方法
            //設定定時要執行的方法
            String urlStr = "http://140.112.18.199:8000/idle/";
            msgeachtime = sendHttpRequest_toser(urlStr,username_public);
            try {
                JSONArray jsonArray = new JSONArray(msgeachtime);
                for (int i=0; i<jsonArray.length() ; i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    if ((Objects.equals(j.getString("chat_with"), username_public))&&(!Objects.equals(j.getString("name"), username_public))) {
                        Log.v("test_with","used1");
                        Log.v("test_with_state",j.getString("state"));
                        if (j.getString("state").equals("busy")) {
                            Log.v("test_with","used2");
                            whetherChangeToChat = true;
                            whoWantToTalkToMe = j.getString("name");
                            switchToChat(whoWantToTalkToMe);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("testdo",msgeachtime);
            return "";
        }

        private String sendHttpRequest_toser(String urlStr, String name){
            String _linestr_from_server = "";
            HttpURLConnection urlConnection1 = null;
            try{
                JSONObject json_sendtoser = new JSONObject();
                json_sendtoser.accumulate("name",name);
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
                    return _linestr_from_server;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("test_lis",_linestr_from_server);
            return _linestr_from_server;
        }
    }


    private class SendHttpRequestTask3 extends AsyncTask<Object, Object, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... urls)
        {
            String urlStr = "http://140.112.18.199:8000/change_status/";
            sendHttpRequest_changtochat(urlStr, username_public, who.getID());
            return "";
        }

        void sendHttpRequest_changtochat(String urlStr,String user, String who){
            String _linestr_from_server;
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
}
