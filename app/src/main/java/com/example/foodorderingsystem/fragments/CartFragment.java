package com.example.foodorderingsystem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.activities.CheckoutActivity;
import com.example.foodorderingsystem.adapters.CartAdapter;
import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class CartFragment extends Fragment {
    private int userId;
    private RecyclerView cartRecycler;
    private CartAdapter cartAdapter;
    private TextView cartTotal;
    private Button btnCheckout;
    private DBHelper dbHelper;

    public CartFragment(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartTotal = view.findViewById(R.id.cart_total);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        cartRecycler = view.findViewById(R.id.cart_recycler);

        dbHelper = new DBHelper(getContext());

        btnCheckout.setOnClickListener(v -> {
            if (dbHelper.getCartTotal(userId) > 0) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        loadCartItems();

        return view;
    }

    private void loadCartItems() {
        List<CartItem> cartItems = dbHelper.getCartItems(userId);
        cartAdapter = new CartAdapter(getContext(), cartItems, userId, () -> {
            updateCartTotal();
        });

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecycler.setAdapter(cartAdapter);

        updateCartTotal();
    }

    private void updateCartTotal() {
        double total = dbHelper.getCartTotal(userId);
        cartTotal.setText(String.format("总计: ¥%.2f", total));
        btnCheckout.setEnabled(total > 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartItems();
    }
}