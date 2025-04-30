package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.dating_app.adapter.UserAdapter;
import com.example.dating_app.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        List<User> users = new ArrayList<>();

        // Tạo user chỉ với ảnh, tên, vị trí
        User user1 = new User();
        user1.setName("Nguyễn Văn A");
        user1.setLocation("Hà Nội");
        user1.setImagePath("profile_image_1");

        User user2 = new User();
        user2.setName("Trần Thị B");
        user2.setLocation("TP. Hồ Chí Minh");
        user2.setImagePath("profile_image_2");

        User user3 = new User();
        user3.setName("Lê Văn C");
        user3.setLocation("Đà Nẵng");
        user3.setImagePath("unknown_image");

        users.add(user1);
        users.add(user2);
        users.add(user3);

        UserAdapter adapter = new UserAdapter(users);
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show();

                return true;
            } else if (id == R.id.nav_message) {
                Toast.makeText(this, "Tin nhắn", Toast.LENGTH_SHORT).show();

                return true;
            }else if (id == R.id.nav_users) {
                Toast.makeText(this, "Mọi người", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, UsersActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Cá nhân", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
