package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    private Button submitButton;
    private ArrayList<ToggleButton> toggleButtonList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private int selectedCount = 0;
    private final int MAX_SELECTION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        // Lấy userId từ Intent
       mAuth = FirebaseAuth.getInstance();
       currentUser = mAuth.getCurrentUser();
       if (currentUser == null) {
           Toast.makeText(this, "Vui lòng đăng nhập trước khi tiếp tục", Toast.LENGTH_SHORT).show();
           startActivity(new Intent(InterestActivity.this, LoginActivity.class));
           finish();
           return;
       }
        // Khởi tạo ToggleButton
        for (int i = 1; i <= 12; i++) {
            int resID = getResources().getIdentifier("checkbox" + i, "id", getPackageName());
            ToggleButton tb = findViewById(resID);
            if (tb != null) {
                toggleButtonList.add(tb);

                tb.setOnClickListener(v -> {
                    if (tb.isChecked()) {
                        if (selectedCount >= MAX_SELECTION) {
                            tb.setChecked(false); // Bỏ chọn lại
                            Toast.makeText(this, "Chỉ được chọn tối đa 3 sở thích", Toast.LENGTH_SHORT).show();
                        } else {
                            selectedCount++;
                        }
                    } else {
                        selectedCount--;
                    }
                });
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
                Toast.makeText(this, "Sở thích của bạn: " + interests, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(InterestActivity.this, ProfileActivity.class);
                intent.putExtra("userId", currentUser.getUid());
                intent.putExtra("interests", interests);
                startActivity(intent);
            }
        });
    }
}

