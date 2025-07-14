package com.example.foodorderingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private int userId;
    private DBHelper dbHelper;
    private TextView totalAmount;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            finish();
            return;
        }

        dbHelper = new DBHelper(this);

        totalAmount = findViewById(R.id.total_amount);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        double total = dbHelper.getCartTotal(userId);
        totalAmount.setText(String.format("¥%.2f", total));

        btnPlaceOrder.setOnClickListener(v -> {
            long orderId = dbHelper.placeOrder(userId, total);
            if (orderId != -1) {
                List<CartItem> cartItems = dbHelper.getCartItems(userId);
                dbHelper.addOrderItems(orderId, cartItems);
                dbHelper.clearCart(userId);

                Toast.makeText(this, "订单提交成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "订单提交失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}