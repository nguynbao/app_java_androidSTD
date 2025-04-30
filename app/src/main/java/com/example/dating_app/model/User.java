package com.example.dating_app.model;

import com.example.dating_app.R;

public class User {
    private String name;
    private String password;
    private String email;
    private String bio;
    private String location;
    private int age;
    private String imagePath; // Đường dẫn đến ảnh hoặc tên ảnh tài nguyên
    private String interest;
    private String gender;

    // Constructor
    public User(int age, String name, String password, String email, String bio,
                String location, String imagePath, String interest, String gender) {
        this.age = age;
        this.name = name;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.imagePath = imagePath;
        this.interest = interest;
        this.gender = gender;
    }

    public User() {
        // Giá trị mặc định
        this.age = 0;
        this.name = "";
        this.password = "";
        this.email = "";
        this.bio = "";
        this.location = "";
        this.imagePath = "";
        this.interest = "";
        this.gender = "";
    }

    // Getter và Setter cho các thuộc tính
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getInterest() { return interest; }
    public void setInterest(String interest) { this.interest = interest; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // Phương thức để lấy ID tài nguyên hình ảnh
    public int getImageResId() {
        // Giả sử imagePath chứa tên hình ảnh, chúng ta sẽ trả về ID tài nguyên dựa vào tên này
        switch (imagePath) {
            case "profile_image_1":
                return R.drawable.user1; // Ví dụ, nếu tên là profile_image_1 thì trả về ID tài nguyên
            case "profile_image_2":
                return R.drawable.user2; // Tương tự với các hình ảnh khác
            default:
                return R.drawable.user3; // Nếu không tìm thấy hình ảnh, trả về hình mặc định
        }
    }
}
