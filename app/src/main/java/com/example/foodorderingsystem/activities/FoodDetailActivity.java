//package com.example.foodorderingsystem.activities;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.foodorderingsystem.R;
//import com.example.foodorderingsystem.models.FoodItem;
//import com.example.foodorderingsystem.utils.DBHelper;
//
//public class FoodDetailActivity extends AppCompatActivity {
//    private int userId;
//    private FoodItem foodItem;
//    private DBHelper dbHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_food_detail);
//
//        userId = getIntent().getIntExtra("USER_ID", -1);
//        int foodId = getIntent().getIntExtra("FOOD_ID", -1);
//
//        if (userId == -1 || foodId == -1) {
//            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        dbHelper = new DBHelper(this);
//        foodItem = dbHelper.getFoodById(foodId);
//
//        if (foodItem == null) {
//            Toast.makeText(this, "食品信息获取失败", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        initViews();
//    }
//
//    private void initViews() {
//        ImageView foodImage = findViewById(R.id.food_image);
//        TextView foodName = findViewById(R.id.food_name);
//        TextView foodDescription = findViewById(R.id.food_description);
//        TextView foodPrice = findViewById(R.id.food_price);
//        TextView btnAddToCart = findViewById(R.id.btn_add_to_cart);
//
//        foodName.setText(foodItem.getName());
//        foodDescription.setText(foodItem.getDescription());
//        foodPrice.setText(String.format("¥%.2f", foodItem.getPrice()));
//
//        // 设置图片
//        int imageResId = getResources().getIdentifier(
//                foodItem.getImage(), "drawable", getPackageName());
//        foodImage.setImageResource(imageResId);
//
//        btnAddToCart.setOnClickListener(v -> {
//            dbHelper.addToCart(userId, foodItem.getFoodId(), 1);
//            Toast.makeText(this, foodItem.getName() + " 已添加到购物车", Toast.LENGTH_SHORT).show();
//        });
//    }
//}
package com.example.foodorderingsystem.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.models.FoodItem;
import com.example.foodorderingsystem.utils.DBHelper;

public class FoodDetailActivity extends AppCompatActivity {
    private int userId;
    private FoodItem foodItem;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        userId = getIntent().getIntExtra("USER_ID", -1);
        int foodId = getIntent().getIntExtra("FOOD_ID", -1);

        if (userId == -1 || foodId == -1) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DBHelper(this);
        foodItem = dbHelper.getFoodById(foodId);

        if (foodItem == null) {
            Toast.makeText(this, "食品信息获取失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        ImageView foodImage = findViewById(R.id.food_image);
        TextView foodName = findViewById(R.id.food_name);
        TextView foodDescription = findViewById(R.id.food_description);
        TextView foodPrice = findViewById(R.id.food_price);
        TextView btnAddToCart = findViewById(R.id.btn_add_to_cart);

        foodName.setText(foodItem.getName());
        foodDescription.setText(foodItem.getDescription());
        foodPrice.setText(String.format("¥%.2f", foodItem.getPrice()));

        // 修改后的图片加载方式（两种方案任选其一）

        // 方案1：使用FoodItem提供的便捷方法（推荐）
        int imageResId = foodItem.getImageResourceId(this);
        foodImage.setImageResource(imageResId);

        // 方案2：直接使用getImageName()
        // int imageResId = getResources().getIdentifier(
        //         foodItem.getImageName(), // 使用getImageName()而不是getImage()
        //         "drawable",
        //         getPackageName());
        // foodImage.setImageResource(imageResId != 0 ? imageResId : R.drawable.food_placeholder);

        btnAddToCart.setOnClickListener(v -> {
            try {
                dbHelper.addToCart(userId, foodItem.getFoodId(), 1);
                Toast.makeText(this,
                        foodItem.getName() + " 已添加到购物车",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this,
                        "添加失败: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}