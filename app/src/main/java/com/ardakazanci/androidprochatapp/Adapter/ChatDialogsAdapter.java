package com.ardakazanci.androidprochatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ardakazanci.androidprochatapp.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogsAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }


    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }


    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_chat_dialog, null);

            TextView txtTittle, txtMessage;
            ImageView imageView;

            txtMessage = view.findViewById(R.id.txt_list_chat_dialog_message);
            txtTittle = view.findViewById(R.id.txt_list_chat_dialog_title);
            imageView = view.findViewById(R.id.img_chat_dialog);

            txtMessage.setText(qbChatDialogs.get(position).getLastMessage());
            txtTittle.setText(qbChatDialogs.get(position).getName());


            // Random Color Generator
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int randomColor = generator.getRandomColor();

            // Library usage...
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();

            // Substring First Character...
            TextDrawable textDrawable = builder.build(txtTittle.getText().toString().substring(0, 1).toUpperCase(), randomColor);
            imageView.setImageDrawable(textDrawable);


        }


        return view;
    }
}
