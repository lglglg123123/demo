package com.example.foodorderingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.activities.OrderDetailActivity;
import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public OrderItemAdapter(OrderDetailActivity context, List<CartItem> orderItems) {
    }

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderItemAdapter(Context context, List<Order> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String orderDate = sdf.format(new Date(order.getOrderDate()));

        holder.orderId.setText(String.format("订单号: %d", order.getOrderId()));
        holder.orderDate.setText(orderDate);
        holder.orderTotal.setText(String.format("总价: ¥%.2f", order.getTotalAmount()));
        holder.orderStatus.setText(order.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}