package com.ardakazanci.androidprochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUpActivity extends AppCompatActivity {


    Button btnSignUp, btnCancel;
    EditText edtRegisterUserName, edtRegisterPassword, edtRegisterFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerSession();

        btnSignUp = findViewById(R.id.btnSignUp);
        btnCancel = findViewById(R.id.btnCancel);

        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterUserName = findViewById(R.id.edtRegisterUserName);
        edtRegisterFullName = findViewById(R.id.edtRegisterFullName);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });

        // Register Operation
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = edtRegisterUserName.getText().toString();
                String password = edtRegisterPassword.getText().toString();

                QBUser qbUser = new QBUser(user, password);

                qbUser.setFullName(edtRegisterFullName.getText().toString());

                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {

                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {

                        Toast.makeText(SignUpActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(SignUpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private void registerSession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                Log.i("SignUpActivity", "Auth Success");

            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("SignUpActivity", e.getMessage());

            }
        });
    }
}
