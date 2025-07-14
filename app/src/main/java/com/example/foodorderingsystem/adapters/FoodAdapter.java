package com.example.foodorderingsystem.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.models.FoodItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    // 正确定义的点击接口（不再使用AdapterView的接口）
    public interface OnItemClickListener {
        void onItemClick(FoodItem food);
    }

    private Context context;
    private List<FoodItem> foodList;
    private int userId;
    private DBHelper dbHelper;
    private OnItemClickListener itemClickListener;

    public FoodAdapter(Context context, List<FoodItem> foodList, int userId) {
        this.context = context;
        this.foodList = foodList;
        this.userId = userId;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
//        FoodItem food = foodList.get(position);
//
//        holder.foodName.setText(food.getName());
//        holder.foodDescription.setText(food.getDescription());
//        holder.foodPrice.setText(String.format("¥%.2f", food.getPrice()));
//
//        // 设置图片
//        int imageResId = context.getResources().getIdentifier(
//                food.getImage(), "drawable", context.getPackageName());
//        holder.foodImage.setImageResource(imageResId);
//
//        // 添加购物车按钮点击
//        holder.btnAddToCart.setOnClickListener(v -> {
//            dbHelper.addToCart(userId, food.getFoodId(), 1);
//            Toast.makeText(context, food.getName() + " 已添加到购物车", Toast.LENGTH_SHORT).show();
//        });
//
//        // 整个item的点击事件
//        holder.itemView.setOnClickListener(v -> {
//            if (itemClickListener != null) {
//                itemClickListener.onItemClick(food);
//            }
//        });
//    }
@Override
public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
    try {
        FoodItem food = foodList.get(position);

        // 文本数据绑定
        holder.foodName.setText(food.getName());
        holder.foodDescription.setText(food.getDescription());
        holder.foodPrice.setText(String.format("¥%.2f", food.getPrice()));

        // 图片加载（使用正确的getImageName()方法）
        int imageResId = food.getImageResourceId(context); // 使用FoodItem提供的便捷方法
        holder.foodImage.setImageResource(imageResId);

        holder.foodImage.setImageResource(imageResId);

        // 购物车按钮点击（带防抖处理）
        holder.btnAddToCart.setOnClickListener(new DebouncedOnClickListener() {
            @Override
            public void onDebouncedClick(View v) {
                try {
                    dbHelper.addToCart(userId, food.getFoodId(), 1);
                    Toast.makeText(context,
                            food.getName() + " 已添加到购物车",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("FoodAdapter", "添加购物车失败: " + e.getMessage());
                    Toast.makeText(context,
                            "添加失败，请重试",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 整个Item点击（带空值检查）
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null && food != null) {
                itemClickListener.onItemClick(food);
            }
        });

    } catch (Exception e) {
        Log.e("FoodAdapter", "数据绑定异常: " + e.getMessage());
    }
}

    // 防抖点击监听器（防止快速重复点击）
    public abstract class DebouncedOnClickListener implements View.OnClickListener {
        private static final long MIN_CLICK_INTERVAL = 600; // 毫秒
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                lastClickTime = currentTime;
                onDebouncedClick(v);
            }
        }

        public abstract void onDebouncedClick(View v);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // 正确的设置监听器方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice;
        TextView btnAddToCart;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}