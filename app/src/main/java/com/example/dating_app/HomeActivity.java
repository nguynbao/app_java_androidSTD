package com.example.dating_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dating_app.adapter.UserAdapter;
import com.example.dating_app.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo RecyclerView và danh sách người dùng
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Kết nối Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        // Lấy dữ liệu từ Firestore
        usersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String id = document.getString("id");
                    String email = document.getString("email");
                    int age = document.getLong("age").intValue();
                    String imageUrl = document.getString("imageUrl");
                    float distance = document.getDouble("distance").floatValue();

                    // Thêm người dùng vào danh sách
                    User user = new User(name, id, email, age, imageUrl, distance);
                    userList.add(user);
                }

                // Cập nhật RecyclerView với dữ liệu
                userAdapter = new UserAdapter(userList);
                recyclerView.setAdapter(userAdapter);
            } else {
                // Thông báo lỗi khi không lấy được dữ liệu
                Toast.makeText(HomeActivity.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
