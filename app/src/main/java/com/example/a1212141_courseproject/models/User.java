package com.example.a1212141_courseproject.models;


public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String phone;
    private String profileImage;

    public User(String email, String firstName, String lastName,
                String password, String gender, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
    }

    public User(int id, String email, String firstName, String lastName,
                String gender, String phone, String profileImage) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {
        if (firstName != null && lastName != null && !firstName.isEmpty() && !lastName.isEmpty()) {
            return String.valueOf(firstName.charAt(0)) + lastName.charAt(0);
        }
        return "U";
    }

    public boolean isValidEmail() {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword() {
        if (password == null) return false;
        return password.length() >= 6 &&
                password.matches(".*[A-Za-z].*") &&
                password.matches(".*[0-9].*");
    }
}