package com.example.dating_app;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dating_app.adapter.UserAdapter;
import com.example.dating_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private static final String TAG = "HomeActivity"; // Thêm Tag cho Log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo RecyclerView và danh sách người dùng
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Lấy thông tin tài khoản đang đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(HomeActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUserId = currentUser.getUid();  // ID của người dùng hiện tại
        Log.d(TAG, "Current user ID: " + currentUserId); // In ra ID người dùng đăng nhập

        // Kết nối Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();  // Xóa danh sách cũ trước khi thêm dữ liệu mới

                // Kiểm tra xem có dữ liệu không
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "No users found in the database.");
                    return;
                }

                // Giới hạn chỉ lấy một người dùng (có thể là tài khoản ngẫu nhiên hoặc theo ID)
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();  // Lấy ID của người dùng
                    Log.d(TAG, "User ID: " + userId); // In ra ID của người dùng

                    // Kiểm tra nếu tài khoản này là tài khoản đang đăng nhập thì bỏ qua
                    if (userId.equals(currentUserId)) {
                        Log.d(TAG, "Skipping current user ID.");
                        continue; // Nếu tài khoản là người dùng đang đăng nhập, bỏ qua
                    }

                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);  // Nếu có trường email thì lấy ra
                    int age = snapshot.child("age").getValue(Integer.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    float distance = 0f; // Nếu có trường distance thì lấy ra

                    // Tạo đối tượng User và thêm vào danh sách
                    User user = new User(name, email, age, imageUrl, distance);
                    userList.add(user);
                    Log.d(TAG, "Added user: " + name); // In ra tên người dùng vừa thêm
                    break;  // Chỉ lấy một người dùng, không cần lặp tiếp
                }

                // Cập nhật RecyclerView với người dùng duy nhất
                if (userList.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "No users found to display.", Toast.LENGTH_SHORT).show();
                } else {
                    userAdapter = new UserAdapter(userList);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error getting users.", databaseError.toException());
                Toast.makeText(HomeActivity.this, "Error getting users.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
