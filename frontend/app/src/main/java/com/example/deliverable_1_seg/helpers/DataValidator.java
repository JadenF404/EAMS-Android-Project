package com.example.deliverable_1_seg.helpers;

public class DataValidator {

    public static boolean signUpPassword(String password){
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()-+=<>?].*");

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}
