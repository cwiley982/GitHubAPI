package com.caitlynwiley.githubapi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.caitlynwiley.githubapi.R;

public class LogInActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText inputUsername;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        inputUsername = findViewById(R.id.input_username);
    }

    public void getUser(View view) {
        i = new Intent(LogInActivity.this, UserActivity.class);
        i.putExtra("STRING_I_NEED", inputUsername.getText().toString());
        startActivity(i);
    }
}
