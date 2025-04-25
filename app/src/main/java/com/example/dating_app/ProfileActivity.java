package com.example.dating_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnAddImage;
    private ImageView imgProfile;
    Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnAddImage = findViewById(R.id.btnAddImage);
        imgProfile = findViewById(R.id.imgProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        // Sự kiện nhấn nút "Thêm ảnh"
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
                // Chuyển đổi URI thành Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap); // Hiển thị ảnh lên ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void saveProfile() {
        // Ở đây bạn có thể lưu các thông tin hồ sơ của người dùng (ví dụ: ảnh, tên, tuổi, v.v.)

        // Sau khi lưu xong, chuyển sang trang khác (ví dụ HomeActivity)
        Intent intent = new Intent(ProfileActivity.this, LocationActivity.class);
        startActivity(intent);

        // Thông báo thành công khi chuyển trang
        Toast.makeText(this, "Hồ sơ đã được lưu!", Toast.LENGTH_SHORT).show();
    }
}
