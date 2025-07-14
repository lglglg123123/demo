package com.example.foodorderingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.fragments.CartFragment;
import com.example.foodorderingsystem.fragments.FoodCategoryFragment;
import com.example.foodorderingsystem.fragments.HomeFragment;
import com.example.foodorderingsystem.fragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private int userId;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 先检查登录状态再加载布局
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            handleNotLoggedIn();
            return; // 确保后续代码不会执行
        }

        // 只有登录成功才加载主界面
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void handleNotLoggedIn() {
        // 1. 显示提示
        Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();

        // 2. 确保跳转前有Context
        if (!isFinishing()) {
            // 3. 使用applicationContext避免可能的Context问题
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    private void initUI() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        loadFragment(new HomeFragment(userId));
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment(userId);
                } else if (itemId == R.id.nav_categories) {
                    selectedFragment = new FoodCategoryFragment(userId);
                } else if (itemId == R.id.nav_cart) {
                    selectedFragment = new CartFragment(userId);
                } else if (itemId == R.id.nav_orders) {
                    selectedFragment = new OrdersFragment(userId);
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}