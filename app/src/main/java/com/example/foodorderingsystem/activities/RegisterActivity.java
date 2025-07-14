package com.example.foodorderingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.models.User;
import com.example.foodorderingsystem.utils.DBHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);
        initViews();

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void handleRegistration() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (!validateInputs(username, password, email, phone)) {
            return;
        }

        try {
            User user = new User(0, username, password, email, phone);

            long result = dbHelper.addUser(user);
            handleRegistrationResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String username, String password,
                                   String email, String phone) {
        if (username.isEmpty() || password.isEmpty() ||
                email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 11 || !phone.matches("\\d+")) {
            Toast.makeText(this, "手机号应为11位数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleRegistrationResult(long result) {
        if (result != -1) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}