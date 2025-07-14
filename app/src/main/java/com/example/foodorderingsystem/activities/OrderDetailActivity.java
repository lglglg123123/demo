package com.example.foodorderingsystem.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.adapters.OrderItemAdapter;
import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.models.Order;
import com.example.foodorderingsystem.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    private Order order;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private OrderItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        long orderId = getIntent().getLongExtra("ORDER_ID", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        dbHelper = new DBHelper(this);
        order = dbHelper.getOrderById(orderId);

        if (order == null) {
            finish();
            return;
        }

        initViews();
        loadOrderItems();
    }

    private void initViews() {
        TextView orderIdText = findViewById(R.id.order_id);
        TextView orderDateText = findViewById(R.id.order_date);
        TextView orderTotalText = findViewById(R.id.order_total);
        TextView orderStatusText = findViewById(R.id.order_status);
        recyclerView = findViewById(R.id.order_items_recycler);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String orderDate = sdf.format(new Date(order.getOrderDate()));

        orderIdText.setText(String.format("订单号: %d", order.getOrderId()));
        orderDateText.setText(orderDate);
        orderTotalText.setText(String.format("总价: ¥%.2f", order.getTotalAmount()));
        orderStatusText.setText(order.getStatus());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderItems() {
        List<CartItem> orderItems = dbHelper.getOrderItems(order.getOrderId());
        adapter = new OrderItemAdapter(this, orderItems);
        recyclerView.setAdapter(adapter);
    }
}