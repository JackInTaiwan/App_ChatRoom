package com.example.logan1436.chatroom;


import android.content.Context;
import android.support.annotation.NonNull;
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

class UserListAdapter extends ArrayAdapter<User>
{
    private Context _context;

    UserListAdapter(Context context, int resourceId, List<User> items)
    {
        super(context, resourceId, items);
        this._context = context;
    }

    private class ViewHolder
    {
        ImageView userPortraitImg;
        TextView userIDView;
        TextView userStatusView;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder holder;
        User ur = getItem(position);

        LayoutInflater mInflater = LayoutInflater.from(_context);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_user_list, null);
            holder = new ViewHolder();
            holder.userPortraitImg = (ImageView) convertView.findViewById(R.id.user_portrait);
            holder.userIDView = (TextView) convertView.findViewById(R.id.user_id);
            holder.userStatusView = (TextView) convertView.findViewById(R.id.user_status);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            assert ur != null;
            holder.userIDView.setText(ur.getID());
            holder.userStatusView.setText(ur.getStatus());
        } catch (Exception e) {
            String errorMan = ur.getID();
            Log.d("User Item Error", errorMan);
        }
        return convertView;

    }
}