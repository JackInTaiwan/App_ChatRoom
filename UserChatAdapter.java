package com.example.logan1436.chatroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Logan1436 on 21/03/2017.
 *
 */

public class UserChatAdapter extends ArrayAdapter<ChatData>
{
    private Context _context;

    public UserChatAdapter(Context context, int resourceId, List<ChatData> items)
    {
        super(context, resourceId, items);
        this._context = context;
    }

    private class ViewHolder
    {

        TextView userIDView_chat;
        TextView userContent_chat;
        TextView userTime_chat;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        ChatData ur = getItem(position);

        LayoutInflater mInflater = LayoutInflater.from(_context);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_chat_list, null);
            holder = new ViewHolder();

            holder.userIDView_chat = (TextView) convertView.findViewById(R.id.user_id_chat);
            holder.userContent_chat = (TextView) convertView.findViewById(R.id.user_content_chat);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            assert ur != null;
            holder.userIDView_chat.setText(ur.get_user_name());
            holder.userContent_chat.setText(ur.get_chatinfo());

        } catch (Exception e) {
            String errorMan = ur.get_who();
            Log.d("User Item Error", errorMan);
        }

        return convertView;
    }
}
