package com.ardakazanci.androidprochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ardakazanci.androidprochatapp.Adapter.ListUsersAdapter;
import com.ardakazanci.androidprochatapp.Common.Common;
import com.ardakazanci.androidprochatapp.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity {

    Button btnCreateChat;
    ListView lstUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        btnCreateChat = findViewById(R.id.btn_create_chat);
        lstUsers = findViewById(R.id.lst_users);
        lstUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        retrieveAllUsers();


        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int countChoice = lstUsers.getCount();

                if (lstUsers.getCheckedItemPositions().size() == 1) {
                    createPrivateChat(lstUsers.getCheckedItemPositions());
                } else if (lstUsers.getCheckedItemPositions().size() > 1) {
                    createGroupChat(lstUsers.getCheckedItemPositions());
                } else {
                    Toast.makeText(ListUsersActivity.this, "Please select friend to chat", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {

        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countChoice = lstUsers.getCount();

        ArrayList<Integer> occupantIdsList = new ArrayList<>();
        for (int i = 0; i < countChoice; i++) {

            if (checkedItemPositions.get(i)) {
                QBUser user = (QBUser) lstUsers.getItemAtPosition(i);
                occupantIdsList.add(user.getId());
            }

        }


        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mDialog.dismiss();
                Toast.makeText(ListUsersActivity.this, "Create chat dialog successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ListUsersActivity", e.getMessage());
            }
        });

    }

    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countChoice = lstUsers.getCount();

        for (int i = 0; i < countChoice; i++) {

            if (checkedItemPositions.get(i)) {
                QBUser user = (QBUser) lstUsers.getItemAtPosition(i);
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mDialog.dismiss();
                        Toast.makeText(ListUsersActivity.this, "Create private chat dialog successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ListUsersActivity", e.getMessage());
                    }
                });
            }

        }

    }

    private void retrieveAllUsers() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser> qbUserWithoutCurrent = new ArrayList<QBUser>();
                for (QBUser user : qbUsers) {

                    if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())) {
                        qbUserWithoutCurrent.add(user);
                    }


                }


                ListUsersAdapter adapter = new ListUsersAdapter(getBaseContext(), qbUsers);
                lstUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("ListUsersActivity", e.getMessage());

            }
        });

    }
}
