package com.example.dating_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.*;

public class ImgurUploader {
    private static final String IMGUR_CLIENT_ID = "a0d5fa3b929e9b3"; // 🔁 Nhớ thay bằng Client ID của bạn
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";

    public interface OnUploadCompleteListener {
        void onSuccess(String imageUrl);
        void onFailure(String errorMessage);
    }

    public static void uploadImage(Uri imageUri, Context context, OnUploadCompleteListener listener) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            uploadBitmap(bitmap, listener);
        } catch (IOException e) {
            runOnUiThread(() -> listener.onFailure("Không thể đọc ảnh: " + e.getMessage()));
        }
    }

    private static void uploadBitmap(Bitmap bitmap, OnUploadCompleteListener listener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("image", base64Image)
                .build();

        Request request = new Request.Builder()
                .url(IMGUR_UPLOAD_URL)
                .addHeader("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> listener.onFailure("Lỗi kết nối: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    String imageUrl = extractImageUrlFromResponse(json);
                    if (imageUrl != null) {
                        runOnUiThread(() -> listener.onSuccess(imageUrl));
                    } else {
                        runOnUiThread(() -> listener.onFailure("Không tìm thấy đường dẫn ảnh từ phản hồi."));
                    }
                } else {
                    runOnUiThread(() -> listener.onFailure("Upload thất bại: " + response.message()));
                }
            }
        });
    }

    private static String extractImageUrlFromResponse(String json) {
        int linkIndex = json.indexOf("\"link\":\"");
        if (linkIndex != -1) {
            int start = linkIndex + 8;
            int end = json.indexOf("\"", start);
            return json.substring(start, end).replace("\\/", "/");
        }
        return null;
    }

    private static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
