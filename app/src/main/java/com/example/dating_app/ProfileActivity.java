package com.example.dating_app;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnAddImage, btnSaveProfile;
    private ImageView imgProfile;
    private EditText editAge, editName, editHeight;
    private RadioGroup radioGender;
    private Uri selectedImageUri;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase setup
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initializing views
        btnAddImage = findViewById(R.id.btnAddImage);
        imgProfile = findViewById(R.id.imgProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        editAge = findViewById(R.id.editAge);
        editName = findViewById(R.id.editName);
        editHeight =findViewById(R.id.editHeight);
        radioGender = findViewById(R.id.radioGender);

        // Set listeners
        btnAddImage.setOnClickListener(v -> openGallery());
        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    // Open the gallery to pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imgProfile.setImageBitmap(bitmap); // Display image on ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Save the profile information
    private void saveProfile() {

        String name = editName.getText().toString();
        String ageString = editAge.getText().toString();
        String heightString = editHeight.getText().toString();

        // Validate inputs
        if (name.isEmpty() || ageString.isEmpty()|| heightString.isEmpty()) {
            Toast.makeText(this, "Please enter your name and age and height", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageString);
        int height = Integer.parseInt(heightString);

        // Validate age
        if (age < 19) {
            Toast.makeText(this, "You must be over 18 to use this app!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected gender
        int selectedGenderId = radioGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving profile...");
        progressDialog.show();

        // Upload image and save profile
        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child(user.getUid() + ".jpg");
            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveUserToDatabase(user.getUid(), name, age, height, gender, imageUrl);
                        progressDialog.dismiss();
                        goToLocationActivity();
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveUserToDatabase(user.getUid(), name, age, height, gender, null);
            progressDialog.dismiss();
            goToLocationActivity();
        }
    }

    // Save user data to Firebase database
    private void saveUserToDatabase(String uid, String name, int age, int height, String gender, String imageUrl) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("age", age);
        userMap.put("height", height);
        userMap.put("imageUrl", imageUrl != null ? imageUrl : "");

        databaseRef.child(uid).setValue(userMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show());
    }

    // Navigate to the next activity
    private void goToLocationActivity() {
        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, LocationActivity.class));
        finish();
    }
}
