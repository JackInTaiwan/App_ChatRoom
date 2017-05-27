package com.example.logan1436.chatroom;

/*
 * Created by jack on 2017/4/4.
 *
 */

class ChatInfo {
    private String _idsend;
    private String _idreceive;
    private String _con1;
    private String _con2;
    private String _con3;
    private String _con4;
    private String _con5;
    private String _con6;
    private String _con7;
    private String _con8;
    private String _con9;
    private String _con10;

    ChatInfo() {
        this._idsend = "";
        this._idreceive = "";
        this._con1 = "";
        this._con2 = "";
        this._con3 = "";
        this._con4 = "";
        this._con5 = "";
        this._con6 = "";
        this._con7 = "";
        this._con8 = "";
        this._con9 = "";
        this._con10 = "";
    }

    ChatInfo(String idsend, String idreceive){
        this._idsend=idsend;
        this._idreceive=idreceive;
        this._con1 = "";
        this._con2 = "";
        this._con3 = "";
        this._con4 = "";
        this._con5 = "";
        this._con6 = "";
        this._con7 = "";
        this._con8 = "";
        this._con9 = "";
        this._con10 = "";
    }

    void change(int i, String content){
        if (i == 1){
            this._con1 = content;
        }
        if (i == 2){
            this._con2 = content;
        }
        if (i == 3){
            this._con3 = content;
        }
        if (i == 4){
            this._con4 = content;
        }
        if (i == 5){
            this._con5 = content;
        }
        if (i == 6){
            this._con6 = content;
        }
        if (i == 7){
            this._con7 = content;
        }
        if (i == 8){
            this._con8 = content;
        }
        if (i == 9){
            this._con9 = content;
        }
        if (i == 10) {
            this._con10 = content;
        }
    }

    public String get_idsend(){return this._idsend;}
    public String get_idreceive(){return this._idreceive;}

    public String get_con(int i){
        if (i==1){
            return this._con1;
        }
        if (i==2){
            return this._con2;
        }
        if (i==3){
            return this._con3;
        }
        if (i==4){
            return this._con4;
        }
        if (i==5){
            return this._con5;
        }
        if (i==6){
            return this._con6;
        }
        if (i==7){
            return this._con7;
        }
        if (i==8){
            return this._con8;
        }
        if (i==9){
            return this._con9;
        }
        if (i==10){
            return this._con10;
        }
        return "";
    }

}

