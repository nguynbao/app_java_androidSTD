// (package declaration giữ nguyên)
package com.example.dating_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAMERA = 2;
    private static final int REQUEST_IMAGE_FILE = 3;

    private Button btnAddImage, btnSaveProfile;
    private ImageView imgProfile;
    private EditText editAge, editName, editHeight;
    private RadioGroup radioGender;
    private Uri selectedImageUri, photoUri;

    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        btnAddImage = findViewById(R.id.btnAddImage);
        imgProfile = findViewById(R.id.imgProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        editAge = findViewById(R.id.editAge);
        editName = findViewById(R.id.editName);
        editHeight = findViewById(R.id.editHeight);
        radioGender = findViewById(R.id.radioGender);

        btnAddImage.setOnClickListener(v -> {
            String[] options = {"Chọn từ Thư viện", "Chụp ảnh", "Chọn từ Tệp"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn ảnh")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0: openGallery(); break;
                            case 1: openCamera(); break;
                            case 2: openFileManager(); break;
                        }
                    }).show();
        });

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_IMAGE_FILE);
    }

    private void openCamera() {
        try {
            File photoFile = File.createTempFile("profile_", ".jpg", getExternalCacheDir());
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAMERA);
        } catch (IOException e) {
            Toast.makeText(this, "Không thể tạo file ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == REQUEST_IMAGE_CAMERA && photoUri != null) {
                    selectedImageUri = photoUri;
                } else if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                }

                if (selectedImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    imgProfile.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Không thể lấy hình ảnh", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi hiển thị ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfile() {
        String name = editName.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String heightStr = editHeight.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int age, height;
        try {
            age = Integer.parseInt(ageStr);
            height = Integer.parseInt(heightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi và chiều cao phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (age < 19) {
            Toast.makeText(this, "Bạn phải trên 18 tuổi để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedGenderId = radioGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang lưu thông tin...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child(user.getUid() + ".jpg");
            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                saveUserToDatabase(user.getUid(), name, age, height, gender, imageUrl);
                                progressDialog.dismiss();
                                goToLocationActivity();
                            }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveUserToDatabase(user.getUid(), name, age, height, gender, "");
            progressDialog.dismiss();
            goToLocationActivity();
        }
    }

    private void saveUserToDatabase(String uid, String name, int age, int height, String gender, String imageUrl) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("age", age);
        userMap.put("height", height);
        userMap.put("gender", gender);
        userMap.put("imageUrl", imageUrl);

        databaseRef.child(uid).setValue(userMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show());
    }

    private void goToLocationActivity() {
        startActivity(new Intent(this, LocationActivity.class));
        finish();
    }
}
