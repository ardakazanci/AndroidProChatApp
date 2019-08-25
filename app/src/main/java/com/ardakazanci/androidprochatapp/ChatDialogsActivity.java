package com.ardakazanci.androidprochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ardakazanci.androidprochatapp.Adapter.ChatDialogsAdapter;
import com.ardakazanci.androidprochatapp.Common.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity {

    private FloatingActionButton fb;
    private ListView lv;


    @Override
    protected void onResume() {
        super.onResume();

        loadChatDialogs();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialogs);


        fb = findViewById(R.id.fbAddUser);
        lv = findViewById(R.id.lstChatDialogs);

        createSessionForChat();

        lv = findViewById(R.id.lstChatDialogs);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog = (QBChatDialog) lv.getAdapter().getItem(position);
                Intent intent = new Intent(ChatDialogsActivity.this, ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA, qbChatDialog);
                startActivity(intent);
            }
        });


        loadChatDialogs();

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChatDialogsActivity.this, ListUsersActivity.class);
                startActivity(intent);

            }
        });


    }

    /* Load All Chat Dialogs Of Users */
    private void loadChatDialogs() {

        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        // We will get 100 newest dialogs
        requestGetBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null, requestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {


                ChatDialogsAdapter adapter = new ChatDialogsAdapter(getBaseContext(), qbChatDialogs);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("ChatDialogsActivity", e.getMessage());

            }
        });


    }

    /* Create Session For Chat Services */
    private void createSessionForChat() {
        final ProgressDialog mDialog = new ProgressDialog(ChatDialogsActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        // Get Intent Extra
        String user, password;
        user = getIntent().getStringExtra("user");
        password = getIntent().getStringExtra("password");

        final QBUser qbUser = new QBUser(user, password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                qbUser.setId(qbSession.getUserId());

                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    Log.e("ChatDialogActivity", e.getMessage());
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ChatDialogActivity", e.getMessage());
                    }
                });


            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ChatDialogActivity", e.getMessage());
            }
        });

    }

}
