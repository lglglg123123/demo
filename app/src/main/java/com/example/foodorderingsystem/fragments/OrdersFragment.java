package com.example.foodorderingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.activities.OrderDetailActivity;
import com.example.foodorderingsystem.adapters.OrderItemAdapter;
import com.example.foodorderingsystem.models.Order;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class OrdersFragment extends Fragment {
    private int userId;
    private RecyclerView ordersRecycler;
    private OrderItemAdapter orderAdapter;
    private TextView noOrdersText;
    private DBHelper dbHelper;

    public OrdersFragment(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecycler = view.findViewById(R.id.orders_recycler);
        noOrdersText = view.findViewById(R.id.no_orders_text);

        dbHelper = new DBHelper(getContext());
        loadOrders();

        return view;
    }

    private void loadOrders() {
        List<Order> orders = dbHelper.getUserOrders(userId);

        if (orders.isEmpty()) {
            noOrdersText.setVisibility(View.VISIBLE);
            ordersRecycler.setVisibility(View.GONE);
        } else {
            noOrdersText.setVisibility(View.GONE);
            ordersRecycler.setVisibility(View.VISIBLE);

            orderAdapter = new OrderItemAdapter(getContext(), orders, order -> {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("ORDER_ID", order.getOrderId());
                startActivity(intent);
            });

            ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            ordersRecycler.setAdapter(orderAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}