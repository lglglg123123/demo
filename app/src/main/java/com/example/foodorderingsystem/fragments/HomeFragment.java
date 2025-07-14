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
import com.example.foodorderingsystem.activities.FoodDetailActivity;
import com.example.foodorderingsystem.adapters.FoodAdapter;
import com.example.foodorderingsystem.models.FoodItem;
import com.example.foodorderingsystem.utils.DBHelper;

import java.util.List;

public class HomeFragment extends Fragment {
    private int userId;
    private RecyclerView popularFoodsRecycler;
    private FoodAdapter foodAdapter;

    public HomeFragment(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView seeAll = view.findViewById(R.id.see_all);
        popularFoodsRecycler = view.findViewById(R.id.popular_foods_recycler);

        seeAll.setOnClickListener(v -> {
            FoodCategoryFragment categoryFragment = new FoodCategoryFragment(userId);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, categoryFragment)
                    .addToBackStack(null)
                    .commit();
        });

        setupPopularFoods();

        return view;
    }

    private void setupPopularFoods() {
        DBHelper dbHelper = new DBHelper(getContext());
        List<FoodItem> popularFoods = dbHelper.getAllFoods(); // 实际项目中应该根据销量或其他标准获取

        foodAdapter = new FoodAdapter(getContext(), popularFoods, userId);
        foodAdapter.setOnItemClickListener(food -> {
            Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("FOOD_ID", food.getFoodId());
            startActivity(intent);
        });

        popularFoodsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularFoodsRecycler.setAdapter(foodAdapter);
    }
}