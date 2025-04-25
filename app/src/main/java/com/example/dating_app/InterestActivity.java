package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    private Button submitButton;
    private ArrayList<ToggleButton> toggleButtonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        // Lấy thông tin người dùng từ RegisterActivity
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        // Khởi tạo ToggleButton
        for (int i = 1; i <= 12; i++) {
            int resID = getResources().getIdentifier("checkbox" + i, "id", getPackageName());
            ToggleButton tb = findViewById(resID);
            if (tb != null) {
                toggleButtonList.add(tb);
            }
        }

        // Nút xác nhận
        submitButton = findViewById(R.id.submitInterestButton);
        submitButton.setOnClickListener(v -> {
            ArrayList<String> selectedInterests = new ArrayList<>();
            for (ToggleButton toggleButton : toggleButtonList) {
                if (toggleButton.isChecked()) {
                    selectedInterests.add(toggleButton.getText().toString());
                }
            }

            if (selectedInterests.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sở thích", Toast.LENGTH_SHORT).show();
            } else {
                // Lưu vào database tại đây hoặc xử lý tiếp theo
                Toast.makeText(this, "Sở thích đã lưu cho " + username, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InterestActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
