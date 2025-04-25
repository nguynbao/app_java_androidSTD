package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText;
    private Button interestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Đảm bảo file XML của bạn tên là activity_register.xml

        // Ánh xạ view
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        interestButton = findViewById(R.id.interestButton);

        // Xử lý khi nhấn nút "Đăng kí"
        interestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                Intent  intent = new Intent(RegisterActivity.this, InterestActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);

            }


        });
    }
}

