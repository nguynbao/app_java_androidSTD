package com.example.dating_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnAddImage, btnSaveProfile;
    private ImageView imgProfile;
    private EditText editAge;
    private EditText editName;

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Lấy userId từ Intent
        userId = getIntent().getLongExtra("userId", -1);

        btnAddImage = findViewById(R.id.btnAddImage);
        imgProfile = findViewById(R.id.imgProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        editAge = findViewById(R.id.editAge);
        editName = findViewById(R.id.editName);


        btnAddImage.setOnClickListener(v -> openGallery());

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    // Mở cửa sổ chọn ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả khi người dùng chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap); // Hiển thị ảnh lên ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lưu thông tin hồ sơ
    public void saveProfile() {

        String ageString = editAge.getText().toString();

        int age = Integer.parseInt(ageString);

        if (age < 19) {
            Toast.makeText(this, "Bạn phải trên 18 tuổi để sử dụng ứng dụng!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Chuyển sang Activity tiếp theo (LocationActivity)
        Intent intent = new Intent(ProfileActivity.this, LocationActivity.class);

        startActivity(intent);
    }
}

