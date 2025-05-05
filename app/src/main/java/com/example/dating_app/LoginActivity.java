package com.example.dating_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText EmailEditText, passwordEditText;
    Button loginButton;
    Button registerButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();



        loginButton.setOnClickListener(v -> {
            String email = EmailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
           if(email.isEmpty()||password.isEmpty()){
               Toast.makeText(LoginActivity.this,"Vui long nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
               return;
           }
           mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   Toast.makeText(LoginActivity.this, "Đăng nhập thành công",Toast.LENGTH_LONG).show();
                   startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                   finish();
               }
               else{
                   Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",Toast.LENGTH_LONG).show();
               }
           });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


}
