package com.ardakazanci.androidprochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainActivity extends AppCompatActivity {


    static final String APP_ID = "78269";
    static final String AUTH_KEY = "DJrUx4zHj4rVAFx";
    static final String AUTH_SECRET = "tJM2N5Z-kE4eQ55";
    static final String ACCOUNT_KEY = "uyxwvxAxa6LWfKK9yyvp";

    private Button btnLogin, btnRegister;
    private EditText edtUserName, edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFramework();


        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtUserName = findViewById(R.id.edtLoginUserName);
        edtPassword = findViewById(R.id.edtLoginPassword);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


        /**
         * Login Operation
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user = edtUserName.getText().toString();
                final String password = edtPassword.getText().toString();

                QBUser qbUser = new QBUser(user, password);
                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ChatDialogsActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("password", password);
                        startActivity(intent);


                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private void initializeFramework() {
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }
}
