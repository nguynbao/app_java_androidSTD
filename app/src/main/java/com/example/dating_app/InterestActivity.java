package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    private Button submitButton;
    private ArrayList<ToggleButton> toggleButtonList = new ArrayList<>();

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        // Lấy userId từ Intent
        userId = getIntent().getLongExtra("userId", -1);

        // Khởi tạo ToggleButton
        for (int i = 1; i <= 12; i++) {
            int resID = getResources().getIdentifier("checkbox" + i, "id", getPackageName());
            ToggleButton tb = findViewById(resID);
            if (tb != null) {
                toggleButtonList.add(tb);
            }
        }

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
                String interests = String.join(", ", selectedInterests);

                Intent intent = new Intent(InterestActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}

