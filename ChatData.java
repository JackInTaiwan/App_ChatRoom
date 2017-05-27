package com.example.logan1436.chatroom;

/**
 * Created by Logan1436 on 06/04/2017.
 *
 */

class ChatData {
    private String _chat_with;
    private String _user_name;
    private String _chatinfo_str;

    ChatData(String who, String user_name, String chatinfostr)
    {
        _chat_with = who;
        _user_name = user_name;
        _chatinfo_str = chatinfostr;
    }

    String get_who() { return this._chat_with; }
    String get_user_name() { return this._user_name; }
    String get_chatinfo() { return this._chatinfo_str; }
}
