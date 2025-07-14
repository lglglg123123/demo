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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.example.foodorderingsystem.activities.FoodDetailActivity;
import com.example.foodorderingsystem.adapters.FoodAdapter;
import com.example.foodorderingsystem.models.FoodItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class FoodCategoryFragment extends Fragment {
    private int userId;
    private RecyclerView foodsRecycler;
    private FoodAdapter foodAdapter;

    public FoodCategoryFragment(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_category, container, false);

        TextView allCategory = view.findViewById(R.id.all_category);
        TextView fastFoodCategory = view.findViewById(R.id.fast_food_category);
        TextView mainCourseCategory = view.findViewById(R.id.main_course_category);
        TextView healthyCategory = view.findViewById(R.id.healthy_category);
        TextView japaneseCategory = view.findViewById(R.id.japanese_category);

        foodsRecycler = view.findViewById(R.id.foods_recycler);

        allCategory.setOnClickListener(v -> loadFoodsByCategory(null));
        fastFoodCategory.setOnClickListener(v -> loadFoodsByCategory("快餐"));
        mainCourseCategory.setOnClickListener(v -> loadFoodsByCategory("主食"));
        healthyCategory.setOnClickListener(v -> loadFoodsByCategory("健康餐"));
        japaneseCategory.setOnClickListener(v -> loadFoodsByCategory("日式料理"));

        // 默认加载所有食品
        loadFoodsByCategory(null);

        return view;
    }

    private void loadFoodsByCategory(String category) {
        DBHelper dbHelper = new DBHelper(getContext());
        List<FoodItem> foods = (category == null || category.isEmpty())
                ? dbHelper.getAllFoods()
                : dbHelper.getFoodsByCategory(category);

        // 初始化Adapter
        foodAdapter = new FoodAdapter(getContext(), foods, userId);

        // 设置点击监听器（兼容Java 7/8的写法）
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodItem food) {
                Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("FOOD_ID", food.getFoodId());
                startActivity(intent);
            }
        });

        // 设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        foodsRecycler.setLayoutManager(layoutManager);
        foodsRecycler.setAdapter(foodAdapter);
    }
}