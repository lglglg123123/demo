package com.example.foodorderingsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private int userId;
    private DBHelper dbHelper;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, int userId, OnCartChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.userId = userId;
        this.dbHelper = new DBHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.itemName.setText(item.getFoodName());
        holder.itemPrice.setText(String.format("¥%.2f", item.getPrice()));
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemTotal.setText(String.format("¥%.2f", item.getTotalPrice()));

        // 设置图片
        int imageResId = context.getResources().getIdentifier(
                item.getImage(), "drawable", context.getPackageName());
        holder.itemImage.setImageResource(imageResId);

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            dbHelper.updateCartItemQuantity(userId, item.getFoodId(), newQuantity);
            item.setQuantity(newQuantity);
            holder.itemQuantity.setText(String.valueOf(newQuantity));
            holder.itemTotal.setText(String.format("¥%.2f", item.getTotalPrice()));
            if (listener != null) {
                listener.onCartChanged();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                dbHelper.updateCartItemQuantity(userId, item.getFoodId(), newQuantity);
                item.setQuantity(newQuantity);
                holder.itemQuantity.setText(String.valueOf(newQuantity));
                holder.itemTotal.setText(String.format("¥%.2f", item.getTotalPrice()));
            } else {
                dbHelper.removeFromCart(userId, item.getFoodId());
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
                Toast.makeText(context, item.getFoodName() + " 已从购物车移除", Toast.LENGTH_SHORT).show();
            }
            if (listener != null) {
                listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity, itemTotal;
        TextView btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.food_image);
            itemName = itemView.findViewById(R.id.food_name);
            itemPrice = itemView.findViewById(R.id.food_price);
            itemQuantity = itemView.findViewById(R.id.tv_quantity);
            itemTotal = itemView.findViewById(R.id.item_total);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}