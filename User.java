package com.example.logan1436.chatroom;

/**
 * Created by jack on 2017/3/28.
 */

class User
{
    private String _id;
    private String _status;

    private String _chat_with;

    User()
    {
        this._id = "";
        this._status = "";

        this._chat_with = "";
    }

    User(String id, String status, String chat_with)
    {
        this._id = id;
        this._status = status;

        this._chat_with = chat_with;
    }

    String getID() { return this._id; }
    String getStatus() { return this._status; }

}